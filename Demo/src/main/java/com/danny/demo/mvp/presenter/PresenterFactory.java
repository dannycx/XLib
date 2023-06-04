package com.danny.demo.mvp.presenter;

import com.danny.demo.mvp.interfaces.IView;

/**
 * 生产presenter工厂
 * Created by danny on 2019/3/5.
 */
public class PresenterFactory implements IPresenterFactory {
    @Override
    public IPresenter newPresenter(IView view, Class clazz) {
        return null;
    }
}
