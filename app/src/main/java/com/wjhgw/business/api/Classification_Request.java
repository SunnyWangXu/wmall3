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
import com.wjhgw.base.BaseModel;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.analytical.ClassAnalytical;
import com.wjhgw.business.data.goods_class_data;
import com.wjhgw.config.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Classification_Request extends BaseModel {

	public ArrayList<goods_class_data> class_List = new ArrayList<>();
	private RequestQueue mRequestQueue;
	private StringRequest stringRequest;
	private String key;		//登录令牌
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
			System.out.println("请求错误:" + error.toString());
		}
	};

	/**
	 * 商品列表接口
	 */
	public void goods_class(String Key) {
		this.key = Key;
		if(!super.checkNetworkAvailable(mContext)){
			Toast.makeText(mContext, "网络错误！", Toast.LENGTH_SHORT).show();
		}else{
		mRequestQueue = Volley.newRequestQueue(mContext);
		Response.Listener<String> SuccessfulResponse = new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				final ClassAnalytical list = new ClassAnalytical();
				try {
					list.fromJson(response);
					ArrayList<goods_class_data> data = list.data;
					if (null != data && data.size() > 0) {
						class_List.clear();
						class_List.addAll(data);
					}
					OnMessageResponse(ApiInterface.Goods_class, response, new JSONObject(new JSONObject(response).getString("status")));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		stringRequest = new StringRequest(Request.Method.POST, BaseQuery.serviceUrl()+ ApiInterface.Goods_class, SuccessfulResponse, FailureResponse) {
			// 携带参数
			@Override
			protected HashMap<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> hashMap = new HashMap<>();
				hashMap.put("key", key);
				return hashMap;
			}

		};
		mRequestQueue.add(stringRequest);

	}
	}
}