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
import com.google.android.material.tabs.TabLayout


class MarketFragment : Fragment() {

    private lateinit var binding:FragmentMarketBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMarketBinding.inflate(inflater,container,false)
        val navHostFragment= childFragmentManager.findFragmentById(R.id.nav_host_market) as NavHostFragment
        val navController = navHostFragment.navController
        binding.tabLayout.getTabAt(0)?.select()
        navController.navigate(R.id.indicesFragment)
        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab?.text?.toString().equals("indices",true)){
                    navController.navigate(R.id.indicesFragment)
                }else if(tab?.text?.toString().equals("all stocks",true)){
                    navController.navigate(R.id.allStockFragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        return binding.root
    }

}