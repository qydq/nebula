package com.nebula.take;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nebula.R;
import com.nebula.model.adpater.SListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * brief:自定义的an框架dialog createdialog hui返回一个Dialog对象,单例枚举参考
 * version3.0增加一个带ListView的Dialog，ListView后期考虑维护为RecyclerView。
 * 创建Dialog必须手动结束掉。
 * version4.0增加Notifation
 * toDo 4.0应该把软件更新的对话框放到这里面。还有需要增加处理Handler 工具类。
 * <br> author：晴雨【qy】
 * <br> email：staryumou@163.com
 * <br> create date：2016/9/23
 * <br> update date：2017年12月27日17:46:42；20180127
 * <br> website：https://qydq.github.io
 * <br> Copyrigth(c),2017,孙顺涛,inasst.com
 * <br> version 4.0
 */

public enum LaDialog {
    INSTANCE;
    private AnimationDrawable animationDrawable;
    /**
     * 不带进度条Dialog
     */
    private Dialog mDialog;
    /**
     * 带进度条的ProgressDialog
     */
    private ProgressDialog progressDialog;

    private Activity mActivity;
    private LayoutInflater inflater;

    /**
     * 监听带ListView列表，确定，取消，以及checkbox的回调。
     */
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private OnClickListener onclickOk, onclickCancel;

    private OnCancelListener mOnCancelListener;
    /**
     * 需要接受的ListData数据。
     */
    private List<String> listData;
    private View inflateView;
    private SListAdapter adapter;

    // style
    public enum STYLE {
        NORMAL, SIMPLE, PROGRESS, LIST, SUPER, OTHER
    }

