package com.wjhgw.ui.activity;

import android.os.Bundle;

import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;

/**
 * 意见反馈Activity
 */
public class M8_MyHelpActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("意见反馈");
    }

    @Override
    public void onFindViews() {

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {

    }
}
