package com.wjhgw.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.wjhgw.business.bean.CadList_data;
import com.wjhgw.business.bean.CheckAddressSupport;
import com.wjhgw.business.bean.MyLockBox;
import com.wjhgw.business.bean.PayOrder;
import com.wjhgw.business.bean.SelectOrder;
import com.wjhgw.business.bean.SelectOrderDatas;
import com.wjhgw.business.bean.TestPaypwd;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.dialog.EntryPaypwdDialog;
import com.wjhgw.ui.dialog.MyDialog;
import com.wjhgw.ui.dialog.RestartInputAndFindPaypwdDialog;
import com.wjhgw.ui.dialog.SetPaypwdDialog;
import com.wjhgw.ui.view.listview.adapter.CabGiveLvAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 犒劳自己Activity
 */
public class J4_GiveMyselfActivity extends BaseActivity implements View.OnClickListener {
    private ListView lvGiveMyself;
    private LinearLayout llGiveMyselfHead;
    private List<CadList_data> datas;
    private List<CadList_data> datas1 = new ArrayList<CadList_data>();
    private int totalNum;
    private LinearLayout llMyselfMessage;
    private TextView tvMyselfNotAddress;
    private CabGiveLvAdapter cabGiveLvAdapter;
    private TextView tvMyselfTotal;
    private TextView tvToGiveMyself;
    private LinearLayout llGiveMyselfFoot;
    private String giveUseName;
    private String giveUsePhone;
    private String giveUseAddressInfo;
    private TextView giveMyselfName;
    private TextView giveMyselfPhone;
    private TextView giveMyselfAddress;
    private double totalPrice;
    private TextView giveMyselfTotal;
    private String giveUseAddressId;
    private String goods_str;
    private String vat_hash;
    private int freight;
    private String freight_hash;
    private String offpay_hash;
    private String offpay_hash_batch;
    private MyLockBox userinformation;
    private boolean isHavePaypwd;
    private SetPaypwdDialog setPaypwdDialog;
    private EntryPaypwdDialog entryPaypwdDialog;
    private String paypwd;
    private TestPaypwd testPaypwd;
    private RestartInputAndFindPaypwdDialog restartInputAndFindPaypwdDialog;
    private LinearLayout llUseBalance;
    private LinearLayout llUseRcBalance;
    //标记使用余额
    private int MAKEBALANCE = 1;
    //标记使用充值卡余额
    private int MAKEAVAILABLERCBALANCE = 1;
    private ImageView ivUseBalance;
    private ImageView ivUseRcBalance;
    private TextView tvUseBalance;
    private TextView tvUseRcBalance;
    private TextView tvUseBalancePrice;
    private TextView tvUseRcBalancePrice;
    private boolean truePaypwd;
    private boolean isUseBalance = false;
    private boolean isUseRcBalance = false;
    private TextView tvFreight;
    private MyDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_myself);

        /**
         * 请求地址列表
         */
        load_Default_Addresst();


        cabGiveLvAdapter = new CabGiveLvAdapter(this, datas1);
        lvGiveMyself.setAdapter(cabGiveLvAdapter);


    }

    @Override
    public void onInit() {
        setUp();
        setTitle("犒劳自己");

        lvGiveMyself = (ListView) findViewById(R.id.lv_give_myself);
        tvMyselfTotal = (TextView) findViewById(R.id.tv_myself_total);

        llGiveMyselfHead = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_give_myself_head, null);
        llGiveMyselfFoot = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_give_myself_foot, null);

        lvGiveMyself.addHeaderView(llGiveMyselfHead);
        lvGiveMyself.addFooterView(llGiveMyselfFoot);


        String jsonStr = getIntent().getStringExtra("list");
        /**
         * 解析筛选选中赠送的数据
         */
        parseGiveData(jsonStr);

    }

    /**
     * 解析选中赠送的数据
     */
    private void parseGiveData(String jsonStr) {
        Type type = new TypeToken<ArrayList<CadList_data>>() {
        }.getType();
        datas = new Gson().fromJson(jsonStr, type);

        for (int i = 0; i < datas.size(); i++) {

            if (!datas.get(i).selected.equals("0")) {
                datas1.add(datas.get(i));
                totalNum += datas.get(i).num;

                totalPrice += (Double.valueOf(datas.get(i).goods_price) * datas.get(i).num);
            }
        }

    }


    @Override
    public void onFindViews() {

        llMyselfMessage = (LinearLayout) llGiveMyselfHead.findViewById(R.id.ll_myself_message);

        tvMyselfNotAddress = (TextView) llGiveMyselfHead.findViewById(R.id.tv_myself_not_address);
        giveMyselfName = (TextView) llGiveMyselfHead.findViewById(R.id.tv_givemyself_name);
        giveMyselfPhone = (TextView) llGiveMyselfHead.findViewById(R.id.tv_givemyself_phone);
        giveMyselfAddress = (TextView) llGiveMyselfHead.findViewById(R.id.tv_givemyself_address);

        giveMyselfTotal = (TextView) llGiveMyselfFoot.findViewById(R.id.tv_givemyself_total);
        llUseBalance = (LinearLayout) llGiveMyselfFoot.findViewById(R.id.ll_use_balance);
        ivUseBalance = (ImageView) llGiveMyselfFoot.findViewById(R.id.iv_balance);
        tvUseBalance = (TextView) llGiveMyselfFoot.findViewById(R.id.tv_balance);
        tvUseBalancePrice = (TextView) llGiveMyselfFoot.findViewById(R.id.tv_balance_price);
        llUseRcBalance = (LinearLayout) llGiveMyselfFoot.findViewById(R.id.ll_rc_balance);
        ivUseRcBalance = (ImageView) llGiveMyselfFoot.findViewById(R.id.iv_rc_balance);
        tvUseRcBalance = (TextView) llGiveMyselfFoot.findViewById(R.id.tv_use_rc_balance);
        tvUseRcBalancePrice = (TextView) llGiveMyselfFoot.findViewById(R.id.tv_rc_balance_price);

        tvFreight = (TextView) llGiveMyselfFoot.findViewById(R.id.tv_freight);

        tvToGiveMyself = (TextView) findViewById(R.id.tv_to_give_myself);

    }

    @Override
    public void onInitViewData() {

        tvMyselfTotal.setText(totalNum + "件");
        giveMyselfTotal.setText("¥" + totalPrice);

        /**
         * 请求用户信息设置余额和充值卡余额
         */
        load_User_information();


        StringBuffer cart_id = new StringBuffer();

        for (int i = 0; i < datas1.size(); i++) {
            cart_id.append(datas1.get(i).goods_id + "|" + datas1.get(i).num + ",");
        }

        goods_str = cart_id.toString().substring(0, cart_id.toString().length() - 1);
        /**
         * 走第一步购买流程
         */
        loadGiveMyself1();

    }

    @Override
    public void onBindListener() {

        llMyselfMessage.setOnClickListener(this);
        tvMyselfNotAddress.setOnClickListener(this);
        tvToGiveMyself.setOnClickListener(this);
        llUseBalance.setOnClickListener(this);
        llUseRcBalance.setOnClickListener(this);

    }

    /**
     * 请求用户信息
     */
    private void load_User_information() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Get_member_base_info, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo.result != null) {
                    userinformation = gson.fromJson(responseInfo.result, MyLockBox.class);

                    if (userinformation.status.code == 10000) {
                        tvUseBalancePrice.setText(userinformation.datas.available_predeposit);   //余额
                        tvUseRcBalancePrice.setText(userinformation.datas.available_rc_balance);   //可用充值卡余额
                    } else {
                        overtime(userinformation.status.code, userinformation.status.msg);
                    }
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
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
                            llMyselfMessage.setVisibility(View.GONE);
                            tvMyselfNotAddress.setVisibility(View.VISIBLE);
                        } else {
                            giveMyselfName.setText(address_list.datas.get(0).true_name);
                            giveMyselfPhone.setText(address_list.datas.get(0).mob_phone);
                            giveMyselfAddress.setText(address_list.datas.get(0).area_info + " " + address_list.datas.get(0).address);

                            giveUseAddressId = address_list.datas.get(0).address_id;
                        }

                    } else {
                        overtime(address_list.status.code, address_list.status.msg);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ll_myself_message:

                intent.setClass(this, S1_OrderAddressActivity.class);
                startActivityForResult(intent, 55555);

                break;

            case R.id.tv_myself_not_address:

                intent.setClass(this, S1_OrderAddressActivity.class);
                startActivityForResult(intent, 55555);

                break;

            case R.id.ll_use_balance:

                MAKEBALANCE++;
                if (MAKEBALANCE % 2 == 0) {
                    ivUseBalance.setImageResource(R.mipmap.ic_push_on);
                    tvUseBalance.setTextColor(Color.parseColor("#333333"));

                    isUseBalance = true;
                } else {
                    ivUseBalance.setImageResource(R.mipmap.ic_push_off);
                    tvUseBalance.setTextColor(Color.parseColor("#999999"));

                    isUseBalance = false;
                }

                break;

            case R.id.ll_rc_balance:
                MAKEAVAILABLERCBALANCE++;
                if (MAKEAVAILABLERCBALANCE % 2 == 0) {
                    ivUseRcBalance.setImageResource(R.mipmap.ic_push_on);
                    tvUseRcBalance.setTextColor(Color.parseColor("#333333"));

                    isUseRcBalance = true;
                } else {
                    ivUseRcBalance.setImageResource(R.mipmap.ic_push_off);
                    tvUseRcBalance.setTextColor(Color.parseColor("#999999"));

                    isUseRcBalance = false;
                }

                break;

            case R.id.tv_to_give_myself:
                if (isUseBalance || isUseRcBalance) {
                    /**
                     * 开启余额或充值卡余额支付判断是否有登录密码,没有就设置，有就去输入并走购买第二步下单
                     */
                    whetherHavePaypwd();
                }

                if (!isUseBalance && !isUseRcBalance) {
                    truePaypwd = true;
                    /**
                     * 检查地址是否支持货到付款，并走购买第二步流程
                     */
                    checkAddressSupport();
                }

                break;

            default:
                break;
        }
    }


    /**
     * 走第一步购买流程
     */
    private void loadGiveMyself1() {
        super.StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("cart_id", goods_str);
        params.addBodyParameter("ifcart", "2");

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Buy_step1, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                J4_GiveMyselfActivity.super.Dismiss();
                Gson gson = new Gson();
                if (responseInfo.result != null) {

                    SelectOrder selectOrder = gson.fromJson(responseInfo.result, SelectOrder.class);
                    if (selectOrder.status.code == 10000) {

                        SelectOrderDatas selectOrderDatas = selectOrder.datas;

                        vat_hash = selectOrder.datas.vat_hash;
                        freight = selectOrderDatas.store_cart_list.freight;
                        freight_hash = selectOrderDatas.freight_hash;

                        if (freight == 0) {
                            llUseBalance.setVisibility(View.GONE);
                            llUseRcBalance.setVisibility(View.GONE);
                        } else {
                            tvFreight.setText(freight + "");
                        }

                    } else {
                        overtime(selectOrder.status.code, selectOrder.status.msg);
                    }
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });


    }

    /**
     * 购买第二步
     */
    private void loadGiveMyself2() {

        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());

        params.addBodyParameter("cabinet_operation", "2");
        params.addBodyParameter("cart_id", goods_str);
        if (giveUseAddressId != null) {
            params.addBodyParameter("address_id", giveUseAddressId);
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
        params.addBodyParameter("pay_name", "online");

        if (isUseBalance) {
            params.addBodyParameter("pd_pay", "1");
        } else {
            params.addBodyParameter("pd_pay", "0");
        }
        if (isUseRcBalance) {
            params.addBodyParameter("rcb_pay", "1");
        } else {
            params.addBodyParameter("rcb_pay", "0");
        }
        if (paypwd != null) {
            params.addBodyParameter("password", paypwd);
        }

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Buy_step2, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                PayOrder payOrder = gson.fromJson(responseInfo.result, PayOrder.class);
                if (payOrder.status.code == 10000) {

                    double balance;
                    double rcBalance;
                    if (isUseBalance) {
                        balance = Double.valueOf(tvUseBalancePrice.getText().toString());
                    } else {
                        balance = 0.00;
                    }
                    if (isUseRcBalance) {
                        rcBalance = Double.valueOf(tvUseBalancePrice.getText().toString());
                    } else {
                        rcBalance = 0.00;
                    }

                    double yu = (rcBalance + balance) - Double.valueOf(tvFreight.getText().toString());
                    /**
                     * 开启使用余额或充值卡余额大于要使用的邮费时支付成功后弹出对话框
                     */
                    if (isUseBalance || isUseRcBalance && yu > 0) {

                        mDialog = new MyDialog(J4_GiveMyselfActivity.this, "订单支付成功，会尽快为您处理！");
                        mDialog.positive.setText("继续送礼");
                        mDialog.negative.setText("查看订单");
                        mDialog.show();
                        mDialog.positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        });
                        mDialog.negative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.dismiss();
                                Intent intent = new Intent(J4_GiveMyselfActivity.this, D0_OrderActivity.class);
                                intent.putExtra("order_state", "");
                                intent.putExtra("name", "所有订单");
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        /**
                         * 不使用余额或者余额充值卡余额金额不够时，跳转到选择支付方式使用第三方支付
                         */
                        Intent intent = new Intent(J4_GiveMyselfActivity.this, S3_SelectPaymentActivity.class);
                        intent.putExtra("giveType","giveMyself");
                        intent.putExtra("tvRealPay", freight + "");
                        if (isUseBalance) {
                            intent.putExtra("tvAvailablePredeposit", tvUseBalancePrice.getText());
                        } else {
                            intent.putExtra("tvAvailablePredeposit", "0.00");
                        }
                        if (isUseRcBalance) {
                            intent.putExtra("tvAvailableRcBalance", tvUseRcBalancePrice.getText());
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
                        intent.putExtra("entrance", "3");

                        if (Double.valueOf(totalFee) > 0 && paySn != null) {
                            startActivity(intent);
                            finish();
                        } else {
                            showToastShort("下单成功");
                            finish();
                        }
                    }

                } else {
                    showToastShort(payOrder.status.msg);
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }


    /**
     * 检查地址是否支持货到付款，并走购买第二步流程
     */
    private void checkAddressSupport() {
        super.StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("freight_hash", freight_hash);
        params.addBodyParameter("address_id", giveUseAddressId);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Check_Address_Support, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                J4_GiveMyselfActivity.super.Dismiss();
                Gson gson = new Gson();
                CheckAddressSupport checkAddressSupport = gson.fromJson(responseInfo.result, CheckAddressSupport.class);

                if (checkAddressSupport.status.code == 10000) {
                    if (checkAddressSupport.datas != null) {
                        offpay_hash = checkAddressSupport.datas.offpay_hash;
                        offpay_hash_batch = checkAddressSupport.datas.offpay_hash_batch;

                        /**
                         *购买第二步
                         */
                        loadGiveMyself2();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            llMyselfMessage.setVisibility(View.GONE);
            tvMyselfNotAddress.setVisibility(View.VISIBLE);
        }
        if (data != null) {
            tvMyselfNotAddress.setVisibility(View.GONE);
            llMyselfMessage.setVisibility(View.VISIBLE);

            giveUseName = data.getStringExtra("tureName");
            giveUsePhone = data.getStringExtra("phone");
            giveUseAddressInfo = data.getStringExtra("addressInfo");
            giveUseAddressId = data.getStringExtra("addressId");
            if (giveUseName != null && giveUsePhone != null && giveUseAddressInfo != null) {
                giveMyselfName.setText(giveUseName);
                giveMyselfPhone.setText(giveUsePhone);
                giveMyselfAddress.setText(giveUseAddressInfo);
            }

        }

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
                J4_GiveMyselfActivity.super.Dismiss();
                Gson gson = new Gson();
                if (responseInfo.result != null) {
                    userinformation = gson.fromJson(responseInfo.result, MyLockBox.class);
                    if (userinformation.status.code == 10000) {
                        isHavePaypwd = userinformation.datas.paypwd.equals("1");
                        if (!isHavePaypwd) {
                            /**
                             *建议设置支付密码的对话框
                             */
                            setPaypwdDialog = new SetPaypwdDialog(J4_GiveMyselfActivity.this, "设置支付密码", "为了提高账号安全性，建议设置支付密码");
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
                                    Intent intent = new Intent(J4_GiveMyselfActivity.this, VerificationCodeActivity.class);
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
        entryPaypwdDialog = new EntryPaypwdDialog(J4_GiveMyselfActivity.this);
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
                    Toast.makeText(J4_GiveMyselfActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
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
    private void testPaypwd(String paypwd) {
        super.StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("paypwd", paypwd);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Test_Paypwd, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                J4_GiveMyselfActivity.super.Dismiss();
                if (responseInfo.result != null) {
                    Gson gson = new Gson();
                    testPaypwd = gson.fromJson(responseInfo.result, TestPaypwd.class);
                    if (testPaypwd.status.code == 10000) {
                        /**
                         * 支付密码正确跳转
                         */
                        entryPaypwdDialog.dismiss();

                        truePaypwd = true;
                        /**
                         * 检查地址是否支持货到付款，并走购买第二步流程
                         */
                        checkAddressSupport();

                    } else if (testPaypwd.status.code == 200103 || testPaypwd.status.code == 200104) {
                        showToastShort("登录超时或未登录");
                        getSharedPreferences("key", MODE_APPEND).edit().putString("key", "0").commit();
                        startActivity(new Intent(J4_GiveMyselfActivity.this, A0_LoginActivity.class));
                    } else {
                        /**
                         * 支付密码不正确， 弹出重新输入或者找回支付密码
                         */
                        restartInputAndFindPaypwdDialog = new RestartInputAndFindPaypwdDialog(J4_GiveMyselfActivity.this);
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
                                Intent intent = new Intent(J4_GiveMyselfActivity.this, VerificationCodeActivity.class);
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
                Toast.makeText(J4_GiveMyselfActivity.this, testPaypwd.status.msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
