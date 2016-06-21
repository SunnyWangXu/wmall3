package com.wjhgw;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.update.UmengUpdateAgent;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.bean.MainMessageNum;
import com.wjhgw.business.bean.Status;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.activity.A0_LoginActivity;
import com.wjhgw.ui.fragment.FindFragment;
import com.wjhgw.ui.fragment.CategoryFragment;
import com.wjhgw.ui.fragment.HomeFragment;
import com.wjhgw.ui.fragment.MyFragment;
import com.wjhgw.ui.fragment.ShoppingCartFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ShoppingCartFragment shoppingCartFragment;  //购物车
    private CategoryFragment categoryFragment; //分类
    private HomeFragment homeFragment;    //首页
    private FindFragment findFragment;    //发现
    private MyFragment mineFragment;     //我的
    private View index_Layout;
    private View contacts_Layout;
    private View shopping_car_Layout;
    private View setting_Layout;
    private View classification_layout;
    private ImageView indexImage;
    private ImageView classification_image;
    private ImageView ivCabinet;
    private ImageView shopping_car_image;
    private ImageView settingImage;
    private TextView indexText;
    private TextView classification_text;
    private TextView contactsText;
    private TextView shopping_car_text;
    private TextView settingText;
    private FragmentManager fragmentManager;
    private String key;
    private TextView tvCabNum;
    private TextView tvCartNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }

        UmengUpdateAgent.update(this);
        setContentView(R.layout.activity_main);
        key = getKey();
        if (!key.equals("0")) {
            check_logo_data();
        }

        fragmentManager = getSupportFragmentManager();
        setTabSelection(0);
    }

    @Override
    public void onInit() {
        index_Layout = findViewById(R.id.index_layout);
        classification_layout = findViewById(R.id.classification_layout);
        contacts_Layout = findViewById(R.id.contacts_layout);
        shopping_car_Layout = findViewById(R.id.shopping_car_layout);
        setting_Layout = findViewById(R.id.setting_layout);
    }

    @Override
    public void onFindViews() {
        indexImage = (ImageView) findViewById(R.id.index_image);
        classification_image = (ImageView) findViewById(R.id.classification_image);
        ivCabinet = (ImageView) findViewById(R.id.iv_cabinet);
        shopping_car_image = (ImageView) findViewById(R.id.shopping_car_image);
        settingImage = (ImageView) findViewById(R.id.setting_image);
        indexText = (TextView) findViewById(R.id.index_text);
        classification_text = (TextView) findViewById(R.id.classification_text);
        contactsText = (TextView) findViewById(R.id.contacts_text);
        shopping_car_text = (TextView) findViewById(R.id.shopping_car_text);
        settingText = (TextView) findViewById(R.id.setting_text);
        tvCabNum = (TextView) findViewById(R.id.tv_cab_num);
        tvCartNum = (TextView) findViewById(R.id.tv_cart_num);

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        index_Layout.setOnClickListener(this);
        classification_layout.setOnClickListener(this);
        contacts_Layout.setOnClickListener(this);
        shopping_car_Layout.setOnClickListener(this);
        setting_Layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.index_layout:
                setTabSelection(0);
                break;
            case R.id.classification_layout:
                setTabSelection(1);
                break;
            case R.id.contacts_layout:
                setTabSelection(2);
                break;
            case R.id.shopping_car_layout:
                setTabSelection(3);
                break;
            case R.id.setting_layout:
                setTabSelection(4);
                break;
            default:
                break;
        }

    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0首页，  1分类  2发现 3购物车 4我的
     */
    public void setTabSelection(int index) {
        if (!getKey().equals("0")) {
            loadMessageNumber();
        }else {
            tvCartNum.setVisibility(View.GONE);
            tvCabNum.setVisibility(View.GONE);
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // hideFragments(transaction);
        Intent intent = new Intent();
        switch (index) {
            case 0:
                clearSelection();
                indexImage.setImageResource(R.mipmap.ic_homepage_select);
                indexText.setTextColor(Color.parseColor("#d63235"));
                homeFragment = new HomeFragment();
                transaction.replace(R.id.content, homeFragment);
                /*if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.replace(R.id.content, homeFragment);
                } else {
                    //transaction.replace(R.id.content, homeFragment);
                    transaction.add(R.id.content,homeFragment);
                }*/
                break;
            case 1:
                clearSelection();
                classification_image.setImageResource(R.mipmap.ic_search_select);
                classification_text.setTextColor(Color.parseColor("#d63235"));
                categoryFragment = new CategoryFragment();
                transaction.replace(R.id.content, categoryFragment);

                break;
            case 2:
                clearSelection();
                ivCabinet.setImageResource(R.mipmap.ic_find_select);
                contactsText.setTextColor(Color.parseColor("#d63235"));
                findFragment = new FindFragment();
                transaction.replace(R.id.content, findFragment);
                break;

            case 3:
                if (getKey().equals("0")) {
                    tvCabNum.setVisibility(View.GONE);
                    tvCartNum.setVisibility(View.GONE);
                    intent.setClass(this, A0_LoginActivity.class);
                    startActivity(intent);
                } else {
                    clearSelection();
                    shopping_car_image.setImageResource(R.mipmap.ic_shopping_cart_select);
                    shopping_car_text.setTextColor(Color.parseColor("#d63235"));
                    shoppingCartFragment = new ShoppingCartFragment();
                    transaction.replace(R.id.content, shoppingCartFragment);
                }
                /*if (shoppingCartFragment == null) {
                    shoppingCartFragment = new ShoppingCartFragment();
                    transaction.replace(R.id.content, shoppingCartFragment);
                } else {
                    //transaction.replace(R.id.content, shoppingCartFragment);
                    transaction.add(R.id.content,shoppingCartFragment);
                }*/
                break;
            case 4:
            default:
                clearSelection();
                settingImage.setImageResource(R.mipmap.ic_mine_select);
                settingText.setTextColor(Color.parseColor("#d63235"));
                mineFragment = new MyFragment();
                transaction.replace(R.id.content, mineFragment);
                /*if (mineFragment == null) {
                    mineFragment = new MyFragment();
                    transaction.replace(R.id.content, mineFragment);
                } else {
                    //transaction.replace(R.id.content, mineFragment);
                    transaction.add(R.id.content,mineFragment);
                }*/

                break;
        }

        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        indexImage.setImageResource(R.mipmap.ic_homepage);
        indexText.setTextColor(Color.parseColor("#ffffff"));
        classification_image.setImageResource(R.mipmap.ic_search2);
        classification_text.setTextColor(Color.parseColor("#ffffff"));
        ivCabinet.setImageResource(R.mipmap.ic_find);
        contactsText.setTextColor(Color.parseColor("#ffffff"));
        shopping_car_image.setImageResource(R.mipmap.ic_shopping_cart);
        shopping_car_text.setTextColor(Color.parseColor("#ffffff"));
        settingImage.setImageResource(R.mipmap.ic_mine);
        settingText.setTextColor(Color.parseColor("#ffffff"));
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
   /* private void hideFragments(FragmentTransaction transaction) {
        if (shoppingCartFragment != null) {
            transaction.remove(shoppingCartFragment);
        }
        if (categoryFragment != null) {
            transaction.remove(categoryFragment);
        }
        if (homeFragment != null) {
            transaction.remove(homeFragment);
        }
        if (mineFragment != null) {
            transaction.remove(mineFragment);
        }
    }
*/

    /**
     * 判断是否登录过期
     */
    private void check_logo_data() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Check_logo_data, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo.result != null) {

                    Status status = gson.fromJson(responseInfo.result, Status.class);
                    if (status.status.code == 10000) {

                    } else {
                        exitLogin();
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
     * 删除本地key
     */
    private void exitLogin() {
        SharedPreferences preferences = getSharedPreferences("key", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        //存入数据
        editor.putString("key", "0");
        editor.commit();
    }

    /**
     * 双击返回键退出App
     */
    private long firstClickTime = 0;

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstClickTime > 2000) {
            showToastShort("再按一次退出程序...");
            firstClickTime = secondTime;
            return;
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 酒柜和购物车的信息条数
     */
    private void loadMessageNumber() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Main_message_num, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo.result != null) {
                    MainMessageNum mainMessageNum = gson.fromJson(responseInfo.result, MainMessageNum.class);
                    if (mainMessageNum.datas != null) {
                        if (mainMessageNum.status.code == 10000) {/*
                            if (mainMessageNum.datas.cab_num <= 9 && mainMessageNum.datas.cab_num != 0 ) {
                                tvCabNum.setVisibility(View.VISIBLE);
                                tvCabNum.setText(mainMessageNum.datas.cab_num + "");
                            } else if(mainMessageNum.datas.cab_num > 9){
                                tvCabNum.setVisibility(View.VISIBLE);
                                tvCabNum.setText("9+");
                            }*/
                            if (mainMessageNum.datas.cart_num <= 9 && mainMessageNum.datas.cart_num != 0) {
                                tvCartNum.setVisibility(View.VISIBLE);
                                tvCartNum.setText(mainMessageNum.datas.cart_num + "");
                            } else if(mainMessageNum.datas.cart_num > 9){
                                tvCartNum.setVisibility(View.VISIBLE);
                                tvCartNum.setText("9+");
                            }else if(mainMessageNum.datas.cart_num == 0){
                                tvCartNum.setVisibility(View.GONE);
                            }
                        } else if (mainMessageNum.status.code == 200103 || mainMessageNum.status.code == 200104) {
                            showToastShort("登录超时或未登录");
                            getSharedPreferences("key", MODE_APPEND).edit().putString("key", "0").commit();
                        } else {
                            showToastShort(mainMessageNum.status.msg);
                        }
                    }
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });


    }
}
