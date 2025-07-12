package com.example.scstrade.views.snapshot
import androidx.compose.ui.res.dimensionResource

import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentOverviewBinding
import com.example.scstrade.databinding.FragmentProfileBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.viewmodels.SnapshotViewModel
import com.example.scstrade.views.MyApp


class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var sharedViewModel: SharedViewModel
    lateinit var snapshotViewModel: SnapshotViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        sharedViewModel= (requireActivity().application as MyApp).viewModel
        snapshotViewModel = (requireActivity() as SnapshotActivity).snapshotViewModel

        sharedViewModel.getCompanyDetail((requireActivity() as SnapshotActivity).symbol)

        sharedViewModel.mutableCompanyDetail.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Resource.Error -> {
                    binding.loader.visibility = View.GONE
                    Utils.showError(requireView(),result.message)
                }
                is Resource.Loading ->{
                    binding.apply {
                        loader.visibility = View.VISIBLE
                        main.visibility = View.GONE
                    }
                }
                is Resource.Success -> {
                    val data=result.data?.first()
                    binding.apply {

                        oilAndGas.text= data?.description
                        cdcShareR.text = data?.registrar
                        auditorBody.text = data?.auditor
                        urlText.text = data?.website
                        urlText.movementMethod = LinkMovementMethod.getInstance()
                        addressBody.text = data?.address
                        loader.visibility = View.GONE
                        main.visibility = View.VISIBLE
                    }
                }
            }
        })
        return binding.root
    }


}