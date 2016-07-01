package com.wjhgw.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.bean.WXPay;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.pay.Alipay.payMethod;

import java.security.MessageDigest;

/**
 * 选择支付Activity
 */
public class S3_SelectPaymentActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llBalancePay;
    private LinearLayout llWeixinPay;
    private LinearLayout llAlipayPay;
    private ImageView ivWeixinPay;
    private ImageView ivAlipayPay;
    private String realPay;
    private String balance;
    private TextView tvPayOrderPrice;
    private TextView tvPayBalance;
    private TextView tvEndPay;
    IWXAPI api;
    StringBuffer sb;
    private Button btn_confirm_pay;
    private boolean isWeixin = true;
    private String rcBalance;
    private TextView tvPayRcBalance;
    private LinearLayout llRcBalancePay;
    private String paySn;
    private String totalFee;
    private String goodsName;
    private String goodsDetail;
    private String entrance;
    private String giveType;
    private TextView tvPayOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment);

        api = WXAPIFactory.createWXAPI(this, "wx99a6bd9b7bdbf645", false);
        api.registerApp("wx99a6bd9b7bdbf645");
        sb = new StringBuffer();

        paySn = getIntent().getStringExtra("paySn");
        totalFee = getIntent().getStringExtra("totalFee");
        goodsName = getIntent().getStringExtra("goodsName");
        goodsDetail = getIntent().getStringExtra("goodsDetail");
        entrance = getIntent().getStringExtra("entrance");

        //showToastShort(paySn + "\\\\" + totalFee + "\\\\" + goodsName + "\\\\" + goodsDetail);
        tvEndPay.setText(totalFee);
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("选择支付");
    }

    @Override
    public void onFindViews() {
        tvPayOrder = (TextView) findViewById(R.id.tv_orderprice);
        tvPayOrderPrice = (TextView) findViewById(R.id.tv_pay_order_price);
        tvPayBalance = (TextView) findViewById(R.id.tv_pay_balance);
        tvPayRcBalance = (TextView) findViewById(R.id.tv_pay_rc_balance);
        tvEndPay = (TextView) findViewById(R.id.tv_end_pay);

        llBalancePay = (LinearLayout) findViewById(R.id.ll_balance_pay);
        llRcBalancePay = (LinearLayout) findViewById(R.id.ll_rc_balance_pay);
        btn_confirm_pay = (Button) findViewById(R.id.btn_confirm_pay);

        llWeixinPay = (LinearLayout) findViewById(R.id.ll_weixin_pay);
        ivWeixinPay = (ImageView) findViewById(R.id.iv_weixin_pay);
        llAlipayPay = (LinearLayout) findViewById(R.id.ll_alipay_pay);
        ivAlipayPay = (ImageView) findViewById(R.id.iv_alipay_pay);

    }

    @Override
    public void onInitViewData() {

        giveType = getIntent().getStringExtra("giveType");      //从自提过来的
        realPay = getIntent().getStringExtra("tvRealPay");      //订单金额或者是自提的邮费
        balance = getIntent().getStringExtra("tvAvailablePredeposit");//使用账号余额
        rcBalance = getIntent().getStringExtra("tvAvailableRcBalance"); //充值卡余额

        if (giveType != null) {
            tvPayOrder.setText("运费");
            tvPayOrderPrice.setText(Double.valueOf(realPay).toString());
        } else {
//            tvPayOrder.setText("订单金额");
            tvPayOrderPrice.setText(realPay);
        }


        if (balance.equals("0.00")) {
            llBalancePay.setVisibility(View.GONE);
        } else {

            tvPayBalance.setText(balance);
        }
        if (rcBalance.equals("0.00")) {
            llRcBalancePay.setVisibility(View.GONE);
        } else {

            tvPayRcBalance.setText(rcBalance);
        }

//        double end = Double.valueOf(realPay) - Double.valueOf(balance) - Double.valueOf(rcBalance);


    }

    @Override
    public void onBindListener() {
        llWeixinPay.setOnClickListener(this);
        llAlipayPay.setOnClickListener(this);
        btn_confirm_pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ll_weixin_pay:
                ivAlipayPay.setImageResource(R.mipmap.ic_order_blank);
                ivWeixinPay.setImageResource(R.mipmap.ic_order_select);
                isWeixin = true;
                break;

            case R.id.ll_alipay_pay:
                ivWeixinPay.setImageResource(R.mipmap.ic_order_blank);
                ivAlipayPay.setImageResource(R.mipmap.ic_order_select);
                isWeixin = false;
                break;

            case R.id.btn_confirm_pay:
                if (isWeixin) {
                    wx_pay();
                } else {
//                    payMethod pay = new payMethod(this, "订单号", "测试的商品", "测试的商品详情", "0.01");
                    payMethod pay = new payMethod(this, paySn, goodsName, goodsDetail, totalFee, entrance);
                    pay.pay();
                }
                break;

            default:
                break;
        }
    }

    /**
     * 发起微信支付
     */
    private void wx_pay() {

        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("pay_sn", paySn);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Wx_pay, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    WXPay WXPay_data = gson.fromJson(responseInfo.result, WXPay.class);
                    if (WXPay_data.status.code == 10000) {
                        String api_key;
                        PayReq req = new PayReq();
                        req.appId = WXPay_data.datas.appid;
                        req.partnerId = WXPay_data.datas.partnerid;
                        req.prepayId = WXPay_data.datas.prepayid;
                        req.nonceStr = WXPay_data.datas.noncestr;
                        req.timeStamp = WXPay_data.datas.timestamp + "";
                        req.packageValue = "Sign=WXPay";
                        api_key = WXPay_data.datas.app_key;
                        String content = "appid=" + req.appId + "&" + "noncestr=" + req.nonceStr + "&" +
                                "package=" + req.packageValue + "&" + "partnerid=" + req.partnerId + "&" +
                                "prepayid=" + req.prepayId + "&" + "timestamp=" + req.timeStamp + "&";
                        req.sign = genAppSign(content, api_key);
                        api.sendReq(req);
                        finish();
                    } else {
                        overtime(WXPay_data.status.code, WXPay_data.status.msg);
                    }

                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }

    /**
     * 二次签名
     *
     * @param params
     * @param api_key
     * @return
     */
    private String genAppSign(String params, String api_key) {
        StringBuilder sb = new StringBuilder(params);
        sb.append("key=");
        sb.append(api_key);
        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = getMessageDigest(sb.toString().getBytes());
        return appSign;
    }

    /**
     * MD5签名
     *
     * @param buffer
     * @return
     */
    public String getMessageDigest(byte[] buffer) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

}
