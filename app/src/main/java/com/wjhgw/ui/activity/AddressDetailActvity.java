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
import com.wjhgw.base.BaseQuery;
import com.wjhgw.base.CityActivity;
import com.wjhgw.config.ApiInterface;
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
    private String type;

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

        viewShutter =  findViewById(R.id.view_shutter);

        mViewProvince = (WheelView) findViewById(R.id.id_province);
        mViewCity = (WheelView) findViewById(R.id.id_city);
        mViewDistrict = (WheelView) findViewById(R.id.id_district);
        mTvConfirm = (TextView) findViewById(R.id.tv_confirm);
        mBtnSave = (Button) findViewById(R.id.btn_save);

        llPickCity = (LinearLayout) findViewById(R.id.ll_pick_city);

        type = getIntent().getType();
        /**
         * 判断如果是点击新增地址跳转过来的 执行
         */
        if (type != null && type.equals("addAddress")) {
            edName.setHint("输入姓名");
            edPhone.setHint("输入11位手机号码");
            tvAddressInfo.setText("点击进行区域选择");
            edAddressDetail.setHint("输入街道名称等信息");

        }
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
                tvAddressInfo.setText(mCurrentProviceName + " " + mCurrentCityName + " " + mCurrentDistrictName);
                viewShutter.setVisibility(View.GONE);
                llPickCity.setVisibility(View.INVISIBLE);
                mBtnSave.setVisibility(View.VISIBLE);

                edName.setEnabled(true);
                edPhone.setEnabled(true);
                edAddressDetail.setEnabled(true);

                break;

            case R.id.iv_title_back:
                finish();
                break;

            case R.id.ll_zone:
                viewShutter.setVisibility(View.VISIBLE);
                llPickCity.setVisibility(View.VISIBLE);
                mBtnSave.setVisibility(View.GONE);
                edName.setEnabled(false);
                edPhone.setEnabled(false);
                edAddressDetail.setEnabled(false);
                /**
                 * 隐藏软键盘
                 */
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
                break;

            case R.id.btn_save:

                if (edName.getEditableText().toString().equals("")) {
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_LONG).show();
                } else if (edPhone.getEditableText().toString().equals("")) {
                    Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_LONG).show();
                } else if (tvAddressInfo.getText().equals("")) {
                    Toast.makeText(this, "区域不能为空，请选择区域", Toast.LENGTH_LONG).show();
                } else if (edAddressDetail.getEditableText().toString().equals("")) {
                    Toast.makeText(this, "详细地址不能为空，请输入详细地址", Toast.LENGTH_LONG).show();
                }

                if (!edName.getEditableText().toString().equals("") && !edPhone.getEditableText().toString().equals("")
                        && !tvAddressInfo.getText().equals("") && !edAddressDetail.getEditableText().toString().equals("")) {
                    /**
                     * 请求修改地址
                     */
                    showSelectedResult();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private void showSelectedResult() {
//		Toast.makeText(AddressDetailActvity.this, "当前选中："+mCurrentProviceName+","+mCurrentCityName+","
//				+mCurrentDistrictName+","+mCurrentZipCode, Toast.LENGTH_SHORT).show();

        String key = getSharedPreferences("key", this.MODE_APPEND).getString("key", "0");
        String useName = edName.getEditableText().toString();
        String area_info = tvAddressInfo.getText().toString().replace(" ", "	");

        String address = edAddressDetail.getText().toString();
        String mob_phone = edPhone.getText().toString();
        String address_id = getIntent().getStringExtra("addressId");

        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("true_name", useName);
        params.addBodyParameter("area_info", area_info);
        params.addBodyParameter("address", address);
        params.addBodyParameter("mob_phone", mob_phone);

        /**
         * 如果是编辑地址 请求编辑地址
         */
        if (type == null || !type.equals("addAddress")) {
            params.addBodyParameter("address_id", address_id);

            /**
             * 请求编辑地址
             */
            loadEditAddress(params);

        }

        /**
         * 如果是新增地址点击进来 请求新增地址
         */
        if (type != null && type.equals("addAddress")) {

            /**
             * 请求新增地址
             */
            loadAddAddress(params);

        }

    }

    /**
     * 请求编辑地址
     */
    private void loadEditAddress(RequestParams params) {
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Address_edit, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                if (responseInfo != null) {
                    Toast.makeText(getApplicationContext(), "收货地址修改成功", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 请求新增地址
     */
    private void loadAddAddress(RequestParams params) {
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Add_address, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                if (responseInfo != null) {
                    Toast.makeText(getApplicationContext(), "新增收货地址成功", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

                Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_LONG).show();
            }
        });

    }

}