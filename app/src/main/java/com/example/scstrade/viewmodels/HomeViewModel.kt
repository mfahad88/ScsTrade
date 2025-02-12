package com.example.scstrade.viewmodels

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
    val chartItem = MutableLiveData<Resource<List<ChartItem>>>()
    val selectedTime= MutableLiveData(arrayOf(true,false,false,false,false))
    val selectedIndex=MutableLiveData<KSEIndices>()

    fun fetchChart(){
        viewModelScope.launch {
            chartItem.value=repository.getIndexChart(
                if(selectedIndex.value?.iNDEXCODE?.lowercase()?.contains("kmi 30")?:false) "kmi30"
                else if(selectedIndex.value?.iNDEXCODE?.lowercase()?.contains("kse 100")?:false) "kse"
                else if(selectedIndex.value?.iNDEXCODE?.lowercase()?.contains("kse 30")?:false) "kse30"
                else "kseall",
                if(selectedTime.value?.indexOf(true)==0) 1
                else if (selectedTime.value?.indexOf(true)==1) 5
                else if (selectedTime.value?.indexOf(true)==2) 15
                else if(selectedTime.value?.indexOf(true)==3) 30
                else 60
            )
        }
    }


    fun setSelectedTime(time:Int){
        when(time){
            1-> {
                selectedTime.value = arrayOf(true, false, false,false, false)
            }
            5-> {
                selectedTime.value = arrayOf(false, true, false,false, false)
            }
            15-> {
                selectedTime.value = arrayOf(false, false, true,false, false)
            }
            30-> {
                selectedTime.value = arrayOf(false, false, false,true, false)
            }
            60-> {
                selectedTime.value = arrayOf(false, false, false,false, true)
            }
        }
        fetchChart()
    }

    fun setSelectedIndex(kseIndices: KSEIndices){
        selectedIndex.value=kseIndices


    }

    fun setSelectedLine(){
        isLineSelected.value = true
        isCandleSelected.value = false
        setSelectedTime(1)
    }

    fun setSelectedCandle(){
        isLineSelected.value = false
        isCandleSelected.value = true
        setSelectedTime(1)
    }
}