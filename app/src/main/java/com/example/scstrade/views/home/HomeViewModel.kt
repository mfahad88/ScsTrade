package com.example.scstrade.views.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scstrade.repository.ChartRepository
import com.example.scstrade.services.RetrofitInstance
import kotlinx.coroutines.launch

class HomeViewModel:ViewModel() {
    val repository= ChartRepository(RetrofitInstance.api)
    val isLineSelected=MutableLiveData<Boolean>(true)
    val isCandleSelected=MutableLiveData<Boolean>(false)
    val timeSelected=MutableLiveData(arrayOf(true,false,false,false))
    val selectedSymbol = MutableLiveData<String>()
    fun fetchChart(){

        viewModelScope.launch {
            val index=timeSelected.value?.filter { it }?.mapIndexed { index, b -> index }?.first()
            Log.e("index: ",index.toString())
        }

    }

    fun selectCandle(){
        isCandleSelected.value=true
        isLineSelected.value=false
    }

    fun selectLine(){
        isCandleSelected.value=false
        isLineSelected.value=true
    }

    fun selectTime(time:Int){
        when(time){
            1-> {
                timeSelected.value = arrayOf(true, false, false, false)
            }
            5-> {
                timeSelected.value = arrayOf(false, true, false, false)
            }
            15-> {
                timeSelected.value = arrayOf(false, false, true, false)
            }
            60-> {
                timeSelected.value = arrayOf(false, false, false, true)
            }
        }
    }

    fun setSelectedIndex(symbol:String){
        selectedSymbol.value = symbol
        fetchChart()
    }

}