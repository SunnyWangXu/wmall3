package com.wjhgw.ui.activity;

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

import java.util.HashMap;

public class A2_ResetPassActivity2 extends BaseActivity implements BusinessResponse, OnClickListener {

    private EditText et_cipher;
    private ImageView iv_delete;
    private TextView tv_next;
    private ImageView iv_title_back;
    private TextView tv_title_name;

    private Registered_Request Request;
    private String Number;
    private String cipher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a2_reset_pass_layout2);

        tv_title_name.setText("密码找回");

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

        Request = new Registered_Request(this);
        Request.addResponseListener(this);
        Number = getIntent().getStringExtra("Number");

    }

    @Override
    public void onInit() {

    }

    @Override
    public void onFindViews() {
        et_cipher = (EditText) findViewById(R.id.et_a2_cipher);
        iv_delete = (ImageView) findViewById(R.id.iv_a2_delete1);
        iv_title_back = (ImageView) findViewById(R.id.iv_title_back);
        tv_title_name = (TextView) findViewById(R.id.tv_title_name);
        tv_next = (TextView) findViewById(R.id.tv_a2_next1);

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
            case R.id.iv_a0_delete1:
                et_cipher.setText("");
                iv_delete.setVisibility(View.GONE);
                break;
            case R.id.tv_a2_next1:
                cipher = et_cipher.getText().toString();
                if (!cipher.equals("")) {
                    tv_next.setClickable(false);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("member_mobile", Number);
                    hashMap.put("password", cipher);
                    Request.ResetPassword(hashMap, BaseQuery.serviceUrl() + ApiInterface.ResetPassword);
                }
                break;
            case R.id.iv_title_back:
                finish(false);
                break;

            default:
                break;
        }

    }

    @Override
    public void OnMessageResponse(String url, String response, JSONObject status) throws JSONException {
        tv_next.setClickable(true);
        if (url.equals(BaseQuery.serviceUrl() + ApiInterface.ResetPassword)) {
            if (status.getString("code").equals("10000")) {
                finish(false);
                Toast.makeText(this, status.getString("msg"), Toast.LENGTH_LONG).show();
            } else if (status.getString("code").equals("300100")) {
                Toast.makeText(this, status.getString("msg"), Toast.LENGTH_LONG).show();
            } else if (status.getString("code").equals("300101")) {
                Toast.makeText(this, status.getString("msg"), Toast.LENGTH_LONG).show();
            } else if (status.getString("code").equals("300102")) {
                Toast.makeText(this, status.getString("msg"), Toast.LENGTH_LONG).show();
            }
        }
    }
}
