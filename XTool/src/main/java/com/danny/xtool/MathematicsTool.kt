/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool

import java.lang.RuntimeException

fun <T : Comparable<T>> max(vararg number: T) : T {
    if (number.isEmpty()) throw RuntimeException("Params can not be empty!")
    var max = number[0]
    for(num in number) {
        if (num > max) {
            max = num
        }
    }
    return max
}

fun <T : Comparable<T>> min(vararg number: T) : T {
    if (number.isEmpty()) throw RuntimeException("Params can not be empty!")
    var min = number[0]
    for(num in number) {
        if (num > min) {
            min = num
        }
    }
    return min
}
