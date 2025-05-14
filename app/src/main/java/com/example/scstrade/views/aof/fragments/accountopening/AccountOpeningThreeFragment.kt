package com.example.scstrade.views.aof.fragments.accountopening

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
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentAccountOpeningThreeBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountOpeningThreeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountOpeningThreeFragment : Fragment() {
    lateinit var binding: FragmentAccountOpeningThreeBinding
    lateinit var viewModel: AofViewModel
    private var cameraImageUri: Uri? = null
    private val PERMISSION_CAMERA = Manifest.permission.CAMERA
    private val PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    private val PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val PERMISSION_REQ_CODE = 100
    private var proofIbanClicked=false
    private var nicFrontClicked=false
    private var nicBackClicked=false
    private var uriIban:Uri?=null
    private var uriNicFront:Uri?=null
    private var uriNicBack:Uri?=null
    private var ibanBase64:String?=null
    private var nicFrontBase64:String?=null
    private var nicBackBase64:String?=null

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            cameraImageUri?.let {
                if(proofIbanClicked) {
                    uriIban=it
                    binding.proofOfIb.fileName = Utils.getFileNameFromUri(requireContext(), it)
                    ibanBase64 = Utils.convertImageUriToBase64(requireContext(), uriIban!!)
                }else if(nicFrontClicked){
                    uriNicFront=it
                    binding.nicFront.fileName = Utils.getFileNameFromUri(requireContext(), it)
                    nicFrontBase64 = Utils.convertImageUriToBase64(requireContext(), uriNicFront!!)
                }else{
                    uriNicBack=it
                    binding.nicBack.fileName = Utils.getFileNameFromUri(requireContext(), it)
                    nicBackBase64 = Utils.convertImageUriToBase64(requireContext(), uriNicBack!!)
                }
            }
        }
    }


    private fun proceedWithCameraOrStorage() {
        cameraImageUri = createImageUri()
        cameraImageUri?.let { takePictureLauncher.launch(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountOpeningThreeBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        initFields()
        binding.proofOfIb.cardUpload.setOnClickListener {
            proofIbanClicked=true
            nicFrontClicked=false
            nicBackClicked=false
            requestRuntimePermission()

        }
        binding.nicFront.cardUpload.setOnClickListener {
            proofIbanClicked=false
            nicFrontClicked=true
            nicBackClicked=false
            requestRuntimePermission()
        }
        binding.nicBack.cardUpload.setOnClickListener{
            proofIbanClicked=false
            nicFrontClicked=false
            nicBackClicked=true
            requestRuntimePermission()
        }

        binding.back.setOnClickListener {
            (requireActivity() as AofActivity).loadFragment(AccountOpeningTwoFragment())
        }

        binding.btnContinue.setOnClickListener {
            if(binding.proofOfIb.fileName.isNotEmpty() && binding.nicBack.fileName.isNotEmpty() && binding.nicFront.fileName.isNotEmpty()){
                viewModel.saveDocuments(binding.proofOfIb.fileName,
                    ibanBase64.toString(),
                    binding.nicFront.fileName,
                    nicFrontBase64.toString(),
                    binding.nicBack.fileName,
                    nicBackBase64.toString()
                    )
                binding.proofOfIb.fileName=null
                binding.nicFront.fileName=null
                binding.nicBack.fileName=null
                (requireActivity() as AofActivity).loadFragment(AccountOpeningFourFragment())
            }else{
                Utils.showError(requireView(),"Empty Fields not allowed")
            }
        }
        return binding.root
    }

    private fun initFields() {
        binding.apply {
            proofOfIb.fileName=viewModel.getDocuments().proofIban
            ibanBase64=viewModel.getDocuments().proofIbanImage
            nicFront.fileName=viewModel.getDocuments().nicFront
            nicFrontBase64=viewModel.getDocuments().nicFrontImage
            nicBack.fileName=viewModel.getDocuments().nicBack
            nicBackBase64=viewModel.getDocuments().nicBackImage
        }
    }

    private fun requestRuntimePermission() {
        if(ActivityCompat.checkSelfPermission(requireContext(),PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED){
            proceedWithCameraOrStorage()
        }else if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),PERMISSION_CAMERA)){
            val builder= AlertDialog.Builder(requireContext())
                .setMessage("This app requires CAMERA permission for taking picture")
                .setTitle("Permission Required")
                .setCancelable(false)
                .setPositiveButton("Ok",object :DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(PERMISSION_CAMERA),PERMISSION_REQ_CODE)
                        p0?.dismiss()

                    }

                })
                .setNeutralButton("Cancel",object:DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        p0?.dismiss()
                    }

                }).show()
        }else{
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(PERMISSION_CAMERA,PERMISSION_READ_EXTERNAL_STORAGE,PERMISSION_WRITE_EXTERNAL_STORAGE),PERMISSION_REQ_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQ_CODE){
            if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                proceedWithCameraOrStorage()
            }else if(!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),PERMISSION_CAMERA)){
                Toast.makeText(requireContext(),"Please check permission...",Toast.LENGTH_SHORT).show()
            }else{
                requestRuntimePermission()
            }
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