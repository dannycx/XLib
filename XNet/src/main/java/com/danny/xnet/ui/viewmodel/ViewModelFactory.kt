/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xnet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.danny.xnet.api.ApiTool
import com.danny.xnet.repository.ApiRepository

/**
 * viewModel
 *
 * @author x
 * @since 2023-05-31
 */
class ViewModelFactory(private val apiHelper: ApiTool) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(ApiRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}
