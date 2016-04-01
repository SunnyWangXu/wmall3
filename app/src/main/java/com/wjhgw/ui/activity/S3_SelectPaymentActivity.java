package com.wjhgw.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.pay.Alipay.payMethod;
import com.wjhgw.ui.dialog.UnderDialog;

import org.json.JSONException;
import org.json.JSONObject;

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

        api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1");
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
        balance = getIntent().getStringExtra("tvAvailablePredeposit");  //使用账号余额
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
                    // buy();
                    final UnderDialog underdevelopmentDialog = new UnderDialog(this, "功能正在开发中,敬请期待");
                    underdevelopmentDialog.show();
                    underdevelopmentDialog.tv_goto_setpaypwd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            underdevelopmentDialog.dismiss();
                        }
                    });
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
     * 微信支付测试数据
     */

    public void buy() {
        RequestQueue mRequestQueue;
        StringRequest stringRequest;
        mRequestQueue = Volley.newRequestQueue(this);
        Response.Listener<String> SuccessfulResponse = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    PayReq req = new PayReq();
                    req.appId = json.getString("appid");
                    req.partnerId = json.getString("partnerid");
                    req.prepayId = json.getString("prepayid");
                    req.nonceStr = json.getString("noncestr");
                    req.timeStamp = json.getString("timestamp");
                    req.packageValue = json.getString("package");
                    req.sign = json.getString("sign");
                    // req.extData = "app data"; // optional

                        /*List<NameValuePair> signParams = new LinkedList<>();
                        signParams.add(new BasicNameValuePair("appid", req.appId));
                        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
                        signParams.add(new BasicNameValuePair("package", req.packageValue));
                        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
                        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
                        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

                        req.sign = genAppSign(signParams);*/
                    showToastShort("正常调起支付");
                    api.sendReq(req);
                    // OnMessageResponse(Route, response, new JSONObject(new JSONObject(response).getString("status")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        stringRequest = new StringRequest(Request.Method.POST, "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android", SuccessfulResponse, FailureResponse);
        mRequestQueue.add(stringRequest);
    }

    /**
     * 请求错误回调
     */
    Response.ErrorListener FailureResponse = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // Toast.makeText(mContext, "网络错误！", Toast.LENGTH_SHORT).show();
        }
    };


 /*   private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append("16103e4Fd8906506991dbbED035632d1");

        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        //Log.e("orion", appSign);
        return appSign;
    }*/
}
