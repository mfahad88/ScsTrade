package com.example.scstrade.views.snapshot
import androidx.compose.ui.res.dimensionResource

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mycalendar_sdk.CustomDatePickerDialog
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentAnnouncementsBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.helper.downloadPdf
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.viewmodels.SnapshotViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.announcement.AnnoucementActivity
import com.example.scstrade.views.snapshot.adapter.AnnouncementAdapter
import com.example.scstrade.views.widgets.CustomArrayAdapter
import com.example.scstrade.views.widgets.VerticalSpaceItemDecoration
import com.example.scstrade.views.widgets.ZoomImageView
import java.io.File
import java.text.SimpleDateFormat


class AnnouncementsFragment : Fragment() {
    lateinit var binding:FragmentAnnouncementsBinding
    lateinit var symbol:String
    val sdf=SimpleDateFormat("dd/MM/yyyy")
    lateinit var snapshotViewModel: SnapshotViewModel
    lateinit var sharedViewModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAnnouncementsBinding.inflate(inflater,container,false)
        init()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.main.apply {
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            addItemDecoration(
                VerticalSpaceItemDecoration(1,
                    ContextCompat.getColor(requireContext(),R.color.md_theme_outline))
            )
            adapter=AnnouncementAdapter(

                onEyeClick = {
                    binding.loader.visibility = View.VISIBLE
                    val dialog = Dialog(requireContext())
                    dialog.setContentView(R.layout.dialog_image)
                    Glide
                        .with(requireContext())
                        .load(it?.imageLink ?: "")
                        .into(dialog.findViewById<ZoomImageView>(R.id.imageView))
                    dialog.setCancelable(false)
                    dialog.setCanceledOnTouchOutside(false)
                    dialog
                        .findViewById<ImageView>(R.id.btnClose)
                        .setOnClickListener {
                            dialog.dismiss()
                        }
                    dialog.show()
                    binding.loader.visibility = View.GONE

                },
                onDownloadClick = {
                    binding.loader.visibility = View.VISIBLE
                    downloadPdf(
                        requireContext(),
                        it?.pDFLink.toString()
                    ) { file ->
                        requireActivity().runOnUiThread {
                            if (file != null) {
                                openPdf(requireContext(), file)
                                binding.loader.visibility = View.GONE
                            }
                        }
                    }
                },
                onShareClick = {
                    binding.loader.visibility = View.VISIBLE
                    downloadPdf(
                        requireContext(),
                        it?.pDFLink.toString()
                    ) { file ->
                        requireActivity().runOnUiThread {
                            if (file != null) {
                                sharePdf(file)
                            }
                        }
                    }
                }
            )
        }

