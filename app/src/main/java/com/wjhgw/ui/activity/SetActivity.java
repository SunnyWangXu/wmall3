package com.wjhgw.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.config.ApiInterface;

/**
 * 设置的Activity  2015/11/24 0024.
 */
public class SetActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivShare;
    private ImageView ivPush;
    private int PushCount = 1;
    private Button btnExit;
    private String memberName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("设置");
        memberName =  getIntent().getStringExtra("memberName");
    }

    @Override
    public void onFindViews() {
        ivShare = (ImageView) findViewById(R.id.iv_title_right);
        ivPush = (ImageView) findViewById(R.id.iv_push);
        btnExit = (Button) findViewById(R.id.btn_exit);
    }

    @Override
    public void onInitViewData() {
        ivShare.setImageResource(R.mipmap.ic_share);
        ivShare.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBindListener() {
        ivPush.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_push:
                PushCount++;
                if (PushCount % 2 == 1) {
                    ivPush.setImageResource(R.mipmap.ic_push_off);
                } else if (PushCount % 2 == 0) {
                    ivPush.setImageResource(R.mipmap.ic_push_on);
                }
                break;
            case R.id.btn_exit:
                 exitLogin();

                break;

            default:
                break;
        }
    }

    /**
     * 退出登录
     */
    private void exitLogin() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("member_name",memberName);
        params.addBodyParameter("key",getKey());
        params.addBodyParameter("client","android");

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Exit_login, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                showToastShort("已退出登录");
                finish();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToastShort("网络错误");
            }
        });
    }
}
