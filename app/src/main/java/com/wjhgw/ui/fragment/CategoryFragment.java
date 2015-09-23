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

import com.wjhgw.R;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.api.Classification_Request;
import com.wjhgw.business.response.BusinessResponse;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.activity.CaptureActivity;
import com.wjhgw.ui.image.LoadImageByVolley;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;
import com.wjhgw.ui.view.listview.adapter.goods_class_adapter;
import com.wjhgw.ui.view.listview.adapter.grid_adapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CategoryFragment extends Fragment implements XListView.IXListViewListener, BusinessResponse, View.OnClickListener {
    public static int MAK = 0;
    private ImageView image;
    private ImageView qrcode_scanner;
    private MyListView mListView;
    private goods_class_adapter adapter;
    private grid_adapter adapter1;
    private Classification_Request dataModel;
    private GridView grid;
    private LoadImageByVolley Image;
    public static String RESULT_MESSAGE = null; // 接收扫二维码返回数据

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View View = inflater.inflate(R.layout.discoverylayout, container, false);

        dataModel = new Classification_Request(getActivity());
        dataModel.addResponseListener(this);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key", "ed86e0e4dd9fd54ffea7511b9fc6728e");
        dataModel.goods_class(hashMap, BaseQuery.serviceUrl() + ApiInterface.Goods_class);


        grid = (GridView) View.findViewById(R.id.grid);
        image = (ImageView) View.findViewById(R.id.image);
        qrcode_scanner = (ImageView) View.findViewById(R.id.qrcode_scanner);

        mListView = (MyListView) View.findViewById(R.id.discovery_listview);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
        Image = new LoadImageByVolley(getActivity(), image);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MAK = position - 1;
                adapter.notifyDataSetChanged();
                Image.loadImageByVolley(dataModel.class_List.get(position - 1).image);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("key", "ed86e0e4dd9fd54ffea7511b9fc6728e");
                hashMap.put("gc_id", dataModel.class_List.get(position - 1).gc_id);
                dataModel.goods_class1(hashMap, BaseQuery.serviceUrl() + ApiInterface.Goods_class1);
            }
        });

        qrcode_scanner.setOnClickListener(this);
        return View;
    }

    @Override
    public void OnMessageResponse(String url, String response, JSONObject status) throws JSONException {
        if (url.equals(BaseQuery.serviceUrl() + ApiInterface.Goods_class)) {
            if (status.getString("code").equals("10000")) {
                adapter = new goods_class_adapter(getActivity(), dataModel.class_List);
                mListView.setAdapter(adapter);
                Image.loadImageByVolley(dataModel.class_List.get(0).image);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("key", "ed86e0e4dd9fd54ffea7511b9fc6728e");
                hashMap.put("gc_id", dataModel.class_List.get(0).gc_id);
                dataModel.goods_class1(hashMap, BaseQuery.serviceUrl() + ApiInterface.Goods_class1);
            } else if (status.getString("code").equals("600100")) {
                Toast.makeText(getActivity(), status.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        }
        if (url.equals(BaseQuery.serviceUrl() + ApiInterface.Goods_class1)) {
            adapter1 = new grid_adapter(getActivity(), dataModel.class_List1);
            grid.setAdapter(adapter1);
        }
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
            /*try {
                URL myURL = new URL(RESULT_MESSAGE);
                if (myURL.getProtocol().equals("http")
                        || myURL.getProtocol().equals("https")
                        || myURL.getProtocol().equals("ftp")) {
                    if (myURL.getPath().equals("/mobile/goods.php")
                            || myURL.getPath().equals("/goods.php")) {
                        Intent it = new Intent(getActivity(),
                                B2_ProductDetailActivity.class);
                        it.putExtra(
                                "good_id",
                                myURL.getQuery().substring(3,
                                        myURL.getQuery().length()));
                        this.startActivity(it);
                    } else {
                        Intent intent = new Intent(getActivity(),
                                BannerWebActivity.class);
                        intent.putExtra("url", RESULT_MESSAGE);
                        startActivity(intent);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }*/
            Toast.makeText(getActivity(), "扫描返回" + RESULT_MESSAGE, Toast.LENGTH_SHORT).show();
            RESULT_MESSAGE = null;
        }
    }
}