    /**
     * 监听是带ListView item点击事件的回调。
     */
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    /**
     * 监听回调
     */
    public void setmOnCancelListener(OnCancelListener mOnCancelListener) {
        this.mOnCancelListener = mOnCancelListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setmOnCheckedChangeListener(OnCheckedChangeListener mOnCheckedChangeListener) {
        this.mOnCheckedChangeListener = mOnCheckedChangeListener;
    }

    public void setOnclickOk(OnClickListener onclickOk) {
        this.onclickOk = onclickOk;
    }

    public void setOnclickCancel(OnClickListener onclickCancel) {
        this.onclickCancel = onclickCancel;
    }



    /*--------------------分割线--------------------*/


    /**
     * 得到自定义的Dialog，默认可以取消。
     *
     * @param activity 上下文对象。
     * @param txt      创建一个对话框，msg是提示语.
     * @param isCancle isCancle是否可以取消对话框.
     * @return Dialog
     */
    public Dialog createDialog(@NonNull Activity activity, @NonNull String txt, boolean isCancle) {
        return create(activity, null, STYLE.NORMAL, 1,
                txt, false, isCancle);
    }

    public Dialog createDialog(@NonNull Activity activity) {
        return create(activity, null, STYLE.NORMAL, -1,
                activity.getString(R.string.listview_loading),
                false,
                true);
    }

    /**
     * 得到自定义的简单Dialog
     *
     * @param activity 上下文对象。
     * @param txt      创建一个对话框，msg是提示语.
     * @param isCancle 创建一个对话框，msg是提示语，isCancle是否可以取消对话框
     * @return Dialog ?android:attr/progressBarStyleLarge,AnSimpleDialog
     */
    public Dialog createSimpleDialog(@NonNull Activity activity,
                                     @NonNull String txt,
                                     boolean isCancle) {
        return create(activity, null, STYLE.SIMPLE, -1, txt, false, isCancle);
    }

    public Dialog createSimpleDialog(@NonNull Activity activity) {
        return create(activity, null, STYLE.SIMPLE, -1,
                activity.getString(R.string.listview_loading),
                false,
                true);
    }

    /**
     * 原始为：ProgressDialog
     *
     * @param activity      上下文对象。
     * @param progressTheme 一般传入${R.style.AnProgressDialog}
     * @param txt           对话框需要的内容提示
     * @param isCancle      是否可以取消
     * @return Dialog
     */
    public Dialog showProgressDialog(@NonNull Activity activity,
                                     @NonNull String txt,
                                     int progressTheme,
                                     boolean isCancle) {
        return create(activity, null, STYLE.PROGRESS, progressTheme, txt, true, isCancle);
    }

    public Dialog showProgressDialog(@NonNull Activity activity,
                                     boolean isCancle) {
        return create(activity, null, STYLE.PROGRESS, R.style.AnProgressDialog,
                activity.getString(R.string.listview_loading), true, isCancle);
    }

    /***
     * 创建含有ListView的Dialog
     */
    public Dialog createListDialog(@NonNull Activity activity,
                                   @NonNull List<String> listData,
                                   String txt,
                                   boolean mCancelable) {
        return create(activity, listData, STYLE.LIST, -1, txt, false, mCancelable);
    }

    /***
     * 创建含有ListView的Dialog
     *     /**
     * 当时ListView dialog时设置数据。
     * 参考数据形式。Exg：
     * <string-array name="ea_items">
     * //      <item>1</item>
     * //      <item>2</item>
     * // </string-array>
     */
    public Dialog createListDialog(@NonNull Activity activity,
                                   @NonNull int resId,
                                   String txt,
                                   boolean mCancelable) {
        String[] items = activity.getResources().getStringArray(resId);
        listData = Arrays.asList(items);
        return create(activity, listData, STYLE.LIST, -1, txt, false, mCancelable);
    }

    public Dialog createSuperDialog(@NonNull Activity activity,
                                    String txt,
                                    boolean mCancelable) {
        return create(activity, listData, STYLE.SUPER, -1, txt, true, mCancelable);

    }

    private Dialog create(@NonNull Activity activity,
                          List<String> listData,
                          @NonNull STYLE style,
                          @NonNull int progressTheme,
                          @NonNull String txt,
                          boolean shouldShow,
                          boolean isCancle) {
        this.mActivity = activity;
        if (listData != null) {
            this.listData = listData;
        }

        inflater = LayoutInflater.from(mActivity);
        // 根据风格得到不同的inflaterview
        if (style == STYLE.NORMAL) {
            inflateView = inflater.inflate(R.layout.base_standard_dialog, null);
            mDialog = new Dialog(mActivity, R.style.AnDialog);
        } else if (style == STYLE.SIMPLE) {
            inflateView = inflater.inflate(R.layout.base_standard_simple_dialog, null);
            mDialog = new Dialog(mActivity, R.style.AnSimpleDialog);
        } else if (style == STYLE.PROGRESS) {
            inflateView = inflater.inflate(R.layout.base_standard_dialog_progress, null);
            progressDialog = new ProgressDialog(activity, progressTheme);
        } else if (style == STYLE.LIST) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflateView = inflater.inflate(activity.getResources().getLayout(R.layout.base_standard_dialog_listview), null);
            mDialog = new Dialog(mActivity, R.style.AnListDialog);
        } else if (style == STYLE.SUPER) {
            inflater = LayoutInflater.from(mActivity);
            inflateView = inflater.inflate(R.layout.base_super_dialog, null);
            ProgressBar progressBar = inflateView.findViewById(R.id.anProgressUpdateBar);
            mDialog = new Dialog(mActivity, R.style.AnSimpleDialog);

//            mDialog = builder.create();
        } else {

        }

        TextView tipTextView = inflateView.findViewById(R.id.anDialogTv);
        ImageView divider = inflateView.findViewById(R.id.title_divider);
        if (TextUtils.isEmpty(txt)) {
            tipTextView.setVisibility(View.GONE);
            if (divider != null)
                divider.setVisibility(View.GONE);
        } else {
            tipTextView.setVisibility(View.VISIBLE);
            if (divider != null)
                divider.setVisibility(View.VISIBLE);
            tipTextView.setText(txt);
        }
        mDialog.setCancelable(isCancle);

        if (progressDialog != null) {
            progressDialog.setCanceledOnTouchOutside(isCancle);
            mDialog = progressDialog;
        }
        Window mWindow = mDialog.getWindow();

        if (shouldShow) {
            mDialog.show();
        }

        if (style == STYLE.NORMAL) {
            LinearLayout contentView = inflateView.findViewById(R.id.anLlHeadView);
            mDialog.setContentView(contentView, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            ImageView spaceshipImage = inflateView.findViewById(R.id.anDialogIv);
            // 加载动画
            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.base_anim_dialog_loading);
            // 使用ImageView显示动画
            spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        } else if (style == STYLE.SIMPLE) {
            mDialog.setContentView(inflateView);
            ImageView spaceshipImage = inflateView.findViewById(R.id.anDialogIv);
            if (mWindow != null) {
                mWindow.getAttributes().gravity = Gravity.CENTER;
            }
            animationDrawable = (AnimationDrawable) spaceshipImage.getBackground();
            if (animationDrawable != null && !animationDrawable.isRunning()) {
                animationDrawable.start();
            }
        } else if (style == STYLE.PROGRESS) {
            progressDialog.setContentView(R.layout.base_standard_dialog_progress);
            Window window = progressDialog.getWindow();
            if (mWindow != null) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = WindowManager.LayoutParams.WRAP_CONTENT;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(params);
            }
        } else if (style == STYLE.LIST) {
            /*初始化ListView的数据.*/
            createItemDialogView();

            mDialog.addContentView(inflateView,
                    new WindowManager.LayoutParams(
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.WRAP_CONTENT));
            mDialog.setContentView(inflateView);
            mDialog.setOnCancelListener(mOnCancelListener);
            if (mWindow != null) {
                mWindow.setGravity(Gravity.BOTTOM);
                WindowManager.LayoutParams attributes = mWindow.getAttributes();
                if (attributes != null) {
                    attributes.width = WindowManager.LayoutParams.MATCH_PARENT;

                    final float scale = activity.getResources().getDisplayMetrics().density;
                    attributes.y = (int) (8 * scale + 0.5f);
                    mWindow.setAttributes(attributes);
                }
            }
        } else if (style == STYLE.SUPER) {
            mDialog.addContentView(inflateView,
                    new WindowManager.LayoutParams(
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.WRAP_CONTENT));
            mDialog.setContentView(inflateView);
            mDialog.setOnCancelListener(mOnCancelListener);
            if (mWindow != null) {
                mWindow.setGravity(Gravity.BOTTOM);
                WindowManager.LayoutParams attributes = mWindow.getAttributes();
                if (attributes != null) {
                    attributes.width = WindowManager.LayoutParams.MATCH_PARENT;

                    final float scale = activity.getResources().getDisplayMetrics().density;
                    attributes.y = (int) (8 * scale + 0.5f);
                    mWindow.setAttributes(attributes);
                }
            }
        }
        return mDialog;
    }


    private void createItemDialogView() {
        final ListView listView = inflateView.findViewById(R.id.listView);
        Button btnOk = inflateView.findViewById(R.id.btnOk);
        Button btnCancel = inflateView.findViewById(R.id.btnCancel);

        CheckBox checkBox = inflateView.findViewById(R.id.checkbox);
        if (listData == null) {
            listData = new ArrayList<>();
        }
        adapter = new SListAdapter(listData);
        listView.setAdapter(adapter);
        /**
         * listView默认选择第一个。
         * */
        listView.setItemChecked(0, true);
        adapter.setChoosePosition(0);

        listView.setOnItemClickListener(new ItemClickListener(adapter, listView));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onclickCancel != null) {
                    onclickCancel.onClick(mDialog, -1);
                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onclickOk != null) {
                    onclickOk.onClick(mDialog, adapter.getChoosePosition());
                }
            }
        });

        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mOnCheckedChangeListener != null) {
                    mOnCheckedChangeListener.onCheckedChanged(buttonView, isChecked);
                }
            }
        });
    }

    private class ItemClickListener implements AdapterView.OnItemClickListener {
        private SListAdapter adapter;
        private ListView listView;

        ItemClickListener(SListAdapter adapter, ListView listView) {
            this.adapter = adapter;
            this.listView = listView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listView.setItemChecked(position, true);
            adapter.setChoosePosition(position);
            /**
             * 决定这里把接口往外面抛出去。先暂时要一个   position
             * */
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(position);
        }
    }

    /**
     * 隐藏掉DiaLog
     */
    public void cancelDialog() {
        if (animationDrawable != null && animationDrawable.isRunning())
            animationDrawable.stop();
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    /**
     * 显示一个Notification
     *
     * @param context   上下文对象。
     * @param showTitle 显示的标题
     * @param showInfo  显示的信息
     * @param channelId 型的信道通量
     * @param isCancel  是否可以取消
     * @return Notification
     */
    public Notification showNotification(@NonNull Context context,
                                         String showTitle,
                                         String showInfo,
                                         Intent intent,
                                         String channelId,
                                         boolean isCancel) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);//v7就ok
//        builder = new Notification.Builder(context);//v4就ok
        int smallIconId = R.drawable.base_drawable_circle_click;
//        Bitmap largeIcon = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_launcher)).getBitmap();//过时的解决方法。
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.umeng_socialize_fav);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap largeIcon = bitmapDrawable.getBitmap();
        builder.setLargeIcon(largeIcon)
                .setSmallIcon(smallIconId)
                .setContentTitle(showTitle)
                .setContentText(showInfo)
                .setTicker(showTitle)
                .setAutoCancel(isCancel)
                .setContentIntent(PendingIntent.getActivity(context, 0, intent, 0));
        int NOTIFICATION_START = 99;
        Notification n = builder.build();
        nm.notify(NOTIFICATION_START, n);
        return n;
    }
}
