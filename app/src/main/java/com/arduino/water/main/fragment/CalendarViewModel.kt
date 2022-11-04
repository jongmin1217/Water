package com.arduino.water.main.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arduino.water.WaterApplication
import com.arduino.water.base.BaseViewModel
import com.arduino.water.model.CalendarData
import com.arduino.water.model.WaterUsageData
import com.arduino.water.utils.SingleLiveEvent
import com.arduino.water.utils.Utils
import timber.log.Timber
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarViewModel : BaseViewModel() {

    val nowYear = Utils.getYear()
    val nowMonth = Utils.getMonth()

    val selectYear = MutableLiveData<Int>().default(nowYear)
    val selectMonth = MutableLiveData<Int>().default(nowMonth)

    val totalWater = MutableLiveData<String>().default("")
    val totalPrice = MutableLiveData<String>().default("")
    val beforeWater = MutableLiveData<String>().default("")
    val beforePrice = MutableLiveData<String>().default("")

    var data = ArrayList<WaterUsageData>()

    private val _initCalendar = SingleLiveEvent<ArrayList<CalendarData>>()
    val initCalendar: LiveData<ArrayList<CalendarData>> get() = _initCalendar

    fun initCalendar(){
        val year = selectYear.value?:0
        val month = selectMonth.value?:0

        initCalendarItems(
            Utils.lastDay(year, month),
            Utils.getDateDay(String.format("%d%s%s", year, Utils.dateText(month), "01"), "yyyyMMdd"),
            year,
            month
        ).apply {
            _initCalendar.value = this

            val dec = DecimalFormat("#,###,###")

            val beforeYear = if(month == 1) year -1 else year
            val beforeMonth = if(month == 1) 12 else month-1
            val thisMonth = data.filter { it.year == year && it.month == month }
            val beforeMonthList = data.filter { it.year == beforeYear && it.month == beforeMonth }

            var thisUsage = 0
            var thisPrice = 0

            if(thisMonth.isEmpty()){
                totalWater.value = "0L"
                totalPrice.value = "0원"
            }else{
                var totalWaterUsage = 0
                for(i in thisMonth){
                    val todayTotalWater = i.bath+i.kitchen+i.laundry
                    totalWaterUsage += todayTotalWater
                }
                totalWater.value = "${totalWaterUsage}L"
                val price = totalWaterUsage * WaterApplication.mInstance.waterPrice

                val priceText = String.format("%s원",dec.format(price))
                totalPrice.value = priceText

                thisUsage = totalWaterUsage
                thisPrice = price
            }

            if(beforeMonthList.isEmpty()){
                beforeWater.value = "${thisUsage}L 증가"
                beforePrice.value = String.format("%s원 증가",dec.format(thisPrice))
            }else{
                var totalWaterUsage = 0
                for(i in beforeMonthList){
                    val todayTotalWater = i.bath+i.kitchen+i.laundry
                    totalWaterUsage += todayTotalWater
                }
                val waterDifference = thisUsage - totalWaterUsage
                beforeWater.value = if(waterDifference>=0) "${waterDifference}L 증가"
                else "${-waterDifference}L 감소"

                val price = totalWaterUsage * WaterApplication.mInstance.waterPrice
                val priceDifference = thisPrice- price

                beforePrice.value = if(priceDifference>=0) String.format("%s원 증가",dec.format(priceDifference))
                else String.format("%s원 감소",dec.format(-priceDifference))
            }
        }
    }

    private fun initCalendarItems(
        lastDay: Int,
        startDay: Int,
        year: Int,
        month: Int
    ): ArrayList<CalendarData> {
        val list = ArrayList<CalendarData>()

        if (startDay != 1) {
            val beforeLastDay = Utils.beforeMonthLastDay(year, month)
            for (i in 0 until startDay - 1) {
                val dayData = data.filter { it.year == beforeLastDay.year && it.month == beforeLastDay.month && it.day == beforeLastDay.lastDay - (startDay - i - 2) }

                list.add(
                    CalendarData(
                        i.toLong(),
                        1,
                        beforeLastDay.year,
                        beforeLastDay.month,
                        beforeLastDay.lastDay - (startDay - i - 2),
                        if(dayData.isEmpty()) 0 else dayData[0].laundry,
                        if(dayData.isEmpty()) 0 else dayData[0].bath,
                        if(dayData.isEmpty()) 0 else dayData[0].kitchen,
                        null,
                        false
                    )
                )
            }
        }

        for (i in 0 until lastDay) {
            val dayData = data.filter { it.year == year && it.month == month && it.day == (i + 1) }

            list.add(
                CalendarData(
                    if (list.isEmpty()) 0 else list[list.size - 1].id + 1,
                    1,
                    year,
                    month,
                    (i + 1),
                    if(dayData.isEmpty()) 0 else dayData[0].laundry,
                    if(dayData.isEmpty()) 0 else dayData[0].bath,
                    if(dayData.isEmpty()) 0 else dayData[0].kitchen,
                    null,
                    true
                )
            )
        }

        val nextMonthYear = if (month == 12) year + 1 else year
        val nextMonth = if (month == 12) 1 else month + 1

        val lastDate =
            Utils.getDateDay(String.format("%d%s%s", year, Utils.dateText(month), lastDay.toString()), "yyyyMMdd")

        if(lastDate != 7){
            for (i in 0 until (7-lastDate)) {
                val dayData = data.filter { it.year == nextMonthYear && it.month == nextMonth && it.day == (i + 1) }
                list.add(
                    CalendarData(
                        if (list.isEmpty()) 0 else list[list.size - 1].id + 1,
                        1,
                        nextMonthYear,
                        nextMonth,
                        (i+1),
                        if(dayData.isEmpty()) 0 else dayData[0].laundry,
                        if(dayData.isEmpty()) 0 else dayData[0].bath,
                        if(dayData.isEmpty()) 0 else dayData[0].kitchen,
                        null,
                        false
                    )
                )
            }
        }

        for(i in list.size downTo 0){
            if(i%7 == 0 && i!=0 || i == list.size){

                var totalWater = 0
                for(j in i-1 downTo i-7){
                    totalWater += list[j].getTotalWater()
                }
                list.add(i,
                    CalendarData(
                        -i.toLong(),
                        2,
                        0,
                        0,
                        0,0,0,0,
                        totalWater, true
                    )
                )
            }
        }


        return list
    }
}