package com.danny.xui.table

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.danny.xtool.UiTool
import com.danny.xui.databinding.ItemTableBinding
import com.danny.xui.table.bean.TableBean

/**
 * 表格adapter
 */
class TableAdapter: RecyclerView.Adapter<TableAdapter.Holder> () {
    private var item: ArrayList<TableBean>? = null
    private var spanCount = 5

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemTableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int = item?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        item?.get(position)?.let {
            holder.setData(it, position)
        }
    }

    fun setItem(item: ArrayList<TableBean>, spanCount: Int) {
        this.item = item
        this.spanCount = spanCount
        notifyDataSetChanged()
    }

    inner class Holder(private val binding: ItemTableBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(bean: TableBean, position: Int) {
            binding.tableKey.text = bean.key
            binding.tableValue.text = bean.value
            val lastCol = ((position + 1) % spanCount == 0)
            if (lastCol || (position == itemCount - 1)) {
                binding.tableR.visibility = View.VISIBLE
            } else {
                binding.tableR.visibility = View.GONE
            }

            val lastRow = (itemCount - 1 - (itemCount - 1) % spanCount) // 最后一行起始位置
            val mod = spanCount - itemCount % spanCount
            val array = Array<Int>(mod) {0}
            if (mod != 0 && mod != spanCount) {
                for (i in (1 .. mod)) {
                    array[i - 1] = lastRow - i
                }
            }
            if (lastRow <= position) {
                binding.tableB.visibility = View.VISIBLE
            } else {
                if (mod != spanCount && position in array) {
                    binding.tableB.visibility = View.VISIBLE
                } else {
                    binding.tableB.visibility = View.GONE
                }
            }

            binding.root.setBackgroundResource(UiTool.drawableId(binding.root.context, bean.bgRes))
        }
    }

}
