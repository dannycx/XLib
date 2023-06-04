package com.danny.demo.home.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.danny.demo.databinding.CFragmentHomeBinding
import com.danny.demo.home.me.CMeFragment
import com.danny.demo.home.message.CMessageFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.danny.demo.home.baby.CDaughterFragment
import com.danny.demo.home.baby.`fun`.CFunFragment

class CHomeFragment : Fragment() {
    private lateinit var cHomeBinding: CFragmentHomeBinding
    val list = listOf<String>(
        "承诺",
        "购物",
        "坏宝",
        "生活",
        "♥"
    )

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cHomeBinding = CFragmentHomeBinding.inflate(inflater)
        initTab()
        initPager()

        initMenu()
        return cHomeBinding.root
    }

    private fun initMenu() {
        cHomeBinding.ivMenu.setOnClickListener {

        }
    }

    private fun initPager() {
        cHomeBinding.viewPager.adapter = CHomeAdapter(initTabPager(), childFragmentManager, lifecycle)

        TabLayoutMediator(cHomeBinding.tabLayout, cHomeBinding.viewPager, TabLayoutMediator.TabConfigurationStrategy {
                tab, position -> tab.text = list[position]
        }).attach()

//        activity?.getSharedPreferences("data", Context.MODE_PRIVATE)?.open {
//            putFloat("", 0f)
//        }

//        activity?.getSharedPreferences("data", Context.MODE_PRIVATE)?.edit {
//            putFloat("", 0f)
//        }
    }

    private fun initTabPager(): List<Fragment> {
        return listOf(
            CMeFragment(),
            CMessageFragment(),
            CMeFragment(),
            CFunFragment(),
            CDaughterFragment()
        )
    }

    private fun initTab() {
        cHomeBinding.tabLayout.run {
            addTab(newTab().setText(list[0]))
            addTab(newTab().setText(list[1]))
            addTab(newTab().setText(list[2]))
            addTab(newTab().setText(list[3]))
            addTab(newTab().setText(list[4]))
        }
    }


}
