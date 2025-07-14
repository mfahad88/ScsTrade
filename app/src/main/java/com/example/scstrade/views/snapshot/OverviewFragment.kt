package com.example.scstrade.views.snapshot
import androidx.compose.ui.res.dimensionResource

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.asFlow
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentOverviewBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.helper.safeToDoubleOrZero
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
import com.example.scstrade.views.widgets.CustomEVCombinedChart
import com.example.scstrade.views.widgets.GroupedBarChart
import com.example.scstrade.views.widgets.MultiLineChartView
import com.example.scstrade.views.widgets.TextDrawable
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import java.io.File


class OverviewFragment : Fragment() {
    lateinit var binding: FragmentOverviewBinding
    lateinit var sharedViewModel: SharedViewModel
    lateinit var symbol:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOverviewBinding.inflate(inflater,container,false)
        symbol=(requireActivity() as SnapshotActivity).symbol
        sharedViewModel= (requireActivity().application as MyApp).viewModel
        if(!sharedViewModel.mutableAllData.value?.data.isNullOrEmpty()) {
            val firstChar = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(symbol) }?.first()?.sYM?.first()?.uppercaseChar().toString()
            val color = Utils.getColorFromSymbol(firstChar)
            val placeholderDrawable = TextDrawable(firstChar, color)
            val icon = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(symbol) }?.map { it.companyLogo }?.first()
            Glide.with(binding.root.context).load(icon)
                .placeholder(placeholderDrawable)
                .circleCrop()
                .listener(object : RequestListener<Drawable> {


                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.imageView16.alpha = 1f
                        return false // Let Glide handle setting the image
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.imageView16.alpha = 1f
                        return false // Let Glide handle setting the image
                    }


                })
                .into(binding.imageView16)
        }

        sharedViewModel.mutableOverview.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred")
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                   try{


                       Log.e("Result",result.data.toString())
                       binding.onePecent.text = "${result.data?.oneMonthReturn}%"
                       binding.threePecent.text = "${result.data?.twoMonthReturn}%"
                       binding.sixPecent.text = "${result.data?.sixMonthReturn}%"
                       binding.oneYrPecent.text = "${result.data?.twelveMonthReturn}%"
                       binding.loader.visibility = View.GONE
                       binding.main.visibility = View.VISIBLE
                       binding.apply {
                               onePerformance.background?.let {
                               val wrappedDrawable = DrawableCompat.wrap(it)
                               if(result.data?.oneMonthReturn?.contains("-")?:false) {
                                   DrawableCompat.setTint(
                                       wrappedDrawable,
                                       ContextCompat.getColor(requireContext(), R.color.md_theme_error)
                                   )
                               }else{
                                   DrawableCompat.setTint(
                                       wrappedDrawable,
                                       ContextCompat.getColor(requireContext(), R.color.md_theme_primary)
                                   )
                               }
                               onePerformance.background = wrappedDrawable
                           }

                           threePerformance.background?.let {
                               val wrappedDrawable = DrawableCompat.wrap(it)
                               if(result.data?.twoMonthReturn?.contains("-")?:false) {
                                   DrawableCompat.setTint(
                                       wrappedDrawable,
                                       ContextCompat.getColor(requireContext(), R.color.md_theme_error)
                                   )
                               }else{
                                   DrawableCompat.setTint(
                                       wrappedDrawable,
                                       ContextCompat.getColor(requireContext(), R.color.md_theme_primary)
                                   )
                               }
                               onePerformance.background = wrappedDrawable
                           }

                           sixPerformance.background?.let {
                               val wrappedDrawable = DrawableCompat.wrap(it)
                               if(result.data?.sixMonthReturn?.contains("-")?:false) {
                                   DrawableCompat.setTint(
                                       wrappedDrawable,
                                       ContextCompat.getColor(requireContext(), R.color.md_theme_error)
                                   )
                               }else{
                                   DrawableCompat.setTint(
                                       wrappedDrawable,
                                       ContextCompat.getColor(requireContext(), R.color.md_theme_primary)
                                   )
                               }
                               onePerformance.background = wrappedDrawable
                           }

                           oneYearPerformance.background?.let {
                               val wrappedDrawable = DrawableCompat.wrap(it)
                               if(result.data?.twelveMonthReturn?.contains("-")?:false) {
                                   DrawableCompat.setTint(
                                       wrappedDrawable,
                                       ContextCompat.getColor(requireContext(), R.color.md_theme_error)
                                   )
                               }else{
                                   DrawableCompat.setTint(
                                       wrappedDrawable,
                                       ContextCompat.getColor(requireContext(), R.color.md_theme_primary)
                                   )
                               }
                               onePerformance.background = wrappedDrawable
                           }
                       }


                       val res=sharedViewModel.mutableAllData.value
                       val item=res?.data?.filter { it.sYM.equals(requireActivity().intent.extras?.getString(AppConstants.SYMBOL),true) }?.first()
                       binding.dayRange.setLow(item?.lP?.toFloat()?:0f,item?.hP?.toFloat()?:0f,item?.cL?.toFloat()?:0f)
//                       binding.dayRange.setLow(result.data?.oneMonthLow?.toFloat()?:0f,result.data?.oneMonthHigh?.toFloat()?:0f,item?.cL?.toFloat()?:0f)
                       binding.dayRange52.setLow(result.data?.twelveMonthLow?.toFloat()?:0f,result.data?.twelveMonthHigh?.toFloat()?:0f,item?.cL?.toFloat()?:0f)
                       binding.valueTrade.text = if (item?.cL != null && item.cL != 0.0 && !item.cL.isNaN()) {
                           item.cL.toString()
                       } else {
                           val fallback = item?.oC?.toString()
                           if (fallback.isNullOrBlank() || fallback.equals("null", ignoreCase = true)) {
                               "0.0"
                           } else {
                               fallback
                           }
                       }

                       binding.netChange.text = "${if (item?.cH!! < 0.0) "" else "+"}${item?.cH.toString()} ${if (item?.cHP!! < 0.0) "" else "+"}${String.format("%.2f",item?.cHP)}%"
                       if(item?.cH!!<0.0) {
                           binding.netChange.setTextColor(android.graphics.Color.parseColor("#D01B10"))
                       }else{
                           binding.netChange.setTextColor(ContextCompat.getColor(requireContext(),R.color.md_theme_secondaryFixed))
                       }
                       if(!TextUtils.isEmpty(item.iN)) {
                           val indices = item?.iN?.split("|")
                           indices?.forEach {
                               if (it.contains("kse 100", true)) {
                                   binding.kse100.visibility = View.VISIBLE

                               } else if (it.contains("kse 30", true)) {
                                   binding.kse30.visibility = View.VISIBLE
                               } else if (it.contains("kmi 30", true)) {
                                   binding.kmi30.visibility = View.VISIBLE
                               } else if (it.contains("kmi all", true)) {
                                   binding.shariah.visibility = View.VISIBLE
                               }
                           }
                       }
                       binding.volumeValue.text = Utils.commaFormat(item?.v?.toDouble())
                       binding.avgVolumeValue.text = Utils.commaFormat(result.data?.avgVolume12M?.toDouble(),true)
                       binding.marketCapValue.text = Utils.convertToBillions(result.data?.marketCap)
                       binding.companyName.text = item?.nM
//                        Glide.with(requireContext()).load(item.companyLogo).transform(RoundedCorners(50)).into(binding.imageView16)
                       binding.imageViewShare.setOnClickListener {
                           shareViaWhatsApp(requireContext(),"Check out this stock snapshot:\n https://scstrade.com/stockscreening/SS_CompanySnapShot.aspx?symbol=${item.sYM}")
                       }

                       binding.companyName.post {

                           if(binding.sector.lineCount>1 || binding.companyName.lineCount>1){
                               val layoutParams=binding.materialCardView2.layoutParams
                               layoutParams.height=Utils.dpToPx(240)
                               binding.materialCardView2.layoutParams=layoutParams
                           }
                       }

                       binding.sector.text = item?.sN
                       binding.ratios.setContent {

                           populateRatios(data = result.data)
                       }

                   }catch(e:Exception){
                       e.printStackTrace()
                   }
                }
            }
        })

