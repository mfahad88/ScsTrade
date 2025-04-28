package com.example.scstrade.views.portfolio.activities

import android.content.Context
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
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityBuySellBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.portfolio.PortfolioDetailItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Locale

class BuySellActivity : AppCompatActivity() {
    lateinit var binding: ActivityBuySellBinding
    lateinit var sharedViewModel: SharedViewModel
    lateinit var list:List<String>
//    lateinit var stockList:ArrayList<PortfolioDetailItem>
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBuySellBinding.inflate(LayoutInflater.from(this))
        sharedViewModel = (this.application as MyApp).viewModel
        setContentView(binding.root)
        list = sharedViewModel.mutableAllData.value?.data?.map { "${it.sYM}-${it.nM}" }?.toList()?: emptyList()
        val porfolioDetail=intent.getIntExtra(AppConstants.PORTFOLIO_MAIN_ID,-1)
        if(intent.getBooleanExtra(AppConstants.IS_Sell,false)){
            findViewById<View>(R.id.sell_container).visibility = View.VISIBLE
//            stockList = intent.getParcelableArrayListExtra<PortfolioDetailItem>(AppConstants.STOCK_INFO)!!
//            (binding.sellContainer as View).visibility = View.VISIBLE
        }
        if(intent.getBooleanExtra(AppConstants.IS_BUY,false)){
            findViewById<View>(R.id.buy_container).visibility = View.VISIBLE
//            (binding.buyContainer as View).visibility = View.VISIBLE
        }
        if(intent.getBooleanExtra(AppConstants.IS_Dividend,false)){
            findViewById<View>(R.id.dividend_container).visibility = View.VISIBLE
//            (binding.dividendContainer as View).visibility = View.VISIBLE
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        if( findViewById<View>(R.id.buy_container).visibility == View.VISIBLE){
            val adapter =ArrayAdapter(this@BuySellActivity,android.R.layout.simple_spinner_dropdown_item,list)

            binding.buyContainer.apply {
                symbol.setAdapter(adapter)
                symbol.setOnDismissListener {
                    val symbol=sharedViewModel.mutableAllData.value?.data?.filter { "${it.sYM}-${it.nM}".contains(symbol.text.toString(),true) }?.first()
                    buyPrice.setText(symbol?.oC.toString())
                    val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    manager.hideSoftInputFromWindow(currentFocus?.applicationWindowToken,0)

                }
                purchaseDate.setOnFocusChangeListener { view, b ->
                    if(b){
                        showDatePicker(purchaseDate)
                    }
                }
                button2.setOnClickListener {
                    if(symbol.text.isNotEmpty() && shares.text.isNotEmpty() && buyPrice.text.isNotEmpty()
                        && comissionShare.text.isNotEmpty() && radioCommissionType.checkedRadioButtonId!=null && purchaseDate.text.isNotEmpty()){
                        sharedViewModel.buyStock(
                            portfolioMainID=porfolioDetail,portfolioDate=purchaseDate.text.toString(),portfolioSymbol=symbol.text.split("-").first(),
                            portfolioQuantity=shares.text.toString(),portfolioRate=buyPrice.text.toString(),portfolioCommission=comissionShare.text.toString(),
                            portfolioCommissionType= if(radioCommissionType.checkedRadioButtonId==R.id.radioShare) "Rs" else "Percentage",portfolioPosition = "0"
                        )
                        Toast.makeText(it.context,"Done",Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Utils.showError(root,"Empty fields not allowed...")
                    }
                }
            }
        }

        if( findViewById<View>(R.id.sell_container).visibility == View.VISIBLE){
            val sym=intent.getStringExtra(AppConstants.SYMBOL)
            val v=sharedViewModel.mutablePortfolioDetail.value?.data?.fifoPortfolio?.filter { it.symbol.equals(sym,true) }?.first()
            val qty=v?.quantity
            val askPrice= sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals(sym,true) }?.map { it.aP }?.first()
            val totalCost = v?.price?.toDouble()
            val avgBuy= totalCost?.div(v.quantity.toInt())
            binding.sellContainer.apply {
                availableShareValue.text = "${qty}"
                symbol.setText(sym)
                buyPrice.setText(Utils.roundTwoDecimal(askPrice))
                avgBuyPriceValue.setText("${avgBuy}")
                purchaseDate.setOnFocusChangeListener { view, b ->
                    if(b){
                        showDatePicker(purchaseDate)
                    }
                }
                buttonSell.setOnClickListener {
                    if(symbol.text.isNotEmpty() && shares.text.isNotEmpty() && buyPrice.text.isNotEmpty()
                        && comissionShare.text.isNotEmpty() && radioCommissionType.checkedRadioButtonId!=null && purchaseDate.text.isNotEmpty()){
                        sharedViewModel.sellStock(
                            portfolioMainID = porfolioDetail,
                            portfolioDate = purchaseDate.text.toString(),
                            portfolioSymbol = symbol.text.split("-").first(),
                            portfolioQuantity = shares.text.toString(),
                            portfolioRate = buyPrice.text.toString(),
                            portfolioCommission = comissionShare.text.toString(),
                            portfolioCommissionType = if (radioCommissionType.checkedRadioButtonId == R.id.radioShare) "Rs" else "Percentage",
                            portfolioPosition = "0"
                        )

                        Toast.makeText(it.context,"Done",Toast.LENGTH_SHORT).show()
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
}