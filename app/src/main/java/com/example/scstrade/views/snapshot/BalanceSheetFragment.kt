package com.example.scstrade.views.snapshot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.asFlow
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentBalanceSheetBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp


class BalanceSheetFragment : Fragment() {
    lateinit var sharedViewModel: SharedViewModel
    lateinit var binding: FragmentBalanceSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = (requireActivity().application as MyApp).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBalanceSheetBinding.inflate(inflater,container,false)
        sharedViewModel.yearsDetails(
            requireActivity().intent?.extras?.getString(AppConstants.SYMBOL) ?: ""
        )
        sharedViewModel.mutableYears.observe(viewLifecycleOwner, Observer { res ->

            if(res!=null){
                when (res.yearsDetails) {
                    is Resource.Error -> {
                        Utils.showError(binding.root,  "An error occurred")
                        binding.loader.visibility = View.GONE
                    }

                    is Resource.Loading -> {
                        binding.loader.visibility = View.VISIBLE
                        binding.main.visibility = View.GONE
                    }

                    is Resource.Success -> {
                        binding.loader.visibility = View.GONE
                        binding.main.visibility = View.VISIBLE
                        val result = res.yearsDetails
                        val  resultQuarter=res.quartersDetails

                        if(result.data?.isNotEmpty()?:false) {
                            binding.main.setContent {

                                Column {
                                    var selectedYear by remember {
                                        mutableStateOf(
                                            result.data?.map { it.yeartext }?.distinct()?.toList()
                                                ?.first()
                                        )
                                    }
                                    var selectedQuarter by remember {
                                        mutableStateOf(resultQuarter.data?.filter {
                                            it.yeartext.equals(
                                                selectedYear
                                            )
                                        }?.filter { it.quarterName.contains(selectedYear.toString()) }
                                            ?.map { it.quarterNumber }?.toList()?.first())
                                    }
                                    sharedViewModel.balanceSheet(
                                        requireActivity().intent.extras?.getString(
                                            AppConstants.SYMBOL
                                        ) ?: "", selectedYear ?: "2025", "quarter${selectedQuarter}"
                                    )
                                    sharedViewModel.distribution(
                                        requireActivity().intent.extras?.getString(
                                            AppConstants.SYMBOL
                                        ) ?: "", selectedYear ?: "2025", "quarter${selectedQuarter}"
                                    )
                                    Row {
                                        mDropdownMenu(
                                            150.dp,
                                            result.data?.map { it.yeartext }?.distinct()?.toList()
                                                ?: emptyList(), selectedYear
                                        ) {
                                            selectedYear = it
                                            selectedQuarter =
                                                resultQuarter.data?.filter { it.yeartext.equals(selectedYear) }
                                                    ?.map { it.quarterNumber }?.toList()?.first()
                                            sharedViewModel.incomeStatement(
                                                requireActivity().intent.extras?.getString(
                                                    AppConstants.SYMBOL
                                                ) ?: "",
                                                selectedYear ?: "2025",
                                                "quarter${selectedQuarter}"
                                            )
                                        }
                                        Spacer(modifier = Modifier.weight(1f))
                                        mDropdownMenu(
                                            180.dp,
                                            resultQuarter.data?.filter { it.yeartext.equals(selectedYear) }?.filter { it.quarterName.contains(selectedYear.toString()) }
                                                ?.map { it.quarterName }?.toList() ?: emptyList(),
                                            resultQuarter.data?.filter { it.yeartext.equals(selectedYear) }
                                                ?.filter { it.quarterNumber.equals(selectedQuarter) }?.map { it.quarterName }?.first()
                                        ) {selected->
                                            selectedQuarter = resultQuarter.data?.filter { it.yeartext.equals(selectedYear) }
                                                ?.filter { it.quarterName.equals(selected) }
                                                ?.map { it.quarterNumber }?.first()
                                            sharedViewModel.incomeStatement(
                                                requireActivity().intent.extras?.getString(
                                                    AppConstants.SYMBOL
                                                ) ?: "",
                                                selectedYear ?: "2025",
                                                "quarter${selectedQuarter}"
                                            )
                                        }
                                    }

                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(bottom = 30.dp)
                                            .verticalScroll(rememberScrollState())
                                    ) {

                                        Text(
                                            modifier = Modifier.padding(
                                                start = 15.dp,
                                                end = 15.dp,
                                                bottom = 10.dp
                                            ),
                                            text = "Balance Sheet",
                                            style = TextStyle(
                                                fontSize = 18.sp,
                                                lineHeight = 27.sp,
                                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                                fontWeight = FontWeight(700),
                                                color = colorResource(id = R.color.md_theme_primary),
                                            )
                                        )
                                        Card(
                                            modifier = Modifier.padding(horizontal = 15.dp),
                                            border = BorderStroke(1.dp, color = colorResource(id = R.color.md_theme_surfaceContainerHighest)),
                                            backgroundColor = colorResource(id = R.color.md_theme_surfaceBright),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            val balanceSheet =
                                            sharedViewModel.mutableBalanceSheet.asFlow()
                                                .collectAsState(
                                                    initial = Resource.Loading()
                                                ).value
                                            Column {
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(45.dp)
                                                        .background(
                                                            colorResource(id = R.color.colorDarkerr),
                                                            shape = RoundedCornerShape(
                                                                topStart = 12.dp,
                                                                topEnd = 12.dp,
                                                                bottomStart = 0.dp,
                                                                bottomEnd = 0.dp
                                                            )
                                                        )
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(
                                                            start = 15.dp,
                                                            end = 15.dp,
                                                            top = 10.dp,
                                                            bottom = 10.dp
                                                        )
                                                    ) {
                                                        Box(
                                                            contentAlignment = Alignment.CenterStart,
                                                            modifier = Modifier
                                                                .weight(1f)
                                                        ) {

                                                            Text(
                                                                text = "Year/Quarter",
                                                                style = TextStyle(
                                                                    fontSize = 16.sp,
                                                                    lineHeight = 30.08.sp,
                                                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                                                    fontWeight = FontWeight(700),
                                                                    color = Color(0xFFFFFFFF),

                                                                    )
                                                            )
                                                        }
                                                        Box(
                                                            contentAlignment = Alignment.CenterEnd,
                                                            modifier = Modifier
                                                                .weight(1f)
                                                        ) {

                                                            Text(
                                                                text = "${selectedYear}/Q${selectedQuarter}",
                                                                style = TextStyle(
                                                                    fontSize = 16.sp,
                                                                    lineHeight = 30.08.sp,
                                                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                                                    fontWeight = FontWeight(700),
                                                                    color = Color(0xFFFFFFFF),

                                                                    )
                                                            )
                                                        }
                                                    }
                                                }
                                                if (balanceSheet.data?.isEmpty() == true) {
                                                    Column(modifier = Modifier.padding(10.dp)) {
                                                        Text(
                                                            text = "No Record Found...",
                                                            style = TextStyle(
                                                                fontSize = 22.sp,
                                                                lineHeight = 30.08.sp,
                                                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                                                fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.colorDarkerr),
                                                            )
                                                        )
                                                    }

                                                } else {
                                                    Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                                                        val sheet = balanceSheet.data?.first()

                                                        val balanceItems = listOf(
                                                            "Cash" to sheet?.cash,
                                                            "Inventory" to sheet?.inventory,
                                                            "Current Asset" to sheet?.currentAsset,
                                                            "Investments" to sheet?.investments,
                                                            "Fixed Asset" to sheet?.fixedAsset,
                                                            "Total Assets" to sheet?.totalAssets,
                                                            "Current Liability" to sheet?.currentLiability,
                                                            "Fixed Liability" to sheet?.fixedLiability,
                                                            "Total Liabilities" to sheet?.totalLiabilities,
                                                            "Paid Up Capital" to sheet?.paidUpCapital,
                                                            "Total Equity" to sheet?.totalEquity
                                                        )

                                                        Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                                                            for ((label, value) in balanceItems) {
                                                                if (value != null && value > 0) {
                                                                    cardItem(label, Utils.commaFormat(value))
                                                                    Divider(
                                                                        thickness = 1.dp,
                                                                        color = colorResource(id = R.color.md_theme_surfaceContainerHighest)
                                                                    )
                                                                }
                                                            }
                                                        }
//                                                        cardItem(
//                                                            "Cash",
//                                                            Utils.commaFormat(balanceSheet.data?.first()?.cash)
//                                                        )
//                                                        Divider(
//                                                            thickness = 1.dp,
//                                                            color = colorResource(id = R.color.md_theme_surfaceContainerHighest)
//                                                        )
//                                                        cardItem(
//                                                            "Inventory",
//                                                            Utils.commaFormat(balanceSheet.data?.first()?.inventory)
//                                                        )
//                                                        Divider(
//                                                            thickness = 1.dp,
//                                                            color = colorResource(id = R.color.md_theme_surfaceContainerHighest)
//                                                        )
//                                                        cardItem(
//                                                            "Current Asset",
//                                                            Utils.commaFormat(balanceSheet.data?.first()?.currentAsset)
//                                                        )
//                                                        Divider(
//                                                            thickness = 1.dp,
//                                                            color = colorResource(id = R.color.md_theme_surfaceContainerHighest)
//                                                        )
//                                                        cardItem(
//                                                            "Investments",
//                                                            Utils.commaFormat(balanceSheet.data?.first()?.investments)
//                                                        )
//                                                        Divider(
//                                                            thickness = 1.dp,
//                                                            color = colorResource(id = R.color.md_theme_surfaceContainerHighest)
//                                                        )
//                                                        cardItem(
//                                                            "Fixed Asset",
//                                                            Utils.commaFormat(balanceSheet.data?.first()?.fixedAsset)
//                                                        )
//                                                        Divider(
//                                                            thickness = 1.dp,
//                                                            color = colorResource(id = R.color.md_theme_surfaceContainerHighest)
//                                                        )
//                                                        cardItem(
//                                                            "Total Assets",
//                                                            Utils.commaFormat(balanceSheet.data?.first()?.totalAssets)
//                                                        )
//                                                        Divider(
//                                                            thickness = 1.dp,
//                                                            color = colorResource(id = R.color.md_theme_surfaceContainerHighest)
//                                                        )
//                                                        cardItem(
//                                                            "Current Liability",
//                                                            Utils.commaFormat(balanceSheet.data?.first()?.currentLiability)
//                                                        )
//                                                        Divider(
//                                                            thickness = 1.dp,
//                                                            color = colorResource(id = R.color.md_theme_surfaceContainerHighest)
//                                                        )
//                                                        cardItem(
//                                                            "Fixed Liability",
//                                                            Utils.commaFormat(balanceSheet.data?.first()?.fixedLiability)
//                                                        )
//                                                        Divider(
//                                                            thickness = 1.dp,
//                                                            color = colorResource(id = R.color.md_theme_surfaceContainerHighest)
//                                                        )
//                                                        cardItem(
//                                                            key = "Total Liabilities",
//                                                            value = Utils.commaFormat(balanceSheet.data?.first()?.totalLiabilities)
//                                                        )
//                                                        Divider(
//                                                            thickness = 1.dp,
//                                                            color = colorResource(id = R.color.md_theme_surfaceContainerHighest)
//                                                        )
//                                                        cardItem(
//                                                            key = "Paid Up Capital",
//                                                            value = Utils.commaFormat(balanceSheet.data?.first()?.paidUpCapital)
//                                                        )
//                                                        Divider(
//                                                            thickness = 1.dp,
//                                                            color = colorResource(id = R.color.md_theme_surfaceContainerHighest)
//                                                        )
//                                                        cardItem(
//                                                            key = "Total Equity",
//                                                            value = Utils.commaFormat(balanceSheet.data?.first()?.totalEquity)
//                                                        )
                                                    }
                                                }

                                            }
                                        }

                                        Text(
                                            modifier = Modifier.padding(
                                                horizontal = 15.dp,
                                                vertical = 15.dp
                                            ),
                                            text = "Distributions",
                                            style = TextStyle(
                                                fontSize = 18.sp,
                                                lineHeight = 27.sp,
                                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                                fontWeight = FontWeight(700),
                                                color = colorResource(id = R.color.md_theme_primary),
                                            )
                                        )

                                        Card(
                                            modifier = Modifier.padding(horizontal = 15.dp),
                                            border = BorderStroke(1.dp, color = colorResource(id = R.color.md_theme_surfaceContainerHighest)),
                                            backgroundColor = colorResource(id = R.color.md_theme_surfaceBright),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            val distribution =
                                                sharedViewModel.mutableDistribution.asFlow()
                                                    .collectAsState(
                                                        initial = Resource.Loading()
                                                    ).value.data
                                            Column {
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(45.dp)
                                                        .background(
                                                            colorResource(id = R.color.colorDarkerr),
                                                            shape = RoundedCornerShape(
                                                                topStart = 12.dp,
                                                                topEnd = 12.dp,
                                                                bottomStart = 0.dp,
                                                                bottomEnd = 0.dp
                                                            )
                                                        )
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(
                                                            start = 15.dp,
                                                            end = 15.dp,
                                                            top = 10.dp,
                                                            bottom = 10.dp
                                                        )
                                                    ) {
                                                        Box(
                                                            contentAlignment = Alignment.CenterStart,
                                                            modifier = Modifier
                                                                .weight(1f)
                                                        ) {

                                                            Text(
                                                                text = "Year/Quarter",
                                                                style = TextStyle(
                                                                    fontSize = 16.sp,
                                                                    lineHeight = 30.08.sp,
                                                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                                                    fontWeight = FontWeight(700),
                                                                    color = Color(0xFFFFFFFF),

                                                                    )
                                                            )
                                                        }
                                                        Box(
                                                            contentAlignment = Alignment.CenterEnd,
                                                            modifier = Modifier
                                                                .weight(1f)
                                                        ) {

                                                            Text(
                                                                text = "${selectedYear}/Q${selectedQuarter}",
                                                                style = TextStyle(
                                                                    fontSize = 16.sp,
                                                                    lineHeight = 30.08.sp,
                                                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                                                    fontWeight = FontWeight(700),
                                                                    color = Color(0xFFFFFFFF),

                                                                    )
                                                            )
                                                        }
                                                    }
                                                }
                                                if (distribution?.isEmpty() == true) {
                                                    Column(modifier = Modifier.padding(10.dp)) {
                                                        Text(
                                                            text = "No Record Found...",
                                                            style = TextStyle(
                                                                fontSize = 22.sp,
                                                                lineHeight = 30.08.sp,
                                                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                                                fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.colorDarkerr),
                                                            )
                                                        )
                                                    }

                                                } else {
                                                    Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                                                        cardItem(
                                                            "Dividend",
                                                            Utils.commaFormat(distribution?.first()?.dividend)
                                                        )
                                                        Divider(
                                                            thickness = 1.dp,
                                                            color = colorResource(id = R.color.md_theme_surfaceContainerHighest)
                                                        )
                                                        cardItem(
                                                            "Bonus",
                                                            Utils.commaFormat(distribution?.first()?.bonus)
                                                        )
                                                        Divider(
                                                            thickness = 1.dp,
                                                            color = colorResource(id = R.color.md_theme_surfaceContainerHighest)
                                                        )
                                                        cardItem(
                                                            "Right",
                                                            Utils.commaFormat(distribution?.first()?.right)
                                                        )
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            Utils.showError(requireView(), getString(R.string.no_record_found))
                        }
                    }
                }
            }
        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.mutableYears.value=null
    }
    @Composable
    private fun cardItem(key: String, value: String) {
        Row {
            Box(modifier = Modifier
                .weight(1f)
                .padding(vertical = 10.dp)) {

                Text(
                    text = key,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 30.08.sp,
                        fontFamily = FontFamily(Font(R.font.custom_font)),
                        fontWeight = FontWeight(500),
                        color = colorResource(id = R.color.black),
                    )
                )
            }

            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp)) {

                Text(
                    text = value,
                    textAlign = TextAlign.End,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 30.08.sp,
                        fontFamily = FontFamily(Font(R.font.custom_font)),
                        fontWeight = FontWeight(500),
                        color = colorResource(id = R.color.black),
                    )
                )
            }
        }

    }

    @Composable
    fun mDropdownMenu(width: Dp, list: List<String>, selectedOption:String?, onClick: (String) -> Unit) {
        var expanded by remember { mutableStateOf(false) }
        /*  var selectedText by remember {
              mutableStateOf(selectedOption)
          }*/
        Box(
            modifier = Modifier
                .padding(16.dp)
                .width(width)
                .border(1.dp, Color.Gray, shape = RoundedCornerShape(10f))
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Text(text = selectedOption?:"", style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontFamily = FontFamily(Font(R.font.custom_font)),
                    fontWeight = FontWeight(400),
                    color = colorResource(id = R.color.black),
                ),
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(painter = painterResource(id = R.drawable.drop_down), modifier = Modifier.size(14.dp), tint = colorResource(id = R.color.black), contentDescription = "More options")
                }

            }
            DropdownMenu(
                expanded = expanded,
                modifier = Modifier.background(color = colorResource(id = R.color.dropdown)),
                onDismissRequest = { expanded = false }
            ) {
                list.forEach {
                    DropdownMenuItem(onClick = {
                        expanded=!expanded
//                        selectedText=it
                        onClick(it)
                    }) {
                        Text(
                            text = it,
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                fontWeight = FontWeight(400),
                                color = colorResource(id = R.color.black),
                            )
                        )
                    }
                }

            }
        }
    }
}