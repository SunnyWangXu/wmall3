package com.wjhgw.ui.activity;

import android.os.Bundle;

import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;

/**
 * 个人资料管理Activity
 */
public class MyLockBoxActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_lockbox_layout);


    }

    @Override
    public void onInit() {
        //finish
        setUp();
        setTitle("个人资料管理");
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
