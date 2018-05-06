package com.nebula.presenter.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface RetrofitApi {

    //对一个简单的html页面进行GET请求
    @Streaming
    @GET
    Call<ResponseBody> getHtml(@Url String requestURL);
}
