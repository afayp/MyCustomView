package com.example.myapplication.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.mvp.MvpActivity;
import com.example.myapplication.mvp.main.MainModel;
import com.example.myapplication.mvp.main.MainPresenter;
import com.example.myapplication.mvp.main.MainView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * MainView接口抽取了MainActivity要实现的业务方法
 * MainActivity首先实现MainView接口，然后把自身作为view层注入到P层
 * 这样p层就持有了view层，当然onDestory里要让p层移除对view的引用
 */
public class MainActivity extends MvpActivity<MainPresenter> implements MainView {

    @Bind(R.id.text)
    TextView text;
    @Bind(R.id.mProgressBar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mvpPrestener.loadData("101010100");
    }

    @Override
    protected MainPresenter createPrestener() {
        return new MainPresenter(this);
    }

    @Override
    public void getDataSuccess(MainModel model) {
        MainModel.WeatherinfoBean weatherinfo = model.getWeatherinfo();
        String showData = weatherinfo.getCity()+" "
                + weatherinfo.getWD()+" "
                + weatherinfo.getWS()+" "
                + weatherinfo.getTime();
        text.setText(showData);

    }

    @Override
    public void getDataFail(String msg) {
        toastShow("网络不给力");

    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);

    }
}
