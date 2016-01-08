package com.wjhgw.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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
import com.wjhgw.business.bean.ActSearch;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.J1_RecordAdapter;

/**
 * 商品排列查询
 */
public class J1_RecordActivity extends BaseActivity implements XListView.IXListViewListener,
        View.OnClickListener {

    private TextView tv_button1;
    private TextView tv_button2;
    private View v_line1;
    private View v_line2;
    private MyListView mListView;
    //当前页码数
    private int curpage = 1;
    //ListView 是刷新状态还是加载更多状态
    private Boolean isSetAdapter = true;
    private Boolean Mark = true;
    private String key;
    private J1_RecordAdapter listAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_layout);


        /**
         * 请求商品排序进来为默认排序
         */
        loadSearchGoodsArr();

        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
        listAdapter = new J1_RecordAdapter(this, null);
        mListView.setAdapter(listAdapter);

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

        v_line1 = (View) findViewById(R.id.v_line1);
        v_line2 = (View) findViewById(R.id.v_line2);
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
                Mark = true;
                container();
                break;
            case R.id.tv_button2:
                Mark = false;
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
        loadSearchGoodsArr();

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
        loadSearchGoodsArr();
    }

    @Override
    public void onResume() {
        super.onResume();
        key = getSharedPreferences("key", MODE_APPEND).getString("key", "0");
        if (!key.equals("0")) {
            // cab_list();
        }
        container();
    }

    /**
     * 提赠记录和商品一览
     */
    private void container() {
        if (Mark) {
            tv_button1.setBackgroundColor(Color.parseColor("#ffffff"));
            tv_button2.setBackgroundColor(Color.parseColor("#f1f1f1"));
            v_line2.setBackgroundColor(Color.parseColor("#00000000"));
            v_line1.setBackgroundColor(Color.parseColor("#f25252"));
        } else {
            tv_button2.setBackgroundColor(Color.parseColor("#ffffff"));
            tv_button1.setBackgroundColor(Color.parseColor("#f1f1f1"));
            v_line1.setBackgroundColor(Color.parseColor("#00000000"));
            v_line2.setBackgroundColor(Color.parseColor("#f25252"));
        }
    }

    /**
     * 请求商品排序
     */
    private void loadSearchGoodsArr() {
        super.StartLoading();
        RequestParams params = new RequestParams();

        params.addBodyParameter("page", "10");
        params.addBodyParameter("curpage", curpage + "");
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Act_search, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                J1_RecordActivity.super.Dismiss();
                Gson gson = new Gson();
                if (responseInfo != null) {
                    ActSearch actSearch = gson.fromJson(responseInfo.result, ActSearch.class);
                    if (actSearch.status.code == 10000) {
                        mListView.stopRefresh();
                        mListView.stopLoadMore();
                        mListView.setRefreshTime();

                        if (actSearch.datas != null) {
                            /*if (actSearch_datas.size() > 0 && isSetAdapter) {
                                actSearch_datas.clear();
                            }
                            actSearch_datas.addAll(actSearch.datas);

                            if (isSetAdapter) {
                                arrSearchAdapter = new ArrSearchAdapter(J1_RecordActivity.this, actSearch_datas);
                                mListView.setAdapter(arrSearchAdapter);
                            } else {
                                arrSearchAdapter.actSearch_datas = actSearch_datas;
                                arrSearchAdapter.notifyDataSetChanged();
                            }*/

                            if (actSearch.pagination.hasmore) {
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
