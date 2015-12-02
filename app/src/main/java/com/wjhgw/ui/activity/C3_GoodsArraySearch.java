package com.wjhgw.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
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
import com.wjhgw.business.bean.ActSearch;
import com.wjhgw.business.bean.ActSearch_datas;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.ArrSearchAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品排列查询
 */
public class C3_GoodsArraySearch extends BaseActivity implements XListView.IXListViewListener, View.OnClickListener, AbsListView.OnScrollListener {
    private ImageView IvBack;
    private LinearLayout ll_search;
    private MyListView mListView;
    private String b_id;
    private int curpage = 1;
    private String a_id;
    private List<ActSearch_datas> actSearch_datas = new ArrayList<>();
    private TextView tvSaleNum;
    private LinearLayout llPriceArr;
    private TextView tvMoods;
    private String k;
    private String order;
    private ArrSearchAdapter arrSearchAdapter;
    private TextView tvSearchDef;
    private TextView tvPriceArr;
    private int PriceCount = 0;
    private ImageView ivPriceArrow;
    //ListView 是刷新状态还是加载更多状态
    private Boolean isSetAdapter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsarray_search);

        b_id = getIntent().getStringExtra("b_id");
        a_id = getIntent().getStringExtra("a_id");

        /**
         * 请求商品排序进来为默认排序
         */
        loadSearchGoodsArr();

        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
        mListView.setAdapter(null);
        mListView.setOnScrollListener(this);

    }


    @Override
    public void onInit() {

    }

    @Override
    public void onFindViews() {
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        IvBack = (ImageView) findViewById(R.id.iv_array_back);
        mListView = (MyListView) findViewById(R.id.lv_array_search);

        tvSearchDef = (TextView) findViewById(R.id.tv_search_default);
        tvSaleNum = (TextView) findViewById(R.id.tv_salenum);
        llPriceArr = (LinearLayout) findViewById(R.id.ll_price_arr);
        tvPriceArr = (TextView) findViewById(R.id.tv_price_arr);
        tvMoods = (TextView) findViewById(R.id.tv_moods);
        ivPriceArrow = (ImageView) findViewById(R.id.iv_price_arrow);

    }

    @Override
    public void onInitViewData() {
        tvSearchDef.setTextColor(Color.parseColor("#d63235"));

    }

    @Override
    public void onBindListener() {
        IvBack.setOnClickListener(this);
        ll_search.setOnClickListener(this);
        tvSearchDef.setOnClickListener(this);
        tvSaleNum.setOnClickListener(this);
        llPriceArr.setOnClickListener(this);
        tvMoods.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_array_back:
                finish(false);
                break;

            case R.id.ll_search:
                Intent intent = new Intent(this, C2_SearchActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_search_default:
                clearTvstate();
                tvSearchDef.setTextColor(Color.parseColor("#d63235"));
                loadSearchGoodsArr();
                break;

            case R.id.tv_salenum:
                clearTvstate();
                tvSaleNum.setTextColor(Color.parseColor("#d63235"));
                k = "1";
                order = "2";
                loadSearchGoodsArr();
                break;

            case R.id.ll_price_arr:
                PriceCount++;
                clearTvstate();
                tvPriceArr.setTextColor(Color.parseColor("#d63235"));
                k = "3";
                if (PriceCount % 2 == 1) {
                    order = "1";
                    ivPriceArrow.setImageResource(R.mipmap.ic_array_default_up);
                } else {
                    order = "2";
                    ivPriceArrow.setImageResource(R.mipmap.ic_array_default_down);
                }
                loadSearchGoodsArr();

                break;

            case R.id.tv_moods:
                clearTvstate();
                tvMoods.setTextColor(Color.parseColor("#d63235"));
                k = "2";
                order = "2";
                loadSearchGoodsArr();

                break;
            default:
                break;
        }
    }

    /**
     * 清除TextView和价格图片的状态的状态
     */
    private void clearTvstate() {
        tvMoods.setTextColor(Color.parseColor("#666666"));
        tvPriceArr.setTextColor(Color.parseColor("#666666"));
        tvSaleNum.setTextColor(Color.parseColor("#666666"));
        tvSearchDef.setTextColor(Color.parseColor("#666666"));
        ivPriceArrow.setImageResource(R.mipmap.ic_array_default);
    }

    @Override
    public void onRefresh(int id) {
        isSetAdapter = true;
        /**
         *刷新再请求
         */
        loadSearchGoodsArr();

    }

    @Override
    public void onLoadMore(int id) {
        isSetAdapter = false;
        curpage++;
        loadSearchGoodsArr();
    }


    /**
     * 请求商品排序
     */
    private void loadSearchGoodsArr() {
        super.StartLoading();
        RequestParams params = new RequestParams();
        if (b_id != null) {
            params.addBodyParameter("b_id", b_id);
        }
        if (a_id != null) {
            params.addBodyParameter("a_id", a_id);
        }
        params.addBodyParameter("page", "10");
        params.addBodyParameter("curpage", curpage + "");
        if (k != null) {
            params.addBodyParameter("k", k);
        }
        if (order != null) {
            params.addBodyParameter("order", order);
        }
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Act_search, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                C3_GoodsArraySearch.super.Dismiss();
                Gson gson = new Gson();
                if (responseInfo != null) {
                    ActSearch actSearch = gson.fromJson(responseInfo.result, ActSearch.class);
                    if (actSearch.status.code == 10000) {
                        mListView.stopRefresh();
                        mListView.setRefreshTime();

                        if (actSearch.datas != null) {
                            if (actSearch_datas.size() > 0  && isSetAdapter) {
                                actSearch_datas.clear();
                            }
                            actSearch_datas.addAll(actSearch.datas);

                            if (isSetAdapter) {
                                arrSearchAdapter = new ArrSearchAdapter(C3_GoodsArraySearch.this, actSearch_datas);
                                mListView.setAdapter(arrSearchAdapter);
                            } else {
                                arrSearchAdapter.actSearch_datas = actSearch_datas;
                                arrSearchAdapter.notifyDataSetChanged();
                            }

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

            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: //
                System.out.println("停止...");
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                System.out.println("正在滑动...");
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                System.out.println("开始滚动...");

                break;
        }
    }

    ;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
