/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xnet.ui.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.danny.xnet.Status
import com.danny.xnet.api.ApiTool
import com.danny.xnet.data.Tests
import com.danny.xnet.databinding.ActivityNetBinding
import com.danny.xnet.http.NetTool
import com.danny.xnet.ui.adapter.MainAdapter
import com.danny.xnet.ui.viewmodel.MainViewModel
import com.danny.xnet.ui.viewmodel.ViewModelFactory

/**
 * test
 *
 * @author x
 * @since 2023-05-31
 */
class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: MainAdapter
    private val mBinding: ActivityNetBinding by lazy {
        ActivityNetBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setupUI()
        setupViewModel()
        setupObserver()
    }

    private fun setupObserver() {
        mainViewModel.getTests()
        mainViewModel.getTests().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
//                       recyclerView.visibility = View.VISIBLE
                        mBinding.progressBar.visibility = View.GONE
                        resource.data?.let { girls -> renderList(girls) }
                    }
                    Status.ERROR -> {
                        mBinding.progressBar.visibility = View.VISIBLE
                        mBinding.recyclerView.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        mBinding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun setupViewModel() {
        mainViewModel =
            ViewModelFactory(ApiTool(NetTool.apiService)).create(MainViewModel::class.java)
    }

    private fun renderList(tests: Tests) {
        adapter.apply {
            addData(tests.data)
            notifyDataSetChanged()
        }
    }

    private fun setupUI() {
        mBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf())
        mBinding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                mBinding.recyclerView.context,
                (mBinding.recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        mBinding.recyclerView.adapter = adapter
    }
}
