package com.example.scstrade.views.snapshot
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.compose.ui.res.dimensionResource

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentTechnicalBinding
import com.example.scstrade.databinding.ItemKeyValueBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import java.util.Locale
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.ViewGroup
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.scstrade.views.ChartActivity
import com.example.scstrade.views.widgets.TextDrawable

class TechnicalFragment : Fragment() {
   lateinit var binding: FragmentTechnicalBinding
    lateinit var sharedViewModel: SharedViewModel
    private var macd:String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTechnicalBinding.inflate(inflater, container, false)
        sharedViewModel=(requireActivity().application as MyApp).viewModel
        sharedViewModel.snapTechnical((requireActivity() as SnapshotActivity).symbol)
        toggleLineChart(binding.candle)
        binding.apply {
            zoom.setOnClickListener {
                val intent = Intent(requireContext(), ChartActivity::class.java)
                intent.putExtra("Indices",(requireActivity() as SnapshotActivity).symbol)
                startActivity(intent)
            }
            line.setOnClickListener {
                toggleLineChart(it)
            }

            candle.setOnClickListener {
                toggleLineChart(it)
            }

            d1.setOnClickListener {
                toggleDay(it)
            }

            hr1.setOnClickListener {
                toggleDay(it)
            }

            min30.setOnClickListener {
                toggleDay(it)
            }

            min15.setOnClickListener {
                toggleDay(it)
            }
            min5.setOnClickListener {
                toggleDay(it)
            }

            min1.setOnClickListener {
                toggleDay(it)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            val fabHeight = (requireActivity() as SnapshotActivity).binding.floatingActionButton.height
            val marginBottom = ((requireActivity() as SnapshotActivity).binding.floatingActionButton.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin

            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                 fabHeight + marginBottom
            )
            insets
        }

        if(!sharedViewModel.mutableAllData.value?.data.isNullOrEmpty()) {
            val firstChar = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals((requireActivity() as SnapshotActivity).symbol) }?.first()?.sYM?.first()?.uppercaseChar().toString()
            val color = Utils.getColorFromSymbol(firstChar)
            val placeholderDrawable = TextDrawable(firstChar, color)
            val icon = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals((requireActivity() as SnapshotActivity).symbol) }?.map { it.companyLogo }?.first()
            Glide.with(binding.root.context).load(icon)
                .placeholder(placeholderDrawable)
                .circleCrop()
                .listener(object : RequestListener<Drawable> {


                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.imageView16.alpha = 1f
                        return false // Let Glide handle setting the image
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.imageView16.alpha = 1f
                        return false // Let Glide handle setting the image
                    }


                })
                .into(binding.imageView16)
            /*val icon = sharedViewModel.mutableAllData.value?.data?.filter { it.sYM.equals((requireActivity() as SnapshotActivity).symbol) }?.map { it.companyLogo }?.first()
            Glide.with(binding.root.context).load(icon).circleCrop().into(binding.imageView16)*/
        }

        sharedViewModel.mutableOverview.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> Utils.showError(binding.root,result.message?:"An error occurred")
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    try{
                        Log.e("Result",result.data.toString())
                        binding.onePecent.text = "${result.data?.oneMonthReturn}%"
                        binding.threePecent.text = "${result.data?.twoMonthReturn}%"
                        binding.sixPecent.text = "${result.data?.sixMonthReturn}%"
                        binding.oneYrPecent.text = "${result.data?.twelveMonthReturn}%"
                        binding.apply {
                            onePerformance.background?.let {
                                val wrappedDrawable = DrawableCompat.wrap(it)
                                if(result.data?.oneMonthReturn?.contains("-")?:false) {
                                    DrawableCompat.setTint(
                                        wrappedDrawable,
                                        ContextCompat.getColor(requireContext(), R.color.md_theme_error)
                                    )
                                }else{
                                    DrawableCompat.setTint(
                                        wrappedDrawable,
                                        ContextCompat.getColor(requireContext(), R.color.md_theme_primary)
                                    )
                                }
                                onePerformance.background = wrappedDrawable
                            }

                            threePerformance.background?.let {
                                val wrappedDrawable = DrawableCompat.wrap(it)
                                if(result.data?.twoMonthReturn?.contains("-")?:false) {
                                    DrawableCompat.setTint(
                                        wrappedDrawable,
                                        ContextCompat.getColor(requireContext(), R.color.md_theme_error)
                                    )
                                }else{
                                    DrawableCompat.setTint(
                                        wrappedDrawable,
                                        ContextCompat.getColor(requireContext(), R.color.md_theme_primary)
                                    )
                                }
                                onePerformance.background = wrappedDrawable
                            }

                            sixPerformance.background?.let {
                                val wrappedDrawable = DrawableCompat.wrap(it)
                                if(result.data?.sixMonthReturn?.contains("-")?:false) {
                                    DrawableCompat.setTint(
                                        wrappedDrawable,
                                        ContextCompat.getColor(requireContext(), R.color.md_theme_error)
                                    )
                                }else{
                                    DrawableCompat.setTint(
                                        wrappedDrawable,
                                        ContextCompat.getColor(requireContext(), R.color.md_theme_primary)
                                    )
                                }
                                onePerformance.background = wrappedDrawable
                            }

                            oneYearPerformance.background?.let {
                                val wrappedDrawable = DrawableCompat.wrap(it)
                                if(result.data?.twelveMonthReturn?.contains("-")?:false) {
                                    DrawableCompat.setTint(
                                        wrappedDrawable,
                                        ContextCompat.getColor(requireContext(), R.color.md_theme_error)
                                    )
                                }else{
                                    DrawableCompat.setTint(
                                        wrappedDrawable,
                                        ContextCompat.getColor(requireContext(), R.color.md_theme_primary)
                                    )
                                }
                                onePerformance.background = wrappedDrawable
                            }
                        }


                        val res=sharedViewModel.mutableAllData.value
                        val item=res?.data?.filter { it.sYM.equals(requireActivity().intent.extras?.getString(
                            AppConstants.SYMBOL),true) }?.first()
                        binding.dayRange.setLow(item?.lP?.toFloat()?:0f,item?.hP?.toFloat()?:0f,item?.cL?.toFloat()?:0f)
//                       binding.dayRange.setLow(result.data?.oneMonthLow?.toFloat()?:0f,result.data?.oneMonthHigh?.toFloat()?:0f,item?.cL?.toFloat()?:0f)
                        binding.dayRange52.setLow(result.data?.twelveMonthLow?.toFloat()?:0f,result.data?.twelveMonthHigh?.toFloat()?:0f,item?.cL?.toFloat()?:0f)
                        binding.valueTrade.text = item?.cL.toString()
                        binding.netChange.text = "${if (item?.cH!! < 0.0) "" else "+"}${item?.cH.toString()} ${if (item?.cHP!! < 0.0) "" else "+"}${String.format("%.2f",item?.cHP)}%"
                        if(item?.cH!!<0.0) {
                            binding.netChange.setTextColor(android.graphics.Color.parseColor("#D01B10"))
                        }else{
                            binding.netChange.setTextColor(
                                ContextCompat.getColor(requireContext(),
                                    R.color.md_theme_secondaryFixed))
                        }
                        if(!TextUtils.isEmpty(item.iN)) {
                            val indices = item?.iN?.split("|")
                            indices?.forEach {
                                if (it.contains("kse 100", true)) {
                                    binding.kse100.visibility = View.VISIBLE

                                } else if (it.contains("kse 30", true)) {
                                    binding.kse30.visibility = View.VISIBLE
                                } else if (it.contains("kmi 30", true)) {
                                    binding.kmi30.visibility = View.VISIBLE
                                } else if (it.contains("kmi all", true)) {
                                    binding.shariah.visibility = View.VISIBLE
                                }
                            }
                        }
                        binding.volumeValue.text = Utils.commaFormat(item?.v?.toDouble())
                        binding.avgVolumeValue.text = Utils.commaFormat(result.data?.avgVolume12M?.toDouble(),true)
                        binding.companyName.text = item?.nM



                        binding.companyName.post {

                            if(binding.sector.lineCount>1 || binding.companyName.lineCount>1){
                                val layoutParams=binding.materialCardView2.layoutParams
                                layoutParams.height=Utils.dpToPx(240)
                                binding.materialCardView2.layoutParams=layoutParams
                            }
                        }

                        binding.sector.text = item?.sN


                    }catch(e:Exception){
                        e.printStackTrace()
                    }
                }
            }
        })
        sharedViewModel.mutableSnapShotChart.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> Utils.showError(requireView(),result.message)
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    val stockIndex=result.data?.stockIndex
                    binding.indexStockChart.setChartData(stockIndex?.date?.filter { it!=null }?.map { convertData(it) }?.toList()?: emptyList(), stockIndex?.index?.map { it.toFloat() }?: emptyList(),
                        stockIndex?.stock?.map { it.toFloat() }?: emptyList(),"Index", (requireActivity() as SnapshotActivity).symbol)
                }
            }
        })

        sharedViewModel.mutableChart.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Resource.Error -> Utils.showError(requireView(),result.message)
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    var interval=0
                    val candleEntry:ArrayList<CandleEntry>?= ArrayList()
                    result.data?.reversed()?.forEachIndexed { index, it ->
                        candleEntry?.add(
                            CandleEntry(index.toFloat(), it.tradingHigh.toFloat(),it.tradingLow.toFloat(),it.tradingOpen.toFloat(),it.tradingClose.toFloat())
                        )
                    }
                    binding.candlestickChart.setCandleData(candleEntry?: emptyList())

                    binding.lineChart.setEntries(result.data?.reversed()?.map {
                        interval+=1
                        Entry(interval.toFloat(),it.tradingHigh.toFloat())
                    },false,true)

                }
            }
        })

        sharedViewModel.mutableSnapTechnical.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Resource.Error -> Utils.showError(requireView(),result.message)
                is Resource.Loading -> {}
                is Resource.Success -> {


                    val data = result.data
                    if (data != null && !data.isJsonNull) {
                        val obj = data.asJsonArray[0].asJsonObject
                        val excludeKeys = setOf("sector_name","company_code", "company_name", "company_id","Beta","1 Month Performance","3 Month Performance","6 Month Performance","1 Year Performance",
                            "52 Week High","52 Week Low","52 Week Volume")

                        val entries = obj.entrySet().filterNot { it.key in excludeKeys }

                        entries.forEachIndexed { index, it ->
                            val childBinding = ItemKeyValueBinding.inflate(layoutInflater)

                            childBinding.key.text = when {
                                it.key.contains(Regex("""Simple\s+Mov\w+\s+Average""", RegexOption.IGNORE_CASE)) -> {
                                    // Extract number (e.g., 10, 20, 200) from the key
                                    val number = Regex("""\d+""").find(it.key)?.value ?: ""
                                    "SMA $number"
                                }

                                else -> {
                                    if(it.key.equals("macd daily",true)){
                                        macd = it.value.asString
                                    }

                                    if(it.key.equals("next signal",true)){
                                        if(macd.equals("buy",true)){
                                            "Sell When Price Closes Below"
                                        }else{
                                            "Buy When Price Closes Above"
                                        }
                                    }else {
                                        if(it.key.equals("current signal",true)){
                                            childBinding.tradingSignal.visibility = View.VISIBLE
                                        }
                                        it.key

                                    }
                                }
                            }
                            childBinding.value.text = try {

                                Utils.roundTwoDecimal(it.value.asString.toDouble())
                            } catch (e: NumberFormatException) {
                                if(it.value.asString.matches(Regex("""\d{1,4}[-/]\d{1,2}[-/]\d{1,4}"""))){
                                    Utils.formatDateString(
                                        it.value.asString,
                                        "M/d/yyyy",
                                        "dd-MMM-yyyy"
                                    )
                                }else {
                                    it.value.asString
                                }
                            }
                            /*childBinding.value.text =
                                Utils.roundTwoDecimal(it.value.asString.toDouble())*/

                            if (index == entries.size - 1) {
                                childBinding.divider.visibility = View.GONE
                            }else if (childBinding.key.text.toString().equals("SMA 200",true) ) {

                                val layoutParams = childBinding.divider.layoutParams
                                layoutParams.height = (4 * resources.displayMetrics.density).toInt() // 2dp to px
                                childBinding.divider.layoutParams = layoutParams
                            }

                            binding.linearContainer.addView(childBinding.root)
                        }
                        binding.apply {
                            loader.visibility = View.GONE
                            main.visibility = View.VISIBLE
                        }
                    }


//                    childBinding.value


                }
            }
        })

        return binding.root
    }

    private fun toggleDay(it: View?) {
        when(it?.id){
            binding.d1.id->{
                binding.apply {
                    d1.setChipSelected(true)
                    hr1.setChipSelected(false)
                    min30.setChipSelected(false)
                    min15.setChipSelected(false)
                    min5.setChipSelected(false)
                    min1.setChipSelected(false)
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"1D")
                }
            }


            binding.hr1.id->{
                binding.apply {
                    d1.setChipSelected(false)
                    hr1.setChipSelected(true)
                    min30.setChipSelected(false)
                    min15.setChipSelected(false)
                    min5.setChipSelected(false)
                    min1.setChipSelected(false)
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"60")
                }
            }


            binding.min30.id->{
                binding.apply {
                    d1.setChipSelected(false)
                    hr1.setChipSelected(false)
                    min30.setChipSelected(true)
                    min15.setChipSelected(false)
                    min5.setChipSelected(false)
                    min1.setChipSelected(false)
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"30")
                }
            }

            binding.min15.id->{
                binding.apply {
                    d1.setChipSelected(false)
                    hr1.setChipSelected(false)
                    min30.setChipSelected(false)
                    min15.setChipSelected(true)
                    min5.setChipSelected(false)
                    min1.setChipSelected(false)
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"15")
                }
            }


            binding.min5.id->{
                binding.apply {
                    d1.setChipSelected(false)
                    hr1.setChipSelected(false)
                    min30.setChipSelected(false)
                    min15.setChipSelected(false)
                    min5.setChipSelected(true)
                    min1.setChipSelected(false)
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"5")
                }
            }

            binding.min1.id->{
                binding.apply {
                    d1.setChipSelected(false)
                    hr1.setChipSelected(false)
                    min30.setChipSelected(false)
                    min15.setChipSelected(false)
                    min5.setChipSelected(false)
                    min1.setChipSelected(true)
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"1")
                }
            }

        }

    }

    private fun toggleLineChart(view: View) {
        if(view.id==binding.line.id){
            binding.line.setChipSelected(true)
            binding.candle.setChipSelected(false)
            binding.lineChart.visibility = View.VISIBLE
            binding.candlestickChart.visibility = View.GONE
            binding.apply {
                if(d1.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"1D")
                }

                if(hr1.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"60")
                }
                if(min30.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"30")
                }
                if(min15.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"15")
                }
                if(min5.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"5")
                }
                if(min1.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"1")
                }
            }

        }else{
            binding.line.setChipSelected(false)
            binding.candle.setChipSelected(true)
            binding.lineChart.visibility = View.GONE
            binding.candlestickChart.visibility = View.VISIBLE

            binding.apply {
                if(d1.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"1D")
                }

                if(hr1.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"60")
                }
                if(min30.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"30")
                }
                if(min15.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"15")
                }
                if(min5.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"5")
                }
                if(min1.isSelected){
                    sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"1")
                }
            }
        }

        binding.apply {
            if(!d1.isSelected && !hr1.isSelected && !min30.isSelected && !min15.isSelected && !min5.isSelected && !min1.isSelected){
                toggleDay(d1)
                sharedViewModel.fetchChart((requireActivity() as SnapshotActivity).symbol,"1D")
            }
        }

    }


    fun convertData(inputDate:String?): String? {
        val input = inputDate
        val inputFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM", Locale.getDefault())

        val date = inputFormat.parse(input)
        val month = outputFormat.format(date) // → "March"
        return month
    }

}