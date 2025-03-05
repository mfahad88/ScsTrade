package com.example.scstrade.views.contact

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.asFlow
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityContactBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp

class ContactActivity : AppCompatActivity() {
    lateinit var binding: ActivityContactBinding
    lateinit var sharedViewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityContactBinding.inflate(LayoutInflater.from(this))
        sharedViewModel = (this.application as MyApp).viewModel
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        binding.horizontalList.setContent {
            showContactList()
        }
    }

    @Composable
    private fun showContactList() {
        val result=sharedViewModel.mutableContactUs.asFlow().collectAsState(Resource.Loading())
        when(result.value){
            is Resource.Error -> Utils.showError(binding.root,result.value.message?:"An error occurred")
            is Resource.Loading -> {

            }
            is Resource.Success -> {
                LazyColumn {
                    items(result.value.data?: emptyList()){item->
                        Column {
                            Text(
                                text = item.contactUsName,
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    lineHeight = 24.sp,
                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                    fontWeight = FontWeight(700),
                                    color = colorResource(R.color.md_theme_primary),
                                )
                            )

                            Text(
                                text = "Branch Manger: ${item.contactUsBranchManager}",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                    fontWeight = FontWeight(500),
                                    color = colorResource(R.color.colorDarkerr),
                                )
                            )

                            Text(
                                text = item.contactUsAddress?:"",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                    fontWeight = FontWeight(500),
                                    color = colorResource(R.color.colorDarkerr),
                                )
                            )
                            if(item.contactUsWhatsApp!=null){
                                Button(
                                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.md_theme_primary)),
                                    onClick = {
                                        print("Clicked")
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
                                                fontSize = 18.sp,
                                                lineHeight = 20.sp,
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

                        }
                    }
                }
            }
        }
    }


}