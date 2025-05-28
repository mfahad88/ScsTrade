package com.example.scstrade.views.news

import RssItem
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
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
import androidx.lifecycle.asFlow
import coil.compose.rememberAsyncImagePainter
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentNewsBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.news.NewsData
import com.example.scstrade.model.response.news.brecoder.Item
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.landing.LandingFragment


class NewsFragment : Fragment() {
    lateinit var binding: FragmentNewsBinding
    lateinit var sharedViewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentNewsBinding.inflate(inflater,container,false)
        sharedViewModel=(requireActivity().application as MyApp).viewModel
        sharedViewModel.news()
        (parentFragment as LandingFragment).binding.toolbar.binding.apply {
            toolbarWithLogo.visibility = View.GONE
            toolbarWithBack.visibility = View.VISIBLE
            backButton.visibility = View.GONE
            titleItem.text = "News"
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.horizontalList.setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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

        Column (
            modifier = Modifier.fillMaxSize()
        ){
            TabRow(
                selectedTabIndex = selectedTabIndex,
                backgroundColor= Color.Transparent,
                contentColor = colorResource(id = R.color.colorDarkerr),
                modifier = Modifier.fillMaxWidth(),
//                edgePadding = 15.dp,
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
                    Tab(selected = selectedTabIndex==index,
                        modifier = Modifier.weight(1f),
                        onClick = {
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
                                    color = colorResource(id = R.color.black),
                                    maxLines = 1,
                                    overflow = TextOverflow.Clip,


                                    )

                            }
                        }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> {
                    val data= sharedViewModel.mutableNews.asFlow().collectAsState(initial = Resource.Loading())
                    when(data.value){
                        is Resource.Error -> {
                            binding.loader.visibility= View.GONE
                            Utils.showError(binding.root,data.value.message?:"An error occurred")
                        }
                        is Resource.Loading -> {
                            binding.loader.visibility= View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding.loader.visibility= View.GONE
                            newList(data = data.value.data?: emptyList()){

                                val intent= Intent(requireContext(),NewsDetailActivity::class.java)
                                intent.putExtra(AppConstants.NEWS_TYPE,AppConstants.SCS)
                                intent.putExtra(AppConstants.TITLE,it.newsDesc)
                                startActivity(intent)
                            }

                        }
                    }
                }
                1 -> {
                    val data = sharedViewModel.mutableBrecoder.asFlow().collectAsState(initial = Resource.Loading())
                    when(data.value){
                        is Resource.Error -> {
                            binding.loader.visibility= View.GONE
                            Utils.showError(binding.root,data.value.message?:"An error occurred")
                        }
                        is Resource.Loading -> binding.loader.visibility= View.VISIBLE
                        is Resource.Success -> {
                            binding.loader.visibility= View.GONE
                            newsListTribune(data=data.value.data?.channel?.items?: emptyList()){
                                val intent= Intent(requireContext(),NewsDetailActivity::class.java)
                                intent.putExtra(AppConstants.NEWS_TYPE,AppConstants.BRECODER)
                                intent.putExtra(AppConstants.TITLE,(it as Item).title)
                                startActivity(intent)
                            }
                        }
                    }
                }
                2 -> {
                    val data = sharedViewModel.mutableTribune.asFlow().collectAsState(initial = Resource.Loading())
                    when(data.value){
                        is Resource.Error -> {
                            binding.loader.visibility= View.GONE
                            Utils.showError(binding.root,data.value.message?:"An error occurred")
                        }
                        is Resource.Loading -> binding.loader.visibility= View.VISIBLE
                        is Resource.Success -> {
                            binding.loader.visibility= View.GONE
                            newsListTribune(data=data.value.data?.channel?.items?: emptyList()){

                                val intent= Intent(requireContext(),NewsDetailActivity::class.java)
                                intent.putExtra(AppConstants.NEWS_TYPE,AppConstants.TRIBUNE)
                                intent.putExtra(AppConstants.TITLE,(it as RssItem).title)
                                startActivity(intent)
                            }
                        }
                    }
                }
                3 -> {
                    val data = sharedViewModel.mutableProfit.asFlow().collectAsState(initial = Resource.Loading())
                    when(data.value){
                        is Resource.Error -> {
                            binding.loader.visibility= View.GONE
                            Utils.showError(binding.root,data.value.message?:"An error occurred")
                        }
                        is Resource.Loading -> binding.loader.visibility= View.VISIBLE
                        is Resource.Success -> {
                            binding.loader.visibility= View.GONE
                            newsListTribune(data=data.value.data?.channel?.items?: emptyList()){
                                val intent= Intent(requireContext(),NewsDetailActivity::class.java)
                                intent.putExtra(AppConstants.NEWS_TYPE,AppConstants.PROFIT)
                                intent.putExtra(AppConstants.TITLE,(it as com.example.scstrade.model.response.news.profit.RssItem).title)
                                startActivity(intent)
                            }
                        }
                    }
                }
                4 -> {
                    val data = sharedViewModel.mutableMettis.asFlow().collectAsState(initial = Resource.Loading())
                    when(data.value){
                        is Resource.Error -> {
                            binding.loader.visibility= View.GONE
                            Utils.showError(binding.root,data.value.message?:"An error occurred")
                        }
                        is Resource.Loading -> binding.loader.visibility= View.VISIBLE
                        is Resource.Success -> {
                            binding.loader.visibility= View.GONE
                            newsListTribune(data=data.value.data?.channel?.items?: emptyList()){
                                val intent= Intent(requireContext(),NewsDetailActivity::class.java)
                                intent.putExtra(AppConstants.NEWS_TYPE,AppConstants.METTIS)
                                intent.putExtra(AppConstants.TITLE,(it as com.example.scstrade.model.response.news.mettis.RssItem).title)
                                startActivity(intent)
                            }
                        }
                    }
                }
                5 -> {
                    val data = sharedViewModel.mutableDawn.asFlow().collectAsState(initial = Resource.Loading())
                    when(data.value){
                        is Resource.Error -> {
                            binding.loader.visibility= View.GONE
                            Utils.showError(binding.root,data.value.message?:"An error occurred")
                        }
                        is Resource.Loading -> binding.loader.visibility= View.VISIBLE
                        is Resource.Success -> {
                            binding.loader.visibility= View.GONE
                            newsListTribune(data=data.value.data?.channel?.items?: emptyList()){
                                val intent= Intent(requireContext(),NewsDetailActivity::class.java)
                                intent.putExtra(AppConstants.NEWS_TYPE,AppConstants.DAWN)
                                intent.putExtra(AppConstants.TITLE,(it as com.example.scstrade.model.response.news.dawn.RssItem).title)
                                startActivity(intent)
                            }
                        }
                    }

                }
            }
        }
    }

    @Composable
    private fun newsListTribune(data: List<Any>,onItemClick:(Any)->Unit) {
        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
            LazyColumn {
                items(data.size) { index ->
                    Row (modifier = Modifier.clickable {
                        onItemClick(data[index])
                    }){

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
                                        minLines = 3,
                                        maxLines = 3,
                                        fontFamily = FontFamily(Font(R.font.inter_28pt_semibold_600)),
                                        fontWeight = FontWeight(600),
                                        color = colorResource(id = R.color.black),
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
                                            text =  Utils.convertDateBrFormat(if(data[index] is RssItem) (data[index] as RssItem).pubDate?.trim() ?: ""
                                            else if(data[index] is com.example.scstrade.model.response.news.brecoder.Item ) (data[index] as com.example.scstrade.model.response.news.brecoder.Item).pubDate?.trim()?:""
                                            else if (data[index] is com.example.scstrade.model.response.news.profit.RssItem ) (data[index] as com.example.scstrade.model.response.news.profit.RssItem).pubDate.trim()
                                            else if (data[index] is com.example.scstrade.model.response.news.mettis.RssItem ) (data[index] as com.example.scstrade.model.response.news.mettis.RssItem).pubDate.trim()
                                            else if (data[index] is com.example.scstrade.model.response.news.dawn.RssItem ) (data[index] as com.example.scstrade.model.response.news.dawn.RssItem).pubDate.trim()
                                            else "" ),

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
                                    else painterResource(id = R.drawable.news_empty_old) ,
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
    fun newList(data: List<NewsData>,onItemClick:(NewsData)->Unit){

        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {

            LazyColumn {
                items(data.size) { index ->
                    Row(modifier = Modifier.clickable {
                        onItemClick(data[index])
                    }){
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
                                        color = colorResource(id = R.color.black),
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
                                    painter = painterResource(id = R.drawable.news_empty_old),
                                    contentDescription = "Dawn",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.FillWidth
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