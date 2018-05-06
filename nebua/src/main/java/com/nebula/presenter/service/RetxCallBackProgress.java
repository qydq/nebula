package com.nebula.presenter.service;

import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 向抛出出一个请求的接口
 * Created by qydq on 2018/1/17.
 * github.com/qydq/an-aw-base
 */

public interface RetxCallBackProgress {

    void onResponse(Call<ResponseBody> call);

    void onLoading(long total, long current, boolean isDownloading);

    void onSuccess(Request request, Response<ResponseBody> response);

    void onFailure(String message);
}
