package com.wjhgw.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.wjhgw.business.bean.RefundDetail;
import com.wjhgw.business.bean.RefundDetailDatas;
import com.wjhgw.business.bean.RefundDetailList;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.adapter.RefundListAdapter;

import java.util.List;

/**
 * 售后详情Activity
 */
public class D6_AfterSaleDetailActivity extends BaseActivity implements View.OnClickListener {
    private ListView lvGoodsMessage;
    private MyListView lvAfterSale;
    private LinearLayout afterSaleHeader1;
    private LinearLayout afterSaleHeader2;
    private LinearLayout afterSaleHeader3;
    private TextView tvRefund1;
    private TextView tvRefund2;
    private TextView tvRefund3;
    private TextView tvRefund4;
    private TextView tvRefund5;
    private TextView tvRefund6;
    private TextView tvRefund7;
    private TextView tvRefund8;
    private TextView tvRefund9;
    private TextView tvRefund10;
    private TextView tvRefund11;
    private TextView tvRefund12;
    private ImageView ivRefund1;
    private ImageView ivRefund2;
    private ImageView ivRefund3;
    private LinearLayout llrefund33;
    private LinearLayout ll_layout;
    private TextView tvRefund13;
    private boolean isCanWrite = false;
    private String refund_id = "";
    private LinearLayout llUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_sale_detail);

        refund_id = getIntent().getStringExtra("refund_id");
    }


    @Override
    public void onInit() {
        setUp();
        setTitle("售后详情");

    }

    @Override
    public void onFindViews() {
        lvAfterSale = (MyListView) findViewById(R.id.lv_after_sale);

        lvAfterSale.setPullLoadEnable(false);
        lvAfterSale.setPullRefreshEnable(false);
        lvAfterSale.setAdapter(null);

        afterSaleHeader1 = (LinearLayout) getLayoutInflater().inflate(R.layout.after_sale_head1, null);
        lvAfterSale.addHeaderView(afterSaleHeader1);
        afterSaleHeader2 = (LinearLayout) getLayoutInflater().inflate(R.layout.after_sale_head2, null);
        lvAfterSale.addHeaderView(afterSaleHeader2);

        afterSaleHeader3 = (LinearLayout) getLayoutInflater().inflate(R.layout.after_sale_header3, null);
        lvAfterSale.addHeaderView(afterSaleHeader3);

        llUpload = (LinearLayout) afterSaleHeader1.findViewById(R.id.ll_uoload);
        ivRefund1 = (ImageView) afterSaleHeader1.findViewById(R.id.iv_refund1);
        ivRefund2 = (ImageView) afterSaleHeader1.findViewById(R.id.iv_refund2);
        ivRefund3 = (ImageView) afterSaleHeader1.findViewById(R.id.iv_refund3);
        tvRefund1 = (TextView) afterSaleHeader1.findViewById(R.id.tv_refund1);
        tvRefund2 = (TextView) afterSaleHeader1.findViewById(R.id.tv_refund2);
        tvRefund3 = (TextView) afterSaleHeader1.findViewById(R.id.tv_refund3);
        tvRefund4 = (TextView) afterSaleHeader1.findViewById(R.id.tv_refund4);
        tvRefund5 = (TextView) afterSaleHeader1.findViewById(R.id.tv_refund5);
        tvRefund6 = (TextView) afterSaleHeader1.findViewById(R.id.tv_refund6);
        tvRefund7 = (TextView) afterSaleHeader1.findViewById(R.id.tv_refund7);
        tvRefund8 = (TextView) afterSaleHeader1.findViewById(R.id.tv_refund8);
        tvRefund9 = (TextView) afterSaleHeader1.findViewById(R.id.tv_refund9);
        tvRefund10 = (TextView) afterSaleHeader2.findViewById(R.id.tv_refund10);
        tvRefund11 = (TextView) afterSaleHeader3.findViewById(R.id.tv_refund11);
        tvRefund12 = (TextView) afterSaleHeader3.findViewById(R.id.tv_refund12);
        tvRefund13 = (TextView) findViewById(R.id.tv_refund13);

        llrefund33 = (LinearLayout) afterSaleHeader3.findViewById(R.id.ll_refund_express);
        ll_layout = (LinearLayout) afterSaleHeader1.findViewById(R.id.ll_layout);

        lvGoodsMessage = (ListView) afterSaleHeader2.findViewById(R.id.lv_goods_message);

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {

        tvRefund13.setOnClickListener(this);
    }


    /**
     * 加载退货详情
     */
    private void loadRefundDetail() {
        StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("refund_id", refund_id);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Refund_return_detail, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                Dismiss();
                RefundDetail refundDetail = gson.fromJson(responseInfo.result, RefundDetail.class);
                if (refundDetail.status.code == 10000) {
                    RefundDetailDatas datas = refundDetail.datas;

                    tvRefund1.setText(datas.seller_state_desc);
                    tvRefund2.setText(datas.refund_sn);
                    tvRefund3.setText(datas.seller_message);
                    tvRefund4.setText(datas.store_name);
                    if (datas.refund_type.equals("1")) {
                        tvRefund5.setText("退款");
                        ll_layout.setVisibility(View.GONE);
                    }
                    if (datas.refund_type.equals("2")) {
                        tvRefund5.setText("退货");
                        tvRefund8.setText(datas.goods_num);
                    }
                    if (datas.refund_type.equals("3")) {
                        tvRefund5.setText("换货");
                        tvRefund8.setText(datas.goods_num);
                    }
                    tvRefund6.setText(datas.reason_info);
                    tvRefund7.setText(datas.refund_amount + "元");
                    tvRefund9.setText(datas.buyer_message);
                    tvRefund10.setText(datas.order_sn);
                    tvRefund11.setText(datas.express_name);
                    tvRefund12.setText(datas.invoice_no);

                    String[] picInfo = datas.pic_info;
                    if (picInfo.length != 0) {
                        if (picInfo.length == 1) {
                            APP.getApp().getImageLoader().displayImage(picInfo[0], ivRefund1);
                        }
                        if (picInfo.length == 2) {
                            APP.getApp().getImageLoader().displayImage(picInfo[0], ivRefund1);
                            APP.getApp().getImageLoader().displayImage(picInfo[1], ivRefund2);
                        }
                        if (picInfo.length == 3) {
                            APP.getApp().getImageLoader().displayImage(picInfo[0], ivRefund1);
                            APP.getApp().getImageLoader().displayImage(picInfo[1], ivRefund2);
                            APP.getApp().getImageLoader().displayImage(picInfo[2], ivRefund3);
                        }
                    }

                    if (picInfo.length == 0) {
                        llUpload.setVisibility(View.GONE);
                    }

                    List<RefundDetailList> refundDetailList = datas.goods_list;
                    LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) lvGoodsMessage.getLayoutParams();
                    linearParams.height = dip2px(D6_AfterSaleDetailActivity.this, 24) * refundDetailList.size();// 当控件的高
                    lvGoodsMessage.setLayoutParams(linearParams);

                    RefundListAdapter mAdapter = new RefundListAdapter(D6_AfterSaleDetailActivity.this, refundDetailList);
                    lvGoodsMessage.setAdapter(mAdapter);

                    if (datas.express_name.equals("") || datas.invoice_no.equals("null")) {
                        llrefund33.setVisibility(View.GONE);
                    }
                    if (datas.ship_goods.equals("1")) {
                        tvRefund13.setBackgroundResource(R.drawable.after_sale_red);
                        isCanWrite = true;
                    } else {
                        tvRefund13.setBackgroundResource(R.drawable.after_sale_gray);
                        isCanWrite = false;
                    }

                } else {
                    overtime(refundDetail.status.code, refundDetail.status.msg);
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {

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
    public void onClick(View v) {
        Intent intent = new Intent();

        switch (v.getId()) {
            case R.id.tv_refund13:
                if (isCanWrite) {
                    intent.setClass(this, D7_RefundMessageActivity.class);
                    intent.putExtra("refund_id", refund_id);
                    startActivity(intent);
                }

                break;
            default:
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 加载退货详情
         */
        if (!refund_id.equals("")) {
            loadRefundDetail();
        }
    }
}
