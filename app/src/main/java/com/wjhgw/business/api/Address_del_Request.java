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
 * 删除和默认收货地址
 */
public class Address_del_Request extends BaseRequest {
    private RequestQueue mRequestQueue;
    private StringRequest stringRequest;
    public static volatile String cookies;
    private LoadDialog Dialog;

    public Address_del_Request(Context context) {
        super(context);
        this.mContext = context;
        Dialog = new LoadDialog(mContext);
    }

    /**
     * 删除地址
     */
    public void Address_del(String address_id, String key) {
        Dialog.ProgressDialog();
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
                            Dialog.dismiss();
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
     * 设置默认地址
     */
    public void Set_add_def(String address_id, String key) {
        Dialog.ProgressDialog();
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
                            Dialog.dismiss();
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
     * 购物车修改数量接口
     */
    public void cart_edit_quantity(String cart_id, String quantity, String key) {
        Dialog.ProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("cart_id", cart_id);
        params.addBodyParameter("quantity", quantity);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Cart_edit_quantity, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo.result != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);
                    if (status.status.code == 10000) {
                        try {
                            OnMessageResponse(BaseQuery.serviceUrl() + ApiInterface.Cart_edit_quantity, responseInfo.result, null);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if(status.status.code == 200103 || status.status.code == 200104){
                        Toast.makeText(mContext, "登录超时或未登录", Toast.LENGTH_SHORT).show();
                        mContext.getSharedPreferences("key", mContext.MODE_APPEND).edit().putString("key","0").commit();
                        mContext.startActivity(new Intent(mContext, A0_LoginActivity.class));
                    }else {
                        Toast.makeText(mContext, status.status.msg, Toast.LENGTH_SHORT).show();
                    }
                    Dialog.dismiss();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 商品收藏
     */
    public void favorites_add(String goods_id, String key) {
        Dialog.ProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("fav_id", goods_id);
        params.addBodyParameter("fav_type", "goods");

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Favorites_add, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dialog.dismiss();
                Gson gson = new Gson();
                if (responseInfo.result != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);
                    if (status.status.code == 10000) {
                        try {
                            OnMessageResponse(BaseQuery.serviceUrl() + ApiInterface.Favorites_add, responseInfo.result, null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if(status.status.code == 200103 || status.status.code == 200104){
                        Toast.makeText(mContext, "登录超时或未登录", Toast.LENGTH_SHORT).show();
                        mContext.getSharedPreferences("key", mContext.MODE_APPEND).edit().putString("key","0").commit();
                        mContext.startActivity(new Intent(mContext, A0_LoginActivity.class));
                    }else {
                        Toast.makeText(mContext, status.status.msg, Toast.LENGTH_SHORT).show();
                    }
                    Dialog.dismiss();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 购物车删除接口
     */
    public void cart_del(String cart_id, String key) {
        Dialog.ProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("cart_id", cart_id);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Cart_del, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dialog.dismiss();
                Gson gson = new Gson();
                if (responseInfo.result != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);
                    if (status.status.code == 10000) {
                        try {
                            OnMessageResponse(BaseQuery.serviceUrl() + ApiInterface.Cart_del, responseInfo.result, null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if(status.status.code == 200103 || status.status.code == 200104){
                        Toast.makeText(mContext, "登录超时或未登录", Toast.LENGTH_SHORT).show();
                        mContext.getSharedPreferences("key", mContext.MODE_APPEND).edit().putString("key","0").commit();
                        mContext.startActivity(new Intent(mContext, A0_LoginActivity.class));
                    }else {
                        Toast.makeText(mContext, status.status.msg, Toast.LENGTH_SHORT).show();
                    }
                    Dialog.dismiss();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 添加购物接口
     */
    public void cart_add(String goods_id, String key) {
        Dialog.ProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("goods_id", goods_id);
        params.addBodyParameter("quantity", "1");

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Cart_add, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dialog.dismiss();
                Gson gson = new Gson();
                if (responseInfo.result != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);
                    if (status.status.code == 10000) {
                        try {
                            OnMessageResponse(BaseQuery.serviceUrl() + ApiInterface.Cart_add, responseInfo.result, null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if(status.status.code == 200103 || status.status.code == 200104){
                        Toast.makeText(mContext, "登录超时或未登录", Toast.LENGTH_SHORT).show();
                        mContext.getSharedPreferences("key", mContext.MODE_APPEND).edit().putString("key","0").commit();
                        mContext.startActivity(new Intent(mContext, A0_LoginActivity.class));
                    }else {
                        Toast.makeText(mContext, status.status.msg, Toast.LENGTH_SHORT).show();
                    }
                    Dialog.dismiss();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}