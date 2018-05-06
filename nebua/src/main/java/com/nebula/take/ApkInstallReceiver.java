package com.nebula.take;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.nebula.AnApplication;
import com.nebula.AnConstants;

import java.io.File;


public class ApkInstallReceiver extends BroadcastReceiver {
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        installApk();
    }

    /**
     * 安装apk
     */
    private void installApk() {
        String resultFilePath = AnApplication.sp.getString(AnConstants.KEY.Extra_apkIntallPath, "");
        if (!TextUtils.isEmpty(resultFilePath)) {
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
}
