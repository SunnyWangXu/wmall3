package com.wjhgw.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.bean.CreateBag;
import com.wjhgw.business.bean.Detail_Gift_List;
import com.wjhgw.business.bean.Detail_Gift_List_datas;
import com.wjhgw.business.bean.MyLockBox;
import com.wjhgw.config.ApiInterface;

/**
 * 商品详情赠送多人Activity
 */
public class J7_DetailGiveMoreActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvDetailGiveTotal;
    private TextView tvConfirmGiveone;
    private EditText edGiftOneNote;
    IWXAPI api;
    StringBuffer sb;
    int s = 0;
    private String edNote;
    private String useName;
    private ImageView ivDetailImg;
    private TextView tvGoodsName;
    private TextView tvGoodsPrice;
    private TextView tvGoodsNum;
    private String paySn;
    private String goods_str;
    private EditText edGiftPaw;
    private EditText edGiftMoreNote;
    private TextView tvConfirmGiveMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_give_more);

        api = WXAPIFactory.createWXAPI(this, "wx99a6bd9b7bdbf645");
        api.registerApp("wx99a6bd9b7bdbf645");
        sb = new StringBuffer();

        paySn = getIntent().getStringExtra("paySn");
        /**
         * 使用paySn查询赠送他人的商品列表
         */
        loadPaysnGoods();

    }

    /**
     * 使用Paysn查询赠送他人的商品列表
     */
    private void loadPaysnGoods() {

        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("pay_sn", paySn);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Gift_goods_list, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                Detail_Gift_List detailGiftList = gson.fromJson(responseInfo.result, Detail_Gift_List.class);

                if (detailGiftList.status.code == 10000) {
                    Detail_Gift_List_datas detailGiftListDatas = detailGiftList.datas.get(0);

                    APP.getApp().getImageLoader().displayImage(detailGiftListDatas.goods_image, ivDetailImg);
                    tvGoodsName.setText(detailGiftListDatas.goods_name);
                    tvGoodsPrice.setText(detailGiftListDatas.goods_price);
                    tvGoodsNum.setText("x" + detailGiftListDatas.goods_num);
                    tvDetailGiveTotal.setText(detailGiftListDatas.goods_num + "件");

                    goods_str = detailGiftListDatas.goods_id + "|" + detailGiftListDatas.goods_num;
                } else {
                    overtime(detailGiftList.status.code, detailGiftList.status.msg);
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }


    @Override
    public void onInit() {
        setUp();
        setTitle("赠送单人");

        /**
         * 获取用户昵称或者用户名
         */
        loadUseName();

    }

    /**
     * 获取用户昵称或者用户名
     */
    private void loadUseName() {

        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.MyLockBox, params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {


                        Gson gson = new Gson();
                        if (null != responseInfo) {
                            MyLockBox myLockBox = gson.fromJson(responseInfo.result, MyLockBox.class);
                            if (myLockBox.status.code == 10000) {
                                if (myLockBox.datas.member_nickname.equals("")) {
                                    useName = myLockBox.datas.member_name;
                                } else {

                                    useName = myLockBox.datas.member_nickname;
                                }
                            } else {
                                overtime(myLockBox.status.code, myLockBox.status.msg);
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                    }
                }

        );

    }

    @Override
    public void onFindViews() {

        ivDetailImg = (ImageView) findViewById(R.id.iv_detail_givem_image);
        edGiftPaw = (EditText) findViewById(R.id.ed_detail_gift_paw);
        tvGoodsName = (TextView) findViewById(R.id.tv_detail_givem_goodsname);
        tvGoodsPrice = (TextView) findViewById(R.id.tv_detail_givem_goodsprice);
        tvGoodsNum = (TextView) findViewById(R.id.tv_detail_givem_goodsnum);
        tvDetailGiveTotal = (TextView) findViewById(R.id.tv_detail_givem_total);
        tvConfirmGiveMore = (TextView) findViewById(R.id.tv_detail_confirm_givemore);
        edGiftMoreNote = (EditText) findViewById(R.id.ed_giftmore_note);


    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        tvConfirmGiveMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {

            case R.id.tv_detail_confirm_givemore:

                /**
                 * 创建单人礼包
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
        StartLoading();
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
        if (!edGiftPaw.getText().toString().equals("")) {
            params.addBodyParameter("extract_pwd", edGiftPaw.getText().toString());
        }

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Create_gift, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                Dismiss();
                CreateBag createBag = gson.fromJson(responseInfo.result, CreateBag.class);
                if (createBag.status.code == 10000 && createBag.datas.state) {
                    String iamgeUrl = createBag.datas.data.gift_ico;
                    String giftUrl = createBag.datas.data.gift_link;
                    /**
                     * 微信单人分享礼包
                     */
                    wechatShare(s, giftUrl);

                } else {
                    overtime(createBag.status.code, createBag.status.msg);
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
        msg.title = useName + "的大礼包";
        if (edNote.equals("")) {
            msg.description = "一点心意，希望您喜欢！";
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
        finish();
    }

}
