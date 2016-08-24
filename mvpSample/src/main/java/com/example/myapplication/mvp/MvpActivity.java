package com.example.myapplication.mvp;

import android.os.Bundle;

import com.example.myapplication.ui.BaseActivity;

/**
 * Activity属于view层，要持有P层对象
 * 所以在onCreate中统一生产出来
 * 在onDestory中统一置为null
 */
public abstract class MvpActivity<P extends BasePresenter> extends BaseActivity{

    protected P mvpPrestener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mvpPrestener = createPrestener();
        super.onCreate(savedInstanceState);
    }

    protected abstract P createPrestener();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mvpPrestener != null){
            mvpPrestener.detachView();
        }
    }
}
