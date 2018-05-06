package com.nebula.view;

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


/**
 * Created by stary on 2016/8/18.
 * 文件名称：SuperActivity
 * 文件描述：
 * 作者：staryumou@163.com
 * 修改时间：2016/8/18
 */

public abstract class SuperActivity extends BaseActivity implements DayNightTheme {
    protected SharedPreferences sp;

    protected Context mContext;

    protected Window mWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO 固件化的操作
        super.onCreate(savedInstanceState);
        //环信集成功能，暂未开启。
//		EMChat.getInstance().init(this.getApplicationContext());
        mContext = this;
        //an框架的夜间模式。用来保存皮肤切换模式的sp，为了兼容性android6.0 到7.0做了如下判断。
        //android7.0提供兼容性。
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

        //初始化视图
        initView();
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
//                setAction("Undo",
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText(getApplication(), "请输入内容后再试试", Toast.LENGTH_SHORT).show();
//                            }
//                        })
            }
        });
    }

    /**
     * 各种对象、组件的初始化
     */
    public abstract void initView();
}

