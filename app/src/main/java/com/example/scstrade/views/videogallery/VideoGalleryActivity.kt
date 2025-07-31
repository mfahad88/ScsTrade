package com.example.scstrade.views.videogallery

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityVideoGalleryBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.videogallery.adapter.VideoGalleryAdapter
import com.example.scstrade.views.widgets.SideBarDivider

class VideoGalleryActivity : AppCompatActivity() {
    lateinit var binding:ActivityVideoGalleryBinding
    lateinit var sharedViewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVideoGalleryBinding.inflate(LayoutInflater.from(this))
        sharedViewModel = (this.application as MyApp).viewModel
        setContentView(binding.root)
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        sharedViewModel.videoGallery()
        sharedViewModel.videoGalleryItem("All")
        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@VideoGalleryActivity,LinearLayoutManager.VERTICAL,false)
            val divider = SideBarDivider(
                dividerColor = Color.parseColor("#B3C6C6CD"),
                marginEnd = 10
            )
            addItemDecoration(divider)

        }

        binding.spinner3.onItemSelectedListener = object:OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedText = p0?.getItemAtPosition(p2).toString()
                sharedViewModel.videoGalleryItem(selectedText)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        sharedViewModel.mutableVideoGalleryListItem.observe(this){result->
            when(result){
                is Resource.Error -> {
                    Utils.showError(binding.root,result.message)
                    binding.progressBar.visibility = View.GONE
                }
                is Resource.Loading -> {
                    /*binding.apply {
                        group.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                    }*/
                }
                is Resource.Success -> {
                    val data=result.data
                    binding.apply {
                        spinner3.adapter = ArrayAdapter(binding.root.context,android.R.layout.simple_list_item_1,data?.map { it.mgvideoplaylist }?: emptyList())
                        progressBar.visibility = View.GONE
                    }
                }
            }
        }

        sharedViewModel.mutableVideoGalleryItem.observe(this){result->

            when(result){
                is Resource.Error -> {
                    Utils.showError(binding.root,result.message)
                    binding.progressBar.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.apply {
                        recyclerView.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                    }
                }
                is Resource.Success -> {
                    binding.apply {

                        recyclerView.adapter = VideoGalleryAdapter(binding.root.context,result.data?: emptyList())
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }
}