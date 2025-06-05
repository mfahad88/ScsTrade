package com.example.scstrade.views.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivitySearchBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.snapshot.SnapshotActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : AppCompatActivity() {
    lateinit var binding:ActivitySearchBinding
    lateinit var sharedViewModel: SharedViewModel
    lateinit var adapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        binding=ActivitySearchBinding.inflate(LayoutInflater.from(this))
        sharedViewModel = (this.application as MyApp).viewModel

        setContentView(binding.root)

        adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1)
        binding.spinnerSector.adapter=adapter
        adapter.add("All Sector")
        sharedViewModel.mutableAllData.value?.data?.map { it.sN }?.forEach {
            adapter.add(it)
        }
        binding.searchText.addTextChangedListener {
            val search=it.toString()
          lifecycleScope.launch {
              val allData= withContext(Dispatchers.Default){
                  sharedViewModel.mutableAllData.value?.data?.filter {
                      if(!binding.spinnerSector.selectedItem.toString().equals("all sector",true)){
                          it.sN.equals(binding.spinnerSector.selectedItem.toString())
                      }else{
                          true
                      }
                  }?.filter {
                      if(!search.isNullOrEmpty()){
                          it.toString().startsWith(binding.searchText.text.toString(),true)|| it.toString().contains(binding.searchText.text.toString(),true)|| it.toString().equals(binding.searchText.text.toString(),true)
                      }else{
                          true
                      }
                  }
              }
              binding.main.setContent {
                  companyList(allData = allData)
              }
          }

        }
        binding.spinnerSector.onItemSelectedListener = object: OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                binding.searchText.text.clear()
                val allData=sharedViewModel.mutableAllData.value?.data?.filter {
                    if(!binding.spinnerSector.selectedItem.toString().equals("all sector",true)){
                        it.sN.equals(binding.spinnerSector.selectedItem.toString())
                    }else{
                        true
                    }
                }
                binding.main.setContent {
                    companyList(allData = allData)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }





    }
    @Composable
    public fun companyList(allData:List<StockItem>?){
        LazyColumn (modifier = Modifier.padding(horizontal = 15.dp)){
            items(allData?.size?:0){index->
                Column{
                    Row(modifier = Modifier
                        .padding(vertical = 15.dp)
                        .clickable {
                            val intent = Intent(binding.root.context, SnapshotActivity::class.java)
                            intent.putExtra(AppConstants.SYMBOL, allData?.get(index)?.sYM)
                            startActivity(intent)
                            finish()
                        }) {
                        Box (modifier = Modifier.weight(1f)){
                            Text(
                                text = allData?.get(index)?.sYM?:"",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 25.61.sp,
                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                    fontWeight = FontWeight(600),
                                    color = colorResource(id = R.color.md_theme_primary)
                                )
                            )
                        }

                        Box (modifier = Modifier.weight(2f)){
                            Text(
                                text = allData?.get(index)?.nM?:"",
                                style =TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 25.61.sp,
                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                    fontWeight = FontWeight(400),
                                    color = colorResource(id = R.color.black),
                                )
                            )
                        }
                    }
                    Divider(thickness = 1.dp, color = Color(0xFFC9C6C4))
                }
            }
        }
    }
}