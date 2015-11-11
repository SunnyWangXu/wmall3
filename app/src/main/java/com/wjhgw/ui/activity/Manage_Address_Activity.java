package com.wjhgw.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.manage_address_adapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Manage_Address_Activity extends BaseActivity implements BusinessResponse, AdapterView.OnItemClickListener, XListView.IXListViewListener {

    private ListView mListView;
    private manage_address_adapter listAdapter;
    private String key;
    private Address_del_Request Request;
    private List<address_data> address_list_data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_address_item);
        mListView = (ListView) findViewById(R.id.home_listview1);
        key = getKey();
        //mListView.setVerticalScrollBarEnabled(true);
        Address_list();

        Request = new Address_del_Request(this);
        Request.addResponseListener(this);
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("收货地址管理");
    }

    @Override
    public void onFindViews() {

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {

    }

    /**
     * 地址列表
     */
    public void Address_list() {
        RequestParams params = new RequestParams();
        if (!key.equals("0")) {
            params.addBodyParameter("key", "de4c81c7ef206bd5dbd825520b97468f");
        }
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Address_list, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Address_list address_list = gson.fromJson(responseInfo.result, Address_list.class);

                    if (address_list.status.code == 10000) {
                        address_list_data.clear();
                        if (address_list.datas.size() != 0) {
                            address_list_data.addAll(address_list.datas);
                            listAdapter = new manage_address_adapter(Manage_Address_Activity.this, address_list_data, Request, key);
                            mListView.setAdapter(listAdapter);
                            Toast.makeText(Manage_Address_Activity.this, address_list.status.msg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(Manage_Address_Activity.this, address_list.status.msg, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(Manage_Address_Activity.this, "失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(Manage_Address_Activity.this, "hdskgja", Toast.LENGTH_SHORT).show();
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
            Address_list();
        } else if (url.equals(BaseQuery.serviceUrl() + ApiInterface.Set_add_def)) {
            //刷新列表
            Address_list();
        }
    }
}
