package com.wjhgw.ui.view.listview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wjhgw.R;
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.attr_gv_item, null);
        TextView tvValue = (TextView) convertView.findViewById(R.id.tv_attr_value);
        tvValue.setText(dataValues.get(position).attr_value_name);
        String attrValueID = dataValues.get(position).attr_value_id;

        tvValue.setClickable(true);
        tvValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        return convertView;
    }
}