package com.example.scstrade.views.notification

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
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
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
import java.io.File

class NotificationDetailActivity : BaseActivity() {
    lateinit var sharedViewModel: SharedViewModel
    lateinit var binding: ActivityNotificationDetailBinding
    var announcmentType:String? = null
    var idRef:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationDetailBinding.inflate(LayoutInflater.from(this))
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Your custom back press logic here
                if(sharedViewModel.isHome){
                    finish()
                    val intent= Intent(this@NotificationDetailActivity,MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

                    startActivity(intent)
                }else{
                    finish()
                }
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(binding.detail.listView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(0,  systemBars.top, 0, systemBars.bottom)
            insets
        }
        enableEdgeToEdge()
        sharedViewModel = (application as MyApp).viewModel
        setContentView(binding.root)
        announcmentType=intent.getStringExtra(AppConstants.ANNOUNCEMENT_TYPE_NAME)
        idRef = intent.getIntExtra(AppConstants.ID_REF,-1)
        sharedViewModel.notificationDetals(id = idRef?:-1, type =  announcmentType?:"")
        binding.toolbar.toggleToolbar(false)
        binding.toolbar.binding.market.text = "Notification"
        sharedViewModel.mutableNotificationDetailList.observe(this, Observer {result->
            when(result){
                is Resource.Error -> {
                    binding.loader.visibility = View.GONE
                    Utils.showError(binding.root,result.message?:"An error occurred...")
                }
                is Resource.Loading -> {
                    binding.apply {
                        loader.visibility = View.VISIBLE

                    }
                }
                is Resource.Success -> {
                    binding.apply {
                        loader.visibility = View.GONE

                        val response=result.data
                        if(response!=null) {
                            if (response?.isJsonArray ?: false) {
                                val array = response?.asJsonArray
                                val jsonObject = array!![0].asJsonObject

                                val list = ArrayList<KeyDescValue>()
                                jsonObject.asMap().entries.forEach {
                                    if (!it.value.isJsonNull && !it.value.asString.isNullOrEmpty() && !it.key.equals(
                                            "company_code"
                                        )
                                        && !it.key.equals("company_name") && !it.key.equals("Heading") && !it.key.equals(
                                            "Board_Meeting_Date"
                                        )
                                        && !it.key.equals("ImageLink") && !it.key.equals("PDFLink")
                                    ) {
                                        list.add(KeyDescValue(it.key, it.value.asString, null))
                                    }
                                }
                                detail.listView.adapter =
                                    InformationAdapter(this@NotificationDetailActivity, list)

                                detail.symbol.text = jsonObject.get("company_code").asString
                                detail.companyName.text = jsonObject.get("company_name").asString
                                detail.description.text = jsonObject.get("Heading").asString

                                if (jsonObject.has("Board_Meeting_Date")) {
                                    detail.datetime.text = Utils.convertDateString(
                                        jsonObject.get("Board_Meeting_Date").asString,
                                        "dd-MMM-yyyy"
                                    )
                                    detail.datetime.visibility = View.VISIBLE
                                } else {
                                    detail.datetime.visibility = View.GONE
                                }
                                if (!jsonObject.get("PDFLink").isJsonNull) {
                                    detail.imageViewDownload.visibility = View.VISIBLE
                                    detail.imageViewDownload.setOnClickListener {
                                        binding.loader.visibility = View.VISIBLE
                                        downloadPdf(
                                            this@NotificationDetailActivity,
                                            jsonObject.get("PDFLink").asString
                                        ) { file ->
                                            this@NotificationDetailActivity.runOnUiThread {
                                                if (file != null) {
                                                    openPdf(this@NotificationDetailActivity, file)
                                                    binding.loader.visibility = View.GONE
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    detail.imageViewDownload.visibility = View.GONE
                                }

                                if (!jsonObject.get("ImageLink").isJsonNull) {
                                    detail.imageViewView.visibility = View.VISIBLE
                                    detail.imageViewView.setOnClickListener {
                                        binding.loader.visibility = View.VISIBLE
                                        downloadPdf(
                                            this@NotificationDetailActivity,
                                            jsonObject.get("ImageLink").asString
                                        ) { file ->
                                            this@NotificationDetailActivity.runOnUiThread {
                                                if (file != null) {
                                                    openPdf(this@NotificationDetailActivity, file)
                                                    binding.loader.visibility = View.GONE
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    detail.imageViewView.visibility = View.GONE
                                }
                                if (!jsonObject.get("PDFLink").isJsonNull) {
                                    detail.imageViewView.visibility = View.VISIBLE
                                    detail.imageViewShare.setOnClickListener {
                                        binding.loader.visibility = View.VISIBLE
                                        downloadPdf(
                                            this@NotificationDetailActivity,
                                            jsonObject.get("PDFLink").asString
                                        ) { file ->
                                            this@NotificationDetailActivity.runOnUiThread {
                                                if (file != null) {
                                                    sharePdf(file)
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    detail.imageViewView.visibility = View.GONE
                                }
                            }
                        }else{
                            Utils.showError(binding.root,"An error occurred...")
                        }

                    }


                }
            }

        })

    }


    private fun sharePdf(file: File) {
        if (!file.exists()) return

        val uri: Uri = FileProvider.getUriForFile(this, "${this.packageName}.fileprovider", file)

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

    override fun getResources(): Resources {

        val res = super.getResources()
        val config = Configuration(res.configuration)

        val metrics = res.displayMetrics

        // Calculate screen width and height in inches
        val widthInches = metrics.widthPixels / metrics.xdpi
        val heightInches = metrics.heightPixels / metrics.ydpi
        val diagonalInches = Math.sqrt((widthInches * widthInches + heightInches * heightInches).toDouble())

        // Set fontScale based on diagonal screen size
        if(diagonalInches>3.9 && diagonalInches<4.9){
            config.fontScale = 0.85f  // Small phones
        }else if (diagonalInches>4.9 && diagonalInches<5.4){
            config.fontScale = 0.95f
        }else if (diagonalInches>5.5 && diagonalInches<6.9){
            config.fontScale = 1.0f
        }else{
            config.fontScale = 1.2f
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            config.fontWeightAdjustment = 0

        }
        res.updateConfiguration(config, metrics)
        return res
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            // Override any incoming configuration changes
            overrideConfiguration.densityDpi = resources.displayMetrics.densityDpi
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }
}