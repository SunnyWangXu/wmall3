package com.wjhgw.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.wjhgw.R;
import com.wjhgw.business.api.Goods_Request;
import com.wjhgw.business.response.BusinessResponse;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.activity.A0_LoginActivity;
import com.wjhgw.ui.image.LoadImageByVolley;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView.IXListViewListener;
import com.wjhgw.ui.view.listview.adapter.DiscountPagerAdapter;
import com.wjhgw.ui.view.listview.adapter.IndexPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class IndexFragment extends Fragment implements BusinessResponse, IXListViewListener,
        View.OnClickListener, RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private static final int SCROLL_COUNT = 3;
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
    private ViewPager discountViewPager;
    private LinearLayout viewPagerContainer;
    private Intent intent;


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


        // pageCount设置缓存的页面数
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


        discountViewPager.setAdapter(new DiscountPagerAdapter(getActivity()));

        dataModel = new Goods_Request(getActivity());
        dataModel.addResponseListener(this);

        indexPager.addOnPageChangeListener(this);
        group.setOnCheckedChangeListener(this);

        group_purchase_layout.setOnClickListener(this);

//        new Handler().sendMessageDelayed();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == HANDLERID) {

                    indexPager.setCurrentItem(indexPager.getCurrentItem() + 1);

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

        return homeLayout;
    }

    private void initView() {
        discountViewPager = (ViewPager) Discountlayout.findViewById(R.id.viewpager_discount);


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

    /**
     * 视图加载 要添加的视图
     */

    private void setInflaterView() {
        MenuLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.index_menu, null);
        Eventlayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.event_layout, null);
        Discountlayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.index_discount__layout, null);
        Themelayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.theme_layout, null);
        Brandlayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.brand_layout, null);
        Guesslikelayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.guess_like_layout, null);
        indexViewPageLayout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.index_page_layout, null);

        indexPager = (ViewPager) indexViewPageLayout.findViewById(R.id.pager);
        group_purchase_layout = (LinearLayout) Eventlayout.findViewById(R.id.group_purchase_layout);
        mPagerAdapter = new IndexPagerAdapter(getActivity());
        indexPager.setAdapter(mPagerAdapter);
        group = (RadioGroup) indexViewPageLayout.findViewById(R.id.radio_group);


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

        switch (v.getId()){
            case R.id.group_purchase_layout:
                intent = new Intent(getActivity(), A0_LoginActivity.class);
                startActivity(intent);
                break;
            default:
                break;

        }
    }

    //RadioGroup监听
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == indexPager.getCurrentItem()) {
            indexPager.setCurrentItem(checkedId);
        }
    }

    //ViewPager 监听
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int[] pointIDs = {R.id.point_1, R.id.point_2, R.id.point_3, R.id.point_4};
        group.check(pointIDs[position % 4]);

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onStart() {
        super.onStart();
        handler.sendEmptyMessageDelayed(HANDLERID, 3000);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(HANDLERID);
    }
}
