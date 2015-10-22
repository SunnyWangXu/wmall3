package com.wjhgw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
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

public class A1_RegisterActivity1 extends BaseActivity implements BusinessResponse, OnClickListener {

    private EditText et_name;
    private ImageView iv_delete;
    private TextView tv_next;
    private ImageView iv_title_back;
    private TextView tv_title_name;

    private Registered_Request Request;
    private String Number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a1_register_layout1);

        tv_title_name.setText("注册");

        et_name.addTextChangedListener(new TextWatcher() {
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
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onFindViews() {
        iv_title_back = (ImageView) findViewById(R.id.iv_title_back);
        tv_title_name = (TextView) findViewById(R.id.tv_title_name);
        et_name = (EditText) findViewById(R.id.et_a1_name);
        iv_delete = (ImageView) findViewById(R.id.iv_a1_delete);
        tv_next = (TextView) findViewById(R.id.tv_a1_next);
    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        iv_delete.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        iv_title_back.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Request.removeResponseListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_a1_delete:
                et_name.setText("");
                iv_delete.setVisibility(View.GONE);
                break;
            case R.id.tv_a1_next:
                Number = et_name.getText().toString();
                if (Number.length() == 11 && Number.substring(0, 1).equals("1")) {
                    //a0_next.setClickable(false);
                    Request.VerificationNumber(Number);
                } else {
                    Toast.makeText(A1_RegisterActivity1.this, "你输入的号码有误！请重新输入", Toast.LENGTH_LONG).show();
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
     * 接口回调
     */
    @Override
    public void OnMessageResponse(String url, String response, JSONObject status)
            throws JSONException {
        if (url.equals(BaseQuery.serviceUrl() + ApiInterface.VerificationRegistered)) {
            if (status.getString("code").equals("100100")) {
                Intent intent = new Intent(this, VerificationCodeActivity.class);
                intent.putExtra("Number", Number);
                intent.putExtra("use", "1");
                startActivity(intent);
                finish(false);
                overridePendingTransition(R.anim.push_right_in,
                        R.anim.push_right_out);
            } else if (status.getString("code").equals("100101")) {
                Toast.makeText(A1_RegisterActivity1.this, status.getString("msg"), Toast.LENGTH_LONG).show();
            } else if (status.getString("code").equals("100102")) {
                Toast.makeText(A1_RegisterActivity1.this, status.getString("msg"), Toast.LENGTH_LONG).show();
            }
        }
    }

}
