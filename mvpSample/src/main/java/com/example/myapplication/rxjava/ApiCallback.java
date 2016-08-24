package com.example.myapplication.rxjava;

/**
 * Created by afayp on 2016/8/24.
 */
public interface ApiCallback<T> {
    void onSuccess(T model);

    void onFailure(int code, String msg);

    void onCompleted();
}
