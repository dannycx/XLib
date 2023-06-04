package com.danny.xui.check

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.danny.xui.check.bean.CheckBean
import com.danny.xui.databinding.CheckViewBinding

/**
 * 水平复杂单选按钮
 */
class CheckView(var cnt: Context, attrSet: AttributeSet): FrameLayout(cnt, attrSet) {
    private val binding = CheckViewBinding.inflate(LayoutInflater.from(cnt), this, false)
    private lateinit var adapter: CheckAdapter
    private lateinit var itemClick: (code: String) -> Unit

    init {
        addView(binding.root)
        initRecycler()
    }

    private fun initRecycler() {
        val manager = LinearLayoutManager(cnt)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        binding.xCheckRecycler.layoutManager = manager
        binding.xCheckRecycler.setHasFixedSize(true)
        adapter = CheckAdapter {
            itemClick(it)
        }
        binding.xCheckRecycler.adapter = adapter
    }

    fun setData(items: ArrayList<ArrayList<CheckBean>>, supportCheck: Boolean, itemClick: (code: String) -> Unit) {
        adapter.setItems(items)
        adapter.setSupportCheck(supportCheck)
        this.itemClick = itemClick
    }
}
