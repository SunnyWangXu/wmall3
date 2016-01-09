package com.wjhgw.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wjhgw.R;

/**
 * 设置支付密码对话框
 */
public class SetPaypwdDialog {

	private Dialog mDialog;
	private TextView dialog_title;
	private TextView dialog_message;
	public TextView tvCancel;
	public TextView tvGotoSetpaypwd;

	public SetPaypwdDialog(Context context, String title, String message) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.setpaypwd_dialog_layout, null);

		mDialog = new Dialog(context, R.style.dialog);
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
		dialog_title = (TextView) view.findViewById(R.id.dialog_title);
		dialog_message = (TextView) view.findViewById(R.id.dialog_message);
		dialog_title.setText(title);
		dialog_message.setText(message);
		
		tvCancel = (TextView) view.findViewById(R.id.tv_setpaypwd_cancel);
		tvGotoSetpaypwd = (TextView) view.findViewById(R.id.tv_goto_setpaypwd);
		
	}
	
	public void show() {
		mDialog.show();
	}
	
	public void dismiss() {
		mDialog.dismiss();
	}

}
