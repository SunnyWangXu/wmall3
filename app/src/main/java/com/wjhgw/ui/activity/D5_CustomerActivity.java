package com.wjhgw.ui.activity;

import android.content.Context;
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
import com.wjhgw.business.bean.refund_allList;
import com.wjhgw.business.bean.return_refund_list;
import com.wjhgw.business.bean.return_refund_list_data;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.D6_customerAdapter;

import java.util.ArrayList;

/**
 * 退款/退货申请列表
 */
public class D5_CustomerActivity extends BaseActivity implements OnClickListener, XListView.IXListViewListener {

    private MyListView mListView;
    private LinearLayout ll_null;
    private D6_customerAdapter listAdapter;
    private boolean isSetAdapter = true;
    private int curpage = 1;
    private ArrayList<return_refund_list_data> return_refund_list = new ArrayList<>();


    private refund_allList refund;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d6_customer_layout);

        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
        mListView.setAdapter(null);
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("申请售后");
    }

    @Override
    public void onFindViews() {
        mListView = (MyListView) findViewById(R.id.d6_list_layout);
        ll_null = (LinearLayout) findViewById(R.id.ll_null);
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
    protected void onResume() {
        super.onResume();
        return_refund_list();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_lauout1:

                break;
            default:
                break;
        }

    }

    /**
     * 退款/退货申请列表
     */
    private void return_refund_list() {
        RequestParams params = new RequestParams();
        StartLoading();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("curpage", "" + curpage);
        params.addBodyParameter("page", "10");

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Return_refund_list, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                Dismiss();
                if (null != responseInfo) {
                    return_refund_list refun = gson.fromJson(responseInfo.result, return_refund_list.class);
                    if (refun.status.code == 10000) {
                        mListView.stopRefresh();
                        mListView.stopLoadMore();
                        mListView.setRefreshTime();
                        if (refun.datas != null) {
                            ll_null.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                            if (return_refund_list.size() > 0 && isSetAdapter) {
                                return_refund_list.clear();
                            }
                            return_refund_list.addAll(refun.datas);
                            if (isSetAdapter) {
                                listAdapter = new D6_customerAdapter(D5_CustomerActivity.this, return_refund_list);
                                mListView.setAdapter(listAdapter);
                            } else {
                                listAdapter.List = return_refund_list;
                                listAdapter.notifyDataSetChanged();
                            }

                            if (refun.pagination.hasmore) {
                                mListView.setPullLoadEnable(true);
                            } else {
                                mListView.setPullLoadEnable(false);
                            }
                        } else {
                            ll_null.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);
                        }
                    } else {
                        overtime(refun.status.code, refun.status.msg);
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
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onRefresh(int id) {
        isSetAdapter = true;
        curpage = 1;
        return_refund_list();
    }

    @Override
    public void onLoadMore(int id) {
        isSetAdapter = false;
        curpage++;
        return_refund_list();
    }
}
