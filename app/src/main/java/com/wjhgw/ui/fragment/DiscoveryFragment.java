package com.wjhgw.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.wjhgw.R;

public class DiscoveryFragment extends Fragment{
	private WebView webView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View View = inflater.inflate(R.layout.discoverylayout, container, false);
		return View;
	}
}
