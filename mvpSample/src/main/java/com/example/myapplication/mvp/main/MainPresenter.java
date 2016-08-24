package com.example.myapplication.mvp.main;

import com.example.myapplication.mvp.BasePresenter;
import com.example.myapplication.rxjava.ApiCallback;
import com.example.myapplication.rxjava.SubscriberCallBack;

/**
 * 首先在构造中注入view层
 * 写一些用户操作view层后，view会回调p层的方法
 */
public class MainPresenter extends BasePresenter<MainView> {

    public MainPresenter(MainView mainView) {
        attachView(mainView);
    }

    /**
     *这里加载数据不是由model层去做了
     * 而是由retrofit来完成数据的加载
     * 加载完成后回调View层的方法
     */
    public void loadData(String cityId){
        mvpView.showLoading();

        addSubscription(apiStores.loadData(cityId),//<——被观察者|观察者——>
                new SubscriberCallBack<MainModel>(new ApiCallback<MainModel>() {
                    @Override
                    public void onSuccess(MainModel model) {
                        mvpView.getDataSuccess(model);

                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        mvpView.getDataFail(msg);

                    }

                    @Override
                    public void onCompleted() {
                        mvpView.hideLoading();
                    }
                }));

    }
}
