package com.wjhgw.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjhgw.R;
import com.wjhgw.ui.activity.A0_LoginActivity;
import com.wjhgw.ui.activity.MyLockBoxActivity;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;

public class MyFragment extends Fragment implements XListView.IXListViewListener, View.OnClickListener {
    private View MyLayout;
    private MyListView mListView;
    private LinearLayout MyAssetsLayout;
    private LinearLayout MyMessageLayout;
    private LinearLayout MySetHelpLayout;
    private LinearLayout MyOrderLayout;
    private ImageView myAvatar;
    private TextView ivLockBox;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyLayout = inflater.inflate(R.layout.my_layout, container, false);

        /**
         * 加载视图
         */
        setInflaterView();

        /**
         * 初始化视图
         */
        initView();

        /**
         * 设置监听事件
         */
        setClick();

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
        MyMessageLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.my_message_layout, null);
        MyOrderLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.my_order_layout, null);
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
     * 初始化视图
     */
    private void initView() {

        myAvatar = (ImageView) MyMessageLayout.findViewById(R.id.my_avatar);
        ivLockBox = (TextView) MyMessageLayout.findViewById(R.id.iv_lockBox);

    }

    /**
     * 设置监听事件
     */
    private void setClick() {

        myAvatar.setOnClickListener(this);
        ivLockBox.setOnClickListener(this);
    }

    @Override
    public void onRefresh(int id) {
      /*  mListView.stopRefresh();
        mListView.setRefreshTime();*/

    }

    @Override
    public void onLoadMore(int id) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.my_avatar:
                intent.setClass(getActivity(), A0_LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_lockBox:
                intent.setClass(getActivity(), MyLockBoxActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
