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
import com.wjhgw.business.bean.Goods_list;

import java.util.ArrayList;

/**
 * 我的收藏ListView适配器
 */
public class MyCollectAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<Goods_list> goodsList;

    public MyCollectAdapter(Context mContext, ArrayList<Goods_list> goodsList) {
        this.mContext = mContext;
        this.goodsList = goodsList;


    }

    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;

        if (null == convertView) {
            holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.my_collect_lv_item, null);

            holder.ivGoodImage = (ImageView) convertView.findViewById(R.id.iv_mycollect_goods);
            holder.tvGoodStatus = (TextView) convertView.findViewById(R.id.tv_mycollect_status);
            holder.tvGoodName = (TextView) convertView.findViewById(R.id.tv_mycollect_goodsname);
            holder.tvGoodPrice = (TextView) convertView.findViewById(R.id.tv_mycollect_goods_price);
            holder.tvGoodSalenum = (TextView) convertView.findViewById(R.id.tv_mycollect_goods_salenum);

            convertView.setTag(holder);
        }
        holder = (Holder) convertView.getTag();

        APP.getApp().getImageLoader().displayImage(goodsList.get(position).goods_image_url, holder.ivGoodImage);
        if (!goodsList.get(position).goods_state.equals("1") || goodsList.get(position).goods_storage.equals("0")) {

            holder.tvGoodStatus.setVisibility(View.VISIBLE);
            if (goodsList.get(position).goods_state.equals("0") || goodsList.get(position).goods_state.equals("10")) {
                holder.tvGoodStatus.setText("下架");
            }
            if (goodsList.get(position).goods_state.equals("1") && goodsList.get(position).goods_storage.equals("0")) {
                holder.tvGoodStatus.setText("无货");
            }

        } else {
            holder.tvGoodStatus.setVisibility(View.GONE);
        }

        holder.tvGoodName.setText(goodsList.get(position).goods_name);
        holder.tvGoodPrice.setText("¥" + goodsList.get(position).goods_price);
        holder.tvGoodSalenum.setText("售出：" + goodsList.get(position).goods_salenum + "件");

        return convertView;
    }

    public static class Holder {
        public ImageView ivGoodImage;
        public TextView tvGoodStatus;
        public TextView tvGoodName;
        public TextView tvGoodPrice;
        public TextView tvGoodSalenum;

    }
}
