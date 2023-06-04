package com.danny.demo.home.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.InputFilter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.danny.demo.R
import com.danny.demo.databinding.CActivityMainBinding
import com.danny.demo.home.home.CHomeFragment
import com.danny.demo.home.me.CMeFragment
import com.danny.demo.home.message.CMessageFragment
import com.danny.xbase.base.BaseActivity
import com.danny.xtool.UiTool
import com.danny.xtool.shortcuts.ShortcutsTool
import com.danny.xtool.softkey.SoftKeyChangedCallback
import com.danny.xtool.softkey.SoftKeyUtil
import com.danny.xui.bottombar.BottomBar
import com.danny.xui.dialog.SuspendView
import java.util.regex.Pattern

class CMainActivity: BaseActivity() {

    private lateinit var cBinding: CActivityMainBinding
    private lateinit var fragmentManager: FragmentManager
    private lateinit var currentFragment: Fragment
    private val emoji = Pattern.compile("[\u8d83c\udff0-\ud83d\uddff]|[\ud83e\udd00-\ud83e\uddff]|[\ud83d\ude00-\ud83d\ude4f]|[\u2600-\u27ff]", Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cBinding = DataBindingUtil.setContentView(this, R.layout.c_activity_main)
        initPager()
        tabSwitch()
        val sv = SuspendView(this)
        sv.init(true, 70, false, object : SuspendView.OnSuspendClickListener{
            override fun onSuspendClick() {
                TODO("Not yet implemented")
            }
        })
        sv.show()

//        cBinding.edit.showSoftInputOnFocus = false
//        cBinding.edit.customSelectionActionModeCallback

        val emojiFilter = InputFilter { source, _, _, _, _, _ ->
            val matcher = emoji.matcher(source)
            if (matcher.find()) {
                ""
            } else null
        }
        cBinding.edit.filters = arrayOf(emojiFilter)


//        val context = this
//        val s = SuspendView(context, false, 75, true,
//            SuspendView.OnWindowClickListener { })
//
//        s.show()

        val context = this
        SoftKeyUtil.startSoftKeyListener(this, object : SoftKeyChangedCallback {
            override fun softKeyHide(height: Int) {
                Toast.makeText(context, "hide-$height", Toast.LENGTH_SHORT).show()
            }

            override fun softKeyShow(height: Int) {
                Toast.makeText(context, "show-$height", Toast.LENGTH_SHORT).show()
            }
        })

//        val shotCapture = XShotScreenCapture(contentResolver)
//        shotCapture.startShotScreen(object : XShortScreenCallback {
//            override fun successCapture(filePath: String) {
//                val intent = Intent(context, ScreenShotWindow::class.java)
//                intent.putExtra("image_path", filePath)
//                startActivity(intent)
//            }
//        })
        ShortcutsTool.createShortcut(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }


    }

    /**
     * 初始化页面
     */
    private fun initPager() {
        fragmentManager = supportFragmentManager
        currentFragment = CHomeFragment()
        fragmentManager.beginTransaction().add(R.id.c_main_contains, currentFragment
            , "home").commitAllowingStateLoss()

    }

    /**
     * tab切换
     */
    private fun tabSwitch() {
        cBinding.cMainBottom.setListener(object : BottomBar.BottomListener {
            override fun home() {
                tabShow("home")
            }

            override fun message() {
                tabShow("message")
            }

            override fun me() {
                tabShow("me")
            }
        })
    }

    private fun tabShow(tag: String) {

        val intent = Intent(this, CMainActivity::class.java)
        hideFragment()
        var fragment = fragmentManager.findFragmentByTag(tag)
        if (null == fragment) {
            fragment = when(tag) {
                "home" -> CHomeFragment()
                "message" -> CMessageFragment()
                "me" -> CMeFragment()
                else -> CHomeFragment()
            }

            currentFragment = fragment
            fragmentManager.beginTransaction().add(R.id.c_main_contains, currentFragment, tag).commitAllowingStateLoss()
        } else {
            currentFragment = fragment
            fragmentManager.beginTransaction().show(currentFragment).commitAllowingStateLoss()
        }

    }

    private fun hideFragment() {
        fragmentManager.beginTransaction().hide(currentFragment).commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        UiTool.exitApp(this)
    }
}
