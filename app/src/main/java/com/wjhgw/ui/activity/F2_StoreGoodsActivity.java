package com.wjhgw.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.wjhgw.business.bean.StoreGoods;
import com.wjhgw.business.bean.StoreGoodsDatas;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.StoreGoodsAdapter;

import java.util.ArrayList;

/**
 * 附件店铺商品详情Activity
 */
public class F2_StoreGoodsActivity extends BaseActivity implements XListView.IXListViewListener {
    private static int MAKECURPAGE = 1;
    private LinearLayout storeHead;
    private MyListView lvStoreGoods;
    private ArrayList<StoreGoodsDatas> storeDatas;
    private ArrayList<StoreGoodsDatas> Data = new ArrayList<>();
    private StoreGoodsAdapter mAdapter;
    private boolean isSetAdapter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_goods);

        Data.clear();
        MAKECURPAGE = 1;
        lvStoreGoods.setPullLoadEnable(true);
        lvStoreGoods.setPullRefreshEnable(false);
        lvStoreGoods.setXListViewListener(this, 1);
        lvStoreGoods.setAdapter(null);

    }

    @Override
    public void onInit() {
        setUp();
        setTitle("万嘉商行");
        storeHead = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.store_goods_head, null);

    }

    @Override
    public void onFindViews() {
        lvStoreGoods = (MyListView) findViewById(R.id.lv_store_goods);
        lvStoreGoods.addHeaderView(storeHead);


    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 请求附近商铺商品详情
         */
        loadStoreGoods();
    }

    /**
     * 请求附近商铺商品详情
     */
    private void loadStoreGoods() {
        StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("nearby_store_id", "589");
        params.addBodyParameter("curpage", MAKECURPAGE + "");
        params.addBodyParameter("page", "10");
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Store_goods, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dismiss();
                Gson gson = new Gson();
                StoreGoods storeGoods = gson.fromJson(responseInfo.result, StoreGoods.class);
                if (storeGoods.status.code == 10000) {

                    lvStoreGoods.stopRefresh();
                    lvStoreGoods.stopLoadMore();

                    storeDatas = storeGoods.datas;
                    Data.addAll(storeDatas);
                    if (isSetAdapter) {
                        mAdapter = new StoreGoodsAdapter(F2_StoreGoodsActivity.this, Data);
                        lvStoreGoods.setAdapter(mAdapter);
                    } else {
                        mAdapter.storeDatas = Data;
                        mAdapter.notifyDataSetChanged();
                    }

                    if (storeGoods.pagination.hasmore) {
                        lvStoreGoods.setPullLoadEnable(true);
                    } else {
                        lvStoreGoods.setPullLoadEnable(false);
                    }
                }
                overtime(storeGoods.status.code, storeGoods.status.msg);

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }

    @Override
    public void onRefresh(int id) {
        isSetAdapter = true;
    }

    @Override
    public void onLoadMore(int id) {
        isSetAdapter = false;
        MAKECURPAGE++;
        /**
         * 请求附近商铺商品详情
         */
        loadStoreGoods();

    }
}
