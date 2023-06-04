package com.danny.demo.mvp.presenter;

import com.danny.demo.mvp.interfaces.IView;

/**
 * Created by 75955 on 2019/3/5.
 */
public interface IPresenterFactory {
    IPresenter newPresenter(IView view, Class clazz);
}
