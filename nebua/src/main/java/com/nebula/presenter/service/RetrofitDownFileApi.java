package com.nebula.presenter.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by qydq on 2017/11/23.
 * github.com/qydq/an-aw-base
 * downloadFileWithFixedRepos
 * 如果你下载的文件很大,则使用@Streaming 定义Request
 * downloadFileDynamicRepos
 * 下载单一的文件，传入文件的地址，或者String下载路径URL的数组
 */

public interface RetrofitDownFileApi {
    //option 1: a resource relative to your base URL
    @Streaming
    @GET("/uploads/allimg/160726/7730-160H6114H5.jpg")
    Call<ResponseBody> downloadFileWithFixedRepos();

    //option 2:using a dynamic URL
    @Streaming
    @GET
    Call<ResponseBody> downloadFileDynamicRepos(@Url String IMAGE_URL);

    @Streaming
    @GET
    Call<ResponseBody> downloadFileDynamicRepos(@Url String[] IMAGE_URLS);
    
}
