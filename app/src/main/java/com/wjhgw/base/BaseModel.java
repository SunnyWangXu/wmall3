package com.wjhgw.base;

import android.content.Context;

import com.wjhgw.business.BusinessResponse;
import com.wjhgw.business.FailedResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class BaseModel implements BusinessResponse, FailedResponse {
	protected ArrayList<BusinessResponse> businessResponseArrayList = new ArrayList<>();
	protected ArrayList<FailedResponse> failedResponseArrayList = new ArrayList<>();
	protected Context mContext;

	public BaseModel(Context context) {
        mContext = context;
	}

	/**
	 *
	 * 增加响应侦听器
	 */
	public void addResponseListener(BusinessResponse listener) {
		if (!businessResponseArrayList.contains(listener)) {
			businessResponseArrayList.add(listener);
		}
	}

	/**
	 *
	 *删除响应侦听器
	 */
	public void removeResponseListener(BusinessResponse listener) {
		businessResponseArrayList.remove(listener);
	}

	public void OnMessageResponse(String url, String response, JSONObject jsonObject)
			throws JSONException {
		for (BusinessResponse iterable_element : businessResponseArrayList) {
			iterable_element.OnMessageResponse(url, response, jsonObject);
		}
	}

	public void OnFailedResponse(String url, String jo, String status)
			throws JSONException {
		for (FailedResponse iterable_element : failedResponseArrayList) {
			iterable_element.OnFailedResponse(url, jo, status);
		}
	}
}
