package com.wjhgw.ui.activity;

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
import com.wjhgw.business.bean.Status;
import com.wjhgw.config.ApiInterface;

import java.util.regex.Pattern;

/**
 * 注册
 */
public class A1_RegisterActivity2 extends BaseActivity implements OnClickListener {

    private EditText et_cipher;
    private ImageView iv_delete;
    private TextView tv_next;

    private String Number;
    private String cipher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a1_register_layout2);

        et_cipher.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 0) {
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

        Number = getIntent().getStringExtra("Number");

    }

    @Override
    public void onInit() {
        setTitle("注册");
        setUp();

    }

    @Override
    public void onFindViews() {
        et_cipher = (EditText) findViewById(R.id.et_a1_cipher);
        iv_delete = (ImageView) findViewById(R.id.iv_a1_delete);
        //a2_return = (ImageView)findViewById(R.id.a2_return);
        tv_next = (TextView) findViewById(R.id.tv_a1_next);

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        iv_delete.setOnClickListener(this);
        tv_next.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_a1_delete:
                et_cipher.setText("");
                iv_delete.setVisibility(View.GONE);
                break;
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_a1_next:
                cipher = et_cipher.getText().toString();
                String strength = null;
                if (cipher.length() > 5) {
                    if (Pattern.compile("^[A-Za-z0-9]+").matcher(cipher).matches()) {
                        if (Pattern.compile("^\\d+$").matcher(cipher).matches()) {
                            strength = "0";
                        } else if (Pattern.compile("^[A-Za-z]+$").matcher(cipher).matches()) {
                            strength = "1";
                        } else {
                            strength = "2";
                        }
                    }
                    Registered(strength);
                } else {
                    showToastShort("密码输入有误");
                }
                break;

            default:
                break;
        }

    }

    /**
     * 注册
     */
    private void Registered(String strength) {
        super.StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("member_mobile", Number);
        params.addBodyParameter("password", cipher);
        params.addBodyParameter("client", "android");
        params.addBodyParameter("passwd_strength", strength);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Registered, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                A1_RegisterActivity2.super.Dismiss();
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);

                    if (status.status.code == 10000) {
                        showToastShort(status.status.msg);
                        finish(false);
                    } else {
                        showToastShort(status.status.msg);
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
