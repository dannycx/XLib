/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool

import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.net.toFile
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * 文件工具
 *
 * @author danny
 * @since 2023-05-24
 */
object FileTool {
//    const val APK_PATH: String = getSDCardPath() + "x/apk/"
    /**
     * 获取sd卡路径
     */
    fun getSDCardPath(): String {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            return Environment.getExternalStorageDirectory().absolutePath + File.separator
        }
        return ""
    }

    /**
     * 创建目录
     */
    fun createDir(dir: String) {
        try {
            val file = File(dir)
            if (!file.exists()) {
                file.mkdirs()
            }
        } catch (e: IOException) {
            Log.e("", e.toString())
        }
    }

    /**
     * 删除目录
     */
    fun deleteFIle(file: File?) {
        file?:return
        if (!file.exists()) {
            return
        }
        if (file.isDirectory) {
            val files = file.listFiles()
            if (files != null) {
                for (f in files) {
                    deleteFIle(f)
                }
            }
        } else {
            file.delete()
        }
        file.delete()
    }
    
    fun space(size: Long): Boolean {
        return false
    }
    
    /**
     * uri转file
     */
    fun toFile(uri: Uri) = uri.toFile()

    /**
     * 是否小于指定大小
	 *     4M = 4 * 1024 * 1024
     */
    fun isLittleFile(uri: Uri, size: Long): Boolean {
        val file = toFile(uri)
        val fileInputStream = FileInputStream(file)
        val fileSizeInBytes = fileInputStream.channel.size()
        fileInputStream.close()
        return fileSizeInBytes < size
    }
}

/**
 * 升序
 */
class FileAscComparator: Comparator<File?> {
    override fun compare(o1: File?, o2: File?): Int {
        o1?: return 0
        o2?: return 0
        if (o1.isDirectory && o2.isDirectory) {
            // 文件夹
            return o1.name.compareTo(o2.name)
        }
        if (o1.isDirectory && o2.isFile) {
            return -1
        }
        if (o1.isFile && o2.isDirectory) {
            return 1
        }
        return o1.name.compareTo(o2.name)
    }
}

/**
 * 降序
 */
class FileDesComparator: Comparator<java.io.File?> {
    override fun compare(o1: java.io.File?, o2: java.io.File?): Int {
        o1?: return 0
        o2?: return 0
        if (o1.isDirectory && o2.isDirectory) {
            // 文件夹
            return o2.name.compareTo(o1.name)
        }
        if (o1.isDirectory && o2.isFile) {
            return 1
        }
        if (o1.isFile && o2.isDirectory) {
            return -1
        }
        return o2.name.compareTo(o1.name)
    }
}

/**
 * 升序
 */
class FileDateAscComparator: Comparator<File?> {
    override fun compare(o1: File?, o2: File?): Int {
        o1?: return 0
        o2?: return 0
        return o1.lastModified().compareTo(o2.lastModified())
    }
}

/**
 * 降序
 */
class FileDateDesComparator: Comparator<File?> {
    override fun compare(o1: File?, o2: File?): Int {
        o1?: return 0
        o2?: return 0
        return o2.lastModified().compareTo(o1.lastModified())
    }
}
