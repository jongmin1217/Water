package com.arduino.water.intro

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.HandlerCompat.postDelayed
import com.arduino.water.R
import com.arduino.water.base.BaseActivity
import com.arduino.water.databinding.ActivityIntroBinding
import com.arduino.water.main.MainActivity

class IntroActivity : BaseActivity<ActivityIntroBinding,IntroViewModel>(R.layout.activity_intro) {
    override val viewModel by viewModels<IntroViewModel>()

    override fun initBinding() {
        binding.vm = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.ivIntro.run {
            postDelayed({
                startActivity(Intent(this@IntroActivity,MainActivity::class.java))
                finish()
            }, 1500)
        }

    }
}