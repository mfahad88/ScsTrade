package com.example.scstrade.views.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentLoginBinding
import com.example.scstrade.helper.AppConstants
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
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


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
    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = result.data
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.w("GoogleSignIn", "Google sign in failed", e)
            Toast.makeText(requireContext(), "Google sign-in failed", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentLoginBinding.inflate(inflater,container,false)
        viewModel=(requireActivity().application as MyApp).viewModel

        firebaseAuth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // from google-services.json
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.facebook.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                this,
                listOf("email", "public_profile")
            )
        }

        binding.google.setOnClickListener {
            signInWithGoogle()
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
                viewModel.fetchLogin(binding.userName.text,binding.password.text)
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

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser

                    Toast.makeText(requireContext(), "Welcome ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    // Navigate or update UI here
                } else {
                    Toast.makeText(requireContext(), "Authentication Failed", Toast.LENGTH_SHORT).show()
                }
            }
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}