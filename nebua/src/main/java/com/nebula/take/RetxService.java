package com.nebula.take;

/**
 * 下载单个文件公共类处理。
 * Created by  qydq on 20180115
 * github.com/qydq/an-aw-base
 */

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.nebula.AnConstants;
import com.nebula.presenter.service.RetrofitX;
import com.nebula.presenter.service.RetxCallBackProgress;

import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * AR20180117Sww
 * brief:该类的作用为Retx框架提供后台下载能力，目前version1.0只能下载单个文件。
 * IntentService 是继承自 Service 并处理异步请求的一个类，在
 * IntentService 内有一个工作线程来处理耗时操作，当任务执行完后，
 * IntentService 会自动停止，不需要我们去手动结束。如果启动
 * IntentService 多次，那么每一个耗时操作会以工作队列的方式在
 * IntentService 的 onHandleIntent 回调方法中执行，依次去执行，执行完自动结束。
 * 备注：这里已经是异步任务，这里已经有工作线程。
 * <br> author：晴雨【qy】
 * <br> email：staryumou@163.com
 * <br> Created by sun on 2017/12/26.
 * <br> update date information：2018/01/15
 * <br> website：https://qydq.github.io
 * <br> Copyrigth(c),2018,孙顺涛,inasst.com
 * <br> version 1.0
 */
public class RetxService extends IntentService {
    public static final String TAG = "DownloadAdService";

    private int downloadState = -1;//-1失败，0成功。
    private int progress;

    public RetxService() {
        super("DownloadAdService");
    }

    @Override
    public void onCreate() {
        Log.i("DownloadAdService", "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("DownloadAdService", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        /*retrofit去请求数据*/
        /*暂定为下载第一个文件，多个文件后台下载，待开发*/
        String requestURl = intent.getStringExtra(AnConstants.URL.Extra_requestURl);
        String fileName = intent.getStringExtra(AnConstants.URL.Extra_requestfileName);
        String apkDirPath = intent.getStringExtra(AnConstants.URL.Extra_requestdirpath);
        Log.d(TAG, "Retrofit service--qydq--" + requestURl);
        Log.d(TAG, "Retrofit service--qydq--" + fileName);
        Log.d(TAG, "Retrofit service--qydq--" + apkDirPath);

        RetrofitX.downLoadFile(AnConstants.URL.BASE_URL, requestURl, apkDirPath, fileName, new RetxCallBackProgress() {
            @Override
            public void onResponse(Call<ResponseBody> call) {
                Toast.makeText(RetxService.this, "response successful", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Retrofit contacted and has file");
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

                progress = (int) (((float) current / total) * 100);
                if (callback != null) {
                    callback.onDataChange(progress);
                }
            }

            @Override
            public void onSuccess(Request request, Response<ResponseBody> response) {
                downloadState = 0;
                Intent intent = new Intent();
                intent.setAction("installapk");
                sendBroadcast(intent);
            }

            @Override
            public void onFailure(String message) {
                downloadState = -1;
                Log.d(TAG, "RetrofitX 下载文件失败");
                Toast.makeText(RetxService.this, "response" + message, Toast.LENGTH_LONG).show();
            }
        });

    }

    public void onDestroy() {
        Log.i("DownloadAdService", "onDestroy");
        super.onDestroy();
    }

    public static interface Callback {
        void onDataChange(int data);
    }

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private String data = "服务器正在执行";

    public Callback getCallback() {
        return callback;
    }

    public class Binder extends android.os.Binder {
        public void setData(String data) {
            RetxService.this.data = data;
        }

        public RetxService getRetxService() {
            return RetxService.this;
        }
    }


}