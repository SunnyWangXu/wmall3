package com.wjhgw.ui.view.listview.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjhgw.R;
import com.wjhgw.business.bean.address_data;
import com.wjhgw.ui.activity.M2_AddressDetailActvity;
import com.wjhgw.ui.activity.S1_OrderAddressActivity;

import java.util.List;

/**
 * 订单地址适配器
 */
public class OrderAddressAdapter extends BaseAdapter {
    private Context mContext;
    private List<address_data> data;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvAddressInfo;
    private ImageView ivEditAddress;
    private ImageView ivSelectAddress;

    public OrderAddressAdapter(S1_OrderAddressActivity mContext, List<address_data> order_address_list) {
        this.mContext = mContext;
        this.data = order_address_list;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(R.layout.order_address_list_item, null);

        tvName = (TextView) convertView.findViewById(R.id.tv_order_address_name);
        tvPhone = (TextView) convertView.findViewById(R.id.tv_order_phone);
        tvAddressInfo = (TextView) convertView.findViewById(R.id.tv_order_address_info);
        ivEditAddress = (ImageView) convertView.findViewById(R.id.iv_order_address_edit);
        ivSelectAddress = (ImageView) convertView.findViewById(R.id.iv_order_address_select);

        tvName.setText(data.get(position).true_name);
        tvPhone.setText(data.get(position).mob_phone);
        tvAddressInfo.setText(data.get(position).area_info + " " +  data.get(position).address);

        if (data.get(position).is_default.equals("1")) {
            ivSelectAddress.setVisibility(View.VISIBLE);
        }

        ivEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, M2_AddressDetailActvity.class);

                String name = data.get(position).true_name;
                String phone = data.get(position).mob_phone;
                String info = data.get(position).area_info;
                String addressId = data.get(position).address_id;
                String addressDetail = data.get(position).address;

                intent.putExtra("addressId", addressId);
                intent.putExtra("name", name);
                intent.putExtra("phone", phone);
                intent.putExtra("info", info);
                intent.putExtra("addressDetail", addressDetail);

                mContext.startActivity(intent);

            }
        });

        return convertView;
    }
}
