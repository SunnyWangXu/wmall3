package com.wjhgw.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.wjhgw.R;


/**
 * 
 * @author 加载栏
 *
 */
public class LoadDialog {
	Context mContext;
	public Dialog dialog1;
	public LoadDialog(Context context) {
		mContext = context;
	}

	/**
	 * 加载时的进度栏
	 */
	public void ProgressDialog() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.progress_dialog, null);
		dialog1 = new Dialog(mContext, R.style.dialog);
		dialog1.setContentView(view);
		dialog1.setCanceledOnTouchOutside(false);
		dialog1.getWindow().setGravity(Gravity.CENTER);
		dialog1.show();
	}

	public void dismiss() {
		dialog1.dismiss();
	}
			
}
