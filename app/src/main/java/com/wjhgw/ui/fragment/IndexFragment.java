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
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.business.api.Goods_Request;
import com.wjhgw.business.bean.Index_Pager;
import com.wjhgw.business.bean.Index_Pager_data;
import com.wjhgw.business.response.BusinessResponse;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.activity.A0_LoginActivity;
import com.wjhgw.ui.activity.PrductDetail;
import com.wjhgw.ui.image.LoadImageByVolley;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView.IXListViewListener;
import com.wjhgw.ui.view.listview.adapter.IndexPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IndexFragment extends Fragment implements BusinessResponse, IXListViewListener,
        View.OnClickListener, ViewPager.OnPageChangeListener {
    //    private static final int SCROLL_COUNT = 3;
    private View homeLayout;
    private LinearLayout MenuLayout;
    private LinearLayout Eventlayout;
    private LinearLayout Discountlayout;
    private LinearLayout Themelayout;
    private LinearLayout Brandlayout;
    private LinearLayout Guesslikelayout;
    private LinearLayout group_purchase_layout;
    private LoadImageByVolley load;
    private Goods_Request dataModel;
    private MyListView mListView;
    private RelativeLayout indexViewPageLayout;
    private ViewPager indexPager;
    private IndexPagerAdapter mPagerAdapter;
    private RadioGroup group;
    private static final int HANDLERID = 1;
    private Handler handler;
//    private ViewPager discountViewPager;
//    private LinearLayout viewPagerContainer;

    private LinearLayout ll_discount01;
    private LinearLayout ll_discount02;
    private LinearLayout ll_discount03;
    private TextView tv;
    private TextView tv1;
    private TextView tv2;

    private List<ImageView> imageViews;
    private LinearLayout ll_Point;
    private ImageView point;
    private ImageView[] points;

    private Index_Pager index_pager;
    private List<Index_Pager_data> data = new ArrayList<Index_Pager_data>();
    private Index_Pager pagers;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeLayout = inflater.inflate(R.layout.index_layout, container, false);

        /**
         * 加载视图
         */
        setInflaterView();

        /**
         * 初始化控件
         */
        initView();
        indexPager = (ViewPager) indexViewPageLayout.findViewById(R.id.pager);

        /**
         * 请求首页焦点图
         */
        loadIndexPager();


   /*     // pageCount设置缓存的页面数
        discountViewPager.setOffscreenPageLimit(SCROLL_COUNT);
        // 设置2张图之前的间距。
        discountViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.page_margin));
        viewPagerContainer = (LinearLayout) Discountlayout.findViewById(R.id.viewPagerContainer);
        viewPagerContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return discountViewPager.dispatchTouchEvent(event);
            }
        });
        discountViewPager.setAdapter(new DiscountPagerAdapter(getActivity()));*/

        dataModel = new Goods_Request(getActivity());
        dataModel.addResponseListener(this);

        indexPager.addOnPageChangeListener(this);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == HANDLERID) {

                    indexPager.setCurrentItem((indexPager.getCurrentItem() + 1));

                    sendEmptyMessageDelayed(HANDLERID, 3000);
                }
            }
        };

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

    private void loadIndexPager() {
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, ApiInterface.Index_pager, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    index_pager = gson.fromJson(responseInfo.result, Index_Pager.class);

                    if (index_pager.status.code == 10000) {
                        data.addAll(index_pager.datas);
                    }
                }

                //适配首页焦点图
                mPagerAdapter = new IndexPagerAdapter(getActivity(), data);
                indexPager.setAdapter(mPagerAdapter);

                if(data != null){
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
     * 视图加载 要添加的视图
     */

    private void setInflaterView() {
        indexViewPageLayout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.index_page_layout, null);
        MenuLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.index_menu, null);
        Eventlayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.event_layout, null);
        Discountlayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.index_discount__layout, null);
        Themelayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.theme_layout, null);
        Brandlayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.brand_layout, null);
        Guesslikelayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.guess_like_layout, null);

        group_purchase_layout = (LinearLayout) Eventlayout.findViewById(R.id.group_purchase_layout);
    }

    /**
     * 初始化视图
     */
    private void initView() {

        ll_Point = (LinearLayout) indexViewPageLayout.findViewById(R.id.ll_index_point);


//        discountViewPager = (ViewPager) Discountlayout.findViewById(R.id.viewpager_discount);
        ll_discount01 = (LinearLayout) Discountlayout.findViewById(R.id.ll_discount_01);
        ll_discount02 = (LinearLayout) Discountlayout.findViewById(R.id.ll_discount_02);
        ll_discount03 = (LinearLayout) Discountlayout.findViewById(R.id.ll_discount_03);

        tv = (TextView) Discountlayout.findViewById(R.id.tv_A0_discount01_original_price);
        tv1 = (TextView) Discountlayout.findViewById(R.id.tv_A0_discount02_original_price);
        tv2 = (TextView) Discountlayout.findViewById(R.id.tv_A0_discount03_original_price);
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
    }

    /**
     * 给ListView添加视图
     */
    private void listAddHeader() {
        mListView = (MyListView) homeLayout.findViewById(R.id.index_listview);
        mListView.addHeaderView(indexViewPageLayout);
        mListView.addHeaderView(MenuLayout);
        mListView.addHeaderView(Eventlayout);
        mListView.addHeaderView(Discountlayout);
        mListView.addHeaderView(Themelayout);
        mListView.addHeaderView(Brandlayout);
        mListView.addHeaderView(Guesslikelayout);
    }


    @Override
    public void OnMessageResponse(String url, String response, JSONObject status) throws JSONException {
        if (url.equals(ApiInterface.Goods_list)) {
            /*if (status == null){
                ArrayList<goods_list_data> list= dataModel.goodsList;
				String goodslist = list.get(1).goods_name;
				String s = list.get(1).goods_id;
				String s1 = list.get(1).goods_id;
			}else{
				if (status.getString("code").equals("10000")) {
					ArrayList<goods_list_data> list= dataModel.goodsList;
					String goodslist = list.get(1).goods_name;
					String s = list.get(1).goods_id;
					Toast.makeText(getActivity(), status.getString("msg"), Toast.LENGTH_LONG).show();
				} else if (status.getString("code").equals("600100")) {
					Toast.makeText(getActivity(), status.getString("msg"), Toast.LENGTH_LONG).show();
				}
			}*/

        }
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
