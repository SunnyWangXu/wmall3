package com.wjhgw.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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
import com.wjhgw.business.bean.Get_goods_List;
import com.wjhgw.business.bean.Get_goods_list_data;
import com.wjhgw.business.bean.Gift_list_data;
import com.wjhgw.business.bean.Send_gift_list;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.J1_GiftAdapter;
import com.wjhgw.ui.view.listview.adapter.J1_RecordAdapter;

import java.util.ArrayList;

/**
 * 酒柜记录
 */
public class J1_RecordActivity extends BaseActivity implements XListView.IXListViewListener,
        View.OnClickListener {

    private TextView tv_button1;
    private TextView tv_button2;
    //private View v_line1;
    //private View v_line2;
    private MyListView mListView;
    //当前页码数
    private int curpage = 1;
    //ListView 是刷新状态还是加载更多状态
    private Boolean isSetAdapter = true;
    private Boolean Mark = true;
    private J1_RecordAdapter listAdapter = null;
    private J1_GiftAdapter listAdapter1 = null;
    private LinearLayout ll_null;
    private Send_gift_list send_gift_list;
    private Get_goods_List get_goods_List;
    private ArrayList<Gift_list_data> gift_list_data = new ArrayList<>();
    private ArrayList<Get_goods_list_data> get_goods_list_datat1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_layout);

        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
       /* listAdapter = new J1_RecordAdapter(this, null);
        mListView.setAdapter(listAdapter);*/

    }


    @Override
    public void onInit() {
        setUp();
        setTitle("记录");
    }

    @Override
    public void onFindViews() {
        mListView = (MyListView) findViewById(R.id.lv_record_layout);
        tv_button1 = (TextView) findViewById(R.id.tv_button1);
        tv_button2 = (TextView) findViewById(R.id.tv_button2);
        ll_null = (LinearLayout) findViewById(R.id.ll_null);
        //v_line1 = (View) findViewById(R.id.v_line1);
        //v_line2 = (View) findViewById(R.id.v_line2);
    }

    @Override
    public void onInitViewData() {
    }

    @Override
    public void onBindListener() {
        tv_button1.setOnClickListener(this);
        tv_button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_array_back:
                finish(false);
                break;
            case R.id.tv_button1:
                isSetAdapter = true;
                Mark = true;
                curpage = 1;
                container();
                break;
            case R.id.tv_button2:
                isSetAdapter = true;
                Mark = false;
                curpage = 1;
                container();
                break;
            default:
                break;
        }
    }


    @Override
    public void onRefresh(int id) {
        isSetAdapter = true;
        curpage = 1;
        /**
         *刷新再请求
         */
        container();

    }

    /**
     * 加载更多
     *
     * @param id
     */
    @Override
    public void onLoadMore(int id) {
        isSetAdapter = false;
        curpage++;
        container();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getKey().equals("0")) {
            container();
        }

    }

    /**
     * 提赠记录和商品一览
     */
    private void container() {
        if (Mark) {
            get_goods_list();
            tv_button1.setBackgroundColor(Color.parseColor("#ffffff"));
            tv_button1.setTextColor(Color.parseColor("#f25252"));
            tv_button2.setBackgroundColor(Color.parseColor("#f25252"));
            tv_button2.setTextColor(Color.parseColor("#ffffff"));
        } else {
            send_goods_list();
            tv_button2.setBackgroundColor(Color.parseColor("#ffffff"));
            tv_button2.setTextColor(Color.parseColor("#f25252"));
            tv_button1.setBackgroundColor(Color.parseColor("#f25252"));
            tv_button1.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    /**
     * 用户发出的商品
     */
    private void send_goods_list() {
        StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("page", "10");
        params.addBodyParameter("curpage", curpage + "");
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Send_gift_list, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dismiss();
                Gson gson = new Gson();
                if (responseInfo != null) {
                    send_gift_list = gson.fromJson(responseInfo.result, Send_gift_list.class);
                    if (send_gift_list.status.code == 10000) {
                        mListView.stopRefresh();
                        mListView.stopLoadMore();
                        mListView.setRefreshTime();

                        if (send_gift_list.datas != null) {
                            ll_null.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                            if (gift_list_data.size() > 0 && isSetAdapter) {
                                gift_list_data.clear();
                            }
                            gift_list_data.addAll(send_gift_list.datas);

                            if (isSetAdapter) {
                                listAdapter1 = new J1_GiftAdapter(J1_RecordActivity.this, gift_list_data);
                                mListView.setAdapter(listAdapter1);
                            } else {
                                listAdapter1.list = gift_list_data;
                                listAdapter1.notifyDataSetChanged();
                            }

                            if (send_gift_list.pagination.hasmore) {
                                mListView.setPullLoadEnable(true);
                            } else {
                                mListView.setPullLoadEnable(false);
                            }
                        } else {
                            ll_null.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);

                        }
                    } else {
                        overtime(send_gift_list.status.code, send_gift_list.status.msg);
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
     * 用户收到的商品
     */
    private void get_goods_list() {
        StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("page", "10");
        params.addBodyParameter("curpage", curpage + "");
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Get_goods_list, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dismiss();
                Gson gson = new Gson();
                if (responseInfo != null) {
                    get_goods_List = gson.fromJson(responseInfo.result, Get_goods_List.class);
                    if (get_goods_List.status.code == 10000) {
                        mListView.stopRefresh();
                        mListView.stopLoadMore();
                        mListView.setRefreshTime();

                        if (get_goods_List.datas != null) {
                            ll_null.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                            if (get_goods_list_datat1.size() > 0 && isSetAdapter) {
                                get_goods_list_datat1.clear();
                            }
                            get_goods_list_datat1.addAll(get_goods_List.datas);

                            if (isSetAdapter) {
                                listAdapter = new J1_RecordAdapter(J1_RecordActivity.this, get_goods_list_datat1);
                                mListView.setAdapter(listAdapter);
                            } else {
                                listAdapter.list = get_goods_list_datat1;
                                listAdapter.notifyDataSetChanged();
                            }

                            if (get_goods_List.pagination.hasmore) {
                                mListView.setPullLoadEnable(true);
                            } else {
                                mListView.setPullLoadEnable(false);
                            }
                        } else {
                            ll_null.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);
                        }
                    } else {
                        overtime(get_goods_List.status.code, get_goods_List.status.msg);
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