        binding.spinnerAnnouncement.apply {
            onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedLabel = parent!!.getItemAtPosition(position) as String
                    if(selectedLabel.equals("all",true)){
                        binding.textDate.text.clear()
                    }
//                    binding.textDate.text.clear()
                    snapshotViewModel.announcement(symbol,selectedLabel,binding.textDate.text.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
        }
        snapshotViewModel.mutableAnnouncementType.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {
                    binding.loader.visibility=View.GONE
                    Utils.showError(requireView(),result.message)
                }
                is Resource.Loading -> {
                    binding.loader.visibility=View.VISIBLE

                }
                is Resource.Success -> {
                    binding.apply {
                        loader.visibility = View.GONE
                    }
                    binding.spinnerAnnouncement.adapter=
                        CustomArrayAdapter(requireContext(),result.data?.map { it.type }?.toList()?: emptyList())

                }
            }
        })

        snapshotViewModel.mutableAnnouncementItem.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Error -> {
                    Utils.showError(requireView(),result.message)
                    binding.loader.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.loader.visibility = View.GONE
                    if(!result.data.isNullOrEmpty()) {
                        binding.main.visibility = View.VISIBLE
                        binding.noAnnouncement.visibility = View.GONE
                        (binding.main.adapter as AnnouncementAdapter).setData(
                            result.data ?: emptyList()
                        )
                    }else{
                        binding.main.visibility = View.GONE
                        binding.noAnnouncement.visibility = View.VISIBLE
                    }

                }
            }
        })

        binding.textDate.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false
            private var deletingHyphen = false
            private var lastInput = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                deletingHyphen = count > after && s?.getOrNull(start) == '/'
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                if (isFormatting || editable == null) return

                isFormatting = true
                val input = editable.toString().replace("/", "") // Remove existing slashes
                val formatted = StringBuilder()

                for (i in input.indices) {
                    formatted.append(input[i])
                    if ((i == 1 || i == 3) && i < input.length - 1) {
                        formatted.append("/") // Add slash at the correct positions
                    }
                }

                lastInput = formatted.toString()
                binding.textDate.setText(lastInput)
                binding.textDate.setSelection(lastInput.length) // Move cursor to the end
                isFormatting = false
                if(binding.textDate.text.length==8){
                    Utils.hideKeyboard(requireContext(),binding.textDate)
                    snapshotViewModel.announcement(
                        symbol,
                        binding.spinnerAnnouncement.selectedItem.toString(),
                        binding.textDate.text.toString()
                    )
                }else if(binding.textDate.text.length==0){
                    Utils.hideKeyboard(requireContext(),binding.textDate)
                    snapshotViewModel.announcement(
                        symbol,
                        binding.spinnerAnnouncement.selectedItem.toString(),
                        ""
                    )
                }
            }
        })

        binding.relativeLayoutDate.setOnClickListener {
            showDatePicker(binding.textDate)
        }


    }

    private fun init() {
        symbol = requireActivity().intent?.extras?.getString(AppConstants.SYMBOL) ?: ""

        snapshotViewModel = if(requireActivity() is SnapshotActivity) (requireActivity() as SnapshotActivity).snapshotViewModel else (requireActivity() as AnnoucementActivity).snapshotViewModel
        sharedViewModel = if(requireActivity() is SnapshotActivity) ((requireActivity() as SnapshotActivity).application as MyApp).viewModel else ((requireActivity() as AnnoucementActivity).application as MyApp).viewModel
//        binding.textDate.text = sdf.format(Date())
        snapshotViewModel.announcementType()
        snapshotViewModel.announcement(symbol,"All",null)

    }


    private fun showDatePicker(textView: TextView) {
        val dialog = CustomDatePickerDialog{date->
            textView.text = date
        }
        /*if(binding.spinnerAnnouncement.selectedItem.toString().equals("Insider",true)){
            dialog.availableDates =
                snapshotViewModel.mutableInsider.value?.data?.map { it.insiderTransactionDate ?: "0L" }
                    ?.toList() ?: emptyList<String>()

        }else if(binding.spinnerAnnouncement.selectedItem.toString().equals("All",true)){
            val list=ArrayList<String>()
            list.addAll(snapshotViewModel.mutableAnnouncementItem.value?.data?.map { it.bmDate ?: "0L" }?.toList()?: emptyList())
            list.addAll(snapshotViewModel.mutableInsider.value?.data?.map { it.insiderTransactionPostDate ?: "0L" }?.toList() ?: emptyList<String>())
            dialog.availableDates = list
        } else{
            dialog.availableDates =
                snapshotViewModel.mutableAnnouncementItem.value?.data?.map { it.bmDate ?: "0L" }
                    ?.toList() ?: emptyList<String>()
        }*/
        dialog.show(requireActivity().supportFragmentManager, "CUSTOM_DATE_PICKER")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snapshotViewModel.apply {
            mutableAnnouncementItem.value=null
            mutableInsider.value=null
            mutableAnnouncementType.value = null
            mutableInsider.value=null
        }
    }


    private fun sharePdf(file: File) {
        if (!file.exists()) return

        val uri: Uri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", file)

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Allow access
        }

        startActivity(Intent.createChooser(shareIntent, "Share PDF via"))
        binding.loader.visibility = View.GONE

    }

    fun openPdf(context: Context, file: File) {
        try {
            val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // Use FileProvider for Android 7.0+
                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            } else {
                Uri.fromFile(file)
            }

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/pdf")
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION // Important for FileProvider
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("PDFOpen", "Error opening PDF: ${e.message}")
        }
    }

}