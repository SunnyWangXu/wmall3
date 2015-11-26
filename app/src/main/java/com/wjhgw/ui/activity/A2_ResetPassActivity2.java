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
 * 重置密码
 */
public class A2_ResetPassActivity2 extends BaseActivity implements OnClickListener {

    private EditText et_cipher;
    private ImageView iv_delete;
    private TextView tv_next;

    private String Number;
    private String cipher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a2_reset_pass_layout2);

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
        setUp();
        setTitle("密码找回");

    }

    @Override
    public void onFindViews() {
        et_cipher = (EditText) findViewById(R.id.et_a2_cipher);
        iv_delete = (ImageView) findViewById(R.id.iv_a2_delete1);
        tv_next = (TextView) findViewById(R.id.tv_a2_next1);

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
            case R.id.iv_a0_delete1:
                et_cipher.setText("");
                iv_delete.setVisibility(View.GONE);
                break;
            case R.id.tv_a2_next1:
                String strength = null;
                cipher = et_cipher.getText().toString();
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
                    ResetPassword(strength);
                } else {
                    showToastShort("密码输入有误");
                }
                break;
            case R.id.iv_title_back:
                finish(false);
                break;

            default:
                break;
        }

    }

    /**
     * 重设密码
     */
    private void ResetPassword(String strength) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("member_mobile", Number);
        params.addBodyParameter("password", cipher);
        params.addBodyParameter("passwd_strength", strength);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.ResetPassword, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
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
