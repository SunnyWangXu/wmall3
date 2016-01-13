package com.wjhgw.business.api;

import android.content.Context;
import android.content.Intent;
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
import com.wjhgw.ui.activity.A0_LoginActivity;
import com.wjhgw.ui.dialog.LoadDialog;

import org.json.JSONException;

/**
 * 订单
 */
public class Order_Request extends BaseRequest {
    private RequestQueue mRequestQueue;
    private StringRequest stringRequest;
    public static volatile String cookies;
    private LoadDialog Dialog;

    public Order_Request(Context context) {
        super(context);
        this.mContext = context;
        Dialog = new LoadDialog(mContext);
    }

    /**
     * 订单确认收货接口
     */
    public void order_receive(String order_id, String key) {
        Dialog.ProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("order_id", order_id);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Order_receive, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);

                    if (status.status.code == 10000) {
                        try {
                            OnMessageResponse(BaseQuery.serviceUrl() + ApiInterface.Order_receive, responseInfo.result, null);
                            Dialog.dismiss();
                            Toast.makeText(mContext, status.status.msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if(status.status.code == 200103 || status.status.code == 200104){
                        Toast.makeText(mContext, "登录超时或未登录", Toast.LENGTH_SHORT).show();
                        mContext.getSharedPreferences("key", mContext.MODE_APPEND).edit().putString("key","0").commit();
                        mContext.startActivity(new Intent(mContext, A0_LoginActivity.class));
                    }else {
                        Toast.makeText(mContext, status.status.msg, Toast.LENGTH_SHORT).show();
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
     * 删除订单接口
     */
    public void order_delete(String order_id, String key) {
        Dialog.ProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("order_id", order_id);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Order_delete, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);

                    if (status.status.code == 10000) {
                        try {
                            OnMessageResponse(BaseQuery.serviceUrl() + ApiInterface.Order_delete, responseInfo.result, null);
                            Dialog.dismiss();
                            Toast.makeText(mContext, status.status.msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if(status.status.code == 200103 || status.status.code == 200104){
                        Toast.makeText(mContext, "登录超时或未登录", Toast.LENGTH_SHORT).show();
                        mContext.getSharedPreferences("key", mContext.MODE_APPEND).edit().putString("key","0").commit();
                        mContext.startActivity(new Intent(mContext, A0_LoginActivity.class));
                    }else {
                        Toast.makeText(mContext, status.status.msg, Toast.LENGTH_SHORT).show();
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
     * 催促卖家发货
     */
    public void order_remind(String order_id, String key) {
        Dialog.ProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("order_id", order_id);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Order_remind, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);

                    if (status.status.code == 10000) {
                        try {
                            OnMessageResponse(BaseQuery.serviceUrl() + ApiInterface.Order_remind, responseInfo.result, null);
                            Dialog.dismiss();
                            Toast.makeText(mContext, status.status.msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if(status.status.code == 200103 || status.status.code == 200104){
                        Toast.makeText(mContext, "登录超时或未登录", Toast.LENGTH_SHORT).show();
                        mContext.getSharedPreferences("key", mContext.MODE_APPEND).edit().putString("key","0").commit();
                        mContext.startActivity(new Intent(mContext, A0_LoginActivity.class));
                    }else {
                        Toast.makeText(mContext, status.status.msg, Toast.LENGTH_SHORT).show();
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
     * 订单取消接口
     */
    public void order_cancel(String order_id, String key, String msg) {
        Dialog.ProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("order_id", order_id);
        params.addBodyParameter("msg", msg);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Order_cancel, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);

                    if (status.status.code == 10000) {
                        try {
                            OnMessageResponse(BaseQuery.serviceUrl() + ApiInterface.Order_cancel, responseInfo.result, null);
                            Dialog.dismiss();
                            Toast.makeText(mContext, status.status.msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if(status.status.code == 200103 || status.status.code == 200104){
                        Toast.makeText(mContext, "登录超时或未登录", Toast.LENGTH_SHORT).show();
                        mContext.getSharedPreferences("key", mContext.MODE_APPEND).edit().putString("key","0").commit();
                        mContext.startActivity(new Intent(mContext, A0_LoginActivity.class));
                    }else {
                        Toast.makeText(mContext, status.status.msg, Toast.LENGTH_SHORT).show();
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