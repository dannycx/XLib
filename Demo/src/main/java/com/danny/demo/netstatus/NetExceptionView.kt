package com.danny.demo.netstatus

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.danny.demo.databinding.NetStatusErrorBinding
import com.danny.xtool.net.NetType

class NetExceptionView(context: Context, attrSet: AttributeSet): FrameLayout(context, attrSet, -1),
    NetObserver {
    private val netBinding = NetStatusErrorBinding.inflate(LayoutInflater.from(context))

    init {
        addView(netBinding.root)
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        XNetStatusChangeReceiver.receiver.registerObserver(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        XNetStatusChangeReceiver.receiver.unRegisterObserver(this)
    }

    override fun netDisconnected() {
        visibility = View.VISIBLE
    }

    override fun netConnected(type: NetType) {
        visibility = View.GONE
    }

}
