package com.wjhgw.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.wjhgw.business.bean.Nickname;
import com.wjhgw.config.ApiInterface;

/**
 * 修改昵称Activity
 */
public class M1_Modify_nicknameActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_modifynickname;
    private ImageView iv_delete;
    private TextView tv_next;
    private String modifynickname;
    private Nickname nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m1_modify_nickname);
        et_modifynickname = (EditText) findViewById(R.id.et_modifynickname);

        et_modifynickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start != 0) {
                    iv_delete.setVisibility(View.VISIBLE);
                } else {
                    if (start == 0 && count > 0) {
                        iv_delete.setVisibility(View.VISIBLE);
                    } else {
                        iv_delete.setVisibility(View.GONE);
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
    }

    @Override
    public void onInit() {
        //finish
        setUp();
        setTitle("修改昵称");
    }

    @Override
    public void onFindViews() {

    }

    @Override
    public void onInitViewData() {
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        tv_next = (TextView) findViewById(R.id.tv_next);
    }

    @Override
    public void onBindListener() {
        iv_delete.setOnClickListener(this);
        tv_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_delete:
                et_modifynickname.setText("");
                iv_delete.setVisibility(View.GONE);
                break;
            case R.id.tv_next:
                modifynickname = et_modifynickname.getText().toString();
                if (!modifynickname.equals("")) {
                    User_information();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 用户信息
     */
    private void User_information() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", this.getSharedPreferences("key", MODE_APPEND).getString("key", "0"));
        params.addBodyParameter("member_nickname", modifynickname);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Change_nickname, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo.result != null) {
                    nickname = gson.fromJson(responseInfo.result, Nickname.class);

                    if (nickname.status.code == 10000) {
                        finish(false);
                    } else {
                        showToastShort(nickname.status.msg);
                    }
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
            }
        });
    }
}
