package com.example.scstrade.views.stockscreener.customscreener

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentCustomScreenerListBinding
import com.example.scstrade.model.response.stockscreener.StockScreenerItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.viewmodels.StockScreenerViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.stockscreener.StockScreenerActivity
import com.example.scstrade.views.widgets.SideBarDivider
import com.example.scstrade.views.widgets.StickyHeaderItemDecoration


class CustomScreenerListFragment : Fragment() {
    lateinit var binding: FragmentCustomScreenerListBinding
    lateinit var viewModel: StockScreenerViewModel
    private lateinit var sharedViewModel: SharedViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomScreenerListBinding.inflate(inflater,container,false)
//
//        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        viewModel = (requireActivity() as StockScreenerActivity).viewModel
        sharedViewModel=(requireActivity().application as MyApp).viewModel
        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerView) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(
                0,0,0,
                systemBars.bottom
            )

            insets
        }
        setupRecyclerView()
        viewModel.mutableFiltered.observe(viewLifecycleOwner, Observer { result->
            val stockListWithEmptyRow = emptyList<StockScreenerItem>() + result
            val symbolMap = sharedViewModel.mutableAllData.value?.data?.associateBy { it.sYM.uppercase() } ?: emptyMap()
            binding.recyclerView.adapter = CustomScreenerAdapter(stockListWithEmptyRow.toMutableList(),symbolMap, viewModel.selectedFilters.toList()){

            }
        })
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            val divider = SideBarDivider(
                dividerColor = Color.parseColor("#B3C6C6CD"),
                marginEnd = 80
            )
            addItemDecoration(divider)
//            addItemDecoration(StickyHeaderItemDecoration { position -> true })
        }
    }

}