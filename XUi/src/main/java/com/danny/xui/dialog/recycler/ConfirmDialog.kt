package com.danny.xui.dialog.recycler

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.fragment.app.DialogFragment
import com.danny.xtool.UiTool
import com.danny.xui.databinding.DialogConfirmBinding

/**
 * 信息确认dialog
 */
class ConfirmDialog: DialogFragment() {
    private lateinit var binding: DialogConfirmBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogConfirmBinding.inflate(inflater, container, false)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)// 默认有actionBar高度位置无法点击
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val window = requireActivity().window
        val dm = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(dm)

        val lp = window.attributes
        lp.gravity = Gravity.BOTTOM
        lp.width = UiTool.screenDisplay(requireContext())[0] - UiTool.dp2px(requireContext(), 30f)
        lp.y = UiTool.dimensionPixelSize(requireContext(), com.danny.common.R.dimen.dp_16) // 底部偏移距离
        window.attributes = lp
    }
}
