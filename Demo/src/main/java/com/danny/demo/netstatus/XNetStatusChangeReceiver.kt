package com.danny.demo.netstatus

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.danny.xtool.net.NetTool
import com.danny.xtool.net.NetType

class XNetStatusChangeReceiver: BroadcastReceiver() {
    companion object {
        val receiver = XNetStatusChangeReceiver()
    }

    private val observers = ArrayList<NetObserver>()

    override fun onReceive(context: Context?, intent: Intent?) {
        when (ConnectivityManager.CONNECTIVITY_ACTION) {
            intent?.action -> {
                context?.let {
                    val type = NetTool.getNetType(context) as NetType
                    notifyObserver(type)
                }
            }
        }
    }

    private fun notifyObserver(type: NetType) {
        observers.apply {
            when (type) {
                NetType.NETWORK_NO -> {
                    for (i in indices) {
                        observers[i].netDisconnected()
                    }
                }
                else -> {
                    for (i in indices) {
                        observers[i].netConnected(type)
                    }
                }
            }
        }
    }

    fun register(context: Context) {
        val filter =  IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(receiver, filter)
    }

    fun unregister(context: Context) {
        context.unregisterReceiver(receiver)
    }

    fun registerObserver(observer: NetObserver?) {
        observer?.let {
            if (!observers.contains(it)) {
                observers.add(it)
            }
        }
    }

    fun unRegisterObserver(observer: NetObserver?) {
        observer?.let {
            receiver.observers.remove(it)
        }
    }

}
