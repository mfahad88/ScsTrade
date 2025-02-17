package com.example.scstrade.views.landing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentLandingBinding
import com.example.scstrade.model.data.KeyDescValue
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.home.HomeFragment
import com.example.scstrade.views.market.MarketFragment
import com.example.scstrade.views.watchlist.WatchlistFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class LandingFragment : Fragment() {
    lateinit var binding: FragmentLandingBinding
    private lateinit var sharedViewModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentLandingBinding.inflate(inflater,container,false)
        initSideMenu()
        binding.bottomNavigationView.selectedItemId=R.id.homeFragment
        loadFragment(HomeFragment())
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
//        sharedViewModel.fetchAllData()
//        sharedViewModel.fetchIndices()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<MarginLayoutParams> {
                topMargin = 0
                leftMargin = systemBars.left
                rightMargin = systemBars.right
                bottomMargin = systemBars.bottom
            }
            WindowInsetsCompat.CONSUMED
        }
        sharedViewModel.mutableIndices.observe(requireActivity(), Observer {
            binding.mMarket.apply {
                if(it.data?.first()?.marketStatus.equals("CLOSE",true)){
                    close.visibility= View.VISIBLE
                    open.visibility = View.GONE
                }else{
                    close.visibility= View.GONE
                    open.visibility = View.VISIBLE
                }
                var sdf = SimpleDateFormat("dd MMM yyyy | hh:mma", Locale.ENGLISH);

                // Get the current date and time
                var formattedDate = sdf.format(Date())
                dateTime.text = formattedDate
            }
        })
        binding.imageViewClose.setOnClickListener {
            if(binding.drawerLayout.isDrawerOpen(GravityCompat.END)){
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
        }
        binding.bottomNavigationView.setOnItemSelectedListener {item ->
//            binding.bottomNavigationView.selectedItemId=item.itemId
            if(item.itemId==R.id.homeFragment){
                loadFragment(HomeFragment())
                true
            }else if(item.itemId==R.id.watchlistFragment){
                loadFragment(WatchlistFragment())
                true
            }else if(item.itemId==R.id.marketFragment){
                loadFragment(MarketFragment())
                true
            }

            if(binding.drawerLayout.isDrawerOpen(GravityCompat.END)){
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
            if (item.itemId==R.id.more){
                if(binding.drawerLayout.isDrawerOpen(GravityCompat.END)){
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                }else {
                    binding.drawerLayout.openDrawer(GravityCompat.END)
                }
                true
            }
            false

        }
        return binding.root
    }
    public fun loadFragment(fragment: Fragment, isBackStack:Boolean = false) {
        if(isBackStack){
            childFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment,"second_level")
                .addToBackStack(null)
                .commit()
        }else{
            childFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment,"second_level")
                .commit()
        }
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
                    val bundle=Bundle()
                    bundle.putString("key","indices")
                    val fragment = MarketFragment()
                    fragment.arguments = bundle
                    loadFragment(fragment,true)
//                    findNavController(R.id.nav_host_fragment).navigate(R.id.marketFragment,bundle)
                }else if(keyDescValue.key?.equals("all stocks",true)?:false){
                    val bundle=Bundle()
                    bundle.putString("key","allStocks")
                    val fragment = MarketFragment()
                    fragment.arguments = bundle
                    loadFragment(fragment,true)
//                    findNavController(R.id.nav_host_fragment).navigate(R.id.marketFragment,bundle)
                }
                binding.drawerLayout.closeDrawers()
            }
            layoutManager=
                LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL,false)
            val divider= DividerItemDecoration(binding.root.context, DividerItemDecoration.VERTICAL)
            divider.setDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.custom_divider)!!)
            addItemDecoration(divider)
        }
    }
}