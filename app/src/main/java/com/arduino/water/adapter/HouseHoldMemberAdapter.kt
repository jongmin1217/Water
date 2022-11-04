package com.arduino.water.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arduino.water.databinding.RecyclerviewHouseholdMemberBinding
import com.arduino.water.model.CalendarData
import com.arduino.water.model.HouseHoldMember

class HouseHoldMemberAdapter(
    private val click : ((value : HouseHoldMember)->Unit)
) : RecyclerView.Adapter<HouseHoldMemberAdapter.ViewHolder>() {

    var items = ArrayList<HouseHoldMember>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerviewHouseholdMemberBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }


    override fun getItemId(position: Int): Long {
        return items[position].text.hashCode().toLong()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(val binding: RecyclerviewHouseholdMemberBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun bind(houseHoldMember: HouseHoldMember) {
            binding.model = houseHoldMember

            binding.cbHouseHold.setOnClickListener {
                if (items[adapterPosition].checked) {
                    for (i in 0 until items.size) {
                        if (items[i].checked && i != adapterPosition) {
                            items[i].checked = false
                        }
                    }
                    notifyDataSetChanged()
                    click(houseHoldMember)
                }
            }
        }
    }
}