package com.example.scstrade.views.aof.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.scstrade.databinding.FragmentLoginAOFBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.request.aof.LoginUser
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.aof.fragments.kyc.attorneyDetail.KycAttorneyDetailOneFragment
import com.example.scstrade.views.aof.fragments.kyc.basicData.KycBasicDataOneFragment
import com.example.scstrade.views.aof.fragments.kyc.contactDetail.KycContactDetailOneFragment


/**
 * A simple [Fragment] subclass.
 * Use the [LoginAOFFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginAOFFragment : Fragment() {
    lateinit var binding: FragmentLoginAOFBinding
    lateinit var viewModel: AofViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginAOFBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        binding.apply {
            loginNow.setOnClickListener {
                if(userName.textInputEditText.text.toString()!="" && password.textInputEditText.text.toString()!="" ){
                    viewModel.loginUser(LoginUser(userName.textInputEditText.text.toString(),password.textInputEditText.text.toString()))
                }
            }
        }


        viewModel.mutableLoginUser.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> Utils.showError(requireView(),result.message?:"An error occurred...")
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    viewModel.saveAccessToken(result.data?.data?.accessToken)
                    viewModel.protectedAppId()
                }
            }
        })

        viewModel.mutableProtected.observe(viewLifecycleOwner,Observer{result->
            when(result){
                is Resource.Error -> Utils.showError(requireView(),result.message?:"An error occurred...")
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val response=result.data
                    val user=response?.user

                    viewModel.applicationId = Utils.decryptStatus(user?.sub?:"")
                    viewModel.basicData.uinType = user?.identificationType
                    viewModel.basicData.fullNicName = user?.name
                    viewModel.basicData.uinNumber = user?.uin
//                    viewModel.basicData.nicValid = user?.expiresAt?.let { Utils.convertIsoToDate(it) }
                    Log.e("ApiId:",Utils.decryptStatus(user?.sub?:""))
                    viewModel.getDocuments()
                    viewModel.getattorneyDetails()
                    viewModel.getBasicData()
                    viewModel.getcontactDetails()
                    viewModel.getNomineeDetails()
                    viewModel.getotherDetails()
                    viewModel.mutableProtected.value=null
                    viewModel.mutableLoginUser.value=null
                    (requireActivity() as AofActivity).loadFragment(KycBasicDataOneFragment())
                }
            }

        })











        return binding.root
    }


}