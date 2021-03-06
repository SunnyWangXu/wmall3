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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjhgw.R;
import com.wjhgw.business.api.Address_del_Request;
import com.wjhgw.business.bean.address_data;
import com.wjhgw.ui.activity.M2_AddressDetailActvity;
import com.wjhgw.ui.dialog.MyDialog;

import java.util.List;

/**
 * 收货地址适配器
 */
public class manage_address_adapter extends BaseAdapter {
    private Context mContext;
    private List<address_data> Address_list_data;
    private LayoutInflater mInflater;
    private Address_del_Request Request;
    private String key;
    private MyDialog mDialog;

    public manage_address_adapter(Context c, List<address_data> address_list_data, Address_del_Request request, String Key) {
        mInflater = LayoutInflater.from(c);
        this.mContext = c;
        this.Address_list_data = address_list_data;
        this.Request = request;
        this.key = Key;
    }

    @Override
    public int getCount() {
        return Address_list_data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View cellView, ViewGroup parent) {
        cellView = mInflater.inflate(R.layout.manage_address_layout, null);
        LinearLayout ll_blank = (LinearLayout) cellView.findViewById(R.id.ll_blank);
        LinearLayout ll_edit = (LinearLayout) cellView.findViewById(R.id.ll_edit);
        LinearLayout ll_delete1 = (LinearLayout) cellView.findViewById(R.id.ll_delete1);
        final ImageView iv_blank = (ImageView) cellView.findViewById(R.id.iv_blank);
        TextView true_name = (TextView) cellView.findViewById(R.id.tv_true_name);
        TextView address = (TextView) cellView.findViewById(R.id.tv_address);
        TextView mob_phone = (TextView) cellView.findViewById(R.id.tv_mob_phone);

        true_name.setText(Address_list_data.get(position).true_name);
        address.setText(Address_list_data.get(position).area_info + Address_list_data.get(position).address);
        mob_phone.setText(Address_list_data.get(position).mob_phone);
        if (Address_list_data.get(position).is_default.equals("1")) {
            iv_blank.setImageResource(R.mipmap.ic_order_select);
        }
        ll_blank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request.Set_add_def(Address_list_data.get(position).address_id, key);
                iv_blank.setImageResource(R.mipmap.ic_order_select);
            }
        });
        ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, M2_AddressDetailActvity.class);
                String name = Address_list_data.get(position).true_name;
                String phone = Address_list_data.get(position).mob_phone;
                String info = Address_list_data.get(position).area_info;
                String addressId = Address_list_data.get(position).address_id;
                String addressDetail = Address_list_data.get(position).address;

                intent.putExtra("addressId", addressId);
                intent.putExtra("name", name);
                intent.putExtra("phone", phone);
                intent.putExtra("info", info);
                intent.putExtra("addressDetail", addressDetail);
                mContext.startActivity(intent);
            }
        });
        ll_delete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog = new MyDialog(mContext, "删除后不可找回");
                mDialog.show();
                mDialog.positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Request.Address_del(Address_list_data.get(position).address_id, key);
                        mDialog.dismiss();
                    }
                });
                mDialog.negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
            }
        });

        return cellView;
    }
}
