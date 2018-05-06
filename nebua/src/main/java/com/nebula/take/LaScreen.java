package com.nebula.take;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by qydq on 2015/11/27.
 * version2.0 这个Context哪来的我们不能确定，很大的可能性，你在某个Activity里面为了方便，直接传了个this;这样问题就来了，
 * 我们的这个类中的sInstance是一个static且强引用的，在其内部引用了一个Activity作为Context，
 * 也就是说，我们的这个Activity只要我们的项目活着，就没有办法进行内存回收。而我们的Activity的生命周期肯定没这么长，所以造成了内存泄漏。
 */
public class LaScreen {

    public static int height;
    public static int width;
    private Context context;

    private static LaScreen instance;

    private LaScreen(Context context) {
        this.context = context;
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
    }
//    fix :解决了内存泄漏的问题，因为我们引用的是一个ApplicationContext，它的生命周期和我们的单例对象一致
    //具体参考 https://blog.csdn.net/lmj623565791/article/details/40481055
    public static synchronized LaScreen getInstance(Context context) {
        if (instance == null) {
            instance = new LaScreen(context.getApplicationContext());
        }
        return instance;
    }


    /**
     * 得到手机屏幕的宽度, pix单位
     */
    public int getScreenWidth() {
        return width;
    }

    /**
     * 得到手机屏幕的宽度, pix单位
     */
    public int getScreenHeight() {
        return height;
    }

}