package com.wjhgw.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjhgw.APP;
import com.wjhgw.MainActivity;
import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.bean.Order_goods_list;
import com.wjhgw.business.bean.SelectOrder;
import com.wjhgw.business.bean.SelectOrderDatas;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.dialog.LoadDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品详情页
 */
public class PrductDetailActivity extends BaseActivity implements View.OnClickListener {
    private WebView webView;
    private ImageView back;
    private String ua;
    private LoadDialog Dialog;
    private String key;
    private String Shopping_Cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prduct_detail);
        webView = (WebView) findViewById(R.id.wb_prduct_detail);
        Dialog = new LoadDialog(this);
        WebSettings webSettings = webView.getSettings();
        ua = webSettings.getUserAgentString() + " WMall/3.0.0";
        key = getKey();

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
        String id = getIntent().getStringExtra("goods_id");
        Shopping_Cart = getIntent().getStringExtra("Shopping_Cart");
        String url = "http://10.10.0.181/wap/index.php?act=goods&op=index&id=" + id;
        Map<String, String> keyMap = new HashMap<>();
        if (!key.equals("0")) {
            keyMap.put("Authentication", getKey());
            webView.loadUrl(url, keyMap);
        }

        // 打开网页时不调用系统浏览器， 而是在本WebView中显示：
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

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

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


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
                Intent intent = new Intent(PrductDetailActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                showToastShort(data);
            }

            if (handlerName.equals("goCartHandler")) {
                if(Shopping_Cart != null){
                    finish();
                }else {
                    Intent intent = new Intent(PrductDetailActivity.this,ShoppingCartActivity.class);
                    startActivity(intent);
                }
            }

            if (handlerName.equals("giftHandler")) {
                //Toast.makeText(mContxt, "赠送他人" + data, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject bject = new JSONObject(data);
                    String cart_id = bject.getString("goods_id")+ "|" +bject.getString("goods_num");
                    buy_step1(cart_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 购买第一步接口
     */
    private void buy_step1(final String cart_id) {
        Dialog.ProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
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

                        Intent intent = new Intent(PrductDetailActivity.this, S0_ConfirmOrderActivity.class);
                        intent.putExtra("selectOrder", responseInfo.result);
                        //String tvTotal = tv_total.getText().toString();
                        intent.putExtra("cart_id", cart_id);
                        intent.putExtra("tv_total", realPay+"");
                        intent.putExtra("realPay", realPay);
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
}