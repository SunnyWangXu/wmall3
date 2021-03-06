package com.wjhgw.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.base.CityActivity;
import com.wjhgw.business.bean.Status;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.dialog.LoadDialog;
import com.wjhgw.utils.widget.OnWheelChangedListener;
import com.wjhgw.utils.widget.WheelView;
import com.wjhgw.utils.widget.adapters.ArrayWheelAdapter;

import java.util.regex.Pattern;

/**
 * 收货地址管理_编辑
 */
public class M2_AddressDetailActvity extends CityActivity implements OnClickListener, OnWheelChangedListener {
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private TextView mTvConfirm;
    private LinearLayout llPickCity;
    private LinearLayout llZone;
    private TextView mTVEdit;
    private View viewShutter;
    private ImageView back;
    private TextView title;
    private EditText edName;
    private EditText edPhone;
    private TextView tvAddressInfo;
    private EditText edAddressDetail;
    private String type;
    private ImageView ivTitle;
    private Button mBtnSave;
    private String edNameStr;
    private String edPhoneStr;
    private String edAddressDetailStr;
    private String key;
    private String address_id;
    private LoadDialog Dialog;
    private ImageView ivRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dialog = new LoadDialog(this);
        setContentView(R.layout.m2_activity_address_detail);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//固定竖屏

        setUpViews();
        setUpListener();
        setUpData();

