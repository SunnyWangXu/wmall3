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
import com.wjhgw.business.data.goods_class_data;
import com.wjhgw.ui.fragment.CategoryFragment;

import java.util.ArrayList;

/**
 * 一级分类列表适配器
 */
public class goods_class_adapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<goods_class_data> List;
    private ViewHolder vh = new ViewHolder();

    private static class ViewHolder {
        private TextView text;
        private LinearLayout class_item_layo;
    }

    public goods_class_adapter(Context c, ArrayList<goods_class_data> list) {
        //mInflater = LayoutInflater.from(c);
        this.mContext = c;
        this.List = list;
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

    @Override
    public View getView(int position, View cellView, ViewGroup parent) {
        cellView = LayoutInflater.from(mContext).inflate(R.layout.discovery_item, null);
        LinearLayout class_item_layout = (LinearLayout)cellView.findViewById(R.id.class_item_layout);
        TextView text = (TextView)cellView.findViewById(R.id.goods_class);
        text.setText(List.get(position).gc_name);

        if(position == CategoryFragment.MAK){
            text.setTextColor(Color.parseColor("#fff14f4f"));
            class_item_layout.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        return cellView;
    }
}
