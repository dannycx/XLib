package com.danny.demo.netstatus

import com.danny.xtool.net.NetType

interface NetObserver {
    fun netDisconnected()
    fun netConnected(type: NetType)
}
