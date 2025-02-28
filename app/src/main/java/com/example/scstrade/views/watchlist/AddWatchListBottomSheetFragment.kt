package com.example.scstrade.views.watchlist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.scstrade.databinding.FragmentAddWatchListBottomSheetBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.model.response.watchList.WatchListItem
import com.example.scstrade.viewmodels.WatchListViewModel
import com.example.scstrade.viewmodels.WatchListViewModelFactory
import com.example.scstrade.views.MyApp
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class AddWatchListBottomSheetFragment() : BottomSheetDialogFragment() {
    lateinit var binding:FragmentAddWatchListBottomSheetBinding
    lateinit var viewModel: WatchListViewModel
    lateinit var login:LoginDataItem
    var mode=-1
    lateinit var watchListItem:WatchListItem
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAddWatchListBottomSheetBinding.inflate(inflater,container,false)

        mode=arguments?.getInt(AppConstants.MODE)?:0
        if(mode==1){
            Log.e("Argus--->", arguments?.getString(AppConstants.WATCHLIST_ID).toString())
            watchListItem= Gson().fromJson(arguments?.getString(AppConstants.WATCHLIST_ID),WatchListItem::class.java)
            binding.editTextName.setText(watchListItem.WatchListMainName)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this,WatchListViewModelFactory(requireActivity().application,(requireActivity().application as MyApp).viewModel)).get(WatchListViewModel::class.java)
        fetchUser()
        binding.buttonAdd.setOnClickListener {
            if(binding.editTextName.text.toString().isNotEmpty()){
                if(mode==0) {
                    viewModel.createWatchList(
                        binding.editTextName.text.toString(),
                        login.registrationID?:0
                    )
                }else{
                    viewModel.updateWatchList(binding.editTextName.text.toString(),watchListItem,login.registrationID?:0)
                    val result = Bundle().apply { putString(AppConstants.BOTTOM_SHEET_STATUS,"Done") }
                    parentFragmentManager.setFragmentResult(AppConstants.BOTTOM_SHEET,result)

                    dismiss()
                }
            }else{
                Snackbar.make(binding.root,"Please provide valid name",Snackbar.LENGTH_SHORT)
            }
        }

        viewModel.mutableCreate.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Error -> {
                    Utils.showError(requireView(),it.message?:"An error occurred")
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    val result = Bundle().apply { putString(AppConstants.BOTTOM_SHEET_STATUS,"Done") }
                    parentFragmentManager.setFragmentResult(AppConstants.BOTTOM_SHEET,result)

                    this.dismiss()
                }
            }
        })

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
    }
    private fun fetchUser() {
        val listType = object : TypeToken<List<LoginDataItem>>() {}
        val user= Utils.getSharedPreference(requireContext(), emptyList<LoginDataItem>(),AppConstants.USER,listType)
        login=user.first()
        Log.e("User: ",user.toString())
    }


}