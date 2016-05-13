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
import android.widget.LinearLayout;

import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.ui.dialog.LoadDialog;

import java.util.HashMap;

/**
 * 通用WebView
 */
public class CurrencyWebViewActivity extends BaseActivity implements View.OnClickListener {
    private WebView webView;
    private String ua;
    private LoadDialog Dialog;
    private String Shopping_Cart;
    private HashMap<String, String> keyMap;
    private String url;
    private LinearLayout title_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prduct_detail);

        webView = (WebView) findViewById(R.id.wb_prduct_detail);

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

        String name = getIntent().getStringExtra("name");
        if (name != null) {
            setTitle(name);
        }

        url = getIntent().getStringExtra("url");

        keyMap = new HashMap<>();
        if (!getKey().equals("0")) {
            keyMap.put("authentication", getKey());
            webView.loadUrl(url, keyMap);
        } else {
            this.getSharedPreferences("key", MODE_APPEND).edit().putString("key", "0").commit();
            startActivity(new Intent(this, A0_LoginActivity.class));
            finish(false);
        }

        /*// 打开网页时不调用系统浏览器， 而是在本WebView中显示：
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
    }

    @Override
    public void onFindViews() {
        title_container = (LinearLayout) findViewById(R.id.title_container);
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
            if (handlerName.equals("cabinetHandler")) {
                Intent intent = new Intent(CurrencyWebViewActivity.this, CabinetActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
