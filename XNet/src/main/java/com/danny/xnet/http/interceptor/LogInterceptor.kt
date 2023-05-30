/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xnet.http.interceptor

import okhttp3.*
import okhttp3.internal.platform.Platform
import okio.Buffer
import java.io.EOFException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.util.concurrent.TimeUnit

/**
 * 日志拦截器
 *
 * @author x
 * @since 2023-05-30
 */
class LogInterceptor: Interceptor {
    companion object{
        val UTF8 = Charset.forName("UTF-8")
    }
    private var mLevel = Level.BODY
    private val mLog: XLogger = object : XLogger {
        override fun log(msg: String) {
            Platform.get().log(5, msg, null)
        }
    }

    fun setLevel(level: Level?) {
        mLevel = level?: Level.NONE
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val level = mLevel
        val request = chain.request()
        if (level == Level.NONE) {
            return chain.proceed(request)
        }

        val isLogBody = mLevel == Level.BODY
        val isLogHeaders = isLogBody || mLevel == Level.HEADERS
        val requestBody = request.body
        val hasRequestBody = requestBody != null
        val requestMsg = StringBuilder("${request.method} ${request.url}")
        if (!isLogHeaders && hasRequestBody) {
            requestMsg.append(" (${requestBody?.contentLength()}-byte body)")
        }
        mLog.log("------request start------")
        mLog.log(requestMsg.toString())
        if (isLogHeaders) {
            if (hasRequestBody) {
                requestBody?.contentType()?.let {
                    mLog.log("Content-Type: $it")
                }
                if ((requestBody?.contentLength()?: -1L) != -1L) {
                    mLog.log("Content-Length: ${requestBody?.contentLength()}")
                }
            }

            val headers = request.headers
            for (i in 0 until headers.size) {
                val name = headers.name(i)
                if (skipHeaders(name)) {
                    mLog.log("$name: ${headers.value(i)}")
                }
            }

            if (!isLogBody || !hasRequestBody) {

            } else if (bodyEncoded(request.headers)) {

            } else if (request.body is MultipartBody) {
                mLog.log("content: large bytes, ignored")
            } else {
                val buffer = Buffer()
                requestBody?.writeTo(buffer)

                val charset = UTF8
                val contentType = requestBody?.contentType()
                contentType?.let {
                    it.charset(charset)
                }
                mLog.log("content: ${buffer.readString(charset)}")
            }
            mLog.log("------request end------")
        }

        val startNs = System.nanoTime()
        val response: Response? =
            try {
                mLog.log("------response start------")
                chain.proceed(request)
            } catch (e: Exception) {
                mLog.log("HTTPS FAILED: $e")
                mLog.log("------response end------")
                null
            }
        response?:return chain.proceed(request)
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        val responseBody = response.body
        val contentLength = requestBody?.contentLength()
        mLog.log(protocol(response.protocol) + " " + response.request.url)
        mLog.log("info: code: ${response.code} result: ${response.message} time: $tookMs ms")

        if (isLogHeaders) {
            if (!isLogBody || !hasBody(response)) {

            } else if (bodyEncoded(request.headers)) {

            } else {
                val source = responseBody!!.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source.buffer()
                var charset: Charset? = UTF8
                val contentType = responseBody.contentType()
                if (contentType != null) {
                    try {
                        charset = contentType.charset(UTF8)
                    } catch (e: UnsupportedCharsetException) {
                        mLog.log("")
                        mLog.log("Couldn't decode the response body; charset is likely " + "malformed.")
                        mLog.log("----------------response end-----------------")
                        return response
                    }
                }

                if (!isPlaintext(buffer)) {
                    mLog.log("")
                    mLog.log("----------------response end-----------------")
                    return response
                }

                if (contentLength != 0L) {
                    mLog.log("")
                    mLog.log("content: " + buffer.clone().readString(charset!!))
                }
            }
        }
        mLog.log("------response end------")
        return response
    }

    fun hasBody(response: Response): Boolean {
        return if (response.request.method == "HEAD") {
            false
        } else {
            val responseCode = response.code
            if ((responseCode < 100 || responseCode >= 200) && responseCode != 204 && responseCode != 304) {
                true
            } else {
                contentLength(response) != -1L || "chunked".equals(
                    response.header("Transfer-Encoding"),
                    ignoreCase = true
                )
            }
        }
    }

    fun contentLength(response: Response): Long {
        return contentLength(response.headers)
    }

    fun contentLength(headers: Headers): Long {
        return stringToLong(headers["Content-Length"])
    }

    private fun stringToLong(s: String?): Long{
        if (s == null){
            return -1L
        } else {
            try {
                return s.toLong()
            }catch (  var2: java.lang.NumberFormatException){
                return -1L
            }
        }
    }

    private fun protocol(protocol: Protocol): String {
        return if (protocol == Protocol.HTTP_1_0) "HTTP/1.0" else "HTTP/1.1"
    }

    private fun skipHeaders(name: String): Boolean {
        return !"Content-Type".equals(name, ignoreCase = true)
                && !"Content-Length".equals(name, ignoreCase = true)
                && !"Host".equals(name, ignoreCase = true)
                && !"Accept-Encoding".equals(name, ignoreCase = true)
                && !"User-Agent".equals(name, ignoreCase = true)
                && !"Connection".equals(name, ignoreCase = true)
    }

    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"]
        return contentEncoding != null
                && !contentEncoding.equals("identity", ignoreCase = true)
    }

    /**
     * Returns true if the body in question probably contains human readable text.
     * Uses a small * sample * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    fun isPlaintext(buffer: Buffer): Boolean {
        return try {
            val prefix = Buffer()
            val byteCount = if (buffer.size < 64) buffer.size else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            true
        } catch (e: EOFException) {
            false // Truncated UTF-8 sequence.
        }
    }

    interface XLogger{
        fun log(msg: String)
    }

    enum class Level{
        /**
         * No logs.
         */
        NONE,

        /**
         * 记录请求和响应行。
         * 示例：
         * {@code * --> POST /greeting HTTP/1.1 (3-byte body) * * <-- HTTP/1.1 200 OK (22ms, 6-byte body) * }
         */
        BASIC,

        /**
         * 记录请求和响应行及其各自的标头。
         * 示例：
         * {@code * --> POST /greeting HTTP/1.1 * Host: example.com * Content-Type: plain/text * Content-Length: 3 * --> END POST * * <-- HTTP/1.1 200 OK (22ms) * Content-Type: plain/text * Content-Length: 6 * <-- END HTTP * }
         */
        HEADERS,

        /**
         * 记录请求和响应行及其各自的标头和正文（如果存在）。
         * 示例：
         * {@code * --> POST /greeting HTTP/1.1 * Host: example.com * Content-Type: plain/text * Content-Length: 3 * * Hi? * --> END GET * * <-- HTTP/1.1 200 OK (22ms) * Content-Type: plain/text * Content-Length: 6 * * Hello! * <-- END HTTP * }
         */
        BODY
    }
}
