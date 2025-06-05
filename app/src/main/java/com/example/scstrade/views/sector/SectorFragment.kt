package com.example.scstrade.views.sector

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.databinding.FragmentSectorBinding
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.stock.StockItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.allstock.AllStockFragment
import com.example.scstrade.views.allstock.StockActivity
import com.example.scstrade.views.landing.LandingFragment
import com.example.scstrade.views.market.MarketFragment


class SectorFragment : Fragment() {
    lateinit var sharedViewModel:SharedViewModel
    lateinit var binding: FragmentSectorBinding
    lateinit var observer: Observer<Resource<List<StockItem>>>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSectorBinding.inflate(inflater,container,false)

    /*    ViewCompat.setOnApplyWindowInsetsListener(
            binding.recyclerViewSector
        ) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.setPadding(0, 0, 0, insets.bottom+32)
            Toast.makeText(requireContext(),insets.bottom.toString(),Toast.LENGTH_SHORT).show()
            windowInsets
        }*/
//        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel = (requireActivity().application as MyApp).viewModel
        binding.recyclerViewSector.apply {
            adapter=SectorAdapter(emptyList()){
                val bundle =Bundle()
                bundle.putString("sector",it)
                val fragment=AllStockFragment()
                fragment.arguments=bundle
                val intent= Intent(requireContext(), StockActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)

//              loadFragment(fragment,it)
            }
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }

        binding.recyclerViewSector.post {
            binding.recyclerViewSector.setPadding(0,0,0,350)
            binding.recyclerViewSector.clipToPadding=false
        }
        sharedViewModel.mutableAllData.observe(viewLifecycleOwner, object : Observer<Resource<List<StockItem>>>{
            override fun onChanged(it: Resource<List<StockItem>>) {
                (binding.recyclerViewSector.adapter as SectorAdapter).addItems(it.data?.map { it.sN }?.distinct()?.sortedBy { it }?: emptyList())
                /*  binding.apply {
                      loader.visibility=View.GONE
                      recyclerViewSector.visibility=View.VISIBLE
                  }*/
                sharedViewModel.mutableAllData.removeObserver(this)
            }

        })

        return binding.root
    }



    fun loadFragment(fragment: Fragment, s: String){
        ((parentFragment as MarketFragment).parentFragment as LandingFragment).loadFragment(fragment,true)
        /*(requireActivity() as MainActivity).loadFragment(fragment,true)
        (requireActivity() as MainActivity).binding.toolbar.visibility=View.VISIBLE
        (requireActivity() as MainActivity).binding.title.text = s*/

    }
}