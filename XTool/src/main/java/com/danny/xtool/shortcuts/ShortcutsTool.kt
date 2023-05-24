/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.shortcuts

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * 创建快捷方式
 *
 * @author x
 * @since 2023-05-25
 */
object ShortcutsTool {
    fun createShortcut(context: Context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            val shortcutManager = context.getSystemService(ShortcutManager::class.java)
            val shortcutInfo = mutableListOf<ShortcutInfo>() as ArrayList
            shortcutInfo.add(
                generateShortcut(context, "appId", "page=2"
                    , "插件", android.R.drawable.ic_menu_edit)
            )
            shortcutManager.dynamicShortcuts = shortcutInfo
        }
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun generateShortcut(context: Context, appId: String , params: String, label: String, iconRes: Int): ShortcutInfo {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        return ShortcutInfo.Builder(context, appId + params)
            .setLongLabel(label)
            .setShortLabel(label)
            .setIcon(Icon.createWithResource(context, iconRes))
            .setIntent(intent)
            .build()
    }
}
