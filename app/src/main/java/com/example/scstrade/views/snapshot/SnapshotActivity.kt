package com.example.scstrade.views.snapshot

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
import com.example.scstrade.factories.SnapshotViewModelFactory
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.viewmodels.WatchListViewModel
import com.example.scstrade.factories.WatchListViewModelFactory
import com.example.scstrade.helper.Utils
import com.example.scstrade.viewmodels.SnapshotViewModel
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.MyApp
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout

class SnapshotActivity : BaseActivity() {
    private lateinit var binding: ActivitySnapshotBinding
    private lateinit var sharedViewModel: SharedViewModel
    lateinit var snapshotViewModel: SnapshotViewModel
    lateinit var watchListViewModel: WatchListViewModel
    lateinit var symbol:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySnapshotBinding.inflate(LayoutInflater.from(this))
        sharedViewModel=(this.application as MyApp).viewModel
        snapshotViewModel = ViewModelProvider(this,
            SnapshotViewModelFactory(this.application,(this.application as MyApp).viewModel)
        ).get(SnapshotViewModel::class.java)
        watchListViewModel = ViewModelProvider(this,
            WatchListViewModelFactory(this.application,(this.application as MyApp).viewModel)
        ).get(WatchListViewModel::class.java)
        symbol = intent.extras?.getString(AppConstants.SYMBOL)?:""
        if(symbol.contains("-")) {
            symbol = symbol.substring(0, symbol.indexOf("-") )
        }
        sharedViewModel.snapshotOverview(symbol)
        sharedViewModel.mutableOverview.observe(this, Observer { result->
            if(result.data!=null){
                sharedViewModel.snapshotChart(symbol)
                sharedViewModel.snapshotDetail(symbol)
            }
        })
        // binding.toolbar.toggleToolbar(false)
        binding.toolbar.binding.apply {
            market.text = intent.extras?.getString(AppConstants.SYMBOL)?:""

//            subTitle.text = intent.extras?.getString(AppConstants.SYMBOL)?:""
        }
        enableEdgeToEdge()
        setContentView(binding.root)
        Utils.setEdgeToEdgeWithWhiteIcons(this)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
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
                    if(bottomSheet.findViewById<EditText>(R.id.editTextName)?.text?.isNotEmpty() == true) {
                        watchListViewModel.createWatchList(
                            bottomSheet.findViewById<EditText>(R.id.editTextName)?.text.toString(),
                            (this.application as MyApp).login.registrationID ?: 0
                        )
                    }
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
                                            color = colorResource(id = R.color.black),
                                        )
                                    )
                                }
                            }
                        }

                        bottomSheet.findViewById<MaterialButton>(R.id.btnSave)?.setOnClickListener {
                            if(checkedStates.any { it }) {
                                watchListViewModel.mutableWatchListItem.value?.data?.forEach {
                                    if (it.isChecked) {
                                        watchListViewModel.addSymbol(
                                            it.WatchListMainID,
                                            intent.extras?.getString(AppConstants.SYMBOL) ?: ""
                                        )
                                    }

                                }
                                bottomSheet.setContentView(R.layout.bottom_added)
                                bottomSheet.findViewById<MaterialButton>(R.id.btnContinue)!!
                                    .setOnClickListener { bottomSheet.dismiss() }
                            }else{
                                bottomSheet.dismiss()
                            }

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