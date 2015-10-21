package com.wjhgw.ui.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.bean.Auction_super_value;
import com.wjhgw.business.bean.GroupBuy;
import com.wjhgw.business.bean.GroupBuy_Data;
import com.wjhgw.business.bean.Guess_Like;
import com.wjhgw.business.bean.Guess_Like_Datas;
import com.wjhgw.business.bean.Home_Pager;
import com.wjhgw.business.bean.Home_Pager_Data;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.activity.A0_LoginActivity;
import com.wjhgw.ui.activity.PrductDetail;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView.IXListViewListener;
import com.wjhgw.ui.view.listview.adapter.HomePagerAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment implements IXListViewListener,
        View.OnClickListener, ViewPager.OnPageChangeListener {
    private View homeLayout;
    private LinearLayout MenuLayout;
    private LinearLayout Eventlayout;
    private LinearLayout Discountlayout;
    private LinearLayout Themelayout;
    private LinearLayout Brandlayout;
    private LinearLayout Guesslikelayout;
    private LinearLayout group_purchase_layout;
    private MyListView mListView;
    private RelativeLayout homeViewPageLayout;
    private ViewPager homePager;
    private HomePagerAdapter mPagerAdapter;
    private static final int HANDLERID = 1;
    private Handler handler;

    private LinearLayout ll_discount01;
    private LinearLayout ll_discount02;
    private LinearLayout ll_discount03;
    private TextView tv;
    private TextView tv1;
    private TextView tv2;
    private TextView time1;
    private TextView time2;
    private TextView time3;
    private TextView time4;
    private TextView time5;
    private TextView time6;
    private TextView time7;
    private TextView time8;
    private TextView time9;
    private TextView time10;
    private TextView time11;
    private TextView time12;
    private TextView time13;
    private TextView time14;
    private TextView time15;

    private TextView tv_home_click;
    private TextView tv_home_name;
    private ImageView iv_home_group_purchase;
    private ImageView iv_home_auction;

    private LinearLayout ll_Point;
    private ImageView point;
    private ImageView[] points;

    private Home_Pager home_pager;
    private List<Home_Pager_Data> data = new ArrayList<>();
    private GroupBuy groupBuys;
    private List<GroupBuy_Data> groupBuy_data = new ArrayList<>();
    private ImageView iv_discount01_image;
    private TextView tv_discount01_name;
    private TextView tv_discount01_price;
    private TextView tv_discount01_groupbuy_price;
    private ImageView iv_discount02_iamge;
    private TextView tv_discount02_name;
    private TextView tv_discount02_price;
    private TextView tv_discount02_groupbuy_price;
    private ImageView iv_discount03_iamge;
    private TextView tv_discount03_name;
    private TextView tv_discount03_price;
    private TextView tv_discount03_groupbuy_price;
    private Guess_Like guess_like;
    private List<Guess_Like_Datas> guess_like_datases = new ArrayList<>();
    private ImageView iv_guessLike01_image;
    private TextView tv_guessLike01_name;
    private TextView tv_guessLike01_price;
    private ImageView iv_guessLike02_image;
    private TextView tv_guessLike02_name;
    private TextView tv_guessLike02_price;
    private ImageView iv_guessLike03_image;
    private TextView tv_guessLike03_name;
    private TextView tv_guessLike03_price;
    private ImageView iv_guessLike04_image;
    private TextView tv_guessLike04_name;
    private TextView tv_guessLike04_price;

    private Auction_super_value auction_super_value;
    private TextView tvRefresh;
    private Timer timer;
    private static Long countdown1;
    private static Long countdown2;
    private static Long countdown3;
    private static Long countdown4;
    private static Long countdown5;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (countdown1 > 0) {
                        countdown1--;
                        secToTime(countdown1, 1);
                    }
                    break;
                case 2:
                    if (countdown2 > 0) {
                        countdown2--;
                        secToTime(countdown2, 2);
                    }
                    break;
                case 3:
                    if (countdown3 > 0) {
                        countdown3--;
                        secToTime(countdown3, 3);
                    }
                    break;
                case 4:
                    if (countdown4 > 0) {
                        countdown4--;
                        secToTime(countdown4, 4);
                    }
                    break;
                case 5:
                    if (countdown5 > 0) {
                        countdown5--;
                        secToTime(countdown5, 5);
                    }
                    break;
                default:
                    // 所做的操作
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeLayout = inflater.inflate(R.layout.home_layout, container, false);

        /**
         * 加载视图
         */
        setInflaterView();

        /**
         * 初始化控件
         */
        initView();
        homePager = (ViewPager) homeViewPageLayout.findViewById(R.id.pager);

        /**
         * 请求首页焦点图
         */
        loadHomePager();
        /**
         * 请求拍卖和团购数据
         */
        auction_super_value();
        /**
         * 请求折扣街数据
         */
        loadGroupBuy();

        /**
         *  请求猜你喜欢数据
         */
        loadGuessLike();
        homePager.addOnPageChangeListener(this);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == HANDLERID) {

                    homePager.setCurrentItem((homePager.getCurrentItem() + 1));

                    sendEmptyMessageDelayed(HANDLERID, 3000);
                }
            }
        };

        /**
         * 给ListView添加视图
         */
        listAddHeader();

        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
        mListView.setAdapter(null);

        /**
         * 设置监听事件
         */
        setClick();

        return homeLayout;
    }

    /**
     * 视图加载 要添加的视图
     */

    private void setInflaterView() {
        homeViewPageLayout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.home_page_layout, null);
        MenuLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.index_menu, null);
        Eventlayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.event_layout, null);
        Discountlayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.home_discount_layout, null);
        Themelayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.theme_layout, null);
        Brandlayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.brand_layout, null);
        Guesslikelayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.guess_like_layout, null);

        group_purchase_layout = (LinearLayout) Eventlayout.findViewById(R.id.group_purchase_layout);
    }

    /**
     * 初始化视图
     */
    private void initView() {

        homePager = (ViewPager) homeViewPageLayout.findViewById(R.id.pager);
        ll_Point = (LinearLayout) homeViewPageLayout.findViewById(R.id.ll_home_point);


        ll_discount01 = (LinearLayout) Discountlayout.findViewById(R.id.ll_discount_01);
        ll_discount02 = (LinearLayout) Discountlayout.findViewById(R.id.ll_discount_02);
        ll_discount03 = (LinearLayout) Discountlayout.findViewById(R.id.ll_discount_03);
        iv_discount01_image = (ImageView) Discountlayout.findViewById(R.id.iv_discount01_iamge);
        tv_discount01_name = (TextView) Discountlayout.findViewById(R.id.tv_discount01_name);
        tv_discount01_price = (TextView) Discountlayout.findViewById(R.id.tv_discount01_price);
        tv_discount01_groupbuy_price = (TextView) Discountlayout.findViewById(R.id.tv_discount01_groupbuy_price);
        time7 = (TextView) Discountlayout.findViewById(R.id.time7);
        time8 = (TextView) Discountlayout.findViewById(R.id.time8);
        time9 = (TextView) Discountlayout.findViewById(R.id.time9);
        time10 = (TextView) Discountlayout.findViewById(R.id.time10);
        time11 = (TextView) Discountlayout.findViewById(R.id.time11);
        time12 = (TextView) Discountlayout.findViewById(R.id.time12);
        time13 = (TextView) Discountlayout.findViewById(R.id.time13);
        time14 = (TextView) Discountlayout.findViewById(R.id.time14);
        time15 = (TextView) Discountlayout.findViewById(R.id.time15);

        iv_discount02_iamge = (ImageView) Discountlayout.findViewById(R.id.iv_discount02_image);
        tv_discount02_name = (TextView) Discountlayout.findViewById(R.id.tv_discount02_name);
        tv_discount02_price = (TextView) Discountlayout.findViewById(R.id.tv_discount02_price);
        tv_discount02_groupbuy_price = (TextView) Discountlayout.findViewById(R.id.tv_discount02_groupbuy_price);

        tv = (TextView) Discountlayout.findViewById(R.id.tv_discount01_groupbuy_price);
        tv1 = (TextView) Discountlayout.findViewById(R.id.tv_discount02_groupbuy_price);
        tv2 = (TextView) Discountlayout.findViewById(R.id.tv_discount03_groupbuy_price);
        iv_home_group_purchase = (ImageView) Eventlayout.findViewById(R.id.iv_home_group_purchase);
        iv_home_auction = (ImageView) Eventlayout.findViewById(R.id.iv_home_auction);
        tv_home_click = (TextView) Eventlayout.findViewById(R.id.tv_home_click);
        tv_home_name = (TextView) Eventlayout.findViewById(R.id.tv_home_name);
        time1 = (TextView) Eventlayout.findViewById(R.id.time1);
        time2 = (TextView) Eventlayout.findViewById(R.id.time2);
        time3 = (TextView) Eventlayout.findViewById(R.id.time3);
        time4 = (TextView) Eventlayout.findViewById(R.id.time4);
        time5 = (TextView) Eventlayout.findViewById(R.id.time5);
        time6 = (TextView) Eventlayout.findViewById(R.id.time6);
        iv_discount03_iamge = (ImageView) Discountlayout.findViewById(R.id.iv_discount03_image);
        tv_discount03_name = (TextView) Discountlayout.findViewById(R.id.tv_discount03_name);
        tv_discount03_price = (TextView) Discountlayout.findViewById(R.id.tv_discount03_price);
        tv_discount03_groupbuy_price = (TextView) Discountlayout.findViewById(R.id.tv_discount03_groupbuy_price);

        iv_guessLike01_image = (ImageView) Guesslikelayout.findViewById(R.id.iv_guess_like01_image);
        tv_guessLike01_name = (TextView) Guesslikelayout.findViewById(R.id.tv_guess_like01_name);
        tv_guessLike01_price = (TextView) Guesslikelayout.findViewById(R.id.tv_guess_like01_price);

        iv_guessLike02_image = (ImageView) Guesslikelayout.findViewById(R.id.iv_guess_like02_image);
        tv_guessLike02_name = (TextView) Guesslikelayout.findViewById(R.id.tv_guess_like02_name);
        tv_guessLike02_price = (TextView) Guesslikelayout.findViewById(R.id.tv_guess_like02_price);

        iv_guessLike03_image = (ImageView) Guesslikelayout.findViewById(R.id.iv_guess_like03_image);
        tv_guessLike03_name = (TextView) Guesslikelayout.findViewById(R.id.tv_guess_like03_name);
        tv_guessLike03_price = (TextView) Guesslikelayout.findViewById(R.id.tv_guess_like03_price);

        iv_guessLike04_image = (ImageView) Guesslikelayout.findViewById(R.id.iv_guess_like04_image);
        tv_guessLike04_name = (TextView) Guesslikelayout.findViewById(R.id.tv_guess_like04_name);
        tv_guessLike04_price = (TextView) Guesslikelayout.findViewById(R.id.tv_guess_like04_price);

        tvRefresh = (TextView) Guesslikelayout.findViewById(R.id.tv_guess_refresh);

        tv = (TextView) Discountlayout.findViewById(R.id.tv_discount01_groupbuy_price);
        tv1 = (TextView) Discountlayout.findViewById(R.id.tv_discount02_groupbuy_price);
        tv2 = (TextView) Discountlayout.findViewById(R.id.tv_discount03_groupbuy_price);
        tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//文件中间加下划线，加上后面的属性字体更清晰一些
        tv1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//文件中间加下划线，加上后面的属性字体更清晰一些
        tv2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//文件中间加下划线，加上后面的属性字体更清晰一些
    }

    /**
     * 添加圆点
     */
    private void addPoints() {
        int size = data.size();
        points = new ImageView[size];

        for (int i = 0; i < points.length; i++) {
            point = new ImageView(getActivity());

            point.setLayoutParams(new LinearLayout.LayoutParams(20, 20));
            points[i] = point;

            if (i == 0) {
                points[i]
                        .setBackgroundResource(R.mipmap.dot_select);
            } else {
                points[i]
                        .setBackgroundResource(R.mipmap.dot_unselect);
            }
            // 添加圆点到容器
            ll_Point.addView(point);
        }
    }

    /**
     * 设置监听事件
     */
    private void setClick() {
        group_purchase_layout.setOnClickListener(this);

        ll_discount01.setOnClickListener(this);
        ll_discount02.setOnClickListener(this);
        ll_discount03.setOnClickListener(this);

        tvRefresh.setOnClickListener(this);
    }

    /**
     * 给ListView添加视图
     */
    private void listAddHeader() {
        mListView = (MyListView) homeLayout.findViewById(R.id.home_listview);
        mListView.addHeaderView(homeViewPageLayout);
        mListView.addHeaderView(MenuLayout);
        mListView.addHeaderView(Eventlayout);
        mListView.addHeaderView(Discountlayout);
        mListView.addHeaderView(Themelayout);
        mListView.addHeaderView(Brandlayout);
        mListView.addHeaderView(Guesslikelayout);
    }

    @Override
    public void onRefresh(int id) {

    }

    @Override
    public void onLoadMore(int id) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.group_purchase_layout:
                intent.setClass(getActivity(), A0_LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_discount_01:

                intent.setClass(getActivity(), PrductDetail.class);
                startActivity(intent);
                break;
            case R.id.ll_discount_02:

                intent.setClass(getActivity(), PrductDetail.class);
                startActivity(intent);
                break;
            case R.id.ll_discount_03:

                intent.setClass(getActivity(), PrductDetail.class);
                startActivity(intent);
                break;
            case R.id.tv_guess_refresh:

                loadGuessLike();
                break;

            default:
                break;

        }
    }

    //ViewPager 监听
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        for (int i = 0; i < points.length; i++) {
            if (i == position % data.size()) {
                points[i].setBackgroundResource(R.mipmap.dot_select);
            } else {
                points[i].setBackgroundResource(R.mipmap.dot_unselect);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 请求首页焦点图
     */
    private void loadHomePager() {
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Home_pager, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    home_pager = gson.fromJson(responseInfo.result, Home_Pager.class);

                    if (home_pager.status.code == 10000) {
                        data.clear();

                        if (home_pager.datas != null) {
                            data.addAll(home_pager.datas);
                        }
                    }
                }
                if (data.size() != 0 && data != null) {

                    //适配首页焦点图
                    mPagerAdapter = new HomePagerAdapter(getActivity(), data);
                    homePager.setAdapter(mPagerAdapter);
                    /**
                     * 添加圆点
                     */
                    addPoints();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 拍卖和团购请求
     */
    private void auction_super_value() {
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Auction_super_value, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Gson gson = new Gson();
                if (responseInfo != null) {
                    auction_super_value = gson.fromJson(responseInfo.result, Auction_super_value.class);

                    if (auction_super_value.status.code == 10000) {
                        APP.getApp().getImageLoader().displayImage(auction_super_value.datas.super_value_goods.ap_ad_image, iv_home_group_purchase, APP.getApp().getImageOptions());
                        APP.getApp().getImageLoader().displayImage(auction_super_value.datas.auction_goods.goods_image, iv_home_auction, APP.getApp().getImageOptions());
                        countdown1 = auction_super_value.datas.super_value_goods.count_dowm_time;
                        countdown2 = auction_super_value.datas.auction_goods.count_dowm_time;
                        tv_home_click.setText(auction_super_value.datas.auction_goods.goods_click + "次围观");
                        tv_home_name.setText(auction_super_value.datas.auction_goods.goods_name);
                        Countdown(1);
                        Countdown(2);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    /**
     * 请求折扣街数据
     */
    private void loadGroupBuy() {
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Group_Buy, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Gson gson = new Gson();
                if (responseInfo != null) {
                    groupBuys = gson.fromJson(responseInfo.result, GroupBuy.class);

                    if (groupBuys.status.code == 10000) {
                        groupBuy_data.clear();

                        if (groupBuys.datas != null) {
                            groupBuy_data.addAll(groupBuys.datas);

                            String imagUrl = groupBuy_data.get(0).groupbuy_image;
                            APP.getApp().getImageLoader().displayImage(imagUrl, iv_discount01_image);
                            tv_discount01_name.setText(groupBuy_data.get(0).goods_name);
                            tv_discount01_price.setText("¥" + groupBuy_data.get(0).goods_price);
                            tv_discount01_groupbuy_price.setText("¥" + groupBuy_data.get(0).groupbuy_price);
                            countdown3 = groupBuy_data.get(0).count_down;
                            Countdown(3);


                            String imagUrl2 = groupBuy_data.get(1).groupbuy_image;
                            APP.getApp().getImageLoader().displayImage(imagUrl2, iv_discount02_iamge);
                            tv_discount02_name.setText(groupBuy_data.get(1).goods_name);
                            tv_discount02_price.setText("¥" + groupBuy_data.get(1).goods_price);
                            tv_discount02_groupbuy_price.setText("¥" + groupBuy_data.get(1).groupbuy_price);
                            countdown4 = groupBuy_data.get(1).count_down;
                            Countdown(4);

                            String imagUrl3 = groupBuy_data.get(2).groupbuy_image;
                            APP.getApp().getImageLoader().displayImage(imagUrl3, iv_discount03_iamge);
                            tv_discount03_name.setText(groupBuy_data.get(2).goods_name);
                            tv_discount03_price.setText("¥" + groupBuy_data.get(2).goods_price);
                            tv_discount03_groupbuy_price.setText("¥" + groupBuy_data.get(2).groupbuy_price);
                            countdown5 = groupBuy_data.get(2).count_down;
                            Countdown(5);
                        }

                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 请求猜你喜欢数据
     */
    private void loadGuessLike() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("num", "4");

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Guess_Like, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    guess_like = gson.fromJson(responseInfo.result, Guess_Like.class);

                    if (guess_like != null && guess_like.status.code == 10000) {
                        guess_like_datases.clear();

                        if (guess_like.datas != null) {
                            guess_like_datases.addAll(guess_like.datas);

                            APP.getApp().getImageLoader().displayImage(guess_like_datases.get(0).goods_image, iv_guessLike01_image);
                            tv_guessLike01_name.setText(guess_like_datases.get(0).goods_name);
                            tv_guessLike01_price.setText(guess_like_datases.get(0).goods_price);

                            APP.getApp().getImageLoader().displayImage(guess_like_datases.get(1).goods_image, iv_guessLike02_image);
                            tv_guessLike02_name.setText(guess_like_datases.get(1).goods_name);
                            tv_guessLike02_price.setText(guess_like_datases.get(1).goods_price);

                            APP.getApp().getImageLoader().displayImage(guess_like_datases.get(2).goods_image, iv_guessLike03_image);
                            tv_guessLike03_name.setText(guess_like_datases.get(2).goods_name);
                            tv_guessLike03_price.setText(guess_like_datases.get(2).goods_price);

                            APP.getApp().getImageLoader().displayImage(guess_like_datases.get(3).goods_image, iv_guessLike04_image);
                            tv_guessLike04_name.setText(guess_like_datases.get(3).goods_name);
                            tv_guessLike04_price.setText(guess_like_datases.get(3).goods_price);
                        }
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

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
     * @param s       1代表团购，2代表拍卖,3为折扣街商品1,4为折扣街商品2，5为折扣街商品3
     */
    public void secToTime(long timeStr, int s) {
        if (timeStr > 0) {
            long hours = Math.abs((timeStr) / (60 * 60));
            long minutes = Math.abs((timeStr - hours * 60 * 60) / 60);
            long seconds = Math
                    .abs((timeStr - hours * 60 * 60 - minutes * 60));
            DecimalFormat df = new DecimalFormat("000");
            if (s == 1) {
                time1.setText(df.format(hours));
                df = new DecimalFormat("00");
                time2.setText(df.format(minutes));
                time3.setText(df.format(seconds));
            } else if (s == 2) {
                time4.setText(df.format(hours));
                df = new DecimalFormat("00");
                time5.setText(df.format(minutes));
                time6.setText(df.format(seconds));
            } else if (s == 3) {
                time7.setText(df.format(hours));
                df = new DecimalFormat("00");
                time8.setText(df.format(minutes));
                time9.setText(df.format(seconds));
            } else if (s == 4) {
                time10.setText(df.format(hours));
                df = new DecimalFormat("00");
                time11.setText(df.format(minutes));
                time12.setText(df.format(seconds));
            } else if (s == 5) {
                time13.setText(df.format(hours));
                df = new DecimalFormat("00");
                time14.setText(df.format(minutes));
                time15.setText(df.format(seconds));
            }
        } else {
            if (s == 1) {
                time1.setText("000");
                time3.setText("00");
                time2.setText("00");
            } else if (s == 2) {
                time4.setText("000");
                time5.setText("00");
                time6.setText("00");
            } else if (s == 3) {
                time7.setText("000");
                time8.setText("00");
                time9.setText("00");
            } else if (s == 4) {
                time10.setText("000");
                time11.setText("00");
                time12.setText("00");
            } else if (s == 5) {
                time13.setText("000");
                time14.setText("00");
                time15.setText("00");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(HANDLERID, 3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeMessages(HANDLERID);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(HANDLERID);
    }
}
