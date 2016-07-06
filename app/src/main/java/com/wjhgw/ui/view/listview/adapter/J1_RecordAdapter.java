package com.wjhgw.ui.view.listview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.business.bean.Get_goods_list_data;
import com.wjhgw.ui.DiyView.RoundImageView;
import com.wjhgw.ui.view.listview.MyListView;

import java.util.List;

/**
 * 接收礼包ListView适配器
 */
public class J1_RecordAdapter extends BaseAdapter {
    public List<Get_goods_list_data> list;
    private Context mContext;
    private J1_ItemAdapter listAdapter;

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
        RoundImageView iv_goods_image = (RoundImageView) convertView.findViewById(R.id.j1_sender_avatar);
        TextView j1_sender_nickname = (TextView) convertView.findViewById(R.id.j1_sender_nickname);
        TextView tv_received_time = (TextView) convertView.findViewById(R.id.tv_received_time);
        TextView tv_gift_note = (TextView) convertView.findViewById(R.id.tv_gift_note);
        MyListView j1_list_item = (MyListView) convertView.findViewById(R.id.j1_list_item);

        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) j1_list_item.getLayoutParams();
        linearParams.height = dip2px(mContext, 124) * list.get(position).goods_list.size();// 当控件的高
        j1_list_item.setLayoutParams(linearParams);

        listAdapter = new J1_ItemAdapter(mContext, list.get(position).goods_list);
        j1_list_item.setAdapter(listAdapter);
        /**
         * 设置listview不能滑动
         */
        j1_list_item.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        APP.getApp().getImageLoader().displayImage(list.get(position).sender_avatar, iv_goods_image);
        j1_sender_nickname.setText(list.get(position).sender_nickname);
        tv_gift_note.setText("礼包赠言:"+list.get(position).gift_note);
        tv_received_time.setText("领取时间:"+list.get(position).received_time);

        return convertView;
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
