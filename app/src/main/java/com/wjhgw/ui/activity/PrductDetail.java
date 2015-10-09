package com.wjhgw.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.wjhgw.R;

/**
 * 商品详情页
 */
public class PrductDetail extends Activity implements View.OnClickListener {
    private WebView vb;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prduct_detail_layout);

        vb = (WebView) findViewById(R.id.vb_prduct_detail);
        back = (ImageView) findViewById(R.id.iv_shopping_back);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_shopping_back:
                this.finish();
                break;

            default:
                break;
        }

    }
}
