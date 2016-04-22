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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.business.bean.OrderList_goods_list_data;
import com.wjhgw.ui.activity.D4_Customer_serviceActivity;

import java.util.ArrayList;

/**
 * 订单详情item
 */
public class D1_OrderAdapter extends BaseAdapter {
    Context c;
    public ArrayList<OrderList_goods_list_data> List;
    private LayoutInflater mInflater;
    private String order_id;
    private String lock_state; //是否处于售后中
    private String order_state; //是否是待发货状态
    private String order_type; //订单状态
    private boolean state = false;

    public D1_OrderAdapter(Context c, ArrayList<OrderList_goods_list_data> dataList, String lock_state,
                           String order_state, String order_id, String order_type) {
        mInflater = LayoutInflater.from(c);
        this.c = c;
        this.List = dataList;
        this.lock_state = lock_state;
        this.order_state = order_state;
        this.order_id = order_id;
        this.order_type = order_type;
        if(lock_state.equals("1") && order_state.equals("20")){
            state =true;
            //售后中和待发货状态
        }
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
        cellView = mInflater.inflate(R.layout.d0_layout_item, null);
        ImageView iv_goods_image_url = (ImageView) cellView.findViewById(R.id.iv_goods_image_url);
        TextView tv_goods_name = (TextView) cellView.findViewById(R.id.tv_goods_name);
        TextView tv_goods_price = (TextView) cellView.findViewById(R.id.tv_goods_price);
        TextView tv_goods_num = (TextView) cellView.findViewById(R.id.tv_goods_num);
        TextView tv_refund = (TextView) cellView.findViewById(R.id.tv_refund);
        TextView tv_button1 = (TextView) cellView.findViewById(R.id.tv_button1);

        APP.getApp().getImageLoader().displayImage(List.get(position).goods_image_url, iv_goods_image_url, APP.getApp().getImageOptions());
        tv_goods_name.setText(List.get(position).goods_name);
        tv_goods_price.setText("¥ " + List.get(position).goods_price);
        tv_goods_num.setText("X " + List.get(position).goods_num);

        if(!lock_state.equals("0") && List.get(position).refund.equals("0") || state){
            tv_refund.setVisibility(View.VISIBLE);
            //待收货状态
        }else if(order_state.equals("30") ||order_state.equals("40") ||order_state.equals("60")){
            if(!order_type.equals("3")){
                tv_button1.setVisibility(View.VISIBLE);
            }

        }

        tv_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, D4_Customer_serviceActivity.class);
                intent.putExtra("lock_state", order_state);
                intent.putExtra("order_type", order_type);
                intent.putExtra("rec_id", List.get(position).rec_id);
                intent.putExtra("order_id", order_id);
                c.startActivity(intent);
            }
        });
        return cellView;
    }
}
