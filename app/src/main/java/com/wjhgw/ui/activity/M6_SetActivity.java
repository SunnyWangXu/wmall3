package com.wjhgw.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.bean.Status;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.pay.WeChat.MD5;
import com.wjhgw.utils.FileUtils;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * 设置的Activity
 */
public class M6_SetActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivShare;
    private ImageView ivPush;
    private int PushCount = 1;
    private Button btnExit;
    private String memberName;
    private LinearLayout llClearCache;
    private String cachePath;
    private TextView tvCache;
    private LinearLayout llCheckVersion;
    IWXAPI api;
    StringBuffer sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1");
        sb = new StringBuffer();
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("设置");
        memberName = getIntent().getStringExtra("memberName");
        //获取缓存的路径
        cachePath = APP.getApp().getAppCache();

    }

    @Override
    public void onFindViews() {
        ivShare = (ImageView) findViewById(R.id.iv_title_right);
        ivPush = (ImageView) findViewById(R.id.iv_push);
        btnExit = (Button) findViewById(R.id.btn_exit);
        tvCache = (TextView) findViewById(R.id.tv_cache);
        llClearCache = (LinearLayout) findViewById(R.id.ll_clear_cache);
        llCheckVersion = (LinearLayout) findViewById(R.id.ll_check_version);
    }

    @Override
    public void onInitViewData() {
        ivShare.setImageResource(R.mipmap.ic_share);
        ivShare.setVisibility(View.VISIBLE);

        if (!getKey().equals("0")) {
            btnExit.setVisibility(View.VISIBLE);
        }

        Long dirSize = FileUtils.getDirSize(new File(cachePath));
        String cacheSize = FileUtils.getFileSize(dirSize);

        tvCache.setText(cacheSize);

    }

    @Override
    public void onBindListener() {
        ivPush.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        llClearCache.setOnClickListener(this);
        llCheckVersion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_push:
                PushCount++;
                if (PushCount % 2 == 1) {
                    ivPush.setImageResource(R.mipmap.ic_push_off);
                } else if (PushCount % 2 == 0) {
                    ivPush.setImageResource(R.mipmap.ic_push_on);
                }
                break;

            case R.id.ll_clear_cache:
                /**
                 * 删除缓存的文件
                 */
                FileUtils.clearAppCache(cachePath);
                tvCache.setText("0KB");
                showToastShort("清除缓存成功");
                break;

            case R.id.btn_exit:
                /**
                 * 退出登录
                 */
                exitLogin();

                break;
            case R.id.ll_check_version:
                /*payMethod pay = new payMethod(this, "订单号", "测试的商品", "测试的商品详情", "0.01");
                pay.pay();*/
                buy();
                //showToastShort("当前已经是最新版本");
                break;

            default:
                break;
        }
    }

    /**
     * 退出登录
     */
    private void exitLogin() {
        super.StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("member_name", memberName);
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("client", "android");

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Exit_login, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                M6_SetActivity.super.Dismiss();
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);
                    if (status.status.code == 10000) {
                        getSharedPreferences("key",MODE_PRIVATE).edit().putString("key","0").commit();
                        showToastShort("已退出登录");
                        finish(false);
                    }else {
                        overtime(status.status.code, status.status.msg);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

                showToastShort("网络错误");
            }
        });
    }

    /**
     * 微信支付测试数据
     */

    public void buy() {
        RequestQueue mRequestQueue;
        StringRequest stringRequest;
        mRequestQueue = Volley.newRequestQueue(this);
        Response.Listener<String> SuccessfulResponse = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    PayReq req = new PayReq();
                    req.appId = json.getString("appid");
                    req.partnerId = json.getString("partnerid");
                    req.prepayId = json.getString("prepayid");
                    req.nonceStr = json.getString("noncestr");
                    req.timeStamp = json.getString("timestamp");
                    req.packageValue = json.getString("package");
                    req.sign = json.getString("sign");
                   // req.extData = "app data"; // optional

                        /*List<NameValuePair> signParams = new LinkedList<>();
                        signParams.add(new BasicNameValuePair("appid", req.appId));
                        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
                        signParams.add(new BasicNameValuePair("package", req.packageValue));
                        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
                        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
                        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

                        req.sign = genAppSign(signParams);*/
                    showToastShort("正常调起支付");
                    api.sendReq(req);
                    // OnMessageResponse(Route, response, new JSONObject(new JSONObject(response).getString("status")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        stringRequest = new StringRequest(Request.Method.POST, "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android", SuccessfulResponse, FailureResponse);
        mRequestQueue.add(stringRequest);
    }

    /**
     * 请求错误回调
     */
    Response.ErrorListener FailureResponse = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // Toast.makeText(mContext, "网络错误！", Toast.LENGTH_SHORT).show();
        }
    };


    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append("16103e4Fd8906506991dbbED035632d1");

        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        //Log.e("orion", appSign);
        return appSign;
    }

}
