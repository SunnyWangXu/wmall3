package com.wjhgw.ui.activity;

import android.os.Bundle;

import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;

/**
 * 管理收货地址
 */
public class Manage_Address_Activity  extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_address_layout);

    }

    @Override
    public void onInit() {
        setUp();
        setTitle("收货地址管理");

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
