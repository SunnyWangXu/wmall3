package com.wjhgw.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.wjhgw.business.bean.Goods_attr;
import com.wjhgw.business.bean.Goods_class1;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.activity.CaptureActivity;
import com.wjhgw.ui.activity.SearchActivity;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.AttrAdapter;
import com.wjhgw.ui.view.listview.adapter.goods_class_adapter;

public class CategoryFragment extends Fragment implements XListView.IXListViewListener, View.OnClickListener {
    public static int MAK = 0;
    private ImageView ivGoods;
    private ImageView qrcode_scanner;
    private MyListView mListView;
    private goods_class_adapter adapter;
    private AttrAdapter attrAdapter;
    private Goods_class1 goods_class1;
    private Goods_attr goods_attr;
    private LinearLayout ll_search;
    View View;
    public static String RESULT_MESSAGE = null; // 接收扫二维码返回数据
    private String key;
    private Intent intent;
    private ListView lvAttr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View = inflater.inflate(R.layout.classification_layout, container, false);
        key = getActivity().getSharedPreferences("key", getActivity().MODE_APPEND).getString("key", "0");

        onFindViews();
        /**
         * 请求一级商品分类
         */
        load_goods_class1();

        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MAK = position - 1;
                adapter.notifyDataSetChanged();

                String imageUrl = goods_class1.datas.get(MAK).gc_image;
                if (imageUrl != "" && imageUrl != null) {

                    APP.getApp().getImageLoader().displayImage(imageUrl, ivGoods, APP.getApp().getImageOptions());
                } else {
                    ivGoods.setVisibility(View.GONE);
                }

                /**
                 * 请求商品分类属性
                 */
                load_goods_attr(goods_class1.datas.get(position - 1).gc_id);
            }
        });

        qrcode_scanner.setOnClickListener(this);
        ll_search.setOnClickListener(this);

        return View;
    }

    private void onFindViews() {
        lvAttr = (ListView) View.findViewById(R.id.lv_attr);
        ivGoods = (ImageView) View.findViewById(R.id.iv_goods_class);
        qrcode_scanner = (ImageView) View.findViewById(R.id.qrcode_scanner);
        ll_search = (LinearLayout) View.findViewById(R.id.ll_search);

        mListView = (MyListView) View.findViewById(R.id.discovery_listview);
    }

    @Override
    public void onRefresh(int id) {

    }

    @Override
    public void onLoadMore(int id) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qrcode_scanner:
                intent = new Intent(getActivity(), CaptureActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.ll_search:
                intent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (RESULT_MESSAGE != null) {
            Toast.makeText(getActivity(), "扫描返回" + RESULT_MESSAGE, Toast.LENGTH_SHORT).show();
            RESULT_MESSAGE = null;
        }
    }

    /**
     * 请求一级商品分类
     */
    private void load_goods_class1() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Goods_class1, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                /**
                 * 缓存一级商品分类
                 */
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("wjhgw_category", getActivity().MODE_PRIVATE).edit();
                editor.putString("goods_class1", responseInfo.result).commit();

                /**
                 * 解析一级商品分类请求
                 */
                parseGoodsClass1(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                /**
                 * * 取出本地緩存数据
                 */
                SharedPreferences preferences = getActivity().getSharedPreferences("wjhgw_category", getActivity().MODE_PRIVATE);
                String goodsClass1Data = preferences.getString("goods_class1", "");
                if (goodsClass1Data != null && goodsClass1Data != "") {
                    /**
                     * 解析一级商品分类请求
                     */
                    parseGoodsClass1(goodsClass1Data);

                }

            }
        });
    }

    /**
     * 解析一级商品分类请求
     */
    private void parseGoodsClass1(String responseInfoResult) {
        Gson gson = new Gson();
        if (responseInfoResult != null) {
            goods_class1 = gson.fromJson(responseInfoResult, Goods_class1.class);

            if (goods_class1.status.code == 10000) {
                adapter = new goods_class_adapter(getActivity(), goods_class1.datas);
                mListView.setAdapter(adapter);
                if (goods_class1.datas.size() > 0 && goods_class1.datas != null) {
                    APP.getApp().getImageLoader().displayImage(goods_class1.datas.get(MAK).gc_image, ivGoods, APP.getApp().getImageOptions());
                    /**
                     * 请求商品分类属性，默认选择第一个
                     * 图片默认第一个
                     */
                    load_goods_attr(goods_class1.datas.get(0).gc_id);
                    APP.getApp().getImageLoader().displayImage(goods_class1.datas.get(0).gc_image, ivGoods);
                }
            }
        }
    }

    /**
     * 请求商品分类属性
     */
    private void load_goods_attr(String gc_id) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("gc_id", gc_id);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Goods_attr, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                /**
                 * 缓存商品属性
                 */
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("wjhgw_category_attr", getActivity().MODE_PRIVATE).edit();
                editor.putString("goods_attr", responseInfo.result).commit();
                /**
                 * 解析商品属性
                 */
                parseGoodsAttr(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();

                /**
                 * * 取出本地緩存数据
                 */
                SharedPreferences preferences = getActivity().getSharedPreferences("wjhgw_category_attr", getActivity().MODE_PRIVATE);
                String goodsClass1Data = preferences.getString("goods_attr", "");

                parseGoodsAttr(goodsClass1Data);
            }
        });
    }

    /**
     * 解析商品属性
     */
    private void parseGoodsAttr(String responseInfoResult) {
        Gson gson = new Gson();
        if (responseInfoResult != null) {
            goods_attr = gson.fromJson(responseInfoResult, Goods_attr.class);

            if (goods_attr.status.code == 10000) {
                attrAdapter = new AttrAdapter(getActivity(), goods_attr.datas);
                lvAttr.setAdapter(attrAdapter);
            }
        }
    }
}
