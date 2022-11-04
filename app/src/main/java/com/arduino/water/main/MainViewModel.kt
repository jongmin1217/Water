package com.arduino.water.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arduino.water.base.BaseViewModel
import com.arduino.water.model.WaterUsageData
import com.arduino.water.utils.Event
import com.arduino.water.utils.SingleLiveEvent
import com.arduino.water.utils.Utils
import com.google.firebase.database.*
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

        //testWriteDatabase(myRef)

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

    //임의로 데이터 베이스 저장하는 함수
    private fun testWriteDatabase(database : DatabaseReference){
        val list = ArrayList<WaterUsageData>()

        // 데이터를 넣기 시작하는 달
        val startMonth = 3

        val nowMonth = Utils.getMonth()
        val nowDay = Utils.getDay()

        for(i in startMonth..nowMonth){

            val lastDay = when(i){
                1,3,5,7,8,10,12 -> 31
                2 -> 28
                else -> 30
            }

            for(j in 1..lastDay){
                if(i == nowMonth && j == nowDay+1) break
                list.add(
                    WaterUsageData(
                        2022,
                        i,
                        j,
                        // 0 ~ 200 랜덤
                        (Random().nextInt(200)).toFloat(),
                        (Random().nextInt(200)).toFloat(),
                        (Random().nextInt(200)).toFloat()
                    )
                )
            }
        }

        database.setValue(list)
    }
}