package com.wjhgw.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.wjhgw.business.bean.MyLockBox;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.activity.A0_LoginActivity;
import com.wjhgw.ui.activity.M0_MyLockBoxActivity;
import com.wjhgw.ui.activity.M6_SetActivity;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;

public class MyFragment extends Fragment implements XListView.IXListViewListener, View.OnClickListener {
    private View MyLayout;
    private MyListView mListView;
    private LinearLayout MyAssetsLayout;
    private LinearLayout MyMessageLayout;
    private LinearLayout MySetHelpLayout;
    private LinearLayout MyOrderLayout;
    private MyLockBox userinformation;
    private ImageView myAvatar;
    private TextView member_nickname;
    private String key;
    private TextView titleName;
    private LinearLayout ll_LockBox;
    private LinearLayout ll_Set;
    private String memberName;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyLayout = inflater.inflate(R.layout.my_layout, container, false);

        /**
         * 加载视图
         */
        setInflaterView();

        /**
         * 初始化视图
         */
        initView();

        /**
         * 设置监听事件
         */
        setClick();

        /**
         * 给ListView添加视图
         */
        listAddHeader();


        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
        mListView.setAdapter(null);
        return MyLayout;
    }

    private void setInflaterView() {
        MyMessageLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.my_message_layout, null);
        MyOrderLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.my_order_layout, null);
        MyAssetsLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.my_assets_layout, null);
        MySetHelpLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.my_set_help_layout, null);
    }

    /**
     * 给ListView添加视图
     */
    private void listAddHeader() {
        mListView = (MyListView) MyLayout.findViewById(R.id.my_listview);
        mListView.addHeaderView(MyMessageLayout);
        mListView.addHeaderView(MyOrderLayout);
        mListView.addHeaderView(MyAssetsLayout);
        mListView.addHeaderView(MySetHelpLayout);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        titleName = (TextView) MyLayout.findViewById(R.id.tv_title_name);
        titleName.setText("我的");

        myAvatar = (ImageView) MyMessageLayout.findViewById(R.id.my_avatar);
        member_nickname = (TextView) MyMessageLayout.findViewById(R.id.member_nickname);
        ll_LockBox = (LinearLayout) MyMessageLayout.findViewById(R.id.ll_lockbox);

        ll_Set = (LinearLayout) MySetHelpLayout.findViewById(R.id.ll_set);
    }

    /**
     * 设置监听事件
     */
    private void setClick() {
        myAvatar.setOnClickListener(this);
        ll_LockBox.setOnClickListener(this);
        ll_Set.setOnClickListener(this);
    }

    @Override
    public void onRefresh(int id) {
        /**
         * 获取网络信息
         */
        if (!key.equals("0")) {
            load_User_information();
        }

        mListView.stopRefresh();
        mListView.setRefreshTime();

    }

    @Override
    public void onLoadMore(int id) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.my_avatar:
              /*  if (key.equals("0")) {
                    intent.setClass(getActivity(), A0_LoginActivity.class);
                    startActivity(intent);

                } else {

                }*/

                break;
            case R.id.ll_lockbox:
                if (key.equals("0")) {
                    intent.setClass(getActivity(), A0_LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), M0_MyLockBoxActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_set:
                if (key.equals("0") || key == null) {
                    intent.setClass(getActivity(), A0_LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), M6_SetActivity.class);
                    intent.putExtra("memberName", memberName);
                    startActivity(intent);
                }
                break;

            default:
                break;
        }
    }

    /**
     * 请求用户信息
     */
    private void load_User_information() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Get_member_base_info, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                /**
                 * 存入数据到本地
                 */
                SharedPreferences sf = getActivity().getSharedPreferences("wjhgw_auction", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor edit = sf.edit();
                edit.putString("userinformation_datas_member_avatar", responseInfo.result).commit();

                /**
                 * 解析
                 */
                Analytical(responseInfo.result);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                /**
                 * 取出本地緩存数据
                 */
                SharedPreferences preferences = getActivity().getSharedPreferences("wjhgw_auction", getActivity().MODE_PRIVATE);
                String auctionSuperValueData = preferences.getString("userinformation_datas_member_avatar", "");

                Analytical(auctionSuperValueData);
            }
        });
    }

    /**
     * 解析用户信息
     */
    private void Analytical(String responseInfoResult) {
        Gson gson = new Gson();
        if (responseInfoResult != null) {
            userinformation = gson.fromJson(responseInfoResult, MyLockBox.class);

            if (userinformation.status.code == 10000) {
                APP.getApp().getImageLoader().displayImage(userinformation.datas.member_avatar, myAvatar);
                member_nickname.setText(userinformation.datas.member_nickname);

                memberName = userinformation.datas.member_name;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        key = getActivity().getSharedPreferences("key", getActivity().MODE_APPEND).getString("key", "0");
        /**
         * 获取网络信息
         */
        if (!key.equals("0")) {
            load_User_information();
        }
    }

}
