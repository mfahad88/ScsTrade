package com.example.scstrade.views.detailquote
import androidx.compose.ui.res.dimensionResource

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityDetailQuoteBinding
import com.example.scstrade.databinding.ActivitySearchBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.snapshot.SnapshotActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailQuoteActivity : BaseActivity() {
    lateinit var binding: ActivityDetailQuoteBinding
    lateinit var sharedViewModel: SharedViewModel
    lateinit var adapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityDetailQuoteBinding.inflate(LayoutInflater.from(this))
        sharedViewModel = (this.application as MyApp).viewModel
        setContentView(binding.root)
        // binding.toolbar.toggleToolbar(false)
        binding.toolbar.binding.market.text = "Detail Quote"
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        binding.searchText.addTextChangedListener {
            val search=it.toString()
            lifecycleScope.launch {
                val allData= withContext(Dispatchers.Default){
                    sharedViewModel.mutableAllData.value?.data?.filter {
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

        if(binding.searchText.text.isNullOrEmpty()){
            lifecycleScope.launch {
                val allData= withContext(Dispatchers.Default){
                    sharedViewModel.mutableAllData.value?.data
                }
                binding.main.setContent {
                    companyList(allData = allData)
                }
            }
        }
    }



    @Composable
    public fun companyList(allData:List<StockItem>?){
        LazyColumn (modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.dp_15).value.dp)){
            items(allData?.size?:0){index->
                Column{
                    Row(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.dp_15).value.dp).clickable {
                        val intent= Intent(binding.root.context, SnapshotActivity::class.java)
                        intent.putExtra(AppConstants.SYMBOL, allData?.get(index)?.sYM)
                        startActivity(intent)
                        finish()
                    }) {

                        Box (modifier = Modifier.weight(1f)){
                            Text(
                                text = allData?.get(index)?.sYM?:"",
                                style = TextStyle(
                                    fontSize = dimensionResource(R.dimen.sp_16).value.sp,
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
                                style = TextStyle(
                                    fontSize = dimensionResource(R.dimen.sp_16).value.sp,
                                    lineHeight = 25.61.sp,
                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                    fontWeight = FontWeight(400),
                                    color = colorResource(R.color.black),
                                )
                            )
                        }
                    }
                    Divider(thickness = dimensionResource(R.dimen.dp_1).value.dp, color = Color(0xFFC9C6C4))
                }
            }
        }
    }

    override fun getResources(): Resources {

        val res = super.getResources()
        val config = Configuration(res.configuration)

        val metrics = res.displayMetrics

        // Calculate screen width and height in inches
        val widthInches = metrics.widthPixels / metrics.xdpi
        val heightInches = metrics.heightPixels / metrics.ydpi
        val diagonalInches = Math.sqrt((widthInches * widthInches + heightInches * heightInches).toDouble())

        // Set fontScale based on diagonal screen size
        if(diagonalInches>3.9 && diagonalInches<4.9){
            config.fontScale = 0.85f  // Small phones
        }else if (diagonalInches>4.9 && diagonalInches<5.4){
            config.fontScale = 0.95f
        }else if (diagonalInches>5.5 && diagonalInches<6.9){
            config.fontScale = 1.0f
        }else{
            config.fontScale = 1.2f
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            config.fontWeightAdjustment = 0

        }
        res.updateConfiguration(config, metrics)
        return res
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            // Override any incoming configuration changes
            overrideConfiguration.densityDpi = resources.displayMetrics.densityDpi
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }
}