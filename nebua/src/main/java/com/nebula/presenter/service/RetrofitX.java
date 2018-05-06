package com.nebula.presenter.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.nebula.AnApplication;
import com.nebula.take.DataService;
import com.nebula.take.LaStorageFile;
import com.nebula.take.NetBroadcastReceiverUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by qydq on 2018/1/17.
 * github.com/qydq/an-aw-base
 */

public class RetrofitX {
    private static String TAG = "RetrofitX";


    /**
     * 下载大文件，比如视频电影类型的电影可以使用。默认保存在跟目录下。
     *
     * @param BASE_URL         基础类
     * @param requestURL       请求类
     * @param fileName         文件名字
     * @param retrofitCallBack 网络请求返回接口
     */
    public static void downLoadFile(String BASE_URL,
                                    @NonNull String requestURL,
                                    @NonNull String fileName,
                                    @NonNull RetxCallBackProgress retrofitCallBack) {

        excuteDownload(BASE_URL, requestURL, 2048, null, false, "", fileName, retrofitCallBack);
    }

    /**
     * 文件下载到缓存目录中
     */
    public static void downLoadFileInCache(String BASE_URL,
                                           @NonNull String requestURL,
                                           @NonNull Context context,
                                           boolean innalPackage,
                                           @NonNull String fileName,
                                           @NonNull RetxCallBackProgress retrofitCallBack) {

        excuteDownload(BASE_URL, requestURL, 2048, context, innalPackage, "", fileName, retrofitCallBack);
    }

    /**
     * 下载大文件，比如视频电影类型的电影可以使用。保存在pwdFilepath目录下。
     *
     * @param BASE_URL         基础类
     * @param requestURL       请求类
     * @param pwdFilepath      文件目录(不包含文件名） like this's [建议/an/apk/],[an/apk/],[an/apk]
     * @param fileName         文件名字 必须包含后缀才算合法
     * @param retrofitCallBack 网络请求返回接口
     */
    public static void downLoadFile(String BASE_URL,
                                    @NonNull String requestURL,
                                    @NonNull String pwdFilepath,
                                    @NonNull String fileName,
                                    @NonNull RetxCallBackProgress retrofitCallBack) {

        excuteDownload(BASE_URL, requestURL, 2048, null, false, pwdFilepath, fileName, retrofitCallBack);
    }

    /**
     * 文件下载到缓存目录中
     */
    public static void downLoadFileInCache(String BASE_URL,
                                           @NonNull String requestURL,
                                           @NonNull String pwdFilepath,
                                           @NonNull Context context,
                                           boolean innalPackage,
                                           @NonNull String fileName,
                                           @NonNull RetxCallBackProgress retrofitCallBack) {

        excuteDownload(BASE_URL, requestURL, 2048, context, innalPackage, pwdFilepath, fileName, retrofitCallBack);
    }

    /**
     * 下载大文件，比如视频电影类型的电影可以使用。可以设置下载的字节数。
     *
     * @param BASE_URL         基础类
     * @param requestURL       请求类
     * @param pwdFilepath      文件目录(不包含文件名） like this's [建议/an/apk/],[an/apk/],[an/apk]
     * @param fileName         文件名字
     * @param laByte           请求是下载文件的字节大小，应该大于=2048
     * @param retrofitCallBack 网络请求返回接口
     */
    public static void downLoadFile(String BASE_URL,
                                    @NonNull String requestURL,
                                    @NonNull String pwdFilepath,
                                    @NonNull String fileName,
                                    int laByte,
                                    @NonNull RetxCallBackProgress retrofitCallBack) {

        excuteDownload(BASE_URL, requestURL, laByte, null, false, pwdFilepath, fileName, retrofitCallBack);
    }


    private static void excuteDownload(String BASE_URL, @NonNull String requestURL, int laByte,
                                       final Context context, final boolean innalPackage,
                                       @NonNull final String pwdFilepath, @NonNull final String fileName,
                                       @NonNull final RetxCallBackProgress retrofitCallBack) {
//        if(!fileName.contains(".")){
//            Log.i("RetrofitX", "请求保存文件名无后缀");
//            retrofitCallBack.onFailure("请求保存文件名无后缀");
//            return;
//        }


        if (!DataService.INSTANCE.regexCheckUrl(requestURL)) {
            Log.i("RetrofitX", "请求的requestURL不是有效的下载文件");
            retrofitCallBack.onFailure("请求的requestURL不是有效的下载文件");
            return;
        }

        if (!NetBroadcastReceiverUtils.isConnectedToInternet(AnApplication.getInstance())) {
            Log.i("RetrofitX", "网络不可用");
            retrofitCallBack.onFailure("请求的网络不可用，请检查网络");
            return;
        }

        if (laByte < 2048) {
            laByte = 2048;
        }


         /*retrofit去请求数据*/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                // 针对rxjava2.x（adapter-rxjava2的版本要 >= 2.2.0）
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        final RetrofitDownFileApi service = retrofit.create(RetrofitDownFileApi.class);
        Call<ResponseBody> call = service.downloadFileDynamicRepos(requestURL);

        final int finalLaByte = laByte;
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(final Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    retrofitCallBack.onResponse(call);//网络请求已经响应。
                    Log.d(TAG, "RetrofitX contacted and has file");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean writtenToDisk = writeResponseBodyToDisk(context,
                                    innalPackage,
                                    pwdFilepath,
                                    fileName,
                                    finalLaByte,
                                    response.body(),
                                    retrofitCallBack);
                            Log.d(TAG, "RetrofitX download was a success? " + writtenToDisk);
                            if (writtenToDisk) {
                                Log.d(TAG, "RetrofitX 回调成功");
                                retrofitCallBack.onSuccess(call.request(), response);
                            }

                            if (!writtenToDisk) {
                                Log.d(TAG, "RetrofitX 下载文件失败");
                                retrofitCallBack.onFailure("下载文件失败");
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("RetrofitX", "error--" + t.getMessage());
                if (!TextUtils.isEmpty(t.getMessage()))
                    retrofitCallBack.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private static boolean writeResponseBodyToDisk(Context context, boolean innalPackage, String pwdFilepath, String fileName, int laByte, ResponseBody body, RetxCallBackProgress retrofitCallBack) {
        try {
            // todo change the file location/name according to your needs
            /*利用LaStorageFile创建文件夹*/
            File futureStudioIconFile;
            if (context != null) {
                futureStudioIconFile = LaStorageFile.INSTANCE.touchCacheFile(context,
                        pwdFilepath,
                        fileName,
                        innalPackage);
            } else {
                if (TextUtils.isEmpty(pwdFilepath)) {
                    futureStudioIconFile = LaStorageFile.INSTANCE.touchFile(fileName);
                } else {
                    futureStudioIconFile = LaStorageFile.INSTANCE.touchFile(pwdFilepath, fileName);
                }
            }
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[laByte];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "Retrofit file download: " + fileSizeDownloaded + " of " + fileSize);

                    retrofitCallBack.onLoading(fileSize, fileSizeDownloaded, true);
                }
                retrofitCallBack.onLoading(fileSize, fileSizeDownloaded, false);
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    public static void getHtml(String BASE_URL,
                               @NonNull String requestURL,
                               @NonNull final RetxCallBack retrofitCallBack) {
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(BASE_URL)
//                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        final RetrofitApi service = retrofit.create(RetrofitApi.class);
        Call<ResponseBody> call = service.getHtml(requestURL);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(final Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    retrofitCallBack.onResponse();
                    retrofitCallBack.onSuccess(call.request(), response.body());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (!TextUtils.isEmpty(t.getMessage()))
                    retrofitCallBack.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

}
