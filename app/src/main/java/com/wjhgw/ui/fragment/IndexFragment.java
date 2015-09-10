package com.wjhgw.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wjhgw.R;
import com.wjhgw.business.api.Goods_Request;
import com.wjhgw.business.response.BusinessResponse;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.image.LoadImageByVolley;

import org.json.JSONException;
import org.json.JSONObject;

public class IndexFragment extends Fragment implements BusinessResponse {
	View messageLayout;
	private ImageView mImageView;
	private LoadImageByVolley load;
	private Goods_Request dataModel;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		messageLayout = inflater.inflate(R.layout.index_layout, container, false);
		mImageView = (ImageView) messageLayout.findViewById(R.id.imageView);
		load = new LoadImageByVolley(getActivity(), mImageView);
		load.loadImageByVolley("http://avatar.csdn.net/6/6/D/1_lfdfhl.jpg");
		dataModel = new Goods_Request(getActivity());
		dataModel.addResponseListener(this);
		mImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

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
}
