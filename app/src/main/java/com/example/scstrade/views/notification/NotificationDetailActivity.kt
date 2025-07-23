package com.example.scstrade.views.notification

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityNotificationDetailBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.helper.downloadPdf
import com.example.scstrade.model.Resource
import com.example.scstrade.model.data.KeyDescValue
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.main.MainActivity
import com.example.scstrade.views.widgets.ZoomImageView
import java.io.File

class NotificationDetailActivity : BaseActivity() {

    lateinit var sharedViewModel: SharedViewModel
    lateinit var binding: ActivityNotificationDetailBinding
    var announcmentType: String? = null
    var idRef: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationDetailBinding.inflate(LayoutInflater.from(this))
        Utils.setEdgeToEdgeWithWhiteIcons(this)

        sharedViewModel = (application as MyApp).viewModel

//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (sharedViewModel.isHome) {
                    finish()
                    val intent = Intent(this@NotificationDetailActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                } else {
                    finish()
                }
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(binding.detail.listView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(0, systemBars.top, 0, systemBars.bottom)
            insets
        }

        enableEdgeToEdge()
        setContentView(binding.root)

        announcmentType = intent.getStringExtra(AppConstants.ANNOUNCEMENT_TYPE_NAME)
        idRef = intent.getIntExtra(AppConstants.ID_REF, -1)

        sharedViewModel.notificationDetals(id = idRef ?: -1, type = announcmentType ?: "")
        binding.toolbar.binding.market.text = "Notification"

        sharedViewModel.mutableNotificationDetailList.observe(this, Observer { result ->
            when (result) {
                is Resource.Error -> {
                    binding.loader.visibility = View.GONE
                    Utils.showError(binding.root, result.message ?: "An error occurred...")
                }

                is Resource.Loading -> {
                    binding.loader.visibility = View.VISIBLE
                }

                is Resource.Success -> {

                    val response = result.data


                    if (response != null && response.isJsonArray) {
                        val array = response.asJsonArray

                        if (array.size() > 0) {
                            val jsonObject = array[0].asJsonObject
                            val list = ArrayList<KeyDescValue>()

                            jsonObject.asMap().entries.forEach {
                                if (!it.value.isJsonNull && !it.value.asString.isNullOrEmpty()
                                    && !listOf(
                                        "company_code", "AnnouncementType",
                                        "company_name", "Heading",
                                        "bm_ann_date", "ImageLink", "PDFLink"
                                    ).contains(it.key)
                                ) {
                                    if (it.key.contains("date", ignoreCase = true) && it.value.asString.contains("-")) return@forEach
                                    list.add(KeyDescValue(it.key, it.value.asString, null))
                                }
                            }

                            binding.detail.listView.adapter = InformationAdapter(this, list)

                            binding.detail.symbol.text = jsonObject.get("company_code").asString + " - "
                            binding.detail.companyName.text = jsonObject.get("company_name").asString
                            binding.detail.description.text = jsonObject.get("Heading").asString
                            binding.detail.announcementType.text =  jsonObject.get("AnnouncementType").asString
                            if(!jsonObject.get("AnnouncementType").asString.equals(jsonObject.get("Heading").asString)){
                                if(jsonObject.get("AnnouncementType").asString.equals("board meetings") && jsonObject.get("Heading").asString.equals("board meeting")){
                                    binding.detail.announcementType.visibility = View.GONE
                                }else{

                                    binding.detail.announcementType.visibility = View.VISIBLE
                                }
                            }

                            if (jsonObject.has("bm_ann_date")) {
                                binding.detail.datetime.text = Utils.convertDateString(
                                    jsonObject.get("bm_ann_date").asString, "dd-MMM-yyyy"
                                )
                                binding.detail.datetime.visibility = View.VISIBLE
                            } else {
                                binding.detail.datetime.visibility = View.GONE
                            }

                            if (!jsonObject.get("PDFLink").asString.isNullOrBlank()) {
                                binding.detail.imageViewDownload.visibility = View.VISIBLE
                                binding.detail.imageViewDownload.setOnClickListener {
                                    binding.loader.visibility = View.VISIBLE
                                    downloadPdf(this, jsonObject.get("PDFLink").asString) { file ->
                                        runOnUiThread {
                                            if (file != null) {
                                                openPdf(this, file)
                                            }
                                            binding.loader.visibility = View.GONE
                                        }
                                    }
                                }
                            } else {
                                binding.detail.imageViewDownload.visibility = View.GONE
                            }

                            if (!jsonObject.get("ImageLink").asString.isNullOrBlank()) {
                                binding.detail.imageViewView.visibility = View.VISIBLE
                                binding.detail.imageViewView.setOnClickListener {
                                    binding.loader.visibility = View.VISIBLE
                                    val dialog = Dialog(this)
                                    dialog.setContentView(R.layout.dialog_image)
                                    Glide.with(this)
                                        .load(jsonObject.get("ImageLink").asString)
                                        .into(dialog.findViewById<ZoomImageView>(R.id.imageView))

                                    dialog.setCancelable(false)
                                    dialog.setCanceledOnTouchOutside(false)
                                    dialog.findViewById<ImageView>(R.id.btnClose).setOnClickListener {
                                        dialog.dismiss()
                                    }
                                    dialog.show()
                                    binding.loader.visibility = View.GONE
                                }
                            } else {
                                binding.detail.imageViewView.visibility = View.GONE
                            }

                            if (!jsonObject.get("PDFLink").asString.isNullOrBlank()) {
                                binding.detail.imageViewShare.visibility = View.VISIBLE
                                binding.detail.imageViewShare.setOnClickListener {
                                    binding.loader.visibility = View.VISIBLE
                                    downloadPdf(this, jsonObject.get("PDFLink").asString) { file ->
                                        runOnUiThread {
                                            if (file != null) {
                                                sharePdf(file)
                                            }
                                            binding.loader.visibility = View.GONE
                                        }
                                    }
                                }
                            } else {
                                binding.detail.imageViewShare.visibility = View.GONE
                            }
                            findViewById<View>(R.id.detail).visibility = View.VISIBLE

                        } else {
                            Utils.showError(binding.root, "No notification details available.")
                        }
                    } else {
                        Utils.showError(binding.root, "Invalid response received.")
                    }

                    binding.loader.visibility = View.GONE
                }
            }
        })
    }

    private fun sharePdf(file: File) {
        if (!file.exists()) return
        val uri: Uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(shareIntent, "Share PDF via"))
    }

    fun openPdf(context: Context, file: File) {
        try {
            val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            } else {
                Uri.fromFile(file)
            }

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("PDFOpen", "Error opening PDF: ${e.message}")
        }
    }

    override fun getResources(): Resources {
        val res = super.getResources()
        val config = Configuration(res.configuration)
        val metrics = res.displayMetrics

        val widthInches = metrics.widthPixels / metrics.xdpi
        val heightInches = metrics.heightPixels / metrics.ydpi
        val diagonalInches = Math.sqrt((widthInches * widthInches + heightInches * heightInches).toDouble())

        config.fontScale = when {
            diagonalInches > 3.9 && diagonalInches < 4.9 -> 0.85f
            diagonalInches > 4.9 && diagonalInches < 5.4 -> 0.95f
            diagonalInches > 5.5 && diagonalInches < 6.9 -> 1.0f
            else -> 1.2f
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            config.fontWeightAdjustment = 0
        }

        res.updateConfiguration(config, metrics)
        return res
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            overrideConfiguration.densityDpi = resources.displayMetrics.densityDpi
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }
}
