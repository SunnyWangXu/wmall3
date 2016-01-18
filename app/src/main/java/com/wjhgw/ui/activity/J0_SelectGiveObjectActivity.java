package com.wjhgw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;

/**
 * 选择赠送对象Activity
 */
public class J0_SelectGiveObjectActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivZhangBei;
    private ImageView ivDear;
    private ImageView ivTongShi;
    private ImageView ivFriend;
    public String[] goods_id = null;
    public int[] buy_number = null;
    public String cadList_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_give);
        goods_id = getIntent().getStringArrayExtra("goods_id");
        cadList_data = getIntent().getStringExtra("cadList_data");
        buy_number = getIntent().getExtras().getIntArray("buy_number");
    }

    @Override
    public void onInit() {

        setUp();
        setTitle("选择赠送对象");
    }

    @Override
    public void onFindViews() {

        ivZhangBei = (ImageView) findViewById(R.id.iv_zhangbei);
        ivDear = (ImageView) findViewById(R.id.iv_dear);
        ivTongShi = (ImageView) findViewById(R.id.iv_tongshi);
        ivFriend = (ImageView) findViewById(R.id.iv_friend);

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {

        ivZhangBei.setOnClickListener(this);
        ivDear.setOnClickListener(this);
        ivTongShi.setOnClickListener(this);
        ivFriend.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent();
        
        switch (v.getId()) {
            case R.id.iv_zhangbei:
                intent.setClass(this, J2_GiveOneActivity.class);
                startActivity(intent);

                break;

            case R.id.iv_dear:

                break;

            case R.id.iv_tongshi:

                break;

            case R.id.iv_friend:

                break;

            default:
                break;
        }

    }
}
