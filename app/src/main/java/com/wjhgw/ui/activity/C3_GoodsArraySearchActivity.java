package com.wjhgw.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.wjhgw.business.api.Address_del_Request;
import com.wjhgw.business.bean.ActSearch;
import com.wjhgw.business.bean.ActSearch_datas;
import com.wjhgw.business.response.BusinessResponse;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.dialog.GoodsArrDialog;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.ArrSearchAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品排列查询
 */
public class C3_GoodsArraySearchActivity extends BaseActivity implements BusinessResponse,XListView.IXListViewListener,
        View.OnClickListener, AbsListView.OnScrollListener, View.OnTouchListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    private ImageView IvBack;
    private LinearLayout ll_GoodsArrSearch;
    private MyListView mListView;
    private String b_id;
    private String a_id;
    //当前页码数
    private int curpage = 1;
    private List<ActSearch_datas> actSearch_datas = new ArrayList<>();
    private TextView tvSaleNum;
    private LinearLayout llPriceArr;
    private TextView tvMoods;
    private String k;
    private String order;
    private ArrSearchAdapter arrSearchAdapter;
    private TextView tvSearchDef;
    private TextView tvPriceArr;
    //点击价格的次数
    private int PriceCount = 0;
    private ImageView ivPriceArrow;
    //ListView 是刷新状态还是加载更多状态
    private Boolean isSetAdapter = true;
    private TextView tvPageTotal;
    private ImageView ivArrBack;
    private int pageTotal;
    private Boolean hasMore;
    private float downY;
    private float upY;
    private LinearLayout ll_GoodsSearchTab;
    private LinearLayout llGoodsSearch;
    private String keyword;
    private String cate_id;
    private Address_del_Request Request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsarray_search);

        b_id = getIntent().getStringExtra("b_id");
        a_id = getIntent().getStringExtra("a_id");

        keyword = getIntent().getStringExtra("keyword");
        cate_id = getIntent().getStringExtra("cate_id");

        /**
         * 请求商品排序进来为默认排序
         */
        loadSearchGoodsArr();

        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
        mListView.setAdapter(null);
        mListView.setOnScrollListener(this);
        mListView.setOnTouchListener(this);
        mListView.setOnItemLongClickListener(this);

        Request = new Address_del_Request(this);
        Request.addResponseListener(this);
    }


    @Override
    public void onInit() {
        mListView = (MyListView) findViewById(R.id.lv_array_search);

//        ll_GoodsArrSearch = (LinearLayout) getLayoutInflater().inflate(R.layout.goods_arr_search, null);
        ll_GoodsSearchTab = (LinearLayout) getLayoutInflater().inflate(R.layout.goods_arr_tab, null);
//        mListView.addHeaderView(ll_GoodsArrSearch);
        mListView.addHeaderView(ll_GoodsSearchTab);
    }

    @Override
    public void onFindViews() {
        IvBack = (ImageView) findViewById(R.id.iv_array_back);
        llGoodsSearch = (LinearLayout)findViewById(R.id.ll_goods_search);

        tvSearchDef = (TextView) findViewById(R.id.tv_search_default);
        tvSaleNum = (TextView) findViewById(R.id.tv_salenum);
        llPriceArr = (LinearLayout) findViewById(R.id.ll_price_arr);
        tvPriceArr = (TextView) findViewById(R.id.tv_price_arr);
        tvMoods = (TextView) findViewById(R.id.tv_moods);
        ivPriceArrow = (ImageView) findViewById(R.id.iv_price_arrow);

        tvPageTotal = (TextView) findViewById(R.id.tv_page_total);
        ivArrBack = (ImageView) findViewById(R.id.iv_goods_arr_back);

    }

    @Override
    public void onInitViewData() {
        tvSearchDef.setTextColor(Color.parseColor("#d63235"));
    }

    @Override
    public void onBindListener() {
        IvBack.setOnClickListener(this);
        llGoodsSearch.setOnClickListener(this);
        tvSearchDef.setOnClickListener(this);
        tvSaleNum.setOnClickListener(this);
        llPriceArr.setOnClickListener(this);
        tvMoods.setOnClickListener(this);
        ivArrBack.setOnClickListener(this);

        mListView.setLongClickable(true);
        mListView.setOnItemLongClickListener(this);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_array_back:
                finish();
                break;

            case R.id.ll_goods_search:
                Intent intent = new Intent(this, C2_SearchActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_search_default:
                clearTvstate();
                tvSearchDef.setTextColor(Color.parseColor("#d63235"));
                PriceCount = 0;
                k = null;
                order = null;
                loadSearchGoodsArr();
                break;

            case R.id.tv_salenum:
                clearTvstate();
                tvSaleNum.setTextColor(Color.parseColor("#d63235"));
                PriceCount = 0;
                k = "1";
                order = "2";
                loadSearchGoodsArr();
                break;

            case R.id.ll_price_arr:
                PriceCount++;
                clearTvstate();
                tvPriceArr.setTextColor(Color.parseColor("#d63235"));
                k = "3";
                if (PriceCount % 2 == 1) {
                    order = "1";
                    ivPriceArrow.setImageResource(R.mipmap.ic_array_default_up);
                } else {
                    order = "2";
                    ivPriceArrow.setImageResource(R.mipmap.ic_array_default_down);
                }
                loadSearchGoodsArr();

                break;

            case R.id.tv_moods:
                clearTvstate();
                tvMoods.setTextColor(Color.parseColor("#d63235"));
                PriceCount = 0;
                k = "2";
                order = "2";
                loadSearchGoodsArr();

                break;

            case R.id.iv_goods_arr_back:
                mListView.setSelection(1);
                ivArrBack.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    /**
     * 清除TextView和价格图片的状态的状态
     */
    private void clearTvstate() {
        tvMoods.setTextColor(Color.parseColor("#666666"));
        tvPriceArr.setTextColor(Color.parseColor("#666666"));
        tvSaleNum.setTextColor(Color.parseColor("#666666"));
        tvSearchDef.setTextColor(Color.parseColor("#666666"));
        ivPriceArrow.setImageResource(R.mipmap.ic_array_default);
    }

    @Override
    public void onRefresh(int id) {
        isSetAdapter = true;
        curpage = 1;
        /**
         *刷新再请求
         */
        loadSearchGoodsArr();

    }

    /**
     * 加载更多
     *
     * @param id
     */
    @Override
    public void onLoadMore(int id) {
        isSetAdapter = false;
        curpage++;
        loadSearchGoodsArr();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: //停止...
                tvPageTotal.setVisibility(View.GONE);
                /**
                 *  向下滑返回图标消失 并且导航栏出现
                 */
              /*  if(upY -downY > 0){
                    ivArrBack.setVisibility(View.GONE);
                }*/
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://滚动过程中...
                /**
                 * 有下一页才去显示
                 */
                if (hasMore) {
                    tvPageTotal.setVisibility(View.VISIBLE);
                    /**
                     * 向上滑动返回图标显示 并且导航栏消失
                     */
                    if (upY - downY < 0) {
                        ivArrBack.setVisibility(View.VISIBLE);
                    }

                    tvPageTotal.setText(curpage + "/" + pageTotal);

                }

                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING://开始滚动...

                break;
        }
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    //测算滑动Y距离
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                upY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                break;

            default:
                break;
        }


        return false;
    }

    /**
     * 长按ListView的Item事件
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        /**
         *对话框
         */
        final GoodsArrDialog arrDialog = new GoodsArrDialog(this, "收藏", "加入购物车");
        arrDialog.show();

        arrDialog.btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request.favorites_add(actSearch_datas.get(position-2).goods_id,getKey());
                arrDialog.dismiss();
            }
        });
        arrDialog.btnGoodsarrAddshopcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request.cart_add(actSearch_datas.get(position-2).goods_id, getKey());
                arrDialog.dismiss();
            }
        });

        return true;
    }

    /**
     * 请求商品排序
     */
    private void loadSearchGoodsArr() {
        super.StartLoading();
        RequestParams params = new RequestParams();
        if (b_id != null) {
            params.addBodyParameter("b_id", b_id);
        }
        if (a_id != null) {
            params.addBodyParameter("a_id", a_id);
        }
        params.addBodyParameter("page", "10");
        params.addBodyParameter("curpage", curpage + "");
        if (k != null) {
            params.addBodyParameter("k", k);
        }
        if (order != null) {
            params.addBodyParameter("order", order);
        }

        if (keyword != null) {
            params.addBodyParameter("keyword", keyword);
        }
        if (cate_id != null) {
            params.addBodyParameter("cate_id", cate_id);
        }
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Act_search, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                C3_GoodsArraySearchActivity.super.Dismiss();
                Gson gson = new Gson();
                if (responseInfo != null) {
                    ActSearch actSearch = gson.fromJson(responseInfo.result, ActSearch.class);
                    if (actSearch.status.code == 10000) {
                        mListView.stopRefresh();
                        mListView.stopLoadMore();
                        mListView.setRefreshTime();

                        if (actSearch.datas != null) {
                            if (actSearch_datas.size() > 0 && isSetAdapter) {
                                actSearch_datas.clear();
                            }
                            actSearch_datas.addAll(actSearch.datas);
                            //总共有多少页
                            pageTotal = actSearch.pagination.page_total;
                            //是否有下一页
                            hasMore = actSearch.pagination.hasmore;

                            if (isSetAdapter) {
                                arrSearchAdapter = new ArrSearchAdapter(C3_GoodsArraySearchActivity.this, actSearch_datas);
                                mListView.setAdapter(arrSearchAdapter);
                            } else {
                                arrSearchAdapter.actSearch_datas = actSearch_datas;
                                arrSearchAdapter.notifyDataSetChanged();
                            }

                            if (actSearch.pagination.hasmore) {
                                mListView.setPullLoadEnable(true);
                            } else {
                                mListView.setPullLoadEnable(false);
                            }
                        }
                    }else {
                        overtime(actSearch.status.code,actSearch.status.msg);
                    }

                }

            }

            @Override
            public void onFailure(HttpException e, String s) {

                showToastShort("网络错误");
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String goods_id = actSearch_datas.get(position - 2 ).goods_id;
        Intent intent = new Intent(C3_GoodsArraySearchActivity.this, PrductDetailActivity.class);
        intent.putExtra("goods_id", goods_id);
        startActivity(intent);


    }

    @Override
    public void OnMessageResponse(String url, String response, JSONObject status) throws JSONException {
        showToastShort("成功");
    }
}
