package com.wjhgw.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.wjhgw.business.bean.Login_Pager;
import com.wjhgw.config.ApiInterface;

/**
 * 登录
 */
public class A0_LoginActivity extends BaseActivity implements OnClickListener {

    private EditText et_name;
    private EditText et_password;
    private ImageView iv_delete;
    private ImageView iv_delete1;
    private TextView tv_next;
    private TextView tv_registered;
    private TextView tv_back;
    private TextView tv_a0_tback;
    private String Number;
    private String password;
    Intent intent;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a0_login_layout);


        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start != 0) {
                    iv_delete.setVisibility(View.VISIBLE);
                } else {
                    if (start == 0 && count > 0) {
                        iv_delete.setVisibility(View.VISIBLE);
                    } else {
                        iv_delete.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 0) {
                    iv_delete1.setVisibility(View.VISIBLE);
                } else {
                    if (start == 0 && count > 0) {
                        iv_delete1.setVisibility(View.VISIBLE);
                    } else {
                        iv_delete1.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onInit() {

        setUp();
        setTitle("登录");

    }

    @Override
    public void onFindViews() {
        et_name = (EditText) findViewById(R.id.et_a0_name);
        et_password = (EditText) findViewById(R.id.et_a0_password);
        iv_delete = (ImageView) findViewById(R.id.iv_a0_delete);
        iv_delete1 = (ImageView) findViewById(R.id.iv_a0_delete1);
        tv_next = (TextView) findViewById(R.id.tv_a0_next);
        tv_registered = (TextView) findViewById(R.id.tv_a0_registered);
        tv_a0_tback = (TextView) findViewById(R.id.tv_a0_tback);
        tv_back = (TextView) findViewById(R.id.tv_a0_tback);

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        iv_delete.setOnClickListener(this);
        iv_delete1.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        tv_registered.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        tv_a0_tback.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_a0_delete:
                et_name.setText("");
                iv_delete.setVisibility(View.GONE);
                break;
            case R.id.iv_a0_delete1:
                et_password.setText("");
                iv_delete1.setVisibility(View.GONE);
                break;
            case R.id.tv_a0_next:
                super.StartLoading();
                Number = et_name.getText().toString();
                password = et_password.getText().toString();
                if (Number.length() == 11 && Number.substring(0, 1).equals("1")) {
                    login();
                } else {
                    showToastShort("你输入的号码有误！请重新输入");
                }
                break;
            case R.id.tv_a0_registered:
                intent = new Intent(this, A1_RegisterActivity1.class);
                startActivity(intent);
                break;
            case R.id.tv_a0_tback:
                intent = new Intent(this, A2_ResetPassActivity1.class);
                startActivity(intent);
                break;
            case R.id.iv_title_back:
                finish();
                break;

            default:
                break;
        }

    }

    /**
     * 登录网络请求
     */
    private void login() {
        super.StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("member_mobile", Number);
        params.addBodyParameter("password", password);
        params.addBodyParameter("client", "android");
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Login, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                A0_LoginActivity.super.Dismiss();
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Login_Pager login = gson.fromJson(responseInfo.result, Login_Pager.class);

                    if (login.status.code == 10000) {
                        preferences = getSharedPreferences("key", MODE_PRIVATE);
                        Editor editor = preferences.edit();
                        //存入数据
                        editor.putString("key", login.datas.key);
                        //提交修改
                        editor.commit();
                        /*//读取出来
                        preferences.getString("key", "0");
                        intent = new Intent(A0_LoginActivity.this, MainActivity.class);
                        startActivity(intent);*/
                        showToastShort("登录成功");
                        finish(false);
                    }else {
                        showToastShort(login.status.msg);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToastShort("请求失败");
            }
        });
    }
}
