package com.wjhgw.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.wjhgw.business.api.Address_del_Request;
import com.wjhgw.business.bean.CadList;
import com.wjhgw.business.response.BusinessResponse;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.activity.J1_RecordActivity;
import com.wjhgw.ui.dialog.LoadDialog;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.CabinetAdapter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 酒柜Fragment
 */
public class CabinetFragment extends Fragment implements BusinessResponse, XListView.IXListViewListener,
        View.OnClickListener {

    private MyListView mListView;
    private MyListView mListView1;
    private String key;
    private CabinetAdapter listAdapter = null;
    private CadList cadList;
    private ImageView iv_select;
    private TextView tv_total_num;

    private TextView tv_record;
    private TextView tv_others;
    private TextView tv_own;
    private LinearLayout ll_wine;
    private LinearLayout ll_select;
    private LinearLayout ll_null;
    private Address_del_Request Request;
    //判断是刷新状态还是加载状态
    private Boolean isSetAdapter = true;
    private View rootView;
    private LoadDialog Dialog;
    private Intent intent;
    private int curpage = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Dialog = new LoadDialog(getActivity());
        rootView = inflater.inflate(R.layout.cabinet_layout, container, false);
        key = getActivity().getSharedPreferences("key", getActivity().MODE_APPEND).getString("key", "0");
        initView();
        setClick();
        listAddHeader();

        Request = new Address_del_Request(getActivity());
        Request.addResponseListener(this);

        return rootView;
    }

    /**
     * 设置ListView
     */
    private void listAddHeader() {
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
        mListView.setAdapter(null);
        mListView.setVisibility(View.VISIBLE);

    }

    /**
     * 初始化视图
     */
    private void initView() {
        iv_select = (ImageView) rootView.findViewById(R.id.iv_select);
        ll_select = (LinearLayout) rootView.findViewById(R.id.ll_select);
        ll_null = (LinearLayout) rootView.findViewById(R.id.ll_null);
        ll_wine = (LinearLayout) rootView.findViewById(R.id.ll_wine);
        tv_total_num = (TextView) rootView.findViewById(R.id.tv_total_num);
        tv_others = (TextView) rootView.findViewById(R.id.tv_others);
        tv_own = (TextView) rootView.findViewById(R.id.tv_own);
        tv_record = (TextView) rootView.findViewById(R.id.tv_record);

        mListView = (MyListView) rootView.findViewById(R.id.lv_list_layout);
    }

    /**
     * 设置监听事件
     */
    private void setClick() {
        ll_select.setOnClickListener(this);
        tv_own.setOnClickListener(this);
        tv_others.setOnClickListener(this);
        tv_record.setOnClickListener(this);
    }

    @Override
    public void onRefresh(int id) {
        curpage = 1;
        cab_list();
        isSetAdapter = true;
    }

    @Override
    public void onLoadMore(int id) {
        curpage++;
        isSetAdapter = false;
        cab_list();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_select:
                if (listAdapter.num == listAdapter.goods_id.length) {
                    eliminate();
                } else {
                    listAdapter.num = listAdapter.goods_id.length;
                    iv_select.setImageResource(R.mipmap.ic_order_select);
                    for (int i = 0; i < listAdapter.goods_id.length; i++) {
                        listAdapter.goods_id[i] = cadList.datas.get(i).goods_id;
                        listAdapter.total_num += listAdapter.buy_number[i];
                    }
                    tv_total_num.setText(listAdapter.total_num + "件");
                }
                listAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_record:
                intent = new Intent(getActivity(), J1_RecordActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_others:
                if (listAdapter.num > 0) {
                    /*StringBuffer cart_id = new StringBuffer();
                    for (int i = 0; i < listAdapter.goods_id.length; i++) {
                        if (!listAdapter.goods_id[i].equals("0")) {
                            cart_id.append(listAdapter.goods_id[i] + "|" + listAdapter.List.get(i).buy_number + ",");
                        }
                    }*/
                    Toast.makeText(getActivity(), "下一步正在开发", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "你还没有选择商品哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_own:
                if (listAdapter.num > 0) {
                    /*StringBuffer cart_id = new StringBuffer();
                    for (int i = 0; i < listAdapter.goods_id.length; i++) {
                        if (!listAdapter.goods_id[i].equals("0")) {
                            cart_id.append(listAdapter.goods_id[i] + "|" + listAdapter.List.get(i).buy_number + ",");
                        }
                    }*/
                    Toast.makeText(getActivity(), "下一步正在开发", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "你还没有选择商品哦", Toast.LENGTH_SHORT).show();
                }
                break;


            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        key = getActivity().getSharedPreferences("key", getActivity().MODE_APPEND).getString("key", "0");
        if (!key.equals("0")) {
            cab_list();
        }
    }

    /**
     * 酒柜商品列表
     */
    private void cab_list() {
        Dialog.ProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("curpage", curpage + "");
        params.addBodyParameter("page", "10");

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Cab_list, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dialog.dismiss();
                Gson gson = new Gson();
                if (responseInfo.result != null) {
                    cadList = gson.fromJson(responseInfo.result, CadList.class);

                    if (cadList.status.code == 10000) {
                        mListView.stopRefresh();
                        mListView.stopLoadMore();
                        mListView.setRefreshTime();
                        ll_wine.setVisibility(View.VISIBLE);
                        tv_record.setVisibility(View.VISIBLE);
                        ll_null.setVisibility(View.GONE);
                        if (cadList.datas != null) {
                            if (isSetAdapter) {
                                listAdapter = new CabinetAdapter(getActivity(), cadList.datas,
                                        iv_select, tv_total_num, Request);
                                mListView.setAdapter(listAdapter);
                                iv_select.setImageResource(R.mipmap.ic_order_blank);
                            } else {
                                listAdapter.List = cadList.datas;
                                listAdapter.notifyDataSetChanged();
                            }

                            if (cadList.pagination.hasmore) {
                                mListView.setPullLoadEnable(true);
                            } else {
                                mListView.setPullLoadEnable(false);
                            }
                        } else {
                            isSetAdapter = true;
                            ll_wine.setVisibility(View.GONE);
                            tv_record.setVisibility(View.GONE);
                            ll_null.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void OnMessageResponse(String url, String response, JSONObject status) throws JSONException {
        if (url.equals(BaseQuery.serviceUrl() + ApiInterface.Cart_edit_quantity)) {
            //ListView 是刷新状态还是加载更多状态
            isSetAdapter = false;
            //刷新列表
            cab_list();
            eliminate();
        }
    }

    /**
     * 清空1
     */
    private void eliminate() {
        if (listAdapter != null) {
            listAdapter.num = 0;
            iv_select.setImageResource(R.mipmap.ic_order_blank);
            listAdapter.total_num = 0;
            tv_total_num.setText(listAdapter.total_num + "件");
            for (int i = 0; i < listAdapter.goods_id.length; i++) {
                listAdapter.goods_id[i] = "0";
            }
        }
    }

}
