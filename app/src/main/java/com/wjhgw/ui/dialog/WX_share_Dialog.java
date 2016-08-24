package com.wjhgw.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjhgw.R;

/**
 * 微信分享
 */
public class WX_share_Dialog {

	private View v_window;
	public TextView tv_payment_confirm;
	public LinearLayout ll_wx_circle;
	public LinearLayout ll_wx_friends;
	private Dialog mDialog;

	public WX_share_Dialog(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.wx_share_dialog, null);

		mDialog = new Dialog(context, R.style.shareDialog_style);
		mDialog.getWindow().setGravity(Gravity.BOTTOM);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_BACK){
                    return true;
                }
                return false;
            }
        });
		ll_wx_friends = (LinearLayout) view.findViewById(R.id.ll_wx_friends);
		ll_wx_circle = (LinearLayout) view.findViewById(R.id.ll_wx_circle);
		tv_payment_confirm = (TextView) view.findViewById(R.id.tv_payment_confirm);
		v_window = (View) view.findViewById(R.id.v_window);
		v_window.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});

	}
	
	public void show() {
		mDialog.show();
	}
	
	public void dismiss() {
		mDialog.dismiss();
	}

}
