package com.wjhgw.ui.view.listview.adapter;

//
//                       __
//                      /\ \   _
//    ____    ____   ___\ \ \_/ \           _____    ___     ___
//   / _  \  / __ \ / __ \ \    <     __   /\__  \  / __ \  / __ \
//  /\ \_\ \/\  __//\  __/\ \ \\ \   /\_\  \/_/  / /\ \_\ \/\ \_\ \
//  \ \____ \ \____\ \____\\ \_\\_\  \/_/   /\____\\ \____/\ \____/
//   \/____\ \/____/\/____/ \/_//_/         \/____/ \/___/  \/___/
//     /\____/
//     \/___/
//
//  Powered by BeeFramework
//

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wjhgw.R;
import com.wjhgw.business.data.goods_class_data1;

import java.util.ArrayList;

/**
 *一级分类列表适配器
 */
public class grid_adapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<goods_class_data1> List;

	public grid_adapter(Context c, ArrayList<goods_class_data1> list) {
		mInflater = LayoutInflater.from(c);
		this.mContext = c;
		this.List = list;
	}

	@Override
	public int getCount() {
		 return List.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public View getView(int position, View cellView, ViewGroup parent) {
		cellView = mInflater.inflate(R.layout.grid_item, null);
		TextView text = (TextView)cellView.findViewById(R.id.goods_class_item);
		text.setText(List.get(position).gc_name);
		return cellView;
	}
}
