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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 注册用户时验证手机号是否正确和注册
 */
public class A1_RegisterActivity1 extends BaseActivity implements OnClickListener {

    private EditText et_name;
    private ImageView iv_delete;
    private TextView tv_next;

    private String Number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a1_register_layout1);

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
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("注册");
    }

    @Override
    public void onFindViews() {
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                    Number(Number);
                } else {
                    showToastShort("你输入的号码有误！请重新输入");
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
     * 验证手机号码是否有效
     */
    private void Number(String number) {
        super.StartLoading();
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, ApiInterface.Number + number, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    if (responseInfo.result == null || "".equals(responseInfo.result) || responseInfo.result.indexOf("{") < 0
                            || new JSONObject(responseInfo.result.substring(responseInfo.result.indexOf("{"))).length() == 0) {
                        showToastShort("手机号码无效");
                    } else if (new JSONObject(responseInfo.result.substring(responseInfo.result.indexOf("{"))).getString("catName").length() > 0) {
                        VerificationRegistered(Number);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToastShort("请求失败");
            }
        });
    }

    /**
     * 验证手机号是否已经注册
     */
    private void VerificationRegistered(String number) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("member_mobile", number);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.VerificationRegistered, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                A1_RegisterActivity1.super.Dismiss();
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);

                    if (status.status.code == 100100) {
                        Intent intent = new Intent(A1_RegisterActivity1.this, VerificationCodeActivity.class);
                        intent.putExtra("Number", Number);
                        intent.putExtra("use", "1");
                        startActivity(intent);
                        finish(false);
                    } else {
                        showToastShort(status.status.msg);
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
