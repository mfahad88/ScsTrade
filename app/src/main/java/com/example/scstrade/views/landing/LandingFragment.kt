package com.example.scstrade.views.landing

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentLandingBinding
import com.example.scstrade.model.data.KeyDescValue
import com.example.scstrade.views.widgets.VerticalDivider


/**
 * A simple [Fragment] subclass.
 * Use the [LandingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LandingFragment : Fragment() {
    lateinit var binding: FragmentLandingBinding
    @SuppressLint("RestrictedApi")
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
        binding.drawerNavView.setupWithNavController(
            findNavController()
        )



        Log.e("Graph",findNavController().graph.startDestDisplayName)
        /*binding.bottomNavigationView.setOnItemSelectedListener {item ->
            if(binding.drawerLayout.isDrawerOpen(GravityCompat.END)){
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
            if (item.itemId==R.id.more){
                if(binding.drawerLayout.isDrawerOpen(GravityCompat.END)){
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                }else {
                    binding.drawerLayout.openDrawer(GravityCompat.END)
                }
            }

            true
        }*/


        return binding.root
    }

    private fun initSideMenu() {
        val list:List<KeyDescValue> = listOf(
            KeyDescValue("Indices",null,R.drawable.indices),
            KeyDescValue("All Stocks",null,R.drawable.all_stocks),
            KeyDescValue("Detailed Quote",null,R.drawable.detail_quote),
            KeyDescValue("Fundamental",null,R.drawable.fundamental),
            KeyDescValue("Technical",null,R.drawable.technicals),
            KeyDescValue("SCS Portfolio",null,R.drawable.scs_portfolio),
            KeyDescValue("My Portfolio",null,R.drawable.my_portfolio),
            KeyDescValue("Announcements",null,R.drawable.announcements)
        )

        binding.sideMenu.apply {
            adapter= SideMenuAdapter(list){ keyDescValue ->
                System.out.println("Clicked: ${keyDescValue.toString()}")
                if(keyDescValue.key?.equals("indices",true)?:false){
                    findNavController().navigate(R.id.indicesFragment)
//                    findNavController().navigate(R.id.action_landingFragment_to_indicesFragment)
                }
                binding.drawerLayout.closeDrawers()
            }
            layoutManager=LinearLayoutManager(binding.root.context,LinearLayoutManager.VERTICAL,false)
            val divider=DividerItemDecoration(binding.root.context,DividerItemDecoration.VERTICAL)
            divider.setDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.custom_divider)!!)
            addItemDecoration(divider)
        }
    }


}