package com.example.myapplication.mvp;

/**
 * 最顶层P层
 * p层要持有view层的引用，
 * 在p层创建的时候赋值
 *要在退出的时候置为null
 */
public interface IPresenter<V> {

    void attachView(V view);

    void detachView();
}
