package com.wjhgw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.wjhgw.business.bean.DeleMyCollect;
import com.wjhgw.business.bean.Goods_list;
import com.wjhgw.business.bean.MyCollect;
import com.wjhgw.business.response.BusinessResponse;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.dialog.GoodsArrDialog;
import com.wjhgw.ui.dialog.MyDialog;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.MyCollectAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 我的收藏
 */
public class M7_MyCollectActivity extends BaseActivity implements XListView.IXListViewListener, View.OnClickListener,
        BusinessResponse, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private int CURPAGE = 1;
    private MyListView lvMyCollect;
    private TextView tvCollectShop;
    private ArrayList<Goods_list> goodsList = new ArrayList<>();
    private boolean isRefressh = true;
    private MyCollectAdapter myCollectAdapter;
    private GoodsArrDialog goodsArrDialog;
    private Address_del_Request Request;
    private String fav_id;
    private MyDialog myDialog;
    private LinearLayout llNull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        /**
         * 请求收藏列表
         */
        loadMyCollect();

        Request = new Address_del_Request(this);
        Request.addResponseListener(this);
    }


    @Override
    public void onInit() {
        setUp();
        setTitle("我的收藏");

    }

    @Override
    public void onFindViews() {
        lvMyCollect = (MyListView) findViewById(R.id.lv_my_collect);
        llNull = (LinearLayout) findViewById(R.id.ll_null);
        tvCollectShop = (TextView) findViewById(R.id.tv_collect_shop);

    }

    @Override
    public void onInitViewData() {
        lvMyCollect.setPullLoadEnable(false);
        lvMyCollect.setPullRefreshEnable(true);
        lvMyCollect.setXListViewListener(this, 1);
        lvMyCollect.setRefreshTime();
        lvMyCollect.setAdapter(null);
        lvMyCollect.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBindListener() {

        tvCollectShop.setOnClickListener(this);

        lvMyCollect.setOnItemClickListener(this);
        lvMyCollect.setOnItemLongClickListener(this);
    }

    @Override
    public void onRefresh(int id) {
        isRefressh = true;
        CURPAGE = 1;
        loadMyCollect();
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMore(int id) {
        isRefressh = false;
        CURPAGE++;
        loadMyCollect();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_collect_shop:

                final Under_developmentDialog underdevelopmentDialog1 = new Under_developmentDialog(this, "功能正在开发中,敬请期待");
                underdevelopmentDialog1.show();
                underdevelopmentDialog1.tv_goto_setpaypwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        underdevelopmentDialog1.dismiss();
                    }

                });

                break;


            default:
                break;
        }
    }

    /**
     * 请求收藏列表
     */
    private void loadMyCollect() {
        super.StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("curpage", CURPAGE + "");
        params.addBodyParameter("page", "10");

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Collect_goods_list, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                MyCollect myCollect = gson.fromJson(responseInfo.result, MyCollect.class);

                if (myCollect.status.code == 10000) {
                    M7_MyCollectActivity.this.Dismiss();


                    if (myCollect.datas.goods_list.size() >= 0 && isRefressh) {
                        goodsList.clear();
                    }
                    goodsList.addAll(myCollect.datas.goods_list);

                    if (goodsList.size() == 0) {
                        lvMyCollect.setVisibility(View.GONE);
                        llNull.setVisibility(View.VISIBLE);
                    } else {

                        if (isRefressh) {
                            myCollectAdapter = new MyCollectAdapter(M7_MyCollectActivity.this, goodsList);
                            lvMyCollect.setAdapter(myCollectAdapter);
                        } else {
                            goodsList = myCollect.datas.goods_list;
                            myCollectAdapter.notifyDataSetChanged();
                        }

                        if (myCollect.pagination.hasmore) {
                            lvMyCollect.setPullLoadEnable(true);
                        } else {
                            lvMyCollect.setPullLoadEnable(false);
                        }
                    }

                } else {
                    overtime(myCollect.status.code, myCollect.status.msg);
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent();
        intent.putExtra("goods_id", goodsList.get(position - 1).goods_id);
        intent.setClass(this, PrductDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        goodsArrDialog = new GoodsArrDialog(this, "加入购物车", "删除");
        goodsArrDialog.show();
        //加入购物车
        goodsArrDialog.btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 加入购物车
                 */
                Request.cart_add(goodsList.get(position - 1).goods_id, getKey());

                goodsArrDialog.dismiss();
            }
        });
        //删除
        goodsArrDialog.btnGoodsarrAddshopcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goodsArrDialog.dismiss();

                myDialog = new MyDialog(M7_MyCollectActivity.this, "确定要删除商品？");

                myDialog.show();

                myDialog.positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fav_id = goodsList.get(position - 1).fav_id;
                        /**
                         * 删除收藏的商品
                         */
                        loadDeleMyCollect(fav_id);

                    }
                });

                myDialog.negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();

                    }
                });
            }
        });

        return true;
    }

    /**
     * 删除收藏的商品
     */
    private void loadDeleMyCollect(String fav_id) {
        super.StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("fav_id", fav_id);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Favorites_del, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                M7_MyCollectActivity.this.Dismiss();
                Gson gson = new Gson();
                DeleMyCollect deleMyCollect = gson.fromJson(responseInfo.result, DeleMyCollect.class);
                if (deleMyCollect.status.code == 10000) {
                    showToastShort("删除成功");
                    myDialog.dismiss();
                    /**
                     * 请求收藏列表
                     */
                    loadMyCollect();
                } else {
                    overtime(deleMyCollect.status.code, deleMyCollect.status.msg);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }

    @Override
    public void OnMessageResponse(String url, String response, JSONObject status) throws JSONException {
        showToastShort("加入购物车成功");
    }

}