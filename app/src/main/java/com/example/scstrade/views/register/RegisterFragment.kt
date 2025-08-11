package com.example.scstrade.views.register

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
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
    private var fcm: String? = null

    // ✅ Use single adapter instance
    private val indexAdapter = IndexAdapter()

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                GoogleSignInUtils.handleSignInResult(
                    data = data,
                    activity = requireActivity(),
                    onSuccess = { username ->
                        viewModel.registerUser(
                            fullName = username?.displayName ?: "",
                            email = username?.email ?: "",
                            password = "scs@123",
                            mobile = username?.phoneNumber ?: "",
                            fireBaseID = fcm ?: ""
                        )
                    },
                    onFailure = { error ->
                        Utils.showError(requireView(), "Sign-in failed: ${error?.message}")
                    }
                )
            } else {
                Utils.showError(requireView(), "Sign-in cancelled")
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        viewModel = (requireActivity().application as MyApp).viewModel
        googleSignInClient = GoogleSignInUtils.initGoogleSignInClient(requireContext())

        bindView()

        viewModel.isLineChart = true
        viewModel.fetchIndices()

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
            fcm = it
        }

        // ✅ Observe indices data and populate adapter
        viewModel.mutableResultIndices.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                    binding.container.visibility = View.GONE
                }

                is Resource.Error -> {
                    binding.loader.visibility = View.GONE
                    binding.container.visibility = View.VISIBLE
                    Utils.showError(requireView(), "Failed to load market indices.")
                }

                is Resource.Success -> {
                    val data = resource.data
                    binding.loader.visibility = View.GONE
                    binding.container.visibility = View.VISIBLE

                    val chartMap = mapOf(
                        "kse 100" to (data?.chartItemKSE100 ?: emptyList()),
                        "kse 30" to (data?.chartItemKSE30 ?: emptyList()),
                        "kse all" to (data?.chartItemKSEALL ?: emptyList()),
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
                            //    { it.iNDEXCODE } // secondary alphabetical sort for the rest
                            )
                        )

                    indexAdapter.addItems(
                        sortedIndices,
                        chartMap
                    )
                }
            }
        })

        // ✅ Observe registration result
        viewModel.mutableRegister.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Error -> Utils.showError(binding.root, "Unable to Register")

                is Resource.Loading -> binding.loader.visibility = View.VISIBLE

                is Resource.Success -> {
                    binding.loader.visibility = View.GONE
                    val json = resource.data
                    if (!json?.isJsonNull!!) {
                        if (json.isJsonArray) {
                            val jObject = json.asJsonArray.firstOrNull()?.asJsonObject

                            val data = mutableListOf<LoginDataItem>()
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
                            Utils.showSuccess(binding.root, "Successfully Registered")
                            Utils.saveSharedPreference(requireContext(), AppConstants.USER, data)
                            (requireActivity() as MainActivity).loadFragment(LandingFragment(), false)
                        } else {
                            Utils.showError(binding.root, json.asString ?: "An error occurred...")
                        }
                    }
                }
            }
        })

        // ✅ Manual registration
        binding.button.setOnClickListener {
            if (binding.fullName.text.isNotEmpty()
                && binding.email.text.isNotEmpty()
                && binding.mobileNumber.text.isNotEmpty()
                && binding.password.text.isNotEmpty()
            ) {
                if (Utils.isPasswordValid(binding.password.text)) {
                    viewModel.registerUser(
                        fullName = binding.fullName.text.toString(),
                        email = binding.email.text.toString(),
                        password = binding.password.text.toString(),
                        mobile = binding.mobileNumber.text.toString(),
                        fireBaseID = fcm ?: ""
                    )
                } else {
                    Utils.showError(requireView(), "Invalid Password. Minimum 6 characters, one letter, and one digit required.")
                }
            } else {
                Utils.showError(binding.root, "Provide data for all fields")
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.mutableRegister.value = null
        viewModel.mutableResultIndices.value = null
        viewModel.isLineChart = false
    }

    private fun loadFragment(fragment: Fragment, isBackStack: Boolean = false) {
        val manager = parentFragmentManager
        if (isBackStack) {
            manager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        } else {
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            manager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }

    private fun bindView() {
        binding.recyclerIndices.apply {
            adapter = indexAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(VerticalDivider())
        }
    }
}
