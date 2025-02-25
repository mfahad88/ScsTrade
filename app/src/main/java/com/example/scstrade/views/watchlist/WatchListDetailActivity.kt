package com.example.scstrade.views.watchlist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.databinding.ActivityWatchListDetailBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.watchList.WatchListDetailItem
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.viewmodels.WatchListViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.watchlist.adapter.WatchListDetailAdapter
import com.example.scstrade.views.widgets.HorizontalDivider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WatchListDetailActivity : AppCompatActivity() {
    lateinit var viewModel: WatchListViewModel
    lateinit var sharedViewModel: SharedViewModel
    lateinit var binding:ActivityWatchListDetailBinding
    var list= emptyList<WatchListDetailItem>()
    var WatchListMainID:Int?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel= ViewModelProvider(this).get(WatchListViewModel::class.java)
        sharedViewModel = (application as MyApp).viewModel
        binding = ActivityWatchListDetailBinding.inflate(LayoutInflater.from(this))
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        val bundle=intent.extras
        WatchListMainID=bundle?.getInt(AppConstants.WatchListMainID)?:0

        binding.buttonAdd.setOnClickListener {
            val intent= Intent(this,AddSymbolActivity::class.java)
            if (bundle != null) {
                intent.putExtras(bundle)
            }

            startActivity(intent)
        }


        viewModel.mutableWatchListDetail.observe(this, Observer {result->
            when(result){
                is Resource.Error -> {
                    binding.loader.visibility = View.GONE
                    Utils.showError(binding.root,result.message?:"An error occurred")
                }
                is Resource.Loading -> binding.loader.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.loader.visibility = View.GONE


                    (binding.recyclerView.adapter as WatchListDetailAdapter).addItems(result.data?: emptyList())
                }
            }

        })

        viewModel.mutableWatchListDetailItem.observe(this, Observer {
            list=it
        })

        viewModel.mutableSymDelete.observe(this, Observer {result->
            when(result){
                is Resource.Error -> {
                    binding.loader.visibility = View.GONE
                    Utils.showError(binding.root,result.message?:"An error occurred")
                }
                is Resource.Loading -> binding.loader.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.loader.visibility = View.GONE
                    viewModel.getWatchListDetail(sharedViewModel,WatchListMainID?:0)
                }
            }
        })


        binding.recyclerView.apply {
            layoutManager= LinearLayoutManager(this@WatchListDetailActivity, LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(20))
            adapter= WatchListDetailAdapter( emptyList()){ str, item->
                if(str.contains("delete",true)){
                    val stockItem=list.filter { it.watchListSymbol.equals(item.sYM,true) }.first()
                    viewModel.deleteSymbol(stockItem.watchListDetailID,WatchListMainID?:0)
                }
            }
            (binding.recyclerView.adapter as WatchListDetailAdapter).getItemTouchHelper().attachToRecyclerView(this)
        }

        sharedViewModel.mutableIndices.observe(this, Observer {
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

    override fun onResume() {
        super.onResume()
        viewModel.getWatchListDetail(sharedViewModel,WatchListMainID?:0)

    }
}