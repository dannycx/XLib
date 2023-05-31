/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xnet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.danny.xnet.data.Test
import com.danny.xnet.databinding.ItemLayoutMainBinding
import com.danny.xtool.LogTool

/**
 * 适配器
 *
 * @author x
 * @since 2023-05-31
 */
class MainAdapter(private val dataSources: ArrayList<Test>) :
    RecyclerView.Adapter<MainAdapter.DataViewHolder>() {
    class DataViewHolder(private val binding: ItemLayoutMainBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(test: Test) {
            binding.textViewUserName.text = test.author
            binding.textViewUserEmail.text = test.desc
            LogTool.i("url = ${test.url}")
            Glide.with(binding.imageViewAvatar.context)
                .load(test.url)
                .into(binding.imageViewAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            ItemLayoutMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )


    override fun getItemCount() = dataSources.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(dataSources[position])

    fun addData(tests: List<Test>) {
        dataSources.addAll(tests)
    }
}
