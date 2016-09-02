package com.wjhgw.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.bean.Ssend_goods_list;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.DiyView.RoundImageView;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.J5_receiveAdapter;
import com.wjhgw.ui.view.listview.adapter.J5_sendAdapter;

/**
 * 发出礼包详情
 */
public class J5_GiftDetailActivity extends BaseActivity implements XListView.IXListViewListener, View.OnClickListener {
    private MyListView lvGiftDetail;
    private MyListView lv_item_layout1;
    private MyListView lv_item_layout2;
    private FrameLayout j5_item0;
    private LinearLayout j5_item1;
    private LinearLayout j5_item2;
    private String cab_gift_id = "";
    private Ssend_goods_list ssend_goods_list;
    private J5_sendAdapter listAdapter1;
    private J5_receiveAdapter listAdapter2;
    private TextView tv_limit_type;
    private TextView tv_gift_state;
    private RoundImageView iv_j5_use_img;
    private TextView tv_member_nickname;
    private TextView tv_gift_note;
    private TextView tv_j5_price;
    private TextView tv_j5_add_time;
    private TextView tv_j5_receive_info;
    private LinearLayout ll_loadmore;
    private Boolean loadmore = true;
    private ImageView iv_loadmore;
    private TextView tv_j5_give;
    private IWXAPI api;
    private String member_name;
    private String member_nickname;
    private String gift_link;
    private String edNote;
    private String useName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_detail);

        lvGiftDetail.setPullLoadEnable(false);
        lvGiftDetail.setPullRefreshEnable(false);
        lvGiftDetail.setXListViewListener(this, 1);
        lvGiftDetail.setRefreshTime();
        api = WXAPIFactory.createWXAPI(this, "wx99a6bd9b7bdbf645");
        api.registerApp("wx99a6bd9b7bdbf645");
    }

    @Override
    protected void onResume() {
        super.onResume();
        cab_gift_id = getIntent().getStringExtra("cab_gift_id");
        if (!cab_gift_id.equals("")) {
            send_goods_list();
        }

    }

    @Override
    public void onInit() {
        setUp();
        setTitle("礼包详情");

    }

    @Override
    public void onFindViews() {
        lvGiftDetail = (MyListView) findViewById(R.id.lv_gift_detail);
        tv_j5_give = (TextView) findViewById(R.id.tv_j5_give);
        j5_item0 = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.j5_item0, null);
        j5_item1 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.j5_item1, null);
        j5_item2 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.j5_item2, null);
        lvGiftDetail.addHeaderView(j5_item0);
        lvGiftDetail.addHeaderView(j5_item1);
        lvGiftDetail.addHeaderView(j5_item2);
        lvGiftDetail.setAdapter(null);

        lv_item_layout1 = (MyListView) j5_item1.findViewById(R.id.lv_item_layout1);
        lv_item_layout2 = (MyListView) j5_item2.findViewById(R.id.lv_item_layout2);
        lv_item_layout1.setPullLoadEnable(false);
        lv_item_layout1.setPullRefreshEnable(false);
        lv_item_layout1.setXListViewListener(this, 1);
        lv_item_layout1.setRefreshTime();

        lv_item_layout2.setPullLoadEnable(false);
        lv_item_layout2.setPullRefreshEnable(false);
        lv_item_layout2.setXListViewListener(this, 1);
        lv_item_layout2.setRefreshTime();

        tv_limit_type = (TextView) j5_item0.findViewById(R.id.tv_limit_type);
        tv_gift_state = (TextView) j5_item0.findViewById(R.id.tv_gift_state);
        iv_j5_use_img = (RoundImageView) j5_item0.findViewById(R.id.iv_j5_use_img);
        tv_member_nickname = (TextView) j5_item0.findViewById(R.id.tv_member_nickname);
        tv_gift_note = (TextView) j5_item0.findViewById(R.id.tv_gift_note);

        tv_j5_price = (TextView) j5_item1.findViewById(R.id.tv_j5_price);
        tv_j5_add_time = (TextView) j5_item1.findViewById(R.id.tv_j5_add_time);
        ll_loadmore = (LinearLayout) j5_item1.findViewById(R.id.ll_loadmore);
        iv_loadmore = (ImageView) j5_item1.findViewById(R.id.iv_loadmore);

        tv_j5_receive_info = (TextView) j5_item2.findViewById(R.id.tv_j5_receive_info);
    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        ll_loadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) lv_item_layout1.getLayoutParams();
                if (loadmore) {
                    loadmore = false;
                    iv_loadmore.setImageResource(R.mipmap.ic_loadmore2);
                    linearParams.height = dip2px(J5_GiftDetailActivity.this, 124) * 3;// 当控件的高
                } else {
                    loadmore = true;
                    iv_loadmore.setImageResource(R.mipmap.ic_loadmore1);
                    linearParams.height = dip2px(J5_GiftDetailActivity.this, 124) * ssend_goods_list.datas.gift_goods_list.size();// 当控件的高
                }
                lv_item_layout1.setLayoutParams(linearParams);
                listAdapter1.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onRefresh(int id) {

    }

    @Override
    public void onLoadMore(int id) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_j5_give:
                wechatShare(0);
                //showToastShort("点击");
                break;
        }
    }

    /**
     * 微信分享礼包
     */
    private void wechatShare(int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = gift_link;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        if (member_nickname.equals("")) {
            msg.title = member_name + "的大礼包";
        } else {
            msg.title = member_nickname + "的大礼包";
        }

        if (edNote.equals("")) {
            msg.description = "上等的好酒送给你";
        } else {
            msg.description = edNote;
        }
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_gift_logo);

        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    /**
     * 发出礼包领取情况品
     */
    private void send_goods_list() {
        StartLoading();
        final RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("gift_id", cab_gift_id);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Send_goods_list, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dismiss();
                Gson gson = new Gson();
                if (responseInfo != null) {
                    ssend_goods_list = gson.fromJson(responseInfo.result, Ssend_goods_list.class);
                    if (ssend_goods_list.status.code == 10000) {
                        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) lv_item_layout1.getLayoutParams();
                        if (ssend_goods_list.datas.gift_goods_list.size() > 3) {
                            linearParams.height = dip2px(J5_GiftDetailActivity.this, 124) * 3;// 当控件的高
                            loadmore = false;
                            ll_loadmore.setVisibility(View.VISIBLE);
                        } else {
                            linearParams.height = dip2px(J5_GiftDetailActivity.this, 124) * ssend_goods_list.datas.gift_goods_list.size();// 当控件的高
                        }

                        lv_item_layout1.setLayoutParams(linearParams);
                        lv_item_layout1.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return true;
                            }
                        });
                        listAdapter1 = new J5_sendAdapter(J5_GiftDetailActivity.this, ssend_goods_list.datas.gift_goods_list);
                        lv_item_layout1.setAdapter(listAdapter1);

                        LinearLayout.LayoutParams linearParams1 = (LinearLayout.LayoutParams) lv_item_layout2.getLayoutParams();
                        linearParams1.height = dip2px(J5_GiftDetailActivity.this, 68) * ssend_goods_list.datas.receive_list.size();// 当控件的高
                        lv_item_layout2.setLayoutParams(linearParams1);
                        lv_item_layout2.setOnTouchListener(new View.OnTouchListener() {


                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return true;
                            }
                        });
                        listAdapter2 = new J5_receiveAdapter(J5_GiftDetailActivity.this, ssend_goods_list.datas.receive_list);
                        lv_item_layout2.setAdapter(listAdapter2);

                        if (ssend_goods_list.datas.gift_info.limit_type.equals("1")) {
                            tv_limit_type.setText("多人礼包");
                        } else if (ssend_goods_list.datas.gift_info.limit_type.equals("0")) {
                            tv_limit_type.setText("单人礼包");
                        }
                        String gift_state = ssend_goods_list.datas.gift_info.gift_state;
                        if (gift_state.equals("0")) {
                            tv_gift_state.setText("无效");
                        } else if (gift_state.equals("1")) {
                            tv_gift_state.setText("进行中");
                            tv_j5_give.setBackgroundColor(Color.parseColor("#f25252"));
                            tv_j5_give.setOnClickListener(J5_GiftDetailActivity.this);
                            member_name = ssend_goods_list.datas.gift_info.member_name;
                            member_nickname = ssend_goods_list.datas.gift_info.member_nickname;
                            gift_link = ssend_goods_list.datas.gift_info.gift_link;
                            edNote = ssend_goods_list.datas.gift_info.gift_note;
                        } else if (gift_state.equals("2")) {
                            tv_gift_state.setText("已完成");
                        } else if (gift_state.equals("3")) {
                            tv_gift_state.setText("已取消");
                        } else if (gift_state.equals("4")) {
                            tv_gift_state.setText("已过期");
                        }

                        APP.getApp().getImageLoader().displayImage(ssend_goods_list.datas.
                                gift_info.member_avatar, iv_j5_use_img);
                        tv_member_nickname.setText(ssend_goods_list.datas.gift_info.member_nickname + "的礼包");
                        tv_gift_note.setText(ssend_goods_list.datas.gift_info.gift_note);

                        tv_j5_add_time.setText("送出的时间:" + ssend_goods_list.datas.gift_info.add_time);
                        tv_j5_receive_info.setText("已领取:" + ssend_goods_list.datas.gift_info.receive_info);
                        Double s = 0.00;
                        for (int i = 0; i < ssend_goods_list.datas.gift_goods_list.size(); i++) {
                            s += Double.parseDouble(ssend_goods_list.datas.gift_goods_list.get(i).goods_price)
                                    * Integer.parseInt(ssend_goods_list.datas.gift_goods_list.get(i).goods_num);
                        }
                        tv_j5_price.setText("¥" + new java.text.DecimalFormat("######0.00").format(s));
                    } else {
                        overtime(ssend_goods_list.status.code, ssend_goods_list.status.msg);
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
