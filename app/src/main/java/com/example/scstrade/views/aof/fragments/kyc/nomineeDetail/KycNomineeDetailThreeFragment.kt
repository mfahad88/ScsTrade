package com.example.scstrade.views.aof.fragments.kyc.nomineeDetail

import android.Manifest
import android.content.ContentValues
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.Uri
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
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycElevenBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.request.aof.nomineeDetail.NomineeDetailDto
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.aof.fragments.kyc.otherDetail.KycOtherDetailOneFragment


/**
 * A simple [Fragment] subclass.
 * Use the [KycNomineeDetailThreeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KycNomineeDetailThreeFragment : Fragment() {
    lateinit var binding: FragmentKycElevenBinding
    lateinit var viewModel: AofViewModel
    private var cameraImageUri: Uri? = null
    private val PERMISSION_CAMERA = Manifest.permission.CAMERA
    private val PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    private val PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val PERMISSION_REQ_CODE = 100
    private var nicFrontClicked=false
    private var nicBackClicked=false
    private var uriNicFront: Uri?=null
    private var uriNicBack: Uri?=null
    private var nicFrontBase64:String?=null
    private var nicBackBase64:String?=null

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            cameraImageUri?.let {
              if(nicFrontClicked){
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

    private val pickImageLauncher  = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        try {
            uri?.let {
                if(nicFrontClicked){
                    uriNicFront=it
                    binding.nicFront.fileName = Utils.getFileNameFromUri(requireContext(), it)
                    nicFrontBase64 = Utils.convertImageUriToBase64(requireContext(), uriNicFront!!)
                }else{
                    uriNicBack=it
                    binding.nicBack.fileName = Utils.getFileNameFromUri(requireContext(), it)
                    nicBackBase64 = Utils.convertImageUriToBase64(requireContext(), uriNicBack!!)
                }
            }
        }catch (e:Exception)
        {
            e.printStackTrace()
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
        binding = FragmentKycElevenBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        (requireActivity() as AofActivity).binding.welcome.text = getString(R.string.nominee_details)
        initFields()

        binding.nicFront.cardUpload.setOnClickListener {
            nicFrontClicked=true
            nicBackClicked=false
            requestRuntimePermission()
        }
        binding.nicFront.materialSelect.setOnClickListener {
            nicFrontClicked=true
            nicBackClicked=false
            pickImageLauncher.launch("image/*")
        }

        binding.nicBack.cardUpload.setOnClickListener{
            nicFrontClicked=false
            nicBackClicked=true
            requestRuntimePermission()
        }

        binding.nicBack.materialSelect.setOnClickListener {
            nicFrontClicked=true
            nicBackClicked=false
            pickImageLauncher.launch("image/*")
        }

        binding.back.setOnClickListener {
            (requireActivity() as AofActivity).supportFragmentManager.popBackStack()
        }

        binding.btnContinue.setOnClickListener {
            if(binding.nicBack.fileName.isNotEmpty() && binding.nicFront.fileName.isNotEmpty()){
                viewModel.nominee.apply {
                    nomineeNicFrontFileName = binding.nicFront.fileName
                    nomineeNicFront = nicFrontBase64
                    nomineeNicBackFileName = binding.nicBack.fileName
                    nomineeNicBack = nicBackBase64
                    viewModel.nomineeDetails(
                        NomineeDetailDto(
                            addressNmn = nomineeAddress?:"",
                            cnicExpiryDateNmn = nomineeNicExpiry?:"",
                            cnicLifeTimeNmn = nomineeNicType?:"",
                            cnicNmn = nomineeUinNumber?:"",
                            id = null,
                            identificationNmn = nomineeUinType?:"",
                            mobileNoNmn = nomineeMobileNumber?:"",
                            nameNmn = nomineeName?:"",
                            nicBackNmn = nicBackBase64?:"",
                            nicFrontNmn = nicFrontBase64?:"",
                            nomineeType = isNominee?:"",
                            relationShipNmn = nomineeRelation?:""
                        )
                    )
                }
//                viewModel.savenominee()

            }else{
                Utils.showError(requireView(),"Empty Fields not allowed")
            }

        }
        viewModel.mutableNomineeDetail.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {
                    Utils.showError(requireView(),result.message?:"An error occurred...")
                    binding.loader.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    val response= result.data

                    if(response?.statusCode==200 && response.isSuccess){
                        (requireActivity() as AofActivity).loadFragment(KycOtherDetailOneFragment())
                    }else{
                        Utils.showError(requireView(),response?.message?:"An error occurred...")
                    }
                    binding.loader.visibility = View.GONE
                }
            }
        })

        return binding.root
    }

    private fun initFields() {
        binding.apply {
            nicFront.fileName=viewModel.getnominee().nomineeNicFrontFileName
            nicFrontBase64=viewModel.getnominee().nomineeNicFront
            nicBack.fileName=viewModel.getnominee().nomineeNicBackFileName
            nicBackBase64=viewModel.getnominee().nomineeNicBack
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
                .setPositiveButton("Ok",object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(PERMISSION_CAMERA),PERMISSION_REQ_CODE)
                        p0?.dismiss()

                    }

                })
                .setNeutralButton("Cancel",object: DialogInterface.OnClickListener{
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
            if(grantResults.size>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                proceedWithCameraOrStorage()
            }else if(!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),PERMISSION_CAMERA)){
                Toast.makeText(requireContext(),"Please check permission...", Toast.LENGTH_SHORT).show()
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