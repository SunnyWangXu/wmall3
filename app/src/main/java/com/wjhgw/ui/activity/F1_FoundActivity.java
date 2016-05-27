package com.wjhgw.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
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
import com.wjhgw.business.bean.Nearby_stores;
import com.wjhgw.config.ApiInterface;

/**
 * AMapV1地图中简单介绍显示定位小蓝点
 */
public class F1_FoundActivity extends BaseActivity implements LocationSource,
        AMapLocationListener, AMap.OnMarkerClickListener {
    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient = null;
    private AMapLocationClientOption mLocationOption;
    private ImageView iv_return;
    private double longitude;
    private double Latitude;
    private Nearby_stores nearby_stores;
    private LinearLayout infoContent;
    private ImageView mapimage;
    private UiSettings mUiSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_locationsource_activity);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        iv_return = (ImageView) findViewById(R.id.iv_return);
        mapimage = (ImageView) findViewById(R.id.mapimage);

        mapimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(F1_FoundActivity.this, F0_Shop_nearbyActivity.class);
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
        init();
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

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.moveCamera(CameraUpdateFactory.zoomTo(14));//放大
            mUiSettings = aMap.getUiSettings();
            setUpMap();
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.mipmap.ic_location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            longitude = amapLocation.getLongitude();
            Latitude = amapLocation.getLatitude();
            nearby_stores();
            deactivate();
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
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

    /**
     * 停止定位
     */
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {
        for (int i = 0; i < nearby_stores.datas.size(); i++) {
            double longitude = Double.parseDouble(nearby_stores.datas.get(i).longitude);
            double latitude = Double.parseDouble(nearby_stores.datas.get(i).latitude);
            //绘制marker
            Marker marker = aMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title("" + i)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .draggable(true));

        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        int i = Integer.parseInt(marker.getTitle());
        String mobile = "";
        if(!nearby_stores.datas.get(i).mobile.equals("")){
            mobile = nearby_stores.datas.get(i).mobile;
        }else {
            mobile = nearby_stores.datas.get(i).tel;
        }
        Intent intent = new Intent(F1_FoundActivity.this, F2_StoreGoodsActivity.class);
        intent.putExtra("name",nearby_stores.datas.get(i).store_name);
        intent.putExtra("mobile",mobile);
        intent.putExtra("address",nearby_stores.datas.get(i).address);
        intent.putExtra("distance",nearby_stores.datas.get(i).distance);
        startActivity(intent);
        return true;
    }

    /**
     * 定位附近店铺
     */
    private void nearby_stores() {
        StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("longitude", longitude + "");
        params.addBodyParameter("latitude", Latitude + "");
        params.addBodyParameter("distance", "5000");
        params.addBodyParameter("num", "20");
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Nearby_stores, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                Dismiss();
                nearby_stores = gson.fromJson(responseInfo.result, Nearby_stores.class);
                if (nearby_stores.status.code == 10000) {
                    addMarkersToMap();
                } else {
                    overtime(nearby_stores.status.code, nearby_stores.status.msg);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }
}
