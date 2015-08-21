package com.wjhgw.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;
import com.wjhgw.R;
import com.wjhgw.ui.image.LoadImageByVolley;

public class IndexFragment extends Fragment {
	View messageLayout;
	private ImageView mImageView;
	private NetworkImageView mNetworkImageView;
	private LoadImageByVolley load;
	Intent intent;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		messageLayout = inflater.inflate(R.layout.index_layout, container, false);
		mImageView = (ImageView)messageLayout.findViewById(R.id.imageView);
		mNetworkImageView = (NetworkImageView) messageLayout.findViewById(R.id.networkImageView);
		load = new LoadImageByVolley(getActivity(),mImageView);
		load.loadImageByVolley("http://avatar.csdn.net/6/6/D/1_lfdfhl.jpg");
		return messageLayout;
	}

}
