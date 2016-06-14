package com.wjhgw.ui.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;

import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;

/**
 * 微信登录Activiy
 */
public class A3_WXLoginActivity extends BaseActivity{
    private TextView tvTologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_login);


    }

    @Override
    public void onInit() {

        setUp();
        setTitle("微信登录");
    }

    @Override
    public void onFindViews() {
        tvTologin = (TextView) findViewById(R.id.tv_tologin);
        tvTologin.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        tvTologin.getPaint().setAntiAlias(true);//抗锯齿

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {

    }
}
