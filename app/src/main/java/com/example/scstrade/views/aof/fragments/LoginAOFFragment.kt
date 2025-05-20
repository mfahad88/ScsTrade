package com.example.scstrade.views.aof.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentLoginAOFBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.request.LoginUser
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.aof.fragments.accountopening.AccountOpeningOneFragment
import com.example.scstrade.views.aof.fragments.kyc.KycOneFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
                    if(result.data?.statusCode==200  && result.data?.isSuccess==true){
                        viewModel.saveAccessToken(result.data?.data?.accessToken)
                        viewModel.protectedAppId()

                    }else{
                        Utils.showError(requireView(),result.data?.message?:"An error occurred...")
                    }
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
                        (requireActivity() as AofActivity).loadFragment(KycOneFragment())
                    viewModel.applicationId = Utils.decryptStatus(user?.sub?:"")
                    Log.e("ApiId:",Utils.decryptStatus(user?.sub?:""))
                }
            }

        })

        return binding.root
    }


}