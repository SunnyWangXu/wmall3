package com.wjhgw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.bean.RefundExpress;
import com.wjhgw.business.bean.RefundExpressDatas;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.view.listview.adapter.ExpressAdapter;

import java.util.List;

/**
 * 获取支持的快递列表的 Activity
 */
public class D8_RefundExpressActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView lvRefundMes;
    private RefundExpress refundExpress;
    private List<RefundExpressDatas> expressDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_express);

        /**
         * 获取支持的快递列表
         */
        loadRefundExpress();
    }


    @Override
    public void onInit() {
        setUp();
        setTitle("填写退货发货信息");

    }

    @Override
    public void onFindViews() {
        lvRefundMes = (ListView) findViewById(R.id.lv_refund_mes);

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        lvRefundMes.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String eName = expressDatas.get(position).e_name;
        String eId = expressDatas.get(position).id;

        Intent intent = new Intent();
        intent.putExtra("eName", eName);
        intent.putExtra("eId", eId);

        setResult(44444, intent);

        finish();
    }


    /**
     * 获取支持的快递列表
     */
    private void loadRefundExpress() {
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Express_list, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                refundExpress = gson.fromJson(responseInfo.result, RefundExpress.class);
                if (refundExpress.status.code == 10000) {

                    expressDatas = refundExpress.datas;
                    ExpressAdapter expressAdapter = new ExpressAdapter(D8_RefundExpressActivity.this, expressDatas);
                    lvRefundMes.setAdapter(expressAdapter);

                } else {
                    overtime(refundExpress.status.code, refundExpress.status.msg);
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }

}
