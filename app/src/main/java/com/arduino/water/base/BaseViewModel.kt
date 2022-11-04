package com.arduino.water.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel()  {

    fun <T : Any?> MutableLiveData<T>.default(initialValue: T?) = apply { setValue(initialValue) }
}