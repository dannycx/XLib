package com.danny.xbase.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BaseViewModel: ViewModel() {
    val liveData by lazy { MutableLiveData<String>() }
}
