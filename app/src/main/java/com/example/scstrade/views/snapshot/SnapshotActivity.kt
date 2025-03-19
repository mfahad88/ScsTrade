package com.example.scstrade.views.snapshot

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxColors
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivitySnapshotBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.viewmodels.WatchListViewModel
import com.example.scstrade.viewmodels.WatchListViewModelFactory
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.landing.LandingFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout

class SnapshotActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySnapshotBinding
    private lateinit var sharedViewModel: SharedViewModel
    lateinit var watchListViewModel: WatchListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySnapshotBinding.inflate(LayoutInflater.from(this))
        sharedViewModel=(this.application as MyApp).viewModel
        watchListViewModel = ViewModelProvider(this,
            WatchListViewModelFactory(this.application,(this.application as MyApp).viewModel)
        ).get(WatchListViewModel::class.java)

        sharedViewModel.snapshotOverview(intent.extras?.getString(AppConstants.SYMBOL)?:"")
        sharedViewModel.mutableOverview.observe(this, Observer { result->
            if(result.data!=null){
                sharedViewModel.snapshotChart(intent.extras?.getString(AppConstants.SYMBOL)?:"")
                sharedViewModel.snapshotDetail(intent.extras?.getString(AppConstants.SYMBOL)?:"")
            }
        })
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        binding.tabLayout.getTabAt(0)?.select()
        loadFragment(OverviewFragment())

        binding.floatingActionButton.setOnClickListener {
            watchListViewModel.getWatchList((this@SnapshotActivity.application as MyApp).login.registrationID?:-1)
            val bottomSheet=BottomSheetDialog(this)
            bottomSheet.setContentView(R.layout.bottom_watchlist)
            bottomSheet.findViewById<RelativeLayout>(R.id.new_watchlist)?.setOnClickListener {
                bottomSheet.setContentView(R.layout.fragment_add_watch_list_bottom_sheet)
                bottomSheet.findViewById<MaterialButton>(R.id.buttonAdd)!!.setOnClickListener {
                    watchListViewModel.createWatchList(bottomSheet.findViewById<EditText>(R.id.editTextName)?.text.toString(),(this.application as MyApp).login.registrationID?:0)
                }
                bottomSheet.findViewById<MaterialButton>(R.id.buttonCancel)!!.setOnClickListener {
                    bottomSheet.dismiss()
                }
                watchListViewModel.mutableCreate.observe(this,Observer{result->

                    when(result){
                        is Resource.Error -> {

                        }
                        is Resource.Loading -> {

                        }
                        is Resource.Success -> {
                            watchListViewModel.addSymbol(result.data?.filter { it.WatchListMainName.equals(bottomSheet.findViewById<EditText>(R.id.editTextName)?.text.toString(),true) }?.first()?.WatchListMainID?:-1,intent.extras?.getString(AppConstants.SYMBOL)?:"")
                            bottomSheet.setContentView(R.layout.bottom_added)
                            bottomSheet.findViewById<MaterialButton>(R.id.btnContinue)!!
                                .setOnClickListener { bottomSheet.dismiss() }
                        }
                    }

                })
            }
            bottomSheet.findViewById<ComposeView>(R.id.list)?.setContent {
                val result = watchListViewModel.mutableWatchListItem.asFlow().collectAsState(initial = Resource.Loading()).value
                when(result){
                    is Resource.Error -> {}
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        val checkedStates = remember {
                            mutableStateListOf(*Array(result.data?.size?:0) { false })
                        }
                        LazyColumn {
                            items(result.data?.size?:0){ index->
                                Row (verticalAlignment = Alignment.CenterVertically){
                                    Checkbox(
                                        checked = checkedStates[index],
                                        onCheckedChange = {
                                            checkedStates[index]=it
                                            result.data?.get(index)?.isChecked=it
                                        },
                                        colors = CheckboxDefaults.colors(checkedColor = colorResource(
                                            id = R.color.md_theme_primary
                                        ))
                                    )
                                    Spacer(modifier = Modifier.width(7.dp))
                                    Text(
                                        text = result.data?.get(index)?.WatchListMainName?:"No name found",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            lineHeight = 36.sp,
                                            fontFamily = FontFamily(Font(R.font.custom_font)),
                                            fontWeight = FontWeight(400),
                                            color = colorResource(id = R.color.colorDarkerr),
                                        )
                                    )
                                }
                            }
                        }

                        bottomSheet.findViewById<MaterialButton>(R.id.btnSave)?.setOnClickListener {

                            watchListViewModel.mutableWatchListItem.value?.data?.forEach {
                                if(it.isChecked){
                                    watchListViewModel.addSymbol(it.WatchListMainID,intent.extras?.getString(AppConstants.SYMBOL)?:"")
                                }

                            }
                            bottomSheet.setContentView(R.layout.bottom_added)
                            bottomSheet.findViewById<MaterialButton>(R.id.btnContinue)!!
                                .setOnClickListener { bottomSheet.dismiss() }

                        }
                    }
                }
                watchListViewModel.mutableSymAdd.observe(this, Observer { result->
                    when(result){
                        is Resource.Error -> {
//                            bottomSheet.dismiss()
//                            Utils.showError(binding.root,"Symbol Already Exists...")
                        }
                        is Resource.Loading -> {

                        }
                        is Resource.Success -> {

                        }
                    }
                })
            }
            bottomSheet.show()
        }

        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.text){
                    "Overview"->loadFragment(OverviewFragment())
                    "Income Statements"->loadFragment(IncomeStatementFragment())
                    "Balance Sheet" -> loadFragment(BalanceSheetFragment())
                    "Announcements" -> loadFragment(AnnouncementsFragment())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }



    public fun loadFragment(fragment: Fragment, isBackStack:Boolean = false) {
        if(isBackStack){
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }else{
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }
}