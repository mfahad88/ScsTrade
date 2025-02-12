package com.example.scstrade.views.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityLandingBinding
import com.example.scstrade.model.data.KeyDescValue
import com.example.scstrade.viewmodels.SharedViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class LandingActivity : AppCompatActivity() {
    lateinit var binding:ActivityLandingBinding
    private val sharedViewModel:SharedViewModel by viewModels()

    private var scaleFactor = 1.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLandingBinding.inflate(LayoutInflater.from(this))
        enableEdgeToEdge()
        setContentView(binding.root)

        initSideMenu()

        sharedViewModel.fetchAllData()
        sharedViewModel.fetchIndices()
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
        val navHostFragment= supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val topLevelDestinations = HashSet<Int>()
        topLevelDestinations.add(R.id.homeFragment)
        topLevelDestinations.add(R.id.indicesFragment)
        topLevelDestinations.add(R.id.stockFragment)


        val appBarConfiguration = AppBarConfiguration.Builder(topLevelDestinations).build()
//        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.customToolbar.setupWithNavController(navController,appBarConfiguration)

        binding.bottomNavigationView.setupWithNavController(navController)
//        binding.drawerNavView.setupWithNavController(navController)
        sharedViewModel.mutableIndices.observe(this, Observer {
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
            val options=NavOptions.Builder().setPopUpTo(R.id.nav_bottom_graph,true).build()
            if(item.itemId==R.id.homeFragment){
                findNavController(R.id.nav_host_fragment).navigate(R.id.homeFragment,null,options)
                true
            }else if(item.itemId==R.id.watchlistFragment){
                findNavController(R.id.nav_host_fragment).navigate(R.id.watchlistFragment,null,options)
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
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController=findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
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
            val options=NavOptions.Builder().setPopUpTo(R.id.nav_bottom_graph,true).build()
            adapter= SideMenuAdapter(list){ keyDescValue ->
                System.out.println("Clicked: ${keyDescValue.toString()}")
                if(keyDescValue.key?.equals("indices",true)?:false){

                    findNavController(R.id.nav_host_fragment).navigate(R.id.indicesFragment)
                }else if(keyDescValue.key?.equals("all stocks",true)?:false){
                    findNavController(R.id.nav_host_fragment).navigate(R.id.allStockFragment)
                }
                binding.drawerLayout.closeDrawers()
            }
            layoutManager=
                LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL,false)
            val divider= DividerItemDecoration(binding.root.context, DividerItemDecoration.VERTICAL)
            divider.setDrawable(AppCompatResources.getDrawable(this@LandingActivity,R.drawable.custom_divider)!!)
            addItemDecoration(divider)
        }
    }

    override fun onResume() {
        super.onResume()


    }

    override fun onStop() {
        super.onStop()
        sharedViewModel.stopAll()
    }
    /*override fun onPause() {
        super.onPause()
        sharedViewModel.stopAll()
    }
*/

}