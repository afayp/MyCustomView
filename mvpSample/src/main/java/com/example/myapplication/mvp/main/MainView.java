package com.example.myapplication.mvp.main;

/**
 * 对应MainActivity这个view层处理业务需要的方法
 */
public interface MainView {

    void getDataSuccess(MainModel model);

    void getDataFail(String msg);

    void showLoading();

    void hideLoading();

}
