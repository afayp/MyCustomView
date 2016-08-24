package com.example.myapplication.retrofit;

import com.example.myapplication.mvp.main.MainModel;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by afayp on 2016/8/24.
 */
public interface ApiStores {

    //baseUrl
    String API_SERVER_URL = "http://www.weather.com.cn/";

    //加载天气
    @GET("adat/sk/{cityId}.html")
    Observable<MainModel> loadData(@Path("cityId") String cityId);
}
