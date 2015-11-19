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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjhgw.R;
import com.wjhgw.business.bean.Goods_class1_data;
import com.wjhgw.ui.fragment.CategoryFragment;

import java.util.ArrayList;

/**
 * 一级分类列表适配器
 */
public class goods_class_adapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Goods_class1_data> data;
    private ViewHolder vh = new ViewHolder();

    private static class ViewHolder {
        private TextView text;
        private LinearLayout ll_class_item;
    }

    public goods_class_adapter(Context c, ArrayList<Goods_class1_data> list) {
        this.mContext = c;
        this.data = list;
    }

    @Override
    public int getCount() {
        return data.size();
    }


    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View cellView, ViewGroup parent) {
        cellView = LayoutInflater.from(mContext).inflate(R.layout.class_item_layout, null);
        vh.ll_class_item = (LinearLayout) cellView.findViewById(R.id.ll_class_item);
        vh.text = (TextView) cellView.findViewById(R.id.goods_class);
        cellView.setTag(vh);
        vh.text.setText(data.get(position).gc_name);

        if (position == CategoryFragment.MAK) {
            vh.text.setTextColor(Color.parseColor("#fff14f4f"));
            vh.ll_class_item.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        return cellView;
    }
}
