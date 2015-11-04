package com.wjhgw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.bean.MyLockBox;
import com.wjhgw.business.bean.MyLockBoxData;
import com.wjhgw.config.ApiInterface;

/**
 * 个人资料管理Activity
 */
public class MyLockBoxActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_nickname;
    private TextView tv_Nickname;
    private Intent intent;
    private ImageView iv_Avatar;
    private TextView tv_UseName;
    private TextView tv_Passwd_Strength;
    private TextView tv_Paypwd;
    private TextView tv_Mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_lockbox_layout);

        /**
         * 请求个人资料
         */
        loadMyLockBox();
    }


    @Override
    public void onInit() {
        //finish
        setUp();
        setTitle("个人资料管理");
    }

    @Override
    public void onFindViews() {
        iv_Avatar = (ImageView) findViewById(R.id.iv_avatar);
        tv_UseName = (TextView) findViewById(R.id.tv_usename);
        tv_Passwd_Strength = (TextView) findViewById(R.id.tv_passwd_strength);
        tv_Paypwd = (TextView) findViewById(R.id.tv_paypwd);
        tv_Mobile = (TextView) findViewById(R.id.tv_mobile);

    }

    @Override
    public void onInitViewData() {
        ll_nickname = (LinearLayout) findViewById(R.id.ll_nickname);
        tv_Nickname = (TextView) findViewById(R.id.tv_nickname);

    }

    @Override
    public void onBindListener() {
        ll_nickname.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_nickname:
                intent = new Intent(this, Modify_nicknameActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 请求个人资料
     */
    private void loadMyLockBox() {
        String key = getSharedPreferences("key", MODE_APPEND).getString("key", "0");
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.MyLockBox, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (null != responseInfo) {
                    MyLockBox myLockBox = gson.fromJson(responseInfo.result, MyLockBox.class);
                    if (myLockBox.status.code == 10000) {
                        MyLockBoxData myLockBoxData = myLockBox.datas;

                        APP.getApp().getImageLoader().displayImage(myLockBoxData.member_avatar, iv_Avatar);
                        tv_UseName.setText(myLockBoxData.member_name);
                        tv_Nickname.setText(myLockBoxData.member_nickname);
                        if (myLockBoxData.passwd_strength.equals("0")) {
                            tv_Passwd_Strength.setText("弱");
                        } else if (myLockBoxData.passwd_strength.equals("1")) {
                            tv_Passwd_Strength.setText("中");
                        } else if (myLockBoxData.passwd_strength.equals("2")) {
                            tv_Passwd_Strength.setText("强");
                        }

                        if(myLockBoxData.paypwd.equals("0")){
                            tv_Paypwd.setText("未设置");
                        }else{
                            tv_Paypwd.setText("已设置");
                        }
                        tv_Mobile.setText(myLockBoxData.member_mobile);

                    }

                }

            }

            @Override
            public void onFailure(HttpException e, String s) {

                showToastShort("网络错误");
            }

        });
    }
}