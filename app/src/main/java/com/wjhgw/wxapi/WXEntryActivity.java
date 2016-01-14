package com.wjhgw.wxapi;


import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //setContentView(R.layout.pay_result);
		api = WXAPIFactory.createWXAPI(this, "wx99a6bd9b7bdbf645",false);
		api.handleIntent(getIntent(), this);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				//分享成功
				finish();
				Toast.makeText(this,"成功",Toast.LENGTH_SHORT).show();
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				finish();
				Toast.makeText(this,"分享取消",Toast.LENGTH_SHORT).show();
				//分享取消
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				finish();
				Toast.makeText(this,"分享拒绝",Toast.LENGTH_SHORT).show();
				//分享拒绝
				break;
			default:
				finish();
				Toast.makeText(this, "分享返回", Toast.LENGTH_LONG).show();
				break;
		}
	}
}