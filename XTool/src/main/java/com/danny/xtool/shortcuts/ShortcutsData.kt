/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.shortcuts

import android.graphics.Bitmap

class ShortcutsData {
    var id: String? = ""// 应用id
    var intentParams: Map<String, String>? = null// 启动activity参数
    var labelName: String? = ""// 桌面应用名称
    var bitmap: Bitmap? = null// 桌面应用图标
    var className: String? = ""// 启动activity
    var allowRepeat: Boolean = true// 允许重复
}
