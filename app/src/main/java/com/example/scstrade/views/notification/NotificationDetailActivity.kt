package com.example.scstrade.views.notification

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.scstrade.R
import com.example.scstrade.helper.Utils
import com.example.scstrade.helper.downloadPdf
import com.example.scstrade.model.response.announcement.AnnouncementDataItem
import com.example.scstrade.views.widgets.ZoomImageView

class NotificationDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notification_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

   /* @Composable
    private fun AnnouncementItems(list: List<AnnouncementDataItem>) {
        *//*var isExpanded by remember {
            mutableStateOf(MutableList(list.size){false})
        }*//*
        val stockItem= sharedViewModel.mutableAllData.value?.data?.filter {
            it.sYM.equals(
                list.map { it.companyCode }.first(),
                true
            )
        }?.first()
        val isExpanded = remember { mutableStateListOf(*Array(list.size) { false }) }
        if(list.size>0) {
            LazyColumn(modifier = Modifier.padding(horizontal = 15.dp)) {
                items(list?.size ?: 0) { index ->
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
                        if(binding.spinnerAnnouncement.selectedItem.toString().equals("all",true)){
                            Row(
                                modifier = Modifier
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
                        Row {
                            Text(
                                text = stockItem?.sYM ?: "",
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
                                    color = Color(0xFF000000),
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
                                                color = Color(0xFF000000),
                                                textAlign = TextAlign.Right,
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Image(
                                            painter = painterResource(id = if (isExpanded[index]) R.drawable.drop_up_icon else R.drawable.drop_down_icon),
                                            contentDescription = "Expanded",
                                            modifier = Modifier.size(20.dp),
                                            colorFilter = ColorFilter.tint(Color(0xFF000000))
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
                                            color = Color(0xFF000000),
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
                                            color = Color(0xFF000000),
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
            Column(modifier = Modifier.fillMaxSize().padding(top = 50.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.no_data), contentDescription = "No data", modifier = Modifier.size(300.dp))
                Text(
                    text = "No records available",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 19.sp,
                        fontFamily = FontFamily(Font(R.font.custom_font)),
                        fontWeight = FontWeight(600),
                        color = Color(0xFF000000),
                    )
                )
            }
        }
    }*/
}