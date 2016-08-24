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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.ui.dialog.LoadDialog;
import com.wjhgw.ui.dialog.WX_share_Dialog;

import java.util.HashMap;

/**
 * 我的推广Activity
 * Created by Administrator on 2016/7/14 0014.
 */
public class W3_MyExtendedActivity extends BaseActivity implements View.OnClickListener {

    private WebView webView;
    private LoadDialog Dialog;
    private String ua;
    private String url;
    private HashMap<String, String> keyMap;
    private ImageView ivExtendedBack;
    private ImageView iv_title_right;
    private IWXAPI api;
    private WX_share_Dialog wx_share_Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_extended);
        api = WXAPIFactory.createWXAPI(this, "wx99a6bd9b7bdbf645");
        api.registerApp("wx99a6bd9b7bdbf645");
        webView = (WebView) findViewById(R.id.wb_extended);
        /**
         * 加载WebView
         */
        toLoadWebView();
    }

    @Override
    public void onInit() {
        ivExtendedBack = (ImageView) findViewById(R.id.iv_extended_back);
        iv_title_right = (ImageView) findViewById(R.id.iv_title_right);
    }

    @Override
    public void onFindViews() {

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        ivExtendedBack.setOnClickListener(this);
        iv_title_right.setOnClickListener(this);
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

        url = BaseQuery.serviceUrl() + "/wap/index.php?act=bonus&op=index#!/";

        keyMap = new HashMap<>();
        if (!getKey().equals("0")) {
            keyMap.put("authentication", getKey());
            webView.loadUrl(url, keyMap);
        } else {
            this.getSharedPreferences("key", MODE_APPEND).edit().putString("key", "0").commit();
            startActivity(new Intent(this, A0_LoginActivity.class));
            finish(false);
        }

        // 打开网页时不调用系统浏览器， 而是在本WebView中显示：
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

//        webView.setWebChromeClient(new WebChromeClient());
//        //调用javascript
//        webView.addJavascriptInterface(new WMallBridge(this), "WMallBridge");

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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_extended_back:

                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                break;
            case R.id.iv_title_right:
                String intr_id = W3_MyExtendedActivity.this.getSharedPreferences("member_id", MODE_APPEND).getString("member_id", "0");
                final String Url = "http://www.wjhgw.com/wap/index.php?act=index&op=index&intr_id=" + intr_id;
                wx_share_Dialog = new WX_share_Dialog(this);
                wx_share_Dialog.show();
                wx_share_Dialog.ll_wx_friends.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wechatShare(0,Url,"喝酒赚钱两不误，快来这里潇洒");
                        wx_share_Dialog.dismiss();
                    }
                });
                wx_share_Dialog.ll_wx_circle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wechatShare(1,Url,"喝酒赚钱两不误，快来这里潇洒");
                        wx_share_Dialog.dismiss();
                    }
                });
                wx_share_Dialog.tv_payment_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wx_share_Dialog.dismiss();
                    }
                });
                break;

            default:
                break;
        }
    }



    private void wechatShare(int flag, String giftUrl, String Name) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = giftUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = Name;
        msg.description = "喝酒有优惠，分享能赚钱，网上购物新玩法，败家的你，再也不用担心月底吃土喝风了，速戳！快来！！";

        Bitmap thumb = null;
        //thumb = getBitmap(goods_image);
        thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_wx_share);

        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
        //finish();
    }
    }

