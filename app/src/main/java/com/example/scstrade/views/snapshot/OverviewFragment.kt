package com.example.scstrade.views.snapshot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentOverviewBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.snapshot.Overview
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
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
                    binding.labelTextIndex.text = item?.iN?.substring(0,item?.iN?.indexOf("|")?:0)
                    binding.volumeValue.text = Utils.commaFormat(item?.v?.toDouble())
                    binding.avgVolumeValue.text = Utils.commaFormat(result.data?.avgVolume12M?.toDouble())
                    binding.marketCapValue.text = Utils.commaFormat(result.data?.marketCap?.toDouble())
                    binding.companyName.text = item?.nM
                    binding.sector.text = item?.sN
                    binding.ratios.setContent {
                        populateRatios(data = result.data)
                    }

                }
            }
        })

        populateBarChart()
        populateGroupBarChart()
        populateCombinedChart()
        populateMultiLineChart()
        return binding.root
    }
    @Composable
    private fun populateRatios(data: Overview?) {
        Card(
            modifier = Modifier
                .background(color = colorResource(id = R.color.md_theme_surfaceBright))
                .fillMaxSize(),
            border = BorderStroke(1.dp, Color(0xFFE5E2E1)),
            shape = RoundedCornerShape(10.dp),
        ) {
            Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
                ItemValue("Paid Up Capital:","43,009.28 mn")
                Row{
                    Divider(
                        thickness = 1.dp,
                        color = Color(0xFFE5E2E1),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                ItemValue("Paid Up Capital:","43,009.28 mn")
                Row{
                    Divider(
                        thickness = 1.dp,
                        color = Color(0xFFE5E2E1),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                ItemValue("Paid Up Capital:","43,009.28 mn")
                Row{
                    Divider(
                        thickness = 1.dp,
                        color = Color(0xFFE5E2E1),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                ItemValue("Paid Up Capital:","43,009.28 mn")
                Row{
                    Divider(
                        thickness = 1.dp,
                        color = Color(0xFFE5E2E1),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                ItemValue("Paid Up Capital:","43,009.28 mn")
                Row{
                    Divider(
                        thickness = 1.dp,
                        color = Color(0xFFE5E2E1),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun ItemValue(key:String,value:String) {
        Row {
            Text(
                text = key,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 30.08.sp,
                    fontFamily = FontFamily(Font(R.font.custom_font)),
                    fontWeight = FontWeight(500),
                    color = colorResource(id = R.color.colorDarkerr),
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = value,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 30.08.sp,
                    fontFamily = FontFamily(Font(R.font.custom_font)),
                    fontWeight = FontWeight(700),
                    color = colorResource(id = R.color.colorDarkerr),
                    textAlign = TextAlign.Right,
                )
            )
        }
    }

    private fun populateMultiLineChart() {

        // Sample Labels
        val labels: List<String> = mutableListOf("Jan", "Feb", "Mar", "Apr")
        val greenLineEntries : MutableList<Entry> = ArrayList()
        greenLineEntries.add(Entry(0f, 400f))
        greenLineEntries.add(Entry(1f, 600f))
        greenLineEntries.add(Entry(2f, 750f))
        greenLineEntries.add(Entry(3f, 650f))


        // Blue Line Data
        val blueLineEntries: MutableList<Entry> = ArrayList()
        blueLineEntries.add(Entry(0f, 500f))
        blueLineEntries.add(Entry(1f, 700f))
        blueLineEntries.add(Entry(2f, 800f))
        blueLineEntries.add(Entry(3f, 600f))


        // Black Line Data
        val blackLineEntries: MutableList<Entry> = ArrayList()
        blackLineEntries.add(Entry(0f, 450f))
        blackLineEntries.add(Entry(1f, 680f))
        blackLineEntries.add(Entry(2f, 780f))
        blackLineEntries.add(Entry(3f, 620f))

        binding.multilineChart.setChartData(labels, greenLineEntries, blueLineEntries, blackLineEntries);

    }

    private fun populateCombinedChart() {
        val barValues: MutableList<Float> = ArrayList()
        barValues.add(178.95f)
        barValues.add(203.54f)
        barValues.add(251.78f)
        barValues.add(290.75f)
        barValues.add(300.26f)

        val lineValues: MutableList<Float> = ArrayList()
        lineValues.add(290.6f)
        lineValues.add(300.4f)
        lineValues.add(100.3f)
        lineValues.add(350.45f)
        lineValues.add(150.5f)

        val labels: MutableList<String> = ArrayList()
        labels.add("2021")
        labels.add("2022")
        labels.add("2023")
        labels.add("2024")
        labels.add("2025")

        binding.combineChart.setChartData(barValues, lineValues, labels)
    }

    private fun populateGroupBarChart() {
        val group1: MutableList<BarEntry> = ArrayList()
        val group2: MutableList<BarEntry> = ArrayList()
        val group3: MutableList<BarEntry> = ArrayList()
        group1.add(BarEntry(0f, 10f))
        group1.add(BarEntry(1f, 20f))
        group1.add(BarEntry(2f, 30f))

        group2.add(BarEntry(0f, 15f))
        group2.add(BarEntry(1f, 25f))
        group2.add(BarEntry(2f, 35f))

        group3.add(BarEntry(0f, 15f))
        group3.add(BarEntry(1f, 25f))
        group3.add(BarEntry(2f, 35f))
        binding.groupBarChart.setGroupedBarData(group1, group2,group3, "Q1", "Q2","Q3")
    }

    private fun populateBarChart() {
        val labels: MutableList<String> = ArrayList()
        labels.add("Jan")
        labels.add("Feb")
        labels.add("Mar")
        labels.add("Apr")

        val values: MutableList<Float> = ArrayList()
        values.add(200f)
        values.add(400f)
        values.add(600f)
        values.add(800f)

        binding.customBarChart.setChartData(labels,values,0.5f,ContextCompat.getColor(requireContext(), R.color.md_theme_primary))

    }


}