package com.example.scstrade.views.news

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DropdownMenu
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.scstrade.R
import com.example.scstrade.databinding.ActivityNewsBinding
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.news.NewsData
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp

class NewsActivity : AppCompatActivity() {
    lateinit var binding:ActivityNewsBinding
    lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityNewsBinding.inflate(LayoutInflater.from(this))
        enableEdgeToEdge()
        setContentView(binding.root)
        sharedViewModel=(this.application as MyApp).viewModel
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
        sharedViewModel.news()
        sharedViewModel.mutableNews.observe(this, Observer {
            when (it){
                is Resource.Error -> {}
                is Resource.Loading ->{}
                is Resource.Success -> {
                    binding.horizontalList.setContent {
                        newsChannels(
                            listOf("SCS", "Recorder", "Tribune", "Profit", "Mettis", "Dawn"),
                            listOf(R.drawable.scs_logo,R.drawable.br_logo,R.drawable.tri_logo,R.drawable.pft_logo,R.drawable.mettis_logo,R.drawable.dwn_logo),
                            it.data?: emptyList()
                        )
                    }
                }
            }
        })

    }
    @Composable
    private fun newsChannels(list: List<String>, images: List<Int>, data: List<NewsData>) {
      var selectedTabIndex by remember { mutableStateOf(0) }
      var isExpanded by remember { mutableStateOf(false) }
        Column(modifier = Modifier.fillMaxSize()) {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                backgroundColor= Color.Transparent,
                contentColor = colorResource(id = R.color.colorDarkerr),
                edgePadding = 15.dp,
                divider = {},
                indicator = {tabPositions ->
                  TabRowDefaults.Indicator(
                      modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                      color = colorResource(id = R.color.md_theme_primary),
                      height = 2.dp
                  )
                }
            ) {

                list.forEachIndexed { index, s ->
                    Tab(selected = selectedTabIndex==index, onClick = {
                        selectedTabIndex=index
                    },
                        text = {

                            Column (verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally){
                                Image(painter = painterResource(id = images[index]), contentDescription = list[index], modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = list[index],
                                   fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.inter_28pt_medium_500)),
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.colorDarkerr)
                                )

                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if(selectedTabIndex>0) {
                DropdownMenu(expanded = isExpanded, onDismissRequest = {
                    isExpanded = false
                }) {

                }
            }
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {

                LazyColumn {
                    items(data.size) { index ->
                        Column {
                            Row {
                                Box(
                                    modifier = Modifier.weight(3f),
                                    content = {
                                        Text(
                                            text = data[index].newsDesc,
                                            fontSize = 16.sp,
                                            maxLines = 2,
                                            fontFamily = FontFamily(Font(R.font.inter_28pt_semibold_600)),
                                            fontWeight = FontWeight(600),
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                )
                                Box(
                                    modifier = Modifier.weight(1f),
                                    content = {
                                        Image(
                                            painter = painterResource(id = R.drawable.news_empty),
                                            contentDescription = "Dawn",
                                            contentScale = ContentScale.FillBounds
                                        )
                                    }
                                )
                            }
                            Row {
                                Image(
                                    painter = painterResource(id = R.drawable.clock),
                                    contentDescription = "Clock",
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = Utils.convertDate(data[index].newsDate),
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily(Font(R.font.custom_font)),
                                        fontWeight = FontWeight(500),
                                        color = Color(0xFF79776F)
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(15.dp))
                        }

                    }
                }
            }
        }
    }
}