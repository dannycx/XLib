package com.danny.demo.home.baby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.danny.demo.databinding.CFragmentDaughterBinding
import com.danny.xui.dialog.recycler.ListDialog

class CDaughterFragment: Fragment() {
    private lateinit var cDaughterBinding: CFragmentDaughterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        cDaughterBinding = CFragmentDaughterBinding.inflate(inflater)
        initView()
        return cDaughterBinding.root
    }

    private fun initView() {
        cDaughterBinding.daughterCake.setOnClickListener {
            val dialog = ListDialog("蛋糕", XDaughterTools.cake())
            dialog.setCallback { _, _ -> }
            dialog.show(requireFragmentManager(), "")
        }

        cDaughterBinding.daughterSnacks.setOnClickListener {
            val dialog = ListDialog("零食", XDaughterTools.snacks())
            dialog.setCallback { _, _ -> }
            dialog.show(requireFragmentManager(), "")
        }

        cDaughterBinding.daughterPorridge.setOnClickListener {
            val dialog = ListDialog("粥", XDaughterTools.porridge())
            dialog.setCallback { _, _ -> }
            dialog.show(requireFragmentManager(), "")
        }

        cDaughterBinding.daughterFruit.setOnClickListener {
            val dialog = ListDialog("水果", XDaughterTools.fruit())
            dialog.setCallback { _, _ -> }
            dialog.show(requireFragmentManager(), "")
        }

        cDaughterBinding.daughterFood.setData(XDaughterTools.food())
    }
}
