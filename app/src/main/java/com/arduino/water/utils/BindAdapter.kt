package com.arduino.water.utils

import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.arduino.water.R
import com.arduino.water.WaterApplication
import com.arduino.water.adapter.CalendarAdapter
import com.arduino.water.adapter.HouseHoldMemberAdapter
import com.arduino.water.adapter.WeekAdapter
import com.bumptech.glide.Glide
import java.io.File

object BindAdapter {

    fun houseHoldMemberAdapter(recyclerview: RecyclerView, adapter : HouseHoldMemberAdapter) {
        if(recyclerview.adapter == null){
            if (!adapter.hasObservers()) adapter.setHasStableIds(true)

            recyclerview.layoutManager = GridLayoutManager(recyclerview.context, 4)
            recyclerview.adapter = adapter
            val animator = recyclerview.itemAnimator
            if (animator is SimpleItemAnimator) {
                animator.supportsChangeAnimations = false
            }
        }
    }

    fun weekAdapter(recyclerview: RecyclerView, adapter : WeekAdapter) {
        if(recyclerview.adapter == null){
            if (!adapter.hasObservers()) adapter.setHasStableIds(true)

            recyclerview.layoutManager = GridLayoutManager(recyclerview.context, 7)
            recyclerview.adapter = adapter
            val animator = recyclerview.itemAnimator
            if (animator is SimpleItemAnimator) {
                animator.supportsChangeAnimations = false
            }
        }
    }

    fun calendarAdapter(recyclerview: RecyclerView, adapter : CalendarAdapter) {
        if(recyclerview.adapter == null){
            if (!adapter.hasObservers()) adapter.setHasStableIds(true)

            val layoutManager = GridLayoutManager(recyclerview.context, 7).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
                    override fun getSpanSize(position: Int): Int {
                        return when(adapter.getItemViewType(position)){
                            1 -> 1
                            else -> 7
                        }
                    }
                }
            }

            recyclerview.layoutManager = layoutManager
            recyclerview.adapter = adapter
            val animator = recyclerview.itemAnimator
            if (animator is SimpleItemAnimator) {
                animator.supportsChangeAnimations = false
            }
        }
    }

    @BindingAdapter("setCalendarColor")
    @JvmStatic
    fun setCalendarColor(layout: ConstraintLayout, totalWater: Float) {

        val memberUsing = WaterApplication.mInstance.waterConsumption * WaterApplication.mInstance.shared.getHouseMember()

        when{
            totalWater == 0F -> {
                layout.setBackgroundColor(WaterApplication.mInstance.resources.getColor(R.color.color_none,null))
            }

            (memberUsing*1.05) <= totalWater && totalWater < (memberUsing*1.1) -> {
                layout.setBackgroundColor(WaterApplication.mInstance.resources.getColor(R.color.yellow,null))
            }

            (memberUsing*1.1) <= totalWater -> {
                layout.setBackgroundColor(WaterApplication.mInstance.resources.getColor(R.color.calendar_pink,null))
            }

            (memberUsing*0.95) <= totalWater && totalWater < (memberUsing*1.05) -> {
                layout.setBackgroundColor(WaterApplication.mInstance.resources.getColor(R.color.white,null))
            }

            totalWater < (memberUsing*0.95) -> {
                layout.setBackgroundColor(WaterApplication.mInstance.resources.getColor(R.color.green,null))
            }
        }
    }

}