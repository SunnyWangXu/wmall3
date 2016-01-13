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
import com.wjhgw.business.api.Order_Request;
import com.wjhgw.business.bean.OrderList;
import com.wjhgw.business.bean.OrderList_data;
import com.wjhgw.business.response.BusinessResponse;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.D0_OrderAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 订单列表
 */
public class D0_OrderActivity extends BaseActivity implements BusinessResponse, OnClickListener, XListView.IXListViewListener {

    private MyListView mListView;
    private LinearLayout ll_no_order;
    private String key;
    private D0_OrderAdapter listAdapter;
    private boolean isSetAdapter = true;
    private int curpage = 1;
    private String order_state = "";
    private String name = "";
    private ArrayList<OrderList_data> OrderList_data = new ArrayList<>();
    private Order_Request Request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d0_order_layout);
        key = getKey();
        order_state = getIntent().getStringExtra("order_state");
        name = getIntent().getStringExtra("name");
        mListView = (MyListView) findViewById(R.id.d0_list_layout);
        ll_no_order = (LinearLayout) findViewById(R.id.ll_no_order);
        setTitle(name);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
        if (order_state.equals("100")) {
            refund_return_list();
        } else {
            order_list();
        }
        Request = new Order_Request(this);
        Request.addResponseListener(this);
    }

    @Override
    public void onInit() {
        setUp();
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
                            ll_no_order.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                            if (OrderList_data.size() > 0 && isSetAdapter) {
                                OrderList_data.clear();
                            }
                            OrderList_data.addAll(orderList.datas);
                            if (isSetAdapter) {
                                listAdapter = new D0_OrderAdapter(D0_OrderActivity.this, OrderList_data, Request, key);
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
                        } else {
                            ll_no_order.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);
                        }
                    } else {
                        overtime(orderList.status.code,orderList.status.msg);
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
                                listAdapter = new D0_OrderAdapter(D0_OrderActivity.this, OrderList_data, Request, key);
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
                    }else {
                        overtime(orderList.status.code,orderList.status.msg);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToastShort("网络错误");
            }

        });
    }

    @Override
    public void OnMessageResponse(String url, String response, JSONObject status) throws JSONException {
        isSetAdapter = true;
        curpage = 1;
        order_list();
    }
}
