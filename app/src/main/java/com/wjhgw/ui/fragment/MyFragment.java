package com.wjhgw.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wjhgw.R;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;

public class MyFragment extends Fragment implements XListView.IXListViewListener {
    private View MyLayout;
    private MyListView mListView;
    private LinearLayout MyAssetsLayout;
    private LinearLayout MyMessageLayout;
    private LinearLayout MySetHelpLayout;
    private LinearLayout MyOrderLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyLayout = inflater.inflate(R.layout.my_layout, container, false);

        /**
         * 加载视图
         */
        setInflaterView();

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

    private void setInflaterView() {
        MyMessageLayout =  (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.my_message_layout, null);
        MyOrderLayout =  (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.my_order_layout, null);
        MyAssetsLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.my_assets_layout, null);
        MySetHelpLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.my_set_help_layout, null);
    }

    /**
     * 给ListView添加视图
     */
    private void listAddHeader() {
        mListView = (MyListView) MyLayout.findViewById(R.id.my_listview);
        mListView.addHeaderView(MyMessageLayout);
        mListView.addHeaderView(MyOrderLayout);
        mListView.addHeaderView(MyAssetsLayout);
        mListView.addHeaderView(MySetHelpLayout);
    }

    /**
     * 设置监听事件
     */
    private void setClick() {

    }

    /**
     * 初始化视图
     */
    private void initView() {

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
