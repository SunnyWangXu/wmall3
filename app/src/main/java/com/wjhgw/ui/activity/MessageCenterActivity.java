package com.wjhgw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;

/**
 * 消息中心Activity
 */
public class MessageCenterActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llUseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center);
    }

    @Override
    public void onInit() {

        setUp();
        setTitle("消息中心");
    }

    @Override
    public void onFindViews() {
        llUseService = (LinearLayout) findViewById(R.id.ll_service);
    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {

        llUseService.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ll_service:
                intent.setClass(this, C4_UseServiceActivity.class);
                startActivity(intent);

                break;

            default:
                break;
        }
    }
}
