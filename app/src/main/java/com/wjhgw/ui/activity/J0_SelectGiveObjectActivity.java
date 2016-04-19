package com.wjhgw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
    private ImageView ivGiveMore;
    private String jsonStr;
    private LinearLayout llGiveOne;
    private LinearLayout llGiveMore;
    private String paySn;
    private String entrance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_give);

        jsonStr = getIntent().getStringExtra("list");
        paySn = getIntent().getStringExtra("paySn");
        entrance = getIntent().getStringExtra("entrance");


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
        ivGiveMore = (ImageView) findViewById(R.id.iv_give_more_people);

        llGiveOne = (LinearLayout) findViewById(R.id.ll_give_one);
        llGiveMore = (LinearLayout) findViewById(R.id.ll_give_more);

    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {

      /*  ivZhangBei.setOnClickListener(this);
        ivDear.setOnClickListener(this);
        ivTongShi.setOnClickListener(this);
        ivFriend.setOnClickListener(this);
        ivGiveMore.setOnClickListener(this);*/

        llGiveOne.setOnClickListener(this);
        llGiveMore.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent();

        switch (v.getId()) {
           /* case R.id.iv_zhangbei:
                intent.setClass(this, J2_GiveOneActivity.class);
                intent.putExtra("zhangbei", "zhangbei");
                intent.putExtra("jsonStr",jsonStr);
                startActivity(intent);

                break;

            case R.id.iv_dear:

                break;

            case R.id.iv_tongshi:

                break;

            case R.id.iv_friend:

                break;*/

            case R.id.ll_give_one:

                if (entrance.equals("4")) {
                    //从详情页过来赠送的
                    intent.setClass(this, J6_DetailGiveOneActivity.class);
                    intent.putExtra("paySn", paySn);
                    startActivity(intent);
                    finish();
                } else {
                    //从酒柜过来赠送的
                    intent.setClass(this, J2_GiveOneActivity.class);
                    intent.putExtra("jsonStr", jsonStr);
                    startActivity(intent);
                    finish();
                }
                break;

            case R.id.ll_give_more:
                if (entrance.equals("4")) {
                    //从详情页过来赠送的
                    intent.setClass(this, J7_DetailGiveMoreActivity.class);
                    intent.putExtra("paySn", paySn);
                    startActivity(intent);
                    finish();
                } else {
                    //从酒柜过来赠送的
                    intent.setClass(this, J3_GiveMoreActivity.class);
                    intent.putExtra("jsonStr", jsonStr);
                    startActivity(intent);
                    finish();
                }
                break;

            default:
                break;
        }

    }
}
