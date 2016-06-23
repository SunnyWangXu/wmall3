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
import com.wjhgw.business.bean.receive_list_data;
import com.wjhgw.ui.DiyView.RoundImageView;

import java.util.List;

/**
 * 礼包详情领取信息
 */
public class J5_receiveAdapter extends BaseAdapter {
    public List<receive_list_data> list;
    private Context mContext;

    public J5_receiveAdapter(Context context, List<receive_list_data> goods_list) {
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

        convertView = LayoutInflater.from(mContext).inflate(R.layout.j5_receive_item, null);
        ImageView iv_j5_goods_image = (ImageView) convertView.findViewById(R.id.iv_j5_goods_image);
        RoundImageView j5_sender_avatar = (RoundImageView) convertView.findViewById(R.id.j5_sender_avatar);
        TextView tv_j5_member_nickname = (TextView) convertView.findViewById(R.id.tv_j5_member_nickname);
        TextView tv_j5_received_time = (TextView) convertView.findViewById(R.id.tv_j5_received_time);

        APP.getApp().getImageLoader().displayImage(list.get(position).member_avatar,j5_sender_avatar);
        APP.getApp().getImageLoader().displayImage(list.get(position).goods_image,iv_j5_goods_image);
        tv_j5_member_nickname.setText(list.get(position).member_nickname);
        tv_j5_received_time.setText(list.get(position).received_time);
        return convertView;
    }
}
