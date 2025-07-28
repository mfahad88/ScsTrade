package com.example.scstrade.views.news
import androidx.compose.ui.res.dimensionResource

import RssItem
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentNewsBinding
import com.example.scstrade.helper.AppConstants
import com.example.scstrade.helper.Utils
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.news.NewsData
import com.example.scstrade.model.response.news.brecoder.Item
import com.example.scstrade.model.response.news.mettis.NewsItem
import com.example.scstrade.viewmodels.SharedViewModel
import com.example.scstrade.views.MyApp
import com.example.scstrade.views.landing.LandingFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
        lifecycleScope.launch {
            delay(1500)
            sharedViewModel.news()
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.horizontalList) { view, windowInsets ->
            (parentFragment as LandingFragment).binding.bottomNavigationView.post {
                val navHeight=(parentFragment as LandingFragment).binding.bottomNavigationView.height
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
                view.setPadding(0,insets.top,0,navHeight)
            }

            windowInsets
        }
        (parentFragment as LandingFragment).binding.toolbar.binding.apply {

//            subTitle.text = "News"
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.horizontalList.setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = dimensionResource(R.dimen.dp_20).value.dp)
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
        val scope = rememberCoroutineScope()
        val scrollState = rememberScrollState()
        Column (
            modifier = Modifier.fillMaxSize()
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.dp_10).value.dp)
                    // TabRow height
            ) {
                Row(
                    modifier = Modifier
                        .horizontalScroll(scrollState)
                        .padding(horizontal = dimensionResource(R.dimen.dp_20).value.dp)
                )
                /*ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    backgroundColor = Color.Transparent,
                    contentColor = colorResource(id = R.color.colorDarkerr),
                    modifier = Modifier.wrapContentSize(),
                    //                edgePadding = -dimensionResource(R.dimen.dp_2).value.dp,
                    divider = {},
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = colorResource(id = R.color.md_theme_primary),
                            height = dimensionResource(R.dimen.dp_2).value.dp
                        )
                    }
                )*/ {

                    list.forEachIndexed { index, s ->
                        Tab(selected = selectedTabIndex == index,
//                            modifier = Modifier.weight(1f),

                            onClick = {
                                selectedTabIndex = index
                                when (index) {
                                    0 -> sharedViewModel.news()
                                    1 -> sharedViewModel.brecoderNews()
                                    2 -> sharedViewModel.tribuneNews()
                                    3 -> sharedViewModel.profitNews()
                                    4 -> sharedViewModel.mettisNews()
                                    5 -> sharedViewModel.dawnNews()
                                }
                            },
                            text = {

                                Column(
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = images[index]),
                                        contentDescription = list[index],
                                        modifier = Modifier.size(dimensionResource(R.dimen.dp_38).value.dp)
                                    )
                                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_6).value.dp))
                                    Text(
                                        text = list[index],
                                        fontSize = dimensionResource(R.dimen.sp_12).value.sp,
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



                IconButton(
                    onClick = {
                        scope.launch {
                            val target = (scrollState.value - 200).coerceAtLeast(0)
                            scrollState.animateScrollTo(target)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
//                        .background(Color.White.copy(alpha = 0.7f), CircleShape)
                ) {
                    Icon(painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24), tint = colorResource(R.color.black), contentDescription = "Scroll Left", modifier = Modifier.size(dimensionResource(R.dimen.dp_30).value.dp))
                }

                // Right scroll button
                IconButton(
                    onClick = {
                        scope.launch {
                            val target = (scrollState.value + 200)
                            scrollState.animateScrollTo(target)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
//                        .background(Color.White.copy(alpha = 0.7f), CircleShape)
                ) {
                    Icon(painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24), tint = colorResource(R.color.black), contentDescription = "Scroll Right", modifier = Modifier.size(dimensionResource(R.dimen.dp_30).value.dp).rotate(180f))
                }
                /*Box(
                    Modifier
                        .width(fadeWidth)
                        .fillMaxHeight()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, Color.White)
                            )
                        )
                        .align(Alignment.CenterEnd)
                )*/
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

                            newList(data = data.value.data?: emptyList()){
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.newsLink))
                                // Optionally add flags, chooser, etc.
                                context?.startActivity(intent)
                                /* val intent= Intent(requireContext(),NewsDetailActivity::class.java)
                                 intent.putExtra(AppConstants.NEWS_TYPE,AppConstants.SCS)
                                 intent.putExtra(AppConstants.TITLE,it.newsDesc)
                                 startActivity(intent)*/
                            }
                            binding.loader.visibility= View.GONE
                            binding.horizontalList.visibility = View.VISIBLE
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

                            newsListMettis(data=data.value.data?: emptyList()){
                                val intent= Intent(requireContext(),NewsDetailActivity::class.java)
                                intent.putExtra(AppConstants.NEWS_TYPE,AppConstants.METTIS)
                                intent.putExtra(AppConstants.TITLE,it.title)
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
        Box(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.dp_20).value.dp, vertical = dimensionResource(R.dimen.dp_20).value.dp)) {
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
                                        fontSize = dimensionResource(R.dimen.sp_16).value.sp,
                                        minLines = 3,
                                        maxLines = 3,
                                        fontFamily = FontFamily(Font(R.font.inter_28pt_semibold_600)),
                                        fontWeight = FontWeight(600),
                                        color = colorResource(id = R.color.black),
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                    Row(
                                        modifier = Modifier.padding(top = dimensionResource(R.dimen.dp_2).value.dp),
                                    ) {

                                        Image(
                                            painter = painterResource(id = R.drawable.clock),
                                            contentDescription = "Clock",
                                            modifier = Modifier.size(dimensionResource(R.dimen.dp_15).value.dp)
                                        )
                                        Text(
                                            text =  Utils.convertDateBrFormat(if(data[index] is RssItem) (data[index] as RssItem).pubDate?.trim() ?: ""
                                            else if(data[index] is com.example.scstrade.model.response.news.brecoder.Item ) (data[index] as com.example.scstrade.model.response.news.brecoder.Item).pubDate?.trim()?:""
                                            else if (data[index] is com.example.scstrade.model.response.news.profit.RssItem ) (data[index] as com.example.scstrade.model.response.news.profit.RssItem).pubDate.trim()
                                            else if (data[index] is com.example.scstrade.model.response.news.mettis.RssItem ) (data[index] as com.example.scstrade.model.response.news.mettis.RssItem).pubDate.trim()
                                            else if (data[index] is com.example.scstrade.model.response.news.dawn.RssItem ) (data[index] as com.example.scstrade.model.response.news.dawn.RssItem).pubDate.trim()
                                            else "" ),

                                            style = TextStyle(
                                                fontSize = dimensionResource(R.dimen.sp_12).value.sp,
                                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                                fontWeight = FontWeight(500),
                                                color = Color(0xFF79776F)
                                            )
                                        )
                                    }
                                }
                            }

                        )
                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.dp_10).value.dp))
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
                                            RoundedCornerShape(dimensionResource(R.dimen.dp_5).value.dp)
                                        ),
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                        )
                    }
                    Divider(
                        color = Color(0xFFB3C6C6CD),
                        thickness = dimensionResource(R.dimen.dp_1).value.dp,
                        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.dp_10).value.dp)
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_5).value.dp))

                }
            }
        }
    }

    @Composable
    private fun newsListMettis(data: List<NewsItem>, onItemClick:(NewsItem)->Unit) {
        Box(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.dp_20).value.dp, vertical = dimensionResource(R.dimen.dp_20).value.dp)) {
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
                                        text =data[index].title?:"No Description",
                                        fontSize = dimensionResource(R.dimen.sp_16).value.sp,
                                        minLines = 3,
                                        maxLines = 3,
                                        fontFamily = FontFamily(Font(R.font.inter_28pt_semibold_600)),
                                        fontWeight = FontWeight(600),
                                        color = colorResource(id = R.color.black),
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                    /*Row(
                                        modifier = Modifier.padding(top = dimensionResource(R.dimen.dp_2).value.dp),
                                    ) {

                                        Image(
                                            painter = painterResource(id = R.drawable.clock),
                                            contentDescription = "Clock",
                                            modifier = Modifier.size(dimensionResource(R.dimen.dp_15).value.dp)
                                        )
                                        Text(
                                            text =  Utils.convertDateBrFormat(if(data[index] is RssItem) (data[index] as RssItem).pubDate?.trim() ?: ""
                                            else if(data[index] is com.example.scstrade.model.response.news.brecoder.Item ) (data[index] as com.example.scstrade.model.response.news.brecoder.Item).pubDate?.trim()?:""
                                            else if (data[index] is com.example.scstrade.model.response.news.profit.RssItem ) (data[index] as com.example.scstrade.model.response.news.profit.RssItem).pubDate.trim()
                                            else if (data[index] is com.example.scstrade.model.response.news.mettis.RssItem ) (data[index] as com.example.scstrade.model.response.news.mettis.RssItem).pubDate.trim()
                                            else if (data[index] is com.example.scstrade.model.response.news.dawn.RssItem ) (data[index] as com.example.scstrade.model.response.news.dawn.RssItem).pubDate.trim()
                                            else "" ),

                                            style = TextStyle(
                                                fontSize = dimensionResource(R.dimen.sp_12).value.sp,
                                                fontFamily = FontFamily(Font(R.font.custom_font)),
                                                fontWeight = FontWeight(500),
                                                color = Color(0xFF79776F)
                                            )
                                        )
                                    }*/
                                }
                            }

                        )
                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.dp_10).value.dp))
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
                                    painter =  rememberAsyncImagePainter((data[index]).imageUrl),
                                    contentDescription = "Dawn",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillParentMaxHeight(0.1f)
                                        .clip(
                                            RoundedCornerShape(dimensionResource(R.dimen.dp_5).value.dp)
                                        ),
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                        )
                    }
                    Divider(
                        color = Color(0xFFB3C6C6CD),
                        thickness = dimensionResource(R.dimen.dp_1).value.dp,
                        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.dp_10).value.dp)
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_5).value.dp))

                }
            }
        }
    }

    @Composable
    fun newList(data: List<NewsData>,onItemClick:(NewsData)->Unit){

        Box(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.dp_20).value.dp, vertical = dimensionResource(R.dimen.dp_20).value.dp)) {

            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(data.size) { index ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
//                            IntentLinkText("Source",data[index].newsLink)
                        onItemClick(data[index])
                    }){
                        Box(
                            modifier = Modifier
                                .weight(3f)
                                .padding(end = dimensionResource(R.dimen.dp_5).value.dp),
                            content = {
                                Column(modifier = Modifier.fillMaxHeight()) {


                                        Text(
                                            text = data[index].type,
                                            fontSize = dimensionResource(R.dimen.sp_14).value.sp,
                                            maxLines = 1,
                                            fontFamily = FontFamily(Font(R.font.custom_font)),
                                            fontWeight = FontWeight(600),
                                            color = colorResource(id = R.color.black),
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = dimensionResource(R.dimen.dp_5).value.dp)
                                        )

                                    if(!data[index].type.equals("General News",true)){
                                        Text(
                                            text = data[index].newsHeading,
                                            fontSize = dimensionResource(R.dimen.sp_14).value.sp,
                                            maxLines = 1,
                                            fontFamily = FontFamily(Font(R.font.custom_font)),
                                            fontWeight = FontWeight(600),
                                            color = colorResource(id = R.color.black),
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = dimensionResource(R.dimen.dp_5).value.dp)
                                        )
                                    }

                                    Text(
                                        text = data[index].newsText,
                                        fontSize = dimensionResource(R.dimen.sp_14).value.sp,
                                        maxLines = 3,
                                        fontFamily = FontFamily(Font(R.font.custom_font)),
                                        fontWeight = FontWeight(400),
                                        color = colorResource(id = R.color.black),
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text(
                                        text = Utils.convertDateString( data[index].newsDate,"dd MMM yyyy"),
                                        fontSize = dimensionResource(R.dimen.sp_14).value.sp,
                                        fontFamily = FontFamily(Font(R.font.custom_font)),
                                        fontWeight = FontWeight(400),
                                        color = colorResource(id = R.color.black),
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.fillMaxWidth().padding(top = dimensionResource(R.dimen.dp_5).value.dp)
                                    )
                                   /* Row (
                                        modifier = Modifier.padding(top = dimensionResource(R.dimen.dp_5).value.dp,),
                                    ){
                                        IntentLinkText("Source",data[index].newsLink,
                                        )

                                    }
*/

                                }
                            }

                        )

                        Box(
                            modifier = Modifier
                                .weight(1f),
                            content = {
                                val context = LocalContext.current
                                if(!TextUtils.isEmpty(data[index].newsLink)) {
                                    val source=extractSourceName(data[index].newsLink)
                                    println("Source: https://scstrade.com/img/newsicon/${
                                        source
                                    }.png")
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data("https://scstrade.com/img/newsicon/${
                                                source
                                            }.png")
                                            .crossfade(true)
                                            .placeholder(R.drawable.news_empty_old)
                                            .error(R.drawable.news_empty_old)
                                            .listener(
                                                onError = { request, throwable ->
                                                    // 🔥 Error caught here
                                                    Log.e("ImageLoad", "Failed to load image", throwable.throwable)
                                                },
                                                onSuccess = { request, result ->
                                                    // ✅ Successfully loaded
                                                    Log.d("ImageLoad", "Image loaded successfully")
                                                }
                                            )
                                            .build(),
                                        contentDescription = data[index].newsLink,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.FillWidth

                                    )
                                }else {
                                    Image(
                                        painter = painterResource(id = R.drawable.news_empty_old),
                                        contentDescription = "Dawn",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.FillWidth
                                    )
                                }
                            }
                        )
                    }
                    Divider(
                        color = Color(0xFFB3C6C6CD),
                        thickness = dimensionResource(R.dimen.dp_1).value.dp,
                        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.dp_10).value.dp)
                    )
