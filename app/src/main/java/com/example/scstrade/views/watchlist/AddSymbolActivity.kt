package com.example.scstrade.views.watchlist
import androidx.compose.ui.res.dimensionResource

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityAddSymbolBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.viewmodels.WatchListViewModel
import com.example.scstrade.factories.WatchListViewModelFactory
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.watchlist.adapter.SymbolAdapter
import com.example.scstrade.views.widgets.HorizontalDivider
import com.google.gson.reflect.TypeToken

class AddSymbolActivity : BaseActivity() {
    lateinit var binding:ActivityAddSymbolBinding
    lateinit var sharedViewModel: SharedViewModel
    lateinit var viewModel: WatchListViewModel
    lateinit var login:LoginDataItem
    var myList=ArrayList<String>()
    var mode:Int=-1
    var selectedItem=-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSymbolBinding.inflate(LayoutInflater.from(this))
        enableEdgeToEdge()
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        setContentView(binding.root)
        // binding.toolbar.toggleToolbar(false)
        binding.toolbar.binding.market.text = "Add Symbol"
//        binding.toolbar.binding.subTitle.text = "Add Symbol"
        fetchUser()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        mode=intent.getIntExtra(AppConstants.MODE,0)
        val bundle=intent.extras
        selectedItem=bundle?.getInt(AppConstants.WatchListMainID)?:0
        viewModel= ViewModelProvider(this,
            WatchListViewModelFactory(this.application,(this.application as MyApp).viewModel)
        ).get(WatchListViewModel::class.java)
        sharedViewModel = (this.application as MyApp).viewModel
        if(mode==0) {
            binding.watchlistName.textInputEditText.addTextChangedListener {

                if (!TextUtils.isEmpty(it.toString()) /*&& binding.watchlistName.textInputEditText.length() > 3*/) {
                    binding.btnDone.isEnabled = true
                }
            }
        }else{
            binding.btnDone.isEnabled = true
            binding.watchlistName.visibility = View.GONE
        }
        binding.btnDone.setOnClickListener {
            if(mode==0){
                if(myList.size>0) {
                    viewModel.createWatchList(
                        binding.watchlistName.text.toString(),
                        login.registrationID ?: 0
                    )
                }else{
                    Utils.showError(binding.root, getString(R.string.please_select_a_symbol))
                }
            }else{

                if(myList.size>0){
                    myList.forEach {
                        viewModel.addSymbol(selectedItem,it)
                    }
                }else{
                    Utils.showError(binding.root, getString(R.string.please_select_a_symbol))
                }
            }
        }

        viewModel.mutableCreate.observe(this, Observer {
            when(it){
                is Resource.Error -> {
                    binding.loader.visibility = View.GONE
                    Utils.showError(binding.root,it.message?:"An error occurred")
                }
                is Resource.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    selectedItem = it.data?.last()?.WatchListMainID!!
                    myList.forEach {
                        viewModel.addSymbol(selectedItem,it)
                    }
                   /* val result = Bundle().apply { putString(AppConstants.BOTTOM_SHEET_STATUS,"Done") }
                    parentFragmentManager.setFragmentResult(AppConstants.BOTTOM_SHEET,result)

                    this.dismiss()*/
                }
            }
        })
        viewModel.mutableSymAdd.observe(this, Observer {
            when(it){
                is Resource.Error -> {

                    Utils.showError(binding.root, it.message ?: "An error occurred")
                }
                is Resource.Loading ->  {
                    if(mode>0) {
                        binding.loader.visibility = View.VISIBLE
                    }
                }
                is Resource.Success -> {
                    binding.loader.visibility = View.GONE
                    if(mode==0) {
                        if (it.data?.last()?.watchListSymbol.equals(myList.last())) {
                            finish()
                            val intent = Intent(this, WatchListDetailActivity::class.java)
                            intent.putExtra(AppConstants.WatchListMainID, selectedItem)
//                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    }else{
                        if (it.data?.last()?.watchListSymbol.equals(myList.last())) {
                            finish()
                        }
                    }
                }
            }
        })
        binding.symbol.textInputEditText.addTextChangedListener(object : TextWatcher{
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

                binding.recyclerView.adapter= SymbolAdapter(value.data?: emptyList()){res->
//                    viewModel.addSymbol(selectedItem,it.sYM)
                    if(myList.contains(res.sYM)){
                        myList.remove(res.sYM)
                    }else {
                        myList.add(res.sYM)
                    }
//                    viewModel.getWatchListDetail(sharedViewModel,selectedItem)

                }
                sharedViewModel.mutableAllData.removeObserver(this)
                binding.loader.visibility = View.GONE
            }

        })

        binding.recyclerView.apply {
            layoutManager=LinearLayoutManager(this@AddSymbolActivity,LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(20.dp))
        }


    }



    private fun fetchUser() {
        val listType = object : TypeToken<List<LoginDataItem>>() {}
        val user= Utils.getSharedPreference(this, emptyList<LoginDataItem>(),AppConstants.USER,listType)
        login=user.first()
        Log.e("User: ",user.toString())
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