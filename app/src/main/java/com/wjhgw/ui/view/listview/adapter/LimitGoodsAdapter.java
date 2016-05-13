package com.wjhgw.ui.view.listview.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.business.bean.LimitGoodsInfo;

import java.util.ArrayList;

/**
 * 限时抢购商品列表的适配器
 */
public class LimitGoodsAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<LimitGoodsInfo> goodsInfo;
    private ImageView goodsImg;
    private TextView tvDiscount;
    private TextView tvName;
    private TextView tvGoodsPrice;
    private TextView tvXianshiPrice;

    public LimitGoodsAdapter(Context mContext, ArrayList<LimitGoodsInfo> goodsInfo) {
        this.mContext = mContext;
        this.goodsInfo = goodsInfo;
    }

    @Override
    public int getCount() {
        return goodsInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(R.layout.limit_goods_item, null);
        goodsImg = (ImageView) convertView.findViewById(R.id.iv_limit_goods);
        tvDiscount = (TextView) convertView.findViewById(R.id.tv_limit_discount);
        tvName = (TextView) convertView.findViewById(R.id.tv_limit_name);
        tvGoodsPrice = (TextView) convertView.findViewById(R.id.tv_goods_price);
        tvXianshiPrice = (TextView) convertView.findViewById(R.id.tv_xianshi_price);

        APP.getApp().getImageLoader().displayImage(goodsInfo.get(position).image_url, goodsImg, APP.getApp().getImageOptions());

        String discount = goodsInfo.get(position).xianshi_discount;
        String discount1 = discount.replace("折", "");
        tvDiscount.setText(discount1);
        tvName.setText(goodsInfo.get(position).goods_name);
        tvGoodsPrice.setText(goodsInfo.get(position).goods_price);
        tvXianshiPrice.setText(goodsInfo.get(position).xianshi_price);
        tvGoodsPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        return convertView;
    }
}
