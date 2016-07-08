package com.wjhgw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.wjhgw.business.bean.MyLockBox;
import com.wjhgw.business.bean.Order_goods_list;
import com.wjhgw.business.bean.PayOrder;
import com.wjhgw.business.bean.SelectOrder;
import com.wjhgw.business.bean.TestPaypwd;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.dialog.EntryPaypwdDialog;
import com.wjhgw.ui.dialog.RestartInputAndFindPaypwdDialog;
import com.wjhgw.ui.dialog.SetPaypwdDialog;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 支付赠送订单Activity
 */
public class PayGiveOrderActivity extends BaseActivity implements View.OnClickListener {
    private String byStep1Result;
    private String available_predeposit;
    private String available_rc_balance;
    private ImageView ivImg;
    private TextView tvName;
    private TextView tvPrice;
    private TextView tvNum;
    private TextView tvBl;
    private TextView tvRcBl;
    private String vat_hash;
    private String cart_id;
    private String store_goods_total;
    private TextView tvGoodsTotal;
    private TextView tvGiveTotal;
    private LinearLayout llUseBl;
    private LinearLayout llUseRcBl;
    private int MAKEBL = 1;
    private int MAKERCBL = 1;
    private ImageView ivBl;
    private ImageView ivRcBl;
    private boolean isBalance = false;
    private boolean isRcBalance = false;
    private TextView tvPayGiveNet;
    private MyLockBox userinformation;
    private boolean isHavePaypwd;
    private SetPaypwdDialog setPaypwdDialog;
    private EntryPaypwdDialog entryPaypwdDialog;
    private String paypwd;
    private TestPaypwd testPaypwd;
    private RestartInputAndFindPaypwdDialog restartInputAndFindPaypwdDialog;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_giveorder);

    }

    @Override
    public void onInit() {
        setUp();
        setTitle("支付赠送订单");

    }

    @Override
    public void onFindViews() {
        tvBl = (TextView) findViewById(R.id.tv_bl);
        tvRcBl = (TextView) findViewById(R.id.tv_rc_bl);
        tvGoodsTotal = (TextView) findViewById(R.id.tv_goods_total);
        tvGiveTotal = (TextView) findViewById(R.id.tv_give_total);


        llUseBl = (LinearLayout) findViewById(R.id.ll_use_bl);
        llUseRcBl = (LinearLayout) findViewById(R.id.ll_use_rc_bl);
        ivBl = (ImageView) findViewById(R.id.iv_bl);
        ivRcBl = (ImageView) findViewById(R.id.iv_rc_bl);

        tvPayGiveNet = (TextView) findViewById(R.id.tv_pay_give_next);
    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {

        llUseBl.setOnClickListener(this);
        llUseRcBl.setOnClickListener(this);
        tvPayGiveNet.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        cart_id = getIntent().getStringExtra("cart_id");
        byStep1Result = getIntent().getStringExtra("by_step1_result");

        if (byStep1Result != null) {
            /**
             * 解析购买第一步的数据
             */
            parseByStep1Result(byStep1Result);
        }

    }

    /**
     * 解析购买第一步的数据并设置适配器
     */
    private void parseByStep1Result(String byStep1Result) {

        Gson gson = new Gson();
        SelectOrder selectOrder = gson.fromJson(byStep1Result, SelectOrder.class);


        store_goods_total = selectOrder.datas.store_cart_list.store_goods_total;
        vat_hash = selectOrder.datas.vat_hash;
        available_predeposit = selectOrder.datas.available_predeposit;
        available_rc_balance = selectOrder.datas.available_rc_balance;

        tvGoodsTotal.setText("¥" + store_goods_total);
        tvGiveTotal.setText("订单金额：¥" + store_goods_total);
        if (available_predeposit != null) {
            tvBl.setText(available_predeposit);
        }
        if (available_rc_balance != null) {
            tvRcBl.setText(available_rc_balance);
        }

        ArrayList<Order_goods_list> goods_list = selectOrder.datas.store_cart_list.goods_list;
        ivImg = (ImageView) findViewById(R.id.iv_give_goods);
        tvName = (TextView) findViewById(R.id.tv_give_goodsname);
        tvPrice = (TextView) findViewById(R.id.tv_give_goodsprice);
        tvNum = (TextView) findViewById(R.id.tv_give_goodsnum);

        APP.getApp().getImageLoader().displayImage(goods_list.get(0).goods_image_url, ivImg);
        tvName.setText(goods_list.get(0).goods_name);
        tvPrice.setText("¥" + goods_list.get(0).goods_price);
        tvNum.setText("x" + goods_list.get(0).goods_num);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_use_bl:
                if (available_predeposit != null) {
                    MAKEBL++;
                    if (MAKEBL % 2 == 0) {
                        ivBl.setImageResource(R.mipmap.ic_push_on);
                        isBalance = true;
                    } else {
                        ivBl.setImageResource(R.mipmap.ic_push_off);
                        isBalance = false;
                    }
                }

                break;

            case R.id.ll_use_rc_bl:
                if (available_rc_balance != null) {
                    MAKERCBL++;
                    if (MAKERCBL % 2 == 0) {
                        ivRcBl.setImageResource(R.mipmap.ic_push_on);
                        isRcBalance = true;
                    } else {
                        ivRcBl.setImageResource(R.mipmap.ic_push_off);
                        isRcBalance = false;
                    }
                }

                break;

            case R.id.tv_pay_give_next:

                if (isBalance || isRcBalance) {
                    /**
                     * 判断是否有登录密码,没有就设置，有就去输入下单
                     */
                    whetherHavePaypwd();
                } else {
                    /**
                     *不使用余额和充值卡不需要输入密码，直接提交订单走购买第二步
                     */
                    nextCommitOrder();

                }
                break;
            default:
                break;
        }
    }

    /**
     * 验证支付密码之后下一步提交订单走购买第二步
     */
    private void nextCommitOrder() {

        StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("cabinet_operation", "1");
        params.addBodyParameter("send_other", "1");
        if (cart_id != null) {
            params.addBodyParameter("cart_id", cart_id);
        }
        if (vat_hash != null) {
            params.addBodyParameter("vat_hash", vat_hash);
        }

        params.addBodyParameter("pay_name", "online");

        if (isBalance) {
            params.addBodyParameter("pd_pay", "1");
            params.addBodyParameter("password", paypwd);

        } else {
            params.addBodyParameter("pd_pay", "0");
        }
        if (isRcBalance) {
            params.addBodyParameter("rcb_pay", "1");
            params.addBodyParameter("password", paypwd);

        } else {
            params.addBodyParameter("rcb_pay", "0");
        }

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Buy_step2, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dismiss();
                Gson gson = new Gson();
                PayOrder payOrder = gson.fromJson(responseInfo.result, PayOrder.class);

                if (payOrder.status.code == 10000) {

                    /**
                     * （余额+上充值卡余额）- 需要使用的金额
                     */
                    double balance;
                    double rcBalance;

                    if (isBalance) {
                        balance = Double.valueOf(available_predeposit);

                    } else {
                        balance = 0.00;
                    }
                    if (isRcBalance) {
                        rcBalance = Double.valueOf(available_rc_balance);
                    } else {
                        rcBalance = 0.00;
                    }
                    double yu = (rcBalance + balance) - Double.valueOf(store_goods_total);

                    if ((isBalance || isRcBalance) && yu > 0) {
                        /**
                         * 选择了余额支付或者使用充值卡余额支付并且余额大于选中商品金额直接购买成功跳转赠送
                         */
                        showToastShort("余额支付成功");
                        Intent intent = new Intent(PayGiveOrderActivity.this, J0_SelectGiveObjectActivity.class);
                        String paySn = payOrder.datas.data.pay_sn;

                        intent.putExtra("paySn", paySn);
                        intent.putExtra("entrance", "4");
                        startActivity(intent);
                        finish();
                    } else {
                        if (payOrder.datas.state = true && payOrder.datas.data.type.equals("order")) {

                            /**
                             * 选择了余额支付或者使用充值卡余额支付但是余额小于选中商品金额还是要跳转到选择支付界面付款余下的金额
                             */
                            intent = new Intent(PayGiveOrderActivity.this, S3_SelectPaymentActivity.class);
                            intent.putExtra("tvRealPay", store_goods_total);
                            if (isBalance) {
                                intent.putExtra("tvAvailablePredeposit", tvBl.getText());
                            } else {
                                intent.putExtra("tvAvailablePredeposit", "0.00");
                            }
                            if (isRcBalance) {
                                intent.putExtra("tvAvailableRcBalance", tvRcBl.getText());
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
                            intent.putExtra("entrance", "4");

                            if (Double.parseDouble(totalFee) > 0 && paySn != null) {
                                startActivity(intent);
                                getSharedPreferences("Gifts", MODE_PRIVATE).edit().putString("Gifts", "1").commit();
                                getSharedPreferences("paySn", MODE_PRIVATE).edit().putString("paySn", paySn).commit();
                                finish();
                            } else {
                                showToastShort("下单异常，请重新操作");
                                finish();
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
        StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Get_member_base_info, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dismiss();
                Gson gson = new Gson();
                if (responseInfo.result != null) {
                    userinformation = gson.fromJson(responseInfo.result, MyLockBox.class);
                    if (userinformation.status.code == 10000) {
                        isHavePaypwd = userinformation.datas.paypwd.equals("1");
                        if (!isHavePaypwd) {
                            /**
                             *建议设置支付密码的对话框
                             */
                            setPaypwdDialog = new SetPaypwdDialog(PayGiveOrderActivity.this, "设置支付密码", "为了提高账号安全性，建议设置支付密码");
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
                                    Intent intent = new Intent(PayGiveOrderActivity.this, VerificationCodeActivity.class);
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
        entryPaypwdDialog = new EntryPaypwdDialog(PayGiveOrderActivity.this);
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
                    Toast.makeText(PayGiveOrderActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
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
        StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("paypwd", paypwd);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Test_Paypwd, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dismiss();
                if (responseInfo.result != null) {
                    Gson gson = new Gson();
                    testPaypwd = gson.fromJson(responseInfo.result, TestPaypwd.class);
                    if (testPaypwd.status.code == 10000) {
                        /**
                         * 支付密码正确跳转
                         */
                        entryPaypwdDialog.dismiss();
                        /**
                         *  验证支付密码之后下一步提交订单
                         */
                        nextCommitOrder();
                    } else if (testPaypwd.status.code == 200103 || testPaypwd.status.code == 200104) {
                        showToastShort("登录超时或未登录");
                        getSharedPreferences("key", MODE_APPEND).edit().putString("key", "0").commit();
                        startActivity(new Intent(PayGiveOrderActivity.this, A0_LoginActivity.class));
                    } else {
                        /**
                         * 支付密码不正确， 弹出重新输入或者找回支付密码
                         */
                        restartInputAndFindPaypwdDialog = new RestartInputAndFindPaypwdDialog(PayGiveOrderActivity.this);
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
                                Intent intent = new Intent(PayGiveOrderActivity.this, VerificationCodeActivity.class);
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
        return false;
    }

}