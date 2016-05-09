package com.wjhgw.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import com.wjhgw.MainActivity;
import com.wjhgw.R;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.api.Address_del_Request;
import com.wjhgw.business.bean.CartList;
import com.wjhgw.business.bean.Order_goods_list;
import com.wjhgw.business.bean.SelectOrder;
import com.wjhgw.business.bean.SelectOrderDatas;
import com.wjhgw.business.bean.Status;
import com.wjhgw.business.response.BusinessResponse;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.activity.A0_LoginActivity;
import com.wjhgw.ui.activity.M7_MyCollectActivity;
import com.wjhgw.ui.activity.S0_ConfirmOrderActivity;
import com.wjhgw.ui.dialog.GoodsArrDialog;
import com.wjhgw.ui.dialog.LoadDialog;
import com.wjhgw.ui.dialog.MyDialog;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.ShoppingCartAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 购物车
 */
public class ShoppingCartFragment extends Fragment implements BusinessResponse, XListView.IXListViewListener,
        View.OnClickListener {

    private MyListView mListView;
    private String key;
    private ShoppingCartAdapter listAdapter = null;
    private CartList cartList;
    private ImageView iv_select;
    public ImageView iv_select1;
    public ImageView iv_store_logo;
    private TextView tv_collection;
    private TextView tv_home;
    private TextView tv_total;
    private TextView tv_total_num;
    private TextView tv_edit;
    private TextView tv_delete;
    private LinearLayout ll_empty_shop_cart;
    private LinearLayout ll_select1;
    private LinearLayout ll_select;
    private LinearLayout ll_settlement;
    private LinearLayout ll_to_settle;
    private LinearLayout shopping_head;
    private FrameLayout fl_edit;
    private Address_del_Request Request;
    //判断是刷新状态还是加载状态
    private Boolean isSetAdapter = true;
    //判断是否是全选删除状态
    private Boolean delete = true;
    private View rootView;
    private boolean Edit = true;
    private LoadDialog Dialog;
    private MyDialog mDialog;
    private GoodsArrDialog goodsArrDialog;
    private Intent intent;
    private TextView toConfirmOrder;
    //勾选的商品是否有下架无货的状态
    private boolean selectStatus = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Dialog = new LoadDialog(getActivity());
        rootView = inflater.inflate(R.layout.shopping_layout, container, false);
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

        mListView.addHeaderView(shopping_head);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        shopping_head = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.shopping_head, null);
        iv_select = (ImageView) rootView.findViewById(R.id.iv_select);
        iv_select1 = (ImageView) rootView.findViewById(R.id.iv_select1);
        iv_store_logo = (ImageView) shopping_head.findViewById(R.id.iv_store_logo);
        tv_edit = (TextView) rootView.findViewById(R.id.tv_edit);
        ll_settlement = (LinearLayout) rootView.findViewById(R.id.ll_settlement);
        ll_to_settle = (LinearLayout) rootView.findViewById(R.id.ll_to_settle);
        ll_select = (LinearLayout) rootView.findViewById(R.id.ll_select);
        ll_select1 = (LinearLayout) rootView.findViewById(R.id.ll_select1);
        ll_empty_shop_cart = (LinearLayout) rootView.findViewById(R.id.ll_empty_shop_cart);
        tv_delete = (TextView) rootView.findViewById(R.id.tv_delete);
        tv_total = (TextView) rootView.findViewById(R.id.tv_total);
        tv_home = (TextView) rootView.findViewById(R.id.tv_home);
        tv_collection = (TextView) rootView.findViewById(R.id.tv_collection);
        fl_edit = (FrameLayout) rootView.findViewById(R.id.fl_edit);
        tv_total_num = (TextView) rootView.findViewById(R.id.tv_total_num);
        mListView = (MyListView) rootView.findViewById(R.id.lv_list_layout);
    }

    /**
     * 设置监听事件
     */
    private void setClick() {
        ll_select.setOnClickListener(this);
        ll_select1.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
        tv_total.setOnClickListener(this);
        tv_home.setOnClickListener(this);
        tv_collection.setOnClickListener(this);
        ll_to_settle.setOnClickListener(this);
    }

    @Override
    public void onRefresh(int id) {
        cart_list();
        if (Edit) {
            isSetAdapter = true;
        } else {
            isSetAdapter = false;
        }
    }

    @Override
    public void onLoadMore(int id) {

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
                    listAdapter.total = 0;
                    for (int i = 0; i < listAdapter.goods_id.length; i++) {
                        listAdapter.goods_id[i] = cartList.datas.get(0).goods_list.get(i).cart_id;
                        listAdapter.total += Double.parseDouble(cartList.datas.get(0).goods_list.get(i).goods_num) *
                                Double.parseDouble(cartList.datas.get(0).goods_list.get(i).goods_price);
                        listAdapter.total_num += Double.parseDouble(cartList.datas.get(0).goods_list.get(i).goods_num);
                    }
                    tv_total.setText("¥ " + listAdapter.df.format(listAdapter.total));
                    tv_total_num.setText("(" + listAdapter.total_num + ")");
                }
                listAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_select1:
                if (listAdapter.num == listAdapter.goods_id.length) {
                    eliminate();
                } else {
                    delete = false;
                    listAdapter.num = listAdapter.goods_id.length;
                    iv_select1.setImageResource(R.mipmap.ic_order_select);
                    for (int i = 0; i < listAdapter.List.size(); i++) {
                        listAdapter.goods_id[i] = listAdapter.List.get(i).cart_id;
                    }

                }
                iv_select.setImageResource(R.mipmap.ic_order_blank);
                listAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_edit:
                eliminate();
                listAdapter.notifyDataSetChanged();
                if (Edit) {
                    Edit = false;
                    tv_edit.setText("完成");
                    fl_edit.setVisibility(View.VISIBLE);
                } else {
                    Edit = true;
                    tv_edit.setText("编辑");
                    fl_edit.setVisibility(View.GONE);
                }

                break;
            case R.id.tv_delete:
                if (listAdapter.num > 0) {
                    mDialog = new MyDialog(getActivity(), "确定要删除该商品？");
                    mDialog.show();
                    mDialog.positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StringBuffer cart_id = new StringBuffer();
                            for (int i = 0; i < listAdapter.goods_id.length; i++) {
                                if (!listAdapter.goods_id[i].equals("0")) {
                                    cart_id.append(listAdapter.goods_id[i] + ",");
                                }
                            }
                            cart_del(cart_id.toString().substring(0, cart_id.toString().length() - 1));
                            mDialog.dismiss();
                        }
                    });
                    mDialog.negative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "你还没有选择商品哦！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_home:
                intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
               /* FragmentTransaction transaction = getFragmentManager().beginTransaction();
                HomeFragment homeFragment = new HomeFragment();
                transaction.replace(R.id.content, homeFragment).commit();*/
                break;
            case R.id.tv_collection:
                if (key.equals("0")) {
                    intent = new Intent(getActivity(), A0_LoginActivity.class);
                    startActivity(intent);
                } else {
                    //Toast.makeText(getActivity(), "该功能正在开发中", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getActivity(), M7_MyCollectActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_to_settle:

                if (listAdapter.num > 0) {
                    StringBuffer cart_id = new StringBuffer();
                    for (int i = 0; i < listAdapter.goods_id.length; i++) {
                        if (!listAdapter.goods_id[i].equals("0")) {
                            cart_id.append(listAdapter.goods_id[i] + "|" + listAdapter.List.get(i).goods_num + ",");
                        }
                    }

                    //判断选中商品是否有无货或者下架状态
                    if (listAdapter.num > 0) {
                        StringBuffer selectStr = new StringBuffer();
                        for (int j = 0; j < listAdapter.selectStr.length; j++) {
                            selectStr.append(listAdapter.selectStr[j]);

                        }
                        String selectStr1 = selectStr.toString();
                        if (selectStr1.contains("无")) {
                            //有下架或无货商品
                            selectStatus = true;
                        } else {
                            //没有下架或无货商品
                            selectStatus = false;
                        }
                    }

                    if (selectStatus) {
                        Toast.makeText(getActivity(), "您选中的商品中有下架或者无货商品", Toast.LENGTH_SHORT).show();

                    } else {
                        /**
                         * 购买第一步接口
                         */
                        buy_step1(cart_id.toString().substring(0, cart_id.toString().length() - 1));
                    }

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

        if (key.equals("0")) {
            ll_empty_shop_cart.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else {
            /**
             * 购物车列表接口
             */
            cart_list();
        }
    }

    /**
     * 购物车列表接口
     */
    private void cart_list() {
        Dialog.ProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Cart_list, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dialog.dismiss();
                Gson gson = new Gson();
                if (responseInfo.result != null) {
                    cartList = gson.fromJson(responseInfo.result, CartList.class);

                    if (cartList.status.code == 10000) {

                        mListView.stopRefresh();
                        mListView.setRefreshTime();
                        tv_edit.setVisibility(View.VISIBLE);
                        ll_settlement.setVisibility(View.VISIBLE);
                        shopping_head.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.VISIBLE);
                        ll_empty_shop_cart.setVisibility(View.GONE);
                        if (cartList.datas != null) {
                            shopping_head.setVisibility(View.VISIBLE);
                            if (isSetAdapter) {
                                listAdapter = new ShoppingCartAdapter(getActivity(), cartList.datas.get(0).goods_list,
                                        iv_select, iv_select1, tv_total, tv_total_num, Request);
                                mListView.setAdapter(listAdapter);
                                iv_select.setImageResource(R.mipmap.ic_order_select);
                                APP.getApp().getImageLoader().displayImage(cartList.datas.get(0).store_logo, iv_store_logo);
                            } else {
                                listAdapter.List = cartList.datas.get(0).goods_list;
                                listAdapter.notifyDataSetChanged();
                            }
                        } else {
                            isSetAdapter = true;
                            Edit = true;
                            tv_edit.setText("编辑");
                            mListView.setAdapter(null);
                            tv_edit.setVisibility(View.GONE);
                            shopping_head.setVisibility(View.GONE);
                            fl_edit.setVisibility(View.GONE);
                            ll_settlement.setVisibility(View.GONE);
                            mListView.setVisibility(View.GONE);
                            ll_empty_shop_cart.setVisibility(View.VISIBLE);
                        }
                    } else if (cartList.status.code == 200103 || cartList.status.code == 200104) {
                        Toast.makeText(getActivity(), "登录超时或未登录", Toast.LENGTH_SHORT).show();
                        getActivity().getSharedPreferences("key", getActivity().MODE_APPEND).edit().putString("key", "0").commit();
                        startActivity(new Intent(getActivity(), A0_LoginActivity.class));
                    } else {
                        Toast.makeText(getActivity(), cartList.status.msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
            }
        });
    }

    /**
     * 购物车删除接口
     */
    private void cart_del(String cart_id) {
        Dialog.ProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("cart_id", cart_id);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Cart_del, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dialog.dismiss();
                Gson gson = new Gson();
                if (responseInfo.result != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);

                    if (status.status.code == 10000) {
                        isSetAdapter = false;
                        delete = false;
                        //刷新列表
                        cart_list();
                        eliminate();
                    } else if (status.status.code == 200103 || status.status.code == 200104) {
                        Toast.makeText(getActivity(), "登录超时或未登录", Toast.LENGTH_SHORT).show();
                        getActivity().getSharedPreferences("key", getActivity().MODE_APPEND).edit().putString("key", "0").commit();
                        startActivity(new Intent(getActivity(), A0_LoginActivity.class));
                    } else {
                        Toast.makeText(getActivity(), status.status.msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
            }
        });
    }

    /**
     * 购买第一步接口
     */
    private void buy_step1(final String cart_id) {
        Dialog.ProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("cart_id", cart_id);
        params.addBodyParameter("ifcart", "1");

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Buy_step1, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dialog.dismiss();
                Gson gson = new Gson();
                if (responseInfo.result != null) {

                    SelectOrder selectOrder = gson.fromJson(responseInfo.result, SelectOrder.class);
                    if (selectOrder.status.code == 10000) {

                        SelectOrderDatas selectOrderDatas = selectOrder.datas;

                        ArrayList<Order_goods_list> order_goods_lists = selectOrderDatas.store_cart_list.goods_list;
                        double realPay = 0.00;
                        for (int i = 0; i < order_goods_lists.size(); i++) {
                            realPay += Double.valueOf(order_goods_lists.get(i).goods_total);
                        }

                        Intent intent = new Intent(getActivity(), S0_ConfirmOrderActivity.class);
                        intent.putExtra("selectOrder", responseInfo.result);
                        //String tvTotal = tv_total.getText().toString();
                        intent.putExtra("cart_id", cart_id);
                        intent.putExtra("tv_total", realPay + "");
                        intent.putExtra("realPay", realPay);
                        intent.putExtra("for", "forShopp");
                        startActivity(intent);

                    } else if (selectOrder.status.code == 200103 || selectOrder.status.code == 200104) {
                        Toast.makeText(getActivity(), "登录超时或未登录", Toast.LENGTH_SHORT).show();
                        getActivity().getSharedPreferences("key", getActivity().MODE_APPEND).edit().putString("key", "0").commit();
                        startActivity(new Intent(getActivity(), A0_LoginActivity.class));
                    } else {
                        Toast.makeText(getActivity(), selectOrder.status.msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
            }
        });
    }

    @Override
    public void OnMessageResponse(String url, String response, JSONObject status) throws JSONException {
        if (url.equals(BaseQuery.serviceUrl() + ApiInterface.Cart_edit_quantity)) {
            if (response.equals("reduce") || response.equals("add")) {
                listAdapter.total = Request.total1;
                listAdapter.total_num = Request.total_num1;
                tv_total.setText("¥ " + listAdapter.df.format(listAdapter.total));
                tv_total_num.setText("(" + listAdapter.total_num + ")");
            } else if (response.equals("default")) {
            } else {
                double y = Double.parseDouble(listAdapter.List.get(Integer.parseInt(response)).goods_price);//编辑前的一个商品价格
                listAdapter.total = listAdapter.total + Request.total_num1 * y;
                listAdapter.total_num = listAdapter.total_num + Request.total_num1;

                tv_total.setText("¥ " + listAdapter.df.format(listAdapter.total));
                tv_total_num.setText("(" + listAdapter.total_num + ")");
            }
            //ListView 是刷新状态还是加载更多状态
            isSetAdapter = false;
            //刷新列表
            cart_list();

        } else if (url.equals(BaseQuery.serviceUrl() + ApiInterface.Favorites_add)) {
            //收藏成功
        } else if (url.equals(BaseQuery.serviceUrl() + ApiInterface.Cart_del)) {
            isSetAdapter = false;
            delete = false;
            //刷新列表
            cart_list();
            eliminate();
        }
    }

    /**
     * 清空1
     */
    private void eliminate() {
        if (listAdapter != null) {
            //delete = true;
            listAdapter.num = 0;
            iv_select1.setImageResource(R.mipmap.ic_order_blank);
            iv_select.setImageResource(R.mipmap.ic_order_blank);
            listAdapter.total = 0;
            listAdapter.total_num = 0;
            selectStatus = false;
            tv_total.setText("¥ " + listAdapter.df.format(listAdapter.total));
            tv_total_num.setText("(" + listAdapter.total_num + ")");
            for (int i = 0; i < listAdapter.goods_id.length; i++) {
                listAdapter.goods_id[i] = "0";
                listAdapter.selectStr[i] = "null";
            }
        }
    }

    /**
     * 清空2
     */
    private void eliminate1() {
        iv_select1.setImageResource(R.mipmap.ic_order_blank);
        listAdapter.goods_id = null;
        listAdapter.goods_id = new String[listAdapter.List.size()];
        for (int i = 0; i < listAdapter.List.size(); i++) {
            listAdapter.goods_id[i] = "0";
        }
    }
}
