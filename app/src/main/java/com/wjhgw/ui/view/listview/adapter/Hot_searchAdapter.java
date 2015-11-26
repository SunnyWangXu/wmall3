package com.wjhgw.ui.view.listview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wjhgw.R;
import com.wjhgw.business.bean.Auto_complete_data;

import java.util.List;

/**
 * 搜索的热点词汇适配器
 */
public class Hot_searchAdapter extends BaseAdapter {


    private Context mContext;
    private List<Auto_complete_data> dataValues;

    public Hot_searchAdapter(Context mContext, List<Auto_complete_data> dataValues) {
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_item, parent, false);
        TextView tvValue = (TextView)convertView.findViewById(R.id.tv_item);
        tvValue.setText(dataValues.get(position).value);

        return convertView;
    }
}
