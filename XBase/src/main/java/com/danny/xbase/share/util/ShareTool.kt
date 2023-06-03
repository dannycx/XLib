/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.share.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import com.danny.xtool.share.bean.ShareData
import com.danny.xtool.share.bean.ShareTarget
import com.danny.xtool.share.bean.ShareType
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXImageObject
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject
import com.tencent.mm.opensdk.modelmsg.WXMusicObject
import com.tencent.mm.opensdk.modelmsg.WXTextObject
import com.tencent.mm.opensdk.modelmsg.WXVideoObject
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * 微信分享
 *
 * @author danny
 * @since 2023-05-28
 */
object ShareTool {
    private const val transactionTxt = "transaction_txt"
    private const val transactionPic = "transaction_pic"
    private const val transactionMusic = "transaction_music"
    private const val transactionVideo = "transaction_video"
    private const val transactionWeb = "transaction_web"
    private const val transactionMini = "transaction_mini"

    private lateinit var iWxApi: IWXAPI
    private lateinit var receiver: BroadcastReceiver

    fun register(context: Context, appId: String) {
        iWxApi = WXAPIFactory.createWXAPI(context, appId, true)
        iWxApi.registerApp(appId)
        receiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                iWxApi.registerApp(appId)
            }
        }
        context.registerReceiver(receiver, IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP), "com.danny.permission.share", null)
    }

    fun unregister(context: Context) {
        if (::iWxApi.isLateinit) {
            iWxApi.unregisterApp()
        }

        if (::receiver.isLateinit) {
            context.unregisterReceiver(receiver)
        }
    }

    fun share(context: Context, data: ShareData?) {
        if (data == null) {
            return
        }
        if (!iWxApi.isWXAppInstalled) {
            Toast.makeText(context, "未安装微信,请下载安装", Toast.LENGTH_SHORT).show()
        }

        when(data.shareType) {
            ShareType.TXT -> shareTxt(data)
            ShareType.IMG -> shareImg(data)
            ShareType.MUSIC -> shareMusic(data)
            ShareType.VIDEO -> shareVideo(data)
            ShareType.WEB -> shareWeb(data)
            else -> {}
//            XShareType.MINI -> shareMini()
        }
    }

    /**
     * 分享文本
     */
    private fun shareTxt(data: ShareData) {
        val txtObject = WXTextObject()
        txtObject.text = data.description

        val msg = WXMediaMessage()
        msg.description = data.description
        msg.mediaObject = txtObject

        val req = SendMessageToWX.Req()
        req.message = msg
        req.transaction = transactionTxt
        req.scene = when(data.shareTarget) {
            ShareTarget.WX_SESSION -> SendMessageToWX.Req.WXSceneSession
            ShareTarget.WX_TIME_LINE -> SendMessageToWX.Req.WXSceneTimeline
            ShareTarget.WX_FAVORITE -> SendMessageToWX.Req.WXSceneFavorite
        }

        iWxApi.sendReq(req)
    }

    private fun shareImg(data: ShareData) {
        var bitmap: Bitmap? = null
        if (null != data.bitmapThumb) {
            bitmap = BitmapFactory.decodeByteArray(data.bitmapThumb, 0, data.bitmapThumb!!.size)
        }

        val imgObject = WXImageObject(bitmap)
        val msg = WXMediaMessage()
        msg.thumbData = data.bitmapThumb
        msg.mediaObject = imgObject

        val req = getReq(msg, transactionPic, "openId", data)
        iWxApi.sendReq(req)
    }

    private fun shareMusic(data: ShareData) {
        val musicObject = WXMusicObject()
        musicObject.musicUrl = data.musicUrl

        val msg = WXMediaMessage()
        msg.mediaObject = musicObject
        msg.title = data.title
        msg.description = data.description
        msg.thumbData = data.bitmapThumb

        val req = getReq(msg, transactionMusic, "openId", data)
        iWxApi.sendReq(req)
    }

    private fun shareVideo(data: ShareData) {
        val videoObject = WXVideoObject()
        videoObject.videoUrl = data.videoUrl

        val msg = WXMediaMessage(videoObject)
        msg.title = data.title
        msg.description = data.description
        msg.thumbData = data.bitmapThumb

        val req = getReq(msg, transactionVideo, "openId", data)
        iWxApi.sendReq(req)
    }

    private fun shareWeb(data: ShareData) {
        val webPageObject = WXWebpageObject()
        webPageObject.webpageUrl = data.webpageUrl

        val msg = WXMediaMessage(webPageObject)
        msg.title = data.title
        msg.description = data.description
        msg.thumbData = data.bitmapThumb

        val req = getReq(msg, transactionWeb, "openId", data)
        iWxApi.sendReq(req)
    }

    private fun shareMini(webPage: String, miniId: String, path: String, title: String, description: String, bitmap: Bitmap) {
        val miniObject = WXMiniProgramObject()
        miniObject.webpageUrl = webPage // "http://www.qq.com"

        // 正式版:0，测试版:1，体验版:2
        miniObject.miniprogramType =  WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE
        miniObject.userName = miniId // 小程序原始id
        //小程序页面路径；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"
        miniObject.path = path

        val msg = WXMediaMessage(miniObject)
        msg.title = title // 小程序消息title
        msg.description = description // 小程序消息desc
//        msg.thumbData = getThumb() // 小程序消息封面图片，小于128k

        val req = SendMessageToWX.Req()
        req.scene = SendMessageToWX.Req.WXSceneSession
        req.message = msg
        req.transaction = transactionMini
        iWxApi.sendReq(req)
    }

    private fun getReq(msg: WXMediaMessage, action: String, openId: String, data: ShareData): SendMessageToWX.Req = SendMessageToWX.Req().apply {
        val req = SendMessageToWX.Req()
        req.message = msg
        req.transaction = action
        req.userOpenId = openId
        req.scene = when(data.shareTarget) {
            ShareTarget.WX_SESSION -> SendMessageToWX.Req.WXSceneSession
            ShareTarget.WX_TIME_LINE -> SendMessageToWX.Req.WXSceneTimeline
            ShareTarget.WX_FAVORITE -> SendMessageToWX.Req.WXSceneFavorite
        }
    }
}
