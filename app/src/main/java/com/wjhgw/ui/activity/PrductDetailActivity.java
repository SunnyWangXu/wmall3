package com.wjhgw.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 商品详情页
 */
public class PrductDetailActivity extends BaseActivity implements View.OnClickListener {
    private WebView webView;
    private ImageView back;
    private String ua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prduct_detail);

        webView = (WebView) findViewById(R.id.wb_prduct_detail);

        WebSettings webSettings = webView.getSettings();
        ua = webSettings.getUserAgentString() + " WMall/3.0.0";


        // 判断是否为WIFI网络状态
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {

            ua = webSettings.getUserAgentString() + " WMall/3.0.0" + " NetType/WIFI";
        }

        webSettings.setUserAgentString(ua);

        //NetType/WIFI
        Log.e("UA", ua);

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
        String url = "http://10.10.0.181/wap/index.php?act=goods&op=index&id=" + id;
        Map<String, String> keyMap = new HashMap<>();
        if (getKey() != null || getKey() != "0") {
            keyMap.put("key", getKey());
        }
        webView.loadUrl(url, keyMap);

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
        public void fun1(String name) {
            Toast.makeText(mContxt, "调用fun1:调用fun1调用fun1调用fun1" + name, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void callHandler(String handlerName, String data) {
            showToastShort("Action:" + handlerName + "      " + "Json对象：" + data);
            Log.e("1111111111111", "Action:：" + handlerName + "      " + "Json对象：" + data);

            Intent intent = new Intent(PrductDetailActivity.this, A0_LoginActivity.class);
            startActivity(intent);
        }
    }
}
