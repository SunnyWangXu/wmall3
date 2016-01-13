package com.wjhgw.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
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

import java.util.regex.Pattern;

/**
 * 修改密码
 */
public class M3_ChangePasswordActivity extends BaseActivity implements OnClickListener {

    private LinearLayout ll_password1;
    private LinearLayout ll_password2;
    private LinearLayout ll_password3;
    private EditText et_password1;
    private EditText et_password2;
    private EditText et_password3;
    private ImageView iv_delete1;
    private ImageView iv_delete2;
    private ImageView iv_delete3;
    private TextView change_password;
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m3_change_password_layout);

        et_password1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start != 0) {
                    iv_delete1.setVisibility(View.VISIBLE);
                    ll_password1.setBackgroundColor(Color.parseColor("#FFFFFF"));
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

        et_password1.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    String password1 = et_password1.getText().toString();
                    if (password1.length() > 5) {
                        load_check_passwor(password1);
                    } else {
                        ll_password1.setBackgroundResource(R.drawable.background_red);
                        showToastShort("原登录密码输入有误");
                    }
                }
            }
        });
        et_password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 0) {
                    iv_delete2.setVisibility(View.VISIBLE);
                    ll_password2.setBackgroundColor(Color.parseColor("#FFFFFF"));
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
        et_password2.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    String password1 = et_password2.getText().toString();
                    if (password1.length() > 5 && !password1.equals(password)) {

                    } else {
                        if(password.length() > 5){
                            showToastShort("新登录密码输入有误");
                            ll_password2.setBackgroundResource(R.drawable.background_red);
                        }else {
                            showToastShort("原登录密码输入有误");
                        }
                    }
                }
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
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("修改密码");

    }

    @Override
    public void onFindViews() {
        et_password1 = (EditText) findViewById(R.id.et_password1);
        et_password2 = (EditText) findViewById(R.id.et_password2);
        et_password3 = (EditText) findViewById(R.id.et_password3);
        iv_delete1 = (ImageView) findViewById(R.id.iv_delete1);
        iv_delete2 = (ImageView) findViewById(R.id.iv_delete2);
        iv_delete3 = (ImageView) findViewById(R.id.iv_delete3);
        ll_password1 = (LinearLayout) findViewById(R.id.ll_password1);
        ll_password2 = (LinearLayout) findViewById(R.id.ll_password2);
        ll_password3 = (LinearLayout) findViewById(R.id.ll_password3);
        change_password = (TextView) findViewById(R.id.change_password);

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        iv_delete1.setOnClickListener(this);
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
            case R.id.iv_delete1:
                et_password1.setText("");
                iv_delete1.setVisibility(View.GONE);
                break;
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
                //保存密码强度
                String strength = null;
                if (password.length() > 5) {
                    if (password2.length() > 5 && !password2.equals(password)) {
                        if (password2.equals(password3) && et_password3.length() >= 6) {
                            if (Pattern.compile("^[A-Za-z0-9]+").matcher(password3).matches()) {
                                if (Pattern.compile("^\\d+$").matcher(password3).matches()) {
                                    strength = "0";
                                } else if (Pattern.compile("^[A-Za-z]+$").matcher(password3).matches()) {
                                    strength = "1";
                                } else {
                                    strength = "2";
                                }
                            }
                            String key = getKey();
                            RequestParams params = new RequestParams();
                            if (!key.equals("0")) {
                                params.addBodyParameter("passwd_strength", strength);
                                params.addBodyParameter("member_old_password", password);
                                params.addBodyParameter("member_new_password", password2);
                                params.addBodyParameter("key", key);
                                change_pwd(params);
                            } else {
                                showToastShort("未登录");
                            }
                        } else {
                            showToastShort("确认新登录密码输入有误");
                            ll_password3.setBackgroundResource(R.drawable.background_red);
                        }
                    } else {
                        ll_password2.setBackgroundResource(R.drawable.background_red);
                        showToastShort("新登录密码输入有误");
                    }
                } else {
                    ll_password1.setBackgroundResource(R.drawable.background_red);
                    showToastShort("原登录密码输入有误");
                }

                break;
            default:
                break;
        }

    }

    /**
     * 校验登录密码
     */
    private void load_check_passwor(String password1) {
        String key = getKey();
        RequestParams params = new RequestParams();
        if (!key.equals("0")) {
            params.addBodyParameter("password", password1);
            params.addBodyParameter("key", key);
        }
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Check_password, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);
                    if (status.status.code == 10000) {
                        password = et_password1.getText().toString();
                    }else {
                        overtime(status.status.code, status.status.msg);
                        ll_password1.setBackgroundResource(R.drawable.background_red);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToastShort("请求失败");
            }
        });
    }

    /**
     * 修改登录密码
     */
    private void change_pwd(RequestParams params) {
        super.StartLoading();
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Change_pwd, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                M3_ChangePasswordActivity.super.Dismiss();
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);

                    if (status.status.code == 10000) {
                        finish(false);
                        showToastShort(status.status.msg);
                    } else {
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
