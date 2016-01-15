package com.wjhgw.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.bean.AssetsList;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.Z1_Adapter;

import java.util.ArrayList;

/**
 * 充值卡余额明细
 */
public class Z1_Prepaid_card_balanceActivity extends BaseActivity implements OnClickListener, XListView.IXListViewListener {

    private MyListView mListView;
    private LinearLayout ll_null;
    private String key;
    private Z1_Adapter listAdapter;
    private boolean isSetAdapter = true;
    private int curpage = 1;
    private String order_state = "";
    private String name = "";
    private ArrayList<com.wjhgw.business.bean.AssetsList_data> AssetsList_data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z1_layout);
        key = getKey();
        mListView = (MyListView) findViewById(R.id.lv_z1_list);
        ll_null = (LinearLayout) findViewById(R.id.ll_null);
        ll_null.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
        /*listAdapter = new Z1_Adapter(this,null);
        mListView.setAdapter(listAdapter);*/
        rcb_log_list();
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("充值卡余额明细");
    }

    @Override
    public void onFindViews() {

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
    /*	case R.id.e0_return:
            finish();
			break;*/

            default:
                break;
        }

    }

    @Override
    public void onRefresh(int id) {
        isSetAdapter = true;
        curpage = 1;
        rcb_log_list();
    }

    @Override
    public void onLoadMore(int id) {
        isSetAdapter = false;
        curpage++;
        rcb_log_list();
    }

    /**
     * 用户充值卡明细
     */
    private void rcb_log_list() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("curpage", Integer.toString(curpage));
        params.addBodyParameter("page", "10");

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Rcb_log_list, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (null != responseInfo) {
                    AssetsList assetsList = gson.fromJson(responseInfo.result, AssetsList.class);
                    if (assetsList.status.code == 10000) {
                        mListView.stopRefresh();
                        mListView.stopLoadMore();
                        mListView.setRefreshTime();
                        if (assetsList.datas != null) {
                            ll_null.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                            if (AssetsList_data.size() > 0 && isSetAdapter) {
                                AssetsList_data.clear();
                            }
                            AssetsList_data.addAll(assetsList.datas);
                            if (isSetAdapter) {
                                listAdapter = new Z1_Adapter(Z1_Prepaid_card_balanceActivity.this, AssetsList_data);
                                mListView.setAdapter(listAdapter);
                            } else {
                                listAdapter.List = AssetsList_data;
                                listAdapter.notifyDataSetChanged();
                            }

                            if (assetsList.pagination.hasmore) {
                                mListView.setPullLoadEnable(true);
                            } else {
                                mListView.setPullLoadEnable(false);
                            }
                        } else {
                            ll_null.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);
                        }
                    }else {
                        overtime(assetsList.status.code, assetsList.status.msg);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToastShort("网络错误");
            }

        });
    }

}
