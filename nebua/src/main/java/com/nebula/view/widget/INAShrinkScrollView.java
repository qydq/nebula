package com.nebula.view.widget;

/**
 * Created by qydda on 2016/12/27.
 */

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;


/**
 *  * 有弹性的ScrollView 实现下拉弹回和上拉弹回
 *  * @author sunshuntao
 *  
 */
public class INAShrinkScrollView extends ScrollView {
    private static final String TAG = "YshrinkScrollView ";
    // 移动因子, 是一个百分比, 比如手指移动了100px, 那么View就只移动50px
// 目的是达到一个延迟的效果
    private static final float MOVE_FACTOR = 0.5f;

    // 松开手指后, 界面回到正常位置需要的动画时间
    private static final int ANIM_TIME = 280;
    // ScrollView的子View， 也是ScrollView的唯一一个子View
    private View contentView;
    // 手指按下时的Y值, 用于在移动时计算移动距离
// 如果按下时不能上拉和下拉， 会在手指移动时更新为当前手指的Y值
    private float startY;
    // 用于记录正常的布局位置
    private Rect originalRect = new Rect();
    // 手指按下时记录是否可以继续下拉
    private boolean canPullDown = false;
    // 手指按下时记录是否可以继续上拉
    private boolean canPullUp = false;
    // 在手指滑动的过程中记录是否移动了布局
    private boolean isMoved = false;


    private GestureDetector mGestureDetector;

    OnTouchListener mGestureListener;
    /*fix 常量*/
    private int downX, downY;
    private int mTouchSlop = 0;

    public INAShrinkScrollView(Context context) {
        super(context);
        mGestureDetector = new GestureDetector(new YSrollDetector());
        setFadingEdgeLength(0);
    }

    public INAShrinkScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(new YSrollDetector());
        setFadingEdgeLength(0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            contentView = getChildAt(0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (contentView == null)
            return;
// ScrollView中的唯一子控件的位置信息, 这个位置信息在整个控件的生命周期中保持不变
        originalRect.set(contentView.getLeft(), contentView.getTop(), contentView.getRight(), contentView.getBottom());
    }

    /**
     * 在触摸事件中, 处理上拉和下拉的逻辑
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (contentView == null) {
            return super.dispatchTouchEvent(ev);
        }

// 手指是否移动到了当前ScrollView控件之外
        boolean isTouchOutOfScrollView = ev.getY() >= this.getHeight() || ev.getY() <= 0;
        if (isTouchOutOfScrollView) { // 如果移动到了当前ScrollView控件之外
            if (isMoved) // 如果当前contentView已经被移动, 首先把布局移到原位置, 然后消费点这个事件
                boundBack();
            return true;
        }
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
// 判断是否可以上拉和下拉
                canPullDown = isCanPullDown();
                canPullUp = isCanPullUp();
// 记录按下时的Y值
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                boundBack();
                break;
            case MotionEvent.ACTION_MOVE:
// 在移动的过程中， 既没有滚动到可以上拉的程度， 也没有滚动到可以下拉的程度
                if (!canPullDown && !canPullUp) {
                    startY = ev.getY();
                    canPullDown = isCanPullDown();
                    canPullUp = isCanPullUp();
                    break;
                }
// 计算手指移动的距离
                float nowY = ev.getY();
                int deltaY = (int) (nowY - startY);
// 是否应该移动布局
                boolean shouldMove = (canPullDown && deltaY > 0) // 可以下拉， 并且手指向下移动
                        || (canPullUp && deltaY < 0) // 可以上拉， 并且手指向上移动
                        || (canPullUp && canPullDown); // 既可以上拉也可以下拉（这种情况出现在ScrollView包裹的控件比ScrollView还小）
                if (shouldMove) {
// 计算偏移量
                    int offset = (int) (deltaY * MOVE_FACTOR);

// 随着手指的移动而移动布局
                    contentView.layout(originalRect.left, originalRect.top + offset, originalRect.right, originalRect.bottom + offset);
                    isMoved = true; // 记录移动了布局
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /*
    * fix ：2018年1月2日15:31:11
    * 修复scrollview和recyclerview冲突的问题
    * */

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "--qydq->ACTION_DOWN");
                downX = (int) ev.getRawX();
                downY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getRawY();
                int moveX = (int) ev.getRawX();
                  /*判断向上滑动,或者向下滑动。判断y > x ；x-y分别为水平，垂直滑动的距离*/
                if (Math.abs(moveY - downY) > Math.abs(moveX - downX)) {
                    Log.i(TAG, "--qydq->上下滑动了");
                    return true;
                }
                  /*判断向右滑动,或者判断xiang右滑动，大于先上滑动*/
                if ((moveX - downX) > Math.abs(moveY - downY)) {
                    Log.i(TAG, "--qydq->右边滑动了");
                }

                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 将内容布局移动到原位置 可以在UP事件中调用, 也可以在其他需要的地方调用, 如手指移动到当前ScrollView外时
     */
    private void boundBack() {
        if (!isMoved)
            return; // 如果没有移动布局， 则跳过执行
// 开启动画
        TranslateAnimation anim = new TranslateAnimation(0, 0, contentView.getTop(), originalRect.top);
        anim.setDuration(ANIM_TIME);
        contentView.startAnimation(anim);
// 设置回到正常的布局位置
        contentView.layout(originalRect.left, originalRect.top, originalRect.right, originalRect.bottom);
// 将标志位设回false
        canPullDown = false;
        canPullUp = false;
        isMoved = false;
    }

    /**
     * 判断是否滚动到顶部
     */
    private boolean isCanPullDown() {
        return getScrollY() == 0 || contentView.getHeight() < getHeight() + getScrollY();
    }

    /**
     * 解决ListView和ScrollView嵌套冲突的方法。
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        //获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);  //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 判断是否滚动到底部
     */
    private boolean isCanPullUp() {
        return contentView.getHeight() <= getHeight() + getScrollY();
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 2);//这里设置滑动的速度。
    }

    class YSrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceY) > Math.abs(distanceX)) {
                return true;
            }
            return false;
        }
    }
}
