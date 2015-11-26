package com.wjhgw.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.utils.FileUtils;

import java.io.File;

/**
 * 设置的Activity
 */
public class M6_SetActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivShare;
    private ImageView ivPush;
    private int PushCount = 1;
    private Button btnExit;
    private String memberName;
    private LinearLayout llClearCache;
    private String cachePath;
    private TextView tvCache;
    private LinearLayout llCheckVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("设置");
        memberName = getIntent().getStringExtra("memberName");
       //获取缓存的路径
        cachePath = APP.getApp().getAppCache();

    }

    @Override
    public void onFindViews() {
        ivShare = (ImageView) findViewById(R.id.iv_title_right);
        ivPush = (ImageView) findViewById(R.id.iv_push);
        btnExit = (Button) findViewById(R.id.btn_exit);
        tvCache = (TextView) findViewById(R.id.tv_cache);
        llClearCache = (LinearLayout) findViewById(R.id.ll_clear_cache);
        llCheckVersion = (LinearLayout) findViewById(R.id.ll_check_version);
    }

    @Override
    public void onInitViewData() {
        ivShare.setImageResource(R.mipmap.ic_share);
        ivShare.setVisibility(View.VISIBLE);

        if(!getKey().equals("0")){
        btnExit.setVisibility(View.VISIBLE);
        }

        Long dirSize = FileUtils.getDirSize(new File(cachePath));
        String cacheSize = FileUtils.getFileSize(dirSize);

        tvCache.setText(cacheSize);

    }

    @Override
    public void onBindListener() {
        ivPush.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        llClearCache.setOnClickListener(this);
        llCheckVersion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_push:
                PushCount++;
                if (PushCount % 2 == 1) {
                    ivPush.setImageResource(R.mipmap.ic_push_off);
                } else if (PushCount % 2 == 0) {
                    ivPush.setImageResource(R.mipmap.ic_push_on);
                }
                break;

            case R.id.ll_clear_cache:
                /**
                 * 删除缓存的文件
                 */
                FileUtils.clearAppCache(cachePath);
                tvCache.setText("0KB");
                showToastShort("清除缓存成功");
                break;

            case R.id.btn_exit:
                /**
                 * 退出登录
                 */
                exitLogin();

                break;
            case R.id.ll_check_version:

                showToastShort("当前已经是最新版本");
                break;

            default:
                break;
        }
    }

    /**
     * 退出登录
     */
    private void exitLogin() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("member_name", memberName);
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("client", "android");

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Exit_login, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                showToastShort("已退出登录");
                SharedPreferences preferences = getSharedPreferences("key", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                //存入数据
                editor.putString("key", "0");
                editor.commit();
                finish();
            }

            @Override
            public void onFailure(HttpException e, String s) {

                showToastShort("网络错误");
            }
        });
    }

}
