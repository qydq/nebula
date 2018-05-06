package com.nebula.take;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.nebula.view.BaseActivity;

/**
 * brief:自定义检查手机网络状态是否切换的广播接受器
 * <br> author：晴雨【qy】
 * <br> email：staryumou@163.com
 * <br> create date：2016/9/14
 * <br> update date information：2017-12-26 22:47:21
 * <br> website：https://qydq.github.io
 * <br> Copyrigth(c),2017,孙顺涛,inasst.com
 * <br> version 2.0
 */


/*记得在manifest中注册
<receiver android:name="cn.broadcastreceiver.NetBroadcastReceiver">
<intent-filter>
<action android:name="android.net.conn.CONNECTIVITY_CHANGE"/>
</intent-filter>
</receiver>*/

public class NetBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NetEvevt evevt = BaseActivity.evevt;
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWrokState = NetBroadcastReceiverUtils.getNetworkState(context);
            // 接口回调传过去状态的类型
            if (evevt != null)
                evevt.onNetChange(netWrokState);
        }
    }

    // 自定义接口
    public interface NetEvevt {
        void onNetChange(int netModile);
    }
}
