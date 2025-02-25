package com.example.scstrade.views.watchlist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityAddSymbolBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.viewmodels.WatchListViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.watchlist.adapter.SymbolAdapter
import com.example.scstrade.views.widgets.HorizontalDivider
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddSymbolActivity : AppCompatActivity() {
    lateinit var binding:ActivityAddSymbolBinding
    lateinit var sharedViewModel: SharedViewModel
    lateinit var viewModel: WatchListViewModel
    lateinit var login:LoginDataItem
    var selectedItem=-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSymbolBinding.inflate(LayoutInflater.from(this))
        enableEdgeToEdge()
        setContentView(binding.root)
        fetchUser()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        val bundle=intent.extras
        selectedItem=bundle?.getInt(AppConstants.WatchListMainID)?:0
        viewModel= ViewModelProvider(this).get(WatchListViewModel::class.java)
        sharedViewModel = (this.application as MyApp).viewModel
        binding.symbol.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                (binding.recyclerView.adapter as SymbolAdapter).filterList(binding.sector.selectedItem.toString(),if(binding.symbol.text.isNotEmpty()) binding.symbol.text.toString() else null)
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding.sector.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                binding.symbol.text=""
                (binding.recyclerView.adapter as SymbolAdapter).filterList((p1 as TextView).text.toString(),if(binding.symbol.text.isNotEmpty()) binding.symbol.text.toString() else null)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                (binding.recyclerView.adapter as SymbolAdapter).filterList(null,null)
            }

        }
        sharedViewModel.mutableAllData.observe(this, object: Observer<Resource<List<StockItem>>>{
            override fun onChanged(value: Resource<List<StockItem>>) {
                val list= ArrayList<String>()
                list.add("All Sector")
                list.addAll(value.data?.map { it.sN }?.toSet()?.toList()?: emptyList())
                binding.sector.adapter=ArrayAdapter(this@AddSymbolActivity,android.R.layout.simple_list_item_1,list)

                binding.recyclerView.adapter= SymbolAdapter(value.data?: emptyList()){
                    viewModel.addSymbol(sharedViewModel,selectedItem,it.sYM)

//                    viewModel.getWatchListDetail(sharedViewModel,selectedItem)

                }
                sharedViewModel.mutableAllData.removeObserver(this)
            }

        })

        binding.recyclerView.apply {
            layoutManager=LinearLayoutManager(this@AddSymbolActivity,LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(20))
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

    private fun fetchUser() {
        val listType = object : TypeToken<List<LoginDataItem>>() {}
        val user= Utils.getSharedPreference(this, emptyList<LoginDataItem>(),AppConstants.USER,listType)
        login=user.first()
        Log.e("User: ",user.toString())
    }


}