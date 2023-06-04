package com.danny.demo.home.me

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.danny.demo.databinding.CFragmentMeBinding
import com.danny.demo.filepicker.view.FilePickerActivity
import com.danny.demo.home.demo.FunnelActivity
import com.danny.demo.home.demo.ShareActivity
import com.danny.xcamera.CameraActivity

class CMeFragment : Fragment() {
    private lateinit var cMeBinding: CFragmentMeBinding

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cMeBinding = CFragmentMeBinding.inflate(inflater)
        initView()
        return cMeBinding.root
    }

    private fun initView() {
        cMeBinding.file.setOnClickListener{
            it.context.startActivity(
                Intent(it.context, FilePickerActivity::class.java)
            )
        }

        cMeBinding.takePhoto.setOnClickListener {
            it.context.startActivity(
                    Intent(it.context, CameraActivity::class.java)
            )
        }

        cMeBinding.funnelView.setOnClickListener {
            it.context.startActivity(
                    Intent(it.context, FunnelActivity::class.java)
            )
        }

        cMeBinding.shareView.setOnClickListener {
            it.context.startActivity(
                    Intent(it.context, ShareActivity::class.java)
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
