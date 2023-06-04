package com.danny.xcamera;

/**
 * 相机扩展
 *
 * @author danny
 * @since 2020-10-30
 */
public interface PreViewCallback {
    /**
     * 缩放
     *
     * @param delta 缩放因子
     */
    void zoom(float delta);

    /**
     * 单击
     *
     * @param x 点击x位置
     * @param y 点击y位置
     */
    void click(float x, float y);

    /**
     * 双击
     *
     * @param x 点击x位置
     * @param y 点击y位置
     */
    void doubleClick(float x, float y);

}
