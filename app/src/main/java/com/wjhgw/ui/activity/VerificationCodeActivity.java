package com.wjhgw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

/**
 * 通用的获取手机验证和
 */
public class VerificationCodeActivity extends BaseActivity implements OnClickListener {

    private EditText et_editText;
    private ImageView iv_delete;
    private TextView tv_next;
    private TextView tv_verificationcode;
    private TextView tv_state;

    private String Number;
    private String Verification_code;
    private TimeCount time;
    Intent intent;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_code_layout);

        et_editText.addTextChangedListener(new TextWatcher() {
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
        tv_state.setText("请输入手机号码 " + Number + " 收到的验证码");
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        time.start();
        tv_verificationcode.setBackgroundColor(0xffcccccc);
        key = getKey();

        Verification_code();
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("验证验证码");

    }

    @Override
    public void onFindViews() {
        et_editText = (EditText) findViewById(R.id.et_editext);
        tv_verificationcode = (TextView) findViewById(R.id.tv_verificationcode);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_next = (TextView) findViewById(R.id.tv_next);
    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        iv_delete.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        tv_verificationcode.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_delete:
                et_editText.setText("");
                iv_delete.setVisibility(View.GONE);
                break;
            case R.id.tv_next:
                Verification_code = et_editText.getText().toString();
                if (Verification_code.length() == 4) {
                    //tv_next.setClickable(false);
                    Number_Verification_code();
                } else {
                    showToastShort("你的输入有误！请重新输入");
                }
                break;
            case R.id.tv_verificationcode:
                if (Number.length() == 11 && Number.substring(0, 1).equals("1")) {
                    time.start();
                    tv_verificationcode.setBackgroundColor(0xffcccccc);
                    Verification_code();
                } else {
                    showToastShort("请先输入正确的11位手机号获取短信验证码");
                }
                break;
            case R.id.iv_title_back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 请求发送验证码
     */
    private void Verification_code() {
        RequestParams params = new RequestParams();
        if (key.length() > 1) {
            params.addBodyParameter("key", key);
        } else {
            params.addBodyParameter("member_mobile", Number);
        }
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.VerificationCode, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);

                    if (status.status.code == 10000) {
                        showToastShort("验证码以短信形式发送到你的手机，60秒有效");
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

    /**
     * 验证验证码
     */
    private void Number_Verification_code() {
        super.StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("sms_code", Verification_code);
        if (key.length() > 1) {
            params.addBodyParameter("key", key);
        }

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.VerificationNumber, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                VerificationCodeActivity.super.Dismiss();
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);

                    if (status.status.code == 10000) {
                        if (getIntent().getStringExtra("use").equals("0")) {
                            intent = new Intent(VerificationCodeActivity.this, A2_ResetPassActivity2.class);
                            intent.putExtra("Number", Number);
                            startActivity(intent);
                            finish(false);
                        } else if (getIntent().getStringExtra("use").equals("1")) {
                            intent = new Intent(VerificationCodeActivity.this, A1_RegisterActivity2.class);
                            intent.putExtra("Number", Number);
                            startActivity(intent);
                            finish(false);
                        } else if (getIntent().getStringExtra("use").equals("2")) {
                            intent = new Intent(VerificationCodeActivity.this, M4_PaymentPasswordActivity.class);
                            intent.putExtra("Number", Number);
                            intent.putExtra("paypwd", getIntent().getStringExtra("paypwd"));
                            startActivity(intent);
                            finish(false);
                        } else if (getIntent().getStringExtra("use").equals("3")) {
                            intent = new Intent(VerificationCodeActivity.this, M5_Change_mobileActivity.class);
                            startActivity(intent);
                            finish(false);
                        }
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

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            tv_verificationcode.setBackgroundColor(0xfff25252);
            tv_verificationcode.setText("重新获取");
            tv_verificationcode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            tv_verificationcode.setClickable(false);
            tv_verificationcode.setText(millisUntilFinished / 1000 + "秒");
        }
    }
}
