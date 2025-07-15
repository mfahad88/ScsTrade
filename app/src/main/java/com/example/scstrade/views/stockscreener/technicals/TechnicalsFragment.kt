package com.example.scstrade.views.stockscreener.technicals

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityTechnicalsBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.stockscreener.technicals.adapter.TechnicalAdapter
import com.example.scstrade.views.widgets.VerticalSpaceItemDecoration

class TechnicalsFragment : Fragment() {
    lateinit var binding: ActivityTechnicalsBinding
    lateinit var viewModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=ActivityTechnicalsBinding.inflate(inflater,container,false)
        viewModel = (requireActivity().application as MyApp).viewModel
       
        // binding.toolbar.toggleToolbar(false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel.getTechnicals()
        observerTechnicals()
        binding.recyclerView.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }


        return binding.root
    }
    private fun observerTechnicals() {
        binding.recyclerView.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(
                VerticalSpaceItemDecoration(1,
                    ContextCompat.getColor(requireContext(),R.color.md_theme_outline))
            )
        }
        viewModel.mutableTechnical.observe(viewLifecycleOwner, Observer { result->

            when(result){
                is Resource.Error -> {
                    binding.loader.visibility  = View.GONE
                    Utils.showError(binding.root,result.message?:"An Error Occurred")
                }
                is Resource.Loading -> binding.loader.visibility  = View.VISIBLE
                is Resource.Success -> {
                    binding.loader.visibility  = View.GONE
                    binding.recyclerView.adapter = TechnicalAdapter(result.data?: emptyList()){
                        val intent= Intent(requireContext(),TechnicalsDetailActivity::class.java)
                        intent.putExtra(AppConstants.TECHNICAL_SELECTION,it.technicals)
                        startActivity(intent)
                    }


                }
            }

        })
    }


}