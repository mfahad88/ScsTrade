package com.example.scstrade.views.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentHomeBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.landing.LandingActivity
import com.example.scstrade.views.stock.StockAdapter
import com.example.scstrade.views.widgets.HorizontalDivider
import com.github.mikephil.charting.data.CandleEntry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel.fetchAllData()

    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as LandingActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
      /*  val appConfiguration= AppBarConfiguration(*//*findNavController().graph*//*setOf(
            R.id.indicesFragment
        ))
        (requireActivity() as LandingActivity).binding.toolbar.customToolbar.setupWithNavController(findNavController(),appConfiguration)
        (requireActivity() as LandingActivity).binding.toolbar.mainItem.visibility=View.VISIBLE*/
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater,container,false)
        val dataList = listOf(
            CandleEntry(5f, 100f, 110f, 95f, 105f), // Timestamp in milliseconds
            CandleEntry(10f, 105f, 115f, 100f, 110f),
            CandleEntry(15F, 110f, 120f, 105f, 115f)
        )
        initCandleStick(dataList)



        binding.recyclerLeaders.apply {
            adapter=StockAdapter(emptyList())
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(30))
            isNestedScrollingEnabled=true
        }

        binding.recyclerGainers.apply {
            adapter=StockAdapter(emptyList())
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(30))
            isNestedScrollingEnabled=true
        }
        binding.recyclerLosers.apply {
            adapter=StockAdapter(emptyList())
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(30))
            isNestedScrollingEnabled=true
        }
        viewModel.mutableIndices.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    Log.e("Home: ",result.data.toString())
                    if(!viewModel.isLoading){

                        initMarket(result.data)
                        initSelection()
                        binding.cardHome.imageViewDropDown.setOnClickListener {

                            showDropdownMenu(binding.cardHome.imageViewDropDown,result.data,binding.cardHome.kmiallshr)
                        }
                        binding.apply {
                            main.visibility = View.VISIBLE
                            loader.visibility = View.GONE
                        }
                    }
                }
            }
        })

        viewModel.mutableAllData.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    (binding.recyclerLeaders.adapter as StockAdapter).addItems(result.data?.sortedByDescending { it.v }?.take(10)?: emptyList())
                    (binding.recyclerGainers.adapter as StockAdapter).addItems(result.data?.sortedByDescending { it.cHP }?.take(10)?: emptyList())
                    (binding.recyclerLosers.adapter as StockAdapter).addItems(result.data?.sortedBy { it.cHP }?.take(10)?: emptyList())
                }
            }
        })

        viewModel.mutableChart.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    var interval=0
                    initCandleStick( result.data?.map {
                        interval+=5
                        CandleEntry(interval.toFloat(),it.tradingHigh.toFloat(),it.tradingLow.toFloat(),it.tradingOpen.toFloat(),it.tradingClose.toFloat())
                    })

                }
            }
        })

        return binding.root
    }

    private fun initCandleStick(entries: List<CandleEntry>?) {
        if(entries!=null) {
            binding.cardHome.candlestickChart.setCandleData(entries)
        }

    }

    private fun initSelection() {
        var kseIndices=viewModel.selectedIndex
        binding.cardHome.apply {
            kmiallshr.text=kseIndices?.iNDEXCODE
            tradeValueView.text=Utils.convertToMillions(kseIndices?.vALUETRADED?.toDouble()?:0.0)
            if(kseIndices?.nETCHANGE?.contains("-")?:false) {
                tradeValueView.drawable =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.drop_down)
            }else{
                tradeValueView.drawable =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.drop_up)
            }
            netChangeChip.text = "${kseIndices?.nETCHANGE} (${String.format("%.2f",(kseIndices?.nETCHANGE?.toDouble()?.div(kseIndices?.preClose?:0.0))?.times(100))}%)"
            volumeChip.text="Volume: ${Utils.convertToMillions(kseIndices?.vOLUMETRADED?.toDouble()?:0.0)}"
            highView.text = "H: ${kseIndices?.hIGHINDEX} ${String.format("%.2f",kseIndices?.hIGHINDEX?.toDouble()?.minus(kseIndices?.preClose?:0.0))} " +
                    "(${String.format("%.2f",(kseIndices?.hIGHINDEX?.toDouble()?.minus(kseIndices?.preClose?:0.0))?.div(kseIndices?.preClose?:1.0)?.times(100))}%)"
            lowView.text = "L: ${kseIndices?.lOWINDEX} ${String.format("%.2f",kseIndices?.lOWINDEX?.toDouble()?.minus(kseIndices?.preClose?:0.0))} " +
                    "(${String.format("%.2f",(kseIndices?.lOWINDEX?.toDouble()?.minus(kseIndices?.preClose?:0.0))?.div(kseIndices?.preClose?:1.0)?.times(100))}%)"


            if(kmiallshr.text.toString().lowercase().contains("kse all")){
                viewModel.fetchChart("kseall",1)
            }else if(kmiallshr.text.toString().lowercase().contains("kse 100")){
                viewModel.fetchChart("kse",1)
            }else if(kmiallshr.text.toString().lowercase().contains("kse 30")){
                viewModel.fetchChart("kse30",1)
            }else if(kmiallshr.text.toString().lowercase().contains("kmi 30")){
                viewModel.fetchChart("kmi30",1)
            }
        }
    }

    private fun showDropdownMenu(view: View, list: List<KSEIndices>?, textView: TextView) {
        val popupMenu = PopupMenu(requireContext(), view)

        list?.forEach{
            popupMenu.getMenu().add(it.iNDEXCODE)
        }


        popupMenu.setOnMenuItemClickListener { item ->
            viewModel.selectedIndex = list?.filter { it.iNDEXCODE.equals(item.title.toString(),true)}?.first()
            initSelection()
            textView.setText(item.getTitle()) // Set selected option
            true
        }

        popupMenu.show()
    }
    private fun initMarket(data: List<KSEIndices>?) {
        if(data?.first()?.marketStatus.equals("open",true)){
            binding.mMarket.open.visibility = View.VISIBLE
            binding.mMarket.close.visibility = View.GONE
        }else{
            binding.mMarket.open.visibility = View.GONE
            binding.mMarket.close.visibility = View.VISIBLE
        }
        var sdf = SimpleDateFormat("dd MMM yyyy | hh:mma", Locale.ENGLISH);

        // Get the current date and time
        var formattedDate = sdf.format(Date())
        binding.mMarket.dateTime.text = formattedDate

    }


}