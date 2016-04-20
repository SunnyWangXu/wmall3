package com.wjhgw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
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
import com.wjhgw.business.bean.MyLockBox;
import com.wjhgw.business.bean.Status;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.dialog.LoadDialog;
import com.wjhgw.ui.dialog.Prepaid_cardDialog;

/**
 * 资产的Activity
 */
public class Z0_AssetsActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_prepaid_card_balance;
    private TextView tv_account_balance;
    private TextView tv_prepaid_card;
    private FrameLayout fl_recharge;
    private FrameLayout fl_prepaid_card_balance;
    private FrameLayout fl_account_balance;
    private MyLockBox userinformation;
    private Status status;
    private LoadDialog Dialog;
    private Prepaid_cardDialog prepaid_cardDialog;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assets_layotu);
        Dialog = new LoadDialog(this);
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("资产一览");

    }

    @Override
    public void onFindViews() {
        tv_prepaid_card_balance = (TextView) findViewById(R.id.tv_prepaid_card_balance);
        tv_account_balance = (TextView) findViewById(R.id.tv_account_balance);
        tv_prepaid_card = (TextView) findViewById(R.id.tv_prepaid_card);
        fl_recharge = (FrameLayout) findViewById(R.id.fl_recharge);
        fl_account_balance = (FrameLayout) findViewById(R.id.fl_account_balance);
        fl_prepaid_card_balance = (FrameLayout) findViewById(R.id.fl_prepaid_card_balance);
    }

    @Override
    public void onInitViewData() {
    }

    @Override
    public void onBindListener() {
        fl_recharge.setOnClickListener(this);
        tv_prepaid_card.setOnClickListener(this);
        fl_account_balance.setOnClickListener(this);
        fl_prepaid_card_balance.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_recharge:
                showToastShort("功能正在开发中，敬请期待");
                break;
            /*case R.id.tv_prepaid_card:
                prepaid_cardDialog = new Prepaid_cardDialog(this);
                prepaid_cardDialog.show();
                prepaid_cardDialog.tv_determine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (prepaid_cardDialog.et_num.getText().toString().length() > 1) {
                            Rechargecard_add(prepaid_cardDialog.et_num.getText().toString());
                        }
                        prepaid_cardDialog.dismiss();
                    }
                });

                prepaid_cardDialog.tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prepaid_cardDialog.dismiss();
                    }
                });
                break;*/
            case R.id.fl_prepaid_card_balance:
                intent = new Intent(this, Z1_Prepaid_card_balanceActivity.class);
                intent.putExtra("state","1");
                startActivity(intent);
                break;
            case R.id.fl_account_balance:
                intent = new Intent(this, Z1_Prepaid_card_balanceActivity.class);
                intent.putExtra("state","2");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        load_User_information();
    }

    /**
     * 请求用户信息
     */
    private void load_User_information() {
        Dialog.ProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Get_member_base_info, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dialog.dismiss();
                Gson gson = new Gson();
                if (responseInfo.result != null) {
                    userinformation = gson.fromJson(responseInfo.result, MyLockBox.class);

                    if (userinformation.status.code == 10000) {
                        tv_account_balance.setText(userinformation.datas.available_predeposit);   //余额
                        tv_prepaid_card_balance.setText(userinformation.datas.available_rc_balance);   //可用充值卡余额
                    } else {
                        overtime(userinformation.status.code, userinformation.status.msg);
                    }
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    /**
     * 充值卡充值
     *//*
    private void Rechargecard_add(String rc_sn) {
        Dialog.ProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("rc_sn", rc_sn);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Rechargecard_add, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dialog.dismiss();
                Gson gson = new Gson();
                if (responseInfo.result != null) {
                    status = gson.fromJson(responseInfo.result, Status.class);

                    if (status.status.code == 10000) {
                        showToastShort(status.status.msg);
                        load_User_information();
                    } else {
                        overtime(status.status.code, status.status.msg);
                    }
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(Z0_AssetsActivity.this, "网络错误", Toast.LENGTH_SHORT).show();

            }
        });
    }*/

}
