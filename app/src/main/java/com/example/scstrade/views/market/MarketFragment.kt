package com.example.scstrade.views.market

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentMarketBinding
import com.example.scstrade.databinding.FragmentWatchlistBinding
import com.example.scstrade.views.allstock.AllStockFragment
import com.example.scstrade.views.indices.IndicesFragment
import com.example.scstrade.views.landing.LandingActivity
import com.example.scstrade.views.sector.SectorFragment
import com.google.android.material.tabs.TabLayout


class MarketFragment : Fragment() {

    private lateinit var binding:FragmentMarketBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMarketBinding.inflate(inflater,container,false)
        initSelection()
        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab?.text?.toString().equals("indices",true)){
                    loadFragment(IndicesFragment())
//                    navController.navigate(R.id.indicesFragment)
                }else if(tab?.text?.toString().equals("all stocks",true)){
//                    navController.navigate(R.id.allStockFragment)
                 loadFragment(AllStockFragment())
                }else if(tab?.text.toString().equals("sectors",true)){
                    loadFragment(SectorFragment())
                }else if(tab?.text.toString().equals("Shariah",true)){
                    val fragment=AllStockFragment()
                    val bundle=Bundle()
                    bundle.putString("kmi","true")
                    fragment.arguments=bundle
                    loadFragment(fragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        return binding.root
    }

    private fun initSelection() {
        if(arguments?.getString("key").equals("indices",true)) {
            loadFragment(IndicesFragment())
            binding.tabLayout.getTabAt(0)?.select()
        }else if(arguments?.getString("key").equals("allStocks",true)){
            loadFragment(AllStockFragment())
            binding.tabLayout.getTabAt(1)?.select()
        }else{
            loadFragment(IndicesFragment())
            binding.tabLayout.getTabAt(0)?.select()
        }
    }

    private fun loadFragment(fragment: Fragment, isBackStack:Boolean = false) {
        if(isBackStack){
            childFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }else{
            childFragmentManager

                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }
}