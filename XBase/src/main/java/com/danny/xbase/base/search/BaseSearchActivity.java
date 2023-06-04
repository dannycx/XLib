package com.danny.xbase.base.search;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.danny.common.Constants;
import com.danny.xbase.R;
import com.danny.xbase.base.BaseActivity;
import com.danny.xbase.databinding.ActivitySearchBinding;
import com.danny.xtool.SPTool;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BaseSearchActivity extends BaseActivity implements View.OnClickListener, SearchAdapter.OnItemClickListener {
    private ActivitySearchBinding binding;
    private String spKey;// 历史纪录key
    private List<String> spList = new ArrayList<>();
    private SearchAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        initView();
        initData();
    }

    private void initView() {
        spKey = getIntent().getStringExtra(Constants.SP_SEARCH_KEY);
        binding.toolLayout.toolbarBack.setOnClickListener(this::onClick);
        binding.toolLayout.toolbarSearch.setOnClickListener(this::onClick);
    }

    private void initData() {
        String history = (String) SPTool.INSTANCE.getValue(this, spKey, "");
        adapter = new SearchAdapter();
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(adapter);
        if (!"".equals(history) || !"[]".equals(history)) {
            Type type = new TypeToken<List<String>>(){}.getType();
            List<String> o = new Gson().fromJson(history, type);
//            if (o != null) {
                if (o.size() > 10) {
                    spList.addAll(o.subList(0, 10));
                } else {
                    spList.addAll(o);
                }
//            }
            adapter.setDataList(spList);
        }

        adapter.setListener(this);
    }

    private void saveSp(String s) {
        if (TextUtils.isEmpty(s) || spList.contains(s)) {
            return;
        }
        spList.add(s);
        String json = new Gson().toJson(spList);
        SPTool.INSTANCE.setValue(this, Constants.SP_SEARCH_KEY, json);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.toolbar_back) {
            finish();
        } else if (id == R.id.toolbar_search) {
            search();
        }
    }

    private void search() {

    }

    @Override
    public void onItemClick(String data) {
        saveSp(data);
    }
}
