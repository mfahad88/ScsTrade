package com.example.scstrade.views.landing

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog

import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentLandingBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.data.KeyDescValue
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.analystopinion.AnalystOpinionActivity
import com.example.scstrade.views.announcement.AnnoucementActivity
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.contact.ContactActivity
import com.example.scstrade.views.detailquote.DetailQuoteActivity
import com.example.scstrade.views.globalmarket.GlobalMarketActivity
import com.example.scstrade.views.home.HomeFragment
import com.example.scstrade.views.login.LoginFragment
import com.example.scstrade.views.main.MainActivity
import com.example.scstrade.views.market.MarketFragment
import com.example.scstrade.views.news.NewsFragment
import com.example.scstrade.views.portfolio.activities.PortfolioActivity
import com.example.scstrade.views.profile.ProfileActivity
import com.example.scstrade.views.reports.ReportActivity
import com.example.scstrade.views.settings.SettingsActivity
import com.example.scstrade.views.stockscreener.StockScreenerActivity
import com.example.scstrade.views.watchlist.WatchlistFragment
import com.example.scstrade.views.widgets.SideBarDivider
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.cancel


class LandingFragment : Fragment() {
    lateinit var binding: FragmentLandingBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private val ROOT_FRAGMENT:String="Home"



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentLandingBinding.inflate(inflater,container,false)
        initSideMenu()




