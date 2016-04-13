package com.wjhgw.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;

/**
 * 发出礼包详情
 */
public class J5_GiftDetailActivity extends BaseActivity implements XListView.IXListViewListener {
    private MyListView lvGiftDetail;
    private RelativeLayout rlGiftUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_detail);

        lvGiftDetail.setPullLoadEnable(false);
        lvGiftDetail.setPullRefreshEnable(false);
        lvGiftDetail.setXListViewListener(this, 1);
        lvGiftDetail.setRefreshTime();
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("礼包详情");

    }

    @Override
    public void onFindViews() {
        lvGiftDetail = (MyListView) findViewById(R.id.lv_gift_detail);
        rlGiftUse = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.rl_gift_use, null);
        lvGiftDetail.addHeaderView(rlGiftUse);
    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {

    }

    @Override
    public void onRefresh(int id) {

    }

    @Override
    public void onLoadMore(int id) {

    }
}
