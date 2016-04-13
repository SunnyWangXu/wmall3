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
import com.wjhgw.business.bean.Goods_data;

import java.util.List;

/**
 * 酒柜历史ListView适配器
 */
public class J1_ItemAdapter extends BaseAdapter {
    public List<Goods_data> list;
    private Context mContext;

    public J1_ItemAdapter(Context context, List<Goods_data> goods_list) {
        this.mContext = context;
        this.list = goods_list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(R.layout.j1_item_item, null);
        ImageView iv_goods_image = (ImageView) convertView.findViewById(R.id.iv_goods_image);
        TextView tv_goods_name = (TextView) convertView.findViewById(R.id.tv_goods_name);
        TextView tv_goods_price = (TextView) convertView.findViewById(R.id.tv_goods_price);
        TextView tv_goods_num = (TextView) convertView.findViewById(R.id.tv_goods_num);

        APP.getApp().getImageLoader().displayImage(list.get(position).goods_image, iv_goods_image);
        tv_goods_name.setText(list.get(position).goods_name);
        tv_goods_price.setText(list.get(position).goods_price);
        tv_goods_num.setText("x"+list.get(position).goods_num);

        return convertView;
    }
}
