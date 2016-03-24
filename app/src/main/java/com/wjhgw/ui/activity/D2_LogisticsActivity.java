package com.wjhgw.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.wjhgw.business.bean.Order_deliver;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.D2_deliverAdapter;

/**
 * 物流详情
 */
public class D2_LogisticsActivity extends BaseActivity implements OnClickListener, XListView.IXListViewListener {

    private MyListView mListView;
    private String order_id = "";
    private String key;
    private D2_deliverAdapter listAdapter;
    private TextView tv_exp_text_name;
    private TextView tv_status;
    private TextView tv_mail_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d2_logistics_layout);

        key = getKey();
        order_id = getIntent().getStringExtra("order_id");

        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
        mListView.setAdapter(null);
        if (order_id != null) {
            order_deliver();
        }
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("物流详情");
    }

    @Override
    public void onFindViews() {

        mListView = (MyListView) findViewById(R.id.d2_list_layout);
        tv_exp_text_name = (TextView) findViewById(R.id.tv_exp_text_name);
        tv_mail_no = (TextView) findViewById(R.id.tv_mail_no);
        tv_status = (TextView) findViewById(R.id.tv_status);
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
            /*case R.id.fl_logistics:
                finish();
                break;*/

            default:
                break;
        }

    }

    @Override
    public void onRefresh(int id) {
    }

    @Override
    public void onLoadMore(int id) {
    }

    /**
     * 物流详情
     */
    private void order_deliver() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("order_id", order_id);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Order_deliver, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (null != responseInfo) {
                    Order_deliver order_deliver = gson.fromJson(responseInfo.result, Order_deliver.class);
                    if (order_deliver.status.code == 10000) {
                        mListView.stopRefresh();
                        mListView.stopLoadMore();
                        mListView.setRefreshTime();
                        if (order_deliver.datas != null) {
                            listAdapter = new D2_deliverAdapter(D2_LogisticsActivity.this, order_deliver.datas.data);
                            mListView.setAdapter(listAdapter);

                            tv_exp_text_name.setText(order_deliver.datas.expTextName);
                            tv_mail_no.setText(order_deliver.datas.mailNo);
                            if (order_deliver.datas.status.equals("1")) {
                                tv_status.setText("已发货");
                            } else if (order_deliver.datas.status.equals("2")) {
                                tv_status.setText("配送中");
                            } else if (order_deliver.datas.status.equals("3")) {
                                tv_status.setText("已签收");
                            }
                        }
                    }else {
                        overtime(order_deliver.status.code,order_deliver.status.msg);
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
}