//                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dp_20).value.dp))

                }
            }
        }
    }
    @Composable
    fun IntentLinkText(
        label: String,
        url: String,
        modifier: Modifier = Modifier,
    ) {
        val context = LocalContext.current

        ClickableText(
            modifier = modifier,
            text = buildAnnotatedString {

                append(label)
                addStyle(
                    SpanStyle(
                        color = MaterialTheme.colors.primary,
                        textDecoration = TextDecoration.Underline
                    ),
                    0,
                    label.length
                )
            },
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                // Optionally add flags, chooser, etc.
                context.startActivity(intent)
            }
        )
    }
    fun extractImage(input: String): String? {
        val regex = """src=["'](https?://[^"']+)["']""".toRegex()
        return regex.find(input)?.groupValues?.get(1) // Returns first matched group
    }

    fun extractSourceName(newsLink: String): String {
        val secondLevelTlds = listOf("com.pk", "org.pk", "net.pk", "gov.pk")
        return try {
            val uri = java.net.URI(newsLink)
            val host = uri.host ?: return "Unknown"
            val parts = host.split(".")
            return if (secondLevelTlds.any { host.endsWith(it) }) {
                // .com.pk domain
                if (parts.size >= 3) parts[parts.size - 3] // e.thenews.com.pk
                else parts[0] // tribune.com.pk
            } else {
                // normal domain
                if (parts.size >= 2) parts[parts.size - 2] else parts[0]
            }
        } catch (e: Exception) {
            "Unknown"
        }
    }
}