package com.danny.demo.filepicker.view

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.danny.demo.R
import com.danny.demo.databinding.ActivityImageBinding
import com.danny.demo.filepicker.FilePickerConstants
import com.danny.xtool.StatusBarTool
import com.danny.xtool.UiTool
import com.danny.xbase.base.BaseActivity
import java.io.File

/**
 * 打开图片
 *
 * @author danny
 * @since 2020-10-31
 */
class OpenImageActivity: BaseActivity() {
    private lateinit var imagePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.requestFeature(Window.FEATURE_NO_TITLE)

        val imageBinding = DataBindingUtil.setContentView<ActivityImageBinding>(this, R.layout.activity_image)

        StatusBarTool.setStatusBarColor(this, UiTool.getColor(this, com.danny.common.R.color.color_ffffff), true)

        val intent = intent
        intent?.let {
            imagePath = it.getStringExtra(FilePickerConstants.PREVIEW_IMAGE_KEY)?:""
        }

        if (::imagePath.isInitialized) {
            when (imagePath.isEmpty()) {
                true -> {Glide.with(this).load(R.mipmap.ic_launcher).into(imageBinding.xImage)}
                else -> {Glide.with(this).load(File(imagePath)).into(imageBinding.xImage)}
            }
        }
    }
}
