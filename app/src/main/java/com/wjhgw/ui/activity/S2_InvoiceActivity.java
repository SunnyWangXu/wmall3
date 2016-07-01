package com.wjhgw.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.wjhgw.business.bean.Invoice;
import com.wjhgw.config.ApiInterface;

/**
 * 选择发票详情的Activity
 */
public class S2_InvoiceActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private TextView tvNeedInvoice;
    private TextView tvNoInvoice;
    private ImageView ivPerson;
    private ImageView ivCompany;
    private EditText edInvoiceTitle;
    private Button btnInvoiceSave;
    private boolean isPerson;
    private String edTitle;
    private String invoiceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        invoiceId = getIntent().getStringExtra("invoiceId");
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("发票信息");
    }

    @Override
    public void onFindViews() {
        tvNeedInvoice = (TextView) findViewById(R.id.tv_need_invoice);
        tvNoInvoice = (TextView) findViewById(R.id.tv_no_invoice);

        ivPerson = (ImageView) findViewById(R.id.iv_person);
        ivCompany = (ImageView) findViewById(R.id.iv_company);

        edInvoiceTitle = (EditText) findViewById(R.id.ed_invoice_title);

        btnInvoiceSave = (Button) findViewById(R.id.btn_invoice_save);
    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        tvNeedInvoice.setOnClickListener(this);
        tvNoInvoice.setOnClickListener(this);

        ivPerson.setOnClickListener(this);
        ivCompany.setOnClickListener(this);
        btnInvoiceSave.setOnClickListener(this);
        edInvoiceTitle.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_need_invoice:
                tvNeedInvoice.setTextColor(Color.parseColor("#f25252"));
                tvNeedInvoice.setBackground(getResources().getDrawable(R.drawable.invoice_tv_redbg));

                tvNoInvoice.setTextColor(Color.parseColor("#666666"));
                tvNoInvoice.setBackground(getResources().getDrawable(R.drawable.invoice_tv_light_graybg));

                btnInvoiceSave.setBackgroundColor(Color.parseColor("#f25252"));
                break;

            case R.id.tv_no_invoice:

                if (invoiceId.equals("")) {
                    finish();
                } else {
                    /**
                     * 不需要发票
                     */
                    Intent intent = new Intent();
                    intent.putExtra("invoice_id", "");
                    intent.putExtra("invoice_title", "");
                    intent.putExtra("invoice_content", "");

                    setResult(22222, intent);
                    finish();

                }


                tvNoInvoice.setTextColor(Color.parseColor("#f25252"));
                tvNoInvoice.setBackground(getResources().getDrawable(R.drawable.invoice_tv_redbg));

                tvNeedInvoice.setTextColor(Color.parseColor("#666666"));
                tvNeedInvoice.setBackground(getResources().getDrawable(R.drawable.invoice_tv_light_graybg));

                btnInvoiceSave.setBackgroundColor(Color.parseColor("#cccccc"));
                btnInvoiceSave.setClickable(false);
                break;

            case R.id.iv_person:
                ivPerson.setImageResource(R.mipmap.ic_order_select);
                ivCompany.setImageResource(R.mipmap.ic_order_blank);

                isPerson = true;
                break;

            case R.id.iv_company:
                ivPerson.setImageResource(R.mipmap.ic_order_blank);
                ivCompany.setImageResource(R.mipmap.ic_order_select);

                isPerson = false;
                break;

            case R.id.btn_invoice_save:
                edTitle = edInvoiceTitle.getText().toString();
                if (edTitle.equals("")) {
                    showToastShort("请输入个人或者单位名称");
                } else {
                    /**
                     * 添加发票
                     */
                    addInvoice();
                }

                break;
            default:
                break;
        }
    }


    /**
     * 添加发票
     */
    private void addInvoice() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        if (isPerson) {
            params.addBodyParameter("inv_title_select", "person");
        } else {
            params.addBodyParameter("inv_title_select", "company");
        }

        params.addBodyParameter("inv_title", edTitle);
        params.addBodyParameter("inv_content", "明细");


        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Add_invoice, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                Invoice invoice = gson.fromJson(responseInfo.result, Invoice.class);
                if (invoice.status.code == 10000) {

                    int invoice_id = invoice.datas.invoice_id;

                    Intent intent = new Intent();
                    intent.putExtra("invoice_id", invoice_id);
                    intent.putExtra("invoice_title", edTitle);
                    intent.putExtra("invoice_content", "明细");

                    setResult(22222, intent);
                    finish();

                } else {
                    overtime(invoice.status.code, invoice.status.msg);
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (s.length() > 0) {
            btnInvoiceSave.setBackgroundColor(Color.parseColor("#f25252"));
            btnInvoiceSave.setClickable(true);
        } else {
            btnInvoiceSave.setBackgroundColor(Color.parseColor("#cccccc"));
            btnInvoiceSave.setClickable(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
