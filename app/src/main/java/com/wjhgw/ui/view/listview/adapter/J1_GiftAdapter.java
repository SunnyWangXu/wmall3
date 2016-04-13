package com.wjhgw.ui.view.listview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wjhgw.R;
import com.wjhgw.business.bean.Gift_list_data;

import java.util.List;

/**
 * 发送出去礼包ListView适配器
 */
public class J1_GiftAdapter extends BaseAdapter {
    public List<Gift_list_data> list;
    private Context mContext;

    public J1_GiftAdapter(Context context, List<Gift_list_data> actSearch_datas) {
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

        convertView = LayoutInflater.from(mContext).inflate(R.layout.j1_item1, null);
        TextView tv_limit_type = (TextView) convertView.findViewById(R.id.tv_limit_type);
        TextView tv_add_time = (TextView) convertView.findViewById(R.id.tv_add_time);
        TextView tv_gift_note = (TextView) convertView.findViewById(R.id.tv_gift_note);
        TextView tv_gift_state = (TextView) convertView.findViewById(R.id.tv_gift_state);
        TextView tv_receive_info = (TextView) convertView.findViewById(R.id.tv_receive_info);

        if(list.get(position).limit_type.equals("1")){
            tv_limit_type.setText("多人礼包");
        }else if(list.get(position).limit_type.equals("0")){
            tv_limit_type.setText("单人礼包");
        }
        String gift_state = list.get(position).gift_state;
        if(gift_state.equals("0")){
            tv_gift_state.setText("无效");
        }else if(gift_state.equals("1")){
            tv_gift_state.setText("进行中");
        }else if(gift_state.equals("2")){
            tv_gift_state.setTextColor(Color.parseColor("#359A2B"));
            tv_gift_state.setText("已完成");
        }else if(gift_state.equals("3")){
            tv_gift_state.setText("已取消");
        }else if(gift_state.equals("4")){
            tv_gift_state.setText("已过期");
        }
        tv_add_time.setText("送出时间:"+list.get(position).add_time);
        tv_gift_note.setText("礼包赠言:"+list.get(position).gift_note);
        tv_receive_info.setText(list.get(position).receive_info);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }
}
