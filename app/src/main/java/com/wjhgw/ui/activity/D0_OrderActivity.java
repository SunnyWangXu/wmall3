package com.wjhgw.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private LinearLayout ll_layout1;
    private View v_view1;
    private View v_view2;
    private View v_view3;
    private View v_view4;
    private View v_view5;
    private TextView tv_text1;
    private TextView tv_text2;
    private TextView tv_text3;
    private TextView tv_text4;
    private TextView tv_text5;
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


        setTitle(name);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();

        Request = new Order_Request(this);
        Request.addResponseListener(this);
    }

    @Override
    public void onInit() {
        setUp();
    }

    @Override
    public void onFindViews() {
        ll_no_order = (LinearLayout) findViewById(R.id.ll_no_order);
        ll_layout1 = (LinearLayout) findViewById(R.id.ll_layout1);
        v_view1 = (View) findViewById(R.id.v_view1);
        v_view2 = (View) findViewById(R.id.v_view2);
        v_view3 = (View) findViewById(R.id.v_view3);
        v_view4 = (View) findViewById(R.id.v_view4);
        v_view5 = (View) findViewById(R.id.v_view5);
        tv_text1 = (TextView) findViewById(R.id.tv_text1);
        tv_text2 = (TextView) findViewById(R.id.tv_text2);
        tv_text3 = (TextView) findViewById(R.id.tv_text3);
        tv_text4 = (TextView) findViewById(R.id.tv_text4);
        tv_text5 = (TextView) findViewById(R.id.tv_text5);
    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        tv_text1.setOnClickListener(this);
        tv_text2.setOnClickListener(this);
        tv_text3.setOnClickListener(this);
        tv_text4.setOnClickListener(this);
        tv_text5.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (order_state.equals("")) {
            ll_layout1.setVisibility(View.VISIBLE);
            order_list();
            click(1);
        } else {
            order_list();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_text1:
                click(1);
                order_state = "";
                OrderList_data.clear();
                order_list();
                break;
            case R.id.tv_text2:
                click(2);
                order_state = "10";
                OrderList_data.clear();
                order_list();
                break;
            case R.id.tv_text3:
                click(3);
                order_state = "20";
                OrderList_data.clear();
                order_list();
                break;
            case R.id.tv_text4:
                click(4);
                order_state = "30";
                OrderList_data.clear();
                order_list();
                break;
            case R.id.tv_text5:
                click(5);
                order_state = "60";
                OrderList_data.clear();
                order_list();
                break;

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
        super.StartLoading();
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
                D0_OrderActivity.super.Dismiss();
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
                        overtime(orderList.status.code, orderList.status.msg);
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

    private void click(int i) {
        v_view1.setBackgroundColor(Color.parseColor("#ffffff"));
        v_view2.setBackgroundColor(Color.parseColor("#ffffff"));
        v_view3.setBackgroundColor(Color.parseColor("#ffffff"));
        v_view4.setBackgroundColor(Color.parseColor("#ffffff"));
        v_view5.setBackgroundColor(Color.parseColor("#ffffff"));
        tv_text1.setTextColor(Color.parseColor("#666666"));
        tv_text2.setTextColor(Color.parseColor("#666666"));
        tv_text3.setTextColor(Color.parseColor("#666666"));
        tv_text4.setTextColor(Color.parseColor("#666666"));
        tv_text5.setTextColor(Color.parseColor("#666666"));

        if (i == 1) {
            v_view1.setBackgroundColor(Color.parseColor("#f14f4f"));
            tv_text1.setTextColor(Color.parseColor("#f14f4f"));
        } else if (i == 2) {
            v_view2.setBackgroundColor(Color.parseColor("#f14f4f"));
            tv_text2.setTextColor(Color.parseColor("#f14f4f"));
        } else if (i == 3) {
            v_view3.setBackgroundColor(Color.parseColor("#f14f4f"));
            tv_text3.setTextColor(Color.parseColor("#f14f4f"));
        } else if (i == 4) {
            v_view4.setBackgroundColor(Color.parseColor("#f14f4f"));
            tv_text4.setTextColor(Color.parseColor("#f14f4f"));
        } else if (i == 5) {
            v_view5.setBackgroundColor(Color.parseColor("#f14f4f"));
            tv_text5.setTextColor(Color.parseColor("#f14f4f"));
        }
    }
}
