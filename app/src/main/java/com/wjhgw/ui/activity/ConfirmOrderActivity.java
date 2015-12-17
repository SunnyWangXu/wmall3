package com.wjhgw.ui.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;

/**
 * 确认订单Activity
 */
public class ConfirmOrderActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llPayment;
    private PopupWindow paymentWindow;
    private TextView tvPaymentClosem;
    //    private int MAKEONLINE = 1;
//    private int MAKEDOWNPAY = 1;
    private int MAKEDONATE = 1;
    private LinearLayout llOnlinePay;
    private LinearLayout llDownlinePay;
    private ImageView ivOnlinePay;
    private ImageView ivDownlinePay;
    private TextView tvPayMethod;
    private View vWindow;
    private TextView tvCommitOrder;
    private LinearLayout llDonate;
    private ImageView ivDonate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);


    }

    @Override
    public void onInit() {
        setUp();
        setTitle("确认订单");
    }

    @Override
    public void onFindViews() {
        llDonate = (LinearLayout) findViewById(R.id.ll_donate);
        ivDonate = (ImageView) findViewById(R.id.iv_donate);

        llPayment = (LinearLayout) findViewById(R.id.ll_payment);
        tvPayMethod = (TextView) findViewById(R.id.tv_pay_method);
        tvCommitOrder = (TextView) findViewById(R.id.tv_commit_order);

    }


    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {

        llDonate.setOnClickListener(this);
        llPayment.setOnClickListener(this);
        tvCommitOrder.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ll_payment:
                /**
                 * 付款方式弹出窗
                 */
                showPaymentWindow();

                break;

            case R.id.tv_commit_order:
                intent.setClass(this, SelectPaymentActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_donate:
                MAKEDONATE ++;
                if(MAKEDONATE % 2 == 0){
                    ivDonate.setImageResource(R.mipmap.ic_push_on);
                }else{
                    ivDonate.setImageResource(R.mipmap.ic_push_off);
                }

                break;
            default:
                break;
        }

    }

    /**
     * 付款方式弹出窗
     */
    private void showPaymentWindow() {
        View paymentWindowLayout = getLayoutInflater().inflate(R.layout.payment_window, null);

        paymentWindow = new PopupWindow(paymentWindowLayout, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置背景,必须要设置
        paymentWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置窗口弹出状态时，点击窗口之外的部分，窗口自动消失
        paymentWindow.setOutsideTouchable(true);
        // 显示窗口
        paymentWindow.showAtLocation(ConfirmOrderActivity.this.findViewById(R.id.start_window), Gravity.BOTTOM, 0, 0);

        tvPaymentClosem = (TextView) paymentWindowLayout.findViewById(R.id.tv_payment_confirm);
        tvPaymentClosem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentWindow.dismiss();

            }
        });

        vWindow = (View) paymentWindowLayout.findViewById(R.id.v_window);
        vWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentWindow.dismiss();
            }
        });

        llOnlinePay = (LinearLayout) paymentWindowLayout.findViewById(R.id.ll_online_pay);
        ivOnlinePay = (ImageView) paymentWindowLayout.findViewById(R.id.iv_online_pay);

        llOnlinePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivDownlinePay.setImageResource(R.mipmap.ic_order_blank);
                ivOnlinePay.setImageResource(R.mipmap.ic_order_select);

                paymentWindow.dismiss();
                tvPayMethod.setText("在线支付");
            }
        });

        llDownlinePay = (LinearLayout) paymentWindowLayout.findViewById(R.id.ll_downline_pay);
        ivDownlinePay = (ImageView) paymentWindowLayout.findViewById(R.id.iv_downline_pay);

        llDownlinePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivOnlinePay.setImageResource(R.mipmap.ic_order_blank);
                ivDownlinePay.setImageResource(R.mipmap.ic_order_select);

                paymentWindow.dismiss();
                tvPayMethod.setText("货到付款");
            }
        });
    }

}
