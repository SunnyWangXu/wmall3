package com.wjhgw.guide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.wjhgw.MainActivity;
import com.wjhgw.R;

public class GuideActivity extends Activity implements
        OnViewChangeListener, OnClickListener {

    private GuideScrollLayout mScrollLayout;

    private ImageView[] mImageViews;
    private ImageView shop;

    private int mViewCount;

    private int mCurSel;
    Button button1;

    /**
     * Activity对象
     **/
    public static Activity MY_ACTIVITY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_layout);
        button1 = (Button) findViewById(R.id.button1);
        shop = (ImageView) findViewById(R.id.shop);
        shop.setOnClickListener(new OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        button1.setOnClickListener(new OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        MY_ACTIVITY = this;

        init();
    }

    private void init() {
        mScrollLayout = (GuideScrollLayout) findViewById(R.id.ScrollLayout);

        //LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llayout);

        mViewCount = mScrollLayout.getChildCount();
        //mImageViews = new ImageView[mViewCount];

		/*for (int i = 0; i < mViewCount; i++) {
            mImageViews[i] = (ImageView) linearLayout.getChildAt(i);
			mImageViews[i].setEnabled(true);
			mImageViews[i].setOnClickListener(this);
			mImageViews[i].setTag(i);
		}*/

        mScrollLayout.setPageSize(mViewCount);
        mCurSel = 0;
        //mImageViews[mCurSel].setEnabled(false);

        mScrollLayout.SetOnViewChangeListener(this);
    }

    private void setCurPoint(int index) {
        if (index < 0 || index > mViewCount - 1 || mCurSel == index) {
            return;
        }

        //mImageViews[mCurSel].setEnabled(true);
        //mImageViews[index].setEnabled(false);
        mScrollLayout.setPosition(index);
        mCurSel = index;
    }

    @Override
    public void OnViewChange(int view) {
        setCurPoint(view);
    }

    @Override
    public void onClick(View v) {
        int pos = (Integer) (v.getTag());
        setCurPoint(pos);
        mScrollLayout.snapToScreen(pos);
    }
}