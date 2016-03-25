package com.wjhgw.ui.view.listview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wjhgw.R;
import com.wjhgw.business.bean.RefundDetailList;

import java.util.List;

/**
 * 售后详情的商品列表适配器
 */
public class RefundListAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<RefundDetailList> refundDetailList;

    public RefundListAdapter(Context mContext, List<RefundDetailList> refundDetailList) {
        this.mContext = mContext;
        this.refundDetailList = refundDetailList;
    }

    @Override
    public int getCount() {
        return refundDetailList.size();
    }

    @Override
    public Object getItem(int position) {
        return refundDetailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.d4_layout_item, null);
        TextView tv_d4_goods_name = (TextView) convertView.findViewById(R.id.tv_d4_goods_name);
        TextView tv_d4_num = (TextView) convertView.findViewById(R.id.tv_d4_num);

        tv_d4_goods_name.setText(refundDetailList.get(position).goods_name);
        tv_d4_num.setText("¥" + refundDetailList.get(position).goods_pay_price + "*" + refundDetailList.get(position).goods_num + "(数量)");
        return convertView;
    }
}
