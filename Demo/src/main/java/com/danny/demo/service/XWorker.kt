package com.danny.demo.service

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * WorkerManager
 * 1.定义一个后台任务,继承Worker,并实现doWork
 * 2.配置后台任务运行条件和约束信息,并构建后台任务请求
 * 3.将后台任务请求传入WorkerManager的enqueue()方法,系统会在合适时间运行
 */
class XWorker(private val context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        return Result.success()
    }
}