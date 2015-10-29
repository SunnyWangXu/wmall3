package com.wjhgw.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wjhgw.R;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;

public class MyFragment extends Fragment implements XListView.IXListViewListener {
    private View MyLayout;
    private MyListView mListView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyLayout = inflater.inflate(R.layout.my_layout, container, false);

        /**
         * 给ListView添加视图
         */
        listAddHeader();

        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
        mListView.setAdapter(null);
        return MyLayout;
    }

    /**
     * 给ListView添加视图
     */
    private void listAddHeader() {
        mListView = (MyListView) MyLayout.findViewById(R.id.home_listview);
        //mListView.addHeaderView(homeViewPageLayout);
    }

    @Override
    public void onRefresh(int id) {
      /*  mListView.stopRefresh();
        mListView.setRefreshTime();*/

    }

    @Override
    public void onLoadMore(int id) {

    }
}
