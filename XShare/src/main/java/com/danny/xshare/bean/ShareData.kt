/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xshare.bean

/**
 *
 *
 * @author danny
 * @since 2020/12/21
 */
class ShareData {
    var shareType: ShareType = ShareType.TXT
    var shareTarget: ShareTarget = ShareTarget.WX_SESSION
    var description: String? = ""
    var bitmapThumb: ByteArray? = ByteArray(0)

    var title: String? = null
    var musicUrl: String? = null
    var videoUrl: String? = null
    var webpageUrl: String? = null
}
