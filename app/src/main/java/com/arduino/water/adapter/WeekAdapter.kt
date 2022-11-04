package com.arduino.water.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arduino.water.databinding.RecyclerviewHouseholdMemberBinding
import com.arduino.water.databinding.RecyclerviewWeekBinding
import com.arduino.water.model.HouseHoldMember

class WeekAdapter : RecyclerView.Adapter<WeekAdapter.ViewHolder>(){

    var items = ArrayList<String>()
        @SuppressLint("NotifyDataSetChanged")
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerviewWeekBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }


    override fun getItemId(position: Int): Long {
        return items[position].hashCode().toLong()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(val binding: RecyclerviewWeekBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(text: String){
            binding.model = text

        }
    }
}