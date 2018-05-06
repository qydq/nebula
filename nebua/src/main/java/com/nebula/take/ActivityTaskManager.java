package com.nebula.take;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.Stack;

/**
 * brief:Activity任务管理类,主要是完成Ａｃｔｉｖｉｔｙ　进出站的管理。
 * <br> author：晴雨【qy】
 * <br> email：staryumou@163.com
 * <br> create date：2017/02/07
 * <br> update date information：2017-12-09 00:15:35
 * <br> website：https://qydq.github.io
 * <br> Copyrigth(c),2017,孙顺涛,inasst.com
 * <br> version 2.0
 */

public class ActivityTaskManager extends Application {
    private static final String TAG = "ActivityTaskManager";
    private static Stack<Activity> activityStack;
    private static ActivityTaskManager instance;

    private final static Object lockObject = new Object();

    private ActivityTaskManager() {
    }

    /**
     * 获取ActivityTaskManager实例对象
     *
     * @return 单例对象
     * @description 单例模式
     */
    public static ActivityTaskManager getInstance() {
        synchronized (lockObject) {
            if (null == instance) {
                instance = new ActivityTaskManager();
            }
            return instance;
        }
    }

    /**
     * 退出栈顶Activity
     *
     * @param activity
     */
    public void popActivity(Activity activity) {
        if (activityStack == null) {
            return;
        }
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
        }
    }

    /**
     * 获得当前栈顶Activity
     */
    public Activity currentActivity() {
        if (activityStack != null && activityStack.size() > 0) {
            Activity activity = activityStack.lastElement();
            return activity;
        } else {
            return null;
        }
    }

    /**
     * 将当前Activity推入栈中
     *
     * @param activity
     */
    public void pushActivity(Activity activity) {
        synchronized (lockObject) {
            if (activityStack == null) {
                activityStack = new Stack<Activity>();
            }
            activityStack.add(activity);
        }
    }

    /**
     * 退出栈中所有Activity
     */
    public void popAllActivity() {
        if (activityStack == null) {
            return;
        }
        while (true) {
            if (activityStack.empty()) {
                return;
            }
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            popActivity(activity);/**退出Activity*/
        }
    }

    /**
     * SingleTask模式下的Activity启动与栈管理
     *
     * @param context      启动Activity的上下文
     * @param activityName 要启动的Activity的类名
     * @param bundle       启动Activity要传递的数据包
     */
    public void startSingleTaskActivity(Context context, String activityName, Bundle bundle) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (context instanceof Activity) {
            intent.setClassName(context, activityName);
            context.startActivity(intent);
        } else {
            Activity activity = currentActivity();
            if (activity != null) {
                intent.setClassName(activity, activityName);
                activity.startActivity(intent);
            }
        }
    }

    /***
     * 获取堆栈中的Activity实例
     *
     * @param activityName
     * @return Activity
     */
    public Activity getActivityInstance(String activityName) {
        if (activityStack == null || activityStack.isEmpty()) {
            return null;
        }
        for (Activity activity : activityStack) {
            if (TextUtils.equals(activityName, activity.getClass().getName())) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 判断当前Activity堆栈中是否包含指定Activity
     *
     * @param activityName
     * @return false && true
     */
    public boolean isStackContainActivity(String activityName) {
        if (activityStack == null || activityStack.isEmpty()) {
            return false;
        }
        for (Activity activity : activityStack) {
            if (TextUtils.equals(activityName, activity.getClass().getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 退出Activity
     */

    public void exit() {
        try {
            for (Activity activity : activityStack) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }


    public void popMoreActivity(int size) {
        if (activityStack == null || activityStack.empty() || activityStack.size() < size) {
            return;
        }
        for (int i = 0; i < size; i++) {
            if (activityStack.empty()) {
                return;
            }
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            popActivity(activity);
        }
    }

    /**
     * 判断当前是否为webview开启多进程
     *
     * @param context
     * @return true&&false
     */
    public boolean isMultipleProcessOpened(Context context) {
        boolean isMultipleProcessOpened = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isMultipleProcessOpened = Settings.Global.getInt(context.getContentResolver(), "webview_multiprocess", 0) != 0;
        }
        return isMultipleProcessOpened;
    }

    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
