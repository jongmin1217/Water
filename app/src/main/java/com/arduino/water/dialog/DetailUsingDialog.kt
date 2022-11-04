package com.arduino.water.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.arduino.water.R
import com.arduino.water.base.BaseDialog
import com.arduino.water.databinding.DialogDetailUsingBinding
import com.arduino.water.model.CalendarData
import timber.log.Timber

class DetailUsingDialog(
    mContext : Context,
    lifecycleOwner: LifecycleOwner,
    private val viewModel: DetailUsingViewModel,
    private val calendarData : CalendarData
) : BaseDialog<DialogDetailUsingBinding>(R.layout.dialog_detail_using,mContext,lifecycleOwner) {

    override fun initBinding() {
        binding.vm = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel.initData(calendarData)

        binding.ivClose.setOnClickListener {
            dismiss()
            cancel()
        }

        binding.tvGraph1.run {
            postDelayed({
                // 전체 길이
                val width = ((binding.tvGraph4.left+binding.tvGraph4.right)/2)-((binding.tvGraph1.left+binding.tvGraph1.right)/2)

                // 값들의 비율
                val kitchenRatio = calendarData.kitchen/calendarData.getMaxValue().toFloat()
                val laundryRatio = calendarData.laundry/calendarData.getMaxValue().toFloat()
                val bathRatio = calendarData.bath/calendarData.getMaxValue().toFloat()

                // 값들의 길이 구하기
                val kitchenValue = width * kitchenRatio
                val laundryValue = width * laundryRatio
                val bathValue = width * bathRatio

                // 뷰에 길이 할당
                if(kitchenValue == 0F) binding.kitchenValue.visibility = View.GONE
                else binding.kitchenValue.setWidth(kitchenValue.toInt())

                if(laundryValue == 0F) binding.laundryValue.visibility = View.GONE
                else binding.laundryValue.setWidth(laundryValue.toInt())

                if(bathValue == 0F) binding.bathValue.visibility = View.GONE
                else binding.bathValue.setWidth(bathValue.toInt())

            },100)
        }
    }

    private fun View.setWidth(value: Int) {
        val lp = layoutParams
        lp?.let {
            lp.width = value
            layoutParams = lp
        }
    }
}