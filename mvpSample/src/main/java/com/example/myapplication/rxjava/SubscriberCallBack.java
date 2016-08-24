package com.example.myapplication.rxjava;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by afayp on 2016/8/24.
 */
public class SubscriberCallBack<T> extends Subscriber<T> {

    private ApiCallback<T> apiCallback;

    public SubscriberCallBack(ApiCallback<T> apiCallback) {
        this.apiCallback = apiCallback;
    }

    @Override
    public void onCompleted() {
        apiCallback.onCompleted();
    }

    @Override
    public void onError(Throwable e) {

        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            //httpException.response().errorBody().string()
            int code = httpException.code();
            String msg = httpException.getMessage();
            if (code == 504) {
                msg = "网络不给力";
            }
            apiCallback.onFailure(code, msg);
        } else {
            apiCallback.onFailure(0, e.getMessage());
        }
        apiCallback.onCompleted();

    }

    @Override
    public void onNext(T t) {
        apiCallback.onSuccess(t);

    }
}
