package com.wjhgw.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wjhgw.ui.activity.J0_SelectGiveObjectActivity;
import com.wjhgw.ui.dialog.MyDialog;
import com.wjhgw.ui.dialog.UnderDialog;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	MyDialog mDialog;
	
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
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

			if (resp.errCode == 0) {
				Toast.makeText(this, "支付成功！", Toast.LENGTH_SHORT).show();
				String Gifts = this.getSharedPreferences("Gifts", this.MODE_APPEND).getString("Gifts", "0");
				final String paySn = this.getSharedPreferences("paySn", this.MODE_APPEND).getString("paySn", "0");
				if(Gifts.equals("1")){
					UnderDialog under = new UnderDialog(this, "支付成功！赠送亲友?");
					under.show();
					under.tv_goto_setpaypwd.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(WXPayEntryActivity.this, J0_SelectGiveObjectActivity.class);
							intent.putExtra("paySn", paySn);
							intent.putExtra("entrance", "4");
							startActivity(intent);
							finish();
						}

					});
				}else {
					finish();
				}

			} else if (resp.errCode == -1) {
				Toast.makeText(this, "支付失败！", Toast.LENGTH_SHORT).show();
				finish();
			} else if (resp.errCode == -2) {
				Toast.makeText(this, "用户取消！", Toast.LENGTH_SHORT).show();
				finish();
			}
			getSharedPreferences("Gifts", MODE_PRIVATE).edit().putString("Gifts", "0").commit();
			getSharedPreferences("paySn", MODE_PRIVATE).edit().putString("paySn", "0").commit();
		}
	}
}