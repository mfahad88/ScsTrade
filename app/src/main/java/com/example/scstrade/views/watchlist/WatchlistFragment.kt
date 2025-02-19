package com.example.scstrade.views.watchlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentWatchlistBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.viewmodels.WatchListViewModel
import com.example.scstrade.views.landing.LandingFragment
import com.example.scstrade.views.widgets.HorizontalDivider
import com.google.gson.reflect.TypeToken


class WatchlistFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentWatchlistBinding
    lateinit var viewModel: WatchListViewModel
    lateinit var login:LoginDataItem
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWatchlistBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(WatchListViewModel::class.java)
        binding.fab.setOnClickListener {
            val bottomSheetFragment=AddWatchListBottomSheetFragment()
            bottomSheetFragment.show(childFragmentManager,"AddWatchList")
        }
       fetchUser()
        viewModel.getWatchList(login.registrationID)
       viewModel.mutableWatchListItem.observe(viewLifecycleOwner, Observer { result->
           when(result){
               is Resource.Error -> {
                   Utils.showError(requireView(),result.message?:"")
               }
               is Resource.Loading -> {
                   binding.loader.visibility = View.VISIBLE
                   binding.recyclerView.visibility = View.GONE
               }
               is Resource.Success -> {
                   binding.loader.visibility = View.GONE
                   binding.recyclerView.visibility = View.VISIBLE
                    binding.recyclerView.apply {
                        layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                        addItemDecoration(HorizontalDivider(30))
                        adapter=WatchListAdapter(result.data?.sortedBy { it.watchListPosition }?.toList()?: emptyList()){
                            viewModel.selectedItem = it
                        }
                    }
               }
           }
       })

        return binding.root
    }

    private fun fetchUser() {
        val listType = object : TypeToken<List<LoginDataItem>>() {}
        val user= Utils.getSharedPreference(requireContext(), emptyList<LoginDataItem>(),AppConstants.USER,listType)
        login=user.first()
        Log.e("User: ",user.toString())
    }


}