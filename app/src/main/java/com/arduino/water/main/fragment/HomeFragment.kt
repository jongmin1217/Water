package com.arduino.water.main.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.fragment.app.activityViewModels
import com.arduino.water.R
import com.arduino.water.WaterApplication
import com.arduino.water.adapter.HouseHoldMemberAdapter
import com.arduino.water.base.BaseFragment
import com.arduino.water.databinding.FragmentHomeBinding
import com.arduino.water.model.HouseHoldMember
import com.arduino.water.model.WaterUsageData
import com.arduino.water.utils.BindAdapter
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import timber.log.Timber
import java.text.DecimalFormat

class HomeFragment(
    private val editHouseMember : (()->Unit)
) : BaseFragment<FragmentHomeBinding,HomeViewModel>(R.layout.fragment_home) {
    override val viewModel by activityViewModels<HomeViewModel>()

    override fun initBinding() {
        binding.vm = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
    }

    fun initLayout(waterUsageData : WaterUsageData){

        val text = binding.tvWaterInfo.text.toString()

        val totalUsage = waterUsageData.laundry+waterUsageData.bath+waterUsageData.kitchen

        val standardCnt = totalUsage/WaterApplication.mInstance.standard.toFloat()

        val startText = text.indexOf("을")
        val endText = text.indexOf("번")

        val newText = text.replace(text.substring(startText+2,endText),String.format("%.1f",standardCnt))

        binding.tvWaterInfo.text = newText

        val content = binding.tvWaterInfo.text

        val spannableString = SpannableString(content)
        val word = "올림픽 규격 수영장"
        val start = content.indexOf(word)
        val end = start + word.length

        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),start,end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#10BABC")),start,end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val sampleDp = 24
        val density = resources.displayMetrics.density
        val value = (sampleDp * density).toInt()

        spannableString.setSpan(
            AbsoluteSizeSpan(value),start,end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val wordCnt = String.format("%.1f번",standardCnt)
        val startCnt = content.indexOf(wordCnt)
        val endCnt = startCnt + wordCnt.length

        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),startCnt,endCnt,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#6369FF")),startCnt,endCnt,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.tvWaterInfo.text = spannableString
    }

    private fun initAdapter(){
        val items = ArrayList<HouseHoldMember>()
        for(i in 1..8){
            items.add(HouseHoldMember(WaterApplication.mInstance.shared.getHouseMember()==i,i.toString()))
        }

        val adapter = HouseHoldMemberAdapter{
            WaterApplication.mInstance.shared.setHouseMember(it.text.toInt())
            editHouseMember()
        }

        BindAdapter.houseHoldMemberAdapter(binding.recyclerviewHouseMember,adapter)
        adapter.items = items
    }

    fun initChart(waterUsageData : WaterUsageData){

        with(binding.pieChart) {
            setUsePercentValues(true)
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
            transparentCircleRadius = 5f
            setDrawCenterText(true)


            setCenterTextColor(resources.getColor(R.color.basic,null))

            setCenterTextSize(64f)
            holeRadius = 70f

            val totalUsage = (waterUsageData.laundry+waterUsageData.bath+waterUsageData.kitchen).toInt()
            val price = totalUsage*WaterApplication.mInstance.waterPrice

            val dec = DecimalFormat("#,###,###")
            val priceText = String.format("%s원",dec.format(price))

            val content = "${totalUsage}L\n${priceText}"

            val spannableString = SpannableString(content)
            val start = content.indexOf(priceText)
            val end = start + priceText.length

            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),0,end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor("#848484")),start,end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            val sampleDp = 32
            val density = resources.displayMetrics.density
            val value = (sampleDp * density).toInt()

            spannableString.setSpan(
                AbsoluteSizeSpan(value),start,end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)



            centerText = spannableString


            val yValues: ArrayList<PieEntry> = ArrayList()

            if(waterUsageData.bath == 0F && waterUsageData.kitchen==0F && waterUsageData.laundry==0F){
                yValues.add(PieEntry(100F,"목욕"))
                yValues.add(PieEntry(100F, "주방"))
                yValues.add(PieEntry(100F, "세탁"))
            }else{
                if(waterUsageData.bath != 0F) yValues.add(PieEntry(waterUsageData.bath,"목욕"))
                if(waterUsageData.kitchen != 0F) yValues.add(PieEntry(waterUsageData.kitchen,"주방"))
                if(waterUsageData.laundry != 0F) yValues.add(PieEntry(waterUsageData.laundry,"세탁"))
            }

            animateY(1000, Easing.EaseInOutCubic)

            val colors = IntArray(3)
            colors[0] = Color.parseColor("#9C87EF")
            colors[1] = Color.parseColor("#86B6FF")
            colors[2] = Color.parseColor("#00C8CB")
            val dataSet = PieDataSet(yValues, "")
            with(dataSet) {
                sliceSpace = 1f
                setColors(*colors)
            }

            val pieData = PieData(dataSet)
            with(pieData) {
                setValueTextSize(14f)
                setValueTextColor(Color.parseColor("#3D3D3D"))
                setEntryLabelTextSize(15f)
                setEntryLabelColor(Color.parseColor("#ffffff"))

            }
            data = pieData
        }
    }
}