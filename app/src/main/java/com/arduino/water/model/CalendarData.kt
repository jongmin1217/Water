package com.arduino.water.model

import com.arduino.water.utils.Utils
import com.google.gson.annotations.SerializedName

data class CalendarData(
    val id : Long,
    val type : Int,
    val year : Int,
    val month : Int,
    val day : Int,
    val laundry : Int,
    val bath : Int,
    val kitchen : Int,
    val totalWeekWater : Int?,
    val visible : Boolean
){
    fun getTotalWater() : Int = laundry+bath+kitchen

    fun getTotalWaterDialog() : String = "총 ${getTotalWater()}L"

    fun getDate() : String{
        val week = when(Utils.getDateDay(String.format("%d%s%s", year, Utils.dateText(month), Utils.dateText(day)), "yyyyMMdd")){
            1 -> "일요일"
            2 -> "월요일"
            3 -> "화요일"
            4 -> "수요일"
            5 -> "목요일"
            6 -> "금요일"
            else -> "토요일"
        }

        return "${day}일 $week"
    }


    fun getMaxValue() : Int{
        val list = arrayListOf(laundry,bath,kitchen)

        val max = list.maxOf { it }
        val margin = max/4
        val total = margin + max
        val plus = total % 10

        return total + (10-plus)
    }

    fun getFirstValue() : Int = ((getMaxValue()/3F)*1).toInt()

    fun getSecondValue() : Int = ((getMaxValue()/3F)*2).toInt()


    fun getWaterText() : String = "${laundry+bath+kitchen}L"

    fun getTotalWaterText() : String = "${totalWeekWater}L"

    fun nowDay() : Boolean = Utils.getYear() == year && Utils.getMonth() == month && Utils.getDay() == day
}

data class BeforeCalendarData(
    val year : Int,
    val month : Int,
    val lastDay : Int
)

data class WaterUsageData(
    @SerializedName("year")
    val year : Int,
    @SerializedName("month")
    val month : Int,
    @SerializedName("day")
    val day : Int,
    @SerializedName("laundry")
    val laundry : Int,
    @SerializedName("kitchen")
    val kitchen : Int,
    @SerializedName("bath")
    val bath : Int
)