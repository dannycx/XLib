package com.danny.xui.noticeview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *
 *
 * @author danny
 * @since 2020/12/25
 */
class NoticeViewModel: ViewModel() {
    val state: MutableLiveData<Int> = MutableLiveData()

    val click: MutableLiveData<IClick> = MutableLiveData()
}
