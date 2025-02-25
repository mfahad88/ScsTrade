package com.example.scstrade.views.landing

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Toast
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
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.data.KeyDescValue
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.fundamental.FundamentalActivity
import com.example.scstrade.views.home.HomeFragment
import com.example.scstrade.views.login.LoginFragment
import com.example.scstrade.views.main.MainActivity
import com.example.scstrade.views.market.MarketFragment
import com.example.scstrade.views.technicals.TechnicalsActivity
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
//        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel = (requireActivity().application as MyApp).viewModel
        binding.toolbar.searchIcon.setOnClickListener {
            Toast.makeText(requireContext(),"Clicked...",Toast.LENGTH_SHORT).show()
        }
       /* requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {

            }

        })*/
//        sharedViewModel.fetchAllData()
//        sharedViewModel.fetchIndices()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<MarginLayoutParams> {
                topMargin = systemBars.top
                leftMargin = systemBars.left
                rightMargin = systemBars.right
                bottomMargin = systemBars.bottom
            }
            WindowInsetsCompat.CONSUMED
        }
        sharedViewModel.mutableIndices.observe(requireActivity(), Observer {
            updateMarket(it)
        })
        binding.imageViewClose.setOnClickListener {
            if(binding.drawerLayout.isDrawerOpen(GravityCompat.END)){
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
        }
        binding.bottomNavigationView.setOnItemSelectedListener {item ->
//            binding.bottomNavigationView.selectedItemId=item.itemId
            item.setChecked(true)
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

    private fun updateMarket(it: Resource<List<KSEIndices>>) {
        binding.mMarket.apply {
            if(it.data?.isNotEmpty()?:false){
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
        }
    }


    public fun loadFragment(fragment: Fragment, isBackStack:Boolean = false) {
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

    private fun initSideMenu() {
        val list:List<KeyDescValue> = listOf(
            KeyDescValue("Indices",null,R.drawable.indices),
            KeyDescValue("All Stocks",null,R.drawable.all_stocks),
            KeyDescValue("Detailed Quote",null,R.drawable.detail_quote),
            KeyDescValue("Fundamental",null,R.drawable.fundamental),
            KeyDescValue("Technical",null,R.drawable.technicals),
            KeyDescValue("SCS Portfolio",null,R.drawable.scs_portfolio),
            KeyDescValue("My Portfolio",null,R.drawable.my_portfolio),
            KeyDescValue("Announcements",null,R.drawable.announcements),
            KeyDescValue("Logout",null,R.drawable.baseline_power_settings_new_24)
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
                }else if(keyDescValue.key?.equals("all stocks",true)?:false){
                    val bundle=Bundle()
                    bundle.putString("key","allStocks")
                    val fragment = MarketFragment()
                    fragment.arguments = bundle
                    loadFragment(fragment,true)
                }else if(keyDescValue.key?.equals("logout",true)?:false){
                    Utils.removeSharedPrefence(requireContext(),AppConstants.USER)
                    Utils.removeSharedPrefence(requireContext(),AppConstants.IS_REMEMBER)
                    (requireActivity() as MainActivity).loadFragment(LoginFragment())
                }else if(keyDescValue.key?.equals("technical",true)?:false){
                    val intent = Intent(requireContext(),TechnicalsActivity::class.java)
                    startActivity(intent)
                }else if(keyDescValue.key?.equals("fundamental",true)?:false){
                    val intent = Intent(requireContext(),FundamentalActivity::class.java)
                    startActivity(intent)
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