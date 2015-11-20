package com.wjhgw.ui.view.listview.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.wjhgw.R;
import com.wjhgw.business.bean.Goods_attr_data;
import com.wjhgw.business.bean.Goods_attr_data_value;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类属性适配器
 */
public class AttrAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Goods_attr_data> data;
    private ViewHolder vh = new ViewHolder();
    private TextView tvAttrCiecle;

    private static class ViewHolder {
        private TextView text;
    }

    public AttrAdapter(Context c, ArrayList<Goods_attr_data> data) {
        this.mContext = c;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public Object getItem(int position) {

        return data.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView goodsName;
        GridView goodsValueGV;
        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.goods_attr_value, null);
            goodsName = (TextView) convertView.findViewById(R.id.tv_goods_attr_name);
            goodsName.setText(data.get(position).name);

            goodsValueGV = (GridView) convertView.findViewById(R.id.gv_goods_attr_value);

            tvAttrCiecle = (TextView) convertView.findViewById(R.id.tv_attr_circle);

         if((position % 5) == 1){
             tvAttrCiecle.setBackgroundResource(R.drawable.attr_rose_shape);
            }else if((position % 5) == 2){
             tvAttrCiecle.setBackgroundResource(R.drawable.attr_purple_shape);
            }else if((position % 5) == 3){
             tvAttrCiecle.setBackgroundResource(R.drawable.attr_blue_shape);
            }else if((position % 5) == 4){
             tvAttrCiecle.setBackgroundResource(R.drawable.attr_yellow_shape);
            }

            //如果是第一条 热门品牌查询的字段不一样 专门设置适配器，其他的设置另外的适配器
            if (position == 0) {
                List<Goods_attr_data_value> dataValues = data.get(position).value;
                AttrValueGVAdapter1 attrValueGVAdapter1 = new AttrValueGVAdapter1(mContext, dataValues);
                goodsValueGV.setAdapter(attrValueGVAdapter1);
            } else {
                List<Goods_attr_data_value> dataValues = data.get(position).value;
                AttrValueGVAdapter2 attrValueGVAdapter2 = new AttrValueGVAdapter2(mContext, dataValues);
                goodsValueGV.setAdapter(attrValueGVAdapter2);
            }

        }
        return convertView;
    }

}
//    @Override
//    public View getView(int position, View attrValue, ViewGroup parent) {
//        attrValue = LayoutInflater.from(mContext).inflate(R.layout.goods_attr_value, null);
//        vh.text = (TextView) attrValue.findViewById(R.id.goods_class_item);
//        vh.text.setText(List.get(position).gc_name);
//        attrValue.setTag(vh);
//        return attrValue;
//    }
//}
