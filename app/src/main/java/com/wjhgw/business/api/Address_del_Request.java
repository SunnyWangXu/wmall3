package com.wjhgw.business.api;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjhgw.APP;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.base.BaseRequest;
import com.wjhgw.business.bean.Status;
import com.wjhgw.config.ApiInterface;

import org.json.JSONException;

/**
 * 删除和默认收货地址
 */
public class Address_del_Request extends BaseRequest {
    private RequestQueue mRequestQueue;
    private StringRequest stringRequest;
    public static volatile String cookies;

    public Address_del_Request(Context context) {
        super(context);
        this.mContext = context;
    }

    /**
     * 删除地址
     */
    public void Address_del(String address_id, String key) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("address_id", address_id);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Address_del, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);

                    if (status.status.code == 10000) {
                        try {
                            OnMessageResponse(BaseQuery.serviceUrl() + ApiInterface.Address_del, responseInfo.result, null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                        Toast.makeText(mContext, status.status.msg, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mContext, status.status.msg, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mContext, "失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 设置默认地址
     */
    public void Set_add_def(String address_id, String key) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("address_id", address_id);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Set_add_def, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);

                    if (status.status.code == 10000) {
                        try {
                            OnMessageResponse(BaseQuery.serviceUrl() + ApiInterface.Set_add_def, responseInfo.result, null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mContext, "失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}