package com.danny.xbase.base.nav;


import com.danny.common.Constants;
import com.danny.xbase.R;

import java.util.ArrayList;

/**
 * 侧边栏管理类
 * @author danny
 * 2018/11/8.
 */
public class NavHelper {
    /** 处理条目点击事件 */
    public static void processEvent(int tag) {
        switch (tag) {
            case Constants.TAG_1:break;
            case Constants.TAG_2:break;
            case Constants.TAG_3:break;
            case Constants.TAG_4:break;
            case Constants.TAG_5:break;
            case Constants.TAG_6:break;
            default:break;
        }
    }

    /** 侧边栏数据 */
    public static ArrayList<NavBean> getData() {
        ArrayList<NavBean> bean = new ArrayList<>();
        bean.add(new NavBean(NavBean.CATEGORY, "动物"));
        bean.add(new NavBean(NavBean.ITEM, Constants.TAG_1, "小猫", R.drawable.shape_nav));
        bean.add(new NavBean(NavBean.ITEM, Constants.TAG_2, "小狗", R.drawable.shape_nav));
        bean.add(new NavBean(NavBean.CATEGORY, "植物"));
        bean.add(new NavBean(NavBean.ITEM, Constants.TAG_3, "花", R.drawable.shape_nav));
        bean.add(new NavBean(NavBean.ITEM, Constants.TAG_4, "草", R.drawable.shape_nav));
        bean.add(new NavBean(NavBean.CATEGORY, "人"));
        bean.add(new NavBean(NavBean.ITEM, Constants.TAG_5, "xx", R.drawable.shape_nav));
        bean.add(new NavBean(NavBean.ITEM, Constants.TAG_6, "xx", R.drawable.shape_nav));
        return bean;
    }
}
