package com.wjhgw.ui.activity;

import android.os.Bundle;

import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;

/**
 * 确认订单Activity
 */
public class ConfirmOrderActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);


    }

    @Override
    public void onInit() {
        setUp();
        setTitle("确认订单");
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
