package com.danny.demo.home.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.danny.demo.R
import com.danny.demo.databinding.ActivityShareBinding
import com.danny.xbase.module.X
import com.danny.xshare.bean.ShareData
import com.danny.xshare.bean.ShareTarget
import com.danny.xshare.bean.ShareType
import com.danny.xshare.util.ShareTool

/**
 *
 *
 * @author danny
 * @since 2020/12/22
 */
class ShareActivity: AppCompatActivity() {
    private lateinit var shareBinding: ActivityShareBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shareBinding = DataBindingUtil.setContentView(this, R.layout.activity_share)

        X.share(this).registerWeChart(applicationContext, "appid")

        initListener()
    }

    private fun initListener() {
        shareBinding.txt.setOnClickListener {
            val data = ShareData()
            data.description = "小坏宝"
            data.shareType = ShareType.TXT
            data.shareTarget = ShareTarget.WX_SESSION
            ShareTool.share(this, data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        X.share(this).unregisterWeChart(this)
    }
}