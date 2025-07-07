package com.example.scstrade.views.aof.fragments.accountopening

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentAccountOpeningFourBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.request.aof.RegisterUser
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
//        initFields()
        binding.back.setOnClickListener {
            (requireActivity() as AofActivity).loadFragment(fragment = AccountOpeningThreeFragment())
        }

        binding.apply {
            captchaValue.text = Utils.generateCaptchaText()
            btnContinue.setOnClickListener {
                if(reference.text.toString()!="" && reference.text.toString()!=null){
                    /*viewModel.saveReference(reference.text)*/
                    viewModel.accountOpening.accountopeningreference =reference.textInputEditText.text.toString()
//                    viewModel.saveaccountOpening()
                }
                if(captchaInput.text.toString().isNotEmpty()){
                    if(captchaValue.text.toString().equals(captchaInput.text.toString())){
                        if(cardTermsOne.checked && cardTermsTwo.checked){

                            val register= RegisterUser(
                                applicationId = 0,
                                lifecycleStatus = 0,
                                id = 0,
                                name = viewModel.getaccountOpening().accountopeningfullName?:"",
                                uin = viewModel.getaccountOpening().accountopeningnicNumber?:"",
                                mobileNo = viewModel.getaccountOpening().accountopeningmobileNumber?:"",
                                nicBack = viewModel.accountOpening.accountopeningnicBackImage?:"",
                                nicFront = viewModel.accountOpening.accountopeningnicFrontImage?:"",
                                proofofIBAN = viewModel.accountOpening.accountopeningproofIbanImage?:"",
                                identificationType = viewModel.getaccountOpening().accountopeningnicType?:"",
                                residentialStatus = viewModel.getaccountOpening().accountopeningresidentialStatus?:"",
                                email = viewModel.getaccountOpening().accountopeningemailAddress?:"",
                                ibanNo = viewModel.getaccountOpening().accountopeningibanNumber?.replace("|","")?:"",
                                reference = viewModel.getaccountOpening().accountopeningreference?:"",
                                proofofRelationships = viewModel.accountOpening.accountopeningproofRelativeImage?:"",
                                relationship = viewModel.getaccountOpening().accountopeningrelationshipType?:"",
                                relativeName = viewModel.getaccountOpening().accountopeningrelativeName?:"",
                                relativeUIN = viewModel.getaccountOpening().accountopeningrelativeUin?:"",
                                isApp = true,
                                issueDate = viewModel.getaccountOpening().accountopeningnicIssueDate?:""

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
                    binding.loader.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    val data = result.data

                    if(data?.statusCode==200 && data.isSuccess==true){
                        (requireActivity() as AofActivity).loadFragment(fragment = LoginAOFFragment())
                    }else{
                        Utils.showError(requireView(),data?.message?:"An error occurred...")
                    }
                    binding.loader.visibility = View.GONE
                }
            }

        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.mutableRegisterUser.value=null
    }

    private fun initFields() {
        binding.reference.text = viewModel.getaccountOpening().accountopeningreference
    }

}