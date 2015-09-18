package com.wjhgw.business.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wjhgw.base.BaseRequest;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.data.goods_list_data;
import com.wjhgw.business.manager.goods_list_manager;
import com.wjhgw.config.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Goods_Request extends BaseRequest {

	public ArrayList<goods_list_data> goodsList = new ArrayList<>();
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
		final goods_list_manager list = new goods_list_manager(mContext);
		if(!super.checkNetworkAvailable(mContext)){
			//list.delete();
			ArrayList<goods_list_data> data = list.query();
			if (null != data && data.size() > 0) {
				goodsList.clear();
				goodsList.addAll(data);
				try {
					OnMessageResponse(ApiInterface.Goods_list, "", null);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}else{
		mRequestQueue = Volley.newRequestQueue(mContext);
		Response.Listener<String> SuccessfulResponse = new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				list.add(response);
				try {
					ArrayList<goods_list_data> data = list.data;
					if (null != data && data.size() > 0) {
						goodsList.clear();
						goodsList.addAll(data);
					}
					OnMessageResponse(ApiInterface.Goods_list, response, new JSONObject(new JSONObject(response).getString("status")));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		stringRequest = new StringRequest(Request.Method.POST, BaseQuery.serviceUrl()+ ApiInterface.Goods_list, SuccessfulResponse, FailureResponse) {
			// 携带参数
			@Override
			protected HashMap<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> hashMap = new HashMap<>();
				return hashMap;
			}

		};
		mRequestQueue.add(stringRequest);

	}
	}
}