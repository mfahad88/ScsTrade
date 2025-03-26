package com.example.scstrade.views.snapshot

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.mycalendar_sdk.CustomDatePickerDialog
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentAnnouncementsBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.helper.downloadPdf
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.announcement.AnnouncementDataItem
import com.example.scstrade.model.response.insider.InsiderDataItem
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.viewmodels.SnapshotViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.widgets.ZoomImageView
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


class AnnouncementsFragment : Fragment() {
    lateinit var binding:FragmentAnnouncementsBinding
    lateinit var symbol:String
    val sdf=SimpleDateFormat("dd/MM/yyyy")
    lateinit var snapshotViewModel: SnapshotViewModel
    lateinit var sharedViewModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAnnouncementsBinding.inflate(inflater,container,false)

        init()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.relativeLayoutDate.setOnClickListener {
            showDatePicker(binding.textDate)
        }

        snapshotViewModel.mutableAnnouncementItem.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    val announcementItem=result.data
                    if(binding.spinnerAnnouncement.selectedItem.toString().equals("All",true)){
                        val list= ArrayList<AnnouncementDataItem>()
                        list.addAll(announcementItem?.filter { Utils.compareDates(
                            it.bmDate ?: "0L",
                            binding.textDate.text.toString()
                        ) }?: emptyList())

                        list.addAll(snapshotViewModel.mutableInsider.value?.data?.filter {
                            Utils.compareDates(
                                it.insiderTransactionPostDate ?: "0L",
                                binding.textDate.text.toString()
                            )
                        }?.map { AnnouncementDataItem("Insider", bmDesc = it.insiderTransactionDesc, bmDate = it.insiderTransactionPostDate, bmImageLink = it.insiderTransactionImageLink, bmPDFLink = it.insiderTransactionPDFLink, companyCode = null, bmEpsQuarter = null, bmEpsCum = null, bmQuarterNumber = null, bmRightPrice = null, bmRightD = null, bmRightP = null, bmRightPer = null, bmBcStartd = null, bmDividend = null, bmBcLd = null, bmTime = null, bmYear = null, bmBcExp = null, bmBonus = null, bmPlace = null, bmBcEndd = null) }?: emptyList())

                        binding.main.setContent {

                            AnnouncementItems(
                                list = list, sharedViewModel.mutableAllData.value?.data?.filter {
                                    it.sYM.equals(
                                        symbol,
                                        true
                                    )
                                }?.first()
                            )
                        }
                    }else {
                        binding.main.setContent {

                            AnnouncementItems(
                                list = announcementItem?.filter {
                                    Utils.compareDates(
                                        it.bmDate ?: "0L",
                                        binding.textDate.text.toString()
                                    )
                                } ?: emptyList(),
                                sharedViewModel.mutableAllData.value?.data?.filter {
                                    it.sYM.equals(
                                        symbol,
                                        true
                                    )
                                }?.first()
                            )
                        }
                    }
                }
            }
        })

        snapshotViewModel.mutableInsider.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    if(binding.spinnerAnnouncement.selectedItem.toString().equals("insider",true)){
                        binding.main.setContent {
                            AnnouncementItems(list = result.data?.filter { Utils.compareDates(it.insiderTransactionPostDate?:"0L",binding.textDate.text.toString()) }
                                ?.map { AnnouncementDataItem("Insider", bmDesc = it.insiderTransactionDesc, bmDate = it.insiderTransactionPostDate, bmImageLink = it.insiderTransactionImageLink, bmPDFLink = it.insiderTransactionPDFLink, companyCode = null, bmEpsQuarter = null, bmEpsCum = null, bmQuarterNumber = null, bmRightPrice = null, bmRightD = null, bmRightP = null, bmRightPer = null, bmBcStartd = null, bmDividend = null, bmBcLd = null, bmTime = null, bmYear = null, bmBcExp = null, bmBonus = null, bmPlace = null, bmBcEndd = null) }?.toList()?: emptyList(), stockItem = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(symbol,true) }?.first())
                        }
                    }
                }
            }
        })

        binding.spinnerAnnouncement.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
               /* binding.textDate.text=""
                binding.main.setContent {
                    AnnouncementItems(list = emptyList(), stockItem = null)
                }*/
                if(binding.textDate.text.isNotEmpty()) {
                    if (binding.spinnerAnnouncement.selectedItem.toString()
                            .equals("Insider", true)
                    ) {
                        snapshotViewModel.insider(symbol)
                    } else if (binding.spinnerAnnouncement.selectedItem.toString()
                            .equals("All", true)
                    ) {
                        snapshotViewModel.insider(symbol)
                        snapshotViewModel.announcement(
                            symbol,
                            binding.spinnerAnnouncement.selectedItem.toString()
                        )
                    } else {
                        snapshotViewModel.announcement(
                            symbol,
                            binding.spinnerAnnouncement.selectedItem.toString()
                        )
                    }
                }


            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }



        snapshotViewModel.mutableAnnouncementType.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {
                    binding.loader.visibility = View.GONE
                    Utils.showError(requireView(), result.message ?: "An error occurred...")
                }
                is Resource.Loading -> {}
                is Resource.Success -> {
                    binding.loader.visibility = View.GONE
                    binding.main.visibility=View.VISIBLE
                    binding.spinnerAnnouncement.adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,result.data?.map {it.type }?.toList() as MutableList)
                    /*snapshotViewModel.announcement(symbol,"All")

                    snapshotViewModel.insider(symbol)*/



                }
            }
        })
    }

    private fun init() {
        symbol = requireActivity().intent?.extras?.getString(AppConstants.SYMBOL) ?: ""
        snapshotViewModel = (requireActivity() as SnapshotActivity).snapshotViewModel
        sharedViewModel = ((requireActivity() as SnapshotActivity).application as MyApp).viewModel
        binding.textDate.text = sdf.format(Date())
        snapshotViewModel.announcementType()

    }


    private fun showDatePicker(textView: TextView) {
        val dialog = CustomDatePickerDialog{date->
            textView.text = date
            if(binding.spinnerAnnouncement.selectedItem.toString().equals("Insider",true)){
                snapshotViewModel.insider(symbol)
            }else if(binding.spinnerAnnouncement.selectedItem.toString().equals("All",true)){
                snapshotViewModel.insider(symbol)
                snapshotViewModel.announcement(symbol, binding.spinnerAnnouncement.selectedItem.toString())
            }else {
                snapshotViewModel.announcement(symbol, binding.spinnerAnnouncement.selectedItem.toString())
            }
        }
        if(binding.spinnerAnnouncement.selectedItem.toString().equals("Insider",true)){
            dialog.availableDates =
                snapshotViewModel.mutableInsider.value?.data?.map { it.insiderTransactionPostDate ?: "0L" }
                    ?.toList() ?: emptyList<String>()
        }else if(binding.spinnerAnnouncement.selectedItem.toString().equals("All",true)){
            val list=ArrayList<String>()
            list.addAll(snapshotViewModel.mutableAnnouncementItem.value?.data?.map { it.bmDate ?: "0L" }?.toList()?: emptyList())
            list.addAll(snapshotViewModel.mutableInsider.value?.data?.map { it.insiderTransactionPostDate ?: "0L" }?.toList() ?: emptyList<String>())
            dialog.availableDates = list
        } else{
            dialog.availableDates =
                snapshotViewModel.mutableAnnouncementItem.value?.data?.map { it.bmDate ?: "0L" }
                    ?.toList() ?: emptyList<String>()
        }
        dialog.show(requireActivity().supportFragmentManager, "CUSTOM_DATE_PICKER")
    }
    @Composable
    private fun AnnouncementItems(list: List<AnnouncementDataItem>, stockItem: StockItem?) {
        var isExpanded by remember {
            mutableStateOf(false)
        }


        LazyColumn(modifier = Modifier.padding(horizontal = 15.dp)) {
            items(list?.size?:0) { index ->
                val announcementItem=list?.get(index)
                var map= mutableMapOf<String,String>()
                if(!announcementItem?.bmPlace.isNullOrBlank()){
                    map["Place"]=announcementItem?.bmPlace?:""
                }
                if(!announcementItem?.bmYear.isNullOrBlank()){
                    map["Year"]=announcementItem?.bmYear?:""
                }
                if(!announcementItem?.bmQuarterNumber.isNullOrBlank()){
                    map["Quarter"]=announcementItem?.bmQuarterNumber?:""
                }
                if(!announcementItem?.bmEpsQuarter.isNullOrBlank()){
                    map["EPS Quarter"]=announcementItem?.bmQuarterNumber?:""
                }
                if(!announcementItem?.bmEpsCum.isNullOrBlank()){
                    map["EPS Cumulative"]=announcementItem?.bmEpsCum?:""
                }
                if(!announcementItem?.bmDividend.isNullOrBlank()){
                    map["Dividend"]=announcementItem?.bmDividend?:""
                }
                if(!announcementItem?.bmBonus.isNullOrBlank()){
                    map["Bonus"]=announcementItem?.bmBonus?:""
                }
                if(!announcementItem?.bmRightPer.isNullOrBlank()){
                    map["Right Percent"]=announcementItem?.bmRightPer?:""
                }
                if(!announcementItem?.bmRightPrice.isNullOrBlank()){
                    map["Right Price"]=announcementItem?.bmRightPrice?:""
                }
                if(!announcementItem?.bmBcStartd.isNullOrBlank()){
                    map["Start Date"]=announcementItem?.bmBcStartd?:""
                }
                if(!announcementItem?.bmBcEndd.isNullOrBlank()){
                    map["End Date"]=announcementItem?.bmBcEndd?:""
                }
                Column{
                    Row (modifier = Modifier
                        .padding(0.dp)
                        .width(126.dp)
                        .height(20.dp)
                        .background(
                            color = when (list?.get(index)?.announcementType) {
                                "Board Meetings" -> Color(0x1A187376)
                                "Shareholder Meetings" -> Color(0x1AA44FA9)
                                "Financial Result" -> Color(0x1AA44FA9)
                                "Material Information" -> Color(0x1A1A73E8)
                                else -> Color(0x1A625B71)
                            },
                            shape = RoundedCornerShape(size = 6.dp)
                        ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(list?.get(index)?.announcementType?:"",
                            style = TextStyle(
                                fontSize = 12.sp,
                                lineHeight = 20.sp,
                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                fontWeight = FontWeight(500),
                                color = when(list?.get(index)?.announcementType){
                                    "Board Meetings" -> Color(0xFF187376)
                                    "Shareholder Meetings" -> Color(0xFF187376)
                                    "Financial Result" -> Color(0xFFA44FA9)
                                    "Material Information" -> Color(0xFF1A73E8)
                                    else -> Color(0xFF625B71)
                                },
                                textAlign = TextAlign.Center,
                                letterSpacing = 0.1.sp,
                            ),

                            )
                    }
                    Spacer(modifier = Modifier.height(7.dp))
                    Row {
                        Text(
                            text = stockItem?.sYM?:"",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 19.sp,
                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                fontWeight = FontWeight(600),
                                color = Color(0xFF000000),
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stockItem?.nM?:"",
                            style = TextStyle(
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                fontWeight = FontWeight(500),
                                color = Color(0xFF79776F),
                                textAlign = TextAlign.Center,
                                letterSpacing = 0.1.sp,
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    if(!announcementItem?.bmDesc.isNullOrBlank()) {
                        Row {
                            Text(
                                text = announcementItem?.bmDesc?:"",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                    fontWeight = FontWeight(600),
                                    color = colorResource(id = R.color.md_theme_primary),
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                    Row {
                        Text(
                            text = "${Utils.convertDateString(announcementItem?.bmDate?:"0L","dd MMM yyyy")} ${if(!announcementItem?.bmTime.isNullOrBlank())"| ${announcementItem?.bmTime?:""}" else ""}",
                            style = TextStyle(
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                fontWeight = FontWeight(500),
                                color = Color(0xFF000000),
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(modifier = Modifier.fillMaxWidth()){
                        Row {
                            if(!announcementItem?.bmImageLink.isNullOrEmpty()){
                                Box(
                                    modifier = Modifier
                                        .border(
                                            width = 0.59191.dp,
                                            color = Color(0xFF79776F),
                                            shape = RoundedCornerShape(size = 59.19118.dp)
                                        )
                                        .width(28.dp)
                                        .height(28.dp)
                                        .clickable {
                                            binding.loader.visibility = View.VISIBLE
                                            val dialog = Dialog(requireContext())
                                            dialog.setContentView(R.layout.dialog_image)
                                            Glide
                                                .with(requireContext())
                                                .load(announcementItem?.bmImageLink ?: "")
                                                .into(dialog.findViewById<ZoomImageView>(R.id.imageView))
                                            dialog.setCancelable(false)
                                            dialog.setCanceledOnTouchOutside(false)
                                            dialog
                                                .findViewById<ImageView>(R.id.btnClose)
                                                .setOnClickListener {
                                                    dialog.dismiss()
                                                }
                                            dialog.show()
                                            binding.loader.visibility = View.GONE
                                        }
                                ){
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_remove_red_eye_24),
                                        contentDescription = "View",
                                        modifier = Modifier
                                            .size(18.dp)
                                            .align(Alignment.Center),
                                        colorFilter = ColorFilter.tint(color = colorResource(id = R.color.md_theme_onSurfaceVariant)))
                                }
                                Spacer(modifier = Modifier.width(5.dp))
                            }
                            if(!announcementItem?.bmPDFLink.isNullOrEmpty()){
                                Box(
                                    modifier = Modifier
                                        .border(
                                            width = 0.59191.dp,
                                            color = Color(0xFF79776F),
                                            shape = RoundedCornerShape(size = 59.19118.dp)
                                        )
                                        .width(28.dp)
                                        .height(28.dp)
                                        .clickable {
                                            binding.loader.visibility = View.VISIBLE
                                            downloadPdf(
                                                requireContext(),
                                                announcementItem?.bmPDFLink.toString()
                                            ) { file ->
                                                requireActivity().runOnUiThread {
                                                    if (file != null) {
                                                        openPdf(requireContext(), file)
                                                        binding.loader.visibility = View.GONE
                                                    }
                                                }
                                            }
                                        }
                                ){
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_arrow_downward_24), contentDescription = "Download", modifier = Modifier
                                            .size(18.dp)
                                            .align(Alignment.Center),
                                        colorFilter = ColorFilter.tint(color = colorResource(id = R.color.md_theme_onSurfaceVariant)))
                                }
                                Spacer(modifier = Modifier.width(5.dp))
                                Box(
                                    modifier = Modifier
                                        .border(
                                            width = 0.59191.dp,
                                            color = Color(0xFF79776F),
                                            shape = RoundedCornerShape(size = 59.19118.dp)
                                        )
                                        .width(28.dp)
                                        .height(28.dp)
                                        .clickable {
                                            binding.loader.visibility = View.VISIBLE
                                            downloadPdf(
                                                requireContext(),
                                                announcementItem?.bmPDFLink.toString()
                                            ) { file ->
                                                requireActivity().runOnUiThread {
                                                    if (file != null) {
                                                        sharePdf(file)
                                                    }
                                                }
                                            }
                                        }
                                ){
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_share_24), contentDescription = "Share", modifier = Modifier
                                            .size(18.dp)
                                            .align(Alignment.Center),
                                        colorFilter = ColorFilter.tint(color = colorResource(id = R.color.md_theme_onSurfaceVariant)))
                                }
                            }


                        }
                        if(map.isNotEmpty()) {
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.clickable { isExpanded = !isExpanded },
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Text(
                                        text = "More Details",
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            lineHeight = 34.67.sp,
                                            fontFamily = FontFamily(Font(R.font.custom_font)),
                                            fontWeight = FontWeight(500),
                                            color = Color(0xFF000000),
                                            textAlign = TextAlign.Right,
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Image(
                                        painter = painterResource(id = if (isExpanded) R.drawable.drop_up_icon else R.drawable.drop_down_icon),
                                        contentDescription = "Expanded",
                                        modifier = Modifier.size(20.dp),
                                        colorFilter = ColorFilter.tint(Color(0xFF000000))
                                    )
                                }
                            }
                        }

                    }

                    if(isExpanded) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Column {
                            Row {
                                Text(
                                    text = "Sector:",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp,
                                        fontFamily = FontFamily(Font(R.font.custom_font)),
                                        fontWeight = FontWeight(500),
                                        color = Color(0xFF000000),
                                    )
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = stockItem?.sN?:"Not found",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp,
                                        fontFamily = FontFamily(Font(R.font.custom_font)),
                                        fontWeight = FontWeight(500),
                                        color = Color(0xFF000000),
                                    )
                                )

                            }
                            Row{
                                GridView(map)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(thickness = 1.dp, color = Color(0xFFE5E2E1), modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }

    private @Composable
    fun GridView(map:Map<String,String>) {
        LazyHorizontalGrid(rows = GridCells.Fixed(map.size/2),
            modifier = Modifier.height(50.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)

        ) {
            items(map.size){index->
                Row {
                    Text(
                        text = "${map.keys.toList()[index]}: ",
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontFamily = FontFamily(Font(R.font.custom_font)),
                            fontWeight = FontWeight(500),
                            color = Color(0xFF000000),
                        )
                    )
                    Text(
                        text = map.values.toList()[index].toString(),
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontFamily = FontFamily(Font(R.font.custom_font)),
                            fontWeight = FontWeight(500),
                            color = Color(0xFF000000),
                        )
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

    fun openPdf(context: Context, file: File) {
        try {
            val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // Use FileProvider for Android 7.0+
                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            } else {
                Uri.fromFile(file)
            }

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/pdf")
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION // Important for FileProvider
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("PDFOpen", "Error opening PDF: ${e.message}")
        }
    }

}