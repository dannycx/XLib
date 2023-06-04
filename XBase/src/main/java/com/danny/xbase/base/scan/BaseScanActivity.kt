package com.danny.xbase.base.scan

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.danny.xbase.base.BaseActivity

/**
 * 扫描基类
 */
class BaseScanActivity: BaseActivity() {
    private val scanBroadcast by lazy { ScanBroadcast(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scanBroadcast.setScanCode(MutableLiveData<String>())
        scanBroadcast.registerReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        scanBroadcast.unregisterReceiver()
    }
}
