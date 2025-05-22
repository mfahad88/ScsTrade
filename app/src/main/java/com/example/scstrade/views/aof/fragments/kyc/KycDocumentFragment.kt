package com.example.scstrade.views.aof.fragments.kyc

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
import com.example.scstrade.databinding.FragmentKycFifteenBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.request.aof.document.DocumentDto
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity


class KycDocumentFragment : Fragment() {
    lateinit var viewModel: AofViewModel
    lateinit var binding: FragmentKycFifteenBinding
    private var cameraImageUri: Uri? = null
    private val PERMISSION_CAMERA = Manifest.permission.CAMERA
    private val PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    private val PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val PERMISSION_REQ_CODE = 100
    private var proofPermanentAddressClicked=false
    private var proofPermanentEmployerAddressClicked=false
    private var proofSignatureClicked=false
    private var proofZakatClicked=false
    private var uriproofPermanentAddress: Uri?=null
    private var uriproofPermanentEmployerAddress: Uri?=null
    private var uriproofSignature: Uri?=null
    private var uriproofZakat: Uri?=null
    private var proofPermanentAddressBase64:String?=null
    private var proofPermanentEmployerAddressBase64:String?=null
    private var proofSignatureBase64:String?=null
    private var proofZakatBase64:String?=null

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            cameraImageUri?.let {
                if(proofPermanentAddressClicked) {
                    uriproofPermanentAddress=it
                    binding.proofOfPe.fileName = Utils.getFileNameFromUri(requireContext(), it)
                    proofPermanentAddressBase64 = Utils.convertImageUriToBase64(requireContext(), uriproofPermanentAddress!!)
                }else if(proofPermanentEmployerAddressClicked){
                    uriproofPermanentEmployerAddress=it
                    binding.incomeProo.fileName = Utils.getFileNameFromUri(requireContext(), it)
                    proofPermanentEmployerAddressBase64 = Utils.convertImageUriToBase64(requireContext(), uriproofPermanentEmployerAddress!!)
                }else if(proofSignatureClicked){
                    uriproofSignature=it
                    binding.specimenSi.fileName = Utils.getFileNameFromUri(requireContext(), it)
                    proofSignatureBase64 = Utils.convertImageUriToBase64(requireContext(), uriproofSignature!!)
                }else if(proofZakatClicked){
                    uriproofZakat = it
                    binding.zakatDecla.fileName = Utils.getFileNameFromUri(requireContext(),it)
                    proofZakatBase64 = Utils.convertImageUriToBase64(requireContext(), uriproofZakat!!)
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKycFifteenBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        initFields()
        binding.apply {
            proofOfPe.cardUpload.setOnClickListener {
                proofPermanentAddressClicked = true
                proofPermanentEmployerAddressClicked = false
                proofSignatureClicked = false
                proofZakatClicked = false
                requestRuntimePermission()
            }

            incomeProo.cardUpload.setOnClickListener {
                proofPermanentAddressClicked = false
                proofPermanentEmployerAddressClicked = true
                proofSignatureClicked = false
                proofZakatClicked = false
                requestRuntimePermission()
            }

            specimenSi.cardUpload.setOnClickListener {
                proofPermanentAddressClicked = false
                proofPermanentEmployerAddressClicked = false
                proofSignatureClicked = true
                proofZakatClicked = false
                requestRuntimePermission()
            }

            zakatDecla.cardUpload.setOnClickListener {
                incomeProo.setOnClickListener {
                    proofPermanentAddressClicked = false
                    proofPermanentEmployerAddressClicked = false
                    proofSignatureClicked = false
                    proofZakatClicked = true
                    requestRuntimePermission()
                }
            }

            btnContinue.setOnClickListener {
                if(uriproofPermanentEmployerAddress!=null && uriproofPermanentAddress!=null && uriproofSignature!=null && uriproofZakat!=null){
                    /*viewModel.documents(
                        DocumentDto(
                            accountType = viewModel.getotherDetail().otherDetailAccountType?:"",
                            identificationType =  viewModel.getSelfInfo().nicType?:"",
                            zakatStatus =  viewModel.getotherDetail().otherDetailZakatStatus?:"",
                            signatureProof = proofSignatureBase64?:"",
                            zakaatDeclaration = proofZakatBase64?:"",
                            addProof = proofPermanentEmployerAddressBase64?:"",
                            empAddProof = proofPermanentEmployerAddressBase64?:"",
                            termsAndCondition = "Y",
                            id = null
                        )
                    )*/
                }
            }
        }


        return binding.root
    }


    private fun proceedWithCameraOrStorage() {
        cameraImageUri = createImageUri()
        cameraImageUri?.let { takePictureLauncher.launch(it) }
    }

    private fun initFields() {
        binding.apply {
            proofOfPe.fileName=viewModel.getotherDetail().otherDetailProofParmanentAddressFilename
            proofPermanentAddressBase64=viewModel.getotherDetail().otherDetailProofParmanentAddress
            incomeProo.fileName=viewModel.getotherDetail().otherDetailProofEmployerAddressFilename
            proofPermanentEmployerAddressBase64=viewModel.getotherDetail().otherDetailProofEmployerAddress
            specimenSi.fileName=viewModel.getotherDetail().otherDetailSpecimenSignatureFilename
            proofSignatureBase64=viewModel.getotherDetail().otherDetailSpecimenSignature
            zakatDecla.fileName=viewModel.getotherDetail().otherDetailZakatDeclarationFilename
            proofZakatBase64=viewModel.getotherDetail().otherDetailZakatDeclaration
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