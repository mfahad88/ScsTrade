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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.asFlow
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentIncomeStatementBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [IncomeStatementFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IncomeStatementFragment : Fragment() {
    lateinit var sharedViewModel: SharedViewModel
    lateinit var binding: FragmentIncomeStatementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = (requireActivity().application as MyApp).viewModel
        sharedViewModel.yearsDetails(
            requireActivity().intent?.extras?.getString(AppConstants.SYMBOL) ?: ""
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIncomeStatementBinding.inflate(inflater, container, false)
        sharedViewModel.mutableYears.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Error -> {
                    Utils.showError(binding.root, result.message ?: "An error occurred")
                    binding.loader.visibility = View.GONE
                }

                is Resource.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                    binding.main.visibility = View.GONE
                }

                is Resource.Success -> {
                    binding.loader.visibility = View.GONE
                    binding.main.visibility = View.VISIBLE
                    binding.main.setContent {

                        Column {
                            var selectedYear by remember {
                                mutableStateOf(
                                    result.data?.map { it.yeartext }?.distinct()?.toList()?.first()
                                )
                            }
                            var selectedQuarter by remember {
                                mutableStateOf(result.data?.filter { it.yeartext.equals(selectedYear) }
                                    ?.map { it.quarterNumber }?.toList()?.first())
                            }
                            sharedViewModel.incomeStatement(
                                requireActivity().intent.extras?.getString(
                                    AppConstants.SYMBOL
                                ) ?: "", selectedYear ?: "2025", "quarter${selectedQuarter}"
                            )
                            Row {
                                mDropdownMenu(
                                    result.data?.map { it.yeartext }?.distinct()?.toList()
                                        ?: emptyList(), selectedYear
                                ) {
                                    selectedYear = it
                                    selectedQuarter =
                                        result.data?.filter { it.yeartext.equals(selectedYear) }
                                            ?.map { it.quarterNumber }?.toList()?.first()
                                    sharedViewModel.incomeStatement(
                                        requireActivity().intent.extras?.getString(
                                            AppConstants.SYMBOL
                                        ) ?: "", selectedYear ?: "2025", "quarter${selectedQuarter}"
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                mDropdownMenu(
                                    result.data?.filter { it.yeartext.equals(selectedYear) }
                                        ?.map { it.quarterNumber }?.toList() ?: emptyList(),
                                    selectedQuarter
                                ) {
                                    selectedQuarter = it
                                    sharedViewModel.incomeStatement(
                                        requireActivity().intent.extras?.getString(
                                            AppConstants.SYMBOL
                                        ) ?: "", selectedYear ?: "2025", "quarter${selectedQuarter}"
                                    )
                                }
                            }

                            Card(modifier = Modifier.padding(horizontal = 15.dp), border = BorderStroke(1.dp, color = Color(0xFFE5E2E1)), shape = RoundedCornerShape(12.dp)) {
                                val incomeStatement =
                                    sharedViewModel.mutableIncomeStatement.asFlow().collectAsState(
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
                                                top = 10.dp,
                                                bottom = 10.dp
                                            )
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
                                            Spacer(modifier = Modifier.width(90.dp))
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
                                    if(incomeStatement?.isNotEmpty() == true) {
                                        Column (modifier = Modifier.padding(horizontal = 15.dp)){
                                            cardItem("Cash", Utils.commaFormat(incomeStatement?.first()?.cash))
                                            Divider(thickness = 1.dp, color = Color(0xFFE5E2E1))
                                            cardItem("Current Asset", Utils.commaFormat(incomeStatement?.first()?.currentAsset))
                                            Divider(thickness = 1.dp, color = Color(0xFFE5E2E1))
                                            cardItem("Current Liability", Utils.commaFormat(incomeStatement?.first()?.currentLiability))
                                            Divider(thickness = 1.dp, color = Color(0xFFE5E2E1))
                                            cardItem("Fixed Asset", Utils.commaFormat(incomeStatement?.first()?.fixedAsset))
                                            Divider(thickness = 1.dp, color = Color(0xFFE5E2E1))
                                            cardItem("Fixed Liability", Utils.commaFormat(incomeStatement?.first()?.fixedLiability))
                                            Divider(thickness = 1.dp, color = Color(0xFFE5E2E1))
                                            cardItem("Inventory", Utils.commaFormat(incomeStatement?.first()?.inventory))
                                            Divider(thickness = 1.dp, color = Color(0xFFE5E2E1))
                                            cardItem("Investments", Utils.commaFormat(incomeStatement?.first()?.investments))
                                            Divider(thickness = 1.dp, color = Color(0xFFE5E2E1))
                                            cardItem("Paid Up Capital", Utils.commaFormat(incomeStatement?.first()?.paidUpCapital))
                                            Divider(thickness = 1.dp, color = Color(0xFFE5E2E1))
                                            cardItem(key = "Total Assets", value = Utils.commaFormat(incomeStatement?.first()?.totalAssets))
                                            Divider(thickness = 1.dp, color = Color(0xFFE5E2E1))
                                            cardItem(key = "Total Equity", value = Utils.commaFormat(incomeStatement?.first()?.totalEquity))
                                            Divider(thickness = 1.dp, color = Color(0xFFE5E2E1))
                                            cardItem(key = "Total Liabilities", value = Utils.commaFormat(incomeStatement?.first()?.totalLiabilities))
                                        }
                                    }else{
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
                                    }
                                }
                            }
                        }
                    }
                }
            }
        })
        return binding.root
    }

    @Composable
    private fun cardItem(key: String, value: String) {
        Row {
            Box(modifier = Modifier.weight(1f).padding(vertical = 10.dp)) {

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
            }

            Box(modifier = Modifier.weight(1f).padding(vertical = 10.dp)) {

                Text(
                    text = value,
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 30.08.sp,
                        fontFamily = FontFamily(Font(R.font.custom_font)),
                        fontWeight = FontWeight(500),
                        color = colorResource(id = R.color.colorDarkerr),
                    )
                )
            }
        }

    }

    @Composable
    fun mDropdownMenu(list: List<String>,selectedOption:String?, onClick: (String) -> Unit) {
        var expanded by remember { mutableStateOf(false) }
        /*  var selectedText by remember {
              mutableStateOf(selectedOption)
          }*/
        Box(
            modifier = Modifier
                .padding(16.dp)
                .width(150.dp)
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
                    color = colorResource(id = R.color.colorDarkerr),
                ),
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(painter = painterResource(id = R.drawable.drop_down), modifier = Modifier.size(18.dp), tint = colorResource(id = R.color.colorDarkerr), contentDescription = "More options")
                }

            }
            DropdownMenu(
                expanded = expanded,
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
                                color = colorResource(id = R.color.colorDarkerr),
                            )
                        )
                    }
                }

            }
        }
    }
}