        address_id = getIntent().getStringExtra("addressId");
    }

    @Override
    protected void onResume() {
        super.onResume();
        key = getSharedPreferences("key", this.MODE_APPEND).getString("key", "0");

    }

    private void setUpViews() {
        back = (ImageView) findViewById(R.id.iv_title_back);
        back.setVisibility(View.VISIBLE);
        title = (TextView) findViewById(R.id.tv_title_name);
        title.setText("编辑地址");


        edName = (EditText) findViewById(R.id.ed_name);
        edName.setText(getIntent().getStringExtra("name"));
        edPhone = (EditText) findViewById(R.id.ed_phone);
        edPhone.setText(getIntent().getStringExtra("phone"));
        tvAddressInfo = (TextView) findViewById(R.id.tv_address_info);
        tvAddressInfo.setText(getIntent().getStringExtra("info"));
        edAddressDetail = (EditText) findViewById(R.id.ed_address_detail);
        edAddressDetail.setText(getIntent().getStringExtra("addressDetail"));


        llZone = (LinearLayout) findViewById(R.id.ll_zone);

        viewShutter = findViewById(R.id.view_shutter);

        mViewProvince = (WheelView) findViewById(R.id.id_province);
        mViewCity = (WheelView) findViewById(R.id.id_city);
        mViewDistrict = (WheelView) findViewById(R.id.id_district);
        mTvConfirm = (TextView) findViewById(R.id.tv_confirm);
//        mTVEdit = (TextView) findViewById(R.id.tv_edit);
//        mTVEdit.setVisibility(View.VISIBLE);
//        mTVEdit.setText("删除");

        mBtnSave = (Button) findViewById(R.id.btn_save);

        llPickCity = (LinearLayout) findViewById(R.id.ll_pick_city);

        type = getIntent().getType();
        /**
         * 判断如果是点击新增地址跳转过来的 执行
         */
        if (type != null && type.equals("addAddress")) {
            title.setText("新增地址");
            edName.setHint("不少于1位");
            edPhone.setHint("请输入11位手机号码");
            tvAddressInfo.setText("北京 朝阳区 三环以内");
            edAddressDetail.setHint("街道名称及楼房门牌号等信息");

//            mTVEdit.setVisibility(View.INVISIBLE);
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
//        mTVEdit.setOnClickListener(this);


        llZone.setOnClickListener(this);

        mBtnSave.setOnClickListener(this);
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(M2_AddressDetailActvity.this, mProvinceDatas));
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
//                mTVEdit.setVisibility(View.VISIBLE);
                mBtnSave.setVisibility(View.VISIBLE);

                edName.setEnabled(true);
                edPhone.setEnabled(true);
                edAddressDetail.setEnabled(true);

                break;
//            case R.id.tv_edit:
//
//                /**
//                 * 删除编辑地址
//                 */
//                deleteAddressDetail();
//
//                break;
            case R.id.iv_title_back:
                finish();
                break;

            case R.id.ll_zone:
                mBtnSave.setVisibility(View.GONE);
                viewShutter.setVisibility(View.VISIBLE);
                llPickCity.setVisibility(View.VISIBLE);
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

                String regEx = "^[a-zA-Z\u4e00-\u9fa5]+$";
                edNameStr = edName.getEditableText().toString();
                edPhoneStr = edPhone.getEditableText().toString();
                edAddressDetailStr = edAddressDetail.getEditableText().toString();

                if (edNameStr.equals("")) {
                    Toast.makeText(this, "收货人不能为空", Toast.LENGTH_SHORT).show();
                } else if (edNameStr.length() < 2) {
                    Toast.makeText(this, " 收货人不能输入少于2个字符,请重新输入", Toast.LENGTH_SHORT).show();
                } else if (!Pattern.compile(regEx).matcher(edNameStr).matches()) {
                    Toast.makeText(this, " 收货人只能输入字母和汉字，请重新输入", Toast.LENGTH_SHORT).show();
                } else if (edPhoneStr.equals("")) {
                    Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                } else if (edPhoneStr.length() < 11) {
                    Toast.makeText(this, "手机号码为11位，请重新输入", Toast.LENGTH_SHORT).show();
                } else if (tvAddressInfo.getText().equals("")) {
                    Toast.makeText(this, "区域不能为空，请选择区域", Toast.LENGTH_SHORT).show();
                } else if (edAddressDetailStr.equals("")) {
                    Toast.makeText(this, "详细地址不能为空，请输入详细地址", Toast.LENGTH_SHORT).show();
                }

                if (!edNameStr.equals("") && !(edNameStr.length() < 2) && Pattern.compile(regEx).matcher(edNameStr).matches() &&
                        !edPhoneStr.equals("") && !(edPhoneStr.length() < 11) && !tvAddressInfo.getText().equals("") && !edAddressDetailStr.equals("")) {
                    /**
                     * 请求修改地址和新增地址
                     */
                    showSelectedResult();

                }
                break;
            default:
                break;
        }
    }

    /**
     * 删除编辑地址
     */
    private void deleteAddressDetail() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("address_id", address_id);
        Dialog.ProgressDialog();

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Address_del, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dialog.dismiss();
                Gson gson = new Gson();
                if (null != responseInfo) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);
                    if (status.status.code == 10000) {
                        Toast.makeText(getApplicationContext(), "删除收货地址成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    if (status.status.code == 200103 || status.status.code == 200104) {
                        Toast.makeText(M2_AddressDetailActvity.this, "登录超时或未登录", Toast.LENGTH_SHORT).show();
                        getSharedPreferences("key", MODE_APPEND).edit().putString("key", "0").commit();
                        startActivity(new Intent(M2_AddressDetailActvity.this, A0_LoginActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getApplicationContext(), "删除地址失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 请求修改地址和新增地址
     */
    private void showSelectedResult() {

        String useName = edName.getEditableText().toString();
        String area_info = tvAddressInfo.getText().toString().replace(" ", "	");
        String address = edAddressDetail.getText().toString();
        String mob_phone = edPhone.getText().toString();

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
        Dialog.ProgressDialog();
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Address_edit, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dialog.dismiss();
                Gson gson = new Gson();
                if (null != responseInfo) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);
                    if (status.status.code == 10000) {
                        Toast.makeText(getApplicationContext(), "收货地址修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    if (status.status.code == 200103 || status.status.code == 200104) {
                        Toast.makeText(M2_AddressDetailActvity.this, "登录超时或未登录", Toast.LENGTH_SHORT).show();
                        getSharedPreferences("key", MODE_APPEND).edit().putString("key", "0").commit();
                        startActivity(new Intent(M2_AddressDetailActvity.this, A0_LoginActivity.class));
                    } else {
                        Toast.makeText(M2_AddressDetailActvity.this, status.status.msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    /**
     * 请求新增地址
     */
    private void loadAddAddress(RequestParams params) {
        Dialog.ProgressDialog();
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Add_address, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dialog.dismiss();
                Gson gson = new Gson();
                if (null != responseInfo) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);
                    if (status.status.code == 10000) {
                        Toast.makeText(getApplicationContext(), "新增收货地址成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    if (status.status.code == 200103 || status.status.code == 200104) {
                        Toast.makeText(M2_AddressDetailActvity.this, "登录超时或未登录", Toast.LENGTH_SHORT).show();
                        getSharedPreferences("key", MODE_APPEND).edit().putString("key", "0").commit();
                        startActivity(new Intent(M2_AddressDetailActvity.this, A0_LoginActivity.class));
                    } else {
                        Toast.makeText(M2_AddressDetailActvity.this, status.status.msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }

}