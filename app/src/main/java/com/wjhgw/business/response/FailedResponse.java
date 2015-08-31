package com.wjhgw.business.response;

import org.json.JSONException;

public interface FailedResponse {
	void OnFailedResponse(String url, String jo, String status) throws JSONException;
}
