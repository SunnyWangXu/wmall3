package com.wjhgw.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.wjhgw.R;
import com.wjhgw.business.api.Classification_Request;
import com.wjhgw.business.response.BusinessResponse;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.goods_class_adapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ClassificationFragment extends Fragment implements XListView.IXListViewListener ,BusinessResponse {
	public static int MAK = 0;
	private WebView webView;
	private MyListView mListView;
	private goods_class_adapter adapter;
	private Classification_Request dataModel;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View View = inflater.inflate(R.layout.discoverylayout, container, false);
		dataModel = new Classification_Request(getActivity());
		dataModel.addResponseListener(this);
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("key", "ed86e0e4dd9fd54ffea7511b9fc6728e");
		dataModel.goods_class(hashMap);
		mListView  = (MyListView)View.findViewById(R.id.discovery_listview);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(false);
		mListView.setXListViewListener(this, 1);
		mListView.setRefreshTime();

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String s = dataModel.class_List.get(position-1).gc_id;
				MAK = position-1;
				adapter.notifyDataSetChanged();
			}
		});
		return View;
	}

	@Override
	public void OnMessageResponse(String url, String response, JSONObject status) throws JSONException {
		if (url.equals(ApiInterface.Goods_class)) {
			if (status.getString("code").equals("10000")) {
				adapter = new goods_class_adapter(getActivity(),dataModel.class_List);
				mListView.setAdapter(adapter);
			} else if (status.getString("code").equals("600100")) {
				Toast.makeText(getActivity(), status.getString("msg"), Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onRefresh(int id) {

	}

	@Override
	public void onLoadMore(int id) {

	}


}
