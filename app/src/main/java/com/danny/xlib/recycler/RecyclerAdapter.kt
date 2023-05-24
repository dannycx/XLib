/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xlib.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.danny.xlib.R

/**
 * RecyclerView适配器
 *
 * @author danny
 * @since 2023-05-24
 */
class RecyclerAdapter(private var list : List<String>, private var context : Context,
    val click: (str: String) -> Unit) : RecyclerView.Adapter<RecyclerAdapter.Holder>() {
    override fun onCreateViewHolder(parent : ViewGroup, position: Int): Holder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_recycler, parent, false)
        return Holder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tv.text = list[position]
        holder.tv.tag = position
        holder.itemView.setOnClickListener {
            holder.tv.setTextColor(ContextCompat.getColor(context, R.color.purple_500))
        }
    }


    class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var tv = itemView.findViewById<TextView>(R.id.tv_item)
    }
}
