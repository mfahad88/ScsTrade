package com.example.scstrade.views.login

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scstrade.databinding.FragmentLoginBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.GoogleSignInUtils
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.login.LoginDataItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.landing.LandingFragment
import com.example.scstrade.views.main.MainActivity
import com.example.scstrade.views.register.IndexAdapter
import com.example.scstrade.views.register.RegisterFragment
import com.example.scstrade.views.widgets.VerticalDivider
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.log

class LoginFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel
    private lateinit var binding: FragmentLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private var fcm: String? = null
    lateinit var login:LoginDataItem
    private val indexAdapter = IndexAdapter()

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                GoogleSignInUtils.run {
                    handleSignInResult(
                                data = data,
                                activity = requireActivity(),
                                onSuccess = { username ->
                                    viewModel.fetchLogin(username?.email ?: "", "scs@123", fcm ?: "")
                                },
                                onFailure = { error ->
                                    Toast.makeText(requireContext(), "Sign-in failed: ${error?.message}", Toast.LENGTH_SHORT).show()
                                }
                            )
                }
            } else {
                Toast.makeText(requireContext(), "Sign-in cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel = (requireActivity().application as MyApp).viewModel
        val remembered =  Utils.getSharedPreference(
            requireContext(),
            listOf(false),
            AppConstants.IS_REMEMBER,
            object : TypeToken<List<Boolean>>() {}
        ).first()

        if (Build.VERSION.SDK_INT >= 29) {
            ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            ViewCompat.setOnApplyWindowInsetsListener(binding.container) { view, insets ->
                val imeInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                view.setPadding(0, 0, 0, imeInsets.bottom + 31)
                insets
            }
        }

        firebaseAuth = FirebaseAuth.getInstance()
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            fcm = it
        }

        googleSignInClient = GoogleSignInUtils.initGoogleSignInClient(requireContext())

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragmentManager = requireActivity().supportFragmentManager
                if (fragmentManager.backStackEntryCount > 0) {
                    fragmentManager.popBackStack()
                } else {
                    (requireActivity() as MainActivity).finish()
                }
            }
        })

        binding.google.setOnClickListener {
            GoogleSignInUtils.signOut(requireContext())
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }

        binding.rememberMe.isChecked = remembered

        if(remembered){
            fetchUser()

            binding.userName.text = login.registrationEmail
            binding.password.text = login.registrationPassword
        }else{
            binding.userName.text = null
            binding.password.text = null
        }

        binding.button.setOnClickListener {
            if (binding.userName.text.isNotEmpty() && binding.password.text.isNotEmpty()) {
                viewModel.fetchLogin(binding.userName.text, binding.password.text, fcm ?: "")
            } else {
                Utils.showError(binding.root, "Please provide valid username and password")
            }
            Utils.closeKeyboard(requireActivity())
        }

        viewModel.isLineChart = true
        viewModel.fetchIndices()

        // 🔧 Setup index recycler
        binding.recyclerIndices.apply {
            adapter = indexAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(VerticalDivider())
        }

        viewModel.mutableLogin.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Error -> {
                    binding.loader.visibility = View.GONE
                    Utils.showError(requireView(), "Invalid login")
                }

                is Resource.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    if (!resource.data.isNullOrEmpty()) {
                        if (binding.rememberMe.isChecked) {
                            Utils.saveSharedPreference(
                                requireContext(),
                                AppConstants.IS_REMEMBER,
                                listOf(true)
                            )
                        }else{
                            Utils.saveSharedPreference(
                                requireContext(),
                                AppConstants.IS_REMEMBER,
                                listOf(false)
                            )
                        }

                        Utils.saveSharedPreference(
                            requireContext(),
                            AppConstants.USER,
                            resource.data ?: emptyList()
                        )
                        Utils.showSuccess(requireView(), "Success")

                        (requireActivity() as MainActivity).loadFragment(LandingFragment(), false)
                        viewModel.mutableLogin.value = null
                        binding.loader.visibility = View.GONE
                    }
                }
            }
        })

        viewModel.mutableResultIndices.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                    binding.container.visibility = View.GONE
                }

                is Resource.Error -> {
                    binding.loader.visibility = View.GONE
                    binding.container.visibility = View.VISIBLE
                    // Optional: show error message
                }

                is Resource.Success -> {
                    val data = resource.data
                    binding.loader.visibility = View.GONE
                    binding.container.visibility = View.VISIBLE
                    binding.recyclerIndices.visibility = View.VISIBLE

                    val chartMap = mapOf(
                        "kse all" to (data?.chartItemKSEALL ?: emptyList()),
                        "kse 100" to (data?.chartItemKSE100 ?: emptyList()),
                        "kse 30" to (data?.chartItemKSE30 ?: emptyList()),
                        "kmi 30" to (data?.chartItemKMI30 ?: emptyList())
                    )

                    val preferredOrder = listOf(
                        "KSE 100 Index",
                        "KSE All Share Index"
                    )

                    val sortedIndices = (data?.kseIndices ?: emptyList())
                        .sortedWith(
                            compareBy(
                                { preferredOrder.indexOf(it.iNDEXCODE).takeIf { idx -> idx >= 0 } ?: Int.MAX_VALUE },
                               // { it.iNDEXCODE } // secondary alphabetical sort for the rest
                            )
                        )

                    indexAdapter.addItems(
                        sortedIndices,
                        chartMap
                    )
                }
            }
        })

        binding.signUp.setOnClickListener {
            (requireActivity() as MainActivity).loadFragment(RegisterFragment(), true)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.mutableLogin.value = null
        viewModel.mutableResultIndices.value = null
        viewModel.isLineChart = false
    }

    private fun fetchUser() {
        val listType = object : TypeToken<List<LoginDataItem>>() {}
        val user= Utils.getSharedPreference(requireContext(), emptyList<LoginDataItem>(),AppConstants.USER,listType)
        login=user.first()
        Log.e("User: ",user.toString())
    }
}
