package com.wjhgw.business.response;

import org.json.JSONException;
import org.json.JSONObject;

public interface BusinessResponse {
	void OnMessageResponse(String url, String response, JSONObject status) throws JSONException;
}
