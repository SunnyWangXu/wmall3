package com.wjhgw.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

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
import com.wjhgw.business.bean.OrderList;
import com.wjhgw.business.bean.OrderList_data;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.D0_OrderAdapter;

import java.util.ArrayList;

/**
 * 订单列表
 */
public class D0_OrderActivity extends BaseActivity implements OnClickListener, XListView.IXListViewListener {

    private MyListView mListView;
    private String key;
    private D0_OrderAdapter listAdapter;
    private boolean isSetAdapter = true;
    private int curpage = 1;
    private String order_state = "";
    private ArrayList<OrderList_data> OrderList_data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d0_order_layout);
        key = getKey();
        order_state = getIntent().getStringExtra("order_state");
        mListView = (MyListView) findViewById(R.id.d0_list_layout);

        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
        if (order_state.equals("100")) {
            refund_return_list();
        } else {
            order_list();
        }
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("订单管理");
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
        order_list();
    }

    @Override
    public void onLoadMore(int id) {
        isSetAdapter = false;
        curpage++;
        order_list();
    }

    /**
     * 订单列表
     */
    private void order_list() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("curpage", Integer.toString(curpage));
        params.addBodyParameter("page", "10");
        if (order_state != null) {
            params.addBodyParameter("order_state", order_state);
        }

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Order_list, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (null != responseInfo) {
                    OrderList orderList = gson.fromJson(responseInfo.result, OrderList.class);
                    if (orderList.status.code == 10000) {
                        mListView.stopRefresh();
                        mListView.stopLoadMore();
                        mListView.setRefreshTime();
                        if (orderList.datas != null) {
                            if (OrderList_data.size() > 0 && isSetAdapter) {
                                OrderList_data.clear();
                            }
                            OrderList_data.addAll(orderList.datas);
                            if (isSetAdapter) {
                                listAdapter = new D0_OrderAdapter(D0_OrderActivity.this, OrderList_data);
                                mListView.setAdapter(listAdapter);
                            } else {
                                listAdapter.List = OrderList_data;
                                listAdapter.notifyDataSetChanged();
                            }

                            if (orderList.pagination.hasmore) {
                                mListView.setPullLoadEnable(true);
                            } else {
                                mListView.setPullLoadEnable(false);
                            }
                        }

                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToastShort("网络错误");
            }

        });
    }

    /**
     * 售后数据接口
     */
    private void refund_return_list() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("curpage", Integer.toString(curpage));
        params.addBodyParameter("page", "10");
        if (order_state != null) {
            params.addBodyParameter("re_type", "1");
        }

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Refund_return_list, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (null != responseInfo) {
                    OrderList orderList = gson.fromJson(responseInfo.result, OrderList.class);
                    if (orderList.status.code == 10000) {
                        mListView.stopRefresh();
                        mListView.stopLoadMore();
                        mListView.setRefreshTime();
                        if (orderList.datas != null) {
                            if (OrderList_data.size() > 0 && isSetAdapter) {
                                OrderList_data.clear();
                            }
                            OrderList_data.addAll(orderList.datas);
                            if (isSetAdapter) {
                                listAdapter = new D0_OrderAdapter(D0_OrderActivity.this, OrderList_data);
                                mListView.setAdapter(listAdapter);
                            } else {
                                listAdapter.List = OrderList_data;
                                listAdapter.notifyDataSetChanged();
                            }

                            if (orderList.pagination.hasmore) {
                                mListView.setPullLoadEnable(true);
                            } else {
                                mListView.setPullLoadEnable(false);
                            }
                        }

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
