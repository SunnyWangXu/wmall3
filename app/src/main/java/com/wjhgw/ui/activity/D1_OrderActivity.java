package com.wjhgw.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
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
import com.wjhgw.business.api.Order_Request;
import com.wjhgw.business.bean.Order_detail;
import com.wjhgw.business.bean.PayOrder;
import com.wjhgw.business.response.BusinessResponse;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.dialog.MyDialog;
import com.wjhgw.ui.dialog.Order_cancelDialog;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.D1_OrderAdapter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 订单详情
 */
public class D1_OrderActivity extends BaseActivity implements BusinessResponse, OnClickListener, XListView.IXListViewListener {

    private MyListView mListView;
    private MyListView mListView1;
    private FrameLayout fl_logistics;
    private TextView tv_store_name;
    private TextView tv_state_desc;
    private TextView tv_reciver_name;
    private TextView tv_state;
    private TextView tv_order_amount;
    private TextView tv_order_sn;
    private TextView tv_phone;
    private TextView tv_address;
    private TextView tv_invoice_type;
    private LinearLayout ll_invoice;
    private TextView tv_invoice_rise;
    private TextView tv_invoice_content;
    private TextView tv_add_time;
    private TextView tv_remarks;
    private TextView tv_button1;
    private TextView tv_button2;
    private TextView tv_button3;
    private String key;
    private D1_OrderAdapter listAdapter;
    private String order_id = "";
    private LinearLayout order1;
    private LinearLayout order2;
    private LinearLayout order3;
    private Order_detail order_detail;
    private MyDialog mDialog;
    private Order_Request Request;
    private Order_cancelDialog order_cancelDialog;
    private String msg = "购买其他商品";
    private LinearLayout ll_logistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d1_order_layout);

        key = getKey();
        order_id = getIntent().getStringExtra("order_id");

        mListView.addHeaderView(order1);
        mListView.addHeaderView(order2);
        mListView.addHeaderView(order3);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
        mListView.setAdapter(null);

        /**
         * 设置listview不能滑动
         */
        mListView1.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        Order_detail();
        Request = new Order_Request(this);
        Request.addResponseListener(this);
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("订单详情");
    }

    @Override
    public void onFindViews() {
        order1 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.d1_collect_item, null);
        order2 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.d1_goods_item, null);
        order3 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.d1_order_item, null);
        mListView = (MyListView) findViewById(R.id.d1_list_layout);
        mListView1 = (MyListView) order2.findViewById(R.id.d1_item_list);

        fl_logistics = (FrameLayout) order1.findViewById(R.id.fl_logistics);
        tv_store_name = (TextView) order1.findViewById(R.id.tv_store_name);
        tv_state_desc = (TextView) order1.findViewById(R.id.tv_state_desc);
        tv_reciver_name = (TextView) order1.findViewById(R.id.tv_reciver_name);
        tv_state = (TextView) order1.findViewById(R.id.tv_state);
        tv_phone = (TextView) order1.findViewById(R.id.tv_phone);
        tv_address = (TextView) order1.findViewById(R.id.tv_address);

        ll_invoice = (LinearLayout) order3.findViewById(R.id.ll_invoice);
        ll_logistics = (LinearLayout) order1.findViewById(R.id.ll_logistics);
        tv_invoice_type = (TextView) order3.findViewById(R.id.tv_invoice_type);
        tv_invoice_rise = (TextView) order3.findViewById(R.id.tv_invoice_rise);
        tv_invoice_content = (TextView) order3.findViewById(R.id.tv_invoice_content);
        tv_order_amount = (TextView) order3.findViewById(R.id.tv_order_amount);
        tv_order_sn = (TextView) order3.findViewById(R.id.tv_order_sn);
        tv_add_time = (TextView) order3.findViewById(R.id.tv_add_time);
        tv_remarks = (TextView) order3.findViewById(R.id.tv_remarks);


        tv_button1 = (TextView) order3.findViewById(R.id.tv_button1);
        tv_button2 = (TextView) order3.findViewById(R.id.tv_button2);
        tv_button3 = (TextView) order3.findViewById(R.id.tv_button3);
    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        fl_logistics.setOnClickListener(this);
        tv_button1.setOnClickListener(this);
        tv_button2.setOnClickListener(this);
        tv_button3.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_logistics:
                Intent intent = new Intent(D1_OrderActivity.this, D2_LogisticsActivity.class);
                intent.putExtra("order_id", order_id);
                startActivity(intent);
                break;
            case R.id.tv_button1:
                if (order_detail.datas.order_state.equals("10")) {
                    if (order_detail.datas.if_cancel) {
                        //showToastShort("取消订单");
                        dialog();
                    }
                    //待付款
                } else if (order_detail.datas.order_state.equals("20")) {
                    if (order_detail.datas.if_remind) {
                        //showToastShort("提醒发货");
                        mDialog = new MyDialog(this, "确定要删除该订单？");
                        mDialog.show();
                        mDialog.positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Request.order_remind(order_id, key);
                                mDialog.dismiss();
                            }
                        });
                        mDialog.negative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.dismiss();
                            }
                        });
                    }
                    //待发货
                } else if (order_detail.datas.order_state.equals("30")) {
                    if (order_detail.datas.if_receive) {
                        //showToastShort("确定收货");
                        mDialog = new MyDialog(this, "是否确定收货？");
                        mDialog.show();
                        mDialog.positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Request.order_receive(order_id, key);
                                mDialog.dismiss();
                            }
                        });
                        mDialog.negative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.dismiss();
                            }
                        });
                    }
                    //待收货
                } else if (order_detail.datas.order_state.equals("60")) {
                    if (order_detail.datas.evaluation) {
                        //showToastShort("商品评价");

                    }
                    //待评价
                } else if (order_detail.datas.order_state.equals("40")) {
                    /*if (order_detail.datas.if_deliver) {
                        showToastShort("查看物流");
                    }*/
                    //评价
                    if (order_detail.datas.evaluation) {
                        intent = new Intent(this, D3_EvaluateActivity.class);
                        String json = new Gson().toJson(order_detail.datas.extend_order_goods);
                        intent.putExtra("extend_order_goods", json);
                        intent.putExtra("order_id", order_detail.datas.order_id);
                        startActivity(intent);
                    }
                    //已完成
                } else if (order_detail.datas.order_state.equals("0")) {
                    if (order_detail.datas.delete) {
                        //showToastShort("删除订单");
                        mDialog = new MyDialog(this,"确定要删除该订单？");
                        mDialog.show();
                        mDialog.positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Request.order_delete(order_id, key);
                                mDialog.dismiss();
                            }
                        });
                        mDialog.negative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.dismiss();
                            }
                        });
                    }
                    //已取消
                }
                break;
            case R.id.tv_button2:
                if (order_detail.datas.order_state.equals("10")) {
                    if (order_detail.datas.payment) {
                        //showToastShort("前往付款");
                        member_payment(order_detail.datas.pay_sn, key, order_detail.datas.order_amount,
                                order_detail.datas.rcb_amount, order_detail.datas.pd_amount);
                    }
                    //待付款
                } else if (order_detail.datas.order_state.equals("20")) {
                    if (order_detail.datas.if_refund_cancel) {
                        intent = new Intent(this, D4_Customer_serviceActivity.class);
                        intent.putExtra("lock_state", order_detail.datas.order_state);
                        intent.putExtra("order_sn", order_detail.datas.order_id);
                        startActivity(intent);
                    }

                } else if (order_detail.datas.order_state.equals("30")) {
                    if (order_detail.datas.if_deliver) {
                        //showToastShort("查看物流");
                        intent = new Intent(D1_OrderActivity.this, D2_LogisticsActivity.class);
                        intent.putExtra("order_id", order_id);
                        startActivity(intent);
                    }
                    //待收货
                } else if (order_detail.datas.order_state.equals("40")) {
                    if (order_detail.datas.delete) {
                        //showToastShort("删除订单");
                        mDialog = new MyDialog(this,"确定要删除该订单？");
                        mDialog.show();
                        mDialog.positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Request.order_delete(order_id, key);
                                mDialog.dismiss();
                            }
                        });
                        mDialog.negative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDialog.dismiss();
                            }
                        });
                    }
                    //已完成
                }
                break;
            case R.id.tv_button3:
                //showToastShort("联系客服");
                mDialog = new MyDialog(this, "是否拨打客服电话");
                mDialog.show();
                mDialog.positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4006569333"));
                        startActivity(intent1);
                        mDialog.dismiss();
                    }
                });
                mDialog.negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
                break;

            default:
                break;
        }

    }

    @Override
    public void onRefresh(int id) {
    }

    @Override
    public void onLoadMore(int id) {
    }

    /**
     * 订单详情
     */
    private void Order_detail() {
        StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("order_id", order_id);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Order_detail, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                Dismiss();
                if (null != responseInfo) {
                    order_detail = gson.fromJson(responseInfo.result, Order_detail.class);
                    if (order_detail.status.code == 10000) {
                        mListView.stopRefresh();
                        mListView.stopLoadMore();
                        mListView.setRefreshTime();
                        if (order_detail.datas != null) {
                            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mListView1.getLayoutParams();
                            linearParams.height = dip2px(D1_OrderActivity.this, 114) * order_detail.datas.extend_order_goods.size();// 当控件的高
                            mListView1.setLayoutParams(linearParams);

                            listAdapter = new D1_OrderAdapter(D1_OrderActivity.this, order_detail.datas.extend_order_goods,
                                    order_detail.datas.lock_state,order_detail.datas.order_state,order_detail.datas.order_id);
                            mListView1.setAdapter(listAdapter);

                            tv_store_name.setText(order_detail.datas.store_name);
                            tv_state_desc.setText(order_detail.datas.state_desc);
                            tv_order_sn.setText(order_detail.datas.order_sn);
                            int num = 0;
                            for(int i = 0; i < order_detail.datas.extend_order_goods.size(); i++){
                                num += Integer.parseInt(order_detail.datas.extend_order_goods.get(i).goods_num);
                            }
                            if(!order_detail.datas.shipping_fee.equals("null")){
                                tv_order_amount.setText("共"+ num +"件商品,合计：¥" + order_detail.datas.order_amount + "(含运费" + order_detail.datas.shipping_fee + ")");
                            }else {
                                tv_order_amount.setText("共"+ num +"件商品,合计：¥" + order_detail.datas.order_amount + "(含运费0.00)");
                            }

                            if (order_detail.datas.extend_order_common.invoice_info == null) {
                                ll_invoice.setVisibility(View.GONE);
                            } else {
                                tv_invoice_type.setText(order_detail.datas.extend_order_common.invoice_info.类型);
                                tv_invoice_rise.setText(order_detail.datas.extend_order_common.invoice_info.抬头);
                                tv_invoice_content.setText(order_detail.datas.extend_order_common.invoice_info.内容);
                            }
                            if (order_detail.datas.order_type.equals("0")) {
                                tv_reciver_name.setText(order_detail.datas.extend_order_common.reciver_name);
                                tv_phone.setText(order_detail.datas.extend_order_common.reciver_info.phone);
                                tv_address.setText("收货地址：" + order_detail.datas.extend_order_common.reciver_info.address);
                            } else {
                                ll_logistics.setVisibility(View.GONE);
                            }

                            tv_add_time.setText(order_detail.datas.add_time);
                            tv_remarks.setText(order_detail.datas.order_message);

                            if (order_detail.datas.order_state.equals("10")) {
                                if (order_detail.datas.if_cancel) {
                                    tv_button1.setVisibility(View.VISIBLE);
                                    tv_button1.setText("取消订单");
                                }
                                if (order_detail.datas.payment) {
                                    tv_button2.setVisibility(View.VISIBLE);
                                    tv_button2.setText("前往付款");
                                }
                                tv_button3.setVisibility(View.VISIBLE);
                                tv_button3.setText("联系客服");
                                tv_state.setText("订单已经提交，等待买家付款，系统将于" + order_detail.datas.warning_time + "自动关闭该订单");
                                //待付款
                            } else if (order_detail.datas.order_state.equals("20")) {
                                if (order_detail.datas.if_remind) {
                                    tv_button1.setVisibility(View.VISIBLE);
                                    tv_button1.setText("提醒发货");
                                }
                                if (order_detail.datas.if_refund_cancel) {
                                    tv_button2.setVisibility(View.VISIBLE);
                                    tv_button2.setText("申请售后");
                                }
                                tv_button3.setVisibility(View.VISIBLE);
                                tv_button3.setText("联系客服");
                                tv_state.setText("已支付成功,订单已提交商家进行备货发货准备");
                                //待发货
                            } else if (order_detail.datas.order_state.equals("30")) {
                                if (order_detail.datas.if_receive) {
                                    tv_button1.setVisibility(View.VISIBLE);
                                    tv_button1.setText("确定收货");
                                }
                                if (order_detail.datas.if_deliver) {
                                    tv_button2.setVisibility(View.VISIBLE);
                                    tv_button2.setText("查看物流");
                                }
                                tv_button3.setVisibility(View.VISIBLE);
                                tv_button3.setText("联系客服");
                                tv_state.setText("商家已发货，系统将于" + order_detail.datas.warning_time+ "自动完成[确认收货],完成交易");
                                //待收货
                            } else if (order_detail.datas.order_state.equals("40")) {
                                if (order_detail.datas.evaluation) {
                                    tv_button1.setVisibility(View.VISIBLE);
                                    tv_button1.setText("商品评价");
                                    tv_state.setText("交易已完成,你可以对购买的商品及商家的服务进行评价及晒单");
                                }
                                if (order_detail.datas.if_deliver) {
                                    tv_button1.setVisibility(View.VISIBLE);
                                    tv_button1.setText("查看物流");
                                }
                                if (order_detail.datas.delete) {
                                    tv_button2.setVisibility(View.VISIBLE);
                                    tv_button2.setText("删除订单");
                                    tv_state.setText("已完成(已评价):交易已完成");
                                }

                                //待评价 || 已完成
                            } /*else if (order_detail.datas.order_state.equals("40")) {
                                if (order_detail.datas.if_deliver) {
                                    tv_button1.setVisibility(View.VISIBLE);
                                    tv_button1.setText("查看物流");
                                }
                                if (order_detail.datas.delete) {
                                    tv_button2.setVisibility(View.VISIBLE);
                                    tv_button2.setText("删除订单");
                                }
                                tv_state.setText("订单交易成功");
                                //
                            }*/ else if (order_detail.datas.order_state.equals("0")) {
                                if (order_detail.datas.delete) {
                                    tv_button1.setVisibility(View.VISIBLE);
                                    tv_button1.setText("删除订单");
                                }
                                tv_state.setText("订单交易关闭");
                                //已取消
                            }
                        }
                    }else {
                        overtime(order_detail.status.code,order_detail.status.msg);
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
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void OnMessageResponse(String url, String response, JSONObject status) throws JSONException {
        Order_detail();
    }

    private void dialog() {
        order_cancelDialog = new Order_cancelDialog(this);
        order_cancelDialog.show();
        order_cancelDialog.iv_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_cancelDialog.iv_button1.setImageResource(R.mipmap.ic_order_select);
                order_cancelDialog.iv_button2.setImageResource(R.mipmap.ic_order_blank);
                order_cancelDialog.iv_button3.setImageResource(R.mipmap.ic_order_blank);
                order_cancelDialog.iv_button4.setImageResource(R.mipmap.ic_order_blank);
                msg = "购买其他商品";
            }
        });
        order_cancelDialog.iv_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_cancelDialog.iv_button1.setImageResource(R.mipmap.ic_order_blank);
                order_cancelDialog.iv_button2.setImageResource(R.mipmap.ic_order_select);
                order_cancelDialog.iv_button3.setImageResource(R.mipmap.ic_order_blank);
                order_cancelDialog.iv_button4.setImageResource(R.mipmap.ic_order_blank);
                msg = "改用其他配送方法";
            }
        });
        order_cancelDialog.iv_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_cancelDialog.iv_button1.setImageResource(R.mipmap.ic_order_blank);
                order_cancelDialog.iv_button2.setImageResource(R.mipmap.ic_order_blank);
                order_cancelDialog.iv_button3.setImageResource(R.mipmap.ic_order_select);
                order_cancelDialog.iv_button4.setImageResource(R.mipmap.ic_order_blank);
                msg = "从其它店铺购买";
            }
        });
        order_cancelDialog.iv_button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_cancelDialog.iv_button1.setImageResource(R.mipmap.ic_order_blank);
                order_cancelDialog.iv_button2.setImageResource(R.mipmap.ic_order_blank);
                order_cancelDialog.iv_button3.setImageResource(R.mipmap.ic_order_blank);
                order_cancelDialog.iv_button4.setImageResource(R.mipmap.ic_order_select);
                msg = "其它原因";
            }
        });
        order_cancelDialog.tv_determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request.order_cancel(order_id, key, msg + "/" + order_cancelDialog.et_content.getText().toString());
                order_cancelDialog.dismiss();
            }
        });
        order_cancelDialog.tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_cancelDialog.dismiss();
            }
        });
    }


    /**
     * 订单支付
     */
    public void member_payment(String pay_sn, String key, final String order_amount, final String rcb_amount, final String pd_amount) {
        super.StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("pay_sn", pay_sn);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Member_payment, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo != null) {
                    Gson gson = new Gson();
                    PayOrder payOrder = gson.fromJson(responseInfo.result, PayOrder.class);
                    D1_OrderActivity.this.Dismiss();
                    if (payOrder.status.code == 10000) {
                        if(Double.valueOf(payOrder.datas.data.total_fee) > 0) {
                            Intent intent = new Intent(D1_OrderActivity.this, S3_SelectPaymentActivity.class);
                            intent.putExtra("tvRealPay", order_amount);
                            intent.putExtra("tvAvailablePredeposit", rcb_amount);
                            intent.putExtra("tvAvailableRcBalance", pd_amount);
                            intent.putExtra("paySn", payOrder.datas.data.pay_sn);
                            intent.putExtra("totalFee", payOrder.datas.data.total_fee);
                            intent.putExtra("goodsName", payOrder.datas.data.goods_name);
                            intent.putExtra("goodsDetail", payOrder.datas.data.goods_detail);
                            intent.putExtra("entrance", "2");
                            startActivity(intent);
                        }else {
                            Toast.makeText(D1_OrderActivity.this, "需支付金额为" + Double.valueOf(payOrder.datas.data.total_fee), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        overtime(payOrder.status.code, payOrder.status.msg);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToastShort("失败");
            }
        });
    }
}
