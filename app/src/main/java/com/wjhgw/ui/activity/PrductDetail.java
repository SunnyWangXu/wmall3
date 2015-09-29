package com.wjhgw.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.wjhgw.R;

/**
 * 商品详情页
 */
public class PrductDetail extends Activity{
    private WebView vb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prduct_detail_layout);

         vb = (WebView) findViewById(R.id.vb_prduct_detail);

    }
}
