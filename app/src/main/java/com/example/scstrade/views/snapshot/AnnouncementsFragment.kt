package com.example.scstrade.views.snapshot

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.icu.util.Calendar
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.ParcelFileDescriptor
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.get
import androidx.lifecycle.Observer
import coil.compose.AsyncImage
import com.bumptech.glide.Glide
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentAnnouncementsBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.helper.downloadPdf
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.announcement.AnnouncementDataItem
import com.example.scstrade.model.response.announcement.AnnouncementItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.widgets.ZoomImageView
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AnnouncementsFragment : Fragment() {
    lateinit var binding:FragmentAnnouncementsBinding
    lateinit var sharedViewModel: SharedViewModel
    lateinit var symbol:String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAnnouncementsBinding.inflate(inflater,container,false)
        sharedViewModel= (requireActivity().application as MyApp).viewModel
        symbol = requireActivity().intent?.extras?.getString(AppConstants.SYMBOL) ?: ""
        sharedViewModel.announcement(symbol)
        sharedViewModel.insider(symbol)
        binding.loader.visibility = View.GONE
        binding.main.visibility=View.VISIBLE

        binding.relativeLayoutDate.setOnClickListener {
           showDatePicker(binding.textDate)
        }
        sharedViewModel.mutableAnnouncement.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_dropdown_item_1line)
                    adapter.add("All Announcements")
                    adapter.addAll(result.data?.distinct()?.filter { !it.announcementType.isNullOrEmpty() }?.map { it.announcementType }?.toMutableList()?: emptyList())
                    if(!sharedViewModel.mutableInsider.value?.data.isNullOrEmpty()){
                        adapter.add("Insider")
                    }
                    binding.spinnerAnnouncement.adapter=adapter
                }
            }
        })

        binding.spinnerAnnouncement.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
               binding.main.setContent {
                   if(p0?.getItemAtPosition(p2).toString().equals("insider",true)) {
                       AnnouncementItems(sharedViewModel.mutableInsider.value?.data?.filter {
                           (Utils.convertDateString(it.insiderTransactionPostDate ?: "", "dd/MM/yyyy")
                                       .equals(binding.textDate.text))
                       }?.map { AnnouncementItem(it.insiderTransactionDesc,it.insiderTransactionPostDate,it.insiderTransactionPostDate,it.insiderTransactionImageLink,it.insiderTransactionPDFLink) }?.toList())
                   }else if (p0?.getItemAtPosition(p2).toString().equals("all announcements",true)){
                       val list = mutableListOf<AnnouncementItem>()
                       sharedViewModel.mutableAnnouncement.value?.data?.forEach {
                           list.add(AnnouncementItem(it.bmDesc,it.bmDate,it.bmTime,it.bmImageLink,it.bmPDFLink))
                           sharedViewModel.mutableInsider.value?.data?.forEach { it2->
                               if(it.bmDate.equals(it2.insiderTransactionPostDate)){
                                   list.add(AnnouncementItem(it2.insiderTransactionDesc,it2.insiderTransactionPostDate,it2.insiderTransactionPostDate,it2.insiderTransactionImageLink,it2.insiderTransactionPDFLink))
                               }
                           }
                       }
                       AnnouncementItems(list = list.filter { Utils.convertDateString(it.date?:"","dd/MM/yyyy").equals(binding.textDate.text) })
                   }else{
                       AnnouncementItems(sharedViewModel.mutableAnnouncement.value?.data?.filter {
                           (it.announcementType?.equals(
                               p0?.getItemAtPosition(p2).toString(),
                               true
                           ) ?: false &&
                                   Utils.convertDateString(it.bmDate ?: "", "dd/MM/yyyy")
                                       .equals(binding.textDate.text))
                       }?.map{AnnouncementItem(it.bmDesc,it.bmDate,it.bmTime,it.bmImageLink,it.bmPDFLink)}?.toList())
                   }
               }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        return binding.root
    }



    private fun showDatePicker(textView: TextView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val sdf=SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//                    val cal=Calendar.getInstance()
                    calendar.set(selectedYear,selectedMonth,selectedDay)
                    textView.text = sdf.format(calendar.time)
                    binding.main.setContent {
                        if(binding.spinnerAnnouncement.selectedItem.toString().equals("insider",true)){
                            AnnouncementItems(list = sharedViewModel.mutableInsider.value?.data?.filter { Utils.convertDateString(it.insiderTransactionPostDate?:"","dd/MM/yyyy").equals(textView.text) }
                                ?.map {
                                    AnnouncementItem(it.insiderTransactionDesc,it.insiderTransactionPostDate,it.insiderTransactionPostDate,it.insiderTransactionImageLink,it.insiderTransactionPDFLink)
                                }?.toList())
                        }else if(binding.spinnerAnnouncement.selectedItem.toString().equals("all announcements",true)){
                            val list = mutableListOf<AnnouncementItem>()
                            sharedViewModel.mutableAnnouncement.value?.data?.forEach {
                                list.add(AnnouncementItem(it.bmDesc,it.bmDate,it.bmTime,it.bmImageLink,it.bmPDFLink))
                                sharedViewModel.mutableInsider.value?.data?.forEach { it2->
                                    if(it.bmDate.equals(it2.insiderTransactionPostDate)){
                                        list.add(AnnouncementItem(it2.insiderTransactionDesc,it2.insiderTransactionPostDate,it2.insiderTransactionPostDate,it2.insiderTransactionImageLink,it2.insiderTransactionPDFLink))
                                    }
                                }
                            }
                            AnnouncementItems(list = list.filter { Utils.convertDateString(it.date?:"","dd/MM/yyyy").equals(binding.textDate.text) })
                        } else{

                            AnnouncementItems(list = sharedViewModel.mutableAnnouncement.value?.data?.filter { (it.announcementType?.equals(binding.spinnerAnnouncement.selectedItem.toString())?:false && Utils.convertDateString(it.bmDate?:"","dd/MM/yyyy").equals(textView.text.toString()))}?.map { AnnouncementItem(it.bmDesc,it.bmDate,it.bmTime, imageLink = it.bmImageLink, pdfLink = it.bmPDFLink) }?.toList())
                        }

                    }
                },
                year, month, day
            )
            datePickerDialog.show()
        } else {
            val date = Date()

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    textView.text = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                },
                date.year, date.month, date.day
            )
            datePickerDialog.show()
        }

    }
    @Composable
    private fun AnnouncementItems(list: List<AnnouncementItem>?) {
        LazyColumn(modifier = Modifier.padding(horizontal = 15.dp)) {
            items(list?.size?:0) { index ->
                Column {
                    Row {
                        Box (modifier = Modifier.weight(0.7f)){
                            Text(
                                text = list?.get(index)?.desc?:"",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 19.sp,
                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                    fontWeight = FontWeight(500),
                                    color = colorResource(R.color.md_theme_primary),
                                )
                            )
                        }
                        Box(modifier = Modifier.weight(0.3f)){
                            Row{
                                Box(modifier = Modifier
                                    .size(32.dp)
                                    .border(
                                        width = 1.dp, color = Color(0xFF79776F),
                                        RoundedCornerShape(21.dp)
                                    )) {
                                    Image(painter = painterResource(id = R.drawable.baseline_remove_red_eye_24), contentDescription = "View", modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(7.dp)
                                        .clickable {
                                           val dialog= Dialog(requireContext())
                                            dialog.setContentView(R.layout.dialog_image)
                                            Glide.with(requireContext()).load(list?.get(index)?.imageLink).into(dialog.findViewById<ZoomImageView>(R.id.imageView))
                                            dialog.setCancelable(false)
                                            dialog.setCanceledOnTouchOutside(false)
//                                            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                                            dialog.findViewById<ImageView>(R.id.btnClose).setOnClickListener {
                                                dialog.dismiss()
                                            }
                                            dialog.show()
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(modifier = Modifier
                                    .size(32.dp)
                                    .border(
                                        width = 1.dp, color = Color(0xFF79776F),
                                        RoundedCornerShape(21.dp)
                                    )) {
                                    Image(painter = painterResource(id = R.drawable.baseline_arrow_downward_24), contentDescription = "Download", modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(7.dp)
                                        .clickable {
                                            val dialog= Dialog(requireContext())
                                            dialog.setContentView(R.layout.dialog_image)
                                            dialog.setCancelable(false)
                                            dialog.setCanceledOnTouchOutside(false)
//                                            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                                            dialog.findViewById<ImageView>(R.id.btnClose).setOnClickListener {
                                                dialog.dismiss()
                                            }
                                            dialog.show()
                                            downloadPdf(requireContext(),list?.get(index)?.pdfLink?:""){file ->
                                                requireActivity().runOnUiThread {
                                                    file?.let { renderPdfPage(it, dialog.findViewById<ZoomImageView>(R.id.imageView)) }
                                                }

                                            }
                                        })
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(modifier = Modifier
                                    .size(32.dp)
                                    .border(
                                        width = 1.dp, color = Color(0xFF79776F),
                                        RoundedCornerShape(21.dp)
                                    )) {
                                    Image(painter = painterResource(id = R.drawable.baseline_share_24), contentDescription = "Share", colorFilter = ColorFilter.tint(color = colorResource(
                                        id = R.color.md_theme_primary
                                    )), modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(7.dp)
                                        .clickable {
                                            downloadPdf(requireContext(),list?.get(index)?.pdfLink?:""){file ->
                                                file?.let { sharePdf(it) }

                                            }
                                        })
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Row{
                        Text(
                            text = "${Utils.convertDateString(list?.get(index)?.date?:"","dd MMM yyyy")} | ${list?.get(index)?.time?:""}",
                            style = TextStyle(
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                fontWeight = FontWeight(500),
                                color = Color(0xFF1C1B1B),
                                textAlign = TextAlign.Center,
                                letterSpacing = 0.1.sp,
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row{
                        Divider(
                            color = Color(0xFFE5E2E1),
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
    private fun renderPdfPage(pdfFile: File, imageView: ImageView) {
        val fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
        val pdfRenderer = PdfRenderer(fileDescriptor)
        val page = pdfRenderer.openPage(0) // Open first page

        val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

        imageView.setImageBitmap(bitmap)

        page.close()
        pdfRenderer.close()
    }
    private fun sharePdf(file: File) {
        if (!file.exists()) return

        val uri: Uri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", file)

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Allow access
        }

        startActivity(Intent.createChooser(shareIntent, "Share PDF via"))
    }

}