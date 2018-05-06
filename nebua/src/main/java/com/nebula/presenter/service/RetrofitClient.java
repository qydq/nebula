package com.nebula.presenter.service;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.nebula.take.tips.LaLog;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by stary on 2018/3/19.
 *
 * 该retrofit工具类需要再补充
 */

public class RetrofitClient {
    private static final int DEFAULT_TIMEOUT = 5;

    private ApiService apiService;

    private OkHttpClient okHttpClient;

    public static String baseUrl = ApiService.Base_URL;

    private static Context mContext;

    private static RetrofitClient sNewInstance;

    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient(
                mContext);
    }

    public static RetrofitClient getInstance(Context context) {
        if (context != null) {
//            Log.v("RetrofitClient", LaLog.isDebug() + "");
            mContext = context;
        }
        return SingletonHolder.INSTANCE;
    }


    public static RetrofitClient getInstance(Context context, String url) {
        if (context != null) {
            mContext = context;
        }
        sNewInstance = new RetrofitClient(context, url);
        return sNewInstance;
    }

    private RetrofitClient(Context context) {

        this(context, null);
    }

    private RetrofitClient(Context context, String url) {

        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public void getData(Subscriber<ResponseBody> subscriber, String ip) {
//        apiService.getData(ip)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
    }

    public void get(String url, Map headers, Map parameters, Subscriber<ResponseBody> subscriber) {
//        apiService.executeGet(url, headers, parameters)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
    }

    public void post(String url, Map headers, Map parameters, Subscriber<ResponseBody> subscriber) {
//        apiService.executePost(url, headers, parameters)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
    }

}