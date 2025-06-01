package com.example.scstrade.views.register

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isNotEmpty
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentRegisterBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.GoogleSignInUtils
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.landing.LandingFragment
import com.example.scstrade.views.main.MainActivity
import com.example.scstrade.views.widgets.VerticalDivider
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.messaging.FirebaseMessaging


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: SharedViewModel

    private lateinit var googleSignInClient: GoogleSignInClient
    var fcm:String?=null
    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                GoogleSignInUtils.handleSignInResult(
                    data = data,
                    activity = requireActivity(),
                    onSuccess = { username ->

                        viewModel.registerUser(
                            fullName = username?.displayName?:"",
                            email = username?.email?:"",
                            password = "scs@123",
                            mobile = username?.phoneNumber?:"",
                            fireBaseID = fcm?:""
                        )
                    },
                    onFailure = { error ->
                        Utils.showError(requireView(),"Sign-in failed: ${error?.message}")

                    }
                )
            } else {
                Utils.showError(requireView(),"Sign-in cancelled")
            }
        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentRegisterBinding.inflate(inflater,container,false)
//        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel = (requireActivity().application as MyApp).viewModel
        googleSignInClient = GoogleSignInUtils.initGoogleSignInClient(requireContext())
        bindView()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.google.setOnClickListener {
            GoogleSignInUtils.signOut(requireContext())
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            fcm=it
        }
        viewModel.mutableIndices.observe(viewLifecycleOwner, Observer { resource ->
            System.out.println(resource.data.toString())
            when (resource){
                is Resource.Loading ->{
                    binding.loader.visibility=View.VISIBLE
                    binding.container.visibility=View.GONE
                }

                is Resource.Error ->{

                }

                is Resource.Success -> {
                    binding.loader.visibility=View.GONE
                    binding.container.visibility=View.VISIBLE
                   (binding.recyclerIndices.adapter as IndexAdapter).addItems(resource.data?: emptyList())
                }
            }
        })

        viewModel.mutableRegister.observe(viewLifecycleOwner, Observer { resource->
            when(resource){
                is Resource.Error -> Utils.showError(binding.root,"Unable to Register")
                is Resource.Loading -> binding.loader.visibility=View.VISIBLE
                is Resource.Success -> {
                    binding.loader.visibility=View.GONE
                    val json = resource.data
                    if(json?.isJsonNull?:false){
                        if(json?.isJsonArray?:false){
                            val jObject=json?.asJsonArray?.first()?.asJsonObject

                            val data= mutableListOf<LoginDataItem>()
                            data.add(
                                LoginDataItem(
                                    jObject?.get("RegistrationDate")?.asString,
                                    jObject?.get("RegistrationEmail")?.asString,
                                    jObject?.get("RegistrationID")?.asInt,
                                    jObject?.get("RegistrationName")?.asString,
                                    jObject?.get("RegistrationPassword")?.asString,
                                    jObject?.get("RegistrationPhone")?.asString,
                                    jObject?.get("RegistrationStatus")?.asBoolean

                                )
                            )
                            Utils.showSuccess(binding.root,"Successfully Register")
                            Utils.saveSharedPreference(requireContext(),AppConstants.USER,data?: emptyList())
                            loadFragment(LandingFragment(),false)
                        }
                    }else{
                        Utils.showError(binding.root,json?.asString?:"An error occurred...")

                    }

                }
            }
        })

        binding.button.setOnClickListener {
            if(binding.fullName.text.isNotEmpty() && binding.email.text.isNotEmpty() && binding.mobileNumber.text.isNotEmpty() && binding.password.text.isNotEmpty()){
                if(Utils.isPasswordValid(binding.password.text)) {
                    binding.apply {
                        viewModel.registerUser(
                            fullName = fullName.text,
                            email = email.text,
                            password = password.text,
                            mobile = mobileNumber.text,
                            fireBaseID = fcm ?: ""
                        )
                    }
                }else{
                    Utils.showError(requireView(),"Invalid Password it should be of 6 characters with  At least one letter and  At least one digit")
                }
            }else{
                Utils.showError(binding.root,"Provide data for all fields")
            }
        }

        return binding.root
    }

    private fun loadFragment(fragment: Fragment, isBackStack: Boolean=false) {
        if(isBackStack){

            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }else{
            val manager= parentFragmentManager
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            manager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }

    private fun bindView() {
        binding.recyclerIndices.apply {
            adapter= IndexAdapter(emptyList(), viewModel, viewLifecycleOwner)
            layoutManager=
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            addItemDecoration(VerticalDivider())


        }

    }

}