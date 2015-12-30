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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wjhgw.R;
import com.wjhgw.business.bean.OrderList_data;
import com.wjhgw.ui.activity.D3_EvaluateActivity;
import com.wjhgw.ui.view.listview.MyListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 订单列表适配器
 */
public class D0_OrderAdapter extends BaseAdapter {
    private Context c;
    private MyListView itemListView;
    private D0_OrderAdapter1 listAdapter;
    public ArrayList<OrderList_data> List;
    private LayoutInflater mInflater;

    public D0_OrderAdapter(Context c, ArrayList<OrderList_data> dataList) {
        mInflater = LayoutInflater.from(c);
        this.c = c;
        this.List = dataList;
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

        itemListView = (MyListView) cellView.findViewById(R.id.d0_item_list);
        final TextView tv_button1 = (TextView) cellView.findViewById(R.id.tv_button1);
        final TextView tv_button2 = (TextView) cellView.findViewById(R.id.tv_button2);
        final TextView tv_button3 = (TextView) cellView.findViewById(R.id.tv_button3);
        TextView tv_order_sn = (TextView) cellView.findViewById(R.id.tv_order_sn);
        TextView tv_add_time = (TextView) cellView.findViewById(R.id.tv_add_time);
        TextView tv_store_name = (TextView) cellView.findViewById(R.id.tv_store_name);
        TextView tv_state_desc = (TextView) cellView.findViewById(R.id.tv_state_desc);
        TextView tv_order_amount = (TextView) cellView.findViewById(R.id.tv_order_amount);
        TextView tv_payment_name = (TextView) cellView.findViewById(R.id.tv_payment_name);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) itemListView.getLayoutParams();
        linearParams.height = dip2px(c, 114) * List.get(position).extend_order_goods.size();// 当控件的高
        itemListView.setLayoutParams(linearParams);
        listAdapter = new D0_OrderAdapter1(c, List.get(position).extend_order_goods, List.get(position).order_id);
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

        Date date = new Date(Long.parseLong(List.get(position).add_time) * 1000);
        String timeString = new SimpleDateFormat("yyyy-MM-dd").format(date);
        tv_order_sn.setText("订单号: " + List.get(position).order_sn);
        tv_add_time.setText(timeString);
        tv_store_name.setText(List.get(position).store_name);
        tv_state_desc.setText(List.get(position).state_desc);
        tv_order_amount.setText("¥" + List.get(position).order_amount + "(含运费" + List.get(position).shipping_fee + ")");
        tv_payment_name.setText(List.get(position).payment_name);

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
            tv_button2.setVisibility(View.VISIBLE);
            tv_button2.setText("联系客服");
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
            //待评价
        } else if (List.get(position).order_state.equals("40")) {
            if (List.get(position).if_deliver) {
                tv_button1.setVisibility(View.VISIBLE);
                tv_button1.setText("查看物流");
            }
            if (List.get(position).delete) {
                tv_button2.setVisibility(View.VISIBLE);
                tv_button2.setText("删除订单");
            }
            //已完成
        } else if (List.get(position).order_state.equals("0")) {
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
                        Toast.makeText(c, "取消订单", Toast.LENGTH_SHORT).show();
                    }
                    //待付款
                } else if (List.get(position).order_state.equals("20")) {
                    if (List.get(position).if_remind) {
                        Toast.makeText(c, "提醒发货", Toast.LENGTH_SHORT).show();
                    }
                    //待发货
                } else if (List.get(position).order_state.equals("30")) {
                    if (List.get(position).if_receive) {
                        Toast.makeText(c, "确定收货", Toast.LENGTH_SHORT).show();
                    }
                    //待收货
                } else if (List.get(position).order_state.equals("40")) {
                    if (List.get(position).evaluation) {
                        Intent intent = new Intent(c, D3_EvaluateActivity.class);
                        String json = new Gson().toJson(List.get(position).extend_order_goods);
                        intent.putExtra("extend_order_goods", json);
                        intent.putExtra("order_id", List.get(position).order_id);
                        c.startActivity(intent);
                    }
                    //待评价
                } else if (List.get(position).order_state.equals("40")) {
                    if (List.get(position).if_deliver) {
                        Toast.makeText(c, "查看物流", Toast.LENGTH_SHORT).show();
                    }
                    //已完成
                } else if (List.get(position).order_state.equals("0")) {
                    if (List.get(position).delete) {
                        Toast.makeText(c, "删除订单", Toast.LENGTH_SHORT).show();
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
                    }
                    //待付款
                } else if (List.get(position).order_state.equals("20")) {
                    Toast.makeText(c, "联系客服", Toast.LENGTH_SHORT).show();

                } else if (List.get(position).order_state.equals("30")) {
                    if (List.get(position).if_deliver) {
                        Toast.makeText(c, "查看物流", Toast.LENGTH_SHORT).show();
                    }
                    //待收货
                } else if (List.get(position).order_state.equals("40")) {
                    if (List.get(position).delete) {
                        Toast.makeText(c, "删除订单", Toast.LENGTH_SHORT).show();
                    }
                    //已完成
                }
            }
        });
        tv_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c, "联系客服", Toast.LENGTH_SHORT).show();

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
}
