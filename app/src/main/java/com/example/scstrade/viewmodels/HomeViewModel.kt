package com.example.scstrade.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scstrade.model.Resource
import com.example.scstrade.model.response.chart.ChartItem
import com.example.scstrade.model.summary.KSEIndices
import com.example.scstrade.repository.MainRepository
import com.example.scstrade.services.ApiService
import com.example.scstrade.services.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application):AndroidViewModel(application) {
    val repository: MainRepository= MainRepository(RetrofitInstance.create(ApiService::class.java),application)
    val isLineSelected=MutableLiveData<Boolean>(true)
    val isCandleSelected=MutableLiveData<Boolean>(false)
    val chartItem = MutableLiveData<Resource<List<ChartItem>>>()
    val selectedTime= MutableLiveData(arrayOf(false,false,false,false,false,true))
    val selectedIndex=MutableLiveData<KSEIndices>()

    fun fetchChart(){
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val result= repository.getIndexChart(
                    if (selectedIndex.value?.iNDEXCODE?.lowercase()?.contains("kmi 30")
                            ?: false
                    ) "KMI 30"
                    else if (selectedIndex.value?.iNDEXCODE?.lowercase()?.contains("kse 100")
                            ?: false
                    ) "KSE 100"
                    else if (selectedIndex.value?.iNDEXCODE?.lowercase()?.contains("kse 30")
                            ?: false
                    ) "KSE 30"
                    else "KSE ALL",
                    if (selectedTime.value?.indexOf(true) == 0) "1"
                    else if (selectedTime.value?.indexOf(true) == 1) "5"
                    else if (selectedTime.value?.indexOf(true) == 2) "15"
                    else if (selectedTime.value?.indexOf(true) == 3) "30"
                    else if (selectedTime.value?.indexOf(true) == 4) "60"
                    else "1D"
                )
                withContext(Dispatchers.Main){
                    chartItem.postValue(result)
                }
                delay(5000)
            }
        }
    }


    fun setSelectedTime(time:Int){
        when(time){
            1-> {
                selectedTime.value = arrayOf(true, false, false,false, false, false)
            }
            5-> {
                selectedTime.value = arrayOf(false, true, false,false, false, false)
            }
            15-> {
                selectedTime.value = arrayOf(false, false, true,false, false, false)
            }
            30-> {
                selectedTime.value = arrayOf(false, false, false,true, false, false)
            }
            60-> {
                selectedTime.value = arrayOf(false, false, false,false, true, false)
            }
            1440->{
                selectedTime.value = arrayOf(false, false, false,false, false, true)
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
        selectedTime.value?.forEachIndexed { index, b ->
            if(index==0){
                if(b){
                    setSelectedTime(1)
                }
            }else if(index==1){
                if(b){
                    setSelectedTime(5)
                }
            }else if(index==2){
                if(b){
                    setSelectedTime(15)
                }
            }else if(index==3){
                if(b){
                    setSelectedTime(30)
                }
            }else if(index==4){
                if(b){
                    setSelectedTime(60)
                }
            }else if(index==5){
                if(b){
                    setSelectedTime(1440)
                }
            }
        }

    }

    fun setSelectedCandle(){
        isLineSelected.value = false
        isCandleSelected.value = true
//        setSelectedTime(1)
        selectedTime.value?.forEachIndexed { index, b ->
            if(index==0){
                if(b){
                    setSelectedTime(1)
                }
            }else if(index==1){
                if(b){
                    setSelectedTime(5)
                }
            }else if(index==2){
                if(b){
                    setSelectedTime(15)
                }
            }else if(index==3){
                if(b){
                    setSelectedTime(30)
                }
            }else if(index==4){
                if(b){
                    setSelectedTime(60)
                }
            }else if(index==5){
                if(b){
                    setSelectedTime(1440)
                }
            }
        }
    }
}