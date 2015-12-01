package com.wjhgw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.wjhgw.business.bean.ActSearch;
import com.wjhgw.business.bean.ActSearch_datas;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.adapter.ArrSearchAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品排列查询
 */
public class C3_GoodsArraySearch extends BaseActivity implements View.OnClickListener {
    private ImageView IvBack;
    private LinearLayout ll_search;
    private MyListView mListView;
    private String b_id;
    private int curpage = 1;
    private String a_id;
    private List<ActSearch_datas> actSearch_datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsarray_search);

        b_id = getIntent().getStringExtra("b_id");
        a_id = getIntent().getStringExtra("a_id");
        /**
         * 请求默认排序
         */
        loadSearchDefault();
    }

    /**
     * 请求默认排序
     */
    private void loadSearchDefault() {

        RequestParams params = new RequestParams();
        if(b_id != null){
            params.addBodyParameter("b_id", b_id);
        }
        if(a_id != null) {
            params.addBodyParameter("a_id", a_id);
        }
        params.addBodyParameter("page", "10");
        params.addBodyParameter("curpage", curpage+"");

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Act_search, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
              if(responseInfo != null) {
                    ActSearch actSearch = gson.fromJson(responseInfo.result, ActSearch.class);
                    if(actSearch.status.code == 10000){
                        actSearch_datas.clear();
                        if(actSearch.datas != null) {
                            actSearch_datas.addAll(actSearch.datas);

                            mListView.setAdapter(new ArrSearchAdapter(C3_GoodsArraySearch.this,actSearch_datas));

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
    public void onInit() {

    }

    @Override
    public void onFindViews() {
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        IvBack = (ImageView) findViewById(R.id.iv_array_back);
        mListView = (MyListView) findViewById(R.id.lv_array_search);

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        IvBack.setOnClickListener(this);
        ll_search.setOnClickListener(this);

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

            default:
                break;
        }
    }
}
