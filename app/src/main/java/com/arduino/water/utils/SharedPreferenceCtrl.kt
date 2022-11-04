package com.arduino.water.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceCtrl {

    companion object{
        const val HOUSE_MEMBER = "SP_HOUSE_MEMBER"
    }

    private lateinit var preference: SharedPreferences

    fun init(pContext: Context) {
        preference = pContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    fun setHouseMember(value : Int){
        val editor = preference.edit()
        editor.putInt(HOUSE_MEMBER, value)
        editor.apply()
    }

    fun getHouseMember() : Int{
        return preference.getInt(HOUSE_MEMBER,1)
    }
}