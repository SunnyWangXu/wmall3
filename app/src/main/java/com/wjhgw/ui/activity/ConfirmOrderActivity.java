package com.wjhgw.ui.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
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
    private TextView tvPaymentConfirm;

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
        llPayment = (LinearLayout) findViewById(R.id.ll_payment);

    }


    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {

        llPayment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_payment:
                showPaymentWindow();

                break;

            default:
                break;
        }

    }

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

        tvPaymentConfirm = (TextView) paymentWindowLayout.findViewById(R.id.tv_payment_confirm);
        tvPaymentConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentWindow.dismiss();

            }
        });
    }
}
