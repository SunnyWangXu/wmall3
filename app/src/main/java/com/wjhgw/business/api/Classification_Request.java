package com.wjhgw.business.api;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wjhgw.base.BaseRequest;
import com.wjhgw.business.analytical.ClassAnalytical;
import com.wjhgw.business.data.goods_class_data;
import com.wjhgw.business.data.goods_class_data1;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Classification_Request extends BaseRequest {

    /**
     * class_List 一级分类数据列表
     */
    public ArrayList<goods_class_data> class_List = new ArrayList<>();
    /**
     * class_List1 制定分类数据列表
     */
    public ArrayList<goods_class_data1> class_List1 = new ArrayList<>();
    private RequestQueue mRequestQueue;
    private StringRequest stringRequest;
    private String key;        //登录令牌

    public Classification_Request(Context context) {
        super(context);
        this.mContext = context;
    }

    /**
     * 请求错误回调
     */
    Response.ErrorListener FailureResponse = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(mContext, "网络请求错误！", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 商品一级分类
     */
    public void goods_class(final HashMap<String, String> mhashMap, final String Route) {
        if (!super.checkNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "网络错误！", Toast.LENGTH_SHORT).show();
        } else {
            mRequestQueue = Volley.newRequestQueue(mContext);
            Response.Listener<String> SuccessfulResponse = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    final ClassAnalytical list = new ClassAnalytical();
                    try {
                        ArrayList<goods_class_data> data = list.fromJson(response, goods_class_data.class);
                        if (null != data && data.size() > 0) {
                            class_List.clear();
                            class_List.addAll(data);
                        }
                        OnMessageResponse(Route, response, new JSONObject(new JSONObject(response).getString("status")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            stringRequest = new StringRequest(Request.Method.POST, Route, SuccessfulResponse, FailureResponse) {
                // 携带参数
                @Override
                protected HashMap<String, String> getParams() throws AuthFailureError {
                    return mhashMap;
                }

            };
            mRequestQueue.add(stringRequest);
        }
    }

    /**
     * 指定分类
     */
    public void goods_class1(final HashMap<String, String> mhashMap, final String Route) {
        if (!super.checkNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "网络错误！", Toast.LENGTH_SHORT).show();
        } else {
            mRequestQueue = Volley.newRequestQueue(mContext);
            Response.Listener<String> SuccessfulResponse = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    final ClassAnalytical list = new ClassAnalytical();
                    try {
                        ArrayList<goods_class_data1> data = list.fromJson(response, goods_class_data1.class);
                        if (null != data && data.size() > 0) {
                            class_List1.clear();
                            class_List1.addAll(data);
                        }
                        OnMessageResponse(Route, response, new JSONObject(new JSONObject(response).getString("status")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            stringRequest = new StringRequest(Request.Method.POST, Route, SuccessfulResponse, FailureResponse) {
                // 携带参数
                @Override
                protected HashMap<String, String> getParams() throws AuthFailureError {
                    return mhashMap;
                }

            };
            mRequestQueue.add(stringRequest);
        }
    }
}