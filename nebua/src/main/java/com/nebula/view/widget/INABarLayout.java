package com.nebula.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nebula.R;


/**
 * title bar create by 晴雨2018-02-12。
 * 提供一个INABar兼容之前an-aw-base框架的布局。
 */
public class INABarLayout extends RelativeLayout {
    protected LinearLayout anLlBack;
    protected LinearLayout anLlRight;
    protected LinearLayout anLlRRight;
    protected ImageView anIvBack;
    protected ImageView anIvRight;
    protected ImageView anIvRRight;

    protected TextView anTxRRight;
    protected TextView anTxRight;
    protected TextView anTxTitle;
    protected TextView anTxBack;

    protected RelativeLayout anRlHeadView;

    protected ProgressBar anPb;

    protected boolean shouldDayNight = false;
    protected boolean shouldComplex = false;
    protected boolean shouldLayout = false;


    protected boolean showRRLl = false;
    protected boolean showRLl = false;
    protected boolean showBackLl = true;

    public INABarLayout(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public INABarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public INABarLayout(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.INABarLayout);
            shouldDayNight = ta.getBoolean(R.styleable.INABarLayout_shouldDayNight, shouldDayNight);
            shouldComplex = ta.getBoolean(R.styleable.INABarLayout_shouldComplex, shouldComplex);
            shouldLayout = ta.getBoolean(R.styleable.INABarLayout_shouldLayout, shouldLayout);
            showRRLl = ta.getBoolean(R.styleable.INABarLayout_showRRLl, showRRLl);
            showRLl = ta.getBoolean(R.styleable.INABarLayout_showRLl, showRLl);
            showBackLl = ta.getBoolean(R.styleable.INABarLayout_showBackLl, showBackLl);

            if (shouldDayNight) {
                if (shouldComplex) {
                    LayoutInflater.from(context).inflate(R.layout.base_headview_daynight_standard_complex, this);
                } else {
                    LayoutInflater.from(context).inflate(R.layout.base_headview_daynight_standard, this);
                }
            } else {
                if (shouldComplex) {
                    LayoutInflater.from(context).inflate(R.layout.base_headview_standard_complex, this);
                } else {
                    LayoutInflater.from(context).inflate(R.layout.base_headview_standard, this);
                }
            }

            /*base_simple*/
            anLlBack = findViewById(R.id.anLlBack);
            anLlRight = findViewById(R.id.anLlRight);

            anIvBack = findViewById(R.id.anIvBack);
            anIvRight = findViewById(R.id.anIvRight);


            anTxRight = findViewById(R.id.anTxRight);
            anTxTitle = findViewById(R.id.anTxTitle);
            anTxBack = findViewById(R.id.anTxBack);

            anRlHeadView = findViewById(R.id.anRlHeadView);

            anPb = findViewById(R.id.anPb);

            if (shouldComplex) {
                anTxRRight = findViewById(R.id.anTxRRight);
                anLlRRight = findViewById(R.id.anLlRRight);
                anIvRRight = findViewById(R.id.anIvRRight);

                if (showRRLl) {
                    anLlRRight.setVisibility(VISIBLE);
                } else {
                    anLlRRight.setVisibility(INVISIBLE);
                }
            }

            if (showRLl) {
                anLlRight.setVisibility(VISIBLE);
            } else {
                anLlRight.setVisibility(INVISIBLE);
            }

            if (showBackLl) {
                anLlBack.setVisibility(VISIBLE);
            } else {
                anLlBack.setVisibility(INVISIBLE);
            }
            parseStyle(context, ta);
        }
    }

    private void parseStyle(Context context, @NonNull TypedArray ta) {
            /*base_simple*/
        String title = ta.getString(R.styleable.INABarLayout_anTxTitle);
        if (!TextUtils.isEmpty(title))
            anTxTitle.setText(title);

        String tvRight = ta.getString(R.styleable.INABarLayout_anTxRight);
        if (!TextUtils.isEmpty(tvRight))
            anTxRight.setText(tvRight);

        String txBack = ta.getString(R.styleable.INABarLayout_anTxBack);
        if (!TextUtils.isEmpty(txBack))
            anTxBack.setText(txBack);

        /*默认只有complex布局才可以自定义大小*/
        if (shouldLayout && shouldComplex) {
            int anIvBackWidth = ta.getDimensionPixelSize(R.styleable.INABarLayout_anIvBackWidth, 18);
            anIvBack.setLayoutParams(new LayoutParams(anIvBackWidth, anIvBackWidth));
            int anIvRightWidth = ta.getDimensionPixelSize(R.styleable.INABarLayout_anIvRightWidth, 10);
            anIvRight.setLayoutParams(new LayoutParams(anIvRightWidth, anIvRightWidth));
            int anIvRRightWidth = ta.getDimensionPixelSize(R.styleable.INABarLayout_anIvRRightWidth, 10);
            anIvRRight.setLayoutParams(new LayoutParams(anIvRRightWidth, anIvRRightWidth));
        }

        Drawable leftDrawable = ta.getDrawable(R.styleable.INABarLayout_anIvBack);
        if (null != leftDrawable) {
            anIvBack.setImageDrawable(leftDrawable);
        }
        Drawable rightDrawable = ta.getDrawable(R.styleable.INABarLayout_anIvRight);
        if (null != rightDrawable) {
            anIvRight.setImageDrawable(rightDrawable);
        }
        Drawable background = ta.getDrawable(R.styleable.INABarLayout_anBarLayoutbg);
        if (null != background) {
            anRlHeadView.setBackgroundDrawable(background);
        }

            /*base_complex*/
        if (shouldComplex) {
            String tvRRight = ta.getString(R.styleable.INABarLayout_anTxRRight);
            if (!TextUtils.isEmpty(tvRRight))
                anTxRRight.setText(tvRRight);

            Drawable rrightDrawable = ta.getDrawable(R.styleable.INABarLayout_anIvRRight);
            if (null != rrightDrawable) {
                anIvRRight.setImageDrawable(rrightDrawable);
            }
        }
        ta.recycle();
    }

    public void setIvBackResource(int resId) {
        anIvBack.setImageResource(resId);
    }


    public void setLlBackClickListener(OnClickListener listener) {
        anLlBack.setOnClickListener(listener);
    }

    public void setIvRightResource(int resId) {
        anIvRight.setImageResource(resId);
    }

    public void setLlRightClickListener(OnClickListener listener) {
        anLlRight.setOnClickListener(listener);
    }

    public void setIvRRightResource(int resId) {
        anIvRRight.setImageResource(resId);
    }

    public void setLlRRightClickListener(OnClickListener listener) {
        if (shouldComplex)
            anLlRRight.setOnClickListener(listener);
    }

    public void setIvRRightVisibility(int visibility) {
        if (shouldComplex)
            anIvRRight.setVisibility(visibility);
    }

    public void setIvRightVisibility(int visibility) {
        anIvRight.setVisibility(visibility);
    }

    public void setRightVisibility(int visibility) {
        setIvRightVisibility(visibility);
        setIvRRightVisibility(visibility);
    }

    public void setIvBackVisibility(int visibility) {
        anIvBack.setVisibility(visibility);
    }

    public void setVisibility(int visibility) {
        anRlHeadView.setVisibility(visibility);
    }

    public void setPbVisibility(int visibility) {
        anPb.setVisibility(visibility);
    }

    public void setTitle(String title) {
        anTxTitle.setText(title);
    }

    public void setBackTx(String backTx) {
        anTxTitle.setText(backTx);
    }

    public void setRightTx(String rightTx) {
        anTxTitle.setText(rightTx);
    }

    public void setRRightTx(String rrightTx) {
        if (shouldComplex)
            anTxTitle.setText(rrightTx);
    }

    public void setBackgroundColor(int color) {
        anRlHeadView.setBackgroundColor(color);
    }


    public LinearLayout getLeftLayout() {
        return anLlBack;
    }

    public LinearLayout getRightLayout() {
        return anLlRight;
    }

    /*需要判空*/
    public LinearLayout getRRightLayout() {
        if (shouldComplex)
            return anLlRRight;
        return null;
    }
}
