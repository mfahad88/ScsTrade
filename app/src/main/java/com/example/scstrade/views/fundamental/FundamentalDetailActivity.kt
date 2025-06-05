package com.example.scstrade.views.fundamental

import android.content.Intent
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
import com.example.scstrade.views.fundamental.adapter.FundamentalDetailAdapter
import com.example.scstrade.views.snapshot.SnapshotActivity
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
        setContentView(binding.root)
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

                            val intent= Intent(this@FundamentalDetailActivity, SnapshotActivity::class.java)
                            intent.putExtra(AppConstants.SYMBOL, it.symbol)
                            startActivity(intent)
                        }
                        layoutManager= LinearLayoutManager(this@FundamentalDetailActivity,
                            LinearLayoutManager.VERTICAL,false)
                    }
                }
            }
        })

    }


}