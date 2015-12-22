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

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.bean.Address_list;
import com.wjhgw.config.ApiInterface;

/**
 * 确认订单Activity
 */
public class S0_ConfirmOrderActivity extends BaseActivity implements View.OnClickListener {

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
    private LinearLayout llUseMessage;
    private LinearLayout llOrderAddress;
    private TextView tvUseName;
    private TextView tvUsePhone;
    private TextView tvUseAddress;
    private String key;
    private String useName;
    private String usePhone;
    private String useAddressInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        key = getKey();

        if (!key.equals("0") && key != null) {
            /**
             * 请求默认地址
             */
            load_Default_Addresst();

        }
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("确认订单");

    }

    @Override
    public void onFindViews() {
        llUseMessage = (LinearLayout) findViewById(R.id.ll_usemessage);
        llDonate = (LinearLayout) findViewById(R.id.ll_donate);
        ivDonate = (ImageView) findViewById(R.id.iv_donate);

        llOrderAddress = (LinearLayout) findViewById(R.id.ll_order_address);
        llPayment = (LinearLayout) findViewById(R.id.ll_payment);
        tvPayMethod = (TextView) findViewById(R.id.tv_pay_method);
        tvCommitOrder = (TextView) findViewById(R.id.tv_commit_order);

        tvUseName = (TextView) findViewById(R.id.tv_useName);
        tvUsePhone = (TextView) findViewById(R.id.tv_usePhone);
        tvUseAddress = (TextView) findViewById(R.id.tv_useAddress);

    }


    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {

        llDonate.setOnClickListener(this);
        llPayment.setOnClickListener(this);
        tvCommitOrder.setOnClickListener(this);
        llOrderAddress.setOnClickListener(this);

    }

    /**
     * activity 返回的数据 forResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null){
            useName = data.getStringExtra("tureName");
            usePhone = data.getStringExtra("phone");
            useAddressInfo = data.getStringExtra("addressInfo");
            if (useName != null && usePhone != null && useAddressInfo != null) {
                tvUseName.setText(useName);
                tvUsePhone.setText(usePhone);
                tvUseAddress.setText(useAddressInfo);
            }

        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ll_order_address:
                intent.setClass(this, S1_OrderAddressActivity.class);
                startActivityForResult(intent, 55555);

                break;
            case R.id.ll_payment:
                /**
                 * 付款方式弹出窗
                 */
                showPaymentWindow();

                break;

            case R.id.tv_commit_order:
                intent.setClass(this, S3_SelectPaymentActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_donate:
                MAKEDONATE++;
                if (MAKEDONATE % 2 == 0) {
                    ivDonate.setImageResource(R.mipmap.ic_push_on);
                    llUseMessage.setVisibility(View.GONE);
                } else {
                    ivDonate.setImageResource(R.mipmap.ic_push_off);
                    llUseMessage.setVisibility(View.VISIBLE);
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
        paymentWindow.showAtLocation(S0_ConfirmOrderActivity.this.findViewById(R.id.start_window), Gravity.BOTTOM, 0, 0);

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


    /**
     * 请求地址列表
     */
    public void load_Default_Addresst() {
        super.StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Address_list, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Address_list address_list = gson.fromJson(responseInfo.result, Address_list.class);
                    S0_ConfirmOrderActivity.super.Dismiss();
                    if (address_list.status.code == 10000) {

                        tvUseName.setText(address_list.datas.get(0).true_name);
                        tvUsePhone.setText(address_list.datas.get(0).mob_phone);
                        tvUseAddress.setText(address_list.datas.get(0).area_info + " " + address_list.datas.get(0).address);


                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

                showToastShort("请求失败");

                String useName = getIntent().getStringExtra("tureName");
                String usePhone = getIntent().getStringExtra("phone");
                String useAddressInfo = getIntent().getStringExtra("addressInfo");

                if (useName != null && usePhone != null && useAddressInfo != null) {
                    tvUseName.setText(useName);
                    tvUsePhone.setText(usePhone);
                    tvUseAddress.setText(useAddressInfo);
                }
            }
        });
    }

}
