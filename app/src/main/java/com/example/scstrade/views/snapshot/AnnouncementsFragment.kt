package com.example.scstrade.views.snapshot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scstrade.R
import com.example.scstrade.databinding.FragmentAnnouncementsBinding


class AnnouncementsFragment : Fragment() {
    lateinit var binding:FragmentAnnouncementsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAnnouncementsBinding.inflate(inflater,container,false)
        binding.loader.visibility = View.GONE
        binding.main.visibility=View.VISIBLE
        binding.main.setContent {
            AnnouncementItems()
        }
        return binding.root
    }
    @Composable
    private fun AnnouncementItems(){
        LazyColumn(modifier = Modifier.padding(horizontal = 15.dp)) {
          item {
              Column {
                  Row {
                      Box (modifier = Modifier.weight(0.7f)){
                          Text(
                              text = "Transmission of Quarterly Report\nfor the Period Ended Septembe....",
                              style = TextStyle(
                                  fontSize = 16.sp,
                                  lineHeight = 19.sp,
                                  fontFamily = FontFamily(Font(R.font.custom_font)),
                                  fontWeight = FontWeight(500),
                                  color = colorResource(R.color.md_theme_primary),
                              )
                          )
                      }
                      Box(modifier = Modifier.weight(0.3f)){
                          Row{
                              Box(modifier = Modifier
                                  .size(32.dp)
                                  .border(
                                      width = 1.dp, color = Color(0xFF79776F),
                                      RoundedCornerShape(21.dp)
                                  )) {
                                  Image(painter = painterResource(id = R.drawable.baseline_remove_red_eye_24), contentDescription = "View", modifier = Modifier.align(Alignment.Center).padding(7.dp))
                              }
                              Spacer(modifier = Modifier.width(8.dp))
                              Box(modifier = Modifier
                                  .size(32.dp)
                                  .border(
                                      width = 1.dp, color = Color(0xFF79776F),
                                      RoundedCornerShape(21.dp)
                                  )) {
                                  Image(painter = painterResource(id = R.drawable.baseline_arrow_downward_24), contentDescription = "Download", modifier = Modifier.align(Alignment.Center).padding(7.dp))
                              }
                              Spacer(modifier = Modifier.width(8.dp))
                              Box(modifier = Modifier
                                  .size(32.dp)
                                  .border(
                                      width = 1.dp, color = Color(0xFF79776F),
                                      RoundedCornerShape(21.dp)
                                  )) {
                                  Image(painter = painterResource(id = R.drawable.baseline_share_24), contentDescription = "Share", colorFilter = ColorFilter.tint(color = colorResource(
                                      id = R.color.md_theme_primary
                                  )), modifier = Modifier.align(Alignment.Center).padding(7.dp))
                              }
                          }
                      }
                  }
                  Spacer(modifier = Modifier.height(3.dp))
                  Row{
                      Text(
                          text = "21 Dec 2024 | 04:30PM",
                          style = TextStyle(
                              fontSize = 14.sp,
                              lineHeight = 20.sp,
                              fontFamily = FontFamily(Font(R.font.custom_font)),
                              fontWeight = FontWeight(500),
                              color = Color(0xFF1C1B1B),
                              textAlign = TextAlign.Center,
                              letterSpacing = 0.1.sp,
                          )
                      )
                  }
                  Spacer(modifier = Modifier.height(10.dp))
                  Row{
                      Divider(
                          color = Color(0xFFE5E2E1),
                          modifier = Modifier.fillMaxWidth(),
                          thickness = 1.dp
                      )
                  }
              }
          }
        }
    }

}