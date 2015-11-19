package com.wjhgw.ui.view.listview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wjhgw.business.bean.Goods_attr_data_value;

import java.util.List;

/**
 * 商品分类属性 GridView 除第一条之外的其他条的适配器
 */
public class AttrValueGVAdapter2 extends BaseAdapter{


    private final Context mContext;
    private final List<Goods_attr_data_value> dataValues;

    public AttrValueGVAdapter2(Context mContext, List<Goods_attr_data_value> dataValues) {
        this.mContext = mContext;
        this.dataValues = dataValues;
    }

    @Override
    public int getCount() {
        return dataValues.size();
    }

    @Override
    public Object getItem(int position) {
        return dataValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tvValue = new TextView(mContext);
        tvValue.setText(dataValues.get(position).attr_value_name);
        String attrValueID = dataValues.get(position).attr_value_id;
//        parent.addView(tvValue);
        return tvValue;
    }
}
