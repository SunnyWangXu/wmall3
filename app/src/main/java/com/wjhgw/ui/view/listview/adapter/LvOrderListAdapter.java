package com.wjhgw.ui.view.listview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.business.bean.Order_goods_list;

import java.util.ArrayList;

/**
 * 选中订单待支付的ListView适配器
 */
public class LvOrderListAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<Order_goods_list> order_goods_lists;
    private ImageView ivOrderListImage;
    private TextView tvOrderGoodsName;
    private TextView tvOrderGoodsPrice;
    private TextView tvOrderGoodsNum;
    public Double realPay = 0.00;

    public LvOrderListAdapter(Context mContext, ArrayList<Order_goods_list> order_goods_lists) {
        this.mContext = mContext;
        this.order_goods_lists = order_goods_lists;

    }

    @Override
    public int getCount() {
        return order_goods_lists.size();
    }

    @Override
    public Object getItem(int position) {
        return order_goods_lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(R.layout.s0_order_list_item, null);
        ivOrderListImage = (ImageView) convertView.findViewById(R.id.iv_order_list);
        tvOrderGoodsName = (TextView) convertView.findViewById(R.id.tv_order_goods_name);
        tvOrderGoodsPrice = (TextView) convertView.findViewById(R.id.tv_order_goods_price);
        tvOrderGoodsNum = (TextView) convertView.findViewById(R.id.tv_order_goods_num);

        tvOrderGoodsName.setText(order_goods_lists.get(position).goods_name);
        tvOrderGoodsPrice.setText("¥" + order_goods_lists.get(position).goods_price);
        tvOrderGoodsNum.setText("x" + order_goods_lists.get(position).goods_num);

        APP.getApp().getImageLoader().displayImage(order_goods_lists.get(position).goods_image_url, ivOrderListImage);

        realPay += Double.parseDouble(order_goods_lists.get(position).goods_total);

        return convertView;
    }
}
