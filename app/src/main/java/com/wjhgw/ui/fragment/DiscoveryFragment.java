package com.wjhgw.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.wjhgw.R;

public class DiscoveryFragment extends Fragment {
	private WebView webView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View View = inflater.inflate(R.layout.discovery_layout,null);
		return View;
	}
}
