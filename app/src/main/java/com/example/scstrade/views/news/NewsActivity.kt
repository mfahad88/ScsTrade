package com.example.scstrade.views.news

import RssItem
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.lifecycle.asFlow
import coil.compose.rememberAsyncImagePainter
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



        binding.horizontalList.setContent {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                newsChannels(
                    listOf("SCS", "Recorder", "Tribune", "Profit", "Mettis", "Dawn"),
                    listOf(
                        R.drawable.scs_logo,
                        R.drawable.br_logo,
                        R.drawable.tri_logo,
                        R.drawable.pft_logo,
                        R.drawable.mettis_logo,
                        R.drawable.dwn_logo
                    )
                )


              
            }
        }
        
        

    }
    @Composable
    private fun newsChannels(list: List<String>, images: List<Int>) {
      var selectedTabIndex by remember { mutableStateOf(0) }

        Column {
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
                        when (index){
                            0-> sharedViewModel.news()
                            1-> sharedViewModel.brecoderNews()
                            2-> sharedViewModel.tribuneNews()
                            3-> sharedViewModel.profitNews()
                            4-> sharedViewModel.mettisNews()
                            5-> sharedViewModel.dawnNews()
                        }
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

            when (selectedTabIndex) {
                0 -> {
                    val data= sharedViewModel.mutableNews.asFlow().collectAsState(initial = Resource.Loading())
                    newList(data = data.value.data?: emptyList())
                }
                1 -> {
                    val data = sharedViewModel.mutableBrecoder.asFlow().collectAsState(initial = Resource.Loading())
                    newsListTribune(data=data.value.data?.channel?.item?: emptyList())
                }
                2 -> {
                    val data = sharedViewModel.mutableTribune.asFlow().collectAsState(initial = Resource.Loading())
                    newsListTribune(data=data.value.data?.channel?.items?: emptyList())
                }
                3 -> {
                    val data = sharedViewModel.mutableProfit.asFlow().collectAsState(initial = Resource.Loading())
                    newsListTribune(data=data.value.data?.channel?.items?: emptyList())
                }
                4 -> {
                    val data = sharedViewModel.mutableMettis.asFlow().collectAsState(initial = Resource.Loading())
                    newsListTribune(data=data.value.data?.channel?.items?: emptyList())
                }
                5 -> {
                    val data = sharedViewModel.mutableDawn.asFlow().collectAsState(initial = Resource.Loading())
                    newsListTribune(data=data.value.data?.channel?.items?: emptyList())
                }
            }
        }
    }

    @Composable
    private fun newsListTribune(data: List<Any>) {
        Box(modifier = Modifier.padding(horizontal = 20.dp)) {
            LazyColumn {
                items(data.size) { index ->
                    Row {
                        Box(
                            modifier = Modifier
                                .weight(2f),
                            content = {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.Start,
                                ) {
                                    Text(
                                        text =
                                        if(data[index] is RssItem) (data[index] as RssItem).title?.trim() ?: "No Description"
                                        else if(data[index] is com.example.scstrade.model.response.news.brecoder.Item ) (data[index] as com.example.scstrade.model.response.news.brecoder.Item).title?.trim()?:""
                                        else if (data[index] is com.example.scstrade.model.response.news.profit.RssItem ) (data[index] as com.example.scstrade.model.response.news.profit.RssItem).title.trim()
                                        else if (data[index] is com.example.scstrade.model.response.news.mettis.RssItem ) (data[index] as com.example.scstrade.model.response.news.mettis.RssItem).title.trim()
                                        else if (data[index] is com.example.scstrade.model.response.news.dawn.RssItem ) (data[index] as com.example.scstrade.model.response.news.dawn.RssItem).title.trim()
                                        else "No Description" ,
                                        fontSize = 16.sp,
                                        maxLines = 3,
                                        fontFamily = FontFamily(Font(R.font.inter_28pt_semibold_600)),
                                        fontWeight = FontWeight(600),
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                    Row(
                                        modifier = Modifier.padding(top = 2.dp),
                                    ) {

                                        Image(
                                            painter = painterResource(id = R.drawable.clock),
                                            contentDescription = "Clock",
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Text(
                                            text =  if(data[index] is RssItem) (data[index] as RssItem).pubDate?.trim() ?: ""
                                            else if(data[index] is com.example.scstrade.model.response.news.brecoder.Item ) (data[index] as com.example.scstrade.model.response.news.brecoder.Item).pubDate?.trim()?:""
                                            else if (data[index] is com.example.scstrade.model.response.news.profit.RssItem ) (data[index] as com.example.scstrade.model.response.news.profit.RssItem).pubDate.trim()
                                            else if (data[index] is com.example.scstrade.model.response.news.mettis.RssItem ) (data[index] as com.example.scstrade.model.response.news.mettis.RssItem).pubDate.trim()
                                            else if (data[index] is com.example.scstrade.model.response.news.dawn.RssItem ) (data[index] as com.example.scstrade.model.response.news.dawn.RssItem).pubDate.trim()
                                            else "" ,

                                            style = TextStyle(
                                                fontSize = 12.sp,
                                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                                fontWeight = FontWeight(500),
                                                color = Color(0xFF79776F)
                                            )
                                        )
                                    }
                                }
                            }

                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(
                            modifier = Modifier
                                .weight(1f),
                            content = {
                                Image(
                                   /* painter = if (data[index].image != null) rememberAsyncImagePainter(
                                        data[index].image?.img?.src
                                    ) else painterResource(
                                        id = R.drawable.news_empty
                                    ),*/
                                    painter =  if(data[index] is RssItem) rememberAsyncImagePainter((data[index] as RssItem).image?.img?.src)
                                    else if(data[index] is com.example.scstrade.model.response.news.brecoder.Item) rememberAsyncImagePainter((data[index] as com.example.scstrade.model.response.news.brecoder.Item).mediaContent?.url)
                                    else if(data[index] is com.example.scstrade.model.response.news.profit.RssItem) rememberAsyncImagePainter(extractImage((data[index] as com.example.scstrade.model.response.news.profit.RssItem).description))
                                    else if(data[index] is com.example.scstrade.model.response.news.mettis.RssItem) rememberAsyncImagePainter(extractImage((data[index] as com.example.scstrade.model.response.news.mettis.RssItem).description))
                                    else if (data[index] is com.example.scstrade.model.response.news.dawn.RssItem ) rememberAsyncImagePainter((data[index] as com.example.scstrade.model.response.news.dawn.RssItem).mediaContent?.url)
                                    else painterResource(id = R.drawable.news_empty) ,
                                    contentDescription = "Dawn",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillParentMaxHeight(0.1f)
                                        .clip(
                                            RoundedCornerShape(5.dp)
                                        ),
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                }
            }
        }
    }

    @Composable
    fun newList(data: List<NewsData>){

        Box(modifier = Modifier.padding(horizontal = 20.dp)) {

            LazyColumn {
                items(data.size) { index ->
                    Row{
                        Box(
                            modifier = Modifier
                                .weight(3f),
                            content = {
                                Column {
                                    Text(
                                        text = data[index].newsDesc,
                                        fontSize = 16.sp,
                                        maxLines = 3,
                                        fontFamily = FontFamily(Font(R.font.inter_28pt_semibold_600)),
                                        fontWeight = FontWeight(600),
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Row (
                                        modifier = Modifier.padding(top = 2.dp),
                                    ){

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
                                }
                            }

                        )

                        Box(
                            modifier = Modifier
                                .weight(1f),
                            content = {
                                Image(
                                    painter = painterResource(id = R.drawable.news_empty),
                                    contentDescription = "Dawn",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                }
            }
        }
    }

    fun extractImage(input: String): String? {
        val regex = """src=["'](https?://[^"']+)["']""".toRegex()
        return regex.find(input)?.groupValues?.get(1) // Returns first matched group
    }
}