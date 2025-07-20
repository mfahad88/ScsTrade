package com.example.scstrade.views.portfolio.activities
import androidx.compose.ui.res.dimensionResource

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityBuySellBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.portfolio.PortfolioDetailItem
import com.example.scstrade.model.response.portfolio.PortfolioDetails
import com.example.scstrade.model.response.portfolio.PortfolioItemDetail
import com.example.scstrade.viewmodels.PortFolioViewModel
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.MyApp
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Locale

class BuySellActivity : BaseActivity() {
    lateinit var binding: ActivityBuySellBinding
    lateinit var sharedViewModel: SharedViewModel

    lateinit var portFolioViewModel: PortFolioViewModel
    //    lateinit var stockList:ArrayList<PortfolioDetailItem>
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBuySellBinding.inflate(LayoutInflater.from(this))
        sharedViewModel = (this.application as MyApp).viewModel
        portFolioViewModel= ViewModelProvider.AndroidViewModelFactory.getInstance(this.application as MyApp).create(PortFolioViewModel::class.java)
        setContentView(binding.root)
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        val porfolioMainId=intent.getIntExtra(AppConstants.PORTFOLIO_MAIN_ID,-1)
        sharedViewModel.mutableAllData.observe(this, Observer { result->
            when(result){
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    if( findViewById<View>(R.id.buy_container).visibility == View.VISIBLE) {
                        binding.buyContainer.apply{
                            val adapter = ArrayAdapter(
                                this@BuySellActivity,
                                android.R.layout.simple_spinner_dropdown_item,
                                result.data?.map { "${it.sYM}-${it.nM}" }?.toList() ?: emptyList()
                            )
                            symbol.setAdapter(adapter)
                        }
                    }

                    if( findViewById<View>(R.id.sell_container).visibility == View.VISIBLE) {
                        binding.sellContainer.apply{
                            val adapter = ArrayAdapter(
                                this@BuySellActivity,
                                android.R.layout.simple_spinner_dropdown_item,
                                result.data?.map { "${it.sYM}-${it.nM}" }?.toList() ?: emptyList()
                            )
                            symbol.setAdapter(adapter)
                        }
                    }

                    if( findViewById<View>(R.id.dividend_container).visibility == View.VISIBLE) {
                        binding.dividendContainer.apply{
                            val adapter = ArrayAdapter(
                                this@BuySellActivity,
                                android.R.layout.simple_spinner_dropdown_item,
                                result.data?.map { "${it.sYM}-${it.nM}" }?.toList() ?: emptyList()
                            )
                            symbol.setAdapter(adapter)
                        }
                    }

                }
            }
        })
//        list = sharedViewModel.mutableAllData.value?.data?.map { "${it.sYM}-${it.nM}" }?.toList()?: emptyList()

        val portfolioDetails=intent.getParcelableExtra<PortfolioDetails>(AppConstants.PORTFOLIO_DETAIL)
        if(intent.getBooleanExtra(AppConstants.IS_Sell,false)){
            findViewById<View>(R.id.sell_container).visibility = View.VISIBLE
            // binding.toolbar.toggleToolbar(false)
            binding.toolbar.binding.market.text = "Sell Stock"
            binding.toolbar.binding.apply {
//                subTitle.text = "Sell Stock"
            }
//            stockList = intent.getParcelableArrayListExtra<PortfolioDetailItem>(AppConstants.STOCK_INFO)!!
//            (binding.sellContainer as View).visibility = View.VISIBLE
        }
        if(intent.getBooleanExtra(AppConstants.IS_BUY,false)){
            findViewById<View>(R.id.buy_container).visibility = View.VISIBLE
            // binding.toolbar.toggleToolbar(false)
            binding.toolbar.binding.market.text = "Buy Stock"
            binding.toolbar.binding.apply {
//                subTitle.text = "Buy Stock"
            }
//            (binding.buyContainer as View).visibility = View.VISIBLE
        }
        if(intent.getBooleanExtra(AppConstants.IS_Dividend,false)){
            findViewById<View>(R.id.dividend_container).visibility = View.VISIBLE
            binding.toolbar.binding.apply {
            }
            binding.toolbar.binding.market.text = "Dividend"
//
        }


        if( findViewById<View>(R.id.buy_container).visibility == View.VISIBLE){


            binding.buyContainer.apply {
                val sym = intent.getStringExtra(AppConstants.SYMBOL)
                if(!sym.isNullOrEmpty()) {
                    val stockItem =
                        sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(sym) }
                            ?.first()
                    val symb = "${stockItem?.sYM}-${stockItem?.nM}"
                    val v =
                        sharedViewModel.mutablePortfolioFinalDetail.value?.data?.fifoPortfolio?.filter {
                            it.symbol.equals(
                                sym,
                                true
                            )
                        }?.first()
                    val qty = v?.quantity
                    val askPrice = sharedViewModel.mutableAllData.value?.data?.filter {
                        it.sYM.equals(
                            sym,
                            true
                        )
                    }?.map { it.aP }?.first()
                    val avgBuy = String.format("%.2f", stockItem?.avgP)
//                availableShareValue.text = "${qty}"
                    symbol.setText(symb)
                    buyPrice.setText(Utils.roundTwoDecimal(askPrice ?: 0.00))
                }

//                avgBuyPriceValue.setText("${avgBuy}")
                purchaseDate.setOnFocusChangeListener { view, b ->
                    if (b) {
                        showDatePicker(purchaseDate)
                    }
                }

                symbol.setOnDismissListener {
                    try{
                        if(sharedViewModel.mutableAllData.value?.data?.filter { "${it.sYM}-${it.nM}".contains(symbol.text.toString(),true) }?.first()!=null) {
                            val symbol = sharedViewModel.mutableAllData.value?.data?.filter {
                                "${it.sYM}-${it.nM}".contains(
                                    symbol.text.toString(),
                                    true
                                )
                            }?.first()
                            buyPrice.setText(symbol?.oC.toString())
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                    val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    manager.hideSoftInputFromWindow(currentFocus?.applicationWindowToken,0)

                }

                if(intent.getIntExtra(AppConstants.MODE,0)==1){
                    symbol.setText(portfolioDetails?.portfolioSymbol)
                    shares.setText(portfolioDetails?.portfolioQuantity.toString())
                    buyPrice.setText(portfolioDetails?.portfolioRate.toString())
                    purchaseDate.setText(Utils.convertDateString(portfolioDetails?.portfolioDate?:"01-01-1990","yyyy-MM-dd"))
                    comissionShare.setText(portfolioDetails?.portfolioCommission.toString())
                    if(portfolioDetails?.portfolioCommissionType.equals("Rs",true)){
                        radioShare.isChecked=true
                        radioVar.isChecked=false
                    }else{
                        radioShare.isChecked=false
                        radioVar.isChecked=true
                    }
                }

                button2.setOnClickListener {
                    if (symbol.text.isNotEmpty() && shares.text.isNotEmpty() && buyPrice.text.isNotEmpty()
                        && comissionShare.text.isNotEmpty() && radioCommissionType.checkedRadioButtonId != null && purchaseDate.text.isNotEmpty()
                    ) {
                        if(intent.getIntExtra(AppConstants.MODE,0)==0){
                            portFolioViewModel.buyStock(
                                portfolioMainID = porfolioMainId,
                                portfolioDate = purchaseDate.text.toString(),
                                portfolioSymbol = symbol.text.split("-").first(),
                                portfolioQuantity = shares.text.toString(),
                                portfolioRate = buyPrice.text.toString(),
                                portfolioCommission = comissionShare.text.toString(),
                                portfolioCommissionType = if (radioCommissionType.checkedRadioButtonId == R.id.radioShare) "Rs" else "Percentage",
                                portfolioPosition = "0"
                            )
//                            portFolioViewModel.getPortfolioItemDetail(porfolioMainId,symbol.text.split("-").first())
                        }else{
                            portFolioViewModel.updateTrade(
                                portfolioMainID = porfolioMainId,
                                portfolioType = "BUY",
                                portfolioDate = purchaseDate.text.toString(),
                                portfolioSymbol = symbol.text.split("-").first(),
                                portfolioQuantity = shares.text.toString(),
                                portfolioRate = buyPrice.text.toString(),
                                portfolioCommission = comissionShare.text.toString(),
                                portfolioCommissionType = if (radioCommissionType.checkedRadioButtonId == R.id.radioShare) "Rs" else "Percentage",
                                portfolioPosition = "0",
                                portfolioDetailID = portfolioDetails?.portfolioDetailID.toString()
                            )
//                            portFolioViewModel.getPortfolioItemDetail(porfolioMainId,symbol.text.split("-").first())
                        }
//                        Toast.makeText(it.context, "Done", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Utils.showError(root, "Empty fields not allowed...")
                    }
                }
            }
        }

        if( findViewById<View>(R.id.sell_container).visibility == View.VISIBLE){


            binding.sellContainer.apply {

                if(intent.getIntExtra(AppConstants.MODE,0)==0) {
                    val sym = intent.getStringExtra(AppConstants.SYMBOL)
                    if(!sym.isNullOrEmpty()){
                        val stockItem =sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(sym) }?.first()
                        val symb="${stockItem?.sYM}-${stockItem?.nM}"
                        val v =
                            sharedViewModel.mutablePortfolioFinalDetail.value?.data?.fifoPortfolio?.filter {
                                it.symbol.equals(
                                    sym,
                                    true
                                )
                            }?.first()
                        val qty = v?.quantity
                        val askPrice = sharedViewModel.mutableAllData.value?.data?.filter {
                            it.sYM.equals(
                                sym,
                                true
                            )
                        }?.map { it.aP }?.first()
                        val avgBuy = String.format("%.2f",stockItem?.avgP)
                        availableShareValue.text = "${qty}"
                        symbol.setText(symb)
                        buyPrice.setText(Utils.roundTwoDecimal(askPrice ?: 0.00))
                        avgBuyPriceValue.setText("${avgBuy}")
                    }
                    purchaseDate.setOnFocusChangeListener { view, b ->
                        if (b) {
                            showDatePicker(purchaseDate)
                        }
                    }
                }else{
                    availableShareValue.visibility=View.GONE
                    avgBuyPriceValue.visibility = View.GONE
                    symbol.setText(portfolioDetails?.portfolioSymbol)
                    shares.setText(portfolioDetails?.portfolioQuantity.toString())
                    buyPrice.setText(portfolioDetails?.portfolioRate.toString())
                    purchaseDate.setText(Utils.convertDateString(portfolioDetails?.portfolioDate?:"01-01-1990","yyyy-MM-dd"))
                    comissionShare.setText(portfolioDetails?.portfolioCommission.toString())
                    if(portfolioDetails?.portfolioCommissionType.equals("Rs",true)){
                        radioShare.isChecked=true
                        radioVar.isChecked=false
                    }else{
                        radioShare.isChecked=false
                        radioVar.isChecked=true
                    }
                }
                buttonSell.setOnClickListener {
                    if(symbol.text.isNotEmpty() && shares.text.isNotEmpty() && buyPrice.text.isNotEmpty()
                        && comissionShare.text.isNotEmpty() && radioCommissionType.checkedRadioButtonId!=null && purchaseDate.text.isNotEmpty()){
                        if(intent.getIntExtra(AppConstants.MODE,0)==0){
                            portFolioViewModel.sellStock(
                                portfolioMainID = porfolioMainId,
                                portfolioDate = purchaseDate.text.toString(),
                                portfolioSymbol = symbol.text.split("-").first(),
                                portfolioQuantity = shares.text.toString(),
                                portfolioRate = buyPrice.text.toString(),
                                portfolioCommission = comissionShare.text.toString(),
                                portfolioCommissionType = if (radioCommissionType.checkedRadioButtonId == R.id.radioShare) "Rs" else "Percentage",
                                portfolioPosition = "0"
                            )
                        }else{
                            portFolioViewModel.updateTrade(
                                portfolioMainID = porfolioMainId,
                                portfolioType = "SELL",
                                portfolioDate = purchaseDate.text.toString(),
                                portfolioSymbol = symbol.text.split("-").first(),
                                portfolioQuantity = shares.text.toString(),
                                portfolioRate = buyPrice.text.toString(),
                                portfolioCommission = comissionShare.text.toString(),
                                portfolioCommissionType = if (radioCommissionType.checkedRadioButtonId == R.id.radioShare) "Rs" else "Percentage",
                                portfolioPosition = "0",
                                portfolioDetailID = portfolioDetails?.portfolioDetailID.toString()
                            )
                        }
//                        Toast.makeText(it.context,"Done",Toast.LENGTH_SHORT).show()
                        finish()

                    }else{
                        Utils.showError(root,"Empty fields not allowed...")
                    }


                }

            }

        }

        if( findViewById<View>(R.id.dividend_container).visibility == View.VISIBLE){


            binding.dividendContainer.apply {
                val sym = intent.getStringExtra(AppConstants.SYMBOL)
                if(!sym.isNullOrEmpty()){
                    val stockItem =sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(sym) }?.first()
                    val symb="${stockItem?.sYM}-${stockItem?.nM}"
                    val v =
                        sharedViewModel.mutablePortfolioFinalDetail.value?.data?.fifoPortfolio?.filter {
                            it.symbol.equals(
                                sym,
                                true
                            )
                        }?.first()
                    val qty = v?.quantity
                    val askPrice = sharedViewModel.mutableAllData.value?.data?.filter {
                        it.sYM.equals(
                            sym,
                            true
                        )
                    }?.map { it.aP }?.first()
                    val avgBuy = String.format("%.2f",stockItem?.avgP)
//                availableShareValue.text = "${qty}"
                    symbol.setText(symb)
                }
                symbol.setOnDismissListener {
//                    val symbol=sharedViewModel.mutableAllData.value?.data?.filter { "${it.sYM}-${it.nM}".contains(symbol.text.toString(),true) }?.first()
                    val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    manager.hideSoftInputFromWindow(currentFocus?.applicationWindowToken,0)

                }
                dividendDate.setOnFocusChangeListener { view, b ->
                    if(b){
                        showDatePicker(dividendDate)
                    }
                }
                btnDividend.setOnClickListener {
                    if(symbol.text.isNotEmpty() && shares.text.isNotEmpty() && dividendShare.text.isNotEmpty() && dividendDate.text.isNotEmpty()){
                        portFolioViewModel.addDividend(
                            dividendSymbol = symbol.text.toString(),
                            dividendDate = dividendDate.text.toString(),
                            portfolioMainID = porfolioMainId.toString(),
                            dividendQuantity = shares.text.toString(),
                            dividendPerShare = dividendShare.text.toString()
                        )
                        finish()
                    }else{
                        Utils.showError(root,"Empty fields not allowed...")
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showDatePicker(tv:EditText) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select a date")
            .build()
        datePicker.show(supportFragmentManager, "DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener { selectedDateInMillis ->
            val calendar = Calendar.getInstance().apply {
                timeInMillis = selectedDateInMillis
            }
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            tv.setText(date)
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