        Utils.setSystemBarIcons(requireActivity(),darkIcons = false)
        binding.bottomNavigationView.selectedItemId=R.id.homeFragment
        loadFragment("Home"){
            HomeFragment()
        }
        binding.toolbar.binding.market.text = getString(R.string.scs_trade_p)
        sharedViewModel = (requireActivity().application as MyApp).viewModel
        binding.aof.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
//            Toast.makeText(requireContext(),"Working In Progress under fixes", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(),AofActivity::class.java))
        }
        if((requireActivity() as MainActivity).intent.getBooleanExtra(AppConstants.IS_MARKET,false)){
            binding.toolbar.binding.market.text = "Market"
            loadFragment("Market"){
                MarketFragment()
            }
            binding.bottomNavigationView.selectedItemId= R.id.marketFragment
        }

        childFragmentManager.addOnBackStackChangedListener {
            val count = childFragmentManager.backStackEntryCount
            val fragment = childFragmentManager.findFragmentById(R.id.fragment_container)

            Log.d("BackStackListener", "Back stack count: $count")
            Log.d("BackStackListener", "Current fragment: ${fragment?.javaClass?.simpleName}")
            val fragmentName = fragment?.javaClass?.simpleName
            if(fragmentName?.contains("Watchlist",true)?:false){
                binding.bottomNavigationView.selectedItemId= R.id.watchlistFragment
            }else if(fragmentName?.contains("Market",true)?:false){
                binding.bottomNavigationView.selectedItemId= R.id.marketFragment
            }else if(fragmentName?.contains("News",true)?:false){
                binding.bottomNavigationView.selectedItemId= R.id.news
            }else{
                binding.bottomNavigationView.selectedItemId= R.id.homeFragment
            }
        }


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val fragmentManager = childFragmentManager
                if(binding.drawerLayout.isDrawerOpen(GravityCompat.END)){
                    binding.drawerLayout.closeDrawers()
                }else {

                    if (fragmentManager.backStackEntryCount > 1) {
                        // 🔙 Pop fragment from back stack
                        fragmentManager.popBackStack()


                    } else {
                        // 🚪 Close the app
                        showExitDialog()
                    }
                }
            }

        })
   /*     sharedViewModel.mutableIndices.observe(requireActivity(), Observer {
            updateMarket(it)
        })*/
        binding.globalMarket.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
            startActivity(Intent(requireContext(),GlobalMarketActivity::class.java))
        }
        
        binding.profile.setOnClickListener {
            startActivity(Intent(requireContext(),ProfileActivity::class.java))
        }
        binding.imageViewClose.setOnClickListener {
            if(binding.drawerLayout.isDrawerOpen(GravityCompat.END)){
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
        }
        binding.bottomNavigationView.setOnItemSelectedListener {item ->
//            binding.bottomNavigationView.selectedItemId=item.itemId
            item.setChecked(true)
            if(item.itemId==R.id.homeFragment){
//                binding.toolbar.toggleToolbar(true)
//                binding.toolbar.binding.tickerScroll.visibility = View.GONE
//                binding.toolbar.binding.constraintMarketStat.visibility = View.VISIBLE
                loadFragment("Home"){
                    HomeFragment()
                }
                binding.toolbar.binding.market.text = getString(R.string.scs_trade_p)
                true
            }else if(item.itemId==R.id.watchlistFragment){
                // binding.toolbar.toggleToolbar(false)
                binding.toolbar.binding.market.text = "Watchlist"
                loadFragment("WatchList"){
                    WatchlistFragment()
                }
                true
            }else if(item.itemId==R.id.marketFragment){
                // binding.toolbar.toggleToolbar(false)
                binding.toolbar.binding.market.text = "Market"
                loadFragment("Market"){
                    MarketFragment()
                }
                true
            }else if(item.itemId==R.id.news){
                // binding.toolbar.toggleToolbar(false)
                binding.toolbar.binding.market.text = "News"
                loadFragment("News"){
                    NewsFragment()
                }
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

    private fun fetchUser() {
        val listType = object : TypeToken<List<LoginDataItem>>() {}
        val user= Utils.getSharedPreference(requireContext(), emptyList<LoginDataItem>(),AppConstants.USER,listType)
        (requireActivity().application as MyApp).login=user.first()
        (requireActivity().application as MyApp).login.apply {
            binding.profileName.text=registrationName
            binding.profileEmail.text = registrationEmail
            binding.roundedAvatar.text = registrationName!![0].toString()
        }
    }

    private fun showExitDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Exit App")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ ->
                (requireActivity().application as MyApp).appScope.cancel()
                requireActivity().finishAffinity()

            } // 🚪 Close the app
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() } // ❌ Dismiss
            .show()
    }

    fun loadFragment(tag: String, newInstance: () -> Fragment) {
        val fm = childFragmentManager
        val current = fm.findFragmentById(R.id.fragment_container)

        val tx = fm.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .setReorderingAllowed(true)

        // 1. Hide the fragment that’s currently visible
        current?.let { tx.hide(it) }

        // 2. Look for a cached instance with this tag
        val cached = fm.findFragmentByTag(tag)

        if (cached == null) {
            // First time → add, *not* replace
            tx.add(R.id.fragment_container, newInstance(), tag)
            tx.addToBackStack(tag)             // makes Back button work

        } else {
            // Second time → just show the already‑created instance
            tx.show(cached)
            fm.popBackStack(tag, 0)            // bring its BackStackEntry to top
        }

        tx.commit()
    }

    private fun initSideMenu() {
        val list:List<KeyDescValue> = listOf(
//            KeyDescValue("Home",null,R.drawable.side_home),
            KeyDescValue("PSX Market",null,R.drawable.side_psx_market),
            KeyDescValue("Watchlist",null,R.drawable.side_watchlist),
            KeyDescValue("News",null,R.drawable.side_news),
            KeyDescValue("Announcements",null,R.drawable.announcements),
            KeyDescValue("Detailed Quote",null,R.drawable.side_detail_quote),
            KeyDescValue("Stock Screener",null,R.drawable.side_fundamental),
            /*KeyDescValue("Fundamental",null,R.drawable.side_fundamental),
            KeyDescValue("Technical",null,R.drawable.side_technical),*/
//            KeyDescValue("SCS Portfolio",null,R.drawable.side_scs_portfolio),
            KeyDescValue("My Portfolio",null,R.drawable.side_scs_portfolio),
            KeyDescValue("Research Reports",null,R.drawable.side_news),
            KeyDescValue("Analyst Opinion",null,R.drawable.side_scs_portfolio),
            KeyDescValue("Logout",null,R.drawable.baseline_power_settings_new_24)
        )
        binding.contact.setOnClickListener {
            startActivity(Intent(requireContext(),ContactActivity::class.java))
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        }

        binding.appSettings.setOnClickListener {
            startActivity(Intent(requireContext(),SettingsActivity::class.java))
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        }
        binding.sideMenu.apply {
            adapter= SideMenuAdapter(list){ keyDescValue ->

                System.out.println("Clicked: ${keyDescValue.toString()}")
                if(keyDescValue.key?.equals("psx market",true)?:false){
                    binding.bottomNavigationView.selectedItemId = R.id.marketFragment
                } else if(keyDescValue.key?.equals("psx market",true)?:false){
                    // binding.toolbar.toggleToolbar(false)
                    binding.toolbar.binding.market.text = "Market"
                    val bundle=Bundle()
                    bundle.putString("key","indices")
                    val fragment = MarketFragment()
                    fragment.arguments = bundle
                    binding.bottomNavigationView.selectedItemId = R.id.marketFragment
//                    loadFragment("Market"){fragment}
                }else if(keyDescValue.key?.equals("all stocks",true)?:false){
                    // binding.toolbar.toggleToolbar(false)
                    binding.toolbar.binding.market.text = "Market"
                    val bundle=Bundle()
                    bundle.putString("key","allStocks")
                    val fragment = MarketFragment()
                    fragment.arguments = bundle
//                    loadFragment("AllStocks"){fragment}
                    binding.bottomNavigationView.selectedItemId = R.id.marketFragment
                }else if(keyDescValue.key?.equals("Watchlist",true)?:false){
                    binding.bottomNavigationView.selectedItemId = R.id.watchlistFragment
//                    loadFragment("Watchlist"){ WatchlistFragment() }
                }else if(keyDescValue.key?.equals("news",true)?:false){
                    binding.bottomNavigationView.selectedItemId = R.id.news
//                    loadFragment("news"){ NewsFragment() }
                } else if(keyDescValue.key?.equals("logout",true)?:false){
                    Utils.removeSharedPrefence(requireContext(),AppConstants.USER)
                    Utils.removeSharedPrefence(requireContext(),AppConstants.IS_REMEMBER)
                    (requireActivity() as MainActivity).loadFragment(LoginFragment())
                }else if (keyDescValue.key?.equals("Research Reports",true)?:false){
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                    val intent = Intent(requireContext(), ReportActivity::class.java)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                }else if (keyDescValue.key?.equals("Analyst Opinion",true)?:false){
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                    val intent = Intent(requireContext(), AnalystOpinionActivity::class.java)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                }
                /*else if(keyDescValue.key?.equals("technical",true)?:false){

                    val intent = Intent(requireContext(), TechnicalsFragment::class.java)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)

                }*/else if(keyDescValue.key?.equals("Stock Screener",true)?:false){
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                    val intent = Intent(requireContext(), StockScreenerActivity::class.java)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                }else if(keyDescValue.key?.equals("detailed quote",true)?:false){
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                    val intent = Intent(requireContext(),DetailQuoteActivity::class.java)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                }else if(keyDescValue.key?.equals("announcements",true)?:false){
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                    val intent = Intent(requireContext(),AnnoucementActivity::class.java)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                }else if(keyDescValue.key?.equals("My Portfolio",true)?:false){
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                    val intent = Intent(requireContext(), PortfolioActivity::class.java)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                }
                binding.drawerLayout.closeDrawers()
            }
            layoutManager=
                LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL,false)
//            val divider= DividerItemDecoration(binding.root.context, DividerItemDecoration.VERTICAL)
//            divider.setDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.custom_divider)!!)
            val divider = SideBarDivider(
                dividerColor = Color.parseColor("#B3C6C6CD"),
                marginEnd = 80
            )
            addItemDecoration(divider)

        }
    }

    override fun onResume() {
        fetchUser()

        super.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            val fragmentToOpen = data?.getStringExtra("fragment_to_open")
            if (fragmentToOpen != null) {
                loadFragment("WatchList"){WatchlistFragment()}
            }

        }
    }
}