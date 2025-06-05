package com.example.scstrade.views.announcement

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
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.snapshot.AnnouncementsFragment

class AnnoucementActivity : AppCompatActivity() {
    lateinit var binding: ActivityAnnoucementBinding
    lateinit var snapshotViewModel: SnapshotViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        binding = ActivityAnnoucementBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
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
}