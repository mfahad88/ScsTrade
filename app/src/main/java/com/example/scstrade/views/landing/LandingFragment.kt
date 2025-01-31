package com.example.scstrade.views.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentLandingBinding


/**
 * A simple [Fragment] subclass.
 * Use the [LandingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LandingFragment : Fragment() {
    lateinit var binding: FragmentLandingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding=FragmentLandingBinding.inflate(inflater,container,false)
        binding.drawerLayout.closeDrawer(GravityCompat.END)
        initSideMenu()
        binding.bottomNavigationView.setupWithNavController(
            findNavController()
        )
        binding.bottomNavigationView.setOnItemSelectedListener {item ->
            if(binding.drawerLayout.isDrawerOpen(GravityCompat.END)){
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
            if(item.itemId==R.id.home){
                findNavController().navigate(R.id.homeFragment)
            }else if (item.itemId==R.id.more){
                if(binding.drawerLayout.isDrawerOpen(GravityCompat.END)){
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                }else {
                    binding.drawerLayout.openDrawer(GravityCompat.END)
                }
            }
            true
        }



        return binding.root
    }

    private fun initSideMenu() {
        val map= mapOf(
            AppCompatResources.getDrawable(requireContext(),R.drawable.indices) to "Indices",
            AppCompatResources.getDrawable(requireContext(),R.drawable.all_stocks) to "All Stocks",
            AppCompatResources.getDrawable(requireContext(),R.drawable.detail_quote) to "Detailed Quote",
            AppCompatResources.getDrawable(requireContext(),R.drawable.fundamental) to "Fundamental",
            AppCompatResources.getDrawable(requireContext(),R.drawable.technicals) to "Technical",
            AppCompatResources.getDrawable(requireContext(),R.drawable.scs_portfolio) to "SCS Portfolio",
            AppCompatResources.getDrawable(requireContext(),R.drawable.my_portfolio) to "My Portfolio",
            AppCompatResources.getDrawable(requireContext(),R.drawable.announcements) to "Announcements",
        )

        binding.sideMenu.apply {
            adapter= SideMenuAdapter(map)
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
    }


}