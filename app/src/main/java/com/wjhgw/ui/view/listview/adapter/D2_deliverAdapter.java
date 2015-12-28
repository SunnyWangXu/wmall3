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
import android.widget.ImageView;
import android.widget.TextView;

import com.wjhgw.R;
import com.wjhgw.business.bean.Deliver_list_data;

import java.util.ArrayList;

/**
 * 物流详情适配器
 */
public class D2_deliverAdapter extends BaseAdapter {
    Context c;
    public ArrayList<Deliver_list_data> List;
    private LayoutInflater mInflater;

    public D2_deliverAdapter(Context c, ArrayList<Deliver_list_data> dataList) {
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

    public View getView(int position, View cellView, ViewGroup parent) {
        cellView = mInflater.inflate(R.layout.d2_item, null);
        ImageView iv_logistics_state = (ImageView) cellView.findViewById(R.id.iv_logistics_state);
        TextView tv_context = (TextView) cellView.findViewById(R.id.tv_context);
        TextView tv_time = (TextView) cellView.findViewById(R.id.tv_time);

        if (position == 0) {
            iv_logistics_state.setImageResource(R.mipmap.ic_logistics_status1);
        }
        tv_context.setText(List.get(position).context);
        tv_time.setText(List.get(position).time);

        return cellView;
    }
}
