/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool.shotscreen

import android.content.Context
import android.view.View

interface ViewCallback {
    fun onAdd(context: Context)

    fun onClick(view: View)
}
