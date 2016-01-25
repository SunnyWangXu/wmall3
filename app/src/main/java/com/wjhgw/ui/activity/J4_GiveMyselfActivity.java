package com.wjhgw.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.bean.Address_list;
import com.wjhgw.business.bean.CadList_data;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.view.listview.adapter.CabGiveLvAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 犒劳自己Activity
 */
public class J4_GiveMyselfActivity extends BaseActivity implements View.OnClickListener {
    private ListView lvGiveMyself;
    private LinearLayout llGiveMyselfHead;
    private List<CadList_data> datas;
    private List<CadList_data> datas1 = new ArrayList<CadList_data>();
    private int totalNum;
    private LinearLayout llMyselfMessage;
    private TextView tvMyselfNotAddress;
    private TextView tvMyname;
    private CabGiveLvAdapter cabGiveLvAdapter;
    private TextView tvMyPhone;
    private TextView tvMyAddress;
    private TextView tvMyselfTotal;
    private TextView tvToGiveMyself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_myself);

        /**
         * 请求地址列表
         */
        load_Default_Addresst();

        String jsonStr = getIntent().getStringExtra("list");
        /**
         * 解析筛选选中赠送的数据
         */
        parseGiveData(jsonStr);

        cabGiveLvAdapter = new CabGiveLvAdapter(this, datas1);
        lvGiveMyself.setAdapter(cabGiveLvAdapter);

    }


    /**
     * 解析选中赠送的数据
     */
    private void parseGiveData(String jsonStr) {
        Type type = new TypeToken<ArrayList<CadList_data>>() {
        }.getType();
        datas = new Gson().fromJson(jsonStr, type);

        for (int i = 0; i < datas.size(); i++) {

            if (!datas.get(i).selected.equals("0")) {
                datas1.add(datas.get(i));
                totalNum += datas.get(i).num;
            }
        }

    }


    @Override
    public void onInit() {
        setUp();
        setTitle("犒劳自己");

        lvGiveMyself = (ListView) findViewById(R.id.lv_give_myself);
        tvMyselfTotal = (TextView) findViewById(R.id.tv_myself_total);

        llGiveMyselfHead = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_give_myself_head, null);

        lvGiveMyself.addHeaderView(llGiveMyselfHead);


    }

    @Override
    public void onFindViews() {

        llMyselfMessage = (LinearLayout) llGiveMyselfHead.findViewById(R.id.ll_myself_message);

        tvMyselfNotAddress = (TextView) llGiveMyselfHead.findViewById(R.id.tv_myself_not_address);
        tvMyname = (TextView) llGiveMyselfHead.findViewById(R.id.tv_myname);
        tvMyPhone = (TextView) llGiveMyselfHead.findViewById(R.id.tv_myphone);
        tvMyAddress = (TextView) llGiveMyselfHead.findViewById(R.id.tv_myaddress);

        tvToGiveMyself = (TextView) llGiveMyselfHead.findViewById(R.id.tv_to_give_myself);

    }

    @Override
    public void onInitViewData() {
        tvMyselfTotal.setText(totalNum + "件");

    }

    @Override
    public void onBindListener() {
        tvToGiveMyself.setOnClickListener(this);
    }


    /**
     * 请求地址列表
     */
    public void load_Default_Addresst() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("address_type", "0");
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Address_list, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Address_list address_list = gson.fromJson(responseInfo.result, Address_list.class);
                    if (address_list.status.code == 10000) {
                        if (address_list.datas == null) {
                            //地址为空显示要去设置收货地址
                            llMyselfMessage.setVisibility(View.GONE);
                            tvMyselfNotAddress.setVisibility(View.VISIBLE);
                        } else {
                            tvMyname.setText(address_list.datas.get(0).true_name);
                            tvMyPhone.setText(address_list.datas.get(0).mob_phone);
                            tvMyAddress.setText(address_list.datas.get(0).area_info + " " + address_list.datas.get(0).address);

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_to_give_myself:
                StringBuffer cart_id = new StringBuffer();

                for (int i = 0; i < datas1.size(); i++) {
                    cart_id.append(datas1.get(i).goods_id + "|" + datas1.get(i).num + ",");
                }

                String goods_str = cart_id.toString().substring(0, cart_id.toString().length() - 1);

                showToastShort(goods_str);


                break;

            default:
                break;
        }


    }
}
