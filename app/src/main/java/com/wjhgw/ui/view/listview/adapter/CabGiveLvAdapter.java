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
import com.wjhgw.business.bean.CadList_data;

import java.util.List;

/**
 * 选中赠送的ListView的适配器
 */
public class CabGiveLvAdapter extends BaseAdapter {

    private Context context;
    private List<CadList_data> datas1;
    private ImageView ivGiveImage;
    private TextView ivGiveGoodsname;
    private TextView ivGiveGoodsnum;


    public CabGiveLvAdapter(Context context, List<CadList_data> datas1) {
        this.datas1 = datas1;
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas1.size();
    }

    @Override
    public Object getItem(int position) {
        return datas1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.cabgivelv_item, null);
        ivGiveImage = (ImageView) convertView.findViewById(R.id.iv_give_image);
        ivGiveGoodsname = (TextView) convertView.findViewById(R.id.tv_give_goodsname);
        ivGiveGoodsnum = (TextView) convertView.findViewById(R.id.tv_give_goodsnum);

        ivGiveGoodsname.setText(datas1.get(position).goods_name);
        ivGiveGoodsnum.setText("x" + datas1.get(position).num );

        APP.getApp().getImageLoader().displayImage(datas1.get(position).goods_image, ivGiveImage);

        return convertView;
    }
}
