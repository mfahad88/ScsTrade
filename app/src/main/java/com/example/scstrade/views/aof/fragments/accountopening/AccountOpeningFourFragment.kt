package com.example.scstrade.views.aof.fragments.accountopening

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentAccountOpeningFourBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.request.RegisterUser
import com.example.scstrade.services.ApiService
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.aof.fragments.LoginAOFFragment

class AccountOpeningFourFragment : Fragment() {
    lateinit var binding: FragmentAccountOpeningFourBinding
    lateinit var viewModel: AofViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountOpeningFourBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        initFields()
        binding.back.setOnClickListener {
            (requireActivity() as AofActivity).loadFragment(AccountOpeningThreeFragment())
        }

        binding.apply {
            captchaValue.text = Utils.generateCaptchaText()
            btnContinue.setOnClickListener {
                if(reference.text.toString()!="" && reference.text.toString()!=null){
                    viewModel.saveReference(reference.text)
                }
                if(captchaInput.text.toString().isNotEmpty()){
                    if(captchaValue.text.toString().equals(captchaInput.text.toString())){
                        if(cardTermsOne.checked && cardTermsTwo.checked){

                            val register=RegisterUser(
                                applicationId = 0,
                                lifecycleStatus = 0,
                                id = 0,
                                name = viewModel.getSelfInfo().fullName?:"",
                                uin = viewModel.getSelfInfo().nicNumber?:"",
                                mobileNo = viewModel.getSelfInfo().mobileNumber?:"",
                                nicBack = viewModel.getSelfInfo().nicBackImage?:"",
                                nicFront = viewModel.getSelfInfo().nicFrontImage?:"",
                                proofofIBAN = viewModel.getSelfInfo().proofIbanImage?:"",
                                identificationType = viewModel.getSelfInfo().nicType?:"",
                                residentialStatus = viewModel.getSelfInfo().residentialStatus?:"",
                                email = viewModel.getSelfInfo().emailAddress?:"",
                                ibanNo = viewModel.getSelfInfo().ibanNumber?.replace("|","")?:"",
                                reference = viewModel.getSelfInfo().reference?:"",
                                proofofRelationships = viewModel.getSelfInfo().proofRelativeImage?:"",
                                relationship = viewModel.getSelfInfo().relationshipType?:"",
                                relativeName = viewModel.getSelfInfo().relativeName?:"",
                                relativeUIN = viewModel.getSelfInfo().relativeUin?:"",
                                isApp = "1",
                                issueDate = viewModel.getSelfInfo().nicIssueDate?:""

                            )
                            viewModel.registerUser(register)
                        }else{
                            Utils.showError(requireView(),
                                getString(R.string.read_terms_conditions_to_continue))
                        }
                    }else{
                        Utils.showError(requireView(), getString(R.string.recaptcha_not_matched))
                    }
                }
            }
        }

        viewModel.mutableRegisterUser.observe(viewLifecycleOwner,{result->
            when(result){
                is Resource.Error -> {
                    Utils.showError(requireView(),result.message?:"An error occurred...")
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    val data = result.data

                    if(data?.statusCode==200 && data.isSuccess==true){
                        (requireActivity() as AofActivity).loadFragment(LoginAOFFragment())
                    }else{
                        Utils.showError(requireView(),data?.message?:"An error occurred...")
                    }
                }
            }

        })
        return binding.root
    }

    private fun initFields() {
        binding.reference.text = viewModel.getDocuments().reference
    }

}