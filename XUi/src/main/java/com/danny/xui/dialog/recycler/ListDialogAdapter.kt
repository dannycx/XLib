package com.danny.xui.dialog.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.danny.xui.R
import com.danny.xui.check.bean.CheckBean
import com.danny.xui.databinding.DialogListItemBinding

class ListDialogAdapter(var item: ArrayList<CheckBean>?, var itemClick: (code: String, pos: Int) -> Unit): RecyclerView.Adapter<ListDialogAdapter.Holder>() {
    private lateinit var cnt: Context
    private var lastPos = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        cnt = parent.context
        val binding = DialogListItemBinding.inflate(LayoutInflater.from(cnt)
            , parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int = item?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        item?.get(position)?.let {
            holder.setData(it, position)
        }
    }

    fun setData(item: ArrayList<CheckBean>) {
        this.item = item
        notifyDataSetChanged()
    }

    inner class Holder(var binding: DialogListItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun setData(bean: CheckBean, position: Int) {
            binding.xListItemValue.text = bean.value
            when (bean.isChecked) {
                true -> binding.xListItemIv.setImageResource(R.drawable.ic_list_select)
                else -> binding.xListItemIv.setImageResource(R.drawable.ic_list_normal)
            }
            binding.root.setOnClickListener {
                itemClick(bean.code, position)
                item?.get(position)?.isChecked = true
                item?.get(lastPos)?.isChecked = false
                notifyItemChanged(position)
                notifyItemChanged(lastPos)
                lastPos = position
            }

        }
    }
}
