package com.example.scstrade.views.reports

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityReportBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.widgets.HorizontalDivider
import com.example.scstrade.views.widgets.SideBarDivider

class ReportActivity : AppCompatActivity() {
    lateinit var binding:ActivityReportBinding
    lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(LayoutInflater.from(this))
        binding.toolbar.binding.market.text="Research Reports"
        sharedViewModel = (this.application as MyApp).viewModel
        enableEdgeToEdge()
        setContentView(binding.root)
        setupRecyclerview()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        sharedViewModel.researchReportList()

        binding.spinner.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = parent?.getItemAtPosition(position).toString()
                sharedViewModel.researchReport(selectedCategory)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        sharedViewModel.mutableReportList.observe(this, Observer { result->
            when(result){
                is Resource.Error -> {
                    Utils.showError(binding.root,result.message)

                }
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    binding.apply {
                        spinner.adapter = ArrayAdapter(binding.root.context,android.R.layout.simple_list_item_1,result.data?.map { it.type }?.toList()?: emptyList())
                        sharedViewModel.researchReport("All")
                    }
                }
            }
        })

        sharedViewModel.mutableReportDetails.observe(this, Observer { result->
            when(result){
                is Resource.Error -> {
                    Utils.showError(binding.root,result.message)
                    binding.progressBar.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.apply {
                        progressBar.visibility = View.GONE
                        group.visibility = View.VISIBLE
                        result.data?.let { reportList ->
                            recyclerView.adapter = ResearchReportAdapter(reportList) { item ->
                                val url = item.fileVPath?.trim()
                                if (!url.isNullOrBlank()) {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            setDataAndType(Uri.parse(url), getMimeTypeFromUrl(url))
                                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                        }
                                        startActivity(intent)
                                    } catch (e: Exception) {
                                        Utils.showError(binding.root, "Cannot open file: ${e.localizedMessage}")
                                    }
                                } else {
                                    Utils.showError(binding.root, "No file URL found.")
                                }
                            }
                        }
                    }
                }
            }
        })

    }

    private fun setupRecyclerview() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(binding.root.context,LinearLayoutManager.VERTICAL,false)
            addItemDecoration(HorizontalDivider(15.dp))
//            addItemDecoration(SideBarDivider(dividerColor = Color.parseColor("#B3C6C6CD"), marginEnd = 0))
        }
    }

    private fun getMimeTypeFromUrl(url: String): String {
        return when {
            url.endsWith(".pdf", ignoreCase = true) -> "application/pdf"
            url.endsWith(".jpg", ignoreCase = true) ||
                    url.endsWith(".jpeg", ignoreCase = true) -> "image/jpeg"
            url.endsWith(".png", ignoreCase = true) -> "image/png"
            url.endsWith(".webp", ignoreCase = true) -> "image/webp"
            url.endsWith(".bmp", ignoreCase = true) -> "image/bmp"
            url.endsWith(".gif", ignoreCase = true) -> "image/gif"
            else -> "*/*" // fallback for unknown types
        }
    }
}