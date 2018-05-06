package com.nebula.take;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by stary on 2016/8/17.
 * 文件名称：AndroidTranslucentBar
 * 文件描述：状态栏透明背景设置，加锁的懒汉，线程安全单例模式参考
 * 作者：staryumou@163.com
 * 修改时间：2016/8/17
 */

public class AndroidTranslucentBar extends Activity {
    private static AndroidTranslucentBar instance;
    public synchronized static AndroidTranslucentBar getInstance() {
        if (null == instance) {
            instance = new AndroidTranslucentBar();
        }
        return instance;
    }

    public void setTranslucentBar(Window mWindow) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
}
