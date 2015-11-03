package com.wjhgw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;

/**
 * 个人资料管理Activity
 */
public class MyLockBoxActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_nickname;
    private TextView tv_nickname;
    private Intent intent;

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
        ll_nickname = (LinearLayout)findViewById(R.id.ll_nickname);
        tv_nickname = (TextView)findViewById(R.id.tv_nickname);

    }

    @Override
    public void onBindListener() {
        ll_nickname.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_nickname:
                intent = new Intent(this,Modify_nicknameActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
