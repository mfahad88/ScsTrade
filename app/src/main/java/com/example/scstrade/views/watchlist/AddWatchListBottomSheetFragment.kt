package com.example.scstrade.views.watchlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentAddWatchListBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


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
        binding.buttonAdd.setOnClickListener {
            if(binding.editTextName.text.toString().isNotEmpty()){

            }
        }

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
    }

}