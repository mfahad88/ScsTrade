package com.example.scstrade.views.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityPortfolioBinding

class PortfolioActivity : AppCompatActivity() {
    lateinit var binding: ActivityPortfolioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPortfolioBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.content.setContent {
            populateList(listOf("A","B","C"))
        }
    }
    @Composable
    private fun populateList(list: List<String>) {
        LazyColumn {
            items(list.size){ index->
                Column (modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .background(
                        color = colorResource(id = R.color.md_theme_surfaceBright),
                        shape = RoundedCornerShape(12)
                    )
                ){
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 13.dp)){
                        Column{
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(color = Color(0xFF79776F), shape = CircleShape)
                            ){
                                Image(painter = painterResource(id = R.drawable.ic_baseline_description), contentDescription ="" , modifier = Modifier
                                    .align(
                                        Alignment.Center
                                    )
                                    .size(12.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(9.dp))

                        Column{
                            Text(
                                text = "My Portfolio 1",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    lineHeight = 21.28.sp,
                                    fontFamily = FontFamily(Font(R.font.custom_font)),
                                    fontWeight = FontWeight(600),
                                    color = colorResource(id = R.color.colorDarkerr),
                                )
                            )
                        }
                    }


                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}