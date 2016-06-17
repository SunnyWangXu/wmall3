package com.wjhgw.wxapi;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wjhgw.APP;
import com.wjhgw.R;

import org.json.JSONException;
import org.json.JSONObject;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;
    private String wxapi = "wx99a6bd9b7bdbf645";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, wxapi, false);
        api.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {

        if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {    //微信分享回调
            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                Finish();
                Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
            } else {
                Finish();
                Toast.makeText(this, "用户取消", Toast.LENGTH_SHORT).show();
            }
        } else if (resp instanceof SendAuth.Resp) {    //微信登录回调
            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                String code = ((SendAuth.Resp) resp).code;
                String state = ((SendAuth.Resp) resp).state;
                String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + wxapi +
                        "&secret=d4f07504b1b9057022153b59ace81208&code=" + code + "&grant_type=authorization_code";
                code(url);
            } else if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                Finish();
                Toast.makeText(this, "用户取消", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 第一步：通过code获取access_token
     */
    private void code(String url) {

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo != null) {
                    try {
                        JSONObject myJsonObject = new JSONObject(responseInfo.result);
                        getSharedPreferences("refresh_token", MODE_PRIVATE).edit().putString("refresh_token",
                                myJsonObject.getString("refresh_token")).commit();
                        getSharedPreferences("unionid", MODE_PRIVATE).edit().putString("unionid",
                                myJsonObject.getString("unionid")).commit();
                        getSharedPreferences("access_token", MODE_PRIVATE).edit().putString("access_token",
                                myJsonObject.getString("access_token")).commit();
                        getSharedPreferences("openid", MODE_PRIVATE).edit().putString("openid",
                                myJsonObject.getString("openid")).commit();
                        Finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(WXEntryActivity.this, "参数错误", Toast.LENGTH_SHORT).show();
                        Finish();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    public void Finish(){
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}