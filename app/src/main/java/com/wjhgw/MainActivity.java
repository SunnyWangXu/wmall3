package com.wjhgw;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjhgw.base.BaseActivity;
import com.wjhgw.ui.fragment.CategoryFragment;
import com.wjhgw.ui.fragment.DiscoveryFragment;
import com.wjhgw.ui.fragment.IndexFragment;
import com.wjhgw.ui.fragment.MineFragment;
import com.wjhgw.ui.fragment.ShoppingCartFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ShoppingCartFragment shoppingCartFragment;  //购物车
    private CategoryFragment categoryFragment; //分类
    private IndexFragment indexFragment;    //首页
    private DiscoveryFragment discoveryFragment;    //发现
    private MineFragment mineFragment;     //我的
    private View index_Layout;
    private View contacts_Layout;
    private View shopping_car_Layout;
    private View setting_Layout;
    private View classification_layout;
    private ImageView indexImage;
    private ImageView classification_image;
    private ImageView contactsImage;
    private ImageView shopping_car_image;
    private ImageView settingImage;
    private TextView indexText;
    private TextView classification_text;
    private TextView contactsText;
    private TextView shopping_car_text;
    private TextView settingText;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        fragmentManager = getSupportFragmentManager();
        setTabSelection(0);
    }

    /**
     * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
     */
    private void initViews() {
        index_Layout = findViewById(R.id.index_layout);
        classification_layout = findViewById(R.id.classification_layout);
        contacts_Layout = findViewById(R.id.contacts_layout);
        shopping_car_Layout = findViewById(R.id.shopping_car_layout);
        setting_Layout = findViewById(R.id.setting_layout);
        indexImage = (ImageView) findViewById(R.id.index_image);
        classification_image = (ImageView) findViewById(R.id.classification_image);
        contactsImage = (ImageView) findViewById(R.id.contacts_image);
        shopping_car_image = (ImageView) findViewById(R.id.shopping_car_image);
        settingImage = (ImageView) findViewById(R.id.setting_image);
        indexText = (TextView) findViewById(R.id.index_text);
        classification_text = (TextView) findViewById(R.id.classification_text);
        contactsText = (TextView) findViewById(R.id.contacts_text);
        shopping_car_text = (TextView) findViewById(R.id.shopping_car_text);
        settingText = (TextView) findViewById(R.id.setting_text);
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
     * @param index 每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
     */
    private void setTabSelection(int index) {
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                indexImage.setImageResource(R.mipmap.ic_homepage_select);
                indexText.setTextColor(Color.parseColor("#ba0e2f"));
                if (indexFragment == null) {
                    indexFragment = new IndexFragment();
                    transaction.add(R.id.content, indexFragment);
                } else {
                    transaction.show(indexFragment);
                }

                break;
            case 1:
                classification_image.setImageResource(R.mipmap.ic_search_select);
                classification_text.setTextColor(Color.parseColor("#ba0e2f"));
                if (categoryFragment == null) {
                    categoryFragment = new CategoryFragment();
                    transaction.add(R.id.content, categoryFragment);
                } else {
                    transaction.show(categoryFragment);
                }

                break;
            case 2:
                contactsImage.setImageResource(R.mipmap.ic_find_select);
                contactsText.setTextColor(Color.parseColor("#ba0e2f"));
                if (discoveryFragment == null) {
                    discoveryFragment = new DiscoveryFragment();
                    transaction.add(R.id.content, discoveryFragment);
                } else {
                    transaction.show(discoveryFragment);
                }
                break;
            case 3:
                shopping_car_image.setImageResource(R.mipmap.ic_shopping_cart_select);
                shopping_car_text.setTextColor(Color.parseColor("#ba0e2f"));
                if (shoppingCartFragment == null) {
                    shoppingCartFragment = new ShoppingCartFragment();
                    transaction.add(R.id.content, shoppingCartFragment);
                } else {
                    transaction.show(shoppingCartFragment);
                }
                break;
            case 4:
            default:
                settingImage.setImageResource(R.mipmap.ic_mine_select);
                settingText.setTextColor(Color.parseColor("#ba0e2f"));
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    transaction.add(R.id.content, mineFragment);
                } else {
                    transaction.show(mineFragment);
                }
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
        classification_image.setImageResource(R.mipmap.ic_search);
        classification_text.setTextColor(Color.parseColor("#ffffff"));
        contactsImage.setImageResource(R.mipmap.ic_find);
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
    private void hideFragments(FragmentTransaction transaction) {
        if (shoppingCartFragment != null) {
            transaction.hide(shoppingCartFragment);
        }
        if (categoryFragment != null) {
            transaction.hide(categoryFragment);
        }
        if (indexFragment != null) {
            transaction.hide(indexFragment);
        }
        if (discoveryFragment != null) {
            transaction.hide(discoveryFragment);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
    }

}
