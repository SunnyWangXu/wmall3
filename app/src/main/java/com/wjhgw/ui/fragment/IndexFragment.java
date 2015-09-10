package com.wjhgw.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wjhgw.R;
import com.wjhgw.business.api.Goods_Request;
import com.wjhgw.business.response.BusinessResponse;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.image.LoadImageByVolley;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView.IXListViewListener;

import org.json.JSONException;
import org.json.JSONObject;

public class IndexFragment extends Fragment implements BusinessResponse, IXListViewListener, View.OnClickListener {
	private View messageLayout;
	private LinearLayout MenuLayout;
	private ImageView mImageView;
	private LoadImageByVolley load;
	private Goods_Request dataModel;
	private MyListView mListView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		messageLayout = inflater.inflate(R.layout.index_layout, container, false);
		MenuLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.index_menu,null);
		dataModel = new Goods_Request(getActivity());
		dataModel.addResponseListener(this);

		mListView = (MyListView)messageLayout.findViewById(R.id.index_listview);
		mListView.addHeaderView(MenuLayout);

		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this, 1);
		mListView.setRefreshTime();
		mListView.setAdapter(null);

		return messageLayout;
	}
	@Override
	public void OnMessageResponse(String url, String response, JSONObject status) throws JSONException {
		if (url.equals(ApiInterface.Goods_list)) {
			/*if (status == null){
				ArrayList<goods_list_data> list= dataModel.goodsList;
				String goodslist = list.get(1).goods_name;
				String s = list.get(1).goods_id;
				String s1 = list.get(1).goods_id;
			}else{
				if (status.getString("code").equals("10000")) {
					ArrayList<goods_list_data> list= dataModel.goodsList;
					String goodslist = list.get(1).goods_name;
					String s = list.get(1).goods_id;
					Toast.makeText(getActivity(), status.getString("msg"), Toast.LENGTH_LONG).show();
				} else if (status.getString("code").equals("600100")) {
					Toast.makeText(getActivity(), status.getString("msg"), Toast.LENGTH_LONG).show();
				}
			}*/

		}
	}

	@Override
	public void onRefresh(int id) {

	}

	@Override
	public void onLoadMore(int id) {

	}

	@Override
	public void onClick(View v) {

	}
}
