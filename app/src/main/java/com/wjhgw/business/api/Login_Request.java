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
import com.wjhgw.business.bean.goods_list_data;
import com.wjhgw.business.bean.login_data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Login_Request extends BaseRequest {

	public ArrayList<goods_list_data> goodsList = new ArrayList<>();
	public login_data data;
	private RequestQueue mRequestQueue;
	private StringRequest stringRequest;
	public Login_Request(Context context) {
		super(context);
		this.mContext = context;
	}

	/**
	 * 请求错误回调
	 */
	Response.ErrorListener FailureResponse = new Response.ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			Toast.makeText(mContext, "网络错误！", Toast.LENGTH_SHORT).show();
		}
	};

	/**
	 * 商品列表接口
	 */
	public void login(final HashMap<String, String> mhashMap, final String Route) {
		if (!super.checkNetworkAvailable(mContext)) {
			Toast.makeText(mContext, "网络错误！", Toast.LENGTH_SHORT).show();
		} else {
			if(mRequestQueue == null){
				mRequestQueue = Volley.newRequestQueue(mContext);
			}
			Response.Listener<String> SuccessfulResponse = new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					final ClassAnalytical list = new ClassAnalytical();
					try {
						data = list.fromJsonObject(response, login_data.class);
						data.getKey();
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