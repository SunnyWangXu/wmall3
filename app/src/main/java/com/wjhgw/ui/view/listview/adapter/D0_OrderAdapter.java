package com.wjhgw.ui.view.listview.adapter;

//
//                       __
//                      /\ \   _
//    ____    ____   ___\ \ \_/ \           _____    ___     ___
//   / _  \  / __ \ / __ \ \    <     __   /\__  \  / __ \  / __ \
//  /\ \_\ \/\  __//\  __/\ \ \\ \   /\_\  \/_/  / /\ \_\ \/\ \_\ \
//  \ \____ \ \____\ \____\\ \_\\_\  \/_/   /\____\\ \____/\ \____/
//   \/____\ \/____/\/____/ \/_//_/         \/____/ \/___/  \/___/
//     /\____/
//     \/___/
//
//  Powered by BeeFramework
//

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.api.Order_Request;
import com.wjhgw.business.bean.OrderList_data;
import com.wjhgw.business.bean.PayOrder;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.activity.A0_LoginActivity;
import com.wjhgw.ui.activity.D2_LogisticsActivity;
import com.wjhgw.ui.activity.D3_EvaluateActivity;
import com.wjhgw.ui.activity.D4_Customer_serviceActivity;
import com.wjhgw.ui.activity.S3_SelectPaymentActivity;
import com.wjhgw.ui.dialog.LoadDialog;
import com.wjhgw.ui.dialog.MyDialog;
import com.wjhgw.ui.dialog.Order_cancelDialog;
import com.wjhgw.ui.dialog.SetPaypwdDialog;
import com.wjhgw.ui.view.listview.MyListView;

import java.util.ArrayList;

/**
 * 订单列表适配器
 */
public class D0_OrderAdapter extends BaseAdapter {
    public Context c;
    //private MyListView itemListView;
    private D0_OrderAdapter1 listAdapter;
    public ArrayList<OrderList_data> List;
    private LayoutInflater mInflater;
    private Order_Request Request;
    private String key;
    private MyDialog mDialog;
    private Order_cancelDialog order_cancelDialog;
    private String msg = "购买其他商品";
    private LoadDialog Dialog;

    public D0_OrderAdapter(Context c, ArrayList<OrderList_data> dataList, Order_Request Request, String key) {
        mInflater = LayoutInflater.from(c);
        this.c = c;
        this.List = dataList;
        this.Request = Request;
        this.key = key;
        Dialog = new LoadDialog(c);
    }

    @Override
    public int getCount() {
        return List.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    public View getView(final int position, View cellView, ViewGroup parent) {

        cellView = mInflater.inflate(R.layout.d0_item, null);

        MyListView itemListView = (MyListView) cellView.findViewById(R.id.d0_item_list);
        final TextView tv_button1 = (TextView) cellView.findViewById(R.id.tv_button1);
        final TextView tv_button2 = (TextView) cellView.findViewById(R.id.tv_button2);
        final TextView tv_button3 = (TextView) cellView.findViewById(R.id.tv_button3);
        TextView tv_store_name = (TextView) cellView.findViewById(R.id.tv_store_name);
        TextView tv_state_desc = (TextView) cellView.findViewById(R.id.tv_state_desc);
        TextView tv_order_amount = (TextView) cellView.findViewById(R.id.tv_order_amount);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) itemListView.getLayoutParams();
        linearParams.height = dip2px(c, 115) * List.get(position).extend_order_goods.size();// 当控件的高
        itemListView.setLayoutParams(linearParams);

        listAdapter = new D0_OrderAdapter1(c, List.get(position).extend_order_goods, List.get(position).lock_state,
                List.get(position).order_state, List.get(position).order_sn,List.get(position).order_id );
        itemListView.setAdapter(listAdapter);
        /**
         * 设置listview不能滑动
         */
        itemListView.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

//       /* itemListView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(c, D1_OrderActivity.class);
//                intent.putExtra("order_id", List.get(position).order_id);
//                c.startActivity(intent);
//            }
//        });
        tv_store_name.setText(List.get(position).store_name);
        tv_state_desc.setText(List.get(position).state_desc);
        int num = 0;
        for(int i = 0; i < List.get(position).extend_order_goods.size(); i++){
            num += Integer.parseInt(List.get(position).extend_order_goods.get(i).goods_num);
        }
        if(List.get(position).shipping_fee.equals("null")){
            tv_order_amount.setText("共"+ num +"件商品,合计：¥" + List.get(position).order_amount + "(含运费0.00)");

        }else {
            tv_order_amount.setText("共"+ num +"件商品,合计：¥" + List.get(position).order_amount + "(含运费" + List.get(position).shipping_fee + ")");

        }

        if (List.get(position).order_state.equals("10")) {
            if (List.get(position).if_cancel) {
                tv_button1.setVisibility(View.VISIBLE);
                tv_button1.setText("取消订单");
            }
            if (List.get(position).payment) {
                tv_button2.setVisibility(View.VISIBLE);
                tv_button2.setText("前往付款");
            }
            tv_button3.setVisibility(View.VISIBLE);
            tv_button3.setText("联系客服");
            //待付款
        } else if (List.get(position).order_state.equals("20")) {
            if (List.get(position).if_remind) {
                tv_button1.setVisibility(View.VISIBLE);
                tv_button1.setText("提醒发货");
            }
            if (List.get(position).if_refund_cancel) {
                tv_button2.setVisibility(View.VISIBLE);
                tv_button2.setText("申请售后");
            }
            tv_button3.setVisibility(View.VISIBLE);
            tv_button3.setText("联系客服");
            //待发货
        } else if (List.get(position).order_state.equals("30")) {
            if (List.get(position).if_receive) {
                tv_button1.setVisibility(View.VISIBLE);
                tv_button1.setText("确定收货");
            }
            if (List.get(position).if_deliver) {
                tv_button2.setVisibility(View.VISIBLE);
                tv_button2.setText("查看物流");
            }
            tv_button3.setVisibility(View.VISIBLE);
            tv_button3.setText("联系客服");
            //待收货
        } else if (List.get(position).order_state.equals("40")) {
            if (List.get(position).evaluation) {
                tv_button1.setVisibility(View.VISIBLE);
                tv_button1.setText("商品评价");
            }
            if (List.get(position).if_deliver) {
                tv_button2.setVisibility(View.VISIBLE);
                tv_button2.setText("查看物流");
            }
            if (List.get(position).delete) {
                tv_button2.setVisibility(View.VISIBLE);
                tv_button2.setText("删除订单");
            }
            //待评价||和已完成
        } /*else if (List.get(position).order_state.equals("40")) {
            if (List.get(position).if_deliver) {
                tv_button1.setVisibility(View.VISIBLE);
                tv_button1.setText("查看物流");
            }
            if (List.get(position).delete) {
                tv_button2.setVisibility(View.VISIBLE);
                tv_button2.setText("删除订单");
            }
            //已完成
        }*/ else if (List.get(position).order_state.equals("0")) {
            if (List.get(position).delete) {
                tv_button1.setVisibility(View.VISIBLE);
                tv_button1.setText("删除订单");
            }
            //已取消
        }

        tv_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (List.get(position).order_state.equals("10")) {
                    if (List.get(position).if_cancel) {
                        //Toast.makeText(c, "取消订单", Toast.LENGTH_SHORT).show();
                        dialog(List.get(position).order_id);
                    }
                    //待付款
                } else if (List.get(position).order_state.equals("20")) {
                    if (List.get(position).if_remind) {
                        //Toast.makeText(c, "提醒发货", Toast.LENGTH_SHORT).show();
                        Request.order_remind(List.get(position).order_id, key);
                    }
                    //待发货
                } else if (List.get(position).order_state.equals("30")) {
                    if (List.get(position).if_receive) {
                        //Toast.makeText(c, "确定收货", Toast.LENGTH_SHORT).show();
                        mDialog = new MyDialog(c, "是否确定收货？");
                        mDialog.show();
                        mDialog.positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Request.order_receive(List.get(position).order_id, key);
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
                } else if (List.get(position).order_state.equals("40")) {
                    if (List.get(position).evaluation) {
                        //评价
                        Intent intent = new Intent(c, D3_EvaluateActivity.class);
                        String json = new Gson().toJson(List.get(position).extend_order_goods);
                        intent.putExtra("extend_order_goods", json);
                        intent.putExtra("order_id", List.get(position).order_id);
                        c.startActivity(intent);
                    }
                    //待评价
                } /*else if (List.get(position).order_state.equals("40")) {
                    if (List.get(position).if_deliver) {
                        //Toast.makeText(c, "查看物流", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(c, D2_LogisticsActivity.class);
                        intent.putExtra("order_id", List.get(position).order_id);
                        c.startActivity(intent);
                    }
                    //已完成
                }*/ else if (List.get(position).order_state.equals("0")) {
                    if (List.get(position).delete) {
                        //Toast.makeText(c, "删除订单", Toast.LENGTH_SHORT).show();
                        mDialog = new MyDialog(c, "确定要删除该订单？");
                        mDialog.show();
                        mDialog.positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Request.order_delete(List.get(position).order_id, key);
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
            }
        });
        tv_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (List.get(position).order_state.equals("10")) {
                    if (List.get(position).payment) {
                        Toast.makeText(c, "前往付款", Toast.LENGTH_SHORT).show();
                        member_payment(List.get(position).pay_sn, key, List.get(position).order_amount,
                                List.get(position).rcb_amount, List.get(position).pd_amount);
                    }
                    //待付款
                } else if (List.get(position).order_state.equals("20")) {
                    if (List.get(position).if_refund_cancel) {
                        Intent intent = new Intent(c, D4_Customer_serviceActivity.class);
                        intent.putExtra("lock_state", List.get(position).order_state);
                        intent.putExtra("order_id", List.get(position).order_id);
                        c.startActivity(intent);
                    }
                    //申请售后
                } else if (List.get(position).order_state.equals("30")) {
                    if (List.get(position).if_deliver) {
                        //Toast.makeText(c, "查看物流", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(c, D2_LogisticsActivity.class);
                        intent.putExtra("order_id", List.get(position).order_id);
                        c.startActivity(intent);
                    }
                    //待收货
                } else if (List.get(position).order_state.equals("40")) {

                    if (List.get(position).if_deliver) {
                        //Toast.makeText(c, "查看物流", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(c, D2_LogisticsActivity.class);
                        intent.putExtra("order_id", List.get(position).order_id);
                        c.startActivity(intent);
                    }
                    //已完成
                }
            }
        });
        tv_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(c, "联系客服", Toast.LENGTH_SHORT).show();
                final SetPaypwdDialog Dialog = new SetPaypwdDialog(c, "联系客服","客服电话:400-6569333");
                Dialog.show();
                Dialog.tvGotoSetpaypwd.setText("拨打");
                Dialog.tvCancel.setTextColor(Color.parseColor("#333333"));
                Dialog.tvGotoSetpaypwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +"4006569333"));
                        c.startActivity(intent1);
                        Dialog.dismiss();
                    }
                });
                Dialog.tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog.dismiss();
                    }
                });
            }
        });

        return cellView;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void dialog(final String order_id) {
        order_cancelDialog = new Order_cancelDialog(c);
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
        Dialog.ProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("pay_sn", pay_sn);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Member_payment, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo != null) {
                    Gson gson = new Gson();
                    PayOrder payOrder = gson.fromJson(responseInfo.result, PayOrder.class);
                    Dialog.dismiss();
                    if (payOrder.status.code == 10000) {
                        if (Double.valueOf(payOrder.datas.data.total_fee) > 0) {
                            Intent intent = new Intent(c, S3_SelectPaymentActivity.class);
                            intent.putExtra("tvRealPay", order_amount);
                            intent.putExtra("tvAvailablePredeposit", rcb_amount);
                            intent.putExtra("tvAvailableRcBalance", pd_amount);
                            intent.putExtra("paySn", payOrder.datas.data.pay_sn);
                            intent.putExtra("totalFee", payOrder.datas.data.total_fee);
                            intent.putExtra("goodsName", payOrder.datas.data.goods_name);
                            intent.putExtra("goodsDetail", payOrder.datas.data.goods_detail);
                            intent.putExtra("entrance", "2");
                            c.startActivity(intent);
                        } else {
                            Toast.makeText(c, "需支付金额为" + Double.valueOf(payOrder.datas.data.total_fee), Toast.LENGTH_SHORT).show();
                        }
                    } else if (payOrder.status.code == 200103 || payOrder.status.code == 200104) {
                        Toast.makeText(c, "登录超时或未登录", Toast.LENGTH_SHORT).show();
                        c.getSharedPreferences("key", c.MODE_APPEND).edit().putString("key", "0").commit();
                        c.startActivity(new Intent(c, A0_LoginActivity.class));
                    } else {
                        Toast.makeText(c, payOrder.status.msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(c, "失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
