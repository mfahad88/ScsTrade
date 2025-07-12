package com.example.scstrade.views.contact
import androidx.compose.ui.res.dimensionResource

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
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Colors
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.asFlow
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityContactBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.BaseActivity
import com.example.scstrade.views.MyApp

class ContactActivity : BaseActivity() {
    lateinit var binding: ActivityContactBinding
    lateinit var sharedViewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityContactBinding.inflate(LayoutInflater.from(this))
        sharedViewModel = (this.application as MyApp).viewModel
        enableEdgeToEdge()
        Utils.setEdgeToEdgeWithWhiteIcons(this)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.horizontalList) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Utils.setSystemBarIcons(this,false)

        sharedViewModel.contactUs()
        binding.horizontalList.setContent {
            showContactList()
        }
    }

    @Composable
    private fun showContactList() {
        val context= LocalContext.current
        val result=sharedViewModel.mutableContactUs.asFlow().collectAsState(Resource.Loading())
        when(result.value){
            is Resource.Error -> {
                Utils.showError(binding.root, result.value.message ?: "An error occurred")
                Log.e("Error: ",result.value.message ?: "An error occurred")
                binding.loader.visibility = View.GONE
            }
            is Resource.Loading -> {
                binding.loader.visibility = View.VISIBLE
            }
            is Resource.Success -> {
                LazyColumn {
                    items(result.value.data?: emptyList()){item->
                        Column {
                            if(item.contactUsName!=null){
                                Text(
                                    text = item.contactUsName,
                                    style = TextStyle(
                                        fontSize = dimensionResource(R.dimen.sp_18).value.sp,
                                        lineHeight = dimensionResource(R.dimen.sp_24).value.sp,
                                        fontFamily = FontFamily(Font(R.font.custom_font)),
                                        fontWeight = FontWeight(700),
                                        color = colorResource(R.color.md_theme_primary),
                                    )
                                )
                            }

                            if(item.contactUsBranchManager!=null) {
                                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10).value.dp))
                                Text(
                                    text = "Branch Manger: ${item.contactUsBranchManager}",
                                    style = TextStyle(
                                        fontSize = dimensionResource(R.dimen.sp_16).value.sp,
                                        lineHeight = dimensionResource(R.dimen.sp_24).value.sp,
                                        fontFamily = FontFamily(Font(R.font.custom_font)),
                                        fontWeight = FontWeight(500),
                                        color = colorResource(R.color.colorDarkerr),
                                    )
                                )
                            }
                            if(item.contactUsAddress!=null) {
                                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10).value.dp))
                                Text(
                                    text = item.contactUsAddress ?: "",
                                    style = TextStyle(
                                        fontSize = dimensionResource(R.dimen.sp_16).value.sp,
                                        lineHeight = dimensionResource(R.dimen.sp_24).value.sp,
                                        fontFamily = FontFamily(Font(R.font.custom_font)),
                                        fontWeight = FontWeight(500),
                                        color = colorResource(R.color.colorDarkerr),
                                    )
                                )
                            }
                            if(item.contactUsPhone!=null){
                                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10).value.dp))
                                Button(
                                    shape = CircleShape,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.md_theme_primary)),
                                    onClick = {
                                        val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                                            data = Uri.parse("tel:${item.contactUsPhone}")
                                        }
                                        startActivity(dialIntent)
                                    },
                                    content = {
                                        Image(
                                            painter = painterResource(id = R.drawable.ph_phone_call),
                                            contentDescription = "image description",
                                            contentScale = ContentScale.None
                                        )
                                        Text(
                                            text = "Call on ${item.contactUsPhone}",
                                            style = TextStyle(
                                                fontSize = dimensionResource(R.dimen.sp_18).value.sp,
                                                lineHeight = dimensionResource(R.dimen.sp_20).value.sp,
                                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                                fontWeight = FontWeight(500),
                                                color = Color(0xFFFFFFFF),
                                                textAlign = TextAlign.Center,
                                                letterSpacing = 0.1.sp,
                                            )
                                        )
                                    }
                                )
                            }

                            if(item.contactUsWhatsApp!=null){
                                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10).value.dp))

                                Button(
                                    shape = CircleShape,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF455A64)),
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            data = Uri.parse("https://wa.me/${item.contactUsWhatsApp}") // Open chat with this number
                                        }
                                        try {
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    content = {
                                        Image(
                                            painter = painterResource(id = R.drawable.whtasapp_logo),
                                            contentDescription = "image description",
                                            contentScale = ContentScale.None
                                        )
                                        Text(
                                            text = "Chat on Whatsapp",
                                            style = TextStyle(
                                                fontSize = dimensionResource(R.dimen.sp_18).value.sp,
                                                lineHeight = dimensionResource(R.dimen.sp_20).value.sp,
                                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                                fontWeight = FontWeight(500),
                                                color = Color(0xFFFFFFFF),
                                                textAlign = TextAlign.Center,
                                                letterSpacing = 0.1.sp,
                                            )
                                        )
                                    }
                                )
                            }

                            if(item.contactUsEmail!=null){
                                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_10).value.dp))
                                OutlinedButton(
                                    shape = CircleShape,
                                    modifier = Modifier.fillMaxWidth(),
                                    border = BorderStroke(dimensionResource(R.dimen.dp_1).value.dp, color = Color(0xFF79776F) ),
//                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF455A64)),
                                    onClick = {
                                        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                            data = Uri.parse("mailto:${item.contactUsEmail}")
                                        }
                                        try {
                                            context.startActivity(emailIntent)
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    content = {
                                        Image(
                                            painter = painterResource(id = R.drawable.baseline_email_24),
                                            contentDescription = "image description",
                                            contentScale = ContentScale.None
                                        )
                                        Text(
                                            text = "Email on ${item.contactUsEmail}",
                                            style = TextStyle(
                                                fontSize = dimensionResource(R.dimen.sp_18).value.sp,
                                                lineHeight = dimensionResource(R.dimen.sp_20).value.sp,
                                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                                fontWeight = FontWeight(500),
                                                color = colorResource(id = R.color.md_theme_primary),
                                                textAlign = TextAlign.Center,
                                                letterSpacing = 0.1.sp,
                                            )
                                        )
                                    }
                                )
                            }
                            
                            if(item.contactUsMap!=null){
                                // Declare a string that contains a url
                                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_20).value.dp))
                                WebViewItem(item.contactUsMap, LocalView.current.context)
                            }
                            
                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_20).value.dp))
                        }
                    }
                }
                binding.loader.visibility = View.GONE
                binding.horizontalList.visibility = View.VISIBLE
            }
        }
    }



    @Composable
    fun WebViewItem(url: String, context: Context) {
        // Remember WebView to avoid recreation on scroll
        val webView = remember {
            WebView(context).apply {
                settings.javaScriptEnabled = true
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                loadUrl(url)
            }
        }

        AndroidView(

            factory = { webView },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.dp_300).value.dp)
        )
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