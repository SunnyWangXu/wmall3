package com.wjhgw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.bean.address_data;
import com.wjhgw.business.bean.Address_list;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.view.listview.adapter.OrderAddressAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单收货地址
 */
public class S1_OrderAddressActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private List<address_data> order_address_list = new ArrayList<>();
    private ListView lvOrderAddress;
    private TextView tvAddAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_address);

    }

    @Override
    public void onInit() {
        setUp();
        setTitle("收货地址");

    }

    @Override
    public void onFindViews() {
        lvOrderAddress = (ListView) findViewById(R.id.lv_order_addresss);
        tvAddAddress = (TextView) findViewById(R.id.tv_order_add_address);

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        lvOrderAddress.setOnItemClickListener(this);
        tvAddAddress.setOnClickListener(this);

    }


    /**
     * 请求地址列表
     */
    public void load_Order_Addresst() {
        super.StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("address_type", "0");
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Address_list, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Address_list address_list = gson.fromJson(responseInfo.result, Address_list.class);
                    S1_OrderAddressActivity.super.Dismiss();
                    if (address_list.status.code == 10000) {
                        order_address_list.clear();
                        if (address_list.datas != null) {
                            order_address_list.addAll(address_list.datas);
                            lvOrderAddress.setAdapter(new OrderAddressAdapter(S1_OrderAddressActivity.this, order_address_list));
                        }
                    } else {
                        overtime(address_list.status.code, address_list.status.msg);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

                showToastShort("请求失败");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_order_add_address:

                Intent intent = new Intent(this, M2_AddressDetailActvity.class);
                intent.setType("addAddress");
                startActivity(intent);

                break;

            default:
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 请求地址列表
         */
        load_Order_Addresst();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent();
        intent.putExtra("tureName", order_address_list.get(position).true_name);
        intent.putExtra("phone", order_address_list.get(position).mob_phone);
        intent.putExtra("addressInfo", order_address_list.get(position).area_info + " " + order_address_list.get(position).address);
        intent.putExtra("addressId", order_address_list.get(position).address_id);

        setResult(55555, intent);
        finish(false);
    }
}
