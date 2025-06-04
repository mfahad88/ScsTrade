package com.example.scstrade.views.landing

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentLandingBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.data.KeyDescValue
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.announcement.AnnoucementActivity
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.contact.ContactActivity
import com.example.scstrade.views.detailquote.DetailQuoteActivity
import com.example.scstrade.views.fundamental.FundamentalActivity
import com.example.scstrade.views.home.HomeFragment
import com.example.scstrade.views.login.LoginFragment
import com.example.scstrade.views.main.MainActivity
import com.example.scstrade.views.market.MarketFragment
import com.example.scstrade.views.news.NewsFragment
import com.example.scstrade.views.notification.NotificationActivity
import com.example.scstrade.views.portfolio.activities.PortfolioActivity
import com.example.scstrade.views.profile.ProfileActivity
import com.example.scstrade.views.settings.SettingsActivity
import com.example.scstrade.views.technicals.TechnicalsActivity
import com.example.scstrade.views.watchlist.WatchlistFragment
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.reflect.TypeToken


class LandingFragment : Fragment() {
    lateinit var binding: FragmentLandingBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentLandingBinding.inflate(inflater,container,false)
        initSideMenu()

        binding.toolbar.binding.apply {
            toolbarWithLogo.visibility = View.VISIBLE
            titleItem.visibility = View.GONE
        }

    /*    ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar.binding.content) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.displayCutout())
            view.setPadding(0,insets.top,0,insets.bottom)
            windowInsets
        }*/
        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigationView){v,windowInsets->
            val insets= windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.updateLayoutParams<MarginLayoutParams> {
                bottomMargin=insets.bottom+31
            }
            windowInsets
        }
        Utils.setSystemBarIcons(requireActivity(),darkIcons = false)
        binding.bottomNavigationView.selectedItemId=R.id.homeFragment
        loadFragment(HomeFragment())
//        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel = (requireActivity().application as MyApp).viewModel
        binding.aof.setOnClickListener {
            startActivity(Intent(requireContext(),AofActivity::class.java))
        }





        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val fragmentManager = requireActivity().supportFragmentManager
                if (fragmentManager.backStackEntryCount > 0) {
                    // 🔙 Pop fragment from back stack
                    fragmentManager.popBackStack()
                } else {
                    // 🚪 Close the app
                    showExitDialog()
                }
            }

        })
   /*     sharedViewModel.mutableIndices.observe(requireActivity(), Observer {
            updateMarket(it)
        })*/
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

                loadFragment(HomeFragment())
                true
            }else if(item.itemId==R.id.watchlistFragment){
                loadFragment(WatchlistFragment())
                true
            }else if(item.itemId==R.id.marketFragment){
                loadFragment(MarketFragment())
                true
            }else if(item.itemId==R.id.news){
                loadFragment(NewsFragment())
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
            .setPositiveButton("Yes") { _, _ -> requireActivity().finish() } // 🚪 Close the app
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() } // ❌ Dismiss
            .show()
    }
  /*  private fun updateMarket(it: Resource<List<KSEIndices>>) {
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
    }*/


    public fun loadFragment(fragment: Fragment, isBackStack:Boolean = false) {
        if(isBackStack){
            childFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }else{
            childFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
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
//            KeyDescValue("SCS Portfolio",null,R.drawable.scs_portfolio),
            KeyDescValue("My Portfolio",null,R.drawable.my_portfolio),
            KeyDescValue("Announcements",null,R.drawable.announcements),
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
                    requireActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)

                }else if(keyDescValue.key?.equals("fundamental",true)?:false){
                    val intent = Intent(requireContext(),FundamentalActivity::class.java)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                }else if(keyDescValue.key?.equals("detailed quote",true)?:false){
                    val intent = Intent(requireContext(),DetailQuoteActivity::class.java)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                }else if(keyDescValue.key?.equals("announcements",true)?:false){
                    val intent = Intent(requireContext(),AnnoucementActivity::class.java)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                }else if(keyDescValue.key?.equals("My Portfolio",true)?:false){
                    val intent = Intent(requireContext(), PortfolioActivity::class.java)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
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

    override fun onResume() {
        fetchUser()
        super.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            val fragmentToOpen = data?.getStringExtra("fragment_to_open")
            if (fragmentToOpen != null) {
                loadFragment(WatchlistFragment())
            }

        }
    }
}