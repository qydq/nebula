package com.nebula.take.tips;


import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nebula.AnApplication;
import com.nebula.AnConstants;
import com.nebula.R;
import com.nebula.model.tips.XHttps;
import com.nebula.model.tips.XProgressCallBack;
import com.nebula.presenter.service.ApiService;
import com.nebula.presenter.service.RetrofitX;
import com.nebula.presenter.service.RetxCallBackProgress;
import com.nebula.take.ApkInstallReceiver;
import com.nebula.take.DataService;
import com.nebula.take.LaDialog;
import com.nebula.take.LaStorageFile;
import com.nebula.take.RetxService;

import org.xutils.http.RequestParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * brief:提供一个软件更新的接口，这里需要服务器判断，完成AR20180112XVW
 * 更新一个功能到后台去处理
 * retrofix替换Xutils3处理下载能力
 * <br> author：晴雨【qy】
 * <br> email：staryumou@163.com
 * <br> update date information：2018/01/14
 * <br> website：https://qydq.github.io
 * <br> Copyrigth(c),2017,孙顺涛,inasst.com
 * <br> version 2.0
 * <p>
 * create date：2016/8/18 by author 晴雨【qy】
 */
public class LaUpdate {
    private String TAG = "UpdateService";

    private int downloadState = -1;//-1失败，0成功。

    private Context mContext;

    /**
     * 通知更新弹窗，提示语
     */
    private String updateMsg = "有最新的软件包哦，亲快下载吧~";

    /**
     * 安全apk的完整网络地址
     */
    private String apkUrl;

    /**
     * 通知更新弹窗
     */
    private Dialog noticeDialog;
    /**
     * 下载更新页面弹窗
     */
    private Dialog downloadDialog;
    /**
     * 下载apk包安装路径(不含文件名)
     */
    private String apkDirPath;
    /**
     * 下载apk的文件名。
     */
    private String apkName;


    //操作的resultFilePath，like this ,/sdcard/an/apk/integrate_2018.apk
    private String resultFilePath;

    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;


    private static final int DOWN_UPDATE = 1;

    private static final int DOWN_OVER = 2;

    private int progress;

    private int persentProgress;//目前textView中显示的百分比。


    private boolean interceptFlag = false;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    Log.d(TAG, "Retrofit file download: faile");
                    break;
                case 0:
                    Log.d(TAG, "Retrofit file download: successful");

                    break;
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
                default:
                    break;
            }
        }
    };
    private Call<ResponseBody> calls;

    /**
     * 构造方法，下载到AN系列Nebula框架下到an目录中。
     * android6.0以上需要动态获取读写文件权限。
     */
    public LaUpdate(Context context, String apkUrl, String apkName) {
        this.mContext = context;
        this.apkUrl = apkUrl;
        this.apkName = apkName + "_" + DataService.INSTANCE.getShotDateTime() + ".apk";
        //apk路径
        apkDirPath = File.separator + AnConstants.AnDIR + File.separator + AnConstants.AnApk + File.separator;
        //这里用时间区分。
        resultFilePath = apkDirPath + apkName;
    }

    /**
     * 构造方法，自定义下载文件目录
     * android6.0以上需要动态获取读写文件权限。
     * <p>
     * apkDirPath 参考 = LaStorageFile.INSTANCE.getskRootPath()
     * apkDirPath = "[/sdcard/an/apk/]-(integrate_time.apk)
     * 备注后面不用加 “/”
     */
    public LaUpdate(Context context, String apkDirPath, String apkUrl, String apkName) {
        this.mContext = context;
        this.apkUrl = apkUrl;
        this.apkName = apkName + "_" + DataService.INSTANCE.getShotDateTime() + ".apk";
        this.apkDirPath = apkDirPath;
        //这里用时间区分。需要验证getshotDateTime
        resultFilePath = apkDirPath + apkName + "_" + DataService.INSTANCE.getShotDateTime() + ".apk";
    }

    /* 检查更新，
    外部接口让主Activity调用*/
    public void checkUpdateInfo() {
        showNoticeDialog();
    }

    /*
    * 设置更新的内容提示
    * */
    public void setUpdateMsg(String updataMsg) {
        this.updateMsg = updataMsg;
    }


    private void showNoticeDialog() {
        Builder builder = new Builder(mContext);
        builder.setTitle("软件版本更新");
        builder.setMessage(updateMsg);
        builder.setPositiveButton("下载", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });
        builder.setNegativeButton("以后再说", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }

    private void showDownloadDialog() {
        Builder builder = new Builder(mContext);
        builder.setTitle("正在下载...");

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.base_progress_update_standard, null);
        mProgress = v.findViewById(R.id.anProgressUpdateBar);

        builder.setView(v);
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
                if (calls != null) {
                    calls.cancel();
                }
            }
        });
        downloadDialog = builder.create();
        downloadDialog.show();

        SharedPreferences.Editor editor = AnApplication.sp.edit();
        editor.putString(AnConstants.KEY.Extra_apkIntallPath, resultFilePath);
        if (editor.commit())
