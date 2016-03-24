package com.wjhgw.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.wjhgw.business.bean.OrderList_goods_list_data;
import com.wjhgw.business.bean.Order_deliver;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.dialog.Customer_serviceDialog;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.D2_deliverAdapter;
import com.wjhgw.ui.view.listview.adapter.D4_customer_serviceAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 申请售后
 */
public class D4_Customer_serviceActivity extends BaseActivity implements OnClickListener, XListView.IXListViewListener {

    private MyListView mListView;
    private MyListView mListView1;
    private String lock_state;
    private String goods_price;
    private String goods_num;
    private String goods_name;
    private String rder_sn;
    private Double order_amount;
    private String goods;  //判断是为商品入口还是订单入口
    private String key;
    private D2_deliverAdapter listAdapter;
    private LinearLayout Customer_service1;
    private LinearLayout Customer_service2;
    private FrameLayout fl_lauout1;
    private FrameLayout fl_lauout2;
    private FrameLayout fl_layout3;
    private TextView tv_d4_text1;
    private TextView tv_d4_goods_name;
    private TextView tv_d4_next;
    private TextView tv_d4_rder_sn;
    private TextView tv_d4_num;
    private TextView tv_d4_text2;
    private TextView tv_d4_text3;
    private TextView tv_d4_text4;
    private TextView tv_d4_text5;
    private EditText et_d4_name1;
    private EditText et_d4_name2;
    private ImageView iv_d4_image1;
    private ImageView iv_d4_image2;
    private Customer_serviceDialog dialog;
    private ArrayList<OrderList_goods_list_data> extend_order_goods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d4_customer_service_layout);

        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
        mListView.setAdapter(null);
        mListView.addHeaderView(Customer_service1);
        mListView.addHeaderView(Customer_service2);

        key = getKey();
        goods = getIntent().getStringExtra("goods");
        if (goods.equals("0")) {
            lock_state = getIntent().getStringExtra("lock_state");
            goods_price = getIntent().getStringExtra("goods_price");
            goods_num = getIntent().getStringExtra("goods_num");
            goods_name = getIntent().getStringExtra("goods_name");
            rder_sn = getIntent().getStringExtra("rder_sn");
            order_amount = Double.parseDouble(goods_price) * Double.parseDouble(goods_num);
            tv_d4_text4.setText("最多" + order_amount + "元");
            tv_d4_text5.setText("最多" + goods_num + "件");
            tv_d4_goods_name.setText(goods_name);
            tv_d4_num.setText("¥" + goods_price + "*" + goods_num + "(数量)");
            tv_d4_rder_sn.setText(rder_sn);
        } else {
            lock_state = getIntent().getStringExtra("lock_state");
            rder_sn = getIntent().getStringExtra("rder_sn");
            order_amount = Double.parseDouble(getIntent().getStringExtra("order_amount"));

            tv_d4_text4.setText("最多" + order_amount + "元");
            Type type = new TypeToken<ArrayList<OrderList_goods_list_data>>() {
            }.getType();
            extend_order_goods = new Gson().fromJson(goods, type);
            tv_d4_text5.setText("最多0件");
            tv_d4_goods_name.setVisibility(View.GONE);
            tv_d4_num.setVisibility(View.GONE);


            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mListView1.getLayoutParams();
            linearParams.height = dip2px(this, 24) * extend_order_goods.size();// 当控件的高
            mListView1.setLayoutParams(linearParams);
            D4_customer_serviceAdapter adapter = new D4_customer_serviceAdapter(this, extend_order_goods);
            mListView1.setAdapter(adapter);

             //设置listview不能滑动
            mListView1.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }

        if (lock_state.equals("20")) {
            click(1);
            tv_d4_text2.setTextColor(Color.parseColor("#999999"));
            et_d4_name2.setText("0");
            et_d4_name2.setTextColor(Color.parseColor("#cccccc"));
            et_d4_name2.setEnabled(false);
            tv_d4_text3.setText("取消订单，全部退款");
        } else {
            click(1);
        }
        et_d4_name1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start != 0) {
                    if (order_amount < Integer.parseInt(et_d4_name1.getText().toString())) {
                        et_d4_name1.getText().delete(et_d4_name1.length() - 1, et_d4_name1.length());
                    }
                } else {
                    if (start == 0 && count > 0) {
                        if (order_amount < Integer.parseInt(et_d4_name1.getText().toString())) {
                            et_d4_name1.getText().delete(et_d4_name1.length() - 1, et_d4_name1.length());
                        }
                    } else {

                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_d4_name2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start != 0) {
                    if (Integer.parseInt(goods_num) < Integer.parseInt(et_d4_name2.getText().toString())) {
                        et_d4_name2.getText().delete(et_d4_name2.length() - 1, et_d4_name2.length());
                    }
                } else {
                    if (start == 0 && count > 0) {
                        if (Integer.parseInt(goods_num) < Integer.parseInt(et_d4_name2.getText().toString())) {
                            et_d4_name2.getText().delete(et_d4_name2.length() - 1, et_d4_name2.length());
                        }
                    } else {

                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    @Override
    public void onInit() {
        setUp();
        setTitle("申请售后");
    }

    @Override
    public void onFindViews() {
        mListView = (MyListView) findViewById(R.id.d4_list_layout);
        Customer_service1 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.d4_customer_service_item1, null);
        Customer_service2 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.d4_customer_service_item2, null);
        mListView1 = (MyListView) Customer_service2.findViewById(R.id.d4_list_item);

        fl_lauout1 = (FrameLayout) Customer_service1.findViewById(R.id.fl_lauout1);
        fl_lauout2 = (FrameLayout) Customer_service1.findViewById(R.id.fl_lauout2);
        fl_layout3 = (FrameLayout) Customer_service1.findViewById(R.id.fl_layout3);
        tv_d4_text1 = (TextView) Customer_service1.findViewById(R.id.tv_d4_text1);
        tv_d4_text2 = (TextView) Customer_service1.findViewById(R.id.tv_d4_text2);
        tv_d4_text3 = (TextView) Customer_service1.findViewById(R.id.tv_d4_text3);
        tv_d4_text4 = (TextView) Customer_service1.findViewById(R.id.tv_d4_text4);
        tv_d4_text5 = (TextView) Customer_service1.findViewById(R.id.tv_d4_text5);
        et_d4_name1 = (EditText) Customer_service1.findViewById(R.id.et_d4_name1);
        et_d4_name2 = (EditText) Customer_service1.findViewById(R.id.et_d4_name2);
        iv_d4_image1 = (ImageView) Customer_service1.findViewById(R.id.iv_d4_image1);
        iv_d4_image2 = (ImageView) Customer_service1.findViewById(R.id.iv_d4_image2);

        tv_d4_goods_name = (TextView) Customer_service2.findViewById(R.id.tv_d4_goods_name);
        tv_d4_num = (TextView) Customer_service2.findViewById(R.id.tv_d4_num);
        tv_d4_rder_sn = (TextView) Customer_service2.findViewById(R.id.tv_d4_rder_sn);
        tv_d4_next = (TextView) Customer_service2.findViewById(R.id.tv_d4_next);
    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        fl_lauout1.setOnClickListener(this);
        fl_lauout2.setOnClickListener(this);
        fl_layout3.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_lauout1:
                if (!lock_state.equals("20")) {
                    click(1);
                }
                break;
            case R.id.fl_lauout2:
                if (!lock_state.equals("20")) {
                    click(2);
                }
                break;
            case R.id.fl_layout3:
                if (!lock_state.equals("20")) {
                    dialog = new Customer_serviceDialog(this, tv_d4_text3);
                    dialog.show();
                }
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
     * 物流详情
     */
    private void order_deliver() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);

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
                            listAdapter = new D2_deliverAdapter(D4_Customer_serviceActivity.this, order_deliver.datas.data);
                            mListView.setAdapter(listAdapter);
                        }
                    } else {
                        overtime(order_deliver.status.code, order_deliver.status.msg);
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

    private void click(int i) {
        tv_d4_text1.setTextColor(Color.parseColor("#666666"));
        tv_d4_text2.setTextColor(Color.parseColor("#666666"));
        iv_d4_image1.setVisibility(View.GONE);
        iv_d4_image2.setVisibility(View.GONE);
        if (i == 1) {
            tv_d4_text1.setTextColor(Color.parseColor("#f25252"));
            iv_d4_image1.setVisibility(View.VISIBLE);
        } else if (i == 2) {
            tv_d4_text2.setTextColor(Color.parseColor("#f25252"));
            iv_d4_image2.setVisibility(View.VISIBLE);
        }

    }
}
