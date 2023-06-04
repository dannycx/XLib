package com.danny.demo.home.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.danny.demo.databinding.CFragmentMessageBinding
import com.danny.xui.check.bean.CheckBean
import java.util.ArrayList

class CMessageFragment : Fragment() {
    private lateinit var cHomeBinding: CFragmentMessageBinding


    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cHomeBinding = CFragmentMessageBinding.inflate(inflater)
        cHomeBinding.xCheck.setData(initData(), false) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        cHomeBinding.xCheckT.setData(initData(), true) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        return cHomeBinding.root
    }

    private fun initData(): ArrayList<ArrayList<CheckBean>> {
        val list = ArrayList<ArrayList<CheckBean>>()
        val item = ArrayList<CheckBean>()

        val bean = CheckBean("Total", "Total", true)
        item.add(bean)

        val item1 = ArrayList<CheckBean>()
        val bean10 = CheckBean("葛星", "gx", false)
        val bean11 = CheckBean("邓朝星", "dcx", false)
        val bean12 = CheckBean("邓葛一", "dgy", false)
        val bean13 = CheckBean("邓葛依", "dgyy", false)
        item1.add(bean10)
        item1.add(bean11)
        item1.add(bean12)
        item1.add(bean13)

        val item2 = ArrayList<CheckBean>()
        val bean20 = CheckBean("哈哈哈", "hhh", false)
        val bean21 = CheckBean("哈哈", "hh", false)
        val bean22 = CheckBean("哈", "h", false)
        item2.add(bean20)
        item2.add(bean21)
        item2.add(bean22)

        list.add(item)
        list.add(item1)
        list.add(item2)
        return list
    }
}
