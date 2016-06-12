package com.wjhgw.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.wjhgw.business.bean.Nickname;
import com.wjhgw.business.bean.OrderList_goods_list_data;
import com.wjhgw.business.bean.Status;
import com.wjhgw.business.bean.add_refundList;
import com.wjhgw.business.bean.refund_allList;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.dialog.Customer_serviceDialog;
import com.wjhgw.ui.dialog.GalleryDialog;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.D2_deliverAdapter;
import com.wjhgw.ui.view.listview.adapter.D4_customer_serviceAdapter;
import com.wjhgw.utils.GalleryConstants;
import com.wjhgw.utils.GalleryUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * 申请售后
 */
public class D4_Customer_serviceActivity extends BaseActivity implements OnClickListener, XListView.IXListViewListener {

    private MyListView mListView;
    private MyListView mListView1;
    private String lock_state;
    private String rec_id;
    private String goods_num;
    private String goods_name;
    private Double order_amount;
    private String order_id;
    private D2_deliverAdapter listAdapter;
    private LinearLayout Customer_service1;
    private LinearLayout Customer_service2;
    private FrameLayout fl_layotu;
    private FrameLayout fl_lauout1;
    private FrameLayout fl_lauout2;
    private FrameLayout fl_layout3;
    private TextView tv_d4_text1;
    private TextView tv_d4_goods_name;
    private TextView tv_d4_next;
    private TextView tv_d4_rder_sn;
    private TextView tv_d4_num;
    private TextView tv_d4_text2;
    private TextView tv_d4_text3;
    private TextView tv_d4_text4;
    private TextView tv_d4_text5;
    public EditText et_d4_name1;
    private EditText et_d4_name2;
    private EditText et_d4_name;
    private ImageView iv_d4_image1;
    private ImageView iv_d4_image2;
    private ImageView iv_upload1;
    private ImageView iv_upload2;
    private ImageView iv_upload3;
    private ImageView iv_delete1;
    private ImageView iv_delete2;
    private ImageView iv_delete3;
    private Customer_serviceDialog dialog;
    private GalleryDialog Dialog;
    private Bitmap bitmap; //上传的图片
    private String position = "0"; //上传的图片的位置
    private String position1 = "0"; //上传的图片的名称
    private String position2 = "0"; //上传的图片的名称
    private String position3 = "0"; //上传的图片的名称
    private String refund_type = "1";
    private refund_allList refund;
    private ArrayList<OrderList_goods_list_data> extend_order_goods;
    private FrameLayout fl_lauout0;
    private TextView tv_d4_text0;
    private ImageView iv_d4_image0;
    private String order_type;
    private LinearLayout ll_order_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d4_customer_service_layout);

        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
        mListView.setAdapter(null);
        mListView.addHeaderView(Customer_service1);
        mListView.addHeaderView(Customer_service2);

        Dialog = new GalleryDialog(this);
        order_id = getIntent().getStringExtra("order_id");
        lock_state = getIntent().getStringExtra("lock_state");
        rec_id = getIntent().getStringExtra("rec_id");
        order_type = getIntent().getStringExtra("order_type");

        if (lock_state.equals("20")) {
            click(1);
            add_refund_all_step1();
            //et_d4_name1.setText("" + order_amount);
            et_d4_name1.setTextColor(Color.parseColor("#cccccc"));
            et_d4_name1.setEnabled(false);
            et_d4_name2.setText("0");
            et_d4_name2.setTextColor(Color.parseColor("#cccccc"));
            et_d4_name2.setEnabled(false);

            fl_layotu.setVisibility(View.GONE);
            tv_d4_text5.setText("最多0件");
            tv_d4_text3.setText("取消订单,全部退款");
        } else {
            if (order_type.equals("4")) {
                click(3);
            } else {
                click(1);
                add_refund_step1();
                tv_d4_text5.setText("最多0件");
                et_d4_name2.setText("0");
                et_d4_name2.setTextColor(Color.parseColor("#cccccc"));
                et_d4_name2.setEnabled(false);
            }

        }
        et_d4_name1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start != 0) {
                    if (order_amount < Double.parseDouble(et_d4_name1.getText().toString())) {
                        et_d4_name1.getText().delete(et_d4_name1.length() - 1, et_d4_name1.length());
                    }
                } else {
                    if (start == 0 && count > 0) {
                        if (order_amount < Double.parseDouble(et_d4_name1.getText().toString())) {
                            et_d4_name1.getText().delete(et_d4_name1.length() - 1, et_d4_name1.length());
                        }
                    } else {

                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_d4_name2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start != 0) {
                    if (Double.parseDouble(goods_num) < Double.parseDouble(et_d4_name2.getText().toString())) {
                        et_d4_name2.getText().delete(et_d4_name2.length() - 1, et_d4_name2.length());
                    }
                } else {
                    if (start == 0 && count > 0) {
                        if (Double.parseDouble(goods_num) < Double.parseDouble(et_d4_name2.getText().toString())) {
                            et_d4_name2.getText().delete(et_d4_name2.length() - 1, et_d4_name2.length());
                        }
                    } else {

                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    @Override
    public void onInit() {
        setUp();
        setTitle("申请售后");
    }

    @Override
    public void onFindViews() {
        mListView = (MyListView) findViewById(R.id.d4_list_layout);
        Customer_service1 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.d4_customer_service_item1, null);
        Customer_service2 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.d4_customer_service_item2, null);
        mListView1 = (MyListView) Customer_service2.findViewById(R.id.d4_list_item);

        ll_order_type = (LinearLayout) Customer_service1.findViewById(R.id.ll_order_type);
        fl_lauout0 = (FrameLayout) Customer_service1.findViewById(R.id.fl_lauout0);
        fl_lauout1 = (FrameLayout) Customer_service1.findViewById(R.id.fl_lauout1);
        fl_lauout2 = (FrameLayout) Customer_service1.findViewById(R.id.fl_lauout2);
        fl_layout3 = (FrameLayout) Customer_service1.findViewById(R.id.fl_layout3);
        tv_d4_text0 = (TextView) Customer_service1.findViewById(R.id.tv_d4_text0);
        tv_d4_text1 = (TextView) Customer_service1.findViewById(R.id.tv_d4_text1);
        tv_d4_text2 = (TextView) Customer_service1.findViewById(R.id.tv_d4_text2);
        tv_d4_text3 = (TextView) Customer_service1.findViewById(R.id.tv_d4_text3);
        tv_d4_text4 = (TextView) Customer_service1.findViewById(R.id.tv_d4_text4);
        tv_d4_text5 = (TextView) Customer_service1.findViewById(R.id.tv_d4_text5);
        et_d4_name1 = (EditText) Customer_service1.findViewById(R.id.et_d4_name1);
        et_d4_name2 = (EditText) Customer_service1.findViewById(R.id.et_d4_name2);
        et_d4_name = (EditText) Customer_service1.findViewById(R.id.et_d4_name);
        iv_d4_image0 = (ImageView) Customer_service1.findViewById(R.id.iv_d4_image0);
        iv_d4_image1 = (ImageView) Customer_service1.findViewById(R.id.iv_d4_image1);
        iv_d4_image2 = (ImageView) Customer_service1.findViewById(R.id.iv_d4_image2);

        iv_upload1 = (ImageView) Customer_service2.findViewById(R.id.iv_upload1);
        iv_upload2 = (ImageView) Customer_service2.findViewById(R.id.iv_upload2);
        iv_upload3 = (ImageView) Customer_service2.findViewById(R.id.iv_upload3);
        iv_delete1 = (ImageView) Customer_service2.findViewById(R.id.iv_delete1);
        iv_delete2 = (ImageView) Customer_service2.findViewById(R.id.iv_delete2);
        iv_delete3 = (ImageView) Customer_service2.findViewById(R.id.iv_delete3);
        fl_layotu = (FrameLayout) Customer_service2.findViewById(R.id.fl_layotu);
        tv_d4_goods_name = (TextView) Customer_service2.findViewById(R.id.tv_d4_goods_name);
        tv_d4_num = (TextView) Customer_service2.findViewById(R.id.tv_d4_num);
        tv_d4_rder_sn = (TextView) Customer_service2.findViewById(R.id.tv_d4_rder_sn);
        tv_d4_next = (TextView) findViewById(R.id.tv_d4_next);
    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        fl_lauout0.setOnClickListener(this);
        fl_lauout1.setOnClickListener(this);
        fl_lauout2.setOnClickListener(this);
        fl_layout3.setOnClickListener(this);
        iv_delete1.setOnClickListener(this);
        iv_delete2.setOnClickListener(this);
        iv_delete3.setOnClickListener(this);
        iv_upload1.setOnClickListener(this);
        iv_upload2.setOnClickListener(this);
        iv_upload3.setOnClickListener(this);
        tv_d4_next.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_lauout0:
                if (lock_state.equals("20")) {
                    showToastShort("待发货订单仅支持退款");
                } else {
                    if (!order_type.equals("4")) {
                        showToastShort("正常订单不支持换货");
                    }
                }
                break;
            case R.id.fl_lauout1:
                if (!lock_state.equals("20") && !order_type.equals("4")) {
                    click(1);
                    tv_d4_text5.setText("最多0件");
                    et_d4_name2.setText("0");
                    et_d4_name2.setTextColor(Color.parseColor("#cccccc"));
                    et_d4_name2.setEnabled(false);
                }

                if (!lock_state.equals("20") && order_type.equals("4")) {
                    showToastShort("取酒订单仅支持换货");
                }
                break;
            case R.id.fl_lauout2:
                if (!lock_state.equals("20") && !order_type.equals("4")) {
                    click(2);
                    tv_d4_text5.setText("最多" + goods_num + "件");

                    et_d4_name2.setText("");
                    et_d4_name2.setTextColor(Color.parseColor("#333333"));
                    et_d4_name2.setEnabled(true);
                }
                if (lock_state.equals("20")) {
                    showToastShort("待发货订单仅支持退款");
                } else {
                    if (order_type.equals("4")) {
                        showToastShort("取酒订单仅支持换货");
                    }
                }

                break;
            case R.id.fl_layout3:
                if (!lock_state.equals("20")) {
                    dialog = new Customer_serviceDialog(this, tv_d4_text3);
                    dialog.show();
                }
                break;
            case R.id.iv_upload1:
                if (position1.equals("0")) {
                    position = "1";
                    Dialog.Get_pictures_Dialog();
                }
                break;
            case R.id.iv_upload2:
                if (position2.equals("0")) {
                    position = "2";
                    Dialog.Get_pictures_Dialog();
                }
                break;
            case R.id.iv_upload3:
                if (position3.equals("0")) {
                    position = "3";
                    Dialog.Get_pictures_Dialog();
                }
                break;
            case R.id.iv_delete1:
                if (!position1.equals("0")) {
                    position = "1";
                    del_img(position1);
                }
            case R.id.iv_delete2:
                if (!position2.equals("0")) {
                    position = "2";
                    del_img(position2);
                }
            case R.id.iv_delete3:
                if (!position3.equals("0")) {
                    position = "3";
                    del_img(position3);
                }
                break;
            case R.id.tv_d4_next:
                String s = "";
                if (!position1.equals("0")) {
                    s += position1 + "|**|";
                }
                if (!position2.equals("0")) {
                    s += position2 + "|**|";
                }
                if (!position3.equals("0")) {
                    s += position3;
                }
                String buyer_message = et_d4_name.getText().toString();
                if (lock_state.equals("20")) {
                    add_refund_all_step2(buyer_message, s);
                } else {
                    String reason_info = tv_d4_text3.getText().toString();
                    if (reason_info.equals("请选择售后原因")) {
                        reason_info = "其他";
                    }
                    if (!order_type.equals("4")) {
                        String refund_amount = et_d4_name1.getText().toString();
                        String goods_num = et_d4_name2.getText().toString();

                        if (refund_amount.length() > 0 && goods_num.length() > 0) {
                            add_refund_step2(buyer_message, s, refund_amount, goods_num, reason_info);
                        } else {
                            showToastShort("退款金额或退款数量不能为空");
                        }
                    } else {
                        add_refund_step2(buyer_message, s, "0", "0", reason_info);
                    }

                }

                break;
            default:
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == GalleryConstants.PHOTO_RESOULT) {
            File picture = new File(Environment.getExternalStorageDirectory()
                    + "/temp.jpg");
            GalleryUtils.getInstance().cropPicture(this, Uri.fromFile(picture));
        }
        if (null == data) {
            return;
        }
        Uri uri = null;
        if (requestCode == GalleryConstants.KITKAT_LESS) {
            uri = data.getData();
            // 调用裁剪方法
            GalleryUtils.getInstance().cropPicture(this, uri);
        } else if (requestCode == GalleryConstants.KITKAT_ABOVE) {
            uri = data.getData();
            // 先将这个uri转换为path，然后再转换为uri
            String thePath = GalleryUtils.getInstance().getPath(this, uri);
            GalleryUtils.getInstance().cropPicture(this,
                    Uri.fromFile(new File(thePath)));
        } else if (requestCode == GalleryConstants.INTENT_CROP) {
            bitmap = data.getParcelableExtra("data");

            File temp = new File(Environment.getExternalStorageDirectory()
                    .getPath() + "/wjhg/");// 自已缓存文件夹
            if (!temp.exists()) {
                temp.mkdir();
            }
            File tempFile = new File(temp.getAbsolutePath() + "/"
                    + Calendar.getInstance().getTimeInMillis() + ".jpg"); // 以时间秒为文件名

            // 图像保存到文件中
            FileOutputStream foutput = null;
            try {
                foutput = new FileOutputStream(tempFile);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, foutput)) {
                    String imgPath = tempFile.getAbsolutePath();
                    String sur = tempFile.getName();
                    FileInputStream input = new FileInputStream(imgPath);
                    StartLoading();
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("key", getKey());
                    params.addBodyParameter("usage_number", "1");
                    params.addBodyParameter("data", input, tempFile.length(), sur);
                    /**
                     * 上传用户头像
                     */
                    load_member_image(params);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传图片凭证
     */
    private void load_member_image(RequestParams params) {
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Member_image, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                Dismiss();
                if (responseInfo.result != null) {
                    Nickname nickname = gson.fromJson(responseInfo.result, Nickname.class);
                    if (nickname.status.code == 10000) {
                        showToastLong(nickname.status.msg);
                        if (position.equals("1")) {
                            iv_upload1.setImageBitmap(bitmap);
                            iv_delete1.setVisibility(View.VISIBLE);
                            position1 = nickname.datas;
                        } else if (position.equals("2")) {
                            iv_upload2.setImageBitmap(bitmap);
                            iv_delete2.setVisibility(View.VISIBLE);
                            position2 = nickname.datas;
                        } else if (position.equals("3")) {
                            iv_upload3.setImageBitmap(bitmap);
                            iv_delete3.setVisibility(View.VISIBLE);
                            position3 = nickname.datas;
                        }
                    } else {
                        overtime(nickname.status.code, nickname.status.msg);
                    }
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToastLong("网络错误");
            }
        });
    }

    /**
     * 删除上传图片
     */
    private void del_img(String img_name) {
        StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("usage_number", "1");
        params.addBodyParameter("img_name", img_name);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Del_img, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                Dismiss();
                if (responseInfo.result != null) {
                    Nickname nickname = gson.fromJson(responseInfo.result, Nickname.class);
                    if (nickname.status.code == 10000) {
                        //showToastLong(nickname.status.msg);
                        if (position.equals("1")) {
                            iv_upload1.setImageResource(R.mipmap.ic_upload);
                            iv_delete1.setVisibility(View.GONE);
                            position1 = "0";
                        } else if (position.equals("2")) {
                            iv_upload2.setImageResource(R.mipmap.ic_upload);
                            iv_delete2.setVisibility(View.GONE);
                            position2 = "0";
                        } else if (position.equals("3")) {
                            iv_upload3.setImageResource(R.mipmap.ic_upload);
                            iv_delete3.setVisibility(View.GONE);
                            position3 = "0";
                        }
                    } else {
                        overtime(nickname.status.code, nickname.status.msg);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                
            }
        });
    }

    @Override
    public void onRefresh(int id) {
    }

    @Override
    public void onLoadMore(int id) {
    }

    /**
     * 售前退款第一步
     */
    private void add_refund_all_step1() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("order_id", order_id);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Add_refund_all_step1, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (null != responseInfo) {
                    refund = gson.fromJson(responseInfo.result, refund_allList.class);
                    if (refund.status.code == 10000) {
                        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mListView1.getLayoutParams();
                        linearParams.height = dip2px(D4_Customer_serviceActivity.this, 24) * refund.datas.goods_list.size();// 当控件的高
                        mListView1.setLayoutParams(linearParams);
                        D4_customer_serviceAdapter adapter = new D4_customer_serviceAdapter(D4_Customer_serviceActivity.this, refund.datas.goods_list);
                        mListView1.setAdapter(adapter);

                        //设置listview不能滑动
                        mListView1.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return true;
                            }
                        });

                        order_amount = Double.parseDouble(refund.datas.order_amount);
                        tv_d4_rder_sn.setText(refund.datas.order_sn);
                        tv_d4_text4.setText("最多" + order_amount + "元");
                        et_d4_name1.setText("" + refund.datas.order_amount);

                        if (refund.datas.order_type.equals("4") && order_amount == 0) {
                            if (order_amount == 0) {
                                tv_d4_text3.setText("取消订单,商品退至您的酒柜");
                            } else {
                                tv_d4_text3.setText("商品退至您的酒柜");
                            }
                        }
                    } else {
                        overtime(refund.status.code, refund.status.msg);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToastShort("网络错误");
            }

        });
    }

    /**
     * 售前退款第二步
     */
    private void add_refund_all_step2(String buyer_message, String pic_info) {
        StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("order_id", order_id);
        params.addBodyParameter("buyer_message", buyer_message);
        params.addBodyParameter("pic_info", pic_info);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Add_refund_all_step2, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                Dismiss();
                if (null != responseInfo) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);
                    if (status.status.code == 10000) {
                        showToastShort(status.status.msg);
                        finish();
                    } else {
                        overtime(status.status.code, status.status.msg);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToastShort("网络错误");
            }

        });
    }

    /**
     * 售后退款,退货第一步
     */
    private void add_refund_step1() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("order_id", order_id);
        params.addBodyParameter("rec_id", rec_id);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Add_refund_step1, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (null != responseInfo) {
                    add_refundList add_refund = gson.fromJson(responseInfo.result, add_refundList.class);
                    if (add_refund.status.code == 10000) {
                        order_amount = Double.parseDouble(add_refund.datas.goods_list.goods_price);
                        goods_num = add_refund.datas.goods_list.goods_num;
                        tv_d4_text4.setText("最多" + order_amount + "元");
                        tv_d4_goods_name.setText(add_refund.datas.goods_list.goods_name);
                        tv_d4_num.setText("¥" + order_amount + "*" + goods_num + "(数量)");
                        tv_d4_rder_sn.setText(add_refund.datas.order_info.order_sn);
                    } else {
                        overtime(add_refund.status.code, add_refund.status.msg);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToastShort("网络错误");
            }

        });
    }

    /**
     * 售后退款,退货第二步
     */
    private void add_refund_step2(String buyer_message, String pic_info,
                                  String refund_amount, String goods_num, String reason_info) {
        StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("order_id", order_id);
        params.addBodyParameter("refund_type", refund_type);  //退款类型
        params.addBodyParameter("rec_id", rec_id);    //记录编号
        params.addBodyParameter("refund_amount", refund_amount); //退款金额
        params.addBodyParameter("goods_num", goods_num);     //数量
        params.addBodyParameter("reason_info", reason_info);   //退款原因
        params.addBodyParameter("buyer_message", buyer_message);
        params.addBodyParameter("pic_info", pic_info);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Add_refund_step2, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                Dismiss();
                if (null != responseInfo) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);
                    if (status.status.code == 10000) {
                        showToastShort(status.status.msg);
                        finish();
                    } else {
                        overtime(status.status.code, status.status.msg);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToastShort("网络错误");
            }

        });
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void click(int i) {
        tv_d4_text0.setTextColor(Color.parseColor("#999999"));
        tv_d4_text1.setTextColor(Color.parseColor("#999999"));
        tv_d4_text2.setTextColor(Color.parseColor("#999999"));
        iv_d4_image0.setVisibility(View.GONE);
        iv_d4_image1.setVisibility(View.GONE);
        iv_d4_image2.setVisibility(View.GONE);
        if (i == 1) {
            tv_d4_text1.setTextColor(Color.parseColor("#f25252"));
            iv_d4_image1.setVisibility(View.VISIBLE);
            refund_type = "1";
        } else if (i == 2) {
            refund_type = "2";
            tv_d4_text2.setTextColor(Color.parseColor("#f25252"));
            iv_d4_image2.setVisibility(View.VISIBLE);
        } else if (i == 3) {
            refund_type = "3";
            tv_d4_text0.setTextColor(Color.parseColor("#f25252"));
            iv_d4_image0.setVisibility(View.VISIBLE);
            ll_order_type.setVisibility(View.GONE);
        }

    }
}
