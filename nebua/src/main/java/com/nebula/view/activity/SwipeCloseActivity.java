package com.nebula.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.nebula.AnConstants;
import com.nebula.R;
import com.nebula.contract.DayNightTheme;
import com.nebula.model.entity.ResponseDayNightModel;
import com.nebula.take.LaStorageFile;
import com.nebula.take.LaUi;
import com.nebula.take.NetBroadcastReceiver;
import com.nebula.take.NetBroadcastReceiverUtils;
import com.nebula.view.BaseActivity;
import com.nebula.view.widget.INASwipeCloseFrameLayout;


public abstract class SwipeCloseActivity extends BaseActivity implements NetBroadcastReceiver.NetEvevt, DayNightTheme {
    protected SharedPreferences sp;
    public static NetBroadcastReceiver.NetEvevt evevt;//广播监听网络
    protected Context mContext;
    /**
     * 网络类型
     */
    private int netModel;

    protected Window mWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //环信集成功能，暂未开启。
//		EMChat.getInstance().init(this.getApplicationContext());
        mContext = this;
        //an框架的夜间模式。用来保存皮肤切换模式的sp
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            sp = LaStorageFile.INSTANCE.getSharedPreferences(mContext);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sp = LaStorageFile.INSTANCE.getDefaultSharedPreferences(mContext);
        }
        setDayNightTheme(R.color.ColorBlack);
              /*
        * LaUi 设置状态栏属性，后期修改该类为单例模式 的枚举类型
        * */
        mWindow = getWindow();
        LaUi.setStatusToolColor((Activity) mContext, R.color.ColorBlack);

        //SwipeCloseActivity init
        INASwipeCloseFrameLayout rootView = new INASwipeCloseFrameLayout(this);
        rootView.bindActivity(this);
        //初始化视图
        initView();
        //网络变化监听相关
        evevt = this;
        inspectNet();
    }

    @Override
    public void setDayNightTheme(int colorId) {
        String mode = sp.getString(AnConstants.KEY.Extra_daynight, ResponseDayNightModel.DAY.getName());
        if (ResponseDayNightModel.NIGHT.getName().equals(mode)) {
            setTheme(R.style.NightTheme);
        } else {
            setTheme(R.style.DayTheme);
        }
    }

    /**
     * 初始化时判断有没有网络
     */

    public boolean inspectNet() {
        this.netModel = NetBroadcastReceiverUtils.getNetworkState(SwipeCloseActivity.this);
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

    /**
     * 简化Toast。
     *
     * @return null.
     */
    protected void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    protected void showToastInCenter(final String msg) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 10);
                toast.show();
            }
        });
    }

    protected void showSnackbar(final View ytipsView, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(ytipsView, msg, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 各种对象、组件的初始化
     */
    public abstract void initView();
}
