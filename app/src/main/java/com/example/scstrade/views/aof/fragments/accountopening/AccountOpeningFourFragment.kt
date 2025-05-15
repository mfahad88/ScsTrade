package com.example.scstrade.views.aof.fragments.accountopening

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentAccountOpeningFourBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity

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
                if(reference.text.toString().isNotEmpty()){
                    viewModel.saveReference(reference.text)
                }
                if(captchaInput.text.toString().isNotEmpty()){
                    if(captchaValue.text.toString().equals(captchaInput.text.toString())){
                        if(cardTermsOne.checked && cardTermsTwo.checked){
                            (requireActivity() as AofActivity).loadFragment(AccountOpeningFiveFragment())
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
        return binding.root
    }

    private fun initFields() {
        binding.reference.text = viewModel.getDocuments().reference
    }

}