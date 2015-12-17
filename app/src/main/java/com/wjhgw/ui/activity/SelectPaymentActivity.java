package com.wjhgw.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;

/**
 * 选择支付
 */
public class SelectPaymentActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llBalancePay;
    private int MAKEBALANCE = 1;
    private LinearLayout llWeixinPay;
    private LinearLayout llAlipayPay;
    private ImageView ivBalancePay;
    private ImageView ivWeixinPay;
    private ImageView ivAlipayPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment);

    }

    @Override
    public void onInit() {
        setUp();
        setTitle("选择支付");
    }

    @Override
    public void onFindViews() {
        llBalancePay = (LinearLayout) findViewById(R.id.ll_balance_pay);
        ivBalancePay = (ImageView) findViewById(R.id.iv_balance_pay);
        llWeixinPay = (LinearLayout) findViewById(R.id.ll_weixin_pay);
        ivWeixinPay = (ImageView) findViewById(R.id.iv_weixin_pay);
        llAlipayPay = (LinearLayout) findViewById(R.id.ll_alipay_pay);
        ivAlipayPay = (ImageView) findViewById(R.id.iv_alipay_pay);

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        llBalancePay.setOnClickListener(this);
        llWeixinPay.setOnClickListener(this);
        llAlipayPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_balance_pay:
                MAKEBALANCE++;
                if (MAKEBALANCE % 2 == 0) {
                    ivBalancePay.setImageResource(R.mipmap.ic_push_on);
                } else {
                    ivBalancePay.setImageResource(R.mipmap.ic_push_off);
                }

                break;

            case R.id.ll_weixin_pay:
                ivAlipayPay.setImageResource(R.mipmap.ic_order_blank);
                ivWeixinPay.setImageResource(R.mipmap.ic_order_select);

                break;

            case R.id.ll_alipay_pay:
                ivWeixinPay.setImageResource(R.mipmap.ic_order_blank);
                ivAlipayPay.setImageResource(R.mipmap.ic_order_select);

                break;

            default:
                break;
        }
    }
}
