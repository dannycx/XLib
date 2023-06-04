package com.danny.demo.home.baby.`fun`

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.danny.demo.databinding.CFragmentFunBinding
import com.danny.xbase.event.AppEvent
import com.danny.xbase.module.IEventBus
import com.danny.xbase.module.X
import org.json.JSONException
import org.json.JSONObject

class CFunFragment: Fragment() {
    private lateinit var funBinding: CFragmentFunBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        funBinding = CFragmentFunBinding.inflate(inflater, container, false)
        return funBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        funBinding.event.setOnClickListener {
            val jsonObject = JSONObject()
            try {
                jsonObject.putOpt("key", "event-key")
                jsonObject.putOpt("value", "event-value")
            } catch (e: JSONException) {

            }
            X.module(context, IEventBus::class.java).post(AppEvent(jsonObject))
        }

        X.module(context, IEventBus::class.java).on(AppEvent::class.java)?.observe(viewLifecycleOwner, Observer {
            val s = it?.data.toString()
            funBinding.eventContent.text = s
        })
    }
}