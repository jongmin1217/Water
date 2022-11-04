package com.arduino.water.main.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.arduino.water.R
import com.arduino.water.adapter.CalendarAdapter
import com.arduino.water.adapter.WeekAdapter
import com.arduino.water.base.BaseFragment
import com.arduino.water.databinding.FragmentCalendarBinding
import com.arduino.water.dialog.DetailUsingDialog
import com.arduino.water.dialog.DetailUsingViewModel
import com.arduino.water.model.WaterUsageData
import com.arduino.water.utils.BindAdapter

class CalendarFragment(
    private val data : ArrayList<WaterUsageData>
) : BaseFragment<FragmentCalendarBinding,CalendarViewModel>(R.layout.fragment_calendar) {
    override val viewModel by activityViewModels<CalendarViewModel>()
    private val dialogViewModel by lazy { DetailUsingViewModel() }

    private val calendarAdapter = CalendarAdapter{
        context?.let { context->
            DetailUsingDialog(context,viewLifecycleOwner,dialogViewModel,it).show()
        }
    }

    override fun initBinding() {
        binding.vm = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initListener()
        initAdapter()
        initData()
    }

    private fun initData(){
        viewModel.data = data
        viewModel.initCalendar()
    }

    fun changeData(data : ArrayList<WaterUsageData>){
        viewModel.data = data
        viewModel.initCalendar()
    }

    override fun initViewModelObserve() {
        super.initViewModelObserve()

        with(viewModel){
            initCalendar.observe(viewLifecycleOwner,{items->
                calendarAdapter.items = items
            })
        }
    }

    private fun initListener(){
        binding.ivBack.setOnClickListener {
            if(viewModel.selectMonth.value == 1){
                viewModel.selectMonth.value = 12
                viewModel.selectYear.value = viewModel.selectYear.value?.minus(1)
            }
            else viewModel.selectMonth.value = viewModel.selectMonth.value?.minus(1)

            viewModel.initCalendar()
        }

        binding.ivNext.setOnClickListener {
            if(viewModel.selectMonth.value == 12){
                viewModel.selectMonth.value = 1
                viewModel.selectYear.value = viewModel.selectYear.value?.plus(1)
            }
            else viewModel.selectMonth.value = viewModel.selectMonth.value?.plus(1)

            viewModel.initCalendar()
        }


    }

    private fun initAdapter(){
        val items = resources.getStringArray(R.array.week).toCollection(ArrayList<String>())
        val adapter = WeekAdapter()
        BindAdapter.weekAdapter(binding.recyclerviewWeek,adapter)
        adapter.items = items

        BindAdapter.calendarAdapter(binding.recyclerviewCalendar,calendarAdapter)
    }
}