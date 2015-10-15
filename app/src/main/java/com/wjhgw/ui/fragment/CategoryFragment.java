package com.wjhgw.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.wjhgw.business.bean.Goods_class_Pager1;
import com.wjhgw.business.bean.Goods_class_Pager2;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.activity.CaptureActivity;
import com.wjhgw.ui.image.LoadImageByVolley;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.goods_class_adapter;
import com.wjhgw.ui.view.listview.adapter.grid_adapter;

public class CategoryFragment extends Fragment implements XListView.IXListViewListener, View.OnClickListener {
    public static int MAK = 0;
    private ImageView image;
    private ImageView qrcode_scanner;
    private MyListView mListView;
    private goods_class_adapter adapter;
    private grid_adapter adapter1;
    private GridView grid;
    private LoadImageByVolley Image;
    private Goods_class_Pager1 goods_class1;
    private Goods_class_Pager2 goods_class2;
    View View;
    public static String RESULT_MESSAGE = null; // 接收扫二维码返回数据

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View = inflater.inflate(R.layout.classification_layout, container, false);
        /**
         * 商品一级分类
         */
        goods_class1();

        grid = (GridView) View.findViewById(R.id.grid);
        image = (ImageView) View.findViewById(R.id.image);
        qrcode_scanner = (ImageView) View.findViewById(R.id.qrcode_scanner);

        mListView = (MyListView) View.findViewById(R.id.discovery_listview);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
        Image = new LoadImageByVolley(getActivity());

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MAK = position - 1;
                adapter.notifyDataSetChanged();
                Image.loadImageByVolley(goods_class1.datas.get(position - 1).image, image);
                goods_class2(goods_class1.datas.get(position - 1).gc_id);
            }
        });

        qrcode_scanner.setOnClickListener(this);
        return View;
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
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                getActivity().startActivity(intent);
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
     * 商品一级分类
     */
    private void goods_class1() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", "b961e533cb73bb5f9b4dce25e38a6f76");
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Goods_class, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    goods_class1 = gson.fromJson(responseInfo.result, Goods_class_Pager1.class);

                    if (goods_class1.status.code == 10000) {
                        adapter = new goods_class_adapter(getActivity(),goods_class1.datas);
                        mListView.setAdapter(adapter);
                        if (goods_class1.datas.size() > 0) {
                            Image.loadImageByVolley(goods_class1.datas.get(0).image, image);
                            goods_class2(goods_class1.datas.get(0).gc_id);
                        }
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 指定分类
     */
    private void goods_class2(String gc_id) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", "b961e533cb73bb5f9b4dce25e38a6f76");
        params.addBodyParameter("gc_id", gc_id);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Goods_class1, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    goods_class2 = gson.fromJson(responseInfo.result, Goods_class_Pager2.class);

                    if (goods_class2.status.code == 10000) {
                        adapter1 = new grid_adapter(getActivity(), goods_class2.datas);
                        grid.setAdapter(adapter1);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
