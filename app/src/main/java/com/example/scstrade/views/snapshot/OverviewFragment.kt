package com.example.scstrade.views.snapshot

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.asFlow
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentOverviewBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.snapshot.Overview
import com.example.scstrade.model.response.snapshot.chart.BookValue
import com.example.scstrade.model.response.snapshot.chart.Charting
import com.example.scstrade.model.response.snapshot.chart.EPS
import com.example.scstrade.model.response.snapshot.chart.EPSYear
import com.example.scstrade.model.response.snapshot.detail.DescNameValue
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.widgets.CustomBarChart
import com.example.scstrade.views.widgets.CustomCombinedChart
import com.example.scstrade.views.widgets.GroupedBarChart
import com.example.scstrade.views.widgets.MultiLineChartView
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry


class OverviewFragment : Fragment() {
    lateinit var binding: FragmentOverviewBinding
    lateinit var sharedViewModel: SharedViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOverviewBinding.inflate(inflater,container,false)

        sharedViewModel= (requireActivity().application as MyApp).viewModel

        sharedViewModel.mutableOverview.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred")
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                   try{
                       binding.onePecent.text = "${result.data?.oneMonthReturn}%"
                       binding.threePecent.text = "${result.data?.twoMonthReturn}%"
                       binding.sixPecent.text = "${result.data?.sixMonthReturn}%"
                       binding.oneYrPecent.text = "${result.data?.twelveMonthReturn}%"
                       val res=sharedViewModel.mutableAllData.value
                       val item=res?.data?.filter { it.sYM.equals(requireActivity().intent.extras?.getString(AppConstants.SYMBOL),true) }?.first()
                       binding.dayRange.setLow(result.data?.oneMonthLow?.toFloat()?:0f,result.data?.oneMonthHigh?.toFloat()?:0f,item?.cL?.toFloat()?:0f)
                       binding.dayRange52.setLow(result.data?.twelveMonthLow?.toFloat()?:0f,result.data?.twelveMonthHigh?.toFloat()?:0f,item?.cL?.toFloat()?:0f)
                       binding.valueTrade.text = item?.cL.toString()
                       binding.netChange.text = "${item?.cH.toString()}(${String.format("%.2f",item?.cHP)}%)"
                       if(item?.iN!="") {
                           binding.labelTextIndex.text =
                               item?.iN?.substring(0, item?.iN?.indexOf("|") ?: 0)
                       }
                       binding.volumeValue.text = Utils.commaFormat(item?.v?.toDouble())
                       binding.avgVolumeValue.text = Utils.commaFormat(result.data?.avgVolume12M?.toDouble())
                       binding.marketCapValue.text = Utils.commaFormat(result.data?.marketCap?.toDouble())
                       binding.companyName.text = item?.nM
                       binding.sector.text = item?.sN
                       binding.ratios.setContent {

                           populateRatios(data = result.data)
                       }
                       binding.loader.visibility = View.GONE
                       binding.main.visibility = View.VISIBLE
                   }catch(e:Exception){
                       e.printStackTrace()
                   }
                }
            }
        })

