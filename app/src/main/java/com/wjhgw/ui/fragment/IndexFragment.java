package com.wjhgw.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wjhgw.R;
public class IndexFragment extends Fragment {
	View messageLayout;
	Intent intent;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		messageLayout = inflater.inflate(R.layout.index_layout, container, false);
		return messageLayout;
	}

}
