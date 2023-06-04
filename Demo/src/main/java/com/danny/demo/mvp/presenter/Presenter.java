package com.danny.demo.mvp.presenter;

import com.danny.demo.mvp.interfaces.IView;

/**
 *
 * Created by danny on 2019/3/5.
 */
public class Presenter {
    private static IPresenterFactory instance;

    private Presenter() {
    }

    public static void init(IPresenterFactory factory) {
        instance = factory;
    }

    /**
     * 绑定view和presenter
     * @param view 绑定view
     * @param clazz 绑定presenter
     */
    public static void bind(IView view, Class... clazz) {
        if (clazz == null) {
            throw new RuntimeException("must init factory");
        }

        for (Class c : clazz) {
            IPresenter presenter = instance.newPresenter(view, c);
            if (presenter != null) {
                view.addPresenter(presenter);
            }
        }
    }
}
