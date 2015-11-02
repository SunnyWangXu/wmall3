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
import android.widget.Toast;

import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.api.Registered_Request;
import com.wjhgw.business.response.BusinessResponse;
import com.wjhgw.config.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class VerificationCodeActivity extends BaseActivity implements BusinessResponse, OnClickListener {

    private EditText et_editText;
    private ImageView iv_delete;
    private TextView tv_next;
    private TextView tv_verificationcode;
    private TextView tv_state;

    private Registered_Request Request;
    private String Number;
    private String Verification_code;
    private TimeCount time;
    Intent intent;
    private int Lock = 0;
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
        Request = new Registered_Request(this);
        Request.addResponseListener(this);
        Number = getIntent().getStringExtra("Number");
        tv_state.setText("请输入手机号码 " + Number + " 收到的验证码");
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        time.start();
        tv_verificationcode.setBackgroundColor(0xff666666);
        key = this.getSharedPreferences("key", MODE_PRIVATE).getString("key", "0");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("member_mobile", Number);
        Request.Verification_code(hashMap, BaseQuery.serviceUrl() + ApiInterface.VerificationCode);
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
        Request.removeResponseListener(this);
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
                if (!Verification_code.equals("")) {
                    tv_next.setClickable(false);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("sms_code", Verification_code);
                    Request.Number_Verification_code(hashMap, BaseQuery.serviceUrl() + ApiInterface.VerificationNumber);
                }
                break;
            case R.id.tv_verificationcode:
                if (!Number.equals("")) {
                    time.start();
                    tv_verificationcode.setBackgroundColor(0xffcccccc);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("member_mobile", Number);
                    Request.Verification_code(hashMap, BaseQuery.serviceUrl() + ApiInterface.VerificationCode);
                }
                break;
            case R.id.iv_title_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void OnMessageResponse(String url, String response, JSONObject status) throws JSONException {
        tv_next.setClickable(true);
        if (url.equals(BaseQuery.serviceUrl() + ApiInterface.VerificationCode)) {
            if (status.getString("code").equals("10000")) {
                Toast.makeText(this, status.getString("msg"), Toast.LENGTH_LONG).show();
            } else if (status.getString("code").equals("100200")) {
                Toast.makeText(this, status.getString("msg"), Toast.LENGTH_LONG).show();
            }
        } else if (url.equals(BaseQuery.serviceUrl() + ApiInterface.VerificationNumber)) {
            if (status.getString("code").equals("10000")) {
                if(getIntent().getStringExtra("use").equals("0")){
                    intent = new Intent(this, A2_ResetPassActivity2.class);
                    intent.putExtra("Number", Number);
                    startActivity(intent);
                    finish(false);
                }else if(getIntent().getStringExtra("use").equals("1")){
                    intent = new Intent(this, A1_RegisterActivity2.class);
                    intent.putExtra("Number", Number);
                    startActivity(intent);
                    finish(false);
                }

                Toast.makeText(this, status.getString("msg"), Toast.LENGTH_LONG).show();
            } else if (status.getString("code").equals("100300")) {
                Toast.makeText(this, status.getString("msg"), Toast.LENGTH_LONG).show();
            } else if (status.getString("code").equals("100300")) {
                Toast.makeText(this, status.getString("msg"), Toast.LENGTH_LONG).show();
            }
        }
    }
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            tv_verificationcode.setBackgroundColor(0xffcccccc);
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