//        populateBarChart(it)
        return binding.root
    }
    @Composable
    private fun populateRatios(data: Overview?) {
        val detail=sharedViewModel.mutableDetail.asFlow().collectAsState(initial = Resource.Loading()).value.data?.first()
        val charting=sharedViewModel.mutableSnapShotChart.asFlow().collectAsState(initial = Resource.Loading()).value

        Column {
            Card(
                modifier = Modifier
//                    .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                    .fillMaxWidth()
                    .wrapContentHeight(),
                border = BorderStroke(1.dp, Color(0xFFE5E2E1)),
                backgroundColor = colorResource(id = R.color.md_theme_surfaceBright),
                elevation = 0.dp,
                shape = RoundedCornerShape(10.dp),
            ) {
                Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
                    ItemValue(
                        "Paid Up Capital:",
                        Utils.commaFormat(data?.paidUpCapital?.toDouble()),
                        null
                    )
                    Row {
                        Divider(
                            thickness = 1.dp,
                            color = Color(0xFFE5E2E1),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                    ItemValue("Authorized Capital:", data?.authorizedCapital ?: "0", null)
                    Row {
                        Divider(
                            thickness = 1.dp,
                            color = Color(0xFFE5E2E1),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                    ItemValue(
                        "Total No Shares:",
                        Utils.commaFormat(data?.totalNoShares?.toDouble()),
                        null
                    )
                    Row {
                        Divider(
                            thickness = 1.dp,
                            color = Color(0xFFE5E2E1),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                    ItemValue("Free_Float:", data?.freeFloat ?: "0", null)
                    Row {
                        Divider(
                            thickness = 1.dp,
                            color = Color(0xFFE5E2E1),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                    ItemValue("Beta:", data?.beta ?: "0", null)
                    Row {
                        Divider(
                            thickness = 1.dp,
                            color = Color(0xFFE5E2E1),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    ItemValue("Face Value:", data?.faceValue ?: "0", null)
                    Row {
                        Divider(
                            thickness = 1.dp,
                            color = Color(0xFFE5E2E1),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    ItemValue(
                        "Beta:",
                        String.format("%.2f", data?.beta?.toDouble() ?: "0".toDouble()),
                        null
                    )
                    Row {
                        Divider(
                            thickness = 1.dp,
                            color = Color(0xFFE5E2E1),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                    ItemValue("Free Float:", "${data?.freeFloatPer ?: "0"}%", null)
                    Row {
                        Divider(
                            thickness = 1.dp,
                            color = Color(0xFFE5E2E1),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                    ItemValue("Year End:", data?.yearEnd ?: "", null)
                    Row {
                        Divider(
                            thickness = 1.dp,
                            color = Color(0xFFE5E2E1),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    ItemValue(
                        "Market Cap:",
                        Utils.convertToMillions(data?.marketCap?.toDouble() ?: "0".toDouble()),
                        null
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Card(
                modifier = Modifier

                    .fillMaxWidth()
                    .wrapContentHeight(),
                border = BorderStroke(1.dp, Color(0xFFE5E2E1)),

                elevation = 0.dp,
                backgroundColor = colorResource(id = R.color.md_theme_surfaceBright),
                shape = RoundedCornerShape(10.dp),
            ) {
                Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
                    Text(
                        text = "Earnings",
                        style = TextStyle(
                            fontSize = 20.sp,
                            lineHeight = 27.sp,
                            fontFamily = FontFamily(Font(R.font.custom_font)),
                            fontWeight = FontWeight(700),
                            color = colorResource(id = R.color.black),
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    detail?.snapShot?.earnings?.forEachIndexed{index, descNameValue ->
                        ItemValue(descNameValue.name?:"",descNameValue.value?:"",descNameValue.desc)
                        if(index<detail.snapShot.earnings.size-1) {
                            Row {
                                Divider(
                                    thickness = 1.dp,
                                    color = Color(0xFFE5E2E1),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }

                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                border = BorderStroke(1.dp, Color(0xFFE5E2E1)),
                elevation = 0.dp,
                backgroundColor = colorResource(id = R.color.md_theme_surfaceBright),
                shape = RoundedCornerShape(10.dp),
            ) {
                Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
                    Text(
                        text = "Important Ratios",
                        style = TextStyle(
                            fontSize = 20.sp,
                            lineHeight = 27.sp,
                            fontFamily = FontFamily(Font(R.font.custom_font)),
                            fontWeight = FontWeight(700),
                            color = colorResource(id = R.color.black),
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    detail?.snapShot?.importantRatios?.forEachIndexed{index, descNameValue ->
                        if(descNameValue!=null){

                            ItemValue(descNameValue.name?:"",descNameValue.value?:"",descNameValue.desc)
                            if(index<detail.snapShot.importantRatios.size-1) {
                                Row {
                                    Divider(
                                        thickness = 1.dp,
                                        color = Color(0xFFE5E2E1),
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))
            when(charting){
                is Resource.Error -> {
                    Utils.showError(binding.root, charting.message ?: "An error occurred")
                    Log.e("Error: ",charting.message?:"")
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                            .height(250.dp),
                        factory = { context -> CustomBarChart(context) },
                        update = { populateBarChart(it,charting.data?.ePSYear) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                    .height(250.dp),
                factory = { context -> GroupedBarChart(context) },
                update = { populateGroupBarChart(it,charting.data?.ePS) }
            )

            Spacer(modifier = Modifier.height(15.dp))
            if(detail?.snapShot?.equity!=null) {
                ExpandableList("Equity Ratios", detail.snapShot.equity,charting.data)
                Spacer(modifier = Modifier.height(5.dp))
            }
            if(detail?.snapShot?.dividend!=null) {
                ExpandableList("Dividend", detail.snapShot.dividend,charting.data)
                Spacer(modifier = Modifier.height(5.dp))
            }
            if(detail?.snapShot?.sales!=null) {
                ExpandableList("Sales", detail.snapShot.sales,charting.data)
                Spacer(modifier = Modifier.height(5.dp))
            }
            if(detail?.snapShot?.enterpriseValue!=null) {
                ExpandableList(
                    "Enterprise Value",
                    detail.snapShot.enterpriseValue,
                    charting.data
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
            if(detail?.snapShot?.cash!=null) {
                ExpandableList("Cash", detail.snapShot.cash, charting.data)
                Spacer(modifier = Modifier.height(5.dp))
            }
            if(detail?.snapShot?.liquidity!=null) {
                ExpandableList("Liquidity", detail.snapShot.liquidity, charting.data)
                Spacer(modifier = Modifier.height(5.dp))
            }
            if(detail?.snapShot?.solvency!=null) {
                ExpandableList("solvency", detail.snapShot.solvency, charting.data)
                Spacer(modifier = Modifier.height(5.dp))
            }
            if(detail?.snapShot?.advancesAndDeposits!=null) {
                ExpandableList(
                    "Advances And Deposits",
                    detail.snapShot.advancesAndDeposits,
                    charting.data
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
            if(detail?.snapShot?.insurance!=null) {
                ExpandableList("Insurance", detail.snapShot.insurance, charting.data)
                Spacer(modifier = Modifier.height(5.dp))
            }
            if(detail?.snapShot?.netAssetValueNAV!=null) {
                ExpandableList(
                    "Net Asset",
                    detail.snapShot.netAssetValueNAV,
                    charting.data
                )
                Spacer(modifier = Modifier.height(5.dp))
            }

            if(detail?.snapShot?.profitablility!=null){
                ExpandableList(
                    "Profitablility",
                    detail.snapShot.profitablility,
                    charting.data
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
            Spacer(modifier = Modifier.height(15.dp))

            Card(
                modifier = Modifier

                    .fillMaxWidth()
                    .wrapContentHeight(),
                border = BorderStroke(1.dp, Color(0xFFE5E2E1)),
                elevation = 0.dp,
                backgroundColor = colorResource(id = R.color.md_theme_surfaceBright),
                shape = RoundedCornerShape(10.dp),
            ){
                Column (
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 20.dp)
                ){
                    Text(
                        text = "About Company:",
                        style = TextStyle(
                            fontSize = 18.sp,
                            lineHeight = 27.sp,
                            fontFamily = FontFamily(Font(R.font.custom_font)),
                            fontWeight = FontWeight(700),
                            color = colorResource(id = R.color.md_theme_primary),
                        )
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = data?.description?:"",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 30.08.sp,
                            fontFamily = FontFamily(Font(R.font.custom_font)),
                            fontWeight = FontWeight(500),
                            color = colorResource(id = R.color.black),
                        )
                    )

                }
            }
        }


    }

    @Composable
    private fun ExpandableList(title: String, list: List<DescNameValue>, charting: Charting?) {
        var expand by remember {
            mutableStateOf(false)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(50.dp)
                .border(
                    width = 1.dp,
                    color = Color(0xFFE5E2E1),
                    shape = RoundedCornerShape(6.dp)
                )
                .background(
                    color = colorResource(
                        id = R.color.md_theme_surfaceBright
                    )
                )
                .clickable {
                    expand = !expand
                }
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(start = 15.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 30.08.sp,
                    fontFamily = FontFamily(Font(R.font.custom_font)),
                    fontWeight = FontWeight(600),
                    color = colorResource(id = R.color.black),
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = if(expand) R.drawable.drop_up else R.drawable.drop_down),
                contentDescription = "Expandable",
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 15.dp),
                colorFilter = ColorFilter.tint(color = colorResource(id = R.color.black)))

        }
        if(expand){
            Spacer(modifier = Modifier.height(7.dp))
            list.forEachIndexed { index, descNameValue ->

                Column {
                    Row {
                        Text(
                            text = descNameValue.name?:"",
                            style = TextStyle(
                                fontSize = 14.sp,
                                lineHeight = 30.08.sp,
                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.black),
                            )
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = descNameValue.desc?:"",
                            style = TextStyle(
                                fontSize = 12.sp,
                                lineHeight = 30.08.sp,
                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                fontWeight = FontWeight(500),
                                color = Color(0xFF787776),
                            )
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = descNameValue.value?:"",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 30.08.sp,
                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                fontWeight = FontWeight(700),
                                color = colorResource(id = R.color.snapshot_value),
                                textAlign = TextAlign.Right,
                            )
                        )
                    }
                    if(index<list.size-1){
                        Divider(
                            thickness = 1.dp,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFFE5E2E1)
                        )
                    }


                }
            }

            if(title.equals("Equity Ratios",true)){
                Column {
                    AndroidView(
                        modifier = Modifier
                            .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                            .fillMaxWidth()
                            .height(250.dp),
                        factory = { context -> CustomCombinedChart(context) },
                        update = {
                            it.setChartData(
                                charting?.bookValue?.bookValuePKR?.map { it.toFloat() }
                                    ?.toMutableList(),
                                charting?.bookValue?.priceToBookValueX?.map { it.toFloat() }
                                    ?.toList(),
                                charting?.bookValue?.year,
                                android.graphics.Color.parseColor("#7cb5ec")
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    AndroidView(
                        modifier = Modifier
                            .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                            .fillMaxWidth()
                            .height(250.dp),
                        factory = { context -> MultiLineChartView(context) },
                        update = {
                            it.setChartData(charting?.rOAROE?.year,
                                listOf("Ret On CE","Ret On Equity","Ret On Assets"),
                                charting?.rOAROE?.returnOnCE?.mapIndexed { index, d -> Entry(index.toFloat(),d.toFloat()) },
                                charting?.rOAROE?.returnOnEquity?.mapIndexed { index, d -> Entry(index.toFloat(),d.toFloat()) },
                                charting?.rOAROE?.returnOnAssets?.mapIndexed { index, d ->  Entry(index.toFloat(),d.toFloat())},
                                mutableListOf(android.graphics.Color.parseColor("#90ed7d"),android.graphics.Color.parseColor("#7cb5ec"),android.graphics.Color.parseColor("#434348"))
                            )
                        }
                    )
                }
            }
            if(title.equals("Dividend",true)){
                AndroidView(
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                        .fillMaxWidth()
                        .height(250.dp),
                    factory = { context -> CustomCombinedChart(context) },
                    update = {
                       it.setChartData(charting?.dividend?.dividend?.map { it.toFloat() },charting?.dividend?.dividendYieldPer?.map { it.toFloat() },charting?.dividend?.year,android.graphics.Color.parseColor("#ffaa07"))
                    }
                )
                Spacer(modifier = Modifier.height(5.dp))

                AndroidView(
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                        .fillMaxWidth()
                        .height(250.dp),
                    factory = { context -> MultiLineChartView(context) },
                    update = {
                        it.setChartData(charting?.payout?.year,
                            listOf("Payout"),charting?.payout?.payoutRatio?.mapIndexed { index, d -> Entry(index.toFloat(),d.toFloat()) }?.toList(),null,null,
                            listOf(android.graphics.Color.parseColor("#000000"))
                        )
                    }
                )
            }

            if(title.equals("Cash",true)){

                AndroidView(
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                        .fillMaxWidth()
                        .height(250.dp),
                    factory = { context -> MultiLineChartView(context) },
                    update = {
                        it.setChartData(charting?.cash?.year,
                            listOf("Cash per Share"),charting?.cash?.cashPerShare?.mapIndexed { index, d -> Entry(index.toFloat(),d.toFloat()) }?.toList(),null,null,
                            listOf(android.graphics.Color.parseColor("#7cb5ec"))
                        )
                    }
                )
            }

            if(title.equals("Advances And Deposits",true)){
                AndroidView(
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                        .fillMaxWidth()
                        .height(250.dp),
                    factory = { context -> MultiLineChartView(context) },
                    update = {
                        it.setChartData(charting?.aDR?.year,
                            listOf("Equity to Ad","ADR","Cash to DPR"),charting?.aDR?.equityToAd?.mapIndexed { index, d -> Entry(index.toFloat(),d.toFloat()) }?.toList(),charting?.aDR?.aDR?.mapIndexed { index, d -> Entry(index.toFloat(),d.toFloat()) }?.toList(),charting?.aDR?.cashToDPR?.mapIndexed { index, d -> Entry(index.toFloat(),d.toFloat()) }?.toList(),
                            listOf(android.graphics.Color.parseColor("#90ed7d"),android.graphics.Color.parseColor("#7cb5ec"),android.graphics.Color.parseColor("#434348"))
                        )
                    }
                )
            }

            if(title.equals("Profitablility",true)){
                AndroidView(
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                        .fillMaxWidth()
                        .height(250.dp),
                    factory = { context -> MultiLineChartView(context) },
                    update = {
                        it.setChartData(charting?.profitablity?.year,
                            listOf("Net Profit Margin","Gross Profit Margin"),charting?.profitablity?.netProfitMargin?.mapIndexed { index, d -> Entry(index.toFloat(),d.toFloat()) }?.toList(),charting?.profitablity?.grossProfitMargin?.mapIndexed { index, d -> Entry(index.toFloat(),d.toFloat()) }?.toList(),null,
                            listOf(android.graphics.Color.parseColor("#7cb5ec"),android.graphics.Color.parseColor("#000000"))
                        )
                    }
                )
            }
            if(title.equals("Insurance",true)){
                AndroidView(
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                        .fillMaxWidth()
                        .height(250.dp),
                    factory = { context -> MultiLineChartView(context) },
                    update = {
                        it.setChartData(charting?.insurance?.year,
                            listOf("UWR to PAT","II to PAT"),charting?.insurance?.uWRToPAT?.mapIndexed { index, d -> Entry(index.toFloat(),d.toFloat()) }?.toList(),charting?.insurance?.iIToPAT?.mapIndexed { index, d -> Entry(index.toFloat(),d.toFloat()) }?.toList(),null,
                            listOf(android.graphics.Color.parseColor("#7cb5ec"),android.graphics.Color.parseColor("#000000"))
                        )
                    }
                )
            }
        }

    }

    @Composable
    private fun ItemValue(key:String,value:String,desc:String?) {
        Row {
            Column {
                Text(
                    text = key,
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 30.08.sp,
                        fontFamily = FontFamily(Font(R.font.custom_font)),
                        fontWeight = FontWeight(500),
                        color = colorResource(id = R.color.black),
                    )
                )
                if(desc!=null){
                    Text(
                        text = desc,
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 30.08.sp,
                            fontFamily = FontFamily(Font(R.font.custom_font)),
                            fontWeight = FontWeight(500),
                            color =  Color(0xFF625B71),
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = value,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 30.08.sp,
                    fontFamily = FontFamily(Font(R.font.custom_font)),
                    fontWeight = FontWeight(700),
                    color = colorResource(id = R.color.snapshot_value),
                    textAlign = TextAlign.Right,
                )
            )
        }
    }




    private fun populateGroupBarChart(groupedBarChart: GroupedBarChart, ePS: EPS?) {

        val group1: MutableList<BarEntry> = ArrayList()
        val group2: MutableList<BarEntry> = ArrayList()
        val group3: MutableList<BarEntry> = ArrayList()
        val group4: MutableList<BarEntry> = ArrayList()
        ePS?.q1?.forEachIndexed { index, d ->
            group1.add(BarEntry(index.toFloat(),d.toFloat()))
        }
        ePS?.q2?.forEachIndexed { index, d ->
            group2.add(BarEntry(index.toFloat(),d.toFloat()))
        }
        ePS?.q3?.forEachIndexed { index, d ->
            group3.add(BarEntry(index.toFloat(),d.toFloat()))
        }
        ePS?.q4?.forEachIndexed { index, d ->
            group4.add(BarEntry(index.toFloat(),d.toFloat()))
        }

        groupedBarChart.setGroupedBarData(group1, group2,group3,group4, "Q1", "Q2","Q3","Q4")
    }

    private fun populateBarChart(customBarChart: CustomBarChart, ePS: EPSYear?) {

        customBarChart.setChartData(ePS?.year,ePS?.earningPerShare?.map { it.toFloat() }?.toList(),0.5f)

    }


}