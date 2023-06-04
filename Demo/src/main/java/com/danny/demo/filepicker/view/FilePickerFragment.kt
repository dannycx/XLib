package com.danny.demo.filepicker.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danny.demo.databinding.FragmentFileBinding
import com.danny.demo.filepicker.FilePickerConstants
import com.danny.demo.filepicker.bean.PickerFile
import com.danny.demo.filepicker.listener.FilePickerCallback
import com.danny.demo.filepicker.listener.IFilePicker
import com.danny.xbase.module.X


class FilePickerFragment: Fragment() {
    private lateinit var fileBinding: FragmentFileBinding
    private lateinit var adapter: FilePickerAdapter
    private var type: Int = 1
    private lateinit var categories: String
    private lateinit var selectedPaths: ArrayList<String>
    private var curList: ArrayList<PickerFile> = ArrayList(20)
    private var totalList: ArrayList<PickerFile> = ArrayList(100)

    companion object {
        fun newInstance(
            type: Int,
            categories: String,
            selectedPaths: ArrayList<String>?
        ): FilePickerFragment = FilePickerFragment().apply {
            val args = Bundle().apply {
                putInt(FilePickerConstants.FILE_TYPE_KEY, type)
                putString(FilePickerConstants.FILE_CATEGORY_KEY, categories)
//                putStringArrayList(FilePickerConstants.FILE_SELECTED_KEY, selectedPaths)
            }
            this.arguments = args
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fileBinding = FragmentFileBinding.inflate(inflater);
        return fileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initData()
    }

    private fun initRecycler() {
        fileBinding.xFileRecycler.setHasFixedSize(true)
        adapter = FilePickerAdapter()
        fileBinding.xFileRecycler.adapter = adapter

        fileBinding.xFileRecycler.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            var lastVisibleItem = 0
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {// 停止
                        adapter.setRequest(true)

                        if (lastVisibleItem + 1 == adapter.itemCount) {// 加载更多
                            val totalSize = totalList.size
                            val curSize = curList.size
                            if (totalSize > curSize) {
                                if (totalList.size - 20 > curList.size) {
                                    curList.addAll(totalList.subList(curSize, curSize + 20))
                                } else {
                                    curList.addAll(totalList.subList(curSize, totalSize))
                                }
                            }
                        }
                    }
                    RecyclerView.SCROLL_STATE_SETTLING -> {// 滑动
                        adapter.setRequest(false)
                    }
                    RecyclerView.SCROLL_STATE_DRAGGING -> {// 惯性(手指离开屏幕)

                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager =
                    recyclerView.layoutManager as LinearLayoutManager?
                //最后一个可见的ITEM
                lastVisibleItem = layoutManager!!.findLastVisibleItemPosition()
            }
        })

        fileBinding.xFileSwipe.setOnRefreshListener {

        }

    }

    private fun initData() {
        arguments?.apply {
            type = getInt(FilePickerConstants.FILE_TYPE_KEY, 1)
            categories = getString(FilePickerConstants.FILE_CATEGORY_KEY, "")
//            selectedPaths = getStringArrayList(FilePickerConstants.FILE_SELECTED_KEY) as ArrayList<String>
        }
        loadFile()
    }

    private fun loadFile() {
        var category = arrayOf(categories)
        if (FilePickerConstants.CATEGORY_OTHER === categories) {
            category = arrayOf(FilePickerConstants.CATEGORY_APK,
                    FilePickerConstants.CATEGORY_VIDEO,
                    FilePickerConstants.CATEGORY_AUDIO)
        } else if (FilePickerConstants.CATEGORY_ALL === categories) {
            category = arrayOf(FilePickerConstants.CATEGORY_QQ, FilePickerConstants.CATEGORY_WECHAT)
        }

        X.module(context, IFilePicker::class.java).prepare()
            .setType(type)
            .setCategories(category)
            .getList(object : FilePickerCallback {
                override fun onFailure(t: Throwable?) {
                    fileBinding.xFileEmpty.visibility = View.VISIBLE
                }

                override fun onResponse(files: ArrayList<PickerFile>?) {
                    fileBinding.xFileEmpty.visibility = View.GONE
                    if (null != files && files.size > 0) {
                        totalList.addAll(files)
                        if (files.size >= 20) {
                            curList.addAll(files.subList(0, 20))
                            adapter.setData(curList)
                        } else {
                            adapter.setData(files)
                        }
                    }
                }
            })
    }

    fun selectFileAsync(selectList: ArrayList<PickerFile?>?) {
        selectList?.let {
            if (0 == it.size) {
                for (i in curList.indices) {
                    curList[i].isSelect = false
                }
                adapter.setData(curList)
                return
            }

            for (i in curList.indices) {
                val var2 = curList[i]
                if (null == var2.file) {
                    continue
                }
                for (var1 in it) {
                    if (null == var1 || null == var1.file) {
                        continue
                    }
                    val asyncPath = var1.file.absolutePath

                    if (asyncPath == var2.file.absolutePath) {
                        var2.isSelect = true
                        curList[i] = var2
                        break
                    } else {
                        var2.isSelect = false
                        curList[i] = var2
                    }
                }
            }
            adapter.setData(curList)
        }
    }
}