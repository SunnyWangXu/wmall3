package com.wjhgw.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.wjhgw.business.bean.Auto_complete_Pager;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.view.listview.adapter.Hot_searchAdapter;
import com.wjhgw.ui.view.listview.adapter.SearchAutoAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 搜索
 */
public class C2_SearchActivity extends BaseActivity implements OnClickListener {
    public static final String SEARCH_HISTORY = "search_history";
    private TextView search;
    private TextView d2_eliminate;
    private ImageView iv_title_back;
    private ImageView d2_delete;
    private EditText search_name;
    private GridView hot_searc;
    private SearchAutoAdapter mSearchAutoAdapter;
    private ListView mAutoListView;
    private ListView lv_listview;
    private Intent intent;
    private Auto_complete_Pager auto_complete;
    private Auto_complete_Pager hot_search;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        init();
        if (mSearchAutoAdapter.DATA == 1) {
            d2_eliminate.setText("暂无历史记录");
        }
        hot_search();
    }

    @Override
    public void onInit() {
        d2_eliminate = (TextView) findViewById(R.id.d2_eliminate);
        search = (TextView) findViewById(R.id.search);
        iv_title_back = (ImageView) findViewById(R.id.iv_title_back);
        d2_delete = (ImageView) findViewById(R.id.d2_delete);
        search_name = (EditText) findViewById(R.id.search_name);

        lv_listview = (ListView) findViewById(R.id.lv_listview);
        mAutoListView = (ListView) findViewById(R.id.auto_listview);
        hot_searc = (GridView) findViewById(R.id.gv_goods_attr_value);
    }

    @Override
    public void onFindViews() {

    }

    @Override
    public void onInitViewData() {
        d2_delete.setOnClickListener(this);
        search.setOnClickListener(this);
        d2_eliminate.setOnClickListener(this);
        iv_title_back.setOnClickListener(this);
    }

    @Override
    public void onBindListener() {

    }

    private void init() {

        search_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 0) {
                    auto_complete(search_name.getText().toString().trim());
                    d2_delete.setVisibility(View.VISIBLE);
                } else {
                    if (start == 0 && count > 0) {
                        auto_complete(search_name.getText().toString().trim());
                        d2_delete.setVisibility(View.VISIBLE);
                    } else {
                        mSearchAutoAdapter = new SearchAutoAdapter(C2_SearchActivity.this, 10, null);
                        mAutoListView.setAdapter(mSearchAutoAdapter);
                        if (mSearchAutoAdapter.DATA == 1) {
                            d2_eliminate.setText("暂无历史记录");
                            d2_delete.setVisibility(View.VISIBLE);
                        } else if (mSearchAutoAdapter.DATA == 0) {
                            d2_delete.setVisibility(View.GONE);
                            d2_eliminate.setText("清空历史记录");
                        }
                        lv_listview.setVisibility(View.GONE);
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
        search_name.setOnKeyListener(onKey);

        mSearchAutoAdapter = new SearchAutoAdapter(this, 10, null);
        mAutoListView.setAdapter(mSearchAutoAdapter);
        mAutoListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {
                SharedPreferences sp = getSharedPreferences(C2_SearchActivity.SEARCH_HISTORY, 0);
                String longhistory = sp.getString(C2_SearchActivity.SEARCH_HISTORY, "");
                String[] hisArrays = longhistory.split(",");
                search_name.setText(hisArrays[position]);
                search.performClick();
            }
        });

        lv_listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {
                search_name.setText(auto_complete.datas.get(position).value);
                search.performClick();
            }
        });
        hot_searc.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {
                search_name.setText(hot_search.datas.get(position).value);
                search.performClick();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 保存搜索记录
     */
    private void saveSearchHistory() {
        String text = search_name.getText().toString().trim();
        if (text.length() < 1) {
            return;
        }
        SharedPreferences sp = getSharedPreferences(SEARCH_HISTORY, 0);
        String longhistory = sp.getString(SEARCH_HISTORY, "");
        String[] tmpHistory = longhistory.split(",");
        ArrayList<String> history = new ArrayList<String>(
                Arrays.asList(tmpHistory));
        if (history.size() > 0) {
            int i;
            for (i = 0; i < history.size(); i++) {
                if (text.equals(history.get(i))) {
                    history.remove(i);
                    break;
                }
            }
            history.add(0, text);
        }
        if (history.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < history.size(); i++) {
                sb.append(history.get(i) + ",");
            }
            sp.edit().putString(SEARCH_HISTORY, sb.toString()).commit();
        } else {
            sp.edit().putString(SEARCH_HISTORY, text + ",").commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:

                String keyword = search_name.getText().toString().trim();
                if (!keyword.equals("")) {
                    saveSearchHistory();
                    search_name.setText("");
                    mSearchAutoAdapter.initSearchHistory();
                    mSearchAutoAdapter = new SearchAutoAdapter(this, 10, null);
                    mAutoListView.setAdapter(mSearchAutoAdapter);
                    intent = new Intent(this, C3_GoodsArraySearchActivity.class);
                    intent.putExtra("keyword", keyword);
                    startActivity(intent);
                    finish(false);
                } else {
                    showToastShort("请输入您想要搜索的商品");
                }

                break;
            case R.id.d2_delete:
                mSearchAutoAdapter = new SearchAutoAdapter(this, 10, null);
                mAutoListView.setAdapter(mSearchAutoAdapter);
                search_name.setText("");
                d2_delete.setVisibility(View.GONE);
                break;
            case R.id.d2_eliminate:
                SharedPreferences sp = this.getSharedPreferences(
                        C2_SearchActivity.SEARCH_HISTORY, 0);
                sp.edit().clear().commit();
                mSearchAutoAdapter.initSearchHistory();
                mSearchAutoAdapter.performFiltering(null);
                d2_eliminate.setText("暂无历史记录");
                break;
            case R.id.iv_title_back:
                finish();
                break;

            default:
                break;
        }

    }

    /**
     * 讯搜
     */
    private void auto_complete(String term) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("term", term);
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Auto_complete, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    Auto_complete_Pager auto= gson.fromJson(responseInfo.result, Auto_complete_Pager.class);

                    if (auto.status.code == 10000) {
                        auto_complete = auto;
                        lv_listview.setVisibility(View.VISIBLE);
                        mSearchAutoAdapter = new SearchAutoAdapter(C2_SearchActivity.this, 10, auto_complete.datas);
                        lv_listview.setAdapter(mSearchAutoAdapter);
                        //mAutoListView.setAdapter(mSearchAutoAdapter);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToastShort("请求失败");
            }
        });
    }

    /**
     * 热点词汇
     */
    private void hot_search() {
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Hot_search, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo != null) {
                    hot_search = gson.fromJson(responseInfo.result, Auto_complete_Pager.class);

                    if (hot_search.status.code == 10000) {
                        Hot_searchAdapter hot_searchAdapter = new Hot_searchAdapter(C2_SearchActivity.this, hot_search.datas);
                        hot_searc.setAdapter(hot_searchAdapter);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToastShort("请求失败");
            }
        });
    }

    /**
     * 监听输入法中为回车键时
     */
    OnKeyListener onKey = new OnKeyListener() {

        @Override

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                saveSearchHistory();
                mSearchAutoAdapter.initSearchHistory();
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                return true;
            }
            return false;
        }

    };
}
