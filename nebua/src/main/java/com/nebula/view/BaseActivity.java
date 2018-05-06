package com.nebula.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nebula.R;
import com.nebula.take.ActivityTaskManager;
import com.nebula.take.NetBroadcastReceiver;
import com.nebula.take.NetBroadcastReceiverUtils;


/**
 * brief:所有Activity的基类
 * <br> author：晴雨【qy】
 * <br> email：staryumou@163.com
 * <br> create date：2016/8/18
 * <br> update date information：2017-12-10 09:51:22；
 * <br> website：https://qydq.github.io
 * <br> Copyrigth(c),2017,孙顺涛,inasst.com
 * <br> version 2.0
 */
public abstract class BaseActivity extends AppCompatActivity implements NetBroadcastReceiver.NetEvevt {
    /**
     * 基类(Activity)所使用的TAG标签
     */
    protected String TAG = "BaseActivity";

    /**
     * 网路监听广播
     * */
    public static NetBroadcastReceiver.NetEvevt evevt;

    /**
     * 网络类型
     */
    private int netModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置标题栏no title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        TAG = getClass().getSimpleName();
        // 将其子activity添加到activity采集器
        ActivityTaskManager.getInstance().pushActivity(this);
        //网络变化监听相关
        evevt = this;

        inspectNet();

    }

    /**
     * 初始化时判断有没有网络
     */

    public boolean inspectNet() {
        this.netModel = NetBroadcastReceiverUtils.getNetworkState(BaseActivity.this);
        return isNetConnect();
    }

    /**
     * 网络变化之后的类型
     */
    @Override
    public void onNetChange(int netModel) {
        // TODO Auto-generated method stub
        this.netModel = netModel;
        isNetConnect();
    }

    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (netModel == 1) {
            return true;
        } else if (netModel == 0) {
            return true;
        } else if (netModel == -1) {
            return false;
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 将其子activity从activity采集器中移除
        ActivityTaskManager.getInstance().popActivity(this);
    }

    // 覆盖以下方法,设置动画.
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.aim_common_right_in,
                R.anim.aim_common_left_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.aim_common_right_in,
                R.anim.aim_common_zoom_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.aim_common_left_in,
                R.anim.aim_common_right_out);
    }

    public String getViewValue(View view) {
        if (view instanceof EditText) {
            return ((EditText) view).getText().toString();
        } else if (view instanceof TextView) {
            return ((TextView) view).getText().toString();
        } else if (view instanceof CheckBox) {
            return ((CheckBox) view).getText().toString();
        } else if (view instanceof RadioButton) {
            return ((RadioButton) view).getText().toString();
        } else if (view instanceof Button) {
            return ((Button) view).getText().toString();
        }
        return null;
    }

}
