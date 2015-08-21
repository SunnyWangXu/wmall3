package com.wjhgw.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjhgw.R;

/**
 * Created by Lance on 15/8/21.
 */
public class NavigationActionBar extends LinearLayout {
    private LinearLayout actionBarTitle;
    private LinearLayout actionBarLeft;
    private LinearLayout actionBarRight;

    public NavigationActionBar(Context context) {
        this(context, null);
    }

    public NavigationActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.actionBarTitle = (LinearLayout) this.findViewById(R.id.actionBarTitle);
        this.actionBarLeft = (LinearLayout) this.findViewById(R.id.actionBarLeft);
        this.actionBarRight = (LinearLayout) this.findViewById(R.id.actionBarRight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void setTitle(String title) {
        TextView titleView = new TextView(this.getContext());
        titleView.setText(title);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTextColor(Color.parseColor("white3"));
        this.setTitleItem(titleView);
    }

    public void setTitleItem(View view) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.setTitleItem(view, layoutParams);
    }

    public void setTitleItem(View view, LayoutParams layoutParams) {
        this.actionBarTitle.removeAllViews();
        this.actionBarTitle.addView(view, layoutParams);
    }

    public void setLeftItem(View view) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.setLeftItem(view, layoutParams);
    }

    public void setLeftItem(View view, LayoutParams layoutParams) {
        this.actionBarLeft.removeAllViews();
        this.actionBarLeft.addView(view, layoutParams);
    }

    public void setRightItem(View view) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.setRightItem(view, layoutParams);
    }

    public void setRightItem(View view, LayoutParams layoutParams) {
        this.actionBarRight.removeAllViews();
        this.actionBarRight.addView(view, layoutParams);
    }
}
