package com.wjhgw.ui.activity;

import android.os.Bundle;

import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;

/**
 * 赠送单人Activity
 */
public class J2_GiveOneActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_one);

    }

    @Override
    public void onInit() {
        setUp();
        setTitle("赠送单人");

       getLayoutInflater().inflate(R.layout.give_one_header, null);
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
