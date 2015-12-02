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
import com.wjhgw.business.bean.ActSearch_datas;

import java.util.List;

/**
 * 排列商品查询ListView适配器
 */
public class ArrSearchAdapter extends BaseAdapter {
    private  List<ActSearch_datas> actSearch_datas;
    private  Context mContext;
    private ImageView ivSearchGoods;
    private TextView tvSearchName;
    private TextView tvSearchPice;
    private TextView tvSearchSale;

    public ArrSearchAdapter(Context context, List<ActSearch_datas> actSearch_datas) {
        this.mContext = context;
        this.actSearch_datas = actSearch_datas;
    }

    @Override
    public int getCount() {
        return actSearch_datas.size();
    }

    @Override
    public Object getItem(int position) {
        return actSearch_datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_actsearch_item, null);
            ivSearchGoods = (ImageView) convertView.findViewById(R.id.iv_search_goods);
            tvSearchName = (TextView) convertView.findViewById(R.id.tv_search_name);
            tvSearchPice = (TextView) convertView.findViewById(R.id.tv_search_pice);
            tvSearchSale = (TextView) convertView.findViewById(R.id.tv_search_sale);

        String imageUrl = actSearch_datas.get(position).goods_image_url;
        APP.getApp().getImageLoader().displayImage(imageUrl,ivSearchGoods);
        tvSearchName.setText(actSearch_datas.get(position).goods_name);
        tvSearchPice.setText("¥ " + actSearch_datas.get(position).goods_price);
        tvSearchSale.setText("销量 " + actSearch_datas.get(position).goods_salenum);

        return convertView;
    }
}
