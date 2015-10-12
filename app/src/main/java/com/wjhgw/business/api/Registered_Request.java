package com.wjhgw.business.api;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.base.BaseRequest;
import com.wjhgw.config.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册接口请求
 */
public class Registered_Request extends BaseRequest {
	private RequestQueue mRequestQueue;
	private StringRequest stringRequest;
	public static volatile String cookies;
	public Registered_Request(Context context) {
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
	 * 验证手机号码是否正确
	 *
	 * @param number
	 *            手机号
	 */
	public void VerificationNumber(final String number) {
		if (!super.checkNetworkAvailable(mContext)) {
			Toast.makeText(mContext, "网络错误！", Toast.LENGTH_SHORT).show();
		} else {
			if(mRequestQueue == null){
				mRequestQueue = Volley.newRequestQueue(mContext);
			}
			Response.Listener<String> SuccessfulResponse = new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					try {
						if (response == null || "".equals(response) || response.indexOf("{") < 0
									|| new JSONObject(response.substring(response.indexOf("{"))).length() == 0) {
								Toast.makeText(mContext, "手机号码不正确！！", Toast.LENGTH_SHORT).show();
								return;
						} else if (new JSONObject(response.substring(response.indexOf("{"))).getString("catName").length() > 0) {
							HashMap<String, String> hashMap = new HashMap<>();
							hashMap.put("member_mobile", number);
							registered(hashMap, BaseQuery.serviceUrl() + ApiInterface.VerificationRegistered);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			stringRequest = new StringRequest(Request.Method.POST, ApiInterface.Number + number, SuccessfulResponse, FailureResponse) ;
			mRequestQueue.add(stringRequest);
		}

	}

	/**
	 * 验证手机号码是否已注册
	 */
	public void registered(final HashMap<String, String> mhashMap, final String Route) {
		if (!super.checkNetworkAvailable(mContext)) {
			Toast.makeText(mContext, "网络错误！", Toast.LENGTH_SHORT).show();
		} else {
			if(mRequestQueue == null){
				mRequestQueue = Volley.newRequestQueue(mContext);
			}
			Response.Listener<String> SuccessfulResponse = new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					try {
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
	 * 请求发送验证码
	 */
	public void Verification_code(final HashMap<String, String> mhashMap, final String Route) {
		if (!super.checkNetworkAvailable(mContext)) {
			Toast.makeText(mContext, "网络错误！", Toast.LENGTH_SHORT).show();
		} else {
			if(mRequestQueue == null){
				mRequestQueue = Volley.newRequestQueue(mContext);
			}
			Response.Listener<String> SuccessfulResponse = new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					try {
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
				@Override
				protected Response<String> parseNetworkResponse(
						NetworkResponse response) {
					Response<String> superResponse = super.parseNetworkResponse(response);
					Map<String, String> responseHeaders = response.headers;
					String rawCookies = responseHeaders.get("Set-Cookie");
					//服务端返回是 set-cookie:JSESSIONID=D90B58454550B4D37C4B66A76BF27B93; Path=/otn BIGipServerotn=2564030730.64545.0000; path=/
					String part1 = substring(rawCookies, "", ";");
					String part2 = substring(rawCookies, "\n", ";");
					//客户端需要的是 cookie:JSESSIONID=D90B58454550B4D37C4B66A76BF27B93; BIGipServerotn=2564030730.64545.0000;
					cookies = part1 + "; " + part2 + ";";
					return superResponse;
				}
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					if(cookies != null && cookies.length() > 0){
						HashMap<String, String>	headers = new HashMap<>();
						headers.put("Cookie", cookies);
						return headers;
					}
					return super.getHeaders();
				}
			};
			mRequestQueue.add(stringRequest);
		}
	}

	/**
	 * 验证验证码是否正确
	 */
	public void Number_Verification_code(final HashMap<String, String> mhashMap, final String Route) {
		if (!super.checkNetworkAvailable(mContext)) {
			Toast.makeText(mContext, "网络错误！", Toast.LENGTH_SHORT).show();
		} else {
			if(mRequestQueue == null){
				mRequestQueue = Volley.newRequestQueue(mContext);
			}
			Response.Listener<String> SuccessfulResponse = new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					try {
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
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					if(cookies != null && cookies.length() > 0){
						HashMap<String, String>	headers = new HashMap<>();
						headers.put("Cookie", cookies);
						return headers;
					}
					return super.getHeaders();
				}
			};
			mRequestQueue.add(stringRequest);
		}
	}
	/**
	 * 注册
	 */
	public void register(final HashMap<String, String> mhashMap, final String Route) {
		if (!super.checkNetworkAvailable(mContext)) {
			Toast.makeText(mContext, "网络错误！", Toast.LENGTH_SHORT).show();
		} else {
			if(mRequestQueue == null){
				mRequestQueue = Volley.newRequestQueue(mContext);
			}
			Response.Listener<String> SuccessfulResponse = new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					try {
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
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					if(cookies != null && cookies.length() > 0){
						HashMap<String, String>	headers = new HashMap<>();
						headers.put("Cookie", cookies);
						return headers;
					}
					return super.getHeaders();
				}
			};
			mRequestQueue.add(stringRequest);
		}
	}
	/**
	 * 重置密码
	 */
	public void ResetPassword(final HashMap<String, String> mhashMap, final String Route) {
		if (!super.checkNetworkAvailable(mContext)) {
			Toast.makeText(mContext, "网络错误！", Toast.LENGTH_SHORT).show();
		} else {
			if(mRequestQueue == null){
				mRequestQueue = Volley.newRequestQueue(mContext);
			}
			Response.Listener<String> SuccessfulResponse = new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					try {
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
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					if(cookies != null && cookies.length() > 0){
						HashMap<String, String>	headers = new HashMap<>();
						headers.put("Cookie", cookies);
						return headers;
					}
					return super.getHeaders();
				}
			};
			mRequestQueue.add(stringRequest);
		}
	}
	/**
	 * 传递cookies至服务器需用
	 * @param src
	 * @param fromString
	 * @param toString
	 * @return
	 */
	public static String substring(String src, String fromString,
								   String toString) {
		int fromPos = 0;
		if (fromString != null && fromString.length() > 0) {
			fromPos = src.indexOf(fromString);
			fromPos += fromString.length();
		}
		int toPos = src.indexOf(toString, fromPos);
		return src.substring(fromPos, toPos);
	}
}