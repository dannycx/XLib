package com.danny.demo.service

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object XWorkUtil {
    fun exeXWork(context: Context) {
        // 单次运行的后台任务请求
        val request = OneTimeWorkRequest.Builder(XWorker::class.java)
            .setInitialDelay(5, TimeUnit.MINUTES)// 延迟5分钟
            .addTag("tag")// 添加标签,通过标签取消后台任务
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)// Result.retry(),10秒后执行,参数一再次失败策略,当前为线性方式延迟
            .build()
        // 周期性的后台任务请求(为降低设备性能消耗,建议不小于15分钟)
//        val request = PeriodicWorkRequest.Builder(XWorker::class.java
//            , 15, TimeUnit.MINUTES).build()

        WorkManager.getInstance(context).enqueue(request)

//        WorkManager.getInstance(context).cancelAllWorkByTag("tag")// 取消标签为tag的任务
//        WorkManager.getInstance(context).cancelWorkById(request.id)// 根据id取消任务
//        WorkManager.getInstance(context).cancelAllWork()// 取消所有任务
    }

    fun listener(context: Context, request: OneTimeWorkRequest, owner: LifecycleOwner) {
        WorkManager.getInstance(context).getWorkInfoByIdLiveData(request.id).observe(owner,
            Observer<WorkInfo> { workInfo ->
                if (workInfo?.state == WorkInfo.State.SUCCEEDED) {
                    println("success")
                } else if (workInfo?.state == WorkInfo.State.FAILED) {
                    println("failed")
                }
            })
    }

    // 先后执行任务,有先后顺序,一个失败了,后面的就不会执行
    fun moreTask(context: Context, sync: OneTimeWorkRequest, compress: OneTimeWorkRequest, upload: OneTimeWorkRequest) {
        WorkManager.getInstance(context)
            .beginWith(sync)// 开启一个链式任务
            .then(compress)
            .then(upload)
            .enqueue()
    }
}
