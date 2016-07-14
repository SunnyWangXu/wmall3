package com.wjhgw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import com.wjhgw.business.bean.Limit;
import com.wjhgw.business.bean.LimitGoodsInfo;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.view.listview.adapter.LimitGoodsAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 限时抢购Activity
 */
public class LimitDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    public Long limitTime;
    private int START = 1;
    private Timer timer;
    private android.os.Handler mHandler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (limitTime > 0 && limitTime != null) {
                        limitTime--;
                        secToTime(limitTime, 1);
                    }
                    break;
            }
        }
    };
    private ImageView ivLimit;
    private ListView lvLimit;
    private LimitGoodsAdapter mAdapter;
    private LinearLayout limitHead;
    private ArrayList<LimitGoodsInfo> goodsInfo;
    private TextView t1;
    private TextView t2;
    private TextView t3;
    private String xianshi_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.limit_detail_layout);

        xianshi_id = getIntent().getStringExtra("xianshi_id");
    }

    @Override
    public void onInit() {

        setUp();
        lvLimit = (ListView) findViewById(R.id.lv_limit);
        limitHead = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.limit_head, null);
        lvLimit.addHeaderView(limitHead);
    }

    @Override
    public void onFindViews() {

        ivLimit = (ImageView) findViewById(R.id.iv_limit);
        t1 = (TextView) limitHead.findViewById(R.id.tv_limit_goods_t1);
        t2 = (TextView) limitHead.findViewById(R.id.tv_limit_goods_t2);
        t3 = (TextView) limitHead.findViewById(R.id.tv_limit_goods_t3);

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {

        lvLimit.setOnItemClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 请求限时抢购的数据
         */
        loadLimit();
    }

    /**
     * 请求限时抢购的数据
     */

    private void loadLimit() {
        StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("xianshi_id", xianshi_id);
        params.addBodyParameter("num", "20");
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Limit_detail, params, new RequestCallBack<String>() {


            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dismiss();
                Gson gson = new Gson();
                Limit limit = gson.fromJson(responseInfo.result, Limit.class);
                setTitle(limit.datas.xianshi_title);

                if (limit.status.code == 10000) {

                    limitTime = limit.datas.count_down_time;

                    /**
                     * 倒计时
                     */
                    if (START == 1) {
                        Countdown(1);
                    }

                    String imgStr = limit.datas.mobile_thumb;
                    APP.getApp().getImageLoader().displayImage(imgStr, ivLimit, APP.getApp().getImageOptions());
                    goodsInfo = limit.datas.goods_info;

                    mAdapter = new LimitGoodsAdapter(LimitDetailActivity.this, goodsInfo);
                    lvLimit.setAdapter(mAdapter);
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }

    /**
     * 倒计时
     *
     * @param i
     */
    public void Countdown(final int i) {
       /* if (timer != null) {
            timer.cancel();
            timer = null;
        }*/
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                Message msg = mHandler.obtainMessage();
                msg.what = i;
                mHandler.sendMessage(msg);
            }
        }, new Date(), 1000);
    }

    /**
     * @param timeStr
     * @param s       1代表限时抢购
     */
    public void secToTime(long timeStr, int s) {
        if (timeStr > 0) {
            long hours = Math.abs((timeStr) / (60 * 60));
            long minutes = Math.abs((timeStr - hours * 60 * 60) / 60);
            long seconds = Math
                    .abs((timeStr - hours * 60 * 60 - minutes * 60));
            DecimalFormat df = new DecimalFormat("000");
            t1.setText(df.format(hours));
            df = new DecimalFormat("00");
            t2.setText(df.format(minutes));
            t3.setText(df.format(seconds));

        } else {
            t1.setText("000");
            t2.setText("00");
            t3.setText("00");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        START++;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {

        } else {
            String goodsId = goodsInfo.get(position - 1).goods_id;
            Intent intent = new Intent(this, W0_PrductDetailActivity.class);
            intent.putExtra("goods_id", goodsId);
            startActivity(intent);
        }

    }
}
