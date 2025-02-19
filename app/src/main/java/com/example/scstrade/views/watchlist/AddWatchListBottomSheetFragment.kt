package com.example.scstrade.views.watchlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentAddWatchListBottomSheetBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.response.login.LoginDataItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.reflect.TypeToken


class AddWatchListBottomSheetFragment : BottomSheetDialogFragment() {
    lateinit var binding:FragmentAddWatchListBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAddWatchListBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listType = object : TypeToken<List<LoginDataItem>>() {}
        val user= Utils.getSharedPreference(requireContext(), emptyList<LoginDataItem>(),
            AppConstants.USER,listType)
        binding.buttonAdd.setOnClickListener {
            if(binding.editTextName.text.toString().isNotEmpty()){

            }else{
                Snackbar.make(binding.root,"Please provide valid name",Snackbar.LENGTH_SHORT)
            }
        }

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
    }

}