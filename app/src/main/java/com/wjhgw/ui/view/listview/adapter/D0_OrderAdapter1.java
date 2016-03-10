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
import com.wjhgw.ui.activity.D1_OrderActivity;

import java.util.ArrayList;

/**
 * @author
 */
public class D0_OrderAdapter1 extends BaseAdapter {
    Context c;
    public ArrayList<OrderList_goods_list_data> List;
    private LayoutInflater mInflater;
    private String order_id;

    public D0_OrderAdapter1(Context c, ArrayList<OrderList_goods_list_data> dataList, String order_id) {
        mInflater = LayoutInflater.from(c);
        this.c = c;
        this.List = dataList;
        this.order_id = order_id;
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

    public View getView(int position, View cellView, ViewGroup parent) {
        cellView = mInflater.inflate(R.layout.d0_layout_item, null);
        ImageView iv_goods_image_url = (ImageView) cellView.findViewById(R.id.iv_goods_image_url);
        TextView tv_goods_name = (TextView) cellView.findViewById(R.id.tv_goods_name);
        TextView tv_goods_price = (TextView) cellView.findViewById(R.id.tv_goods_price);
        TextView tv_goods_num = (TextView) cellView.findViewById(R.id.tv_goods_num);
        TextView tv_refund = (TextView) cellView.findViewById(R.id.tv_refund);

        APP.getApp().getImageLoader().displayImage(List.get(position).goods_image_url, iv_goods_image_url, APP.getApp().getImageOptions());
        tv_goods_name.setText(List.get(position).goods_name);
        tv_goods_price.setText("¥ " + List.get(position).goods_price);
        tv_goods_num.setText("X " + List.get(position).goods_num);
        if(List.get(position).refund.equals("0")){
            tv_refund.setVisibility(View.VISIBLE);
        }
        cellView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, D1_OrderActivity.class);
                intent.putExtra("order_id", order_id);
                c.startActivity(intent);
            }
        });
        return cellView;
    }
}
