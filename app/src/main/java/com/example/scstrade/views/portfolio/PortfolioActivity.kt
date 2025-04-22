package com.example.scstrade.views.portfolio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityPortfolioBinding
import com.example.scstrade.databinding.BottomPortfolioBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.widgets.HorizontalDivider
import com.google.android.material.bottomsheet.BottomSheetDialog

class PortfolioActivity : AppCompatActivity() {
    lateinit var binding: ActivityPortfolioBinding
    lateinit var login:LoginDataItem
    lateinit var sharedViewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPortfolioBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        login=(this.application as MyApp).login
        sharedViewModel = (this.application as MyApp).viewModel
        sharedViewModel.getPortfolio(login.registrationID)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@PortfolioActivity,LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(30))
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

                            val intent=Intent(this@PortfolioActivity,PortfolioDetailActivity::class.java)
                            intent.putExtra(AppConstants.PORTFOLIO_MAIN_ID,it.portfolioMainID)
                            startActivity(intent)
                        }, onItemPopupClick = {str,item->
                            if(str.contains("delete",true)) {
                                Utils.showConfirmationDialog(this@PortfolioActivity,null,null,"Are you sure you want to delete your portfolio?"){
                                    sharedViewModel.deletePortfolio(item.portfolioMainID,login.registrationID?:-1)

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


}