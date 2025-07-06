package com.example.scstrade.views.aof.fragments.kyc.otherDetail

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycTwelveBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.request.aof.otherDetail.OtherDetailDto
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.aof.fragments.kyc.CongratulationsFragment
import com.example.scstrade.views.aof.fragments.kyc.nomineeDetail.KycNomineeDetailOneFragment


class KycOtherDetailOneFragment : Fragment() {
    lateinit var  binding: FragmentKycTwelveBinding
    lateinit var viewModel: AofViewModel
    /* Base‑64 strings for API */
    private var permanentAddressB64: String? = null
    private var employeerAddressB64 : String? = null
    private var signatureB64 : String? = null
    private var zakatB64 : String? = null
    /* Output Uris for camera */
    private var permanentAddressUri: Uri? = null
    private var employeerAddressUri : Uri? = null
    private var signatureUri:Uri? =null
    private var zakatUri:Uri? =null
    private var permanentAddressNm:String?=null
    private var employeerAddressNm:String?=null
    private var signatureNm:String?=null
    private var zakatNm:String?=null
    private var uinType:String?=null
    /* ---------- runtime CAMERA permission ---------- */

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

    /* ------------------------------------------------ */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentKycTwelveBinding.inflate(inflater,container,false)
        viewModel = (requireActivity() as AofActivity).viewModel
        (requireActivity() as AofActivity).binding.welcome.text = getString(R.string.other_details)
        viewModel.getotherDetails()
        viewModel.getDocuments()
        initFields()
        binding.remittanceBasis.autoCompleteTextView1.isEnabled=false
        viewModel.mutableOtherDetail.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {
                    Log.e("Aof",result.message?:"")
                    Utils.showError(requireView(), result.message)
                    binding.loader.visibility = View.GONE
                }
                is Resource.Loading -> binding.loader.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.apply {
                        viewModel.documents(
                            requireContext(),
                            zakatStatus.selectedDropDown1.first ?: "",
                            accountType.selectedOption.second ?: "",
                            "Y",
                            uinType ?: "",
                            if(!TextUtils.isEmpty(signatureB64)) signatureB64!! else  signatureNm!!,
                            if(!TextUtils.isEmpty(permanentAddressB64)) permanentAddressB64!! else  permanentAddressNm!!,
                            if(!TextUtils.isEmpty(employeerAddressB64)) employeerAddressB64!! else  employeerAddressNm!!,
                            if(!TextUtils.isEmpty(zakatB64)) zakatB64!! else  zakatNm!!,
                        )
                    }
                    /*binding.loader.visibility = View.GONE
                    (requireActivity() as AofActivity).loadFragment(KycOtherDetailOneFragment())*/
                }
            }
        })

        viewModel.mutableDocument.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {
                    Log.e("Aof",result.message?:"")
                    Utils.showError(requireView(), result.message)
                    binding.loader.visibility = View.GONE
                }
                is Resource.Loading -> {}
                is Resource.Success -> {

                    binding.loader.visibility = View.GONE
                    val response =result.data

                    if(response?.isSuccess?:false){
                        (requireActivity() as AofActivity).loadFragment(CongratulationsFragment())
                    }else{
                        Utils.showError(requireView(),response?.message?:"An error occurred...")
                    }
                }
            }
        })
        binding.apply {
            back.setOnClickListener {
                (requireActivity() as AofActivity).loadFragment(KycNomineeDetailOneFragment())
            }

            btnContinue.setOnClickListener {
                if(!accountType.isSelectedOtionEmpty){
                    if(!grossAnnualIncomeslab.isEmpty &&
                        !sourceOfIncome.isEmpty &&
                        occupation.selectedDropDown1 !=null &&
                        !TextUtils.isEmpty(jobDetails.textview_1.text.toString()) &&
                        !TextUtils.isEmpty(jobDetails.textview_2.text.toString()) &&
                        !TextUtils.isEmpty(employerDetails.textview_1.text.toString()) &&
                        !TextUtils.isEmpty(employerDetails.textview_2.text.toString()) &&
                        zakatStatus.selectedDropDown1!=null){
                        if(accountType.selectedOption.second.equals("nka",true)) {
                            viewModel.otherDetails(
                                OtherDetailDto(
                                    accountType = accountType.selectedOption.second,
                                    annualIncomeNormal = grossAnnualIncomeslab.selectedDropDown.second,
                                    department = jobDetails.textview_2.text.toString(),
                                    employeeAddress = employerDetails.textview_2.text.toString(),
                                    employeeName = employerDetails.textview_1.text.toString(),
                                    id = null,
                                    jobTitle = jobDetails.textview_1.text.toString(),
                                    occupation = occupation.selectedDropDown1.first,
                                    otherOccupation = occupation.textview_2.text.toString(),
                                    remittanceBasis = remittanceBasis.selectedDropDown1.first,
                                    sourceOfIncome = sourceOfIncome.selectedOption,
                                    zakatStatus = zakatStatus.selectedDropDown1.first

                                )
                            )
                        }else if(accountType.selectedOption.second.equals("ska",true)){
                            viewModel.otherDetails(
                                OtherDetailDto(
                                    accountType = accountType.selectedOption.second,
                                    annualIncomeNormal = grossAnnualIncomeslab.selectedDropDown.second,
                                    department = if(!TextUtils.isEmpty(jobDetails.textview_2.text.toString()))jobDetails.textview_2.text.toString()else "N/A",
                                    employeeAddress = if(!TextUtils.isEmpty(employerDetails.textview_2.text.toString()))employerDetails.textview_2.text.toString() else "N/A",
                                    employeeName = if(!TextUtils.isEmpty(employerDetails.textview_1.text.toString())) employerDetails.textview_1.text.toString()else "N/A",
                                    id = null,
                                    jobTitle = if(!TextUtils.isEmpty(jobDetails.textview_1.text.toString()))jobDetails.textview_1.text.toString() else "N/A",
                                    occupation = occupation.selectedDropDown1.first,
                                    otherOccupation = occupation.textview_2.text.toString(),
                                    remittanceBasis = remittanceBasis.selectedDropDown1.first,
                                    sourceOfIncome = sourceOfIncome.selectedOption,
                                    zakatStatus = zakatStatus.selectedDropDown1.first

                                )
                            )
                        }
                    }else{
                        Utils.showError(requireView(),getString(R.string.empty_fields_not_allowed))
                    }

                }else{
                    Utils.showError(requireView(),getString(R.string.empty_fields_not_allowed))
                }
            }
        }
        binding.accountType.setOnButtonOneClickListener {
            binding.grossAnnualIncomeslab.setListEntries(AppConstants.AnnualIncomeNormal.map { android.util.Pair(it.first,it.second) })
            binding.proofOfPe.visibility=View.VISIBLE
            binding.incomeProo.visibility = View.VISIBLE
            clearFields()
        }

        binding.accountType.setOnButtonTwoClickListener {
            binding.grossAnnualIncomeslab.setListEntries(AppConstants.AnnualIncomeSahulat.map { android.util.Pair(it.first,it.second) })
            binding.proofOfPe.visibility=View.GONE
            binding.incomeProo.visibility = View.GONE
            clearFields()
        }

        binding.occupation.mutableselectedDropDown1.observe(viewLifecycleOwner, Observer { result->
            if(result.second.equals("others",true)){
                binding.occupation.textview_2.isEnabled=true
            }else{
                binding.occupation.textview_2.isEnabled=false
            }
        })

        binding.zakatStatus.mutableselectedDropDown1.observe(viewLifecycleOwner, Observer { result->
            if(result.first.equals("5")){
                binding.zakatDecla.visibility = View.VISIBLE
            }else{
                binding.zakatDecla.visibility = View.GONE
            }
        })
     /*   binding.zakatStatus.autoCompleteTextView1.setOnItemClickListener { adapterView, view, i, l ->
            if(binding.zakatStatus.selectedDropDown1.first.equals("5")){
                binding.zakatDecla.visibility = View.VISIBLE
            }else{
                binding.zakatDecla.visibility = View.GONE
            }
        }*/


        val permanentAddressGallery = registerForActivityResult(GetContent()) { uri ->
            uri?.let {
                binding.proofOfPe.bindImage(it)
                permanentAddressB64 = "data:image/jpeg;base64,${binding.proofOfPe.getBase64()}"
            }
        }

        val permanentAddressCamera = registerForActivityResult(TakePicture()) { ok ->
            if (ok) {
                permanentAddressUri?.let {
                    binding.proofOfPe.bindImage(it)
                    permanentAddressB64 = "data:image/jpeg;base64,${binding.proofOfPe.getBase64()}"
                }
            }
        }

        permanentAddressUri = createImageUri()
        binding.proofOfPe.setGalleryLauncher(permanentAddressGallery)
        binding.proofOfPe.binding.cardUpload.setOnClickListener {          // ← click from custom view
            withCameraPermission { permanentAddressCamera.launch(permanentAddressUri) }
        }
        /////////////////////////////////////////////////////////////////////////////////
        val employerAddressGallery = registerForActivityResult(GetContent()) { uri ->
            uri?.let {
                binding.incomeProo.bindImage(it)
                employeerAddressB64 = "data:image/jpeg;base64,${binding.incomeProo.getBase64()}"
            }
        }

        val employerAddressCamera = registerForActivityResult(TakePicture()) { ok ->
            if (ok) {
                employeerAddressUri?.let {
                    binding.incomeProo.bindImage(it)
                    employeerAddressB64 = "data:image/jpeg;base64,${binding.incomeProo.getBase64()}"
                }
            }
        }

        employeerAddressUri = createImageUri()
        binding.incomeProo.setGalleryLauncher(employerAddressGallery)
        binding.incomeProo.binding.cardUpload.setOnClickListener {          // ← click from custom view
            withCameraPermission { employerAddressCamera.launch(employeerAddressUri) }
        }

        /////////////////////////////////////////////////////////////////////////////////


        val signatureGallery = registerForActivityResult(GetContent()) { uri ->
            uri?.let {
                binding.specimenSi.bindImage(it)
                signatureB64 = "data:image/jpeg;base64,${binding.specimenSi.getBase64()}"
            }
        }

        val signatureCamera = registerForActivityResult(TakePicture()) { ok ->
            if (ok) {
                signatureUri?.let {
                    binding.specimenSi.bindImage(it)
                    signatureB64 = "data:image/jpeg;base64,${binding.specimenSi.getBase64()}"
                }
            }
        }

        signatureUri = createImageUri()
        binding.specimenSi.setGalleryLauncher(signatureGallery)
        binding.specimenSi.binding.cardUpload.setOnClickListener {          // ← click from custom view
            withCameraPermission { signatureCamera.launch(signatureUri) }
        }

        /////////////////////////////////////////////////////////////////////////////////


        val zakatGallery = registerForActivityResult(GetContent()) { uri ->
            uri?.let {
                binding.zakatDecla.bindImage(it)
                zakatB64 = "data:image/jpeg;base64,${binding.zakatDecla.getBase64()}"
            }
        }

        val zakatCamera = registerForActivityResult(TakePicture()) { ok ->
            if (ok) {
                zakatUri?.let {
                    binding.zakatDecla.bindImage(it)
                    zakatB64 = "data:image/jpeg;base64,${binding.zakatDecla.getBase64()}"
                }
            }
        }

        zakatUri = createImageUri()
        binding.zakatDecla.setGalleryLauncher(zakatGallery)
        binding.zakatDecla.binding.cardUpload.setOnClickListener {          // ← click from custom view
            withCameraPermission { zakatCamera.launch(zakatUri) }
        }

        /////////////////////////////////////////////////////////////////////////////////


        return binding.root
    }
    private fun createImageUri(): Uri? {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return requireContext().contentResolver
            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    private fun initFields() {
        binding.apply {
            bank.textview_1.isEnabled=false
            accountType.setList(AppConstants.AccountType.map { android.util.Pair(it.first,it.second) })
            occupation.setListEntriesFirst(AppConstants.Occupation.map { android.util.Pair(it.second,it.first) })
            zakatStatus.setListEntriesFirst(AppConstants.ZakatType.map { android.util.Pair(it.second,it.first) })
            remittanceBasis.setListEntriesFirst(AppConstants.RemittanceDescription.map { android.util.Pair(it.second,it.first) })
        }
        viewModel.mutableOtherDetailResponse.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Resource.Error -> {
                    binding.loader.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.loader.visibility =View.VISIBLE
                }
                is Resource.Success -> {
                    val response = result.data?.data
                    binding.accountType.setSelectedOption(response?.accountType)
                    if(response?.accountType?.equals("nka")?:false){
                        binding.grossAnnualIncomeslab.setListEntries(AppConstants.AnnualIncomeNormal.map { android.util.Pair(it.first,it.second) })
                        binding.grossAnnualIncomeslab.setSelectedDropDown(response?.annualIncomeNormal)
                    }else{
                        binding.grossAnnualIncomeslab.setListEntries(AppConstants.AnnualIncomeSahulat.map { android.util.Pair(it.first,it.second) })
                        binding.grossAnnualIncomeslab.setSelectedDropDown(response?.annualIncomeNormal)
                    }

                    binding.sourceOfIncome.selectedOption=response?.sourceOfIncome ?:""
                    binding.occupation.setSelectedDropDown1(response?.occupation)
                    if(!TextUtils.isEmpty(response?.otherOccupation)?:false) {
                        binding.occupation.textview_1.setText(response?.otherOccupation ?: "")
                    }
                    if(response?.occupation.equals("P999")?:false){
                        binding.occupation.textview_1.isEnabled=true
                    }else{
                        binding.occupation.textview_1.isEnabled=false
                    }

                    binding.jobDetails.textview_1.setText(response?.jobTitle ?: "")
                    binding.jobDetails.textview_2.setText(response?.department?: "")
                    binding.employerDetails.textview_1.setText(response?.employeeName?: "")
                    binding.employerDetails.textview_2.setText(response?.employeeAddress?: "")
                    binding.bank.textview_1.setText(response?.bankName?:"")
                    binding.zakatStatus.setSelectedDropDown1(response?.zakatStatus)
                    if(!TextUtils.isEmpty(response?.remittanceBasis)) {
                        if(!response?.remittanceBasis.equals("N/A",true)){
                            binding.remittanceBasis.setSelectedDropDown1(response?.remittanceBasis)
                        }

                    }
                    binding.loader.visibility = View.GONE
                }
            }
        })

        viewModel.mutableDocumentDataResponse.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> Utils.showError(requireView(),result.message)
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val data=result.data?.data
                    uinType=data?.identificationType
                    permanentAddressNm=data?.addProof
                    employeerAddressNm=data?.empAddProof
                    signatureNm=data?.signatureProof
                    zakatNm=data?.zakaatDeclaration

                    if(!TextUtils.isEmpty(permanentAddressNm)){
                        binding.proofOfPe.fileName=permanentAddressNm
                        binding.proofOfPe.updateVisuals(true)
                    }

                    if(!TextUtils.isEmpty(employeerAddressNm)){
                        binding.incomeProo.fileName=employeerAddressNm
                        binding.incomeProo.updateVisuals(true)
                    }

                    if(!TextUtils.isEmpty(signatureNm)){
                        binding.specimenSi.fileName=signatureNm
                        binding.specimenSi.updateVisuals(true)
                    }

                    if(!TextUtils.isEmpty(zakatNm)){
                        binding.zakatDecla.fileName=zakatNm
                        binding.zakatDecla.updateVisuals(true)
                    }
                }
            }
        })


    }

    public fun clearFields(){
        binding.apply {
            grossAnnualIncomeslab.dropdown.text.clear()
            sourceOfIncome.textInputEditText.text?.clear()
            sourceOfIncome.textInputEditText.text?.clear()
            jobDetails.textview_1.text.clear()
            jobDetails.textview_2.text.clear()
            employerDetails.textview_1.text.clear()
            employerDetails.textview_2.text.clear()
            zakatStatus.autoCompleteTextView1.text.clear()
        }
    }
}