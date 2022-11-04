package com.arduino.water.utils

import android.annotation.SuppressLint
import com.arduino.water.model.BeforeCalendarData
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object{
        fun getYear(): Int = Calendar.getInstance().get(Calendar.YEAR)

        fun getMonth(): Int = Calendar.getInstance().get(Calendar.MONTH) + 1

        fun getDay(): Int = Calendar.getInstance().get(Calendar.DATE)

        fun lastDay(year : Int, month : Int) : Int{
            val cal = Calendar.getInstance()

            cal.set(year,month-1,1)

            return cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        }

        fun dateText(value : Int) : String{
            return if (value < 10) "0$value" else value.toString()
        }

        fun beforeMonthLastDay(year : Int, month : Int) : BeforeCalendarData {
            val cal = Calendar.getInstance()

            val searchYear = if(month == 1) year - 1 else year
            val searchMonth = if(month == 1) 12 else month - 1
            cal.set(searchYear,searchMonth-1,1)

            return BeforeCalendarData(searchYear,searchMonth,cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        }

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        @SuppressLint("SimpleDateFormat")
        fun getDateDay(date: String, dateType: String): Int {
            val dateFormat = SimpleDateFormat(dateType)
            val nDate = dateFormat.parse(date)

            val cal = Calendar.getInstance()
            cal.time = nDate

            return cal.get(Calendar.DAY_OF_WEEK)
        }
    }
}