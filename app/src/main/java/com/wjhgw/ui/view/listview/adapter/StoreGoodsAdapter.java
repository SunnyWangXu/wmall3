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
import com.wjhgw.business.bean.StoreGoodsDatas;

import java.util.ArrayList;

/**
 * 附近店铺内商品详情适配器
 */
public class StoreGoodsAdapter extends BaseAdapter {
    private Context mContext;
    public ArrayList<StoreGoodsDatas> storeDatas;
    private ImageView goodsImage;
    private TextView goodsName;
    private TextView goodsDec;
    private TextView goodsPrice;

    public StoreGoodsAdapter(Context mContext, ArrayList<StoreGoodsDatas> storeDatas) {
        this.mContext = mContext;
        this.storeDatas = storeDatas;
    }


    @Override
    public int getCount() {
        return storeDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return storeDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.store_goods_item, null);
        goodsImage = (ImageView) convertView.findViewById(R.id.iv_store_goods_image);
        goodsName = (TextView) convertView.findViewById(R.id.tv_store_goods_name);
        goodsDec = (TextView) convertView.findViewById(R.id.tv_store_goods_dec);
        goodsPrice = (TextView) convertView.findViewById(R.id.tv_store_shop_price);

        APP.getApp().getImageLoader().displayImage(storeDatas.get(position).goods_image, goodsImage);
        goodsName.setText(storeDatas.get(position).goods_name);
        goodsDec.setText(storeDatas.get(position).goods_dec);
        goodsPrice.setText(storeDatas.get(position).shop_price);
        return convertView;
    }
}
