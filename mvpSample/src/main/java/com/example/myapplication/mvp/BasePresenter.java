package com.example.myapplication.mvp;

import com.example.myapplication.retrofit.ApiStores;
import com.example.myapplication.retrofit.AppClient;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 与业务逻辑有关的顶层P层
 * 因为项目中使用了RxJava，所以要在最后取消所有订阅
 * P层要刷新View层的状态，持有View层引用,
 * 所以提供attachView方法注入p层
 * detachView方法清除p层的view引用
 */
public class BasePresenter<V> implements IPresenter<V> {

    public V mvpView;
    public ApiStores apiStores = AppClient.retrofit().create(ApiStores.class);
    private CompositeSubscription mCompositeSubscription;


    @Override
    public void attachView(V view) {
        this.mvpView = view;

    }

    @Override
    public void detachView() {
        this.mvpView = null;
        onUnsubscribe();

    }

    //RXjava取消注册，以避免内存泄露
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }


    /**
     *P层的观察者和订阅者统一调用这个方法订阅，便于最后取消订阅
     */
    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }
}
