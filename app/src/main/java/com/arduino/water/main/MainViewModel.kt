package com.arduino.water.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arduino.water.base.BaseViewModel
import com.arduino.water.model.WaterUsageData
import com.arduino.water.utils.Event
import com.arduino.water.utils.SingleLiveEvent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel : BaseViewModel() {

    private val _initData = SingleLiveEvent<ArrayList<WaterUsageData>>()
    val initData: LiveData<ArrayList<WaterUsageData>> get() = _initData

    private var timer = Timer()

    var reloading = true


    fun getWaterUsageData(){
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("waterUsage")

        val gson = GsonBuilder().create()

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(reloading){
                    try {
                        val listType : TypeToken<ArrayList<WaterUsageData>> = object : TypeToken<ArrayList<WaterUsageData>>(){}
                        val list = gson.fromJson<ArrayList<WaterUsageData>>(snapshot.value.toString(),listType.type)

                        _initData.value = list

                        reloading = false

                        timer.schedule(object : TimerTask() {
                            override fun run() {
                                reloading = true
                            }
                        },5000)
                    }catch (e : Exception){
                        Timber.d("timberMsg onDataChange error $e")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.d("timberMsg $error")
            }

        })
    }
}