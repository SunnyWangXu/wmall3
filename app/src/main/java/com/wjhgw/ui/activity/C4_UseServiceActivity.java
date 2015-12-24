package com.wjhgw.ui.activity;

import android.os.Bundle;

import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;

/**
 * 客户反馈的Activity
 */
public class C4_UseServiceActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_service);


    }

    @Override
    public void onInit() {
        setUp();
        setTitle("客服反馈");

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
