package com.example.scstrade.views.aof.fragments.accountopening
import androidx.compose.ui.res.dimensionResource

import android.Manifest
import android.content.ContentValues
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentAccountOpeningThreeBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity


/**
 * A simple [Fragment] subclass.
 * Use the [AccountOpeningThreeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountOpeningThreeFragment : Fragment() {
    lateinit var binding: FragmentAccountOpeningThreeBinding
    lateinit var viewModel: AofViewModel
    private var proofIbanB64: String? = null
    private var proofIbanCamUri: Uri? = null
    private var proofIbanNM:String? =null

    private var nicFrontB64: String? = null
    private var nicFrontCamUri: Uri? = null
    private var nicFrontNM:String? =null

    private var nicBackB64: String? = null
    private var nicBackCamUri: Uri? = null
    private var nicBackNM:String? =null

    private var proofRelB64: String? = null
    private var proofRelCamUri: Uri? = null
    private var proofRelNM:String? =null



    private var pendingCameraAction: (() -> Unit)? = null

    private val cameraPermissionLauncher =
        registerForActivityResult(RequestPermission()) { granted ->
            if (granted) {
                pendingCameraAction?.invoke()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Camera permission is required to take photos",
                    Toast.LENGTH_SHORT
                ).show()
            }
            pendingCameraAction = null
        }

    private fun withCameraPermission(action: () -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            action()
        } else {
            pendingCameraAction = action
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountOpeningThreeBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
//        initFields()
        if(viewModel.accountOpening.accountopeningrelationshipType.equals("1")){
            binding.proofOfRelative.visibility = View.GONE
        }else{
            binding.proofOfRelative.visibility = View.VISIBLE
        }

        binding.back.setOnClickListener {
            (requireActivity() as AofActivity).loadFragment(AccountOpeningTwoFragment())
        }

        val IbanGallery = registerForActivityResult(GetContent()) { uri ->
            uri?.let {
                binding.proofOfIb.bindImage(it)
                proofIbanB64 = "data:image/jpeg;base64,${binding.proofOfIb.getBase64()}"
            }
        }

        val IbanCamera = registerForActivityResult(TakePicture()) { ok ->
            if (ok) {
                proofIbanCamUri?.let {
                    binding.proofOfIb.bindImage(it)
                    proofIbanB64 = "data:image/jpeg;base64,${binding.proofOfIb.getBase64()}"
                }
            }
        }

        proofIbanCamUri = createImageUri()
        binding.proofOfIb.setGalleryLauncher(IbanGallery)
        binding.proofOfIb.binding.cardUpload.setOnClickListener {          // ← click from custom view
            withCameraPermission { IbanCamera.launch(proofIbanCamUri) }
        }
        //////////////////////////////////////////////////////////////

        val frontGallery = registerForActivityResult(GetContent()) { uri ->
            uri?.let {
                binding.nicFront.bindImage(it)
                nicFrontB64 = "data:image/jpeg;base64,${binding.nicFront.getBase64()}"
            }
        }

        val frontCamera = registerForActivityResult(TakePicture()) { ok ->
            if (ok) {
                nicFrontCamUri?.let {
                    binding.nicFront.bindImage(it)
                    nicFrontB64 = "data:image/jpeg;base64,${binding.nicFront.getBase64()}"
                }
            }
        }

        nicFrontCamUri = createImageUri()
        binding.nicFront.setGalleryLauncher(frontGallery)
        binding.nicFront.binding.cardUpload.setOnClickListener {          // ← click from custom view
            withCameraPermission { frontCamera.launch(nicFrontCamUri) }
        }

        /////////////////////////////////////////////////////////

        val backGallery = registerForActivityResult(GetContent()) { uri ->
            uri?.let {
                binding.nicBack.bindImage(it)
                nicBackB64 = "data:image/jpeg;base64,${binding.nicBack.getBase64()}"
            }
        }

        val backCamera = registerForActivityResult(TakePicture()) { ok ->
            if (ok) {
                nicFrontCamUri?.let {
                    binding.nicFront.bindImage(it)
                    nicBackB64 = "data:image/jpeg;base64,${binding.nicBack.getBase64()}"
                }
            }
        }

        nicBackCamUri = createImageUri()
        binding.nicBack.setGalleryLauncher(backGallery)
        binding.nicBack.binding.cardUpload.setOnClickListener {          // ← click from custom view
            withCameraPermission { backCamera.launch(nicBackCamUri) }
        }

        ////////////////////////////////////////////

        val relGallery = registerForActivityResult(GetContent()) { uri ->
            uri?.let {
                binding.proofOfRelative.bindImage(it)
                proofRelB64 = "data:image/jpeg;base64,${binding.proofOfRelative.getBase64()}"
            }
        }

        val relCamera = registerForActivityResult(TakePicture()) { ok ->
            if (ok) {
                nicFrontCamUri?.let {
                    binding.nicFront.bindImage(it)
                    proofRelB64 = "data:image/jpeg;base64,${binding.proofOfRelative.getBase64()}"
                }
            }
        }

        proofRelCamUri = createImageUri()
        binding.proofOfRelative.setGalleryLauncher(relGallery)
        binding.proofOfRelative.binding.cardUpload.setOnClickListener {          // ← click from custom view
            withCameraPermission { relCamera.launch(proofRelCamUri) }
        }


        binding.btnContinue.setOnClickListener {
            if(binding.proofOfIb.fileName!!.isNotEmpty() && binding.nicBack.fileName!!.isNotEmpty() && binding.nicFront.fileName!!.isNotEmpty()){
                viewModel.accountOpening.apply {
                    accountopeningproofIban = binding.proofOfIb.fileName
                    accountopeningproofIbanImage = proofIbanB64.toString()
                    accountopeningnicFront = binding.nicFront.fileName
                    accountopeningnicFrontImage = nicFrontB64.toString()
                    accountopeningnicBack = binding.nicBack.fileName
                    accountopeningnicBackImage = nicBackB64.toString()
                    accountopeningproofRelative = binding.proofOfRelative.fileName
                    accountopeningproofRelativeImage = proofRelB64.toString()
                }


//                viewModel.saveaccountOpening()

                (requireActivity() as AofActivity).loadFragment(AccountOpeningFourFragment())
            }else{
                Utils.showError(requireView(),"Empty Fields not allowed")
            }
        }
        return binding.root
    }

    private fun initFields() {
        binding.apply {
//            proofOfIb.fileName=viewModel.accountOpening.accountopeningproofIban
            proofIbanB64=viewModel.accountOpening.accountopeningproofIbanImage
//            nicFront.fileName=viewModel.accountOpening.accountopeningnicFront
            nicFrontB64=viewModel.accountOpening.accountopeningnicFrontImage
//            nicBack.fileName=viewModel.accountOpening.accountopeningnicBack
            nicBackB64=viewModel.accountOpening.accountopeningnicBackImage
//            proofOfRelative.fileName=viewModel.accountOpening.accountopeningproofRelative
            proofRelB64=viewModel.accountOpening.accountopeningproofRelativeImage
        }
    }






    private fun createImageUri(): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }


}