package com.danny.xbase.base.nav;

/**
 * 侧边栏bean
 * @author danny
 * 2018/11/8.
 */

public class NavBean {
    public static final int CATEGORY = 0;
    public static final int ITEM = 1;

    private int type;
    private int tag;
    private String title;
    private int drawable;

    /** CATEGORY初始化 */
    public NavBean(int type, String title) {
        this.type = type;
        this.title = title;
    }

    /** ITEM初始化 */
    public NavBean(int type, int tag, String title, int drawable) {
        this.type = type;
        this.tag = tag;
        this.title = title;
        this.drawable = drawable;
    }

    public int getType() {
        return type;
    }

    public int getTag() {
        return tag;
    }

    public String getTitle() {
        return title;
    }

    public int getDrawable() {
        return drawable;
    }
}
