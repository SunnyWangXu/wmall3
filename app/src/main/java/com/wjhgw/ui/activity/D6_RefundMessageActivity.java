package com.wjhgw.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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
import com.wjhgw.business.bean.Status;
import com.wjhgw.config.ApiInterface;

/**
 * 填写退货发货信息Activity
 */
public class D6_RefundMessageActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private LinearLayout llSelectExpress;
    private TextView tvSelectExpress;
    private EditText edInvoiceNo;
    private TextView edExpressCommit;
    private boolean isCommitExpress = false;
    private String tvName;
    private String eId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_message);


    }

    @Override
    public void onInit() {

        setUp();
        setTitle("填写退货发货信息");

    }

    @Override
    public void onFindViews() {
        tvSelectExpress = (TextView) findViewById(R.id.tv_select_express);
        llSelectExpress = (LinearLayout) findViewById(R.id.ll_select_express);
        edInvoiceNo = (EditText) findViewById(R.id.ed_invoice_no);
        edExpressCommit = (TextView) findViewById(R.id.tv_express_commit);

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {

        edInvoiceNo.addTextChangedListener(this);
        llSelectExpress.setOnClickListener(this);
        edExpressCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ll_select_express:
                intent.setClass(this, D7_RefundExpressActivity.class);
                startActivityForResult(intent, 44444);

                break;

            case R.id.tv_express_commit:
                if (isCommitExpress && tvName != null) {
                    /**
                     * 提交用户退货发货
                     */
                    loadExpressCommit();

                }
                break;

            default:
                break;
        }

    }

    /**
     * 提交用户退货发货
     */
    private void loadExpressCommit() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("refund_id", "20");
        params.addBodyParameter("express_id", eId);
        params.addBodyParameter("invoice_no", edInvoiceNo.getText().toString());

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Refund_express, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Gson gson = new Gson();
                Status data = gson.fromJson(responseInfo.result, Status.class);
                if (data.status.code == 10000) {
                    showToastShort("退货发货成功");

                    finish(false);
                } else {
                    overtime(data.status.code, data.status.msg);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToastShort("网络错误");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            tvName = data.getStringExtra("eName");
            tvSelectExpress.setText(tvName);
            tvSelectExpress.setTextColor(Color.parseColor("#666666"));

            eId = data.getStringExtra("eId");
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0 && tvName != null) {
            edExpressCommit.setBackgroundResource(R.drawable.after_sale_red);
            isCommitExpress = true;
        }
        if (s.length() == 0) {
            edExpressCommit.setBackgroundResource(R.drawable.after_sale_gray);
            isCommitExpress = false;
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
