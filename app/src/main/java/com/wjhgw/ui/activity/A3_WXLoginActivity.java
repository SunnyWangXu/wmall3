package com.wjhgw.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.wjhgw.business.bean.BindWX;
import com.wjhgw.business.bean.Status;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.DiyView.RoundImageView;

/**
 * 微信登录Activiy
 */
public class A3_WXLoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private EditText tvWXPhone;
    private TextView tvAuthCode;
    private TimeCount time;
    private String number;
    private String unionid;
    //手机号码是否在本平台注册
    private boolean isValidate;
    private String nickname;
    private String headimgurl;
    private RoundImageView ivHeaderImg;
    private TextView tvWXNickname;
    private EditText edValidate;
    private Button btnComplete;
    private LinearLayout llPassword4;
    private LinearLayout llPhoneValidate;
    private BindWX bindWX;
    private Button btnCompletePass;
    private EditText edValidatePass;
    private TextView tvBindStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_login);

        unionid = getIntent().getStringExtra("unionid");
        nickname = getIntent().getStringExtra("nickname");
        headimgurl = getIntent().getStringExtra("headimgurl");
        APP.getApp().getImageLoader().displayImage(headimgurl, ivHeaderImg);
        tvWXNickname.setText("亲爱的用户：" + nickname);

        edValidate.setFocusable(false);
        btnComplete.setClickable(false);

        /**
         * 监听输入密码
         */
        edValidatePass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (edValidatePass.getText().toString().length() >= 6) {

                    btnCompletePass.setBackgroundColor(Color.parseColor("#f25252"));
                    btnCompletePass.setClickable(true);

                } else {
                    btnCompletePass.setBackgroundColor(Color.parseColor("#cccccc"));
                    btnCompletePass.setClickable(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onInit() {

        setUp();
        setTitle("微信登录");
    }

    @Override
    public void onFindViews() {

        ivHeaderImg = (RoundImageView) findViewById(R.id.iv_header);
        tvWXNickname = (TextView) findViewById(R.id.tv_wx_nickname);
        tvBindStatus = (TextView) findViewById(R.id.tv_bind_status);
        edValidatePass = (EditText) findViewById(R.id.ed_validate_pass);
        tvWXPhone = (EditText) findViewById(R.id.ed_wx_phone);
        edValidate = (EditText) findViewById(R.id.ed_validate);
        tvAuthCode = (TextView) findViewById(R.id.tv_auth_code);
        btnComplete = (Button) findViewById(R.id.btn_complete);
        btnCompletePass = (Button) findViewById(R.id.btn_complete_pass);
        llPassword4 = (LinearLayout) findViewById(R.id.ll_password4);
        llPhoneValidate = (LinearLayout) findViewById(R.id.ll_phone_validate);

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        tvAuthCode.setOnClickListener(this);
        edValidate.addTextChangedListener(this);
        btnCompletePass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_auth_code:

                number = tvWXPhone.getText().toString();
                if (number.length() == 11 && number.substring(0, 1).equals("1")) {

                    /**
                     * 验证手机是否绑定其他微信
                     */
                    checkBind();

                } else {
                    showToastShort("请先输入正确的11位手机号获取短信验证码");
                }
                break;

            case R.id.btn_complete:
                /**
                 * 验证手机验证码是否正确，已经注册的就直接登录成功否则就去输入密码注册并登录
                 */
                checkValidate();

                break;

            case R.id.btn_complete_pass:

                /**
                 *  微信绑定
                 */
                loadBindWX();

                break;

            default:
                break;
        }

    }

    /**
     * 验证手机验证码是否正确
     */
    private void checkValidate() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("sms_code", edValidate.getText().toString());

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.VerificationNumber, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {


                Gson gson = new Gson();
                if (responseInfo != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);

                    if (status.status.code == 10000) {

                        /**
                         * 已经注册的就直接登录成功否则就去输入密码注册并登录
                         */
                        if (isValidate) {

                            /**
                             *  微信绑定
                             */
                            loadBindWX();

                        } else {
                            tvBindStatus.setText("为了您更好的体验，请输入登录密码");
                            llPhoneValidate.setVisibility(View.GONE);
                            btnComplete.setVisibility(View.GONE);
                            llPassword4.setVisibility(View.VISIBLE);
                            btnCompletePass.setVisibility(View.VISIBLE);
                        }

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
     * 微信绑定
     */
    private void loadBindWX() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("unionid", unionid);
        params.addBodyParameter("nickname", nickname);
        params.addBodyParameter("headimgurl", headimgurl);
        params.addBodyParameter("member_mobile", number);
        if (!isValidate) {
            params.addBodyParameter("password", edValidatePass.getText().toString());
        }
        params.addBodyParameter("client", "android");
        params.addBodyParameter("client_type", "1");
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Bind_wx, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Gson gson = new Gson();
                bindWX = gson.fromJson(responseInfo.result, BindWX.class);
                if (bindWX.status.code == 10000) {

                    getSharedPreferences("key", MODE_PRIVATE).edit().putString("key", bindWX.datas.key).commit();
                    getSharedPreferences("member_id", MODE_PRIVATE).edit().putString("member_id", bindWX.datas.member_id).commit();
                    getSharedPreferences("username", MODE_PRIVATE).edit().putString("username", bindWX.datas.member_mobile).commit();
                    finish();
                } else {
                    overtime(bindWX.status.code, bindWX.status.msg);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });


    }

    /**
     * 验证手机是否绑定其他微信
     */
    private void checkBind() {

        RequestParams params = new RequestParams();
        params.addBodyParameter("member_mobile", number);
        params.addBodyParameter("open_type", "1");

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Check_bind, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Gson gson = new Gson();
                Status status = gson.fromJson(responseInfo.result, Status.class);

                /**
                 * 请求发送验证码
                 */
                Verification_code();

                if (status.status.code == 10000) {
                    isValidate = true;

                } else if (status.status.code == 100100) {
                    isValidate = false;

                } else {
                    overtime(status.status.code, status.status.msg);
                    isValidate = true;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }


    /**
     * 请求发送验证码
     */
    private void Verification_code() {
        super.StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("member_mobile", number);
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * 监听输入验证码
     */
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
