package com.wjhgw.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.wjhgw.business.bean.Status;
import com.wjhgw.config.ApiInterface;

/**
 * 开启支付密码
 */
public class M4_PaymentPasswordActivity extends BaseActivity implements OnClickListener {

    private LinearLayout ll_password3;
    private EditText et_password2;
    private EditText et_password3;
    private ImageView iv_delete2;
    private ImageView iv_delete3;
    private TextView change_password;
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m4_payment_password_layout);

        et_password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 0) {
                    iv_delete2.setVisibility(View.VISIBLE);
                } else {
                    if (start == 0 && count > 0) {
                        iv_delete2.setVisibility(View.VISIBLE);
                    } else {
                        iv_delete2.setVisibility(View.GONE);
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
        et_password3.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 0) {
                    iv_delete3.setVisibility(View.VISIBLE);
                    ll_password3.setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else {
                    if (start == 0 && count > 0) {
                        iv_delete3.setVisibility(View.VISIBLE);
                    } else {
                        iv_delete3.setVisibility(View.GONE);
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
        /*et_password3.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ll_password3.setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else {

                }
            }
        });*/
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onInit() {
        setUp();
        if(getIntent().getStringExtra("paypwd").equals("0")){
            setTitle("支付密码");
        }else {
            setTitle("修改支付密码");
        }
    }

    @Override
    public void onFindViews() {
        et_password2 = (EditText) findViewById(R.id.et_password2);
        et_password3 = (EditText) findViewById(R.id.et_password3);
        iv_delete2 = (ImageView) findViewById(R.id.iv_delete2);
        iv_delete3 = (ImageView) findViewById(R.id.iv_delete3);
        ll_password3 = (LinearLayout) findViewById(R.id.ll_password3);
        change_password = (TextView) findViewById(R.id.change_password);

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        iv_delete2.setOnClickListener(this);
        iv_delete3.setOnClickListener(this);
        change_password.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_delete2:
                et_password2.setText("");
                iv_delete2.setVisibility(View.GONE);
                break;
            case R.id.iv_delete3:
                et_password3.setText("");
                iv_delete3.setVisibility(View.GONE);
                break;
            case R.id.change_password:
                String password2 = et_password2.getText().toString();
                String password3 = et_password3.getText().toString();

                if (password2.equals(password3) && et_password3.length() == 6) {
                    String key = getKey();
                    RequestParams params = new RequestParams();
                    if (!key.equals("0")) {
                        params.addBodyParameter("paypwd", password2);
                        params.addBodyParameter("key", key);
                        Set_paypwd(params);
                    } else {
                        showToastShort("未登录");
                    }
                } else {
                    showToastShort("密码长度太短、新密码不一致、原密码验证失败或错误");
                    ll_password3.setBackgroundResource(R.drawable.background_red);
                }

                break;
            default:
                break;
        }

    }

    /**
     * 设置支付密码
     */
    private void Set_paypwd(RequestParams params) {
        super.StartLoading();
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Set_paypwd, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                M4_PaymentPasswordActivity.super.Dismiss();
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);
                    if (status.status.code == 10000) {
                        finish(false);
                        showToastShort(status.status.msg);
                    }else {
                        overtime(status.status.code, status.status.msg);
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
