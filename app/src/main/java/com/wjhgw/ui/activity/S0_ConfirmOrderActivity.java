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
import com.wjhgw.business.bean.CheckAddressSupport;
import com.wjhgw.business.bean.MyLockBox;
import com.wjhgw.business.bean.Order_goods_list;
import com.wjhgw.business.bean.PayOrder;
import com.wjhgw.business.bean.SelectOrder;
import com.wjhgw.business.bean.SelectOrderDatas;
import com.wjhgw.business.bean.TestPaypwd;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.dialog.EntryPaypwdDialog;
import com.wjhgw.ui.dialog.RestartInputAndFindPaypwdDialog;
import com.wjhgw.ui.dialog.SetPaypwdDialog;
import com.wjhgw.ui.view.listview.adapter.LvOrderListAdapter;

import java.math.BigDecimal;
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
    //标记使用充值卡余额
    private int MAKEAVAILABLERCBALANCE = 1;
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
    private int freight;
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
    private boolean isUseBalance = false;
    private boolean isDonate = false;
    private String cart_id;
    private String address_id;
    private String vat_hash;
    private String freight_hash;
    private String offpay_hash;
    private String offpay_hash_batch;
    private String paypwd;
    private EditText etPayMessage;
    private TextView tvAvailablePredeposit;
    private TextView tvAvailableRcBalance;
    private LinearLayout llAvailableRcBalance;
    private boolean isUseRcBalance = false;
    private ImageView ivAvailableRcBalance;
    private TextView tvRcBalance;
    private LinearLayout llUseMessage01;
    private TextView tvNotAddress;
    private LinearLayout llInvoice;
    private String invoice_id = "";
    private String invoice_title = "";
    private String invoice_content = "";
    private TextView tvInvoice;
    private Intent intent;
    private boolean Offpay = false;
    private TextView tvDownlinePay;
    private int START = 0;
    private BigDecimal bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);


    }

    @Override
    protected void onResume() {
        super.onResume();

        START++;

        String selectOrder = getIntent().getStringExtra("selectOrder");

        if (START == 1 && selectOrder != null) {
            /**
             * 解析选中订单
             */
            parseSelectOrdert(selectOrder);
        }

        cart_id = getIntent().getStringExtra("cart_id");

        if (START == 1 && !getKey().equals("0")) {
            /**
             * 请求默认地址
             */
            load_Default_Addresst();
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

                String freightMessage = selectOrderDatas.store_cart_list.freight_desc;
                tvFreightMessage.setText(freightMessage);

                if (selectOrderDatas.address_info != null) {
                    address_id = selectOrderDatas.address_info.address_id;
                }
                vat_hash = selectOrderDatas.vat_hash;
                freight_hash = selectOrderDatas.freight_hash;

                if (selectOrderDatas.available_predeposit == null) {
                    tvAvailablePredeposit.setText("0.00");
                    isUseBalance = false;

                } else {
                    tvAvailablePredeposit.setText(selectOrderDatas.available_predeposit);
                }

                if (selectOrderDatas.available_rc_balance == null) {
                    tvAvailableRcBalance.setText("0.00");
                    isUseRcBalance = false;

                } else {
                    tvAvailableRcBalance.setText(selectOrderDatas.available_rc_balance);
                }

                if (address_id != null) {
                    /**
                     * 检查地址是否支持货到付款
                     */
                    checkAddressSupport();
                }


                if (isDonate) {
                    tvFreight.setText("+ ¥ 0.00");
                } else {
                    tvFreight.setText("+ ¥ " + freight + ".00");
                }


                realPay = getIntent().getDoubleExtra("realPay", 0);
                BigDecimal bd = new BigDecimal(realPay + Double.valueOf(freight));
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                tvRealPay.setText("" + bd);

                ArrayList<Order_goods_list> order_goods_lists = selectOrderDatas.store_cart_list.goods_list;

                lvOrderListAdapter = new LvOrderListAdapter(this, order_goods_lists);
                lvOrderList.setAdapter(lvOrderListAdapter);
            } else {
                overtime(selectOrder.status.code, selectOrder.status.msg);
            }

        }
    }

    /**
     * 检查地址是否支持货到付款
     */
    private void checkAddressSupport() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("freight_hash", freight_hash);
        params.addBodyParameter("address_id", address_id);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Check_Address_Support, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                CheckAddressSupport checkAddressSupport = gson.fromJson(responseInfo.result, CheckAddressSupport.class);

                if (checkAddressSupport.status.code == 10000) {
                    if (checkAddressSupport.datas != null) {
                        String allowOffpay = checkAddressSupport.datas.allow_offpay;
                        if (!allowOffpay.equals("1")) {
                            //不支持货到付款
                            Offpay = true;
                            tvPayMethod.setText("在线支付");
                            isDownlinePay = false;
                        } else {
                            //支持货到付款
                            Offpay = false;
                        }

                        offpay_hash = checkAddressSupport.datas.offpay_hash;
                        offpay_hash_batch = checkAddressSupport.datas.offpay_hash_batch;
                    }
                } else {
                    overtime(checkAddressSupport.status.code, checkAddressSupport.status.msg);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToastShort("网络错误");
            }
        });

    }

    @Override
    public void onInit() {
        setUp();
        setTitle("填写订单");

        lvOrderList = (ListView) findViewById(R.id.lv_order_list);

        llConfirmOrderHeader = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.confirm_order_header, null);
        llConfirmOrderFoot = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.confirm_order_foot, null);

        lvOrderList.addHeaderView(llConfirmOrderHeader);
        lvOrderList.addFooterView(llConfirmOrderFoot);
    }

    @Override
    public void onFindViews() {
        llUseMessage01 = (LinearLayout) llConfirmOrderHeader.findViewById(R.id.ll_usemessage_01);
        llUseMessage = (LinearLayout) llConfirmOrderHeader.findViewById(R.id.ll_usemessage);
        llDonate = (LinearLayout) findViewById(R.id.ll_donate);
        tvDonate = (TextView) findViewById(R.id.tv_donate);
        ivDonate = (ImageView) findViewById(R.id.iv_donate);

        llOrderAddress = (LinearLayout) findViewById(R.id.ll_order_address);
        llPayment = (LinearLayout) findViewById(R.id.ll_payment);
        tvPayMethod = (TextView) findViewById(R.id.tv_pay_method);
        tvCommitOrder = (TextView) findViewById(R.id.tv_commit_order);

        llInvoice = (LinearLayout) findViewById(R.id.ll_invoice);
        tvInvoice = (TextView) findViewById(R.id.tv_invoice);

        tvNotAddress = (TextView) findViewById(R.id.tv_not_address);
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
        tvAvailablePredeposit = (TextView) findViewById(R.id.tv_available_predeposit);

        llAvailableRcBalance = (LinearLayout) findViewById(R.id.ll_available_rc_balance);
        tvRcBalance = (TextView) findViewById(R.id.tv_is_use_rc_balance);
        ivAvailableRcBalance = (ImageView) findViewById(R.id.iv_available_rc_balance);
        tvAvailableRcBalance = (TextView) findViewById(R.id.tv_available_rc_balance);

        etPayMessage = (EditText) findViewById(R.id.et_pay_message);

    }


    @Override
    public void onInitViewData() {
        tvTotal = getIntent().getStringExtra("tv_total");
        bd = new BigDecimal(tvTotal);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        tvTotalAmount.setText("" + bd);

    }

    @Override
    public void onBindListener() {
        tvNotAddress.setOnClickListener(this);
        llOrderAddress.setOnClickListener(this);
        llPayment.setOnClickListener(this);
        llInvoice.setOnClickListener(this);
        llDonate.setOnClickListener(this);
        llUseBalance.setOnClickListener(this);
        llAvailableRcBalance.setOnClickListener(this);
        tvCommitOrder.setOnClickListener(this);

    }

    /**
     * activity 返回的数据 forResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 订单收货地址返回
        if (resultCode == 55555) {
            if (data == null) {
                llUseMessage01.setVisibility(View.GONE);
                tvNotAddress.setVisibility(View.VISIBLE);
            }
            if (data != null) {
                tvNotAddress.setVisibility(View.GONE);
                llUseMessage01.setVisibility(View.VISIBLE);

                useName = data.getStringExtra("tureName");
                usePhone = data.getStringExtra("phone");
                useAddressInfo = data.getStringExtra("addressInfo");
                address_id = data.getStringExtra("addressId");

                if (useName != null && usePhone != null && useAddressInfo != null) {
                    tvUseName.setText(useName);
                    tvUsePhone.setText(usePhone);
                    tvUseAddress.setText(useAddressInfo);
                }

                /**
                 * 检查地址是否支持货到付款
                 */
                checkAddressSupport();
            }
        }

        //选择发票详情的Activity返回
        if (resultCode == 22222 && data != null) {

            int invoice_id_int = data.getIntExtra("invoice_id", 0);

            invoice_id = invoice_id_int + "";
            invoice_title = data.getStringExtra("invoice_title");
            invoice_content = data.getStringExtra("invoice_content");

            if (invoice_content.equals("")) {
                tvInvoice.setText("不需要发票");
                tvInvoice.setTextColor(Color.parseColor("#cccccc"));
            } else {
                tvInvoice.setText(invoice_title + "-" + invoice_content);
                tvInvoice.setTextColor(Color.parseColor("#333333"));
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        //选中货到付款
        isDownlinePay = tvPayMethod.getText().equals("货到付款");

        switch (v.getId()) {
            case R.id.ll_order_address:
                intent.setClass(this, S1_OrderAddressActivity.class);
                startActivityForResult(intent, 55555);
                break;

            case R.id.tv_not_address:
                intent.setClass(this, S1_OrderAddressActivity.class);
                startActivityForResult(intent, 55555);

                break;
            case R.id.ll_payment:
                /**
                 * 付款方式弹出窗
                 */
                showPaymentWindow();

                break;

            case R.id.ll_invoice:
                intent.setClass(this, S2_InvoiceActivity.class);
                intent.putExtra("invoiceId", invoice_id);
                startActivityForResult(intent, 22222);

                break;
            case R.id.ll_donate:
                if (!isDownlinePay) {
                    MAKEDONATE++;
                    if (MAKEDONATE % 2 == 0) {
                        ivDonate.setImageResource(R.mipmap.ic_push_on);
                        llUseMessage.setVisibility(View.GONE);
                        tvDonate.setTextColor(Color.parseColor("#333333"));
                        //存入酒柜，赠送他人
                        isDonate = true;
                    } else {
                        ivDonate.setImageResource(R.mipmap.ic_push_off);
                        llUseMessage.setVisibility(View.VISIBLE);
                        tvDonate.setTextColor(Color.parseColor("#cccccc"));
                        //不存入酒柜，赠送他人
                        isDonate = false;
                    }

                    if (isDonate) {
                        tvFreight.setText("+ ¥ 0.00");
                        tvRealPay.setText(realPay + "0");
                    } else {
                        tvFreight.setText("+ ¥ " + freight + ".00");
                        tvRealPay.setText(realPay + Double.valueOf(freight) + "0");
                    }
                }


                break;
            case R.id.ll_use_balance:
                /**
                 * 余额为0.00 不能开启余额支付
                 */
                if (!tvAvailablePredeposit.getText().equals("0.00")) {

                    /**
                     * 线上支付才能开启余额支付
                     */
                    if (!isDownlinePay) {
                        MAKEBALANCE++;
                        if (MAKEBALANCE % 2 == 0) {
                            ivBalance.setImageResource(R.mipmap.ic_push_on);
                            tvBalance.setTextColor(Color.parseColor("#333333"));
                            //使用余额
                            isUseBalance = true;

                        } else {
                            ivBalance.setImageResource(R.mipmap.ic_push_off);
                            tvBalance.setTextColor(Color.parseColor("#cccccc"));
                            //不使用余额
                            isUseBalance = false;
                        }

                        if (isUseBalance && isUseRcBalance) {
                            showToastLong("余额和充值卡同时开启时，系统优先使用余额支付！");
                        }
                    }
                   /* *//**
                     * 余额支付和充值卡支付只能开启一个
                     *//*
                    double tvRealPay1 = Double.valueOf(tvRealPay.getText() + "");
                    double tvAvailableRcBalance2 = Double.valueOf(tvAvailableRcBalance.getText() + "");
                    if (MAKEAVAILABLERCBALANCE % 2 == 0 && tvRealPay1 <= tvAvailableRcBalance2) {
                        *//**
                     * 线上支付才能开启余额支付
                     *//*
                        if (!isDownlinePay) {
                            MAKEBALANCE++;
                            if (MAKEBALANCE % 2 == 0) {
                                ivBalance.setImageResource(R.mipmap.ic_push_on);
                                tvBalance.setTextColor(Color.parseColor("#333333"));
                                //使用余额
                                isUseBalance = true;

                                ivAvailableRcBalance.setImageResource(R.mipmap.ic_push_off);
                                tvRcBalance.setTextColor(Color.parseColor("#cccccc"));
                                //不使用充值卡余额
                                isUseRcBalance = false;
                                MAKEAVAILABLERCBALANCE++;

                            } else {
                                ivBalance.setImageResource(R.mipmap.ic_push_off);
                                tvBalance.setTextColor(Color.parseColor("#cccccc"));
                                //不使用余额
                                isUseBalance = false;
                            }
                        }
                    } else {

                    }*/
                }
                break;

            case R.id.ll_available_rc_balance:
                /**
                 * 充值卡余额为0.00 不能开启使用充值卡余额
                 */
                if (!tvAvailableRcBalance.getText().equals("0.00")) {

                    /**
                     * 线上支付才能开启余额支付
                     */
                    if (!isDownlinePay) {
                        MAKEAVAILABLERCBALANCE++;
                        if (MAKEAVAILABLERCBALANCE % 2 == 0) {
                            ivAvailableRcBalance.setImageResource(R.mipmap.ic_push_on);
                            tvRcBalance.setTextColor(Color.parseColor("#333333"));
                            //使用余额
                            isUseRcBalance = true;

                        } else {
                            ivAvailableRcBalance.setImageResource(R.mipmap.ic_push_off);
                            tvRcBalance.setTextColor(Color.parseColor("#cccccc"));
                            //不使用余额
                            isUseRcBalance = false;
                        }
                        if (isUseBalance && isUseRcBalance) {
                            showToastLong("余额支付和充值卡支付同时使用时，系统优先使用充值卡支付！");
                        }
                    }
                   /* *//**
                     * 余额支付和充值卡支付只能开启一个
                     *//*
                    double tvRealPay1 = Double.valueOf(tvRealPay.getText() + "");
                    double tvAvailablePredeposit3 = Double.valueOf(tvAvailablePredeposit.getText() + "");
                    if (MAKEBALANCE % 2 == 0 && tvRealPay1 <= tvAvailablePredeposit3) {
                        *//**
                     * 线上支付才能开启余额支付
                     *//*
                        if (!isDownlinePay) {
                            MAKEAVAILABLERCBALANCE++;
                            if (MAKEAVAILABLERCBALANCE % 2 == 0) {
                                ivAvailableRcBalance.setImageResource(R.mipmap.ic_push_on);
                                tvRcBalance.setTextColor(Color.parseColor("#333333"));
                                //使用余额
                                isUseRcBalance = true;

                                ivBalance.setImageResource(R.mipmap.ic_push_off);
                                tvBalance.setTextColor(Color.parseColor("#cccccc"));
                                //不使用余额
                                isUseBalance = false;
                                MAKEBALANCE++;

                            } else {
                                ivAvailableRcBalance.setImageResource(R.mipmap.ic_push_off);
                                tvRcBalance.setTextColor(Color.parseColor("#cccccc"));
                                //不使用余额
                                isUseRcBalance = false;
                            }
                        }
                    } else {

                    }*/

                }
                break;

            case R.id.tv_commit_order:
                if (tvUseAddress.equals("")) {
                    showToastLong("请填写收货地址");
                    
                } else {

                    if (isDownlinePay) {
                        /**
                         * 提交订单
                         */
                        CommitOrder();
                    } else if (isUseBalance || isUseRcBalance) {
                        /**
                         * 判断是否有登录密码,没有就设置，有就去输入下单
                         */
                        whetherHavePaypwd();
                    } else {
                        /**
                         * 提交订单
                         */
                        CommitOrder();
                    }
                }

                break;
            default:
                break;
        }

    }

    /**
     * 提交订单
     */
    private void CommitOrder() {
        super.StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        if (isDonate) {
            params.addBodyParameter("cabinet_operation", "1");
            params.addBodyParameter("send_other", "1");
        }
        if (getIntent().getStringExtra("for").equals("forShopp")) {
            params.addBodyParameter("ifcart", "1");
        }
        if (cart_id != null) {
            params.addBodyParameter("cart_id", cart_id);
        }
        if (address_id != null) {
            params.addBodyParameter("address_id", address_id);
        }
        if (vat_hash != null) {
            params.addBodyParameter("vat_hash", vat_hash);
        }
        if (offpay_hash != null) {
            params.addBodyParameter("offpay_hash", offpay_hash);
        }
        if (offpay_hash_batch != null) {
            params.addBodyParameter("offpay_hash_batch", offpay_hash_batch);
        }

        if (!isDownlinePay) {
            params.addBodyParameter("pay_name", "online");
        } else {
            params.addBodyParameter("pay_name", "offline");
        }

        if (!invoice_title.equals("")) {
            params.addBodyParameter("inv_title", invoice_title);
        }

        if (!invoice_content.equals("")) {
            params.addBodyParameter("inv_content", invoice_content);
        }

        if (isUseBalance) {
            params.addBodyParameter("pd_pay", "1");
            params.addBodyParameter("password", paypwd);

        } else {
            params.addBodyParameter("pd_pay", "0");
        }
        if (isUseRcBalance) {
            params.addBodyParameter("rcb_pay", "1");
            params.addBodyParameter("password", paypwd);

        } else {
            params.addBodyParameter("rcb_pay", "0");
        }

        params.addBodyParameter("pay_message", etPayMessage.getText().toString());

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Buy_step2, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                S0_ConfirmOrderActivity.super.Dismiss();
                Gson gson = new Gson();
                PayOrder payOrder = gson.fromJson(responseInfo.result, PayOrder.class);

                if (payOrder.status.code == 10000) {

                    if (isDownlinePay) {
                        Intent intent = new Intent(S0_ConfirmOrderActivity.this, D0_OrderActivity.class);
                        intent.putExtra("order_state", "");
                        intent.putExtra("name", "所有订单");
                        startActivity(intent);
                        finish();
                    } else {

                        /**
                         * （余额+上充值卡余额）- 需要使用的金额
                         */
                        double balance;
                        double rcBalance;

                        if (isUseBalance) {
                            balance = Double.valueOf(tvAvailablePredeposit.getText().toString());

                        } else {
                            balance = 0.00;
                        }
                        if (isUseRcBalance) {
                            rcBalance = Double.valueOf(tvAvailableRcBalance.getText().toString());
                        } else {
                            rcBalance = 0.00;
                        }
                        double yu = (rcBalance + balance) - Double.valueOf(tvRealPay.getText().toString());

                        if (isUseBalance || isUseRcBalance && yu > 0) {
                            /**
                             * 选择了余额支付或者使用充值卡余额支付并且余额大于选中商品金额直接购买成功
                             */
                            showToastShort("余额支付成功");
                            Intent intent = new Intent(S0_ConfirmOrderActivity.this, D0_OrderActivity.class);
                            intent.putExtra("order_state", "");
                            intent.putExtra("name", "所有订单");
                            startActivity(intent);
                            finish();
                        } else {
                            if (payOrder.datas.state = true && payOrder.datas.data.type.equals("order")) {
                                showToastShort("订单提交成功");
                                /**
                                 * 选择了余额支付或者使用充值卡余额支付但是余额小于选中商品金额还是要跳转到选择支付界面付款余下的金额
                                 */
                                intent = new Intent(S0_ConfirmOrderActivity.this, S3_SelectPaymentActivity.class);
                                intent.putExtra("tvRealPay", tvRealPay.getText());
                                if (isUseBalance) {
                                    intent.putExtra("tvAvailablePredeposit", tvAvailablePredeposit.getText());
                                } else {
                                    intent.putExtra("tvAvailablePredeposit", "0.00");
                                }
                                if (isUseRcBalance) {
                                    intent.putExtra("tvAvailableRcBalance", tvAvailableRcBalance.getText());
                                } else {
                                    intent.putExtra("tvAvailableRcBalance", "0.00");
                                }

                                String paySn = payOrder.datas.data.pay_sn;
                                String totalFee = payOrder.datas.data.total_fee;
                                String goodsName = payOrder.datas.data.goods_name;
                                String goodsDetail = payOrder.datas.data.goods_detail;

                                intent.putExtra("paySn", paySn);
                                intent.putExtra("totalFee", totalFee);
                                intent.putExtra("goodsName", goodsName);
                                intent.putExtra("goodsDetail", goodsDetail);
                                intent.putExtra("entrance", "1");

                                if (Double.parseDouble(totalFee) > 0 && paySn != null) {
                                    startActivity(intent);
                                    finish();
                                } else {
                                    showToastShort("下单异常，请重新操作");
                                    finish();
                                }
                            }
                        }
                    }
                } else {
                    overtime(payOrder.status.code, payOrder.status.msg);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToastShort("网络错误");
            }
        });

    }

    /**
     * 判断是否有登录密码
     */
    private void whetherHavePaypwd() {
        super.StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Get_member_base_info, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                S0_ConfirmOrderActivity.super.Dismiss();
                Gson gson = new Gson();
                if (responseInfo.result != null) {
                    userinformation = gson.fromJson(responseInfo.result, MyLockBox.class);
                    if (userinformation.status.code == 10000) {
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
                                    Intent intent = new Intent(S0_ConfirmOrderActivity.this, VerificationCodeActivity.class);
                                    intent.putExtra("Number", userinformation.datas.member_mobile);
                                    intent.putExtra("use", "2");
                                    intent.putExtra("paypwd", "0");
                                    startActivity(intent);
                                }
                            });
                        } else {
                            /**
                             *输入支付密码的对话框
                             */
                            entryPaypwdAndTest();
                        }
                    } else {
                        overtime(userinformation.status.code, userinformation.status.msg);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToastShort("网络错误");
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
                /**
                 * 用户输的入的支付密码
                 */
                paypwd = edPaypwd.getText().toString();
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
        super.StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("paypwd", paypwd);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Test_Paypwd, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                S0_ConfirmOrderActivity.super.Dismiss();
                if (responseInfo.result != null) {
                    Gson gson = new Gson();
                    testPaypwd = gson.fromJson(responseInfo.result, TestPaypwd.class);
                    if (testPaypwd.status.code == 10000) {
                        /**
                         * 支付密码正确跳转
                         */
                        entryPaypwdDialog.dismiss();
                        /**
                         * 提交订单
                         */
                        CommitOrder();
                    } else if (testPaypwd.status.code == 200103 || testPaypwd.status.code == 200104) {
                        showToastShort("登录超时或未登录");
                        getSharedPreferences("key", MODE_APPEND).edit().putString("key", "0").commit();
                        startActivity(new Intent(S0_ConfirmOrderActivity.this, A0_LoginActivity.class));
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
                                Intent intent = new Intent(S0_ConfirmOrderActivity.this, VerificationCodeActivity.class);
                                intent.putExtra("Number", userinformation.datas.member_mobile);
                                intent.putExtra("use", "2");
                                intent.putExtra("paypwd", "1");
                                startActivity(intent);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

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
        tvDownlinePay = (TextView) paymentWindowLayout.findViewById(R.id.tv_downline_pay);
        //不支持货到付款
        if (Offpay) {

            tvDownlinePay.setTextColor(Color.parseColor("#666666"));
            llDownlinePay.setClickable(false);
        }

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

                tvRealPay.setText("" + realPay + Double.valueOf(freight));
                tvFreight.setText("+ ¥ " + freight + ".00");
                tvTotalAmount.setText("" + bd);

            }
        });

        if (!Offpay) {

            tvDownlinePay.setTextColor(Color.parseColor("#333333"));
            llDownlinePay.setClickable(true);

            llDownlinePay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ivOnlinePay.setImageResource(R.mipmap.ic_order_blank);
                    ivDownlinePay.setImageResource(R.mipmap.ic_order_select);

                    paymentWindow.dismiss();
                    tvPayMethod.setText("货到付款");

                    isDonate = false;
                    isUseBalance = false;
                    isUseRcBalance = false;
                    ivDonate.setImageResource(R.mipmap.ic_push_off);
                    ivBalance.setImageResource(R.mipmap.ic_push_off);
                    ivAvailableRcBalance.setImageResource(R.mipmap.ic_push_off);
                    llUseMessage.setVisibility(View.VISIBLE);
                    tvDonate.setTextColor(Color.parseColor("#cccccc"));
                    tvRealPay.setText("0.00");
                    tvFreight.setText("+ ¥ 0.00");
                    tvTotalAmount.setText("0.00");


                }
            });
        }
    }


    /**
     * 请求地址列表
     */
    public void load_Default_Addresst() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("address_type", "0");
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Address_list, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Address_list address_list = gson.fromJson(responseInfo.result, Address_list.class);
                    if (address_list.status.code == 10000) {
                        if (address_list.datas == null) {
                            //地址为空显示要去设置收货地址
                            llUseMessage01.setVisibility(View.GONE);
                            tvNotAddress.setVisibility(View.VISIBLE);
                            //不支持货到付款
                            Offpay = true;

                        } else {
                            tvUseName.setText(address_list.datas.get(0).true_name);
                            tvUsePhone.setText(address_list.datas.get(0).mob_phone);
                            tvUseAddress.setText(address_list.datas.get(0).area_info + " " + address_list.datas.get(0).address);
                        }
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
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
