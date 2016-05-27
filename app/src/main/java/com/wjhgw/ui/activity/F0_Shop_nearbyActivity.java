package com.wjhgw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
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
import com.wjhgw.business.bean.Nearby_stores_list;
import com.wjhgw.business.bean.Nearby_stores_list_data;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.F0_Shop_nearbvAdapter;

import java.util.ArrayList;

/**
 * 附近店铺
 */
public class F0_Shop_nearbyActivity extends BaseActivity implements XListView.IXListViewListener, AMapLocationListener, Runnable {

    private AMapLocation aMapLocation;// 用于判断定位超时
    public F0_Shop_nearbvAdapter listAdapter;
    private ImageView mapimage;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private MyListView mListView;
    private LinearLayout ll_null;
    private int curpage = 1;
    private double longitude;
    private double Latitude;
    private Boolean isSetAdapter = true;
    private ArrayList<Nearby_stores_list_data> nearby_stores_list_data = new ArrayList<>();
    private ImageView iv_return;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f0_list);
        iv_return = (ImageView) findViewById(R.id.iv_return);
        mapimage = (ImageView) findViewById(R.id.mapimage);
        mListView = (MyListView) findViewById(R.id.goods_listview);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
        ll_null = (LinearLayout) findViewById(R.id.ll_null);
        mapimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(F0_Shop_nearbyActivity.this, F1_FoundActivity.class);
                startActivity1(intent);
                finish(false);
            }
        });
        iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Location();
    }

    //定位
    public void Location() {
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onFindViews() {

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {

    }

    @Override
    public void onRefresh(int id) {
        curpage = 1;
        isSetAdapter = true;
        nearby_stores_list();
    }

    @Override
    public void onLoadMore(int id) {
        curpage++;
        isSetAdapter = false;
        nearby_stores_list();
    }

    @Override
    public void run() {
        if (aMapLocation == null) {
            //ToastUtil.show(this, "12秒内还没有定位成功，停止定位");
            stopLocation();// 销毁掉定位
        }
    }

    /**
     * 混合定位回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location != null) {
            this.aMapLocation = location;// 判断超时机制
            //showToastShort(location.getLatitude() + "  " + location.getLongitude());
            longitude = location.getLongitude();
            Latitude = location.getLatitude();
            nearby_stores_list();
            stopLocation();
        }
    }

    /**
     * 销毁定位
     */
    private void stopLocation() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 获取附近店铺列表
     */
    private void nearby_stores_list() {
        StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("longitude", longitude + "");
        params.addBodyParameter("latitude", Latitude + "");
        params.addBodyParameter("distance", "5000");
        params.addBodyParameter("curpage", curpage + "");
        params.addBodyParameter("page", "10");
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Nearby_stores_list, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                Dismiss();
                Nearby_stores_list nearby_stores_list = gson.fromJson(responseInfo.result, Nearby_stores_list.class);
                if (nearby_stores_list.status.code == 10000) {
                    mListView.stopRefresh();
                    mListView.stopLoadMore();
                    mListView.setRefreshTime();
                    if (nearby_stores_list.datas != null) {
                        ll_null.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        if (nearby_stores_list_data.size() > 0 && isSetAdapter) {
                            nearby_stores_list_data.clear();
                        }
                        nearby_stores_list_data.addAll(nearby_stores_list.datas);
                        if (isSetAdapter) {
                            listAdapter = new F0_Shop_nearbvAdapter(F0_Shop_nearbyActivity.this, nearby_stores_list_data);
                            mListView.setAdapter(listAdapter);
                        } else {
                            listAdapter.List = nearby_stores_list_data;
                            listAdapter.notifyDataSetChanged();
                        }

                        if (nearby_stores_list.pagination.hasmore) {
                            mListView.setPullLoadEnable(true);
                        } else {
                            mListView.setPullLoadEnable(false);
                        }
                    } else {
                        ll_null.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                    }
                } else {
                    overtime(nearby_stores_list.status.code, nearby_stores_list.status.msg);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }
}