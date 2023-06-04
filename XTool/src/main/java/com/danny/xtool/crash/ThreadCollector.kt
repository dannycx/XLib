/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.crash

import java.lang.StringBuilder

/**
 * 线程信息收集
 *
 * @author x
 * @since 2023-06-04
 */
object ThreadCollector {
    fun collect(thread: Thread?): String {
        val result = StringBuilder()
        thread?.let {
            result.append("id = ").append(thread.id).append("\n")
            result.append("name = ").append(thread.name).append("\n")
            result.append("priority = ").append(thread.priority).append("\n")
            thread.threadGroup?.let {
                result.append("groupName = ").append(thread.threadGroup.name).append("\n")
            }
        }

        return result.toString()
    }
}
