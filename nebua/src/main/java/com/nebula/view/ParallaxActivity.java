package com.nebula.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
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
import com.nebula.view.widget.ParallaxBackActivityHelper;
import com.nebula.view.widget.ParallaxBackLayout;


/**
 * Created by qydq on 2017年3月23日15:42:16
 * <p>
 * 如有使用问题请访问https://github.com/qydq
 */
public abstract class ParallaxActivity extends BaseActivity implements DayNightTheme {
    private ParallaxBackActivityHelper mHelper;

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
        mHelper = new ParallaxBackActivityHelper(this);
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }
    /*fix need cast findview by id 晴雨2018*/
    public <T extends View> T findViewById(@IdRes int id) {
        View view = getDelegate().findViewById(id);
        if (view == null && mHelper != null)
            return mHelper.findViewById(id);
        return getDelegate().findViewById(id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.onActivityDestroy();
    }

    public ParallaxBackLayout getBackLayout() {
        return mHelper.getBackLayout();
    }

    public void setBackEnable(boolean enable) {
        mHelper.setBackEnable(enable);
    }

    public void scrollToFinishActivity() {
        mHelper.scrollToFinishActivity();
    }

    @Override
    public void onBackPressed() {
        if (!getSupportFragmentManager().popBackStackImmediate()) {
            scrollToFinishActivity();
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
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
