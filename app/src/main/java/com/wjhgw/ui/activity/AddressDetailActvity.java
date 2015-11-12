package com.wjhgw.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.base.CityActivity;
import com.wjhgw.utils.widget.OnWheelChangedListener;
import com.wjhgw.utils.widget.WheelView;
import com.wjhgw.utils.widget.adapters.ArrayWheelAdapter;

public class AddressDetailActvity extends CityActivity implements OnClickListener, OnWheelChangedListener {
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private TextView mTvConfirm;
    private LinearLayout llPickCity;
    private LinearLayout llZone;
    private Button mBtnSave;
    private View viewShutter;
    private ImageView back;
    private TextView title;
    private EditText edName;
    private EditText edPhone;
    private TextView tvAddressInfo;
    private EditText edAddressDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_detail);
        setUpViews();
        setUpListener();
        setUpData();


    }

    private void setUpViews() {
        back = (ImageView) findViewById(R.id.iv_title_back);
        back.setVisibility(View.VISIBLE);
        title = (TextView) findViewById(R.id.tv_title_name);
        title.setText("收货地址管理");

        edName = (EditText) findViewById(R.id.ed_name);
        edName.setText(getIntent().getStringExtra("name"));
        edPhone = (EditText) findViewById(R.id.ed_phone);
        edPhone.setText(getIntent().getStringExtra("phone"));
        tvAddressInfo = (TextView) findViewById(R.id.tv_address_info);
        tvAddressInfo.setText(getIntent().getStringExtra("info"));
        edAddressDetail = (EditText) findViewById(R.id.ed_address_detail);
        edAddressDetail.setText(getIntent().getStringExtra("addressDetail"));


        llZone = (LinearLayout) findViewById(R.id.ll_zone);

        viewShutter = (View) findViewById(R.id.view_shutter);

        mViewProvince = (WheelView) findViewById(R.id.id_province);
        mViewCity = (WheelView) findViewById(R.id.id_city);
        mViewDistrict = (WheelView) findViewById(R.id.id_district);
        mTvConfirm = (TextView) findViewById(R.id.tv_confirm);
        mBtnSave = (Button) findViewById(R.id.btn_save);

        llPickCity = (LinearLayout) findViewById(R.id.ll_pick_city);
    }

    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
        mTvConfirm.setOnClickListener(this);

        back.setOnClickListener(this);
        mBtnSave.setOnClickListener(this);

        llZone.setOnClickListener(this);
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(AddressDetailActvity.this, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);

        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewDistrict.setCurrentItem(0);

        updateZipCode();
    }

    private void updateZipCode() {
        mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[mViewDistrict.getCurrentItem()];
        mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm:
                showSelectedResult();
                viewShutter.setVisibility(View.GONE);
                llPickCity.setVisibility(View.INVISIBLE);
                mBtnSave.setVisibility(View.VISIBLE);
                break;

            case R.id.iv_title_back:
                finish();
                break;

            case R.id.ll_zone:
                viewShutter.setVisibility(View.VISIBLE);
                llPickCity.setVisibility(View.VISIBLE);
                mBtnSave.setVisibility(View.GONE);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
                break;

            case R.id.btn_save:

                break;
            default:
                break;
        }
    }

    private void showSelectedResult() {
//		Toast.makeText(AddressDetailActvity.this, "当前选中："+mCurrentProviceName+","+mCurrentCityName+","
//				+mCurrentDistrictName+","+mCurrentZipCode, Toast.LENGTH_SHORT).show();

        String key = "de4c81c7ef206bd5dbd825520b97468f";
        String name = "王旭";
        String area_info = mCurrentProviceName + "|" + mCurrentCityName + "|" + mCurrentDistrictName;
        String address = "中南海";
        String mob_phone = "18205447201";
        String url = "http://10.10.0.143/mobile/index.php?act=member_address&op=address_add";

        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("true_name", name);
        params.addBodyParameter("area_info", area_info);
        params.addBodyParameter("address", address);
        params.addBodyParameter("mob_phone", mob_phone);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Toast.makeText(getApplicationContext(), "区域选择成功", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

       /* RequestQueue mQueue = Volley.newRequestQueue(AddressDetailActvity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,SuccessfulResponse,FailureResponse){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String key = "de4c81c7ef206bd5dbd825520b97468f";
                String name = "王旭";
                String area_info = mCurrentProviceName + "|" + mCurrentCityName + "|" + mCurrentDistrictName;
                String address = "中南海";
                String mob_phone = "18205447201";

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("key", key);
                    map.put("true_name", name);
                    map.put("area_info", area_info);
                    map.put("address", address);
                    map.put("mob_phone", mob_phone);
                    return map;
                }
        };

        mQueue.add(stringRequest);
    }


    Response.Listener<String> SuccessfulResponse = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            Toast.makeText(getApplicationContext(), "区域选择成功", Toast.LENGTH_LONG).show();

        }
    };

    Response.ErrorListener FailureResponse = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }*/
    };

}