//        downloadApkWithHttpUrlConnect();
//        downloadApkWithRetrX();
//        downloadApkWithXHttps();
        /*后台下载开一个通知。*/
            downloadApkinBackground();
    }

    private void downloadApkinBackground() {
//        ApiService.set;

        Intent downAdService = new Intent(mContext, RetxService.class);
        downAdService.putExtra(AnConstants.URL.Extra_requestURl, apkUrl);
        downAdService.putExtra(AnConstants.URL.Extra_requestfileName, apkName);
        downAdService.putExtra(AnConstants.URL.Extra_requestdirpath, apkDirPath);
        mContext.startService(downAdService);
        downloadDialog.cancel();
        LaDialog.INSTANCE.showNotification(mContext, "Integrate",
                "正在下载", downAdService, "andownload", true);
//        ApkInstallReceiver apkInstallReceiver = new ApkInstallReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("installapk");
//        mContext.registerReceiver(apkInstallReceiver, filter);
    }


    private void downloadApkWithHttpUrlConnect() {
        Thread downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 最原始方法，通过HttpURLConnection去下载文件。
     */

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                /*利用LaStorageFile创建文件夹*/

                File ApkFile = LaStorageFile.INSTANCE.touchFile(apkDirPath, apkName);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    //更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        //下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);//点击取消就停止下载.

                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    /**
     * 利用第三方开源框架Retrofit去下载文件。
     */
    private void downloadApkWithRetrX() {

        RetrofitX.downLoadFile(AnConstants.URL.BASE_URL, apkUrl, apkDirPath, apkName, new RetxCallBackProgress() {
            @Override
            public void onResponse(Call<ResponseBody> call) {
                Toast.makeText(mContext, "response successful", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Retrofit contacted and has file");
                calls = call;
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

                progress = (int) (((float) current / total) * 100);
                //更新进度
                mHandler.sendEmptyMessage(DOWN_UPDATE);
            }

            @Override
            public void onSuccess(Request request, Response<ResponseBody> response) {
                downloadState = 0;
                mHandler.sendEmptyMessage(downloadState);
            }

            @Override
            public void onFailure(String message) {
                downloadState = -1;
                mHandler.sendEmptyMessage(downloadState);
                Log.d(TAG, "RetrofitX 下载文件失败");
                Toast.makeText(mContext, "response" + message, Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * 利用第三方框架，xhttps去下载文件。
     */

    private void downloadApkWithXHttps() {
        RequestParams params = new RequestParams(apkUrl);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(resultFilePath);
        XHttps.downLoadFile(apkUrl, resultFilePath, new XProgressCallBack<File>() {

            @Override
            public void onSuccess(File result) {
                if (downloadDialog != null && downloadDialog.isShowing()) {
                    downloadDialog.dismiss();
                }
                installApk();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                super.onLoading(total, current, isDownloading);
                mProgress.setMax((int) total);
                progress = (int) current;
                persentProgress = progress * (100 / (int) total);
                mHandler.sendEmptyMessage(DOWN_UPDATE);
            }
        });
    }

    /**
     * 安装apk
     *
     * @param
     */

    private void installApk() {
        File apkfile = new File(resultFilePath);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.setDataAndType(Uri.fromFile(new File(resultFilePath)), "application/vnd.android.package-archive");
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }
}

