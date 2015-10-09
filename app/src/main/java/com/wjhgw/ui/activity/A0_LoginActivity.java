package com.wjhgw.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import com.wjhgw.business.api.Login_Request;
import com.wjhgw.business.response.BusinessResponse;
import com.wjhgw.config.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 登录
 */
public class A0_LoginActivity extends BaseActivity implements BusinessResponse, OnClickListener {

    private EditText et_name;
    private EditText et_password;
    private ImageView iv_delete;
    private ImageView iv_delete1;
    private ImageView iv_title_back;
    private TextView tv_title_name;
    private TextView tv_next;
    private TextView tv_registered;
    private TextView tv_back;

    private Login_Request Request;
    private String Number;
    private String password;
    Intent intent;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a0_login_layout);
        iv_title_back = (ImageView) findViewById(R.id.iv_title_back);
        tv_title_name = (TextView) findViewById(R.id.tv_title_name);

        et_name = (EditText) findViewById(R.id.et_a0_name);
        et_password = (EditText) findViewById(R.id.et_a0_password);
        iv_delete = (ImageView) findViewById(R.id.iv_a0_delete);
        iv_delete1 = (ImageView) findViewById(R.id.iv_a0_delete1);
        tv_next = (TextView) findViewById(R.id.tv_a0_next);
        tv_registered = (TextView) findViewById(R.id.tv_a0_registered);
        tv_back = (TextView) findViewById(R.id.tv_a0_tback);

        tv_title_name.setText("登录");

        iv_delete.setOnClickListener(this);
        iv_delete1.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        tv_registered.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        iv_title_back.setOnClickListener(this);
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start != 0) {
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
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 0) {
                    iv_delete1.setVisibility(View.VISIBLE);
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
        Request = new Login_Request(this);
        Request.addResponseListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Request.removeResponseListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_a0_delete:
                et_name.setText("");
                iv_delete.setVisibility(View.GONE);
                break;
            case R.id.iv_a0_delete1:
                et_password.setText("");
                iv_delete1.setVisibility(View.GONE);
                break;
            case R.id.tv_a0_next:
                Number = et_name.getText().toString();
                password = et_password.getText().toString();
                if (Number.length() == 11 && Number.substring(0, 1).equals("1")) {
                    tv_next.setClickable(false);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("member_mobile", Number);
                    hashMap.put("password", password);
                    hashMap.put("client", "android");
                    Request.login(hashMap, BaseQuery.serviceUrl() + ApiInterface.Login);
                } else {
                    Toast.makeText(A0_LoginActivity.this, "你输入的号码有误！请重新输入", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.tv_a0_registered:
                intent = new Intent(this, A1_RegisterActivity1.class);
                startActivity(intent);
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
        tv_next.setClickable(true);
        if (url.equals(BaseQuery.serviceUrl() + ApiInterface.Login)) {
            if (status.getString("code").equals("10000")) {
                preferences = getSharedPreferences("key", MODE_PRIVATE);
                Editor editor = preferences.edit();
                //存入数据
                editor.putString("key", Request.data.getKey());
                editor.putString("username", Request.data.getMember_name());
                //提交修改
                editor.commit();
                //读取出来
                preferences.getString("key", "0");
                preferences.getString("username", "0");
	        /*intent = new Intent(this,MainActivity.class);
			startActivity(intent);*/
                Toast.makeText(A0_LoginActivity.this, "登录成功！", Toast.LENGTH_LONG).show();
                finish(false);
            } else if (status.getString("code").equals("200100")) {
                Toast.makeText(A0_LoginActivity.this, status.getString("msg"), Toast.LENGTH_LONG).show();
            } else if (status.getString("code").equals("200101")) {
                Toast.makeText(A0_LoginActivity.this, status.getString("msg"), Toast.LENGTH_LONG).show();
            } else if (status.getString("code").equals("200102")) {
                Toast.makeText(A0_LoginActivity.this, status.getString("msg"), Toast.LENGTH_LONG).show();
            } else if (status.getString("code").equals("200103")) {
                Toast.makeText(A0_LoginActivity.this, status.getString("msg"), Toast.LENGTH_LONG).show();
            }
        }
    }
}
