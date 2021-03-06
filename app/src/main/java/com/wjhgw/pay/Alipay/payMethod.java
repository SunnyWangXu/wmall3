package com.wjhgw.pay.Alipay;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.wjhgw.ui.activity.D0_OrderActivity;
import com.wjhgw.ui.activity.J0_SelectGiveObjectActivity;
import com.wjhgw.ui.dialog.MyDialog;
import com.wjhgw.ui.dialog.UnderDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * 支付宝调用SDK支付
 */
public class payMethod {
    //私钥
    private String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKKo6kFgdBEGg+IrzU2Jts9740JpSUZCWwK4A6Zhz9sdm9/McN7S6H9VApIO+9xouaIcgYKHSBHPA7mryAgSWHVpUYOz+WFwbOMim55i/5KoLeR80RNwtyR7EHREZGeIZNKIpXV94JT7kN95d8xcARG28n8Za9RSe8F+ptxIK8gLAgMBAAECgYAlBA+bJRoErdjhZVppJMfHW8gR2ufj50HS0pFyEZw9nvVT7rBY5OIn5+6X0nUaaXKlI5uNWr/Z3aRZYnA49gPPB0jU5jgvRJtUOqZ82AVC7aWsZILcu/+vVaCbIFTUZ3sDZ4R0sWN3aKQzHDF9B4mOFAx+fOOIvae6NV+NT6gFgQJBANh5CevdEyuidFA3C/5HlAsn9LQzXnZbeytq2NB7rwQW72MVipXtySwviZcMtZiWFGfKBbBf+eVQP4jv7F6YG+ECQQDAXGdCep6Wc7NYXwTgM9dtVAl+aDL9PA4DZpBqrM1QyOCHTXNnyowo/Rh+0PW4WoSCGB+3Wl9mrGt0ZaGwBEFrAkBa+RMYUoI59j3XcCrUJBStU49dI7FxICT1LCQOCcyVdWYJSAZD1/iRXzD9j9PJWlkc/SPC6bLFkiIBVIMLhfVBAkEAolrbRH7d7zpEOugn41ueDcaKJMipkJbpdPFSHtcui2qgJ1K1fvqzhbzoudCANFMh1/OogUajkrMgFqPY7gkJdQJAX7YNU86YOkFvpI/CNwUP8C3hwFOIA0OeeZEVxWDIhvxHu1pHz8hwUnqyikBXLjTyPqRqjPBS+p7XHzadRhTLiA==";
    // 商户PID
    public static final String PARTNER = "2088311968348704";
    // 商户收款账号
    public static final String SELLER = "371037888@qq.com";
    private String order_amount;    // 商品金额
    private String desc;    //商品详情
    private String subject; // 商品名称
    private String order_sn;    // 商户网站唯一订单号
    private String entrance;    //用于判断当前界面
    private Activity mContext;
    private MyDialog mDialog;

    public payMethod(Activity context, String order_sn, String subject, String desc, String order_amount, String entrance) {
        this.order_amount = order_amount;
        this.desc = desc;
        this.subject = subject;
        this.order_sn = order_sn;
        this.mContext = context;
        this.entrance = entrance;
    }

    /**
     * 支付宝
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay() {
        /*if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
                || TextUtils.isEmpty(SELLER)) {
			new AlertDialog.Builder(this)
					.setTitle("警告")
					.setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									//
									finish();
								}
							}).show();
			return;
		}*/
        // 订单
        String orderInfo = getOrderInfo();

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mContext);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo() {

        // 签约合作者身份I
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + order_sn + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + desc + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + order_amount + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://dev.wjhgw.com/mobile/api/payment/alipay/sdk/notify_url.php" + "\"";
        //orderInfo += "&notify_url=" + "\"" + "www.wjhgw.com/ECMobile/payment/alipay/sdk/notify_url.php" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		/*// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
*/        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    String message = null;
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        message = "订单支付成功，会尽快为您处理";
                        if (entrance.equals("1")) {
                            mDialog = new MyDialog(mContext, message);
                            mDialog.positive.setText("继续购物");
                            mDialog.negative.setText("查看订单");
                            mDialog.show();
                            mDialog.positive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mContext.finish();
                                }
                            });
                            mDialog.negative.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialog.dismiss();
                                    Intent intent = new Intent(mContext, D0_OrderActivity.class);
                                    intent.putExtra("order_state", "");
                                    intent.putExtra("name", "所有订单");
                                    mContext.startActivity(intent);
                                    mContext.finish();
                                }
                            });
                        } else if (entrance.equals("2")) {
                            UnderDialog under = new UnderDialog(mContext, message);
                            under.show();
                            under.tv_goto_setpaypwd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mContext.finish();
                                }
                            });
                        } else if (entrance.equals("3")) {
                            mDialog = new MyDialog(mContext, message);
                            mDialog.positive.setText("继续送礼");
                            mDialog.negative.setText("查看订单");
                            mDialog.show();
                            mDialog.positive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mContext.finish();
                                }
                            });
                            mDialog.negative.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mDialog.dismiss();
                                    Intent intent = new Intent(mContext, D0_OrderActivity.class);
                                    intent.putExtra("order_state", "");
                                    intent.putExtra("name", "所有订单");
                                    mContext.startActivity(intent);
                                    mContext.finish();
                                }
                            });
                        } else if (entrance.equals("4")) {

                            UnderDialog under = new UnderDialog(mContext, "支付成功！赠送亲友?");
                            under.show();
                            under.tv_goto_setpaypwd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext, J0_SelectGiveObjectActivity.class);
                                    intent.putExtra("paySn", order_sn);
                                    intent.putExtra("entrance", "4");
                                    mContext.startActivity(intent);

                                    mContext.finish();

                                }

                            });


                        }
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "6001")) {
                            Toast.makeText(mContext, "操作取消",
                                    Toast.LENGTH_SHORT).show();
                            mContext.finish();
                        } else if (TextUtils.equals(resultStatus, "8000")) {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(mContext, "支付确定中",
                                    Toast.LENGTH_SHORT).show();
                            mContext.finish();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            message = "订单支付失败，请继续尝试支付！";
                            UnderDialog under = new UnderDialog(mContext, message);
                            under.show();
                            under.tv_goto_setpaypwd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mContext.finish();
                                }
                            });
                        }
                    }

                    break;
                }
                default:
                    break;
            }
        }

        ;
    };
}
