package com.nebula.model.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nebula.R;

/**
 * 自定义视图提供给SListAdapter使用。
 */

public class SItemView extends RelativeLayout implements Checkable {
    private boolean mCheckable;
    private TextView mNameText;
    private ImageView iv_choose;

    public SItemView(Context context) {
        this(context, null);
    }

    public SItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_item_normal_listview, this);
        mNameText = findViewById(R.id.tvTitile);
        iv_choose = findViewById(R.id.iv_choose);
    }

    @Override
    public void setChecked(boolean checked) {
        mCheckable = checked;
        iv_choose.setSelected(checked);
    }

    @Override
    public boolean isChecked() {
        return mCheckable;
    }

    @Override
    public void toggle() {
        setChecked(!mCheckable);
    }

    public void setItemName(String name) {
        if (!TextUtils.isEmpty(name)) {
            mNameText.setText(name);
        } else {
            mNameText.setText("");
        }
    }
}
