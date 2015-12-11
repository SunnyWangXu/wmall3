package com.wjhgw.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.wjhgw.business.bean.Status;
import com.wjhgw.business.response.BusinessResponse;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.dialog.GoodsArrDialog;
import com.wjhgw.ui.dialog.LoadDialog;
import com.wjhgw.ui.dialog.MyDialog;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.ShoppingCartAdapter;

import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Dialog = new LoadDialog(getActivity());
        rootView = inflater.inflate(R.layout.shopping_layout, container, false);
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

        mListView.addHeaderView(shopping_head);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                /**
                 *对话框
                 */
                goodsArrDialog = new GoodsArrDialog(getActivity(), "收藏", "删除");
                goodsArrDialog.show();
                goodsArrDialog.btnCollect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        favorites_add(listAdapter.List.get(position - 2).goods_id);
                        goodsArrDialog.dismiss();
                    }
                });
                goodsArrDialog.btnGoodsarrAddshopcar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cart_del(listAdapter.List.get(position - 2).cart_id);
                        goodsArrDialog.dismiss();
                    }
                });
                return false;
            }
        });
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
                    iv_select.setImageResource(R.mipmap.ic_default);
                    listAdapter.total = 0;
                    for (int i = 0; i < listAdapter.goods_id.length; i++) {
                        listAdapter.goods_id[i] = cartList.datas.get(0).goods_list.get(i).cart_id;
                        listAdapter.total += Double.parseDouble(cartList.datas.get(0).goods_list.get(i).goods_num) *
                                Double.parseDouble(cartList.datas.get(0).goods_list.get(i).goods_price);
                        listAdapter.total_num += Double.parseDouble(cartList.datas.get(0).goods_list.get(i).goods_num);
                    }
                    tv_total.setText("¥ " + listAdapter.total);
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
                    iv_select1.setImageResource(R.mipmap.ic_default);
                    for (int i = 0; i < listAdapter.List.size(); i++) {
                        listAdapter.goods_id[i] = listAdapter.List.get(i).cart_id;
                    }

                }
                iv_select.setImageResource(R.mipmap.ic_blank);
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
                    mDialog = new MyDialog(getActivity(), "温馨提示", "确定要删除该商品？");
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
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_collection:
                Toast.makeText(getActivity(), "该功能正在开发中", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        key = getActivity().getSharedPreferences("key", getActivity().MODE_APPEND).getString("key", "0");
        /**
         * 购物车列表请求
         */
        cart_list();
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
                                iv_select.setImageResource(R.mipmap.ic_default);
                                APP.getApp().getImageLoader().displayImage(cartList.datas.get(0).store_logo, iv_store_logo);
                            } else {
                                listAdapter.List = cartList.datas.get(0).goods_list;
                                if (!Edit) {
                                    eliminate1();
                                }
                                if (!delete) {
                                    delete = true;
                                    eliminate1();
                                }
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
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
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
                    } else {
                        Toast.makeText(getActivity(), status.status.msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 商品收藏
     */
    private void favorites_add(String goods_id) {
        Dialog.ProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);
        params.addBodyParameter("goods_id", goods_id);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Favorites_add, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Dialog.dismiss();
                Gson gson = new Gson();
                if (responseInfo.result != null) {
                    Status status = gson.fromJson(responseInfo.result, Status.class);
                    if (status.status.code == 10000) {
                        Toast.makeText(getActivity(), status.status.msg, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), status.status.msg, Toast.LENGTH_SHORT).show();
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
            iv_select1.setImageResource(R.mipmap.ic_blank);
            iv_select.setImageResource(R.mipmap.ic_blank);
            listAdapter.total = 0;
            listAdapter.total_num = 0;
            tv_total.setText("¥ " + listAdapter.total);
            tv_total_num.setText("(" + listAdapter.total_num + ")");
            for (int i = 0; i < listAdapter.goods_id.length; i++) {
                listAdapter.goods_id[i] = "0";
            }
        }
    }

    /**
     * 清空2
     */
    private void eliminate1() {
        listAdapter.goods_id = null;
        listAdapter.goods_id = new String[listAdapter.List.size()];
        for (int i = 0; i < listAdapter.List.size(); i++) {
            listAdapter.goods_id[i] = "0";
        }
    }
}
