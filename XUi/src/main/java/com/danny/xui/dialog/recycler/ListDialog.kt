package com.danny.xui.dialog.recycler

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.fragment.app.DialogFragment
import com.danny.xtool.UiTool
import com.danny.xui.check.bean.CheckBean
import com.danny.xui.databinding.DialogListBinding

/**
 * 列表对话框
 */
class ListDialog(private var title: String, var item: ArrayList<CheckBean>): DialogFragment() {
    private lateinit var binding: DialogListBinding
    private lateinit var adapter: ListDialogAdapter
    private var count = 7
    private var itemHeight: Int = 0
    private lateinit var listClick: (code: String, pos: Int) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogListBinding.inflate(inflater, container, false)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)// 默认有actionBar高度位置无法点击
        initView()
        initData()
        return binding.root
    }

    private fun initView() {
        itemHeight = UiTool.dimensionPixelSize(requireContext(), com.danny.common.R.dimen.dp_48)
        binding.xListTitle.text = title
        binding.xListRecycler.setHasFixedSize(true)
        adapter = ListDialogAdapter(item) { code, pos ->
            listClick(code, pos)
            dismiss()
        }
        binding.xListRecycler.adapter = adapter
    }

    private fun initData() {
        item.apply {
            setParam()
            adapter.item = this
        }
    }

    override fun onStart() {
        super.onStart()

        val window = dialog!!.window
//        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val dm = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(dm)

        val lp = window?.attributes
        lp?.gravity = Gravity.BOTTOM
        lp?.width = UiTool.screenDisplay(requireContext())[0] -
                UiTool.dimensionPixelSize(requireContext(), com.danny.common.R.dimen.dp_20)
        lp?.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp?.y = UiTool.dimensionPixelSize(requireContext(), com.danny.common.R.dimen.dp_8) // 底部偏移距离
        window?.attributes = lp
    }

    fun setCallback(listClick: (code: String, pos: Int) -> Unit) {
        this.listClick = listClick
    }

    fun setParam() {
        item.apply {
            val layoutParams = binding.xListRecycler.layoutParams
            if (size > count) {
                layoutParams.height = count * itemHeight
            } else {
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            }
            binding.xListRecycler.layoutParams = layoutParams
        }
    }
}
