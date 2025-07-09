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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.asFlow
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentIncomeStatementBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.incomestatement.IncomeStatementDataItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import kotlin.reflect.full.memberProperties


class IncomeStatementFragment : Fragment() {
    lateinit var sharedViewModel: SharedViewModel
    lateinit var binding: FragmentIncomeStatementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = (requireActivity().application as MyApp).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIncomeStatementBinding.inflate(inflater, container, false)
        sharedViewModel.yearsDetails(
            requireActivity().intent?.extras?.getString(AppConstants.SYMBOL) ?: ""
        )
        sharedViewModel.mutableYears.observe(viewLifecycleOwner, Observer { res ->

          if(res!=null){
              when (res.yearsDetails) {
                  is Resource.Error -> {
                      Utils.showError(binding.root, "An error occurred")
                      binding.loader.visibility = View.GONE
                  }

                  is Resource.Loading -> {
                      binding.loader.visibility = View.VISIBLE
                      binding.main.visibility = View.GONE
                  }

                  is Resource.Success -> {
                      val result=res.yearsDetails
                      val resultQuarter=res.quartersDetails

                      if(result.data?.isNotEmpty()?:false){

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
                                      mDropdownMenu(150.dp,
                                          result.data?.map { it.yeartext }?.distinct()?.toList()
                                              ?: emptyList(), selectedYear
                                      ) {
                                          selectedYear = it
                                          selectedQuarter =
                                              resultQuarter.data?.filter { it.yeartext.equals(selectedYear) }
                                                  ?.filter { it.quarterName.contains(selectedYear.toString()) }
                                                  ?.map { it.quarterNumber }?.toList()?.first()
                                          sharedViewModel.incomeStatement(
                                              requireActivity().intent.extras?.getString(
                                                  AppConstants.SYMBOL
                                              ) ?: "", selectedYear ?: "2025", "quarter${selectedQuarter}"
                                          )
                                      }
                                      Spacer(modifier = Modifier.weight(1f))
                                      mDropdownMenu(200.dp,
                                          resultQuarter.data?.filter { it.yeartext.equals(selectedYear) }
                                              ?.filter { it.quarterName.contains(selectedYear.toString()) }
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
                                              ) ?: "", selectedYear ?: "2025", "quarter${selectedQuarter}"
                                          )
                                      }
                                  }

                                  Card(
                                      modifier = Modifier.padding(horizontal = 15.dp),
                                      border = BorderStroke(
                                          1.dp, color = colorResource(
                                              id = R.color.md_theme_surfaceContainerHighest
                                          )
                                      ),
                                      shape = RoundedCornerShape(12.dp),
                                      backgroundColor = colorResource(id = R.color.md_theme_surfaceBright),
                                  ) {
                                      val res =
                                          sharedViewModel.mutableIncomeStatement.asFlow().collectAsState(
                                              initial = Resource.Loading()
                                          ).value

                                      when(res){
                                          is Resource.Error -> {

                                          }
                                          is Resource.Loading -> {

                                          }
                                          is Resource.Success -> {
                                              val incomeStatement=res.data
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
                                                  if(incomeStatement?.isNotEmpty() == true) {
                                                      Column (modifier = Modifier.padding(horizontal = 15.dp)){
                                                          val statement = incomeStatement?.first()

                                                          val financialItems = listOf(
                                                              "Sales" to statement?.sales,
                                                              "Cost Of Sales" to statement?.costOfSales,
                                                              "Gross Profit" to statement?.grossProfit,
                                                              "Operating Profit" to statement?.operatingProfit,
                                                              "Other Income" to statement?.otherIncome,
                                                              "Finance Cost" to statement?.financeCost,
                                                              "Profit Before Tax" to statement?.profitBeforeTax,
                                                              "Taxation" to statement?.taxation,
                                                              "Profit After Tax" to statement?.profitAfterTax
                                                          )

                                                          for ((label, value) in financialItems) {
                                                              if (value != null && value > 0) {
                                                                  Divider(thickness = 1.dp, color = colorResource(id = R.color.md_theme_surfaceContainerHighest))
                                                                  cardItem(label, Utils.commaFormat(value))
                                                              }
                                                          }

//                                                          cardItem("Sales", Utils.commaFormat(incomeStatement?.first()?.sales))
//                                                          Divider(thickness = 1.dp, color = colorResource(id = R.color.md_theme_surfaceContainerHighest))
//                                                          cardItem("Cost Of Sales", Utils.commaFormat(incomeStatement?.first()?.costOfSales))
//                                                          Divider(thickness = 1.dp, color = colorResource(id = R.color.md_theme_surfaceContainerHighest))
//                                                          cardItem("Gross Profit", Utils.commaFormat(incomeStatement?.first()?.grossProfit))
//                                                          Divider(thickness = 1.dp, color = colorResource(id = R.color.md_theme_surfaceContainerHighest))
//                                                          cardItem("Operating Profit", Utils.commaFormat(incomeStatement?.first()?.operatingProfit))
//                                                          Divider(thickness = 1.dp, color = colorResource(id = R.color.md_theme_surfaceContainerHighest))
//                                                          cardItem("Other Income", Utils.commaFormat(incomeStatement?.first()?.otherIncome))
//                                                          Divider(thickness = 1.dp, color = colorResource(id = R.color.md_theme_surfaceContainerHighest))
//                                                          cardItem("Finance Cost", Utils.commaFormat(incomeStatement?.first()?.financeCost))
//                                                          Divider(thickness = 1.dp, color = colorResource(id = R.color.md_theme_surfaceContainerHighest))
//                                                          cardItem("Profit Before Tax", Utils.commaFormat(incomeStatement?.first()?.profitBeforeTax))
//                                                          Divider(thickness = 1.dp, color = colorResource(id = R.color.md_theme_surfaceContainerHighest))
//                                                          cardItem("Taxation", Utils.commaFormat(incomeStatement?.first()?.taxation))
//                                                          Divider(thickness = 1.dp, color = colorResource(id = R.color.md_theme_surfaceContainerHighest))
//                                                          cardItem(key = "Profit After Tax", value = Utils.commaFormat(incomeStatement?.first()?.profitAfterTax))
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
                                                                  color = colorResource(id = R.color.black),
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
                          binding.loader.visibility = View.GONE
                          binding.main.visibility = View.VISIBLE
                      }else{
                          Utils.showError(requireView(), getString(R.string.no_record_found))
                          binding.loader.visibility = View.GONE
                          binding.main.visibility = View.VISIBLE
                      }
                  }
              }
          }
        })
        return binding.root
    }

    @Composable
    private fun cardItem(key: String, value: String) {
        Row (modifier = Modifier.fillMaxWidth()){
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp)) {

                Text(
                    text = key,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
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
                    modifier = Modifier.fillMaxWidth(),
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
                horizontalArrangement = Arrangement.spacedBy(10.dp),
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

    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.mutableYears.value=null

    }
}