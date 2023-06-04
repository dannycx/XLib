package com.danny.xui.table

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.danny.xui.databinding.TableRecyclerBinding
import com.danny.xui.table.bean.XTableBean

/**
 * RecyclerView流式布局实现表格
 */
class XTableView(var cnt: Context, attrSet: AttributeSet? = null): FrameLayout(cnt, attrSet) {
    private var binding = TableRecyclerBinding.inflate(LayoutInflater.from(cnt), this, false)
    private var spanCont = 5
    private val adapter by lazy { TableAdapter() }

    init {
        addView(binding.root)
        initRecycler()
    }

    private fun initRecycler() {
//        setPadding(10, 0, 10, 0)
//        val rv = RecyclerView(cnt)
        binding.tableRecycler.setHasFixedSize(true)
        binding.tableRecycler.layoutManager = StaggeredGridLayoutManager(spanCont
            , StaggeredGridLayoutManager.VERTICAL)
        binding.tableRecycler.adapter = adapter
//        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
//            , ViewGroup.LayoutParams.WRAP_CONTENT)
//        rv.layoutParams = lp
//        addView(rv)
    }

    fun setData(item: ArrayList<XTableBean>) {
        adapter.setItem(item, spanCont)
    }
}
