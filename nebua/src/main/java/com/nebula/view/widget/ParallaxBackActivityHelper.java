package com.nebula.view.widget;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.View;

import java.util.Stack;

/**
 * Created by sunshuntao.
 */
public class ParallaxBackActivityHelper {
    private static final Stack<ParallaxBackActivityHelper> sActivities = new Stack<>();

    public Activity getActivity() {
        return mActivity;
    }

    private Activity mActivity;

    private ParallaxBackLayout mParallaxBackLayout;

    public ParallaxBackActivityHelper(Activity activity) {
        mActivity = activity;
        mParallaxBackLayout = new ParallaxBackLayout(mActivity);
        sActivities.push(this);
    }

    public boolean hasSecondActivity() {
        return sActivities.size() >= 2;
    }

    public void onPostCreate() {
        mParallaxBackLayout.attachToActivity(this);
    }

    public void onActivityDestroy() {
        sActivities.remove(this);
    }

    public ParallaxBackActivityHelper getSecondActivity() {
        if (sActivities.size() >= 2)
            return sActivities.elementAt(sActivities.size() - 2);
        return null;
    }

    public void drawThumb(Canvas canvas) {
        View decorChild = getBackLayout().getContentView();
        decorChild.draw(canvas);
    }

    /*fix need cast findview by id 晴雨2018*/
    public <T extends View> T findViewById(int id) {
        if (mParallaxBackLayout != null) {
            return mParallaxBackLayout.findViewById(id);
        }
        return null;
    }

    public void scrollToFinishActivity() {
        getBackLayout().scrollToFinishActivity();
    }

    public void setBackEnable(boolean enable) {
        getBackLayout().setEnableGesture(enable);
    }

    public ParallaxBackLayout getBackLayout() {
        return mParallaxBackLayout;
    }
}
