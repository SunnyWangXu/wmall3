package com.wjhgw.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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
import com.wjhgw.business.bean.MyLockBox;
import com.wjhgw.business.bean.Order_goods_list;
import com.wjhgw.business.bean.SelectOrder;
import com.wjhgw.business.bean.SelectOrderDatas;
import com.wjhgw.business.bean.TestPaypwd;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.dialog.EntryPaypwdDialog;
import com.wjhgw.ui.dialog.RestartInputAndFindPaypwdDialog;
import com.wjhgw.ui.dialog.SetPaypwdDialog;
import com.wjhgw.ui.view.listview.adapter.LvOrderListAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 确认订单Activity
 */
public class S0_ConfirmOrderActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llPayment;
    private PopupWindow paymentWindow;
    private TextView tvPaymentClosem;
    //    private int MAKEONLINE = 1;
    //    private int MAKEDOWNPAY = 1;
    //标记赠送点击
    private int MAKEDONATE = 1;
    //标记使用余额
    private int MAKEBALANCE = 1;
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
    private TextView tvTotalAmount;
    private String tvTotal;
    private ListView lvOrderList;
    private LinearLayout llConfirmOrderHeader;
    private LinearLayout llConfirmOrderFoot;
    private TextView tvFreightMessage;
    private TextView tvFreight;
    private TextView tvRealPay;
    private LvOrderListAdapter lvOrderListAdapter;
    private String freight;
    private double realPay = 0;
    private boolean isDownlinePay;
    private ImageView ivBalance;
    private LinearLayout llUseBalance;
    private TextView tvBalance;
    private TextView tvDonate;
    private MyLockBox userinformation;
    private boolean isHavePaypwd;
    private SetPaypwdDialog setPaypwdDialog;
    private EntryPaypwdDialog entryPaypwdDialog;
    private boolean truePaypwd;
    private TestPaypwd testPaypwd;
    private RestartInputAndFindPaypwdDialog restartInputAndFindPaypwdDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        String selectOrder = getIntent().getStringExtra("selectOrder");

        key = getKey();

        if (!key.equals("0") && key != null) {
            /**
             * 请求默认地址
             */
            load_Default_Addresst();

        }

        if (selectOrder != null) {
            /**
             * 解析选中订单
             */
            parseSelectOrdert(selectOrder);

        }

    }

    /**
     * 解析选中的商品订单
     */
    private void parseSelectOrdert(String selectOrderContent) {

        Gson gson = new Gson();
        if (selectOrderContent != null) {
            SelectOrder selectOrder = gson.fromJson(selectOrderContent, SelectOrder.class);
            if (selectOrder.status.code == 10000) {

                SelectOrderDatas selectOrderDatas = selectOrder.datas;
                freight = selectOrderDatas.store_cart_list.freight;
                String freightMessage = selectOrderDatas.store_cart_list.freight_message;
                tvFreight.setText("+ ¥ " + freight);
                tvFreightMessage.setText(freightMessage);

                realPay = getIntent().getDoubleExtra("realPay", 0);
                tvRealPay.setText(realPay + Double.valueOf(freight) + "");

                ArrayList<Order_goods_list> order_goods_lists = selectOrderDatas.store_cart_list.goods_list;

                lvOrderListAdapter = new LvOrderListAdapter(this, order_goods_lists);
                lvOrderList.setAdapter(lvOrderListAdapter);

            }

        }
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("确认订单");

        lvOrderList = (ListView) findViewById(R.id.lv_order_list);

        llConfirmOrderHeader = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.confirm_order_header, null);
        llConfirmOrderFoot = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.confirm_order_foot, null);

        lvOrderList.addHeaderView(llConfirmOrderHeader);
        lvOrderList.addFooterView(llConfirmOrderFoot);
    }

    @Override
    public void onFindViews() {
        llUseMessage = (LinearLayout) llConfirmOrderHeader.findViewById(R.id.ll_usemessage);
        llDonate = (LinearLayout) findViewById(R.id.ll_donate);
        tvDonate = (TextView) findViewById(R.id.tv_donate);
        ivDonate = (ImageView) findViewById(R.id.iv_donate);

        llOrderAddress = (LinearLayout) findViewById(R.id.ll_order_address);
        llPayment = (LinearLayout) findViewById(R.id.ll_payment);
        tvPayMethod = (TextView) findViewById(R.id.tv_pay_method);
        tvCommitOrder = (TextView) findViewById(R.id.tv_commit_order);

        tvUseName = (TextView) llConfirmOrderHeader.findViewById(R.id.tv_useName);
        tvUsePhone = (TextView) llConfirmOrderHeader.findViewById(R.id.tv_usePhone);
        tvUseAddress = (TextView) llConfirmOrderHeader.findViewById(R.id.tv_useAddress);

        tvTotalAmount = (TextView) findViewById(R.id.tv_total_amount);
        tvFreightMessage = (TextView) findViewById(R.id.tv_freight_message);
        tvFreight = (TextView) findViewById(R.id.tv_freight);

        tvRealPay = (TextView) findViewById(R.id.tv_real_pay);


        llUseBalance = (LinearLayout) findViewById(R.id.ll_use_balance);
        tvBalance = (TextView) findViewById(R.id.tv_use_balance);
        ivBalance = (ImageView) findViewById(R.id.iv_balance);

    }


    @Override
    public void onInitViewData() {
        tvTotal = getIntent().getStringExtra("tv_total");
        tvTotalAmount.setText(tvTotal);

    }

    @Override
    public void onBindListener() {

        llOrderAddress.setOnClickListener(this);
        llPayment.setOnClickListener(this);
        llDonate.setOnClickListener(this);
        llUseBalance.setOnClickListener(this);
        tvCommitOrder.setOnClickListener(this);

    }

    /**
     * activity 返回的数据 forResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
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
                isDownlinePay = tvPayMethod.getText().equals("货到付款");
                /**
                 * 付款方式弹出窗
                 */
                showPaymentWindow();

                break;

            case R.id.tv_commit_order:

                /**
                 * 判断是否有登录密码,没有就设置，有就去输入下单
                 */
                whetherHavePaypwd();

                break;

            case R.id.ll_donate:

                MAKEDONATE++;
                if (MAKEDONATE % 2 == 0) {
                    ivDonate.setImageResource(R.mipmap.ic_push_on);
                    llUseMessage.setVisibility(View.GONE);
                    tvDonate.setTextColor(Color.parseColor("#666666"));
                } else {
                    ivDonate.setImageResource(R.mipmap.ic_push_off);
                    llUseMessage.setVisibility(View.VISIBLE);
                    tvDonate.setTextColor(Color.parseColor("#333333"));
                }

                break;
            case R.id.ll_use_balance:

                MAKEBALANCE++;
                if (MAKEBALANCE % 2 == 0) {
                    ivBalance.setImageResource(R.mipmap.ic_push_on);
                    tvBalance.setTextColor(Color.parseColor("#666666"));

                } else {
                    ivBalance.setImageResource(R.mipmap.ic_push_off);
                    tvBalance.setTextColor(Color.parseColor("#333333"));
                }

                break;

            default:
                break;
        }

    }

    /**
     * 判断是否有登录密码
     */
    private void whetherHavePaypwd() {

        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Get_member_base_info, params, new RequestCallBack<String>() {


            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo.result != null) {
                    userinformation = gson.fromJson(responseInfo.result, MyLockBox.class);

                    isHavePaypwd = userinformation.datas.paypwd.equals("1");

                    if (!isHavePaypwd) {
                        /**
                         *建议设置支付密码的对话框
                         */
                        setPaypwdDialog = new SetPaypwdDialog(S0_ConfirmOrderActivity.this, "设置支付密码", "为了提高账号安全性，建议设置支付密码");

                        setPaypwdDialog.show();

                        setPaypwdDialog.tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                setPaypwdDialog.dismiss();
                            }
                        });

                        setPaypwdDialog.tvGotoSetpaypwd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setPaypwdDialog.dismiss();
                                Intent intent = new Intent(S0_ConfirmOrderActivity.this, M4_PaymentPasswordActivity.class);
                                startActivity(intent);
                            }
                        });

                    } else {

                        /**
                         *输入支付密码的对话框
                         */
                        entryPaypwdAndTest();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }

    /**
     * 输入支付密码的对话框
     */
    private void entryPaypwdAndTest() {
        entryPaypwdDialog = new EntryPaypwdDialog(S0_ConfirmOrderActivity.this);
        entryPaypwdDialog.show();

        /**
         * 弹出软键盘
         */
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 200);

        entryPaypwdDialog.tvEntryCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entryPaypwdDialog.dismiss();
            }
        });

        entryPaypwdDialog.tvEntryPaypwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edPaypwd = entryPaypwdDialog.etEntryPaypwd;

                String paypwd = edPaypwd.getText().toString();
                if (paypwd.equals("")) {
                    Toast.makeText(S0_ConfirmOrderActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }
                entryPaypwdDialog.dismiss();
                /**
                 * 验证支付密码
                 */
                testPaypwd(paypwd);

            }
        });
    }

    /**
     * 验证支付密码
     */
    private boolean testPaypwd(String paypwd) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("paypwd", paypwd);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Test_Paypwd, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo.result != null) {
                    Gson gson = new Gson();
                    testPaypwd = gson.fromJson(responseInfo.result, TestPaypwd.class);

                    if (testPaypwd.status.code == 10000) {
                        /**
                         * 支付密码正确跳转
                         */
                        entryPaypwdDialog.dismiss();

                        Intent intent = new Intent();
                        intent.setClass(S0_ConfirmOrderActivity.this, S3_SelectPaymentActivity.class);
                        startActivity(intent);

                    } else {
                        /**
                         * 支付密码不正确， 弹出重新输入或者找回支付密码
                         */
                        restartInputAndFindPaypwdDialog = new RestartInputAndFindPaypwdDialog(S0_ConfirmOrderActivity.this);

                        restartInputAndFindPaypwdDialog.show();
                        restartInputAndFindPaypwdDialog.tvRestartInput.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                restartInputAndFindPaypwdDialog.dismiss();
                                /**
                                 *输入支付密码的对话框
                                 */
                                entryPaypwdAndTest();
                            }
                        });


                        restartInputAndFindPaypwdDialog.tvFindPaypwd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                restartInputAndFindPaypwdDialog.dismiss();
                                Intent intent = new Intent(S0_ConfirmOrderActivity.this, M4_PaymentPasswordActivity.class);
                                startActivity(intent);

                            }
                        });


                    }
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(S0_ConfirmOrderActivity.this, testPaypwd.status.msg, Toast.LENGTH_SHORT).show();
            }
        });

        return truePaypwd;
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

        llOnlinePay = (LinearLayout) paymentWindowLayout.findViewById(R.id.ll_online_pay);
        ivOnlinePay = (ImageView) paymentWindowLayout.findViewById(R.id.iv_online_pay);

        llDownlinePay = (LinearLayout) paymentWindowLayout.findViewById(R.id.ll_downline_pay);
        ivDownlinePay = (ImageView) paymentWindowLayout.findViewById(R.id.iv_downline_pay);
        /**
         * 如果选中货到付款点击弹出弹窗默认选中货到付款
         */
        if (isDownlinePay) {
            ivOnlinePay.setImageResource(R.mipmap.ic_order_blank);
            ivDownlinePay.setImageResource(R.mipmap.ic_order_select);

            tvPayMethod.setText("货到付款");
        }

        vWindow = (View) paymentWindowLayout.findViewById(R.id.v_window);
        vWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentWindow.dismiss();
            }
        });

        llOnlinePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivDownlinePay.setImageResource(R.mipmap.ic_order_blank);
                ivOnlinePay.setImageResource(R.mipmap.ic_order_select);

                paymentWindow.dismiss();
                tvPayMethod.setText("在线支付");
            }
        });


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
