package com.wjhgw.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wjhgw.R;
import com.wjhgw.ui.image.LoadImageByVolley;

public class IndexFragment extends Fragment {
	View messageLayout;
	private ImageView mImageView;
	private LoadImageByVolley load;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		messageLayout = inflater.inflate(R.layout.index_layout, container, false);
		mImageView = (ImageView)messageLayout.findViewById(R.id.imageView);
		load = new LoadImageByVolley(getActivity(),mImageView);
		load.loadImageByVolley("http://avatar.csdn.net/6/6/D/1_lfdfhl.jpg");
		return messageLayout;
	}

}
