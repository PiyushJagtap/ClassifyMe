package com.piyushjagtap.classifyme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.reflect.KProperty

open class ImageViewModel : ViewModel() {
     private val mutableLiveData = MutableLiveData<String>()

    fun data(item: String) {
        mutableLiveData.value = item
    }

}