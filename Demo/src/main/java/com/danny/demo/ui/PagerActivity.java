package com.danny.demo.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.danny.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danny on 2018/5/2.
 */

public class PagerActivity extends AppCompatActivity {
    private ViewPager mPager;
    private TextView mTitle;
    private LinearLayout mLayout;
    private ImageView mImageView;
    private List<ImageView> mLists = new ArrayList<>();
    private View mPointView;
    private int[] mImages;
    private int mLastPosition = 0;
    private String[] mTitles;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_view_pager);
        initView();
        initData();
        initAdapter();

        new Thread(new Runnable() {
            @Override
            public void run() {
                flag = true;
                while (flag) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                        }
                    });
                }
            }
        }).start();
    }

    private void initView() {
        mPager = (ViewPager) findViewById(R.id.view_pager);
        mTitle = (TextView) findViewById(R.id.image_title);
        mLayout = (LinearLayout) findViewById(R.id.pointer);

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //滚动
            }

            @Override
            public void onPageSelected(int position) {
                int newPosition = position % mLists.size();
                //选中
                mTitle.setText(mTitles[newPosition]);
//                for (int i=0;i<mLayout.getChildCount();i++) {
//                    mLayout.getChildAt(i).setEnabled(position==i);
//                }

                mLayout.getChildAt(mLastPosition).setEnabled(false);
                mLayout.getChildAt(newPosition).setEnabled(true);
                mLastPosition = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //状态变化
            }
        });
    }

    private void initData() {
        mImages = new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};
        mTitles = new String[]{"1", "2", "3", "4", "5"};
        for (int i = 0; i < mImages.length; i++) {
            mImageView = new ImageView(this);
            mImageView.setBackgroundResource(mImages[i]);
            mLists.add(mImageView);

            //加小白点
            mPointView = new View(this);
            mPointView.setBackgroundResource(R.drawable.point_select);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(5, 5);
            if (i != 0) params.leftMargin = 10;
            mPointView.setEnabled(false);
            mLayout.addView(mPointView, params);
        }
    }

    private void initAdapter() {
        mLayout.getChildAt(0).setEnabled(true);
        mTitle.setText(mTitles[0]);
//        mPager.setOffscreenPageLimit(3);
        mLastPosition = 0;
        mPager.setAdapter(new MyAdapter());
//        int pos=Integer.MAX_VALUE / 2 -(Integer.MAX_VALUE / 2 % 5) ;
        //数太大有bug，设置到某个位置
        mPager.setCurrentItem(500000);
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override//指定复用
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//当滑到新条目，又返回来
        }

        @Override//创建条目
        public Object instantiateItem(ViewGroup container, int position) {
            int newPosition = position % mLists.size();
            ImageView i = mLists.get(newPosition);
            container.addView(i);
            return i;//view返回给框架
        }

        @Override//销毁条目
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = false;
    }
}
