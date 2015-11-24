package com.wjhgw.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.api.Registered_Request;
import com.wjhgw.business.response.BusinessResponse;
import com.wjhgw.config.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 注册
 */
public class A1_RegisterActivity2 extends BaseActivity implements BusinessResponse, OnClickListener {

    private EditText et_cipher;
    private ImageView iv_delete;
    private TextView tv_next;

    private Registered_Request Request;
    private String Number;
    private String cipher;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a1_register_layout2);

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
        setTitle("注册");
        setUp();

    }

    @Override
    public void onFindViews() {
        et_cipher = (EditText) findViewById(R.id.et_a1_cipher);
        iv_delete = (ImageView) findViewById(R.id.iv_a1_delete);
        //a2_return = (ImageView)findViewById(R.id.a2_return);
        tv_next = (TextView) findViewById(R.id.tv_a1_next);

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
        Request.removeResponseListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_a1_delete:
                et_cipher.setText("");
                iv_delete.setVisibility(View.GONE);
                break;
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_a1_next:
                cipher = et_cipher.getText().toString();
                if (!cipher.equals("")) {
                    tv_next.setClickable(false);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("member_mobile", Number);
                    hashMap.put("password", cipher);
                    hashMap.put("client", "android");
                    Request.register(hashMap, BaseQuery.serviceUrl() + ApiInterface.Registered);
                }
                break;

            default:
                break;
        }

    }

    @Override
    public void OnMessageResponse(String url, String response, JSONObject status) throws JSONException {
        tv_next.setClickable(true);
        if (url.equals(BaseQuery.serviceUrl() + ApiInterface.Registered)) {
            if (status.getString("code").equals("10000")) {
                /*preferences = getSharedPreferences("key", MODE_PRIVATE);
                Editor editor = preferences.edit();
                //存入数据
                editor.putString("key", new JSONObject(new JSONObject(response).getString("datas")).getString("key"));
                editor.putString("username", new JSONObject(new JSONObject(response).getString("datas")).getString("member_name"));
                //提交修改
                editor.commit();
                //读取出来
                preferences.getString("key", "0");
                preferences.getString("username", "0");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);*/
                finish(false);
                showToastShort(status.getString("msg"));
                // Lock = 0;
            } else {
                showToastShort(status.getString("msg"));
            }
        }
    }
}
