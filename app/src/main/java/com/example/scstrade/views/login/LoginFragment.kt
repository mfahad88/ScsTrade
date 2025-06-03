package com.example.scstrade.views.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentLoginBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.GoogleSignInUtils
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.landing.LandingFragment
import com.example.scstrade.views.main.MainActivity
import com.example.scstrade.views.register.IndexAdapter
import com.example.scstrade.views.register.RegisterFragment
import com.example.scstrade.views.widgets.VerticalDivider
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private  lateinit var viewModel: SharedViewModel
    private lateinit var binding: FragmentLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager
    var fcm:String?=null
    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                GoogleSignInUtils.handleSignInResult(
                    data = data,
                    activity = requireActivity(),
                    onSuccess = { username ->
                        viewModel.fetchLogin(username?.email?:"","scs@123",fcm?:"")

                    },
                    onFailure = { error ->
                        Toast.makeText(requireContext(), "Sign-in failed: ${error?.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                Toast.makeText(requireContext(), "Sign-in cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentLoginBinding.inflate(inflater,container,false)
        viewModel=(requireActivity().application as MyApp).viewModel

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        firebaseAuth = FirebaseAuth.getInstance()
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            fcm=it
        }
        callbackManager = CallbackManager.Factory.create()
        googleSignInClient = GoogleSignInUtils.initGoogleSignInClient(requireContext())

        binding.facebook.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                this,
                listOf("email", "public_profile")
            )
        }

        binding.google.setOnClickListener {
            GoogleSignInUtils.signOut(requireContext())
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
       }

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Log.e("Facebook---->",result.toString())
                    handleFacebookAccessToken(result.accessToken)
                }

                override fun onCancel() {
                    Toast.makeText(requireContext(), "Login Cancelled", Toast.LENGTH_SHORT).show()
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_LONG).show()
                }
            })
        binding.button.setOnClickListener {
            if(binding.userName.text.isNotEmpty() && binding.password.text.isNotEmpty()){
                viewModel.fetchLogin(binding.userName.text,binding.password.text,fcm?:"")
            }else{
                Utils.showError(binding.root,"Please provide valid username and password")
            }
        }
        viewModel.fetchIndices()
        binding.recyclerIndices.apply {
            adapter= IndexAdapter(emptyList(),viewModel,viewLifecycleOwner)
            layoutManager=
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            addItemDecoration(VerticalDivider())


        }

        viewModel.mutableLogin.observe(viewLifecycleOwner, Observer { resource->
            when(resource){
                is Resource.Error -> {
                    binding.loader.visibility=View.GONE
                    Utils.showError(requireView(),"Invalid login")
                }
                is Resource.Loading -> binding.loader.visibility=View.VISIBLE
                is Resource.Success -> {
                    binding.loader.visibility=View.GONE
                    if(!resource.data.isNullOrEmpty()){
                        if(binding.rememberMe.isChecked) {
                           Utils.saveSharedPreference(requireContext(),AppConstants.IS_REMEMBER,
                               listOf(true)
                           )
                        }


                        Utils.saveSharedPreference(
                            requireContext(),
                            AppConstants.USER,
                            resource.data?: emptyList()
                        )
                        Utils.showSuccess(requireView(),"Success")
                        viewModel.mutableLogin.removeObservers(this)
                        viewModel.mutableLogin.value=null
                        binding.userName.text=null
                        binding.password.text= null
                        (requireActivity() as MainActivity).loadFragment(LandingFragment(),false)
                    }
                }
            }
        })

        viewModel.mutableIndices.observe(viewLifecycleOwner, Observer { resource ->
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
                    binding.recyclerIndices.visibility=View.VISIBLE
                    (binding.recyclerIndices.adapter as IndexAdapter).addItems(
                        resource.data ?: emptyList()
                    )
                    /*if(resource.data?.first()?.marketStatus?.lowercase()=="close"){
                        binding.recyclerIndices.visibility=View.GONE
                    }else {
                        binding.recyclerIndices.visibility=View.VISIBLE
                        (binding.recyclerIndices.adapter as IndexAdapter).addItems(
                            resource.data ?: emptyList()
                        )
                    }*/
                }
            }
        })
        binding.signUp.setOnClickListener {

            (requireActivity() as MainActivity).loadFragment(RegisterFragment(),true)
        }
        return binding.root
    }



    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Toast.makeText(requireContext(), "Logged in as ${user?.displayName}", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(requireContext(), "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.mutableLogin.value = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}