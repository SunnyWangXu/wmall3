package com.wjhgw.ui.activity;

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
import com.wjhgw.business.bean.Status;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.D3_EvaluateAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 商品评价
 */
public class D3_EvaluateActivity extends BaseActivity implements OnClickListener, XListView.IXListViewListener {

    private MyListView mListView;
    private String extend_order_goods;
    private String key;
    private String order_id;
    private D3_EvaluateAdapter listAdapter;
    private TextView tv_submit;
    private JSONArray myJsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d3_evaluate_layout);

        key = getKey();
        //extend_order_goods = getIntent().getStringExtra("extend_order_goods");
        extend_order_goods = getIntent().getStringExtra("extend_order_goods");
        order_id = getIntent().getStringExtra("order_id");
        try {
            myJsonObject = new JSONArray(extend_order_goods);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
        mListView.setAdapter(null);
        if (extend_order_goods != null) {
            extend_order_goods.toString();
            listAdapter = new D3_EvaluateAdapter(D3_EvaluateActivity.this, myJsonObject);
            mListView.setAdapter(listAdapter);
        }
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("商品评价");
    }

    @Override
    public void onFindViews() {

        mListView = (MyListView) findViewById(R.id.d3_list_layout);
        tv_submit = (TextView) findViewById(R.id.tv_submit);

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        tv_submit.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                try {
                    StringBuffer evaluate_content = new StringBuffer();
                    for (int i = 0; i < myJsonObject.length(); i++) {
                        String goods_id = ((JSONObject) myJsonObject.get(i)).getString("goods_id");
                        evaluate_content.append(goods_id + "|-|" + listAdapter.content[i] + "|-|" + listAdapter.rating[i] + "|*|");
                    }
                    add_evaluate(evaluate_content.toString().substring(0, evaluate_content.toString().length() - 3));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                finish();
                break;

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
     * 订单商品评价
     */
    private void add_evaluate(String evaluate_content) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("order_id", order_id);
        params.addBodyParameter("evaluate_content", evaluate_content);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Add_evaluate, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (null != responseInfo) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);
                    if (status.status.code == 10000) {
                        finish(false);
                    }else {
                        overtime(status.status.code,status.status.msg);
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
