package com.danny.xnet

import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.*
import kotlin.math.roundToInt

class RetrofitTool {
    companion object{
        const val NORMAL_FILE_LIMIT = 60 * 1024 * 1024
    }

    private val mBuilder: Retrofit.Builder? = null
    private val mOkHttpClient: OkHttpClient.Builder? = null
    private var mDefaultPath: File? = null
    private val mDefaultFolder = "x"

    fun <T> createService(serviceClass: Class<T>, url: String): T {
        mBuilder?: Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
        mOkHttpClient?: OkHttpClient.Builder()
        mBuilder?.baseUrl(url)
        val retrofit = mBuilder!!.client(mOkHttpClient!!.build()).build()
        return retrofit.create(serviceClass)
    }

    /**
     * 文件上传
     *
     * @param token token
     * @param url url
     * @param body 请求体
     * @param listener 回调
     */
    fun upload(token: String?, url: String?, body: RequestBody?, listener: (code: Int) -> Unit) {
        token?:return
        url?:return
        body?:return

        val service = createService(RetroApi::class.java, url.substring(0, url.lastIndexOf("/")) + "/")
        val call = service.uploadToOneDrive(url, String.format("Bearer %s", token), body) as retrofit2.Call<ResponseBody>
        call.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: retrofit2.Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    listener(-1)
                    return
                }
                listener(-1)
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                Log.e("", t.message?:"")
                listener(-1)
            }
        })
    }

    /**
     * 下载
     *
     * @param token token
     * @param url url
     * @param customPath 自定义下载路径
     * @param fileName 文件名
     * @param listener 回调
     */
    fun download(token: String?, url: String?, customPath: File?, fileName: String?, size: Long,
        listener: (file: File?) -> Unit) {
        token?:return
        url?:return
        if (fileName.isNullOrEmpty()) return
        var realName = fileName
        if (customPath != null && !customPath.exists() && !customPath.mkdirs()) return
        initDefaultPath()
        var file = File(customPath?:mDefaultPath, fileName)
        if (file.exists()) {
            realName = generateUniqueName(customPath?:mDefaultPath, fileName)
            realName?.let { 
                file = File(customPath?:mDefaultPath, it)
            }
        }
        val service = createService(RetroApi::class.java, url.substring(0, url.lastIndexOf("/")) + "/")
        if (size < NORMAL_FILE_LIMIT) {
            val call = service.downloadFromOneDrive(url, String.format("Bearer %s", token))
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        outputFile(response.body(), file, listener)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("", "e: ${t.message}")
                }
            })
        } else {
            val call = service.downloadLargeFile(url, String.format("Bearer %s", token))
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        // 开启线程写入
                        outputFile(response.body(), file, listener)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("", "e: ${t.message}")
                }
            })
        }
    }

    private fun outputFile(body: ResponseBody?, file: File, listener: (file: File?) -> Unit) {
        body?:return
        var result = false
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        val bytes = ByteArray(4096)
        val contentLength = body.contentLength()
        var downloadSize: Long = 0
        try {
            inputStream = body.byteStream()
            outputStream = FileOutputStream(file)
            var lastPre = -1
            while (true) {
                val read = inputStream.read(bytes)
                if (read == -1) {
                    break
                }
                outputStream.write(bytes, 0, read)
                downloadSize += read
                val percent = ((downloadSize / contentLength).toDouble() * 100).roundToInt()
                if (lastPre != percent) {
                    lastPre = percent
                }
            }
            outputStream.flush()
            result = true
        } catch (e: IOException) {
            Log.e("", "e: $e")
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
        listener(if (result) file else null)
    }

    private fun generateUniqueName(file: File?, fileName: String): String? {
        file?:return null
        val period = fileName.lastIndexOf('.')
        var counter = 1
        val prefix = fileName.substring(0, period)
        val suffix = fileName.substring(period)
        var newName = "$prefix($counter)$suffix"
        var newFile = File(file, newName)
        while (newFile.exists()) {
            counter++
            newName = "$prefix($counter)$suffix"
            newFile = File(file, newName)
        }
        return newName
    }

    private fun initDefaultPath(): File {
        if (mDefaultPath == null) {
            mDefaultPath = File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS),
                mDefaultFolder + File.separator)
            if (mDefaultPath?.exists() == false && mDefaultPath?.mkdirs() == false) {
                mDefaultPath = Environment.getExternalStorageDirectory()
            }
        }
        return mDefaultPath!!
    }

    interface RetroApi {
        @PUT
        fun uploadToOneDrive(@Url url: String, @Header("Authorization") auth: String, @Body body: RequestBody):
            Callback<ResponseBody>

        @GET
        fun downloadFromOneDrive(@Url url: String, @Header("Authorization") auth: String): Call<ResponseBody>

        @Streaming
        @GET
        fun downloadLargeFile(@Url url: String, @Header("Authorization") auth: String): Call<ResponseBody>

    }
}
