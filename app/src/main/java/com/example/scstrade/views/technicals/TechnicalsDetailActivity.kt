package com.example.scstrade.views.technicals
import androidx.compose.ui.res.dimensionResource

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityTechnicalsDetailBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.snapshot.SnapshotActivity
import com.example.scstrade.views.technicals.adapter.TechnicalDetailAdapter
import com.example.scstrade.views.widgets.VerticalSpaceItemDecoration

class TechnicalsDetailActivity : BaseActivity() {
    lateinit var binding: ActivityTechnicalsDetailBinding
    lateinit var viewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTechnicalsDetailBinding.inflate(LayoutInflater.from(this))
        viewModel = (this.application as MyApp).viewModel
        enableEdgeToEdge()
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        setContentView(binding.root)
        // binding.toolbar.toggleToolbar(false)
        binding.toolbar.binding.market.text = "Technicals"
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel.getTechnicalDetail(intent.extras?.getString(AppConstants.TECHNICAL_SELECTION)?:"")

        viewModel.mutableTechnicalDetail.observe(this, Observer {
            when(it){
                is Resource.Error -> {
                    binding.loader.visibility = View.GONE
                    Utils.showError(binding.root,it.message?:"An error occurred")
                }
                is Resource.Loading -> binding.loader.visibility=View.VISIBLE
                is Resource.Success -> {

                    val jsonElement=it.data

                    if(jsonElement?.isJsonArray?:false){
                        var count=0
                        val resultList = mutableListOf<Array<String>>()
                        jsonElement?.asJsonArray?.first()?.asJsonObject?.entrySet()?.distinctBy { it.key }?.forEach {
                            if(!it.key.equals("company_name",true)) {
                                count++
                                if(count==1){
                                    binding.header1.text = it.key
                                }
                                if(count==2){
                                    binding.header2.text = it.key
                                }
                                if(count==3){
                                    binding.header3.text = it.key
                                }
                                if(count==4){
                                    binding.header4.text = it.key
                                }

                                System.out.println(it.key)
                            }
                        }
                        jsonElement?.asJsonArray?.forEach { it ->
                            val obj = it.asJsonObject
//                               val map = Gson().fromJson(jsonElement, Map::class.java) as Map<String, Any>
                            val values = obj.entrySet().map { it.value.asString }.toTypedArray()
                            resultList.add(values)


                        }
                        binding.recyclerView.apply {
                            adapter = TechnicalDetailAdapter(resultList,viewModel){
                                val intent= Intent(this@TechnicalsDetailActivity, SnapshotActivity::class.java)
                                intent.putExtra(AppConstants.SYMBOL, it)
                                startActivity(intent)
                            }

                            doOnPreDraw {
                                Log.d("TAG", "RecyclerView visible rows are rendered")
                                binding.groupMain.visibility = View.VISIBLE
                                binding.loader.visibility = View.GONE
                            }
                        }


                    }


                }
            }
        })

        binding.recyclerView.apply {
            layoutManager=LinearLayoutManager(this@TechnicalsDetailActivity,LinearLayoutManager.VERTICAL,false)
            addItemDecoration(VerticalSpaceItemDecoration(1,ContextCompat.getColor(this@TechnicalsDetailActivity,R.color.md_theme_outline)))
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