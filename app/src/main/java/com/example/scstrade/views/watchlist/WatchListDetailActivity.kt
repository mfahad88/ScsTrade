package com.example.scstrade.views.watchlist

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.databinding.ActivityWatchListDetailBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.model.response.watchList.WatchListDetailItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.viewmodels.WatchListViewModel
import com.example.scstrade.factories.WatchListViewModelFactory
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.watchlist.adapter.WatchListDetailAdapter
import com.example.scstrade.views.widgets.HorizontalDivider
import com.google.gson.reflect.TypeToken

class WatchListDetailActivity : BaseActivity() {
    lateinit var viewModel: WatchListViewModel
    lateinit var login: LoginDataItem
    lateinit var sharedViewModel: SharedViewModel
    lateinit var binding:ActivityWatchListDetailBinding
    var list= emptyList<WatchListDetailItem>()
    var WatchListMainID:Int?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedViewModel = (application as MyApp).viewModel
        viewModel= ViewModelProvider(this,
            WatchListViewModelFactory(this.application,sharedViewModel)
        ).get(WatchListViewModel::class.java)
        binding = ActivityWatchListDetailBinding.inflate(LayoutInflater.from(this))
        binding.toolbar.toggleToolbar(false)
        binding.toolbar.binding.market.text = "Watchlist"
        enableEdgeToEdge()
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(binding.frameLayout) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(0,0,0,insets.bottom)
            windowInsets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerView) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.ime())

            view.setPadding(0,0,0,insets.bottom)
            windowInsets
        }
        fetchUser(this)

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
                    viewModel.getWatchListDetail(WatchListMainID?:0)
                    Utils.showDeleteBottomSheet(this,"Your symbol has been deleted from current watchlist.")

                }
            }
        })


        binding.recyclerView.apply {
            layoutManager= LinearLayoutManager(this@WatchListDetailActivity, LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(20.dp))
            adapter= WatchListDetailAdapter( emptyList()){ str, item->
                if(str.contains("delete",true)){
                    Utils.showConfirmationDialog(this.context,null,null,"Are you sure you want to delete this symbol?"){
                        val stockItem=list.filter { it.watchListSymbol.equals(item.sYM,true) }.first()
                        viewModel.deleteSymbol(stockItem.watchListDetailID,WatchListMainID?:0)
                    }

                }
            }
            (binding.recyclerView.adapter as WatchListDetailAdapter).getItemTouchHelper().attachToRecyclerView(this)
        }


    }

    private fun fetchUser(context: Context) {
        val listType = object : TypeToken<List<LoginDataItem>>() {}
        val user= Utils.getSharedPreference(context, emptyList<LoginDataItem>(),AppConstants.USER,listType)
        login=user.first()
        Log.e("User: ",user.toString())
    }

    override fun onResume() {
        super.onResume()
        viewModel.getWatchListDetail(WatchListMainID?:0)

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