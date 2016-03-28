package com.wjhgw.ui.view.listview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wjhgw.R;
import com.wjhgw.business.bean.RefundExpressDatas;

import java.util.List;

/**
 * 获取支持的快递列表  适配器
 */
public class ExpressAdapter extends BaseAdapter {


    private final Context mContext;
    private final List<RefundExpressDatas> expressDatas;

    public ExpressAdapter(Context mContext, List<RefundExpressDatas> expressDatas) {
        this.mContext = mContext;
        this.expressDatas = expressDatas;

    }

    @Override
    public int getCount() {
        return expressDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return expressDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_express_item, null);
        TextView tvExpress = (TextView) convertView.findViewById(R.id.tv_express);
        tvExpress.setText(expressDatas.get(position).e_name);


        return convertView;
    }
}
