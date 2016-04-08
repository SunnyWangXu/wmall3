package com.wjhgw.ui.activity;

import android.content.Intent;
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
 * 赠送单人Activity
 */
public class J2_GiveOneActivity extends BaseActivity implements View.OnClickListener {

    private ListView lvGiveOne;
    private LinearLayout llGiveOneHeader;
    private String fromWho;
    private LinearLayout llGiveOneMessage;
    private List<CadList_data> datas;
    private List<CadList_data> datas1 = new ArrayList<CadList_data>();
    private TextView tvGiveTotal;
    private CabGiveLvAdapter cabGiveLvAdapter;
    private int totalNum;
    private LinearLayout llGiveoneAddress;
    private String giveUseName;
    private String giveUsePhone;
    private String giveUseAddressInfo;
    private TextView tvGiveName;
    private TextView tvGivePhone;
    private TextView tvGiveAddress;
    private TextView tvConfirmGiveone;
    private EditText edGiftOneNote;
    IWXAPI api;
    StringBuffer sb;
    int s = 0;
    private String edNote;
    private LinearLayout llConfirmGiveOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_one);

        api = WXAPIFactory.createWXAPI(this, "wx99a6bd9b7bdbf645");
        api.registerApp("wx99a6bd9b7bdbf645");
        sb = new StringBuffer();

        cabGiveLvAdapter = new CabGiveLvAdapter(this, datas1);
        lvGiveOne.setAdapter(cabGiveLvAdapter);

     /*   InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();

        if (isOpen) {
            llConfirmGiveOne.setVisibility(View.GONE);
        } else {
            llConfirmGiveOne.setVisibility(View.VISIBLE);
        }*/
    }


    @Override
    public void onInit() {
        setUp();
        setTitle("赠送单人");
        lvGiveOne = (ListView) findViewById(R.id.lv_give_one);

        llGiveOneHeader = (LinearLayout) getLayoutInflater().inflate(R.layout.give_one_header, null);

        lvGiveOne.addHeaderView(llGiveOneHeader);

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
        tvGiveTotal = (TextView) findViewById(R.id.tv_give_total);
        tvConfirmGiveone = (TextView) findViewById(R.id.tv_confirm_giveone);
        edGiftOneNote = (EditText) findViewById(R.id.ed_giftone_note);
        llConfirmGiveOne = (LinearLayout) findViewById(R.id.ll_confirm_give_one);
       /* llGiveOneMessage = (LinearLayout) llGiveOneHeader.findViewById(R.id.ll_giveone_use_message);
        llGiveoneAddress = (LinearLayout) llGiveOneHeader.findViewById(R.id.ll_giveone_address);
        tvGiveName = (TextView) llGiveOneHeader.findViewById(R.id.tv_giveone_usename);
        tvGivePhone = (TextView) llGiveOneHeader.findViewById(R.id.tv_giveone_usephone);
        tvGiveAddress = (TextView) llGiveOneHeader.findViewById(R.id.tv_giveone_useaddressinfo);*/


    }

    @Override
    public void onInitViewData() {
        tvGiveTotal.setText(totalNum + "件");

    }

    @Override
    public void onBindListener() {
       /* llGiveoneAddress.setOnClickListener(this);
        llGiveOneMessage.setOnClickListener(this);*/
        tvConfirmGiveone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
          /*  case R.id.ll_giveone_address:

                intent.setClass(this, S1_OrderAddressActivity.class);
                startActivityForResult(intent, 55555);

                break;
            case R.id.ll_giveone_use_message:

                intent.setClass(this, S1_OrderAddressActivity.class);
                startActivityForResult(intent, 55555);

                break;*/

            case R.id.tv_confirm_giveone:
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
        params.addBodyParameter("limit_type", "0");
        edNote = edGiftOneNote.getText().toString();
        if (edNote.equals("")) {
            params.addBodyParameter("gift_note", "一点心意，希望您喜欢！");
        } else {
            params.addBodyParameter("gift_note", edNote);
        }

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Create_gift, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                CreateBag createBag = gson.fromJson(responseInfo.result, CreateBag.class);
                if (createBag.status.code == 10000 && createBag.datas.state) {

                    String iamgeUrl = createBag.datas.data.gift_ico;
                    String giftUrl = createBag.datas.data.gift_link;
                    /**
                     * 微信单人分享礼包
                     */
                    wechatShare(s, giftUrl);
                    finish();
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
    }

   /* private Bitmap loadBitMap(String iamgeUrl) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(iamgeUrl);
        try {
            HttpResponse  httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = httpResponse.getEntity();
                InputStream in = in = entity.getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(in);

                return bitmap;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
*/

    /**
     * activity 返回的数据 forResult
     */
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            llGiveOneMessage.setVisibility(View.GONE);
            llGiveoneAddress.setVisibility(View.VISIBLE);
        }
        if (data != null) {
            llGiveoneAddress.setVisibility(View.GONE);
            llGiveOneMessage.setVisibility(View.VISIBLE);

            giveUseName = data.getStringExtra("tureName");
            giveUsePhone = data.getStringExtra("phone");
            giveUseAddressInfo = data.getStringExtra("addressInfo");
            if (giveUseName != null && giveUsePhone != null && giveUseAddressInfo != null) {
                tvGiveName.setText(giveUseName);
                tvGivePhone.setText(giveUsePhone);
                tvGiveAddress.setText(giveUseAddressInfo);
            }

        }

    }*/
}
