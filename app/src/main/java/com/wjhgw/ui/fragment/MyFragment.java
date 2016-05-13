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
import android.widget.RelativeLayout;
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
import com.wjhgw.business.bean.OrderAmount;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.activity.A0_LoginActivity;
import com.wjhgw.ui.activity.CabinetActivity;
import com.wjhgw.ui.activity.D0_OrderActivity;
import com.wjhgw.ui.activity.D5_CustomerActivity;
import com.wjhgw.ui.activity.M0_MyLockBoxActivity;
import com.wjhgw.ui.activity.M6_SetActivity;
import com.wjhgw.ui.activity.M7_MyCollectActivity;
import com.wjhgw.ui.activity.Z0_AssetsActivity;
import com.wjhgw.ui.activity.Z1_Prepaid_card_balanceActivity;
import com.wjhgw.ui.dialog.LoadDialog;
import com.wjhgw.ui.dialog.UnderDialog;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView;

public class MyFragment extends Fragment implements XListView.IXListViewListener, View.OnClickListener {
    private View MyLayout;
    private MyListView mListView;
    private LinearLayout MyAssetsLayout;
    private LinearLayout MyMessageLayout;
    private LinearLayout MyHelpLayout;
    private LinearLayout MyOrderLayout;
    private MyLockBox userinformation;
    private ImageView myAvatar;
    private TextView member_nickname;
    private TextView available_predeposit;
    private TextView tv_rechargeable_card;
    private TextView tv_hgb;
    private TextView tv_vouchers;
    private LinearLayout ll_all_orders;
    private RelativeLayout rl_layout1;
    private RelativeLayout rl_layout5;
    private RelativeLayout rl_layout2;
    private RelativeLayout rl_layout3;
    private RelativeLayout rl_layout4;
    private TextView tv_statistics1;
    private TextView tv_statistics5;
    private TextView tv_statistics2;
    private TextView tv_statistics3;
    private TextView tv_statistics4;
    private ImageView iv_message;
    private String key;
    private ImageView ivSet;
    private FrameLayout ll_LockBox;
    private LinearLayout ll_assets;
    private LinearLayout ll_cabinet;
    private String memberName;
    private LoadDialog Dialog;
    private LinearLayout ll_my_collect;
    private FrameLayout profileHeadShare;
    private FrameLayout profileHeadAddClient;
    private LinearLayout ll_rechargeable_card;
    private LinearLayout ll_available_predeposit;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyLayout = inflater.inflate(R.layout.my_layout, container, false);
        Dialog = new LoadDialog(getActivity());
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
        MyHelpLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.my_help_layout, null);
    }

    /**
     * 给ListView添加视图
     */
    private void listAddHeader() {
        mListView = (MyListView) MyLayout.findViewById(R.id.my_listview);
        mListView.addHeaderView(MyMessageLayout);
        mListView.addHeaderView(MyOrderLayout);
        mListView.addHeaderView(MyAssetsLayout);
        mListView.addHeaderView(MyHelpLayout);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        ivSet = (ImageView) MyLayout.findViewById(R.id.iv_set);
        iv_message = (ImageView) MyLayout.findViewById(R.id.iv_message);

        myAvatar = (ImageView) MyMessageLayout.findViewById(R.id.my_avatar);
        member_nickname = (TextView) MyMessageLayout.findViewById(R.id.member_nickname);

        ll_my_collect = (LinearLayout) MyHelpLayout.findViewById(R.id.ll_my_collect);
        profileHeadShare = (FrameLayout) MyMessageLayout.findViewById(R.id.profile_head_share);
        profileHeadAddClient = (FrameLayout) MyMessageLayout.findViewById(R.id.profile_head_AddClient);

        ll_all_orders = (LinearLayout) MyOrderLayout.findViewById(R.id.ll_all_orders);
        rl_layout1 = (RelativeLayout) MyOrderLayout.findViewById(R.id.rl_layout1);
        rl_layout5 = (RelativeLayout) MyOrderLayout.findViewById(R.id.rl_layout5);
        rl_layout2 = (RelativeLayout) MyOrderLayout.findViewById(R.id.rl_layout2);
        rl_layout3 = (RelativeLayout) MyOrderLayout.findViewById(R.id.rl_layout3);
        rl_layout4 = (RelativeLayout) MyOrderLayout.findViewById(R.id.rl_layout4);
        tv_statistics1 = (TextView) MyOrderLayout.findViewById(R.id.tv_statistics1);
        tv_statistics5 = (TextView) MyOrderLayout.findViewById(R.id.tv_statistics5);
        tv_statistics2 = (TextView) MyOrderLayout.findViewById(R.id.tv_statistics2);
        tv_statistics3 = (TextView) MyOrderLayout.findViewById(R.id.tv_statistics3);
        tv_statistics4 = (TextView) MyOrderLayout.findViewById(R.id.tv_statistics4);
        available_predeposit = (TextView) MyAssetsLayout.findViewById(R.id.tv_available_predeposit);
        tv_rechargeable_card = (TextView) MyAssetsLayout.findViewById(R.id.tv_rechargeable_card);
        tv_hgb = (TextView) MyAssetsLayout.findViewById(R.id.tv_hgb);
        tv_vouchers = (TextView) MyAssetsLayout.findViewById(R.id.tv_vouchers);
        ll_assets = (LinearLayout) MyAssetsLayout.findViewById(R.id.ll_assets);
        ll_rechargeable_card = (LinearLayout) MyAssetsLayout.findViewById(R.id.ll_rechargeable_card);
        ll_available_predeposit = (LinearLayout) MyAssetsLayout.findViewById(R.id.ll_available_predeposit);
        ll_LockBox = (FrameLayout) MyMessageLayout.findViewById(R.id.ll_lockbox);

        ll_cabinet = (LinearLayout) MyHelpLayout.findViewById(R.id.ll_cabinet);
    }

    /**
     * 设置监听事件
     */
    private void setClick() {
        iv_message.setOnClickListener(this);
        ll_LockBox.setOnClickListener(this);
        ll_my_collect.setOnClickListener(this);
        profileHeadShare.setOnClickListener(this);
        profileHeadAddClient.setOnClickListener(this);
        ivSet.setOnClickListener(this);
        ll_cabinet.setOnClickListener(this);
        ll_all_orders.setOnClickListener(this);
        ll_assets.setOnClickListener(this);
        rl_layout1.setOnClickListener(this);
        rl_layout5.setOnClickListener(this);
        rl_layout2.setOnClickListener(this);
        rl_layout3.setOnClickListener(this);
        rl_layout4.setOnClickListener(this);
        ll_available_predeposit.setOnClickListener(this);
        ll_rechargeable_card.setOnClickListener(this);
    }

    @Override
    public void onRefresh(int id) {
        /**
         * 请求用户信息
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
        final UnderDialog underdevelopmentDialog = new UnderDialog(getActivity(), "功能正在开发中,敬请期待");
        switch (v.getId()) {
            case R.id.ll_lockbox:
                if (key.equals("0")) {
                    intent.setClass(getActivity(), A0_LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), M0_MyLockBoxActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.ll_cabinet:
                if (key.equals("0")) {
                    intent.setClass(getActivity(), A0_LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), CabinetActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_my_collect:
                if (key.equals("0")) {
                    intent.setClass(getActivity(), A0_LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), M7_MyCollectActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.profile_head_share:

                underdevelopmentDialog.show();
                underdevelopmentDialog.tv_goto_setpaypwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        underdevelopmentDialog.dismiss();
                    }
                });
                break;

            case R.id.profile_head_AddClient:

                underdevelopmentDialog.show();
                underdevelopmentDialog.tv_goto_setpaypwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        underdevelopmentDialog.dismiss();
                    }
                });
                break;

            case R.id.iv_set:
//                if (key.equals("0") || key == null) {
//                    intent.setClass(getActivity(), A0_LoginActivity.class);
//                    startActivity(intent);
//                } else {
                intent.setClass(getActivity(), M6_SetActivity.class);
                intent.putExtra("memberName", memberName);
                startActivity(intent);
//                }
                break;
            case R.id.ll_all_orders:

                if (key.equals("0") || key == null) {
                    intent.setClass(getActivity(), A0_LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), D0_OrderActivity.class);
                    intent.putExtra("order_state", "");
                    intent.putExtra("name", "所有订单");
                    startActivity(intent);
                }
                break;
            case R.id.rl_layout1:
                if (key.equals("0") || key == null) {
                    intent.setClass(getActivity(), A0_LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), D0_OrderActivity.class);
                    intent.putExtra("order_state", "10");
                    intent.putExtra("name", "待付款");
                    startActivity(intent);
                }
                break;
            case R.id.rl_layout5:
                if (key.equals("0") || key == null) {
                    intent.setClass(getActivity(), A0_LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), D0_OrderActivity.class);
                    intent.putExtra("order_state", "20");
                    intent.putExtra("name", "待发货");
                    startActivity(intent);
                }
                break;
            case R.id.rl_layout2:
                if (key.equals("0") || key == null) {
                    intent.setClass(getActivity(), A0_LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), D0_OrderActivity.class);
                    intent.putExtra("order_state", "30");
                    intent.putExtra("name", "待收货");
                    startActivity(intent);
                }
                break;
            case R.id.rl_layout3:
                if (key.equals("0") || key == null) {
                    intent.setClass(getActivity(), A0_LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), D0_OrderActivity.class);
                    intent.putExtra("order_state", "60");
                    intent.putExtra("name", "待评价");
                    startActivity(intent);
                }
                break;
            case R.id.rl_layout4:
                if (key.equals("0") || key == null) {
                    intent.setClass(getActivity(), A0_LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), D5_CustomerActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_assets:
                if (key.equals("0") || key == null) {
                    intent.setClass(getActivity(), A0_LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(getActivity(), Z0_AssetsActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_available_predeposit:
                if (key.equals("0") || key == null) {
                    intent.setClass(getActivity(), A0_LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), Z1_Prepaid_card_balanceActivity.class);
                    intent.putExtra("state","2");
                    startActivity(intent);
                }
                break;
            case R.id.ll_rechargeable_card:
                if (key.equals("0") || key == null) {
                    intent.setClass(getActivity(), A0_LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), Z1_Prepaid_card_balanceActivity.class);
                    intent.putExtra("state","1");
                    startActivity(intent);
                }
                break;
            case R.id.iv_message:
                underdevelopmentDialog.show();
                underdevelopmentDialog.tv_goto_setpaypwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        underdevelopmentDialog.dismiss();
                    }
                });
                break;

            default:
                break;
        }
    }

    /**
     * 获取未完成处理的订单数量
     */
    private void order_amount() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Order_amount, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo.result != null) {
                    OrderAmount orderAmount = gson.fromJson(responseInfo.result, OrderAmount.class);

                    if (orderAmount.status.code == 10000) {
                        if (orderAmount.datas.un_pay.equals("0")) {
                            tv_statistics1.setVisibility(View.GONE);
                        } else {
                            tv_statistics1.setVisibility(View.VISIBLE);
                            if (Integer.parseInt(orderAmount.datas.un_pay) > 9) {
                                tv_statistics1.setText("9+");
                            } else {
                                tv_statistics1.setText(orderAmount.datas.un_pay);
                            }
                        }

                        if (orderAmount.datas.un_shipping.equals("0")) {
                            tv_statistics5.setVisibility(View.GONE);
                        } else {
                            tv_statistics5.setVisibility(View.VISIBLE);
                            if (Integer.parseInt(orderAmount.datas.un_shipping) > 9) {
                                tv_statistics5.setText("9+");
                            } else {
                                tv_statistics5.setText(orderAmount.datas.un_shipping);
                            }
                        }

                        if (orderAmount.datas.un_receive.equals("0")) {
                            tv_statistics2.setVisibility(View.GONE);
                        } else {
                            tv_statistics2.setVisibility(View.VISIBLE);
                            if (Integer.parseInt(orderAmount.datas.un_receive) > 9) {
                                tv_statistics2.setText("9+");
                            } else {
                                tv_statistics2.setText(orderAmount.datas.un_receive);
                            }
                        }

                        if (orderAmount.datas.un_evaluate.equals("0")) {
                            tv_statistics3.setVisibility(View.GONE);
                        } else {
                            tv_statistics3.setVisibility(View.VISIBLE);
                            if (Integer.parseInt(orderAmount.datas.un_evaluate) > 9) {
                                tv_statistics3.setText("9+");
                            } else {
                                tv_statistics3.setText(orderAmount.datas.un_evaluate);
                            }
                        }

                        if (orderAmount.datas.un_done_fefund.equals("0")) {
                            tv_statistics4.setVisibility(View.GONE);
                        } else {
                            tv_statistics4.setVisibility(View.VISIBLE);
                            if (Integer.parseInt(orderAmount.datas.un_done_fefund) > 9) {
                                tv_statistics4.setText("9+");
                            } else {
                                tv_statistics4.setText(orderAmount.datas.un_done_fefund);
                            }
                        }
                    } else if (orderAmount.status.code == 200103 || orderAmount.status.code == 200104) {
                        Toast.makeText(getActivity(), "登录超时或未登录", Toast.LENGTH_SHORT).show();
                        getActivity().getSharedPreferences("key", getActivity().MODE_APPEND).edit().putString("key", "0").commit();
                        startActivity(new Intent(getActivity(), A0_LoginActivity.class));
                    } else {
                        //Toast.makeText(getActivity(), userinformation.status.msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    /**
     * 请求用户信息
     */
    private void load_User_information() {
        Dialog.ProgressDialog();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Get_member_base_info, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                /**
                 * 存入数据到本地
                 *//*
                SharedPreferences sf = getActivity().getSharedPreferences("wjhgw_auction", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor edit = sf.edit();
                edit.putString("userinformation_datas_member_avatar", responseInfo.result).commit();*/
                Dialog.dismiss();
                /**
                 * 解析用户信息
                 */
                Analytical(responseInfo.result);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                /**
                 * 取出本地緩存数据
                 *//*
                SharedPreferences preferences = getActivity().getSharedPreferences("wjhgw_auction", getActivity().MODE_PRIVATE);
                String auctionSuperValueData = preferences.getString("userinformation_datas_member_avatar", "");

                Analytical(auctionSuperValueData);*/
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
                available_predeposit.setText(userinformation.datas.available_predeposit);   //余额
                tv_rechargeable_card.setText(userinformation.datas.available_rc_balance);   //可用充值卡余额
                tv_hgb.setText(userinformation.datas.hg_points);              //欢购币
                tv_vouchers.setText(userinformation.datas.member_voucher);         //代金券

                memberName = userinformation.datas.member_name;
            } else if (userinformation.status.code == 200103 || userinformation.status.code == 200104) {
                Toast.makeText(getActivity(), "登录超时或未登录", Toast.LENGTH_SHORT).show();
                getActivity().getSharedPreferences("key", getActivity().MODE_APPEND).edit().putString("key", "0").commit();
                startActivity(new Intent(getActivity(), A0_LoginActivity.class));
            } else {
                //Toast.makeText(getActivity(), userinformation.status.msg, Toast.LENGTH_SHORT).show();
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
        if (key.equals("0")) {
            myAvatar.setImageResource(R.mipmap.ic_default_avatar);
            member_nickname.setText("点击登录");
            tv_statistics1.setVisibility(View.GONE);
            tv_statistics2.setVisibility(View.GONE);
            tv_statistics5.setVisibility(View.GONE);
            tv_statistics3.setVisibility(View.GONE);
            tv_statistics4.setVisibility(View.GONE);
            available_predeposit.setText("0");   //余额
            tv_rechargeable_card.setText("0");   //可用充值卡余额
            tv_hgb.setText("0");              //欢购币
            tv_vouchers.setText("0");         //代金券
        } else {
            load_User_information();
            order_amount();
        }
    }

}
