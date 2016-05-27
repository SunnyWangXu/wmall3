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
import android.widget.TextView;

import com.wjhgw.R;
import com.wjhgw.business.bean.Nearby_stores_list_data;
import com.wjhgw.ui.activity.F2_StoreGoodsActivity;

import java.util.ArrayList;

/**
 * 附件店铺内商品
 */
public class F0_Shop_nearbvAdapter extends BaseAdapter {
    Context c;
    public ArrayList<Nearby_stores_list_data> List;
    private LayoutInflater mInflater;

    public F0_Shop_nearbvAdapter(Context c, ArrayList<Nearby_stores_list_data> dataList) {
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
        cellView = mInflater.inflate(R.layout.f0_listview_item, null);
        TextView tv_f0_store_name = (TextView) cellView.findViewById(R.id.tv_f0_store_name);
        TextView tv_f0_address = (TextView) cellView.findViewById(R.id.tv_f0_address);
        TextView tv_f0_distance = (TextView) cellView.findViewById(R.id.tv_f0_distance);

        tv_f0_store_name.setText(List.get(position).store_name);
        tv_f0_address.setText(List.get(position).address);
        tv_f0_distance.setText(List.get(position).distance+"km");

        cellView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = "";
                if(!List.get(position).mobile.equals("")){
                    mobile = List.get(position).mobile;
                }else {
                    mobile = List.get(position).tel;
                }
                Intent intent = new Intent(c, F2_StoreGoodsActivity.class);
                intent.putExtra("name",List.get(position).store_name);
                intent.putExtra("mobile",mobile);
                intent.putExtra("address",List.get(position).address);
                c.startActivity(intent);
            }
        });
        return cellView;
    }
}
