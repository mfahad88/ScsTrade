package com.example.scstrade.views.announcement
import androidx.compose.ui.res.dimensionResource

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityAnnoucementBinding
import com.example.scstrade.factories.SnapshotViewModelFactory
import com.example.scstrade.helper.Utils
import com.example.scstrade.viewmodels.SnapshotViewModel
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.snapshot.AnnouncementsFragment

class AnnoucementActivity : BaseActivity() {
    lateinit var binding: ActivityAnnoucementBinding
    lateinit var snapshotViewModel: SnapshotViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        binding = ActivityAnnoucementBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        // binding.toolbar.toggleToolbar(false)
        binding.toolbar.binding.market.text = "Announcements"
        snapshotViewModel = ViewModelProvider(this,
            SnapshotViewModelFactory(this.application,(this.application as MyApp).viewModel)
        ).get(SnapshotViewModel::class.java)
      /*  ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar.binding.customToolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
*/

        loadFragment(fragment = AnnouncementsFragment())
    }


    public fun loadFragment(fragment: Fragment, isBackStack:Boolean = false) {
        if(isBackStack){
                supportFragmentManager
                .beginTransaction()
                .replace(binding.fragmentContainer.id, fragment)
                .addToBackStack(null)
                .commit()
        }else{
            supportFragmentManager
                .beginTransaction()
                .replace(binding.fragmentContainer.id,fragment)
                .commit()
        }
    }

    override fun getResources(): Resources {

        val res = super.getResources()
        val config = Configuration(res.configuration)

        val metrics = res.displayMetrics

        // Calculate screen width and height in inches
        val widthInches = metrics.widthPixels / metrics.xdpi
        val heightInches = metrics.heightPixels / metrics.ydpi
        val diagonalInches = Math.sqrt((widthInches * widthInches + heightInches * heightInches).toDouble())

        // Set fontScale based on diagonal screen size
        if(diagonalInches>3.9 && diagonalInches<4.9){
            config.fontScale = 0.85f  // Small phones
        }else if (diagonalInches>4.9 && diagonalInches<5.4){
            config.fontScale = 0.95f
        }else if (diagonalInches>5.5 && diagonalInches<6.9){
            config.fontScale = 1.0f
        }else{
            config.fontScale = 1.2f
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            config.fontWeightAdjustment = 0

        }
        res.updateConfiguration(config, metrics)
        return res
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            // Override any incoming configuration changes
            overrideConfiguration.densityDpi = resources.displayMetrics.densityDpi
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }
}