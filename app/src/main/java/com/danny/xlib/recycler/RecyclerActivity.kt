/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xlib.recycler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danny.xlib.R

/**
 * RecyclerView页面
 *
 * @author x
 * @since2023-05-24
 */
class RecyclerActivity : AppCompatActivity() {
    private var list = listOf(
        "1",
        "2",
        "3",
        "1",
        "2",
        "3",
        "1",
        "2",
        "3"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        var r = findViewById<RecyclerView>(R.id.recycler)
        r.layoutManager = LinearLayoutManager(this)
        r.adapter = RecyclerAdapter(list, this) {}
    }
}
