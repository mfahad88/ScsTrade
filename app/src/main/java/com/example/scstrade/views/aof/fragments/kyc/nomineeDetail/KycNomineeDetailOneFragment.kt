package com.example.scstrade.views.aof.fragments.kyc.nomineeDetail

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentKycNineBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.request.aof.attorneyDetail.AttorneyDetailDto
import com.example.scstrade.model.request.aof.nomineeDetail.NomineeDetailDto
import com.example.scstrade.viewmodels.AofViewModel
import com.example.scstrade.views.aof.AofActivity
import com.example.scstrade.views.aof.fragments.kyc.attorneyDetail.KycAttorneyDetailOneFragment
import com.example.scstrade.views.aof.fragments.kyc.otherDetail.KycOtherDetailOneFragment

class KycNomineeDetailOneFragment : Fragment() {

    private lateinit var binding: FragmentKycNineBinding
    private lateinit var viewModel: AofViewModel

    /* Base‑64 strings for API */
    private var nicFrontB64: String? = null
    private var nicBackB64 : String? = null

    /* Output Uris for camera */
    private var frontCamUri: Uri? = null
    private var backCamUri : Uri? = null
    private var nicNMBack:String? =null
    private var nicNMFront:String? =null
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

    override fun onDestroyView() {
        viewModel.mutableNomineeDetail.value =null
        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentKycNineBinding.inflate(inflater, container, false)
        viewModel = (requireActivity() as AofActivity).viewModel
        (requireActivity() as AofActivity).binding.welcome.text =
            getString(R.string.nominee_details)

        initFields()
        binding.nominee.setList(AppConstants.NomineeType.map { android.util.Pair(it.first,it.second) })
        binding.nominee.apply {
            setOnButtonOneClickListener {
                binding.nomineeView.visibility = View.VISIBLE
            }
            setOnButtonTwoClickListener {
                binding.nomineeView.visibility = View.GONE
            }
        }
        /* ---------- FRONT ---------- */
        val frontGallery = registerForActivityResult(GetContent()) { uri ->
            uri?.let {
                binding.nicFront.bindImage(it)
                nicFrontB64 = "data:image/jpeg;base64,${binding.nicFront.getBase64()}"
            }
        }

        val frontCamera = registerForActivityResult(TakePicture()) { ok ->
            if (ok) {
                frontCamUri?.let {
                    binding.nicFront.bindImage(it)
                    nicFrontB64 = "data:image/jpeg;base64,${binding.nicFront.getBase64()}"
                }
            }
        }

        frontCamUri = createImageUri()
        binding.nicFront.setGalleryLauncher(frontGallery)
        binding.nicFront.binding.cardUpload.setOnClickListener {          // ← click from custom view
            withCameraPermission { frontCamera.launch(frontCamUri) }
        }

        /* ---------- BACK ---------- */
        val backGallery = registerForActivityResult(GetContent()) { uri ->
            uri?.let {
                binding.nicBack.bindImage(it)
                nicBackB64 = "data:image/jpeg;base64,${binding.nicBack.getBase64()}"
            }
        }

        val backCamera = registerForActivityResult(TakePicture()) { ok ->
            if (ok) {
                backCamUri?.let {
                    binding.nicBack.bindImage(it)
                    nicBackB64 = "data:image/jpeg;base64,${binding.nicBack.getBase64()}"
                }
            }
        }

        backCamUri = createImageUri()
        binding.nicBack.setGalleryLauncher(backGallery)
        binding.nicBack.binding.cardUpload.setOnClickListener {
            withCameraPermission { backCamera.launch(backCamUri) }
        }
        viewModel.mutableNomineeDetail.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {
                    Log.e("Aof",result.message?:"")
                    Utils.showError(requireView(), result.message)
                    binding.loader.visibility = View.GONE
                }
                is Resource.Loading -> binding.loader.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.loader.visibility = View.GONE
                    (requireActivity() as AofActivity).loadFragment(KycOtherDetailOneFragment())
                }
            }
        })
        binding.apply {
            nomineeNic.editText.setOnFocusChangeListener { view, b ->
                if(b){
                    Utils.showDatePicker(requireContext()){
                        nomineeNic.textFieldValue=it
                    }
                }
            }
            back.setOnClickListener {
                (requireActivity() as AofActivity).loadFragment(KycAttorneyDetailOneFragment())
            }
            btnContinue.setOnClickListener {

                if(nominee.selectedOption.second.equals("n",true)){

                    viewModel.nomineeDetails(
                        NomineeDetailDto(
                            addressNmn = null,
                            cnicExpiryDateNmn = null,
                            cnicLifeTimeNmn = null,
                            cnicNmn = null,
                            identificationNmn = null,
                            mobileNoNmn = null,
                            nameNmn = null,
                            nicBackNmn = null,
                            nicFrontNmn = null,
                            nomineeType = nominee.selectedOption.second,
                            relationShipNmn = null,
                            id = null
                        )
                    )

                }else{
                    if(
                        !nomineeRelation.isEmpty &&
                        !nomineeName.isEmpty &&
                        !nomineeMobile.isEmpty &&
                        !uinType.isEmpty &&
                        !uinNumber.isEmpty &&
                        !TextUtils.isEmpty(nomineeAddress.text.toString()) &&
                        !nomineeNic.isSelectedOtionEmpty &&
                        (!TextUtils.isEmpty(nicNMBack.toString()) || !TextUtils.isEmpty(nicBackB64.toString())) &&
                        (!TextUtils.isEmpty(nicNMFront.toString()) || !TextUtils.isEmpty(nicFrontB64.toString()))
                    ){
                        viewModel.nomineeDetails(
                            NomineeDetailDto(
                                addressNmn = nomineeAddress.text.toString(),
                                cnicExpiryDateNmn = nomineeNic.textFieldValue,
                                cnicLifeTimeNmn = nomineeNic.selectedOption.second,
                                cnicNmn = uinNumber.selectedOption,
                                identificationNmn = uinType.selectedDropDown.second,
                                mobileNoNmn = nomineeMobile.selectedOption,
                                nameNmn = nomineeName.selectedOption,
                                nicBackNmn = if(!TextUtils.isEmpty(nicBackB64)) nicBackB64 else nicNMBack,
                                nicFrontNmn = if(!TextUtils.isEmpty(nicFrontB64)) nicFrontB64 else nicNMFront,
                                nomineeType = nominee.selectedOption.second,
                                relationShipNmn = nomineeRelation.selectedDropDown.second,
                                id = null
                            )
                        )
                    }else{
                        Utils.showError(requireView(),getString(R.string.empty_fields_not_allowed))
                    }

                }


            }
        }
        return binding.root
    }

    /* ---------- helpers ---------- */

    private fun createImageUri(): Uri? {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return requireContext().contentResolver
            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    private fun initFields() {
        binding.nominee.setList(AppConstants.NomineeType.map { android.util.Pair(it.first, it.second) })
        binding.nomineeRelation.setListEntries(AppConstants.NomineeRelation.map { android.util.Pair(it.first, it.second) })
        binding.uinType.setListEntries(AppConstants.NIC_TYPE_LIST.map { android.util.Pair(it.first, it.second) })
        binding.nomineeNic.setList(AppConstants.LIFETIMECNICSTATUSLIST.map { android.util.Pair(it.first, it.second) })

        viewModel.mutableNomineeDetailResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    val data = result.data?.data
                    binding.nominee.setSelectedOption(data?.nomineeType)
                    if (data?.nomineeType == "Y") {
                        binding.nomineeView.visibility = View.VISIBLE
                        binding.nomineeRelation.setSelectedDropDown(data.relationShipNmn)
                        binding.nomineeName.selectedOption   = data.nameNmn
                        binding.nomineeMobile.selectedOption = data.mobileNoNmn
                        binding.uinType.setSelectedDropDown(data.identificationNmn)
                        binding.uinNumber.selectedOption=data.cnicNmn
                        binding.nomineeAddress.setText(data.addressNmn)
                        binding.nomineeNic.setSelectedOption(data.cnicLifeTimeNmn)
                        if(data.cnicLifeTimeNmn.equals("n",true)){

                            binding.nomineeNic.textFieldValue=Utils.formatDateString(data.cnicExpiryDateNmn)
                        }
                        nicNMBack=data.nicBackNmn
                        nicNMFront=data.nicFrontNmn

                        if(!TextUtils.isEmpty(nicNMBack)){
                            binding.nicBack.fileName=nicNMBack
                            binding.nicBack.updateVisuals(true)
                        }

                        if(!TextUtils.isEmpty(nicNMFront)){
                            binding.nicFront.fileName=nicNMFront
                            binding.nicFront.updateVisuals(true)
                        }
                    }
                }
                else -> { /* Loading & Error cases unchanged */ }
            }
        })
    }
}
