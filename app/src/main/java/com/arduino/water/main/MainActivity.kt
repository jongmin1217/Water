package com.arduino.water.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.arduino.water.R
import com.arduino.water.base.BaseActivity
import com.arduino.water.databinding.ActivityMainBinding
import com.arduino.water.intro.IntroViewModel
import com.arduino.water.main.fragment.CalendarFragment
import com.arduino.water.main.fragment.HomeFragment
import com.arduino.water.model.WaterUsageData
import com.arduino.water.utils.Utils
import com.google.firebase.database.*
import com.google.gson.Gson
import timber.log.Timber
import kotlin.random.Random

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {
    override val viewModel by viewModels<MainViewModel>()

    private val homeFragment = HomeFragment {
        calendarFragment?.viewModel?.initCalendar()
    }
    private var calendarFragment: CalendarFragment? = null

    var list = ArrayList<WaterUsageData>()

    override fun initBinding() {
        binding.vm = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, homeFragment)
            .commit()
        initListener()

        viewModel.getWaterUsageData()
    }

    override fun initViewModelObserve() {
        super.initViewModelObserve()

        with(viewModel) {
            initData.observe(binding.lifecycleOwner!!, { data ->

                list = data

                val nowData =
                    list.filter { item -> item.year == Utils.getYear() && item.month == Utils.getMonth() && item.day == Utils.getDay() }

                val sendData = if(nowData.isEmpty()){
                    WaterUsageData(
                        Utils.getYear(),
                        Utils.getMonth(),
                        Utils.getDay(),
                        0F,
                        0F,
                        0F
                    )
                }else nowData[0]

                homeFragment.initChart(sendData)
                homeFragment.initLayout(sendData)
                calendarFragment?.changeData(data)

            })
        }
    }

    private fun initListener() {
        binding.bottomNavMain.run {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.bottom_nav_home -> {
                        changeFragment(homeFragment)
                    }
                    R.id.bottom_nav_calendar -> {
                        if (calendarFragment == null) {
                            calendarFragment = CalendarFragment(list)
                            addFragment(calendarFragment!!)
                        } else changeFragment(calendarFragment!!)
                    }
                }
                true
            }
            selectedItemId = R.id.bottom_nav_home
        }
    }

    private fun changeFragment(newFragment: Fragment) {
        if (!newFragment.isVisible) {
            hideFragment()
            supportFragmentManager.beginTransaction().show(newFragment).commit()
        }
    }

    private fun addFragment(newFragment: Fragment) {
        hideFragment()
        supportFragmentManager.beginTransaction().add(binding.frameLayout.id, newFragment).commit()
    }

    private fun hideFragment() {
        for (fragment: Fragment in supportFragmentManager.fragments) {
            if (fragment.isVisible) {
                supportFragmentManager.beginTransaction().hide(fragment).commit()
            }
        }
    }


}

