package com.danny.xbase.base.scan

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent

class ScanBroadcast(private val cnt: Context): LifecycleObserver {
    private val receiver by lazy { XScanReceiver() }
    private lateinit var scanCodeData: MutableLiveData<String>

    companion object {
        fun initScanBroadcastReceiver(context: Context?) {
            val intent = Intent("com.android.scanner.service_settings").apply {
                putExtra("action_barcode_broadcast", "con.danny.x.scan.filter") // 自定义的广播filter
                putExtra("broadcast_send_mode", "BROADCAST") // 扫描设备接收方式:广播
                putExtra("key_barcode_broadcast", "scannerdata") // 东大设备接受data的key
                putExtra("key_barcode_broadcast", "com.cympol.datawedge.data_string") // 斑马设备接受data的key
                putExtra("endchar", "NONE") // 扫描设备结尾字符
            }
            context?.sendBroadcast(intent)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun registerReceiver() {
        val filter = IntentFilter("con.danny.x.scan.filter")
        cnt.registerReceiver(receiver, filter)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun unregisterReceiver() {
        cnt.unregisterReceiver(receiver)
    }

    inner class XScanReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.apply {
                val scanCode = getStringExtra("scannerdata") ?: ""
                when (scanCode.isNotEmpty()) {
                    true -> scanCodeData.value = scanCode
                    else -> scanCodeData.value = getStringExtra("com.cympol.datawedge.data_string")
                }
            }
        }
    }

    fun setScanCode(scanCode: MutableLiveData<String>) {
        this.scanCodeData = scanCode
    }
}
