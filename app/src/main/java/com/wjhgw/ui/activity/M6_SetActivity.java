package com.wjhgw.ui.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.bean.Status;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.utils.FileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 设置的Activity
 */
public class M6_SetActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivShare;
    //private ImageView ivPush;
    private int PushCount = 1;
    private Button btnExit;
    private String memberName;
    private LinearLayout llClearCache;
    private String cachePath;
    private TextView tvCache;
    private TextView tv_edition;
    private LinearLayout llCheckVersion;
    IWXAPI api;
    StringBuffer sb;
    int s = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        api = WXAPIFactory.createWXAPI(this, "wx99a6bd9b7bdbf645");
        api.registerApp("wx99a6bd9b7bdbf645");
        sb = new StringBuffer();
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("设置");
        memberName = getIntent().getStringExtra("memberName");

    }

    @Override
    public void onFindViews() {
        ivShare = (ImageView) findViewById(R.id.iv_title_right);
        //ivPush = (ImageView) findViewById(R.id.iv_push);
        btnExit = (Button) findViewById(R.id.btn_exit);
        tvCache = (TextView) findViewById(R.id.tv_cache);
        tv_edition = (TextView) findViewById(R.id.tv_edition);
        llClearCache = (LinearLayout) findViewById(R.id.ll_clear_cache);
        llCheckVersion = (LinearLayout) findViewById(R.id.ll_check_version);
    }

    @Override
    public void onInitViewData() {
        /*ivShare.setImageResource(R.mipmap.ic_share);
        ivShare.setVisibility(View.VISIBLE);*/

        if (!getKey().equals("0")) {
            btnExit.setVisibility(View.VISIBLE);
        }


        String currentapiVersion = null;
        try {
            currentapiVersion = getVersionName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_edition.setText(currentapiVersion);
    }

    @Override
    public void onBindListener() {
        //ivPush.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        llClearCache.setOnClickListener(this);
        llCheckVersion.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取缓存的路径
        cachePath = APP.getApp().getAppCache();
        Long dirSize = FileUtils.getDirSize(new File(cachePath));
        String cacheSize = FileUtils.getFileSize(dirSize);

        tvCache.setText(cacheSize);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.iv_push:
                PushCount++;
                if (PushCount % 2 == 1) {
                    ivPush.setImageResource(R.mipmap.ic_push_off);
                } else if (PushCount % 2 == 0) {
                    ivPush.setImageResource(R.mipmap.ic_push_on);
                }
                break;*/

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
                pay.pay();*//*
                //buy();
                //showToastShort("当前已经是最新版本");
                wechatShare(s++);*/
                //UmengUpdateAgent.update(this);
                break;

            default:
                break;
        }
    }

    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
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
                        getSharedPreferences("key", MODE_PRIVATE).edit().putString("key", "0").commit();
                        showToastShort("已退出登录");
                        finish(false);
                    } else {
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

    private void wechatShare(int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://wjhgw.com/";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "礼包";
        msg.description = "花心大萝卜的大礼包！";
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo);
        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
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


  /*  private String genAppSign(List<NameValuePair> params) {
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
    }*/

}
