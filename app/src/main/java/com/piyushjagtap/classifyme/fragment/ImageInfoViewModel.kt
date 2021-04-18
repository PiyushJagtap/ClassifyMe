package com.piyushjagtap.classifyme.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class ImageInfoViewModel : ViewModel() {
    private val text = MutableLiveData<CharSequence>()
    fun setText(input: CharSequence) {
        text.value = input
    }

    fun getText(): LiveData<CharSequence>? {
        return text
    }
}