package com.wjhgw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.wjhgw.business.api.Address_del_Request;
import com.wjhgw.business.bean.Address_list;
import com.wjhgw.business.bean.address_data;
import com.wjhgw.business.response.BusinessResponse;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.manage_address_adapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 收货地址管理—地址列表
 */
public class M2_Manage_Address_Activity extends BaseActivity implements BusinessResponse, AdapterView.OnItemClickListener, XListView.IXListViewListener, View.OnClickListener {

    private MyListView mListView;
    private manage_address_adapter listAdapter;
    private String key;
    private Address_del_Request Request;
    private List<address_data> address_list_data = new ArrayList<>();
    private TextView tvAddAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m2_manage_address_item);
        mListView = (MyListView) findViewById(R.id.home_listview1);
        key = getKey();
        //mListView.setVerticalScrollBarEnabled(true);

        Request = new Address_del_Request(this);
        Request.addResponseListener(this);

        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
        mListView.setAdapter(null);
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("地址管理");
    }

    @Override
    public void onFindViews() {
        tvAddAddress = (TextView) findViewById(R.id.tv_add_address);

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        tvAddAddress.setOnClickListener(this);
    }

    /**
     * 请求地址列表
     */
    public void load_Address_list() {

        RequestParams params = new RequestParams();
        if (!key.equals("0")) {
            params.addBodyParameter("key", key);
        }
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Address_list, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Address_list address_list = gson.fromJson(responseInfo.result, Address_list.class);

                    if (address_list.status.code == 10000) {
                        address_list_data.clear();
                        if (address_list.datas != null) {
                            address_list_data.addAll(address_list.datas);
                            listAdapter = new manage_address_adapter(M2_Manage_Address_Activity.this, address_list_data, Request, key);
                            mListView.setAdapter(listAdapter);
                        }
                    } else if (address_list.status.code == 200103 || address_list.status.code == 200104) {
                        showToastShort(address_list.status.msg);
                        getSharedPreferences("key", MODE_APPEND).edit().putString("key", "0").commit();
                        startActivity(new Intent(M2_Manage_Address_Activity.this, A0_LoginActivity.class));
                    } else {
                        showToastShort(address_list.status.msg);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onRefresh(int id) {

    }

    @Override
    public void onLoadMore(int id) {

    }

    @Override
    public void OnMessageResponse(String url, String response, JSONObject status) throws JSONException {
        if (url.equals(BaseQuery.serviceUrl() + ApiInterface.Address_del)) {
            //刷新列表
            load_Address_list();
        } else if (url.equals(BaseQuery.serviceUrl() + ApiInterface.Set_add_def)) {
            showToastShort("设置默认地址成功");
            //刷新列表
            load_Address_list();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * 请求地址列表
         */
        load_Address_list();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_add_address:

                Intent intent = new Intent(this, M2_AddressDetailActvity.class);
//                intent.putExtra("addAddress", "addAddress");
                intent.setType("addAddress");
                startActivity(intent);

                break;

            default:
                break;
        }
    }
}
