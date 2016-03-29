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
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.business.bean.return_refund_list_data;
import com.wjhgw.ui.activity.D6_AfterSaleDetailActivity;

import java.util.ArrayList;

/**
 * 退款/退货申请列表
 */
public class D6_customerAdapter extends BaseAdapter {
    Context c;
    public ArrayList<return_refund_list_data> List;
    private LayoutInflater mInflater;

    public D6_customerAdapter(Context c, ArrayList<return_refund_list_data> dataList) {
        mInflater = LayoutInflater.from(c);
        this.c = c;
        this.List = dataList;
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

    public View getView(final int position, View cellView, ViewGroup parent) {
        cellView = mInflater.inflate(R.layout.d6_item, null);
        LinearLayout ll_item = (LinearLayout) cellView.findViewById(R.id.ll_item);
        ImageView iv_goods_image_url = (ImageView) cellView.findViewById(R.id.iv_goods_image_url);
        TextView tv_text1 = (TextView) cellView.findViewById(R.id.tv_text1);
        TextView tv_text2 = (TextView) cellView.findViewById(R.id.tv_text2);
        TextView tv_text3 = (TextView) cellView.findViewById(R.id.tv_text3);
        TextView tv_text4 = (TextView) cellView.findViewById(R.id.tv_text4);
        TextView tv_text5 = (TextView) cellView.findViewById(R.id.tv_text5);

        APP.getApp().getImageLoader().displayImage(List.get(position).goods_image, iv_goods_image_url, APP.getApp().getImageOptions());
        tv_text1.setText(List.get(position).seller_state_desc);
        tv_text2.setText(List.get(position).goods_name);

        if(List.get(position).refund_type.equals("1")){
            tv_text4.setText("退款");
            tv_text3.setVisibility(View.GONE);
        }else if(List.get(position).refund_type.equals("2")){
            tv_text4.setText("退货退款");
            tv_text3.setText("X "+List.get(position).goods_num);
        }
        tv_text5.setText(List.get(position).refund_amount);

        ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, D6_AfterSaleDetailActivity.class);
                intent.putExtra("refund_id",List.get(position).refund_id);
                c.startActivity(intent);
            }
        });
        return cellView;
    }
}
