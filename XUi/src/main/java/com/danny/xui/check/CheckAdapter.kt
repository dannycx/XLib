package com.danny.xui.check

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.danny.xtool.UiTool
import com.danny.xui.R
import com.danny.xui.check.bean.CheckBean
import com.danny.xui.databinding.CheckItemBinding

/**
 * 水平复杂单选按钮适配器
 */
class CheckAdapter(var itemClick: (code: String) -> Unit): RecyclerView.Adapter<CheckAdapter.Holder>() {
    private var items: ArrayList<ArrayList<CheckBean>>? = null
    private var lastPos: Int = 0
    private var lastInnerPos: Int = 0
    private var supportCheck = false // 是否支持复选

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = CheckItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int = items?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        items?.get(position)?.let {
            holder.setData(it, position)
        }
    }

    fun setItems(items: ArrayList<ArrayList<CheckBean>>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun setSupportCheck(supportCheck: Boolean) {
        this.supportCheck = supportCheck
    }

    inner class Holder(var binding: CheckItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val cnt = binding.checkLayout.context

        fun setData(list: ArrayList<CheckBean>, position: Int) {
            binding.checkLayout.removeAllViews()
            val dp16 = UiTool.dimensionPixelSize(cnt, com.danny.common.R.dimen.dp_16)
            for (i in list.indices) {
                val bean = list[i]
                val tv = TextView(binding.root.context)
                tv.textSize = 12f
                tv.text = bean.value
                tv.gravity = Gravity.CENTER
                tv.setPadding(dp16, 0, dp16, 0)
                if (i == 0) {
                    tv.paint.isFakeBoldText = true
                    tv.setTextColor(UiTool.getColor(cnt, com.danny.common.R.color.color_222222))
                } else {
                    tv.paint.isFakeBoldText = false
                    tv.setTextColor(UiTool.getColor(cnt, com.danny.common.R.color.color_999999))
                }
                if (bean.isChecked) {
                    if ((i - 1 >= 0) && (i + 1) < list.size && list[i - 1].isChecked && list[i + 1].isChecked) {
                        tv.setBackgroundResource(R.drawable.shape_check_select_m)
                    } else if ((i + 1) < list.size && list[i + 1].isChecked) {
                        tv.setBackgroundResource(R.drawable.shape_check_select_r)
                    }else if ((i - 1 >= 0) && list[i - 1].isChecked) {
                        tv.setBackgroundResource(R.drawable.shape_check_select_l)
                    } else {
                        tv.setBackgroundResource(R.drawable.shape_check_select)
                    }
                    tv.setTextColor(UiTool.getColor(cnt, com.danny.common.R.color.color_3986ff))
                }
                tv.setOnClickListener {
                    itemClick(bean.code)
                    when (supportCheck) {
                        true -> {
                            if (lastPos == position) {
                                items?.get(lastPos)?.get(i)?.isChecked = true
                            } else {
                                for (j in (items?.get(lastPos)?.indices ?: (0 .. 0))) {
                                    items?.get(lastPos)?.get(j)?.isChecked = false
                                }
                                items?.get(position)?.get(i)?.isChecked = true
                            }
                        }
                        else -> {
                            items?.get(lastPos)?.get(lastInnerPos)?.isChecked = false
                            items?.get(position)?.get(i)?.isChecked = true
                        }
                    }
                    notifyItemChanged(lastPos)
                    notifyItemChanged(position)
                    lastPos = position
                    lastInnerPos = i
                }
                val layoutParams = binding.checkLayout.layoutParams
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                layoutParams.height = UiTool.dimensionPixelSize(cnt, com.danny.common.R.dimen.dp_27)
                tv.layoutParams = layoutParams
                binding.checkLayout.addView(tv)
            }
        }
    }

}
