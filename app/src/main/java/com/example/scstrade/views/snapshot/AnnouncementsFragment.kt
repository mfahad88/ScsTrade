package com.example.scstrade.views.snapshot

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.example.mycalendar_sdk.CustomDatePickerDialog
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentAnnouncementsBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.announcement.AnnouncementDataItem
import com.example.scstrade.model.response.announcement.AnnouncementItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


class AnnouncementsFragment : Fragment() {
    lateinit var binding:FragmentAnnouncementsBinding
    lateinit var sharedViewModel: SharedViewModel
    lateinit var symbol:String
    val sdf=SimpleDateFormat("dd/MM/yyyy")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAnnouncementsBinding.inflate(inflater,container,false)
        sharedViewModel= (requireActivity().application as MyApp).viewModel
        symbol = requireActivity().intent?.extras?.getString(AppConstants.SYMBOL) ?: ""
        sharedViewModel.announcementType()
        sharedViewModel.insider(symbol)
        binding.loader.visibility = View.GONE
        binding.main.visibility=View.VISIBLE
        binding.textDate.text = sdf.format(Date())
        binding.relativeLayoutDate.setOnClickListener {
           showDatePicker(binding.textDate)
        }
        sharedViewModel.mutableAnnouncementType.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    val adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_dropdown_item_1line)
                    result.data?.map { it.type }?.forEach {
                        adapter.add(it)
                    }
                    binding.loader.visibility = View.GONE

                    binding.spinnerAnnouncement.adapter=adapter
                }
            }
        })

        sharedViewModel.mutableAnnouncement.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {
                    binding.loader.visibility = View.GONE
                }
                is Resource.Loading -> binding.loader.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.loader.visibility = View.GONE
                    binding.main.setContent {
                        AnnouncementItems(list = result.data?.filter {
                            Utils.compareDates(it.bmDate?:"",binding.textDate.text.toString())
                        }?.toList())
                    }
                }
            }
        })


        binding.spinnerAnnouncement.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
               binding.main.setContent {
                    sharedViewModel.announcement(symbol,p0?.getItemAtPosition(p2).toString())

               }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        return binding.root
    }



    private fun showDatePicker(textView: TextView) {

        val dialog = CustomDatePickerDialog{date->
            textView.text = date
            binding.main.setContent {
                val result=sharedViewModel.mutableAnnouncement.value
                AnnouncementItems(list = result?.data?.filter {
                    Utils.compareDates(it.bmDate?:"",binding.textDate.text.toString())
                }?.toList())
            }
        }
        dialog.show(requireActivity().supportFragmentManager, "CUSTOM_DATE_PICKER")
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val calendar = Calendar.getInstance()
            val sdf=SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            val year = if(textView.text.isNullOrEmpty()) calendar.get(Calendar.YEAR) else textView.text.split("/").get(2).toInt()
            val month = if(textView.text.isNullOrEmpty()) calendar.get(Calendar.MONTH) else textView.text.split("/").get(1).toInt()-1
            val day = if(textView.text.isNullOrEmpty()) calendar.get(Calendar.DAY_OF_MONTH) else textView.text.split("/").get(0).toInt()
            textView.text = sdf.format(calendar.time)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->

                    calendar.set(selectedYear,selectedMonth,selectedDay)
                    textView.text = sdf.format(calendar.time)
                    binding.main.setContent {
                        val result=sharedViewModel.mutableAnnouncement.value
                        AnnouncementItems(list = result?.data?.filter {
                             Utils.compareDates(it.bmDate?:"",binding.textDate.text.toString())
                        }?.map { AnnouncementItem(it.bmDesc,it.bmDate,it.bmTime,it.bmImageLink,it.bmPDFLink) }?.toList())
                    }
                },
                year, month, day
            )
            datePickerDialog.setCancelable(false)
            datePickerDialog.setCanceledOnTouchOutside(false)
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
        }*/

    }
    @Composable
    private fun AnnouncementItems(list: List<AnnouncementDataItem>?) {
        var toggleImage by remember {
            mutableStateOf(true)
        }
        LazyColumn(modifier = Modifier.padding(horizontal = 15.dp)) {
            items(list?.size?:0) { index ->
                Column{
                    Text(list?.get(index)?.announcementType?:"",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 20.sp,
                            fontFamily = FontFamily(Font(R.font.custom_font)),
                            fontWeight = FontWeight(500),
                            color = when(list?.get(index)?.announcementType){
                                "Board Meetings" -> Color(0xFF187376)
                                "Shareholder Meetings" -> Color(0x1A1A73E8)
                                "Financial Result" -> Color(0x1AA44FA9)
                                "Material Information" -> Color(0x1A1A73E8)
                                else -> Color(0x1A625B71)
                            },
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.1.sp,
                        ),
                        modifier = Modifier
                            .padding(0.dp)
                            .width(126.dp)
                            .height(20.dp)
                            .background(color = Color(0x1A187376), shape = RoundedCornerShape(size = 6.dp))
                    )
                }
            }
        }
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
        binding.loader.visibility = View.GONE

    }

}