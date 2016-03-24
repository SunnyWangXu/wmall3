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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wjhgw.R;
import com.wjhgw.business.bean.OrderList_goods_list_data;

import java.util.ArrayList;

/**
 * @author
 */
public class D4_customer_serviceAdapter extends BaseAdapter {
    Context c;
    public ArrayList<OrderList_goods_list_data> List;
    private LayoutInflater mInflater;

    public D4_customer_serviceAdapter(Context c, ArrayList<OrderList_goods_list_data> dataList) {
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
        cellView = mInflater.inflate(R.layout.d4_layout_item, null);
        TextView tv_d4_goods_name = (TextView) cellView.findViewById(R.id.tv_d4_goods_name);
        TextView tv_d4_num = (TextView) cellView.findViewById(R.id.tv_d4_num);

        tv_d4_goods_name.setText(List.get(position).goods_name);
        tv_d4_num.setText("¥" + List.get(position).goods_price + "*" + List.get(position).goods_num + "(数量)");
        return cellView;
    }
}
