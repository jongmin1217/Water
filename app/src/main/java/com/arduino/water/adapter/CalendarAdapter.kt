package com.arduino.water.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arduino.water.databinding.RecyclerviewCalendarBarBinding
import com.arduino.water.databinding.RecyclerviewCalendarBinding
import com.arduino.water.databinding.RecyclerviewHouseholdMemberBinding
import com.arduino.water.model.CalendarData
import com.arduino.water.model.HouseHoldMember

class CalendarAdapter(
    private val click : ((value : CalendarData)->Unit)
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var items = ArrayList<CalendarData>()
        @SuppressLint("NotifyDataSetChanged")
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                val binding = RecyclerviewCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CalendarHolder(binding)
            }

            else -> {
                val binding = RecyclerviewCalendarBarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BarHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type
    }

    override fun getItemCount(): Int {
        return items.size
    }


    override fun getItemId(position: Int): Long {
        return items[position].id
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CalendarHolder -> holder.bind(items[position])
            is BarHolder -> holder.bind(items[position])
        }
    }

    inner class CalendarHolder(val binding: RecyclerviewCalendarBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(calendarData: CalendarData){
            binding.model = calendarData

            binding.layout.setOnClickListener {
                if(calendarData.getTotalWater() != 0) click(calendarData)
            }

        }
    }

    inner class BarHolder(val binding: RecyclerviewCalendarBarBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(calendarData: CalendarData){
            binding.model = calendarData

        }
    }
}