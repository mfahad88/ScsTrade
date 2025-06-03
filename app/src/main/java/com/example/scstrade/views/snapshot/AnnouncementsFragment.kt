package com.example.scstrade.views.snapshot

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
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
import com.example.scstrade.views.announcement.AnnoucementActivity
import com.example.scstrade.views.widgets.ZoomImageView
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
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

                    if(binding.spinnerAnnouncement.adapter!=null && binding.spinnerAnnouncement.adapter.count>0){
                        binding.spinnerAnnouncement.setSelection(0)
                        snapshotViewModel.announcement(symbol,binding.spinnerAnnouncement.selectedItem.toString(),binding.textDate.text.toString())
                    }

                }
            }
        })

        snapshotViewModel.mutableAnnouncementItem.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {
                    Utils.showError(requireView(), result.message ?: "An error occurred...")
                    binding.loader.visibility = View.GONE
                }
                is Resource.Loading -> binding.loader.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.loader.visibility = View.GONE
                    if(result.data?.isNotEmpty()?:false){
                        binding.main.setContent {

                            AnnouncementItems(list = result.data?: emptyList())
                        }
                    }else{
                        Utils.showError(requireView(), getString(R.string.no_record_found))
                    }

                }
            }
        })
        binding.textDate.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false
            private var deletingHyphen = false
            private var lastInput = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                deletingHyphen = count > after && s?.getOrNull(start) == '/'
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                if (isFormatting || editable == null) return

                isFormatting = true
                val input = editable.toString().replace("/", "") // Remove existing slashes
                val formatted = StringBuilder()

                for (i in input.indices) {
                    formatted.append(input[i])
                    if ((i == 1 || i == 3) && i < input.length - 1) {
                        formatted.append("/") // Add slash at the correct positions
                    }
                }

                lastInput = formatted.toString()
                binding.textDate.setText(lastInput)
                binding.textDate.setSelection(lastInput.length) // Move cursor to the end
                isFormatting = false
                if(binding.textDate.text.length==8){
                    Utils.hideKeyboard(requireContext(),binding.textDate)
                    snapshotViewModel.announcement(symbol,binding.spinnerAnnouncement.selectedItem.toString(),binding.textDate.text.toString())
                }else if(binding.textDate.text.length==0){
                    Utils.hideKeyboard(requireContext(),binding.textDate)
                    snapshotViewModel.announcement(symbol,binding.spinnerAnnouncement.selectedItem.toString(),null)

                }
            }
        })

        binding.imageView.setOnClickListener {
            showDatePicker(binding.textDate)
        }
        binding.spinnerAnnouncement.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                snapshotViewModel.announcement(symbol,binding.spinnerAnnouncement.selectedItem.toString(),binding.textDate.text.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

    }

    private fun init() {
        symbol = requireActivity().intent?.extras?.getString(AppConstants.SYMBOL) ?: ""

        snapshotViewModel = if(requireActivity() is SnapshotActivity) (requireActivity() as SnapshotActivity).snapshotViewModel else (requireActivity() as AnnoucementActivity).snapshotViewModel
        sharedViewModel = if(requireActivity() is SnapshotActivity) ((requireActivity() as SnapshotActivity).application as MyApp).viewModel else ((requireActivity() as AnnoucementActivity).application as MyApp).viewModel
//        binding.textDate.text = sdf.format(Date())
        snapshotViewModel.announcementType()

    }


    private fun showDatePicker(textView: TextView) {
        val dialog = CustomDatePickerDialog{date->
            textView.text = date
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

    override fun onDestroyView() {
        super.onDestroyView()
        snapshotViewModel.apply {
            mutableAnnouncementItem.value=null
            mutableInsider.value=null
            mutableAnnouncementType.value = null
            mutableInsider.value=null
        }
    }
    @Composable
    private fun AnnouncementItems(list: List<AnnouncementDataItem>) {
        /*var isExpanded by remember {
            mutableStateOf(MutableList(list.size){false})
        }*/

        val stockItem = sharedViewModel.mutableAllData.value?.data?.filter {
            it.sYM.equals(
                list.filter { it.companyCode!=null }.map { it.companyCode }.first(),
                true
            )
        }?.first()
        val isExpanded = remember { mutableStateListOf(*Array(list.size) { false }) }
        if(list.size>0) {
            LazyColumn(modifier = Modifier.padding(horizontal = 15.dp)) {
                items(list.size) { index ->
                    val announcementItem = list?.get(index)
                    var map = mutableMapOf<String, String>()
                    if (!announcementItem?.bmPlace.isNullOrBlank()) {
                        map["Place"] = announcementItem?.bmPlace ?: ""
                    }
                    if (!announcementItem?.bmYear.isNullOrBlank()) {
                        map["Year"] = announcementItem?.bmYear ?: ""
                    }
                    if (!announcementItem?.bmQuarterNumber.isNullOrBlank()) {
                        map["Quarter"] = announcementItem?.bmQuarterNumber ?: ""
                    }
                    if (!announcementItem?.bmEpsQuarter.isNullOrBlank()) {
                        map["EPS Quarter"] = announcementItem?.bmQuarterNumber ?: ""
                    }
                    if (!announcementItem?.bmEpsCum.isNullOrBlank()) {
                        map["EPS Cumulative"] = announcementItem?.bmEpsCum ?: ""
                    }
                    if (!announcementItem?.bmDividend.isNullOrBlank()) {
                        map["Dividend"] = announcementItem?.bmDividend ?: ""
                    }
                    if (!announcementItem?.bmBonus.isNullOrBlank()) {
                        map["Bonus"] = announcementItem?.bmBonus ?: ""
                    }
                    if (!announcementItem?.bmRightPer.isNullOrBlank()) {
                        map["Right Percent"] = announcementItem?.bmRightPer ?: ""
                    }
                    if (!announcementItem?.bmRightPrice.isNullOrBlank()) {
                        map["Right Price"] = announcementItem?.bmRightPrice ?: ""
                    }
                    if (!announcementItem?.bmBcStartd.isNullOrBlank()) {
                        map["Start Date"] = announcementItem?.bmBcStartd ?: ""
                    }
                    if (!announcementItem?.bmBcEndd.isNullOrBlank()) {
                        map["End Date"] = announcementItem?.bmBcEndd ?: ""
                    }
                    Column {

                        if(binding.spinnerAnnouncement.selectedItem!=null){
                            if(binding.spinnerAnnouncement.selectedItem.toString().equals("all",true)){
                                Row(
                                    modifier = Modifier
                                        .padding(0.dp)
                                        .width(126.dp)
                                        .height(20.dp)
                                        .background(
                                            color = when (list?.get(index)?.announcementType) {
                                                "Board Meetings" -> colorResource(id = R.color.board_meeting)
                                                "Shareholder Meetings" -> colorResource(id = R.color.shareholder_meeting)
                                                "Financial Result" -> colorResource(id = R.color.financial_result)
                                                "Material Information" -> colorResource(id = R.color.material_information)
                                                else -> Color(0x66625B71)
                                            },
                                            shape = RoundedCornerShape(size = 6.dp)
                                        ),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        list?.get(index)?.announcementType ?: "",
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            lineHeight = 20.sp,
                                            fontFamily = FontFamily(Font(R.font.custom_font)),
                                            fontWeight = FontWeight(500),
                                            color = when (list?.get(index)?.announcementType) {
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
                            }
                        }

                        Row {
                            Text(
                                text = stockItem?.sYM ?: "",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 19.sp,
                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                    fontWeight = FontWeight(600),
                                    color = colorResource(id = R.color.black),
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stockItem?.nM ?: "",
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
                        if (!announcementItem?.bmDesc.isNullOrBlank()) {
                            Row {
                                Text(
                                    text = announcementItem?.bmDesc ?: "",
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
                                text = "${
                                    Utils.convertDateString(
                                        announcementItem?.bmDate ?: "0L",
                                        "dd MMM yyyy"
                                    )
                                } ${if (!announcementItem?.bmTime.isNullOrBlank()) "| ${announcementItem?.bmTime ?: ""}" else ""}",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.black),
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Row {
                                if (!announcementItem?.bmImageLink.isNullOrEmpty()) {
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
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.baseline_remove_red_eye_24),
                                            contentDescription = "View",
                                            modifier = Modifier
                                                .size(18.dp)
                                                .align(Alignment.Center),
                                            colorFilter = ColorFilter.tint(color = colorResource(id = R.color.md_theme_onSurfaceVariant))
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(5.dp))
                                }
                                if (!announcementItem?.bmPDFLink.isNullOrEmpty()) {
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
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.baseline_arrow_downward_24),
                                            contentDescription = "Download",
                                            modifier = Modifier
                                                .size(18.dp)
                                                .align(Alignment.Center),
                                            colorFilter = ColorFilter.tint(color = colorResource(id = R.color.md_theme_onSurfaceVariant))
                                        )
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
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.baseline_share_24),
                                            contentDescription = "Share",
                                            modifier = Modifier
                                                .size(18.dp)
                                                .align(Alignment.Center),
                                            colorFilter = ColorFilter.tint(color = colorResource(id = R.color.md_theme_onSurfaceVariant))
                                        )
                                    }
                                }


                            }
                            if (map.isNotEmpty()) {
                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.clickable {
                                            isExpanded[index] = !isExpanded[index]
                                            isExpanded.forEachIndexed { i, b ->
                                                if (index != i) isExpanded[i] = false
                                            }
                                        },
                                        verticalAlignment = Alignment.Bottom
                                    ) {
                                        Text(
                                            text = "More Details",
                                            style = TextStyle(
                                                fontSize = 14.sp,
                                                lineHeight = 34.67.sp,
                                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                                fontWeight = FontWeight(500),
                                                color = colorResource(id = R.color.black),
                                                textAlign = TextAlign.Right,
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Image(
                                            painter = painterResource(id = if (isExpanded[index]) R.drawable.drop_up_icon else R.drawable.drop_down_icon),
                                            contentDescription = "Expanded",
                                            modifier = Modifier.size(20.dp),
                                            colorFilter = ColorFilter.tint(colorResource(id = R.color.black))
                                        )
                                    }
                                }
                            }

                        }

                        if (isExpanded[index]) {
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
                                            color = colorResource(id = R.color.black),
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = stockItem?.sN ?: "Not found",
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            lineHeight = 20.sp,
                                            fontFamily = FontFamily(Font(R.font.custom_font)),
                                            fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black),
                                        )
                                    )

                                }
                                Row {
                                    GridView(map)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(
                            thickness = 1.dp,
                            color = Color(0xFFE5E2E1),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }else{
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.no_data), contentDescription = "No data", modifier = Modifier.size(300.dp))
                Text(
                    text = "No records available",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 19.sp,
                        fontFamily = FontFamily(Font(R.font.custom_font)),
                        fontWeight = FontWeight(600),
                        color = colorResource(id = R.color.black),
                    )
                )
            }
        }
    }

    private @Composable
    fun GridView(map:Map<String,String>?) {
        if(map!=null && map.size>0){
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(if (map.size == 1) 20.dp else if (map.size / 2 == 3) 80.dp else 50.dp)) {
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(if (map.size==1) 1 else map.size/2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)

                ) {
                    items(map.size) { index ->
                        Row {
                            Text(
                                text = "${map.keys.toList()[index]}: ",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.black),
                                )
                            )
                            Text(
                                text = map.values.toList()[index].toString(),
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.black),
                                )
                            )
                        }
                    }
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