package com.nebula.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.nebula.AnConstants;
import com.nebula.R;
import com.nebula.contract.DayNightTheme;
import com.nebula.model.entity.ResponseDayNightModel;
import com.nebula.take.DayNightHelper;
import com.nebula.take.LaStorageFile;


/**
 * brief:所有Fragment的基类
 * <br> author：晴雨【qy】
 * <br> email：staryumou@163.com
 * <br> create date：2016/8/18
 * <br> update date information：20171216
 * <br> website：https://qydq.github.io
 * <br> Copyrigth(c),2017,孙顺涛,inasst.com
 * <br> version 2.0
 */

public abstract class BaseFragment extends Fragment implements OnTouchListener, DayNightTheme {

    /**
     * 子类默认使用的日志输出标签
     */
    protected String TAG = "BaseFragment";

    protected Context mContext = null;
    protected View view;
    protected SharedPreferences sp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        this.mContext = inflater.getContext();
        /*这里做这样的处理，如果是小于android6.0则名字自己控制，如果大于6.0则用系统的sp*/
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            sp = LaStorageFile.INSTANCE.getSharedPreferences(mContext);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sp = LaStorageFile.INSTANCE.getDefaultSharedPreferences(mContext);
        }
        setDayNightTheme(R.color.ColorBlack);

        view = inflater.inflate(getLayoutId(), null);
        init();

        TAG = BaseFragment.class.getSimpleName();
        return view;
    }

    @Override
    public void setDayNightTheme(int colorId) {
        String mode = sp.getString(AnConstants.KEY.Extra_daynight, ResponseDayNightModel.DAY.getName());
        if (ResponseDayNightModel.NIGHT.getName().equals(mode)) {
            DayNightHelper.setNightTheme((Activity) mContext);
        } else {
            DayNightHelper.setDayTheme((Activity) mContext);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.setOnTouchListener(this);// 拦截触摸事件，防止内存泄露下去
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 拦截触摸事件，防止内存泄露下去
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    public abstract int getLayoutId();

    public abstract void init();

}

