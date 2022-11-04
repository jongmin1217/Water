package com.arduino.water.dialog

import androidx.lifecycle.MutableLiveData
import com.arduino.water.base.BaseViewModel
import com.arduino.water.model.CalendarData

class DetailUsingViewModel : BaseViewModel() {

    val calendarData = MutableLiveData<CalendarData>().default(null)


    fun initData(data : CalendarData){
        calendarData.value = data
    }
}