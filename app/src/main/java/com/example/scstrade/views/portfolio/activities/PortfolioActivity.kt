package com.example.scstrade.views.portfolio.activities

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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.databinding.ActivityPortfolioBinding
import com.example.scstrade.databinding.BottomPortfolioBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.portfolio.adapter.PortFolioAdapter
import com.example.scstrade.views.widgets.HorizontalDivider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.reflect.TypeToken

class PortfolioActivity : BaseActivity() {
    lateinit var binding: ActivityPortfolioBinding
    lateinit var login:LoginDataItem
    lateinit var sharedViewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPortfolioBinding.inflate(LayoutInflater.from(this))
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        setContentView(binding.main)
        // binding.toolbar.toggleToolbar(false)
        binding.toolbar.binding.market.text = "Portfolio"
        fetchUser(this)
//        login=(this.application as MyApp).login
        sharedViewModel = (this.application as MyApp).viewModel
        sharedViewModel.getPortfolio(login.registrationID)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@PortfolioActivity,LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(30.dp))
        }
        sharedViewModel.mutablePortfolio.observe(this, Observer { result ->

            when(result){
                is Resource.Error -> {
                    binding.loader.visibility = View.GONE
                    Utils.showError(binding.root,result.message?:"An error occurred...")
                }
                is Resource.Loading -> {
                    if(binding.loader.visibility == View.GONE) {
                        binding.loader.visibility = View.VISIBLE
                    }
                }
                is Resource.Success -> {
                    binding.loader.visibility = View.GONE
                    binding.recyclerView.apply {
                        adapter = PortFolioAdapter(result.data?.sortedBy { it.portfolioMainPosition }?.toMutableList()?: emptyList(), onItemClick = {

                            val intent=Intent(this@PortfolioActivity, PortfolioDetailActivity::class.java)
                            intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID,it.portfolioMainID)
                            startActivity(intent)
                        }, onItemPopupClick = {str,item->
                            if(str.contains("delete",true)) {
                                Utils.showConfirmationDialog(this@PortfolioActivity,null,null,"Are you sure you want to delete your portfolio?"){
                                    sharedViewModel.deletePortfolio(item.portfolioMainID,login.registrationID?:-1)
                                    Utils.showDeleteBottomSheet(this@PortfolioActivity,"Your portfolio has been deleted.")

                                }
                            }
                        })
                        layoutManager=LinearLayoutManager(this@PortfolioActivity,LinearLayoutManager.VERTICAL,false)
                    }


                }
            }
        })

        binding.btnCreate.setOnClickListener {
            createPortfolio(this)
        }
    }

    private fun createPortfolio(context: Context) {
        val dialogBinding=BottomPortfolioBinding.inflate(LayoutInflater.from(context))
        val dialog = BottomSheetDialog(context)
        dialog.apply {
            setContentView(dialogBinding.root)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            show()
        }
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }


        dialogBinding.btnAdd.setOnClickListener {
            sharedViewModel.cretePortfolio(dialogBinding.portfolioName.text.toString(),login.registrationID)
            dialog.dismiss()
        }

    }
    private fun fetchUser(context: Context) {
        val listType = object : TypeToken<List<LoginDataItem>>() {}
        val user= Utils.getSharedPreference(context, emptyList<LoginDataItem>(),
            AppConstants.USER,listType)
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