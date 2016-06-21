package com.wjhgw.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wjhgw.APP;
import com.wjhgw.MainActivity;
import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.bean.Get_share_info;
import com.wjhgw.business.bean.Order_goods_list;
import com.wjhgw.business.bean.SelectOrder;
import com.wjhgw.business.bean.SelectOrderDatas;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.dialog.LoadDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 首页轮播图通用WebView
 */
public class GeneralPrductDetailActivity extends BaseActivity implements View.OnClickListener {
    private WebView webView;
    private ImageView back;
    private String ua;
    private LoadDialog Dialog;
    private String Shopping_Cart;
    private HashMap<String, String> keyMap;
    private String url;
    private LinearLayout title_container;
    private IWXAPI api;
    private String id;
    private ImageView iv_title_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prduct_detail);

        webView = (WebView) findViewById(R.id.wb_prduct_detail);
        api = WXAPIFactory.createWXAPI(this, "wx99a6bd9b7bdbf645");
        api.registerApp("wx99a6bd9b7bdbf645");
        /**
         * 加载WebView
         */
        toLoadWebView();
    }

    /**
     * 加载WebView
     */
    private void toLoadWebView() {
        Dialog = new LoadDialog(this);
        WebSettings webSettings = webView.getSettings();
        ua = webSettings.getUserAgentString() + " WMall/3.0.0";

        // 判断是否为WIFI网络状态
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {

            ua = webSettings.getUserAgentString() + " WMall/3.0.0" + " NetType/WIFI";
        }

        webSettings.setUserAgentString(ua);

        //页面支持缩放：
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

        //webview必须设置支持获取手势焦点。
        webView.requestFocusFromTouch();

        //设置此属性，可任意比例缩放
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        /**
         * 加载WebView的url和传key给H5端
         */

        /**
         * 如果判断是广告专题那么就不显示title
         */
        String isDetail = getIntent().getStringExtra("isDetail");
        if (isDetail != null && isDetail.equals("no")) {
            title_container.setVisibility(View.GONE);
        }

        Shopping_Cart = getIntent().getStringExtra("Shopping_Cart");
        //BaseQuery.environment()
        url = getIntent().getStringExtra("url");

        id = url.substring(url.lastIndexOf("=") + 1);

        keyMap = new HashMap<>();
        if (!getKey().equals("0")) {
            keyMap.put("authentication", getKey());
        }
        webView.loadUrl(url, keyMap);

       /* // 打开网页时不调用系统浏览器， 而是在本WebView中显示：
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });*/

        webView.setWebChromeClient(new WebChromeClient());
        //调用javascript
        webView.addJavascriptInterface(new WMallBridge(this), "WMallBridge");
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("商品详情");

    }

    @Override
    public void onFindViews() {
        title_container = (LinearLayout) findViewById(R.id.title_container);
        iv_title_right = (ImageView) findViewById(R.id.iv_title_right);
    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        iv_title_right.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_right:
                /**
                 * 获取分享内容
                 */
                get_share_info(id);
                break;

            default:
                break;
        }

    }

    /**
     * 按返回键时， 不退出程序而是返回上一浏览页面：
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public class WMallBridge {
        Context mContxt;

        public WMallBridge(Context mContxt) {
            this.mContxt = mContxt;
        }

        @JavascriptInterface
        public void callHandler(String handlerName, String data) {

            if (handlerName.equals("goHomeHandler")) {
                Intent intent = new Intent();
                intent.setClass(GeneralPrductDetailActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新将要跳到的界面
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity
                startActivity(intent);
                finish();

            }

            if (handlerName.equals("goCartHandler")) {
                if (Shopping_Cart != null) {
                    finish();
                } else {
                    Intent intent = new Intent(GeneralPrductDetailActivity.this, ShoppingCartActivity.class);
                    startActivity(intent);
                }
            }

            if (handlerName.equals("giftHandler")) {
                //Toast.makeText(mContxt, "赠送他人" + data, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject bject = new JSONObject(data);
                    String cart_id = bject.getString("goods_id") + "|" + bject.getString("goods_num");
                    buy_step1(cart_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (handlerName.equals("loginHandler")) {
                Intent intent = new Intent(GeneralPrductDetailActivity.this, A0_LoginActivity.class);
                startActivityForResult(intent, 12345);

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.getStringExtra("forWhere").equals("forPrductDetail")) {

            /**
             * 加载WebView
             */
            toLoadWebView();
        }

    }

    /**
     * 购买第一步接口
     */
    private void buy_step1(final String cart_id) {
        Dialog.ProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("cart_id", cart_id);
        params.addBodyParameter("ifcart", "3");

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Buy_step1, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dialog.dismiss();
                Gson gson = new Gson();
                if (responseInfo.result != null) {

                    SelectOrder selectOrder = gson.fromJson(responseInfo.result, SelectOrder.class);
                    if (selectOrder.status.code == 10000) {
                        SelectOrderDatas selectOrderDatas = selectOrder.datas;
                        ArrayList<Order_goods_list> order_goods_lists = selectOrderDatas.store_cart_list.goods_list;
                        double realPay = 0.00;
                        for (int i = 0; i < order_goods_lists.size(); i++) {
                            realPay += Double.valueOf(order_goods_lists.get(i).goods_total);
                        }

                        Intent intent = new Intent(GeneralPrductDetailActivity.this, S0_ConfirmOrderActivity.class);
                        intent.putExtra("selectOrder", responseInfo.result);
                        //String tvTotal = tv_total.getText().toString();
                        intent.putExtra("cart_id", cart_id);
                        intent.putExtra("tv_total", realPay + "");
                        intent.putExtra("realPay", realPay);
                        intent.putExtra("for", "forDetail");
                        startActivity(intent);
                    } else {
                        overtime(selectOrder.status.code, selectOrder.status.msg);
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
     * 获取分享内容
     */
    private void get_share_info(String goods_id) {
        Dialog.ProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("goods_id", goods_id);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Get_share_info, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo.result != null) {
                    final Get_share_info get_share_info = gson.fromJson(responseInfo.result, Get_share_info.class);
                    if (get_share_info.status.code == 10000) {
                        final String Url;
                        if (getKey().equals("0")) {
                            Url = "http://www.wjhgw.com/wap/index.php?act=goods&id=" + id;
                        } else {
                            String member_id = GeneralPrductDetailActivity.this.getSharedPreferences("member_id", MODE_APPEND).getString("member_id", "0");
                            Url = "http://www.wjhgw.com/wap/index.php?act=goods&id=" + id + "&intr_id=" + member_id;
                        }

                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                wechatShare(0, Url, get_share_info.datas.goods_image, get_share_info.datas.goods_name);
                            }
                        }).start();
                    } else {
                        overtime(get_share_info.status.code, get_share_info.status.msg);
                    }
                }
                Dialog.dismiss();
            }

            @Override
            public void onFailure(HttpException e, String s) {
            }
        });
    }

    /**
     * 微信分享礼包
     */
    private void wechatShare(int flag, String giftUrl, String goods_image, String Name) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = giftUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = Name;
        msg.description = "我在万嘉欢购发现一件不错的商品，赶快来看看吧!";

        Bitmap thumb = null;
        try {
            thumb = getBitmap(goods_image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
        //finish();
    }

    public static Bitmap getBitmap(String path) throws IOException {

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }
}
