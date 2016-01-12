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
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wjhgw.R;
import com.wjhgw.business.bean.AssetsList_data;

import java.util.ArrayList;

/**
 * 充值卡余额明细列表适配器
 */
public class Z1_Adapter extends BaseAdapter {
    private Context c;
    public ArrayList<AssetsList_data> List;
    private LayoutInflater mInflater;


    public Z1_Adapter(Context c, ArrayList<AssetsList_data> dataList) {
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

        cellView = mInflater.inflate(R.layout.z1_item, null);
        TextView tv_explain = (TextView) cellView.findViewById(R.id.tv_explain);
        TextView tv_amount = (TextView) cellView.findViewById(R.id.tv_amount);
        TextView tv_time = (TextView) cellView.findViewById(R.id.tv_time);
        TextView tv_tagging = (TextView) cellView.findViewById(R.id.tv_tagging);

        tv_explain.setText(List.get(position).description);
        tv_time.setText(List.get(position).add_time);
        if (Double.parseDouble(List.get(position).available_amount) > 0) {
            tv_tagging.setText("收入");
            tv_amount.setTextColor(Color.parseColor("#f25252"));
            tv_amount.setText("+" + List.get(position).available_amount);
        } else {
            tv_tagging.setText("支出");
            tv_amount.setTextColor(Color.parseColor("#359a2b"));
            tv_amount.setText(List.get(position).available_amount);
        }

        return cellView;
    }
}
