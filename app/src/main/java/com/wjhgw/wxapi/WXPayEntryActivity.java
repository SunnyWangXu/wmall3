package com.wjhgw.wxapi;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.pay_result);

		api = WXAPIFactory.createWXAPI(this, "wxd930ea5d5a258f4f");

		api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		//Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if(resp.errCode == 0){
				//Toast.makeText(this, "成功！"+resp.errCode, Toast.LENGTH_SHORT).show();
				 /*Resources resource = (Resources) getBaseContext().getResources();
            		Intent intent = new Intent(WXPayEntryActivity.this, WebViewActivity.class);
            		intent.putExtra(WebViewActivity.WEBURL, ConfigModel.getInstance().config.site_url + "/app/user.php?act=order_list");
                    String off=resource.getString(R.string.order_list);
                    intent.putExtra(WebViewActivity.WEBTITLE, off);
                    startActivity(intent);*/
             	  //finish();
			}else if(resp.errCode == -1){
				new AlertDialog.Builder(this)
	            .setMessage("支付失败！")
	            .setPositiveButton("知道了",
	                           new DialogInterface.OnClickListener(){
	                                   public void onClick(DialogInterface dialoginterface, int i){
	                                	   finish();
	                                    }
	                            })
	            .setCancelable(false)	//点击对话框外不关闭
	            .show();
			}else if(resp.errCode == -2){
				new AlertDialog.Builder(this)
	            .setMessage("用户取消！")
	            .setPositiveButton("知道了",
	                           new DialogInterface.OnClickListener(){
	                                   public void onClick(DialogInterface dialoginterface, int i){
	                                	  finish();
	                                    }
	                            })
	            .setCancelable(false)	//点击对话框外不关闭
	            .show();
			}
		}
	}
}