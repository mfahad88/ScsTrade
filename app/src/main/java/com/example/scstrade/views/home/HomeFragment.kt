package com.example.scstrade.views.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentHomeBinding
import com.example.scstrade.model.Resource
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.repository.IndicesRepository
import com.example.scstrade.services.RetrofitInstance
import com.example.scstrade.viewmodels.IndicesViewModel
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.register.IndexAdapter
import com.example.scstrade.views.stock.StockAdapter
import com.example.scstrade.views.widgets.HorizontalDivider
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater,container,false)
        val entries = listOf(
            CandleEntry(1f, 220f, 180f, 200f, 210f),
            CandleEntry(2f, 240f, 190f, 210f, 230f),
            CandleEntry(3f, 250f, 200f, 230f, 240f),
            CandleEntry(4f, 260f, 210f, 240f, 250f)
        )

        binding.cardHome.candlestickChart.setCandleData(entries)
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
                    initMarket(result.data)
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
     /*   val entries=ArrayList<Entry>()
        entries.add(Entry(0f, 5f))
        entries.add(Entry(1f, 10f))
        entries.add(Entry(2f, 12f))
        entries.add(Entry(3f, 9f))
        entries.add(Entry(4f, 5f))


        binding.lineChart.lineColor=ContextCompat.getColor(requireContext(), R.color.md_theme_inversePrimary_highContrast)
        binding.lineChart.filledColor=ContextCompat.getColor(requireContext(),R.color.md_theme_secondaryFixedDim)
        binding.lineChart.entries=entries*/


        return binding.root
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