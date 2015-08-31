package com.wjhgw.business.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wjhgw.base.BaseModel;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.controller.goods_list_controller;
import com.wjhgw.business.data.goods_list_data;
import com.wjhgw.config.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Goods_Request extends BaseModel {

	public ArrayList<goods_list_data> simplegoodsList = new ArrayList<>();
	private RequestQueue mRequestQueue;
	private StringRequest stringRequest;
	public Goods_Request(Context context) {
		super(context);
		this.mContext = context;
	}

	/**
	 * 请求错误回调
	 */
	Response.ErrorListener FailureResponse = new Response.ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			System.out.println("请求错误:" + error.toString());
		}
	};

	/**
	 * 商品列表接口
	 */
	public void goods_list() {
		mRequestQueue = Volley.newRequestQueue(mContext);
		Response.Listener<String> SuccessfulResponse = new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					goods_list_controller list = new goods_list_controller(mContext);
					list.add(response);
					OnMessageResponse(ApiInterface.Goods_list, response, new JSONObject(new JSONObject(response.toString()).getString("status").toString()));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		stringRequest = new StringRequest(Request.Method.POST, BaseQuery.serviceUrl()+ ApiInterface.Goods_list, SuccessfulResponse, FailureResponse) {
			// 携带参数
			@Override
			protected HashMap<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> hashMap = new HashMap<String, String>();
				return hashMap;
			}

		};
		mRequestQueue.add(stringRequest);

	}
}