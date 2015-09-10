package com.wjhgw.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wjhgw.R;
public class MineFragment extends Fragment {
	View MineLayout;

	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		MineLayout = inflater.inflate(R.layout.mine_layout, container,
				false);
		return MineLayout;
	}

}
