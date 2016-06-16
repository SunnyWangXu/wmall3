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
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.bean.Login_Pager;
import com.wjhgw.business.bean.Status;
import com.wjhgw.config.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 登录
 */
public class A0_LoginActivity extends BaseActivity implements OnClickListener {

    private EditText et_name;
    private EditText et_password;
    private ImageView iv_delete;
    private ImageView iv_delete1;
    private TextView tv_next;
    private ImageView iv_registered;
    private TextView tv_back;
    private TextView tv_a0_tback;
    private String Number;
    private String password;
    private String username;
    private Intent intent;
    private ImageView ivWxlogin;
    private IWXAPI api;
    private String openid = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a0_login_layout);

        api = WXAPIFactory.createWXAPI(this, "wx99a6bd9b7bdbf645");
        api.registerApp("wx99a6bd9b7bdbf645");

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

        username = getSharedPreferences("username", this.MODE_APPEND).getString("username", "0");
        if (!username.equals("0")) {
            et_name.setText(username);
            et_name.setSelection(et_name.length());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        openid = getSharedPreferences("openid", this.MODE_APPEND).getString("openid", "0");
        if (!openid.equals("0")) {
            getSharedPreferences("openid", MODE_APPEND).edit().putString("openid", "0").commit();
            check_bind(openid);
        }
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("登录");
    }

    @Override
    public void onFindViews() {
        et_name = (EditText) findViewById(R.id.et_a0_name);
        et_password = (EditText) findViewById(R.id.et_a0_password);
        iv_delete = (ImageView) findViewById(R.id.iv_a0_delete);
        iv_delete1 = (ImageView) findViewById(R.id.iv_a0_delete1);
        tv_next = (TextView) findViewById(R.id.tv_a0_next);
        iv_registered = (ImageView) findViewById(R.id.iv_a0_registered);
        ivWxlogin = (ImageView) findViewById(R.id.iv_wx_login);
        tv_a0_tback = (TextView) findViewById(R.id.tv_a0_tback);
        tv_back = (TextView) findViewById(R.id.tv_a0_tback);

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        iv_delete.setOnClickListener(this);
        iv_delete1.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        iv_registered.setOnClickListener(this);
        ivWxlogin.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        tv_a0_tback.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                    login("0");
                } else {
                    showToastShort("你输入的号码有误！请重新输入");
                }
                break;

            case R.id.iv_a0_registered:
                intent = new Intent(this, A1_RegisterActivity1.class);
                startActivity(intent);
                break;

            case R.id.iv_wx_login:
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wechat_sdk_demo_test";
                api.sendReq(req);
                break;

            case R.id.tv_a0_tback:
                intent = new Intent(this, A2_ResetPassActivity1.class);
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
     * 登录网络请求
     */
    private void login(String openid) {
        super.StartLoading();
        RequestParams params = new RequestParams();
        if (openid.equals("0")) {
            params.addBodyParameter("member_mobile", Number);
            params.addBodyParameter("password", password);
        } else {
            params.addBodyParameter("member_wxopenid", openid);

        }

        params.addBodyParameter("client", "android");
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Login, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                A0_LoginActivity.super.Dismiss();
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Login_Pager login = gson.fromJson(responseInfo.result, Login_Pager.class);
                    if (login.status.code == 10000) {
                        getSharedPreferences("key", MODE_PRIVATE).edit().putString("key", login.datas.key).commit();
                        getSharedPreferences("member_id", MODE_PRIVATE).edit().putString("member_id", login.datas.member_id).commit();
                        getSharedPreferences("username", MODE_PRIVATE).edit().putString("username", Number).commit();
                        showToastShort("登录成功");

                        Intent intent = new Intent();
                        intent.putExtra("forWhere", "forPrductDetail");
                        setResult(12345, intent);

                        finish(false);

                    } else {
                        overtime(login.status.code, login.status.msg);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    /**
     * 手机open_id绑定验证
     */
    private void check_bind(final String openid) {
        super.StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("open_id", openid);
        params.addBodyParameter("open_type", "1");
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Check_bind, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                A0_LoginActivity.super.Dismiss();
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);
                    if (status.status.code == 100401 ) {
                        login(openid);
                    } else {
                        String access_token = getSharedPreferences("access_token", MODE_APPEND).getString("access_token", "0");
                        if (!openid.equals("0") && !access_token.equals("0")) {
                            String url1 = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid;
                            UnionID(url1);
                        }

                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    /**
     * 获取用户个人信息
     */
    private void UnionID(String url) {
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo != null) {
                    try {
                        JSONObject myJsonObject = new JSONObject(responseInfo.result);
                        myJsonObject.getString("nickname");
                        myJsonObject.getString("headimgurl");
                        myJsonObject.getString("unionid");
                        myJsonObject.getString("openid");
                        intent = new Intent(A0_LoginActivity.this, A3_WXLoginActivity.class);
                        intent.putExtra("openid", myJsonObject.getString("openid"));
                        intent.putExtra("nickname", myJsonObject.getString("nickname"));
                        intent.putExtra("headimgurl", myJsonObject.getString("headimgurl"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }
}
