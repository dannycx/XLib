/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXImageObject
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXMusicObject
import com.tencent.mm.opensdk.modelmsg.WXVideoObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * 微信分享
 *
 * @author danny
 * @since 2020-11-12
 */
object XShareUtil {
    private var iWxApi: IWXAPI? = null
    private var cnt: Context? = null

    // 替换为微信注册的appId
    private const val X_APP_ID = "app_id"
    private const val OPEN_ID = ""

    // 事件类型
    private const val TEXT_TYPE = "transaction_text"
    private const val IMAGE_TYPE = "transaction_image"
    private const val VIDEO_TYPE = "transaction_video"
    private const val AUDIO_TYPE = "transaction_audio"
    private const val WEB_TYPE = "transaction_web"

    fun registerWeChat(context: Context) {
        cnt = context
        iWxApi = WXAPIFactory.createWXAPI(context, X_APP_ID, true)
        iWxApi?.registerApp(X_APP_ID)
    }

    /**
     * 分享图片
     */
    fun imageShare(drawable: Drawable, filePath: String) {
//        val bmp = BitmapFactory.decodeResource(cnt!!.resources, R.drawable.send_img)
        val bitmap = BitmapFactory.decodeFile(filePath)
        //初始化 WXImageObject 和 WXMediaMessage 对象
        val imgObj = WXImageObject(bitmap)

        val msg = WXMediaMessage()
        msg.mediaObject = imgObj

        //设置缩略图
//        val thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true)
//        bitmap.recycle()
//        msg.thumbData = Util.bmpToByteArray(thumbBmp, true)

        // 发送数据到微信
        val req = setRequest(msg, IMAGE_TYPE)
        iWxApi?.sendReq(req)
    }

    /**
     * 视频分享
     */
    fun videoShare(url: String) {
        //初始化一个WXVideoObject，填写url
        val video = WXVideoObject()
        video.videoUrl = url

        //用 WXVideoObject 对象初始化一个 WXMediaMessage 对象
        val msg = WXMediaMessage(video)
        msg.title = "视频标题"
        msg.description = "视频描述"
//        val thumbBmp = BitmapFactory.decodeResource(cnt!!.resources, R.drawable.send_music_thumb)
//        msg.thumbData = Util.bmpToByteArray(thumbBmp, true)

        // 发送数据到微信
        val req = setRequest(msg, VIDEO_TYPE)
        iWxApi?.sendReq(req)
    }

    /**
     * 音乐分享
     */
    fun audioShare(url: String) {
        //初始化一个WXMusicObject，填写url
        val music = WXMusicObject()
        music.musicUrl = url

        //用 WXMusicObject 对象初始化一个 WXMediaMessage 对象
        val msg = WXMediaMessage()
        msg.mediaObject = music
        msg.title = "音乐标题"
        msg.description = "音乐描述"
//        val thumbBmp = BitmapFactory.decodeResource(cnt!!.resources, R.drawable.send_music_thumb)

        //设置音乐缩略图
//        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        // 发送数据到微信
        val req = setRequest(msg, AUDIO_TYPE)
        iWxApi?.sendReq(req);
    }


    /**
     * 分享到对话:SendMessageToWX.Req.WXSceneSession
     * 分享到朋友圈:SendMessageToWX.Req.WXSceneTimeline
     * 分享到收藏:SendMessageToWX.Req.WXSceneFavorite
     */
    private fun setRequest(msg: WXMediaMessage, transactionType: String): SendMessageToWX.Req {
        val req = SendMessageToWX.Req()
        req.transaction = transactionType
        req.message = msg
        req.scene = SendMessageToWX.Req.WXSceneSession
        req.userOpenId = OPEN_ID
        return req
    }
}
