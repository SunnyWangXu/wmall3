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
import com.wjhgw.business.bean.Get_goods_list_data;

import java.util.List;

/**
 * 酒柜历史ListView适配器
 */
public class J1_RecordAdapter extends BaseAdapter {
    public List<Get_goods_list_data> list;
    private Context mContext;

    public J1_RecordAdapter(Context context, List<Get_goods_list_data> actSearch_datas) {
        this.mContext = context;
        this.list = actSearch_datas;
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

        convertView = LayoutInflater.from(mContext).inflate(R.layout.j1_item, null);
        ImageView iv_goods_image = (ImageView) convertView.findViewById(R.id.iv_goods_image);
        TextView tv_member_nickname = (TextView) convertView.findViewById(R.id.tv_member_nickname);
        TextView tv_goods_name = (TextView) convertView.findViewById(R.id.tv_goods_name);
        TextView tv_goods_num = (TextView) convertView.findViewById(R.id.tv_goods_num);
        TextView tv_received_time = (TextView) convertView.findViewById(R.id.tv_received_time);

        APP.getApp().getImageLoader().displayImage(list.get(position).goods_image, iv_goods_image);
        tv_member_nickname.setText(list.get(position).member_nickname);
        tv_goods_name.setText(list.get(position).goods_name);
        tv_goods_num.setText("X" + list.get(position).goods_num);
        tv_received_time.setText(list.get(position).received_time);

        return convertView;
    }
}
