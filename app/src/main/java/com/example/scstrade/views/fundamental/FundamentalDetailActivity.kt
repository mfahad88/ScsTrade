package com.example.scstrade.views.fundamental

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityFundamentalDetailBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.technicals.TechnicalDetailAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FundamentalDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityFundamentalDetailBinding
    lateinit var viewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (this.application as MyApp).viewModel
        binding = ActivityFundamentalDetailBinding.inflate(LayoutInflater.from(this))
        enableEdgeToEdge()
        setContentView(R.layout.activity_fundamental_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel.getFundamentalDetail(intent.extras?.getString(AppConstants.TECHNICAL_SELECTION)?:"")

        viewModel.mutableFundamentalDetail.observe(this, Observer {
            when(it){
                is Resource.Error -> {
                    binding.loader.visibility = View.GONE
                    Utils.showError(binding.root,it.message?:"An error occurred")
                }
                is Resource.Loading -> binding.loader.visibility=View.VISIBLE
                is Resource.Success -> {
                    binding.loader.visibility = View.GONE
                    binding.recyclerView.apply {
                        adapter = FundamentalDetailAdapter(it.data?: emptyList()){

                        }
                        layoutManager= LinearLayoutManager(this@FundamentalDetailActivity,
                            LinearLayoutManager.VERTICAL,false)
                    }
                }
            }
        })

        viewModel.mutableIndices.observe(this, Observer {
            updateMarket(it)
        })
    }

    private fun updateMarket(it: Resource<List<KSEIndices>>) {
        binding.mMarket.apply {
            if(it.data?.isNotEmpty()?:false){
                if(it.data?.first()?.marketStatus.equals("CLOSE",true)){
                    close.visibility= View.VISIBLE
                    open.visibility = View.GONE
                }else{
                    close.visibility= View.GONE
                    open.visibility = View.VISIBLE
                }
                val sdf = SimpleDateFormat("dd MMM yyyy | hh:mma", Locale.ENGLISH);

                // Get the current date and time
                val formattedDate = sdf.format(Date())
                dateTime.text = formattedDate
            }
        }
    }
}