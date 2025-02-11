package com.example.scstrade.views.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.chart.ChartItem
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.repository.ChartRepository
import com.example.scstrade.services.RetrofitInstance
import kotlinx.coroutines.launch

class HomeViewModel:ViewModel() {
    val repository= ChartRepository(RetrofitInstance.api)
    val isLineSelected=MutableLiveData<Boolean>(true)
    val isCandleSelected=MutableLiveData<Boolean>(false)
    val timeSelected=MutableLiveData(arrayOf(true,false,false,false,false))
    val selectedIndex = MutableLiveData<KSEIndices?>()
    val chartMutable=MutableLiveData<Resource<List<ChartItem>>>()
    fun fetchChart(){

        viewModelScope.launch {

            val index=timeSelected.value?.indexOf(true)
            chartMutable.value =  repository.getIndexChart(
                if(selectedIndex.value?.iNDEXCODE?.lowercase()?.contains("kmi 30")?:false) "kmi30"
                else if(selectedIndex.value?.iNDEXCODE?.lowercase()?.contains("kse 100")?:false) "kse"
                else if(selectedIndex.value?.iNDEXCODE?.lowercase()?.contains("kse 30")?:false) "kse30"
                else "kseall",
                if(index==0) 1
                else if (index==1) 5
                else if (index==2) 15
                else if(index==3) 30
                else 60
            )
            Log.e("index: ",index.toString())
        }

    }

    fun selectCandle(){
        isCandleSelected.value=true
        isLineSelected.value=false
        timeSelected.value=arrayOf(true, false, false,false, false)
        setSelectedIndex(selectedIndex.value)
    }

    fun selectLine(){
        isCandleSelected.value=false
        isLineSelected.value=true
        timeSelected.value=arrayOf(true, false, false,false, false)
        setSelectedIndex(selectedIndex.value)
    }

    fun selectTime(time:Int){
        when(time){
            1-> {
                timeSelected.value = arrayOf(true, false, false,false, false)
            }
            5-> {
                timeSelected.value = arrayOf(false, true, false,false, false)
            }
            15-> {
                timeSelected.value = arrayOf(false, false, true,false, false)
            }
            30-> {
                timeSelected.value = arrayOf(false, false, false,true, false)
            }
            60-> {
                timeSelected.value = arrayOf(false, false, false,false, true)
            }
        }
        setSelectedIndex(selectedIndex.value)
    }

    fun setSelectedIndex(kseIndices: KSEIndices?){
        selectedIndex.value = kseIndices

    }

}