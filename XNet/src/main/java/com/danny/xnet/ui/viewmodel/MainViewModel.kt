/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xnet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.danny.xnet.repository.ApiRepository
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

/**
 * viewModel
 *
 * @author x
 * @since 2023-05-31
 */
class MainViewModel(private val apiRepository: ApiRepository) : ViewModel() {
    fun getTests() = liveData(Dispatchers.IO) {
        emit(com.danny.xnet.Result.loading(null))
        try {
            emit(com.danny.xnet.Result.success(apiRepository.getTests()))
        } catch (e: Exception) {
            emit(com.danny.xnet.Result.error(e.message, null))
        }
    }
}
