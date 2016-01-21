package com.wjhgw.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.bean.CadList_data;
import com.wjhgw.business.bean.CreateBag;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.view.listview.adapter.CabGiveLvAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 赠送多人的Activity
 */
public class J3_GiveMoreActivity extends BaseActivity implements View.OnClickListener {

    private ListView lvGiveMore;
    private LinearLayout llGiveMoreHeader;
    private List<CadList_data> datas;
    private List<CadList_data> datas1 = new ArrayList<CadList_data>();
    private TextView tvGiveMoreTotal;
    private int totalNum;
    private TextView tvConfirmGivemore;
    IWXAPI api;
    StringBuffer sb;
    private EditText edGiftMoreNote;
    private String edNote;
    private EditText edGiftPaw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_more);

        api = WXAPIFactory.createWXAPI(this, "wx99a6bd9b7bdbf645");
        api.registerApp("wx99a6bd9b7bdbf645");
        sb = new StringBuffer();

        lvGiveMore.setAdapter(new CabGiveLvAdapter(this, datas1));

    }

    @Override
    public void onInit() {
        setUp();
        setTitle("赠送多人");

        lvGiveMore = (ListView) findViewById(R.id.lv_give_more);
        llGiveMoreHeader = (LinearLayout) getLayoutInflater().inflate(R.layout.give_more_header, null);
        lvGiveMore.addHeaderView(llGiveMoreHeader);

        String jsonStr = getIntent().getStringExtra("jsonStr");
        /**
         * 解析筛选选中赠送的数据
         */
        parseGiveData(jsonStr);

    }

    /**
     * 解析选中赠送的数据
     */
    private void parseGiveData(String jsonStr) {
        Type type = new TypeToken<ArrayList<CadList_data>>() {
        }.getType();
        datas = new Gson().fromJson(jsonStr, type);

        for (int i = 0; i < datas.size(); i++) {

            if (!datas.get(i).selected.equals("0")) {
                datas1.add(datas.get(i));
                totalNum += datas.get(i).num;

            }
        }


    }

    @Override
    public void onFindViews() {
        edGiftPaw = (EditText) llGiveMoreHeader.findViewById(R.id.ed_gift_paw);
        edGiftMoreNote = (EditText) llGiveMoreHeader.findViewById(R.id.ed_giftmore_note);
        tvGiveMoreTotal = (TextView) findViewById(R.id.tv_give_more_total);
        tvConfirmGivemore = (TextView) findViewById(R.id.tv_confirm_givemore);


    }

    @Override
    public void onInitViewData() {
        tvGiveMoreTotal.setText(totalNum + "件");

    }

    @Override
    public void onBindListener() {

        tvConfirmGivemore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm_givemore:

                StringBuffer cart_id = new StringBuffer();

                for (int i = 0; i < datas1.size(); i++) {
                    cart_id.append(datas1.get(i).goods_id + "|" + datas1.get(i).num + ",");
                }

                String goods_str = cart_id.toString().substring(0, cart_id.toString().length() - 1);
                /**
                 * 创建礼包
                 */
                createGiftBag(goods_str);

                break;

            default:
                break;
        }
    }


    /**
     * 创建礼包
     */
    private void createGiftBag(String goods_str) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        if (!goods_str.equals("")) {
            params.addBodyParameter("goods_str", goods_str);
        }
        params.addBodyParameter("limit_type", "1");
        edNote = edGiftMoreNote.getText().toString();
        if (edNote.equals("")) {
            params.addBodyParameter("gift_note", "上等的好酒送给你");
        } else {
            params.addBodyParameter("gift_note", edNote);
        }
        params.addBodyParameter("extract_pwd", edGiftPaw.getText().toString());

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Create_gift, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                CreateBag createBag = gson.fromJson(responseInfo.result, CreateBag.class);
                if (createBag.status.code == 10000 && createBag.datas.state) {

                    String giftUrl = createBag.datas.data.gift_link;
                    /**
                     * 微信多人分享礼包
                     */
                    wechatShare(1, giftUrl);
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }

    /**
     * 微信分享礼包
     */
    private void wechatShare(int flag, String giftUrl) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = giftUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "老王的大礼包";
        if (edNote.equals("")) {
            msg.description = "上等的好酒送给你";
        } else {
            msg.description = edNote;
        }
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_gift_logo);

        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }
}
