package com.wjhgw.ui.view.listview.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.bean.Goods_attr_data_value;
import com.wjhgw.config.ApiInterface;

import java.util.List;

/**
 * 商品分类属性 GridView 第一条热门品牌的适配器
 */
public class AttrValueGVAdapter1 extends BaseAdapter {


    private Context mContext;
    private List<Goods_attr_data_value> dataValues;
    private String brandId;

    public AttrValueGVAdapter1(Context mContext, List<Goods_attr_data_value> dataValues) {
        this.mContext = mContext;
        this.dataValues = dataValues;

    }

    @Override
    public int getCount() {
        return dataValues.size();
    }

    @Override
    public Object getItem(int position) {
        return dataValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.attr_gv_item, null);
        TextView tvValue = (TextView) convertView.findViewById(R.id.tv_attr_value);
        tvValue.setText(dataValues.get(position).brand_name);
        brandId = dataValues.get(position).brand_id;

        tvValue.setClickable(true);
        tvValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams();
                params.addBodyParameter("b_id", brandId);
                params.addBodyParameter("curpage", "1");
                params.addBodyParameter("page", "10");

                APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Act_search, params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        Log.e("responseInfo", responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                    }
                });

            }
        });

        return convertView;
    }
}