//        populateBarChart(it)
        return binding.root
    }


    fun shareViaWhatsApp(context: Context, message: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
            setPackage("com.whatsapp") // Ensures only WhatsApp handles it
        }

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "WhatsApp is not installed.", Toast.LENGTH_SHORT).show()
        }
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
                border = BorderStroke(dimensionResource(R.dimen.dp_1).value.dp, Color(0xFFE5E2E1)),
                backgroundColor = colorResource(id = R.color.md_theme_surfaceBright),
                elevation = dimensionResource(R.dimen.dp_0).value.dp,
                shape = RoundedCornerShape(dimensionResource(R.dimen.dp_10).value.dp),
            ) {
                Column(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.dp_15).value.dp, vertical = dimensionResource(R.dimen.dp_10).value.dp)) {
                    if(!data?.paidUpCapital.isNullOrEmpty()) {
                        ItemValue(
                            "Paid Up Capital",
                            Utils.convertToMillions(data?.paidUpCapital?.safeToDoubleOrZero()),
                            null
                        )
                        Row {
                            Divider(
                                thickness = dimensionResource(R.dimen.dp_1).value.dp,
                                color = Color(0xFFE5E2E1),
                                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.dp_4).value.dp)
                            )
                        }
                    }
                    if(!data?.authorizedCapital.isNullOrEmpty()) {
                        ItemValue(
                            "Authorized Capital",
                            Utils.convertToMillions(
                                data?.authorizedCapital?.safeToDoubleOrZero()
                            ),
                            null
                        )
                        Row {
                            Divider(
                                thickness = dimensionResource(R.dimen.dp_1).value.dp,
                                color = Color(0xFFE5E2E1),
                                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.dp_4).value.dp)
                            )
                        }
                    }
                    if(!data?.totalNoShares.isNullOrEmpty()) {
                        ItemValue(
                            "Total No Shares",
                            Utils.convertToMillions(
                                data?.totalNoShares?.safeToDoubleOrZero()
                            ),
                            null
                        )
                        Row {
                            Divider(
                                thickness = dimensionResource(R.dimen.dp_1).value.dp,
                                color = Color(0xFFE5E2E1),
                                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.dp_4).value.dp)
                            )
                        }
                    }

                    if(!data?.freeFloat.isNullOrEmpty()) {
                        ItemValue(
                            "Free Float",
                            Utils.convertToMillions(
                                data?.freeFloat?.safeToDoubleOrZero()
                            ),
                            null
                        )
                        Row {
                            Divider(
                                thickness = dimensionResource(R.dimen.dp_1).value.dp,
                                color = Color(0xFFE5E2E1),
                                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.dp_4).value.dp)
                            )
                        }
                    }
                    if(!data?.freeFloatPer.isNullOrEmpty()) {
                        ItemValue(
                            "Free Float(%)",
                            "${Utils.roundPercent(data?.freeFloatPer?.safeToDoubleOrZero())}%",
                            null
                        )
                        Row {
                            Divider(
                                thickness = dimensionResource(R.dimen.dp_1).value.dp,
                                color = Color(0xFFE5E2E1),
                                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.dp_4).value.dp)
                            )
                        }
                    }
                    if(!data?.beta.isNullOrEmpty()) {
                        ItemValue(
                            "Beta",
                            Utils.roundTwoDecimal(data?.beta?.safeToDoubleOrZero()),
                            null
                        )
                        Row {
                            Divider(
                                thickness = dimensionResource(R.dimen.dp_1).value.dp,
                                color = Color(0xFFE5E2E1),
                                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.dp_4).value.dp)
                            )
                        }
                    }
                    if(!data?.faceValue.isNullOrEmpty()) {
                        ItemValue("Face Value", data?.faceValue ?: "0", null)
                        Row {
                            Divider(
                                thickness = dimensionResource(R.dimen.dp_1).value.dp,
                                color = Color(0xFFE5E2E1),
                                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.dp_4).value.dp)
                            )
                        }
                    }

                    if(!data?.yearEnd.isNullOrEmpty()) {
                        ItemValue("Year End", data?.yearEnd ?: "", null)
                        Row {
                            Divider(
                                thickness = dimensionResource(R.dimen.dp_1).value.dp,
                                color = Color(0xFFE5E2E1),
                                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.dp_4).value.dp)
                            )
                        }
                    }

                    if(!data?.marketCap.isNullOrEmpty()) {
                        ItemValue(
                            "Market Cap",
                            Utils.convertToBillions(data?.marketCap ?: "0"),
                            null
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_15).value.dp))
            if(detail?.snapShot?.earnings!=null){
                Card(
                    modifier = Modifier

                        .fillMaxWidth()
                        .wrapContentHeight(),
                    border = BorderStroke(dimensionResource(R.dimen.dp_1).value.dp, Color(0xFFE5E2E1)),

                    elevation = dimensionResource(R.dimen.dp_0).value.dp,
                    backgroundColor = colorResource(id = R.color.md_theme_surfaceBright),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.dp_10).value.dp),
                ) {
                    Column(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.dp_15).value.dp, vertical = dimensionResource(R.dimen.dp_10).value.dp)) {
                        Text(
                            text = "Earnings",
                            style = TextStyle(
                                fontSize = dimensionResource(R.dimen.sp_20).value.sp,
                                lineHeight = dimensionResource(R.dimen.sp_27).value.sp,
                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                fontWeight = FontWeight(700),
                                color = colorResource(id = R.color.black),
                            )
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10).value.dp))
                        detail?.snapShot?.earnings?.forEachIndexed{index, descNameValue ->
                            ItemValue(descNameValue.name?:"",descNameValue.value?:"",descNameValue.desc)
                            if(index<detail.snapShot.earnings.size-1) {
                                Row {
                                    Divider(
                                        thickness = dimensionResource(R.dimen.dp_1).value.dp,
                                        color = Color(0xFFE5E2E1),
                                        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.dp_4).value.dp)
                                    )
                                }
                            }
                        }

                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_15).value.dp))
            }

            if(detail?.snapShot?.importantRatios!=null){
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    border = BorderStroke(dimensionResource(R.dimen.dp_1).value.dp, Color(0xFFE5E2E1)),
                    elevation = dimensionResource(R.dimen.dp_0).value.dp,
                    backgroundColor = colorResource(id = R.color.md_theme_surfaceBright),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.dp_10).value.dp),
                ) {
                    Column(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.dp_15).value.dp, vertical = dimensionResource(R.dimen.dp_10).value.dp)) {
                        Text(
                            text = "Important Ratios",
                            style = TextStyle(
                                fontSize = dimensionResource(R.dimen.sp_20).value.sp,
                                lineHeight = dimensionResource(R.dimen.sp_27).value.sp,
                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                fontWeight = FontWeight(700),
                                color = colorResource(id = R.color.black),
                            )
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10).value.dp))
                        detail?.snapShot?.importantRatios?.forEachIndexed{index, descNameValue ->
                            if(descNameValue!=null){

                                ItemValue(descNameValue.name?:"",descNameValue.value?:"",descNameValue.desc)
                                if(index<detail.snapShot.importantRatios.size-1) {
                                    Row {
                                        Divider(
                                            thickness = dimensionResource(R.dimen.dp_1).value.dp,
                                            color = Color(0xFFE5E2E1),
                                            modifier = Modifier.padding(vertical = dimensionResource(R.dimen.dp_4).value.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_15).value.dp))
            }

            when(charting){
                is Resource.Error -> {
                    Utils.showError(binding.root, charting.message ?: "An error occurred")
                    Log.e("Error: ",charting.message?:"")
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if(charting.data?.ePSYear!=null) {
                        Column (modifier = Modifier
                            .border(
                                width = dimensionResource(R.dimen.dp_1).value.dp,
                                color = Color(0xFFE5E2E1),
                                shape = RoundedCornerShape(dimensionResource(R.dimen.dp_6).value.dp)
                            )
                            .padding(dimensionResource(R.dimen.dp_10).value.dp)
                        ){
                            Row (modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center){
                                Text(
                                    text = charting.data?.ePSYear?.chartName ?: "",
                                    style = TextStyle(
                                        fontSize = dimensionResource(R.dimen.sp_14).value.sp,
                                        fontFamily = FontFamily(Font(R.font.custom_font)),
                                        fontWeight = FontWeight(600),
                                        color = colorResource(id = R.color.black),
                                    )
                                )
                            }
                            AndroidView(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                                    .height(dimensionResource(R.dimen.dp_250).value.dp),
                                factory = { context -> CustomBarChart(context) },
                                update = { populateBarChart(it, charting.data?.ePSYear) }
                            )
                        }
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_15).value.dp))
                    }
                }
            }


            if(charting.data?.ePS!=null) {
                Column(modifier = Modifier
                    .border(
                        width = dimensionResource(R.dimen.dp_1).value.dp,
                        color = Color(0xFFE5E2E1),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.dp_6).value.dp)
                    )
                    .padding(dimensionResource(R.dimen.dp_10).value.dp)) {

                    Row (modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center){
                        Text(
                            text = charting.data?.ePS?.chartName ?: "",
                            style = TextStyle(
                                fontSize = dimensionResource(R.dimen.sp_14).value.sp,
                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                fontWeight = FontWeight(600),
                                color = colorResource(id = R.color.black),
                            )
                        )
                    }
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                            .height(dimensionResource(R.dimen.dp_250).value.dp),
                        factory = { context -> GroupedBarChart(context) },
                        update = { populateGroupBarChart(it, charting.data?.ePS) }
                    )
                }
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_15).value.dp))
            }


            if(detail?.snapShot?.equity!=null) {

                ExpandableList("Equity Ratios", detail.snapShot.equity,charting.data)
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_5).value.dp))
            }
            if(detail?.snapShot?.dividend!=null) {
                ExpandableList("Dividend", detail.snapShot.dividend,charting.data)
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_5).value.dp))
            }
            if(detail?.snapShot?.sales!=null) {
                ExpandableList("Sales", detail.snapShot.sales,charting.data)
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_5).value.dp))
            }
            if(detail?.snapShot?.enterpriseValue!=null) {
                ExpandableList(
                    "Enterprise Value",
                    detail.snapShot.enterpriseValue,
                    charting.data
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_5).value.dp))
            }
            if(detail?.snapShot?.cash!=null) {
                ExpandableList("Cash", detail.snapShot.cash, charting.data)
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_5).value.dp))
            }
            if(detail?.snapShot?.liquidity!=null) {
                ExpandableList("Liquidity", detail.snapShot.liquidity, charting.data)
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_5).value.dp))
            }
            if(detail?.snapShot?.solvency!=null) {
                ExpandableList("Solvency", detail.snapShot.solvency, charting.data)
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_5).value.dp))
            }
            if(detail?.snapShot?.advancesAndDeposits!=null) {
                ExpandableList(
                    "Advances And Deposits",
                    detail.snapShot.advancesAndDeposits,
                    charting.data
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_5).value.dp))
            }
            if(detail?.snapShot?.insurance!=null) {
                ExpandableList("Insurance", detail.snapShot.insurance, charting.data)
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_5).value.dp))
            }
            if(detail?.snapShot?.netAssetValueNAV!=null) {
                ExpandableList(
                    "Net Asset",
                    detail.snapShot.netAssetValueNAV,
                    charting.data
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_5).value.dp))
            }

            if(detail?.snapShot?.profitablility!=null){
                ExpandableList(
                    "Profitablility",
                    detail.snapShot.profitablility,
                    charting.data
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_5).value.dp))
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_15).value.dp))

        /*    Card(
                modifier = Modifier

                    .fillMaxWidth()
                    .wrapContentHeight(),
                border = BorderStroke(dimensionResource(R.dimen.dp_1).value.dp, Color(0xFFE5E2E1)),
                elevation = dimensionResource(R.dimen.dp_0).value.dp,
                backgroundColor = colorResource(id = R.color.md_theme_surfaceBright),
                shape = RoundedCornerShape(dimensionResource(R.dimen.dp_10).value.dp),
            ){
                Column (
                    modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.dp_15).value.dp, vertical = dimensionResource(R.dimen.dp_20).value.dp)
                ){
                    Text(
                        text = "About Company:",
                        style = TextStyle(
                            fontSize = dimensionResource(R.dimen.sp_18).value.sp,
                            lineHeight = dimensionResource(R.dimen.sp_27).value.sp,
                            fontFamily = FontFamily(Font(R.font.custom_font)),
                            fontWeight = FontWeight(700),
                            color = colorResource(id = R.color.md_theme_primary),
                        )
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_5).value.dp))
                    AndroidView(factory = { context ->
                        TextView(context).apply {
                            text = HtmlCompat.fromHtml(data?.description?:"", HtmlCompat.FROM_HTML_MODE_LEGACY)
                            movementMethod = LinkMovementMethod.getInstance() // Enable links if present
                        }
                    })
                    *//*Text(
                        text = data?.description?:"",
                        style = TextStyle(
                            fontSize = dimensionResource(R.dimen.sp_16).value.sp,
                            lineHeight = 30.08.sp,
                            fontFamily = FontFamily(Font(R.font.custom_font)),
                            fontWeight = FontWeight(500),
                            color = colorResource(id = R.color.black),
                        )
                    )*//*

                }
            }*/
        }


    }

    @Composable
    private fun ExpandableList(title: String?, list: List<DescNameValue?>?, charting: Charting?) {
        var expand by remember {
            mutableStateOf(false)
        }
        Column (modifier = Modifier.border(
            width = dimensionResource(R.dimen.dp_1).value.dp,
            color = Color(0xFFE5E2E1),
            shape = RoundedCornerShape(dimensionResource(R.dimen.dp_6).value.dp)
        ).padding(horizontal = dimensionResource(R.dimen.dp_15).value.dp, vertical = dimensionResource(R.dimen.dp_10).value.dp)){

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(dimensionResource(R.dimen.dp_50).value.dp)
                    /*  .border(
                        width = dimensionResource(R.dimen.dp_1).value.dp,
                        color = Color(0xFFE5E2E1),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.dp_6).value.dp)
                    )*/
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
                    text = title?:"",
                    style = TextStyle(
                        fontSize = dimensionResource(R.dimen.sp_18).value.sp,
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
                        .size(dimensionResource(R.dimen.dp_15).value.dp),
                    colorFilter = ColorFilter.tint(color = colorResource(id = R.color.black)))

            }
            if(expand){
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_7).value.dp))
                list?.forEachIndexed { index, descNameValue ->
                    val digitsPart = Regex("""[\d.]+""").find(descNameValue?.value?:"")?.value ?: ""
                    val lettersPart = Regex("""[a-zA-Z]+""").find(descNameValue?.value?:"")?.value ?: ""
                    Column {
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = dimensionResource(R.dimen.dp_5).value.dp/*, horizontal = dimensionResource(R.dimen.dp_5).value.dp*/)
                        ){
                            Column{
                                Text(
                                    text = descNameValue?.name?:"",
                                    style = TextStyle(
                                        fontSize = dimensionResource(R.dimen.sp_14).value.sp,
                                        lineHeight = 30.08.sp,
                                        fontFamily = FontFamily(Font(R.font.custom_font)),
                                        fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.black),
                                    )
                                )
                                Text(
                                    text = descNameValue?.desc?:"",
                                    style = TextStyle(
                                        fontSize = dimensionResource(R.dimen.sp_12).value.sp,
                                        lineHeight = 30.08.sp,
                                        fontFamily = FontFamily(Font(R.font.custom_font)),
                                        fontWeight = FontWeight(500),
                                        color = Color(0xFF787776),
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))

                           Column (modifier = Modifier.fillMaxHeight()){
                               Text(
                                   text = /*if(title.equals("Enterprise Value")) "${Utils.convertToBillions( digitsPart)} ${if(!lettersPart.isNullOrEmpty()) lettersPart else ""}" else */descNameValue?.value?:"",
                                   style = TextStyle(
                                       fontSize = dimensionResource(R.dimen.sp_16).value.sp,
                                       lineHeight = 30.08.sp,
                                       fontFamily = FontFamily(Font(R.font.custom_font)),
                                       fontWeight = FontWeight(700),
                                       color = colorResource(id = R.color.snapshot_value),
                                       textAlign = TextAlign.Right,
                                   )
                               )
                           }

                        }
                        if(index<list.size-1){
                            Divider(
                                thickness = dimensionResource(R.dimen.dp_1).value.dp,
                                modifier = Modifier.fillMaxWidth(),
                                color = Color(0xFFE5E2E1)
                            )
                        }


                    }
                }

                if(title.equals("Equity Ratios",true)){
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center) {
                            Text(
                                text = "Book Value PKR",
                                modifier = Modifier.padding(start = dimensionResource(R.dimen.dp_15).value.dp),
                                style = TextStyle(
                                    fontSize = dimensionResource(R.dimen.sp_14).value.sp,
                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                    fontWeight = FontWeight(600),
                                    color = colorResource(id = R.color.black),
                                )
                            )
                        }
                        AndroidView(
                            modifier = Modifier
                                .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                                .fillMaxWidth()
                                .height(dimensionResource(R.dimen.dp_250).value.dp),
                            factory = { context -> CustomCombinedChart(context) },
                            update = {
                                it.setChartData(
                                    charting?.bookValue?.bookValuePKR?.map { it.toFloat() }?.reversed()
                                        ?.toMutableList(),
                                    charting?.bookValue?.priceToBookValueX?.map { it.toFloat() }?.reversed()
                                        ?.toList(),
                                    charting?.bookValue?.year?.reversed(),
                                    android.graphics.Color.parseColor("#7cb5ec"),
                                    "Book Value",
                                    "Price To BookValue"
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_5).value.dp))
                        AndroidView(
                            modifier = Modifier
                                .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                                .fillMaxWidth()
                                .height(dimensionResource(R.dimen.dp_250).value.dp),
                            factory = { context -> MultiLineChartView(context) },
                            update = {
                                it.setChartData(charting?.rOAROE?.year?.reversed(),
                                    listOf("Ret On CE","Ret On Equity","Ret On Assets"),
                                    charting?.rOAROE?.returnOnCE?.reversed()?.mapIndexed { index, d -> Entry(index.toFloat(),d.toFloat()) },
                                    charting?.rOAROE?.returnOnEquity?.reversed()?.mapIndexed { index, d -> Entry(index.toFloat(),d.toFloat()) },
                                    charting?.rOAROE?.returnOnAssets?.reversed()?.mapIndexed { index, d ->  Entry(index.toFloat(),d.toFloat())},
                                    mutableListOf(android.graphics.Color.parseColor("#90ed7d"),android.graphics.Color.parseColor("#7cb5ec"),android.graphics.Color.parseColor("#434348"))
                                )
                            }
                        )
                    }
                }
                if(title.equals("Dividend",true)){
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center) {
                            Text(
                                text = "Dividend",
                                modifier = Modifier.padding(start = dimensionResource(R.dimen.dp_15).value.dp),
                                style = TextStyle(
                                    fontSize = dimensionResource(R.dimen.sp_14).value.sp,
                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                    fontWeight = FontWeight(600),
                                    color = colorResource(id = R.color.black),
                                )
                            )
                        }
                        AndroidView(
                            modifier = Modifier
                                .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                                .fillMaxWidth()
                                .height(dimensionResource(R.dimen.dp_250).value.dp),
                            factory = { context -> CustomCombinedChart(context) },
                            update = {
                                it.setChartData(
                                    charting?.dividend?.dividend?.reversed()?.map { it.toFloat() },
                                    charting?.dividend?.dividendYieldPer?.reversed()?.map { it.toFloat() },
                                    charting?.dividend?.year?.reversed(),
                                    android.graphics.Color.parseColor("#ffaa07"),
                                    "Dividend Yield",
                                    "Dividend"

                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_5).value.dp))

                        AndroidView(
                            modifier = Modifier
                                .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                                .fillMaxWidth()
                                .height(dimensionResource(R.dimen.dp_250).value.dp),
                            factory = { context -> MultiLineChartView(context) },
                            update = {
                                it.setChartData(
                                    charting?.payout?.year,
                                    listOf("Payout"),
                                    charting?.payout?.payoutRatio?.mapIndexed { index, d ->
                                        Entry(
                                            index.toFloat(),
                                            d.toFloat()
                                        )
                                    }?.toList(),
                                    null,
                                    null,
                                    listOf(android.graphics.Color.parseColor("#000000"))
                                )
                            }
                        )
                    }
                }

                if(title.equals("sales",true)){
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center) {
                            Text(
                                text = "Sales Per Share",
                                modifier = Modifier.padding(start = dimensionResource(R.dimen.dp_15).value.dp),
                                style = TextStyle(
                                    fontSize = dimensionResource(R.dimen.sp_14).value.sp,
                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                    fontWeight = FontWeight(600),
                                    color = colorResource(id = R.color.black),
                                )
                            )
                        }
                        AndroidView(
                            modifier = Modifier
                                .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                                .fillMaxWidth()
                                .height(dimensionResource(R.dimen.dp_250).value.dp),
                            factory = { context -> CustomCombinedChart(context) },
                            update = {
                                it.setChartData(
                                    charting?.sales?.salesPerSharePKR?.map { it.toFloat() }
                                        ?.reversed(),
                                    charting?.sales?.priceToSalesPer?.map { it.toFloat() }
                                        ?.reversed(),
                                    charting?.sales?.year?.reversed(),
                                    android.graphics.Color.parseColor("#cebca6"),
                                    "Price to Sales",
                                    "Sales Per Share"

                                )
                            }
                        )
                    }
                }

                if(title.equals("Cash",true)){

                    Column {
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center) {
                            Text(
                                text = "Cash PS PKR",
                                modifier = Modifier.padding(start = dimensionResource(R.dimen.dp_15).value.dp),
                                style = TextStyle(
                                    fontSize = dimensionResource(R.dimen.sp_14).value.sp,
                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                    fontWeight = FontWeight(600),
                                    color = colorResource(id = R.color.black),
                                )
                            )
                        }
                        AndroidView(
                            modifier = Modifier
                                .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                                .fillMaxWidth()
                                .height(dimensionResource(R.dimen.dp_250).value.dp),
                            factory = { context -> MultiLineChartView(context) },
                            update = {
                                it.setChartData(
                                    charting?.cash?.year?.reversed(),
                                    listOf("Cash per Share"),
                                    charting?.cash?.cashPerShare?.reversed()?.mapIndexed { index, d ->
                                        Entry(
                                            index.toFloat(),
                                            d.toFloat()
                                        )
                                    }?.toList(),
                                    null,
                                    null,
                                    listOf(android.graphics.Color.parseColor("#7cb5ec"))
                                )
                            }
                        )
                    }
                }
                if(title.equals("Enterprise Value",true)){

                    Column {
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center) {
                            Text(
                                text = "Market Cap / EV PKR in Billion",
                                modifier = Modifier.padding(start = dimensionResource(R.dimen.dp_15).value.dp),
                                style = TextStyle(
                                    fontSize = dimensionResource(R.dimen.sp_14).value.sp,
                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                    fontWeight = FontWeight(600),
                                    color = colorResource(id = R.color.black),
                                )
                            )
                        }
                        AndroidView(
                            modifier = Modifier
                                .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                                .fillMaxWidth()
                                .height(dimensionResource(R.dimen.dp_250).value.dp),
                            factory = { context -> CustomEVCombinedChart(context) },
                            update = {
                                /*it.setChartData(
                                    listOf(520.45f, 475.30f, 412.89f, 390.22f, 450.75f),
                                    listOf(430.00f, 445.00f, 460.00f, 470.00f, 480.00f),
                                    listOf(3.5f, 2.9f, 2.2f, 1.8f, 2.4f),
                                    listOf("2020", "2021", "2022", "2023", "2024")
                                )*/
                                it.setChartData(
                                    charting?.enterprise?.marketCap?.reversed()?.map { it.toFloat() }?.toList(),
                                    charting?.enterprise?.ePValue?.reversed()?.map { it.toFloat() }?.toList(),
                                    charting?.enterprise?.eVEBITDA?.reversed()?.map { it.toFloat() }?.toList(),
                                    charting?.enterprise?.year?.reversed(),
                                )
                               /* it.setChartData(
                                    charting?.enterprise?.marketCap?.map { it.toFloat() },
                                    charting?.enterprise?.ePValue?.map { it.toFloat() },
                                    charting?.enterprise?.eVEBITDA?.map { it.toFloat() },
                                    charting?.enterprise?.year,
                                    android.graphics.Color.parseColor("#eeeeee"),
                                    android.graphics.Color.parseColor("#7cb5ec")

                                )*/
                            }
                        )
                    }
                }
                if(title.equals("Advances And Deposits",true)){
                    AndroidView(
                        modifier = Modifier
                            .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                            .fillMaxWidth()
                            .height(dimensionResource(R.dimen.dp_250).value.dp),
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
                            .height(dimensionResource(R.dimen.dp_250).value.dp),
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
                            .height(dimensionResource(R.dimen.dp_250).value.dp),
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

    }

    @Composable
    private fun ItemValue(key:String?,value:String?,desc:String?) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(
                    text = key?:"",
                    style = TextStyle(
                        fontSize = dimensionResource(R.dimen.sp_16).value.sp,
                        lineHeight = 30.08.sp,
                        fontFamily = FontFamily(Font(R.font.custom_font)),
                        fontWeight = FontWeight(500),
                        textAlign = TextAlign.Start,
                        color = colorResource(id = R.color.black),
                    )
                )
                if(desc!=null){
                    Text(
                        text = desc,
                        style = TextStyle(
                            fontSize = dimensionResource(R.dimen.sp_12).value.sp,
                            lineHeight = 30.08.sp,
                            fontFamily = FontFamily(Font(R.font.custom_font)),
                            fontWeight = FontWeight(500),
                            textAlign = TextAlign.Start,
                            color =  colorResource(R.color.md_theme_outline),
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = value?:"",
                style = TextStyle(
                    fontSize = dimensionResource(R.dimen.sp_16).value.sp,
                    lineHeight = 30.08.sp,
                    fontFamily = FontFamily(Font(R.font.custom_font)),
                    fontWeight = FontWeight(600),
                    color = colorResource(id = R.color.snapshot_value),
                    textAlign = TextAlign.End,
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

        groupedBarChart.setGroupedBarData(group1, group2,group3,group4,ePS?.year, "Q1", "Q2","Q3","Q4")
    }

    private fun populateBarChart(customBarChart: CustomBarChart, ePS: EPSYear?) {
        try {
            if(!ePS?.year.isNullOrEmpty() && !ePS?.earningPerShare.isNullOrEmpty()) {
                customBarChart.setChartData(
                    ePS?.year?.reversed(),
                    ePS?.earningPerShare?.map { it.toFloat() }?.toList()?.reversed(),
                    0.5f
                )
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


}