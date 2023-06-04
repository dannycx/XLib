package com.danny.xbase.base.nav;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.danny.xbase.R;
import com.danny.xbase.base.BaseActivity;
import com.danny.xbase.event.AppEvent;
import com.danny.xbase.event.EventBusImpl;
import com.danny.xbase.module.X;
import com.google.android.material.navigation.NavigationView;


public abstract class BaseNavActivity extends BaseActivity {
    private DrawerLayout drawerLayout;
    private ImageButton back;
    private TextView title;
    private ImageButton menu;
    private FrameLayout contentLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    protected boolean isOver = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        initView();
        initLayout();
        initRecycler();
        initToolbar();


        X.module(this, EventBusImpl.class).on(AppEvent.class).observe(this, objectEvent -> {

        });
    }

    private void initView() {
        drawerLayout = findViewById(R.id.base_drawer);
        back = findViewById(R.id.base_back);
        title = findViewById(R.id.base_title);
        menu = findViewById(R.id.base_menu);
        contentLayout = findViewById(R.id.base_content);
        navigationView =  findViewById(R.id.base_navigation_);
        recyclerView = findViewById(R.id.base_recycler);
    }

    private void initLayout() {
        contentLayout.removeAllViews();
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);// 滑动不能打开侧边栏
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) contentLayout.getLayoutParams();
        params.setMargins(params.leftMargin, getToolbarHeight(), params.rightMargin, params.bottomMargin);
        contentLayout.setLayoutParams(params);
        contentLayout.addView(getContentView());
    }

    private void initRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        NavAdapter adapter = new NavAdapter();
        adapter.setBeans(NavHelper.getData());
        adapter.setItemClick(new NavAdapter.OnItemClick() {
            @Override
            public void onItemClick(int tag) {
                NavHelper.processEvent(tag);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initToolbar() {
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        title.setText(getActivityTitle());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOver) {

                }else {
                    onBackPressed();
                }
            }
        });
    }

    protected abstract int getToolbarHeight();

    protected abstract View getContentView();

    protected abstract String getActivityTitle();

}
