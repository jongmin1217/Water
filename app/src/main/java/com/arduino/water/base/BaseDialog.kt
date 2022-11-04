package com.arduino.water.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.arduino.water.R

abstract class BaseDialog<T : ViewDataBinding>(@LayoutRes val layoutId: Int, private val mContext : Context, private val lifecycle : LifecycleOwner) :
    Dialog(mContext, R.style.msg_dialog_style) {

    abstract fun initBinding()
    lateinit var binding : T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), layoutId, null, false)
        setContentView(binding.root)

        binding.lifecycleOwner = lifecycle


        initBinding()
        setCanceledOnTouchOutside(false)
    }


}