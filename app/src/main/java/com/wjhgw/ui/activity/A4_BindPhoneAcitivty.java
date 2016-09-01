package com.wjhgw.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by Administrator on 2016/9/1 0001.
 * 账号绑定手机
 */
public class A4_BindPhoneAcitivty extends BaseActivity implements View.OnClickListener, TextWatcher {
    private TextView tvSkip;
    private TimeCount time;
    private TextView tvAuthCode;
    private EditText edValidate;
    private Button btnComplete;
    private EditText edPhone;
    private String number;
    private String memberName;
    private TextView tvMemberName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);


    }

    @Override
    public void onInit() {
        tvMemberName = (TextView) findViewById(R.id.tv_memberName);
        memberName = getIntent().getStringExtra("member_name");
        tvMemberName.setText(memberName);

    }

    @Override
    public void onFindViews() {
        tvSkip = (TextView) findViewById(R.id.tv_skip);

        tvAuthCode = (TextView) findViewById(R.id.tv_bind_auth_code);
        edValidate = (EditText) findViewById(R.id.ed_bind_validate);
        btnComplete = (Button) findViewById(R.id.btn_bind_complete);
        edPhone = (EditText) findViewById(R.id.ed_mobile_phone);
    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        tvSkip.setOnClickListener(this);
        tvAuthCode.setOnClickListener(this);
        edValidate.addTextChangedListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_skip:

                finish();

                break;
            case R.id.tv_auth_code:
                number = edPhone.getText().toString();
                if (number.length() == 11 && number.substring(0, 1).equals("1")) {
                    /**
                     * 请求发送验证码
                     */
                    Verification_code();
                }
                break;

            case R.id.btn_bind_complete:
                /**
                 * 验证验证码，验证码正确才去绑定手机号
                 */
                checkValidate();

                break;

            default:
                break;
        }
    }

    /**
     * 请求发送验证码
     */
    private void Verification_code() {
        super.StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("member_mobile", number);
        params.addBodyParameter("key", getKey());

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.VerificationCode, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dismiss();
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);

                    if (status.status.code == 10000) {
                        edValidate.setFocusable(true);//设置输入框可聚集
                        edValidate.setFocusableInTouchMode(true);//设置触摸聚焦
                        edValidate.requestFocus();//请求焦点
                        edValidate.findFocus();//获取焦点
                        showToastShort("验证码以短信形式发送到你的手机，60秒有效");
                        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
                        time.start();
                    } else {
                        overtime(status.status.code, status.status.msg);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }


    /**
     * 验证手机验证码是否正确
     */
    private void checkValidate() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("sms_code", edValidate.getText().toString());
        params.addBodyParameter("key", getKey());

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.VerificationNumber, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {


                Gson gson = new Gson();
                if (responseInfo != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);

                    if (status.status.code == 10000) {

                        /**
                         * 验证码验证成功去绑定账号和手机
                         */
                        loadBindMobile();

                    } else {
                        overtime(status.status.code, status.status.msg);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }

    /**
     * 验证码验证成功去绑定账号和手机
     */
    private void loadBindMobile() {

        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("member_mobile", number);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Bind_mobile, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                Status status = gson.fromJson(responseInfo.result, Status.class);
                if (status.status.code == 10000) {
                    showToastShort("手机号绑定成功");
                    finish();

                } else {
                    overtime(status.status.code, status.status.msg);
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (edValidate.getText().toString().length() == 4) {
            btnComplete.setBackgroundColor(Color.parseColor("#f25252"));
            btnComplete.setClickable(true);
            btnComplete.setOnClickListener(this);
        } else {
            btnComplete.setBackgroundColor(Color.parseColor("#cccccc"));
            btnComplete.setClickable(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            tvAuthCode.setText("重新获取");
            tvAuthCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            tvAuthCode.setClickable(false);
            tvAuthCode.setText(millisUntilFinished / 1000 + "秒");
        }
    }
}
