/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.share

import android.content.Context
import com.danny.xbase.module.IShare
import com.danny.xtool.share.util.ShareTool

/**
 * 分享实现类
 *
 * @author danny
 * @since 2020/12/21
 */
class ShareImpl(): IShare {
    constructor(context: Context): this()

    override fun registerWeChart(context: Context, appId: String) {
        ShareTool.register(context, "")
    }

    override fun unregisterWeChart(context: Context) {
        ShareTool.unregister(context)
    }
}
