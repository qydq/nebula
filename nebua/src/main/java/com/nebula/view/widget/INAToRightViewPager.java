package com.nebula.view.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 此ViewPager解决与父容器ScrollView冲突的问题,无法完美解决.有卡顿 此自定义组件和下拉刷新scrollview配合暂时小完美，有待改善
 *
 * @author 孙顺涛 ,em: staryumou@163.com
 */

public class INAToRightViewPager extends ViewPager {
    /*fix 常量*/
    private int downRawX, downRawY;
    private int mTouchSlop = 10;
    private static String TAG = "INAToRightViewPager";

    public INAToRightViewPager(Context context) {
        // TODO Auto-generated constructor stub
        super(context);
    }

    public INAToRightViewPager(Context context, AttributeSet attrs) {
        // TODO Auto-generated constructor stub
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //继续分发到--onInterceptTouchEvent
        return super.dispatchTouchEvent(ev);
    }

    /*
    * 拦截该事件，不再分发，交给onInterceptTouchEvent消费。
    * */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downRawX = (int) ev.getRawX();
                downRawY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getRawY();
                int moveX = (int) ev.getRawX();
                /*判断向右滑动,或者判断xiang右滑动，大于先上滑动*/
                if ((moveX - downRawX) > Math.abs(moveY - downRawY)) {
                    Log.i(TAG, "--qydq->右边滑动了");
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

}
