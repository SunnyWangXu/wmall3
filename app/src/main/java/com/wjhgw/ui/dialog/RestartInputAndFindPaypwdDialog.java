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
 * 支付密码输入有误
 */
public class RestartInputAndFindPaypwdDialog {

    public  TextView tvFindPaypwd;
    public TextView tvRestartInput;
    private Dialog mDialog;

    public RestartInputAndFindPaypwdDialog(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.restartinput_find_paypwd_dialog, null);

        mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });

        tvRestartInput = (TextView) view.findViewById(R.id.tv_restart_input);
        tvFindPaypwd = (TextView) view.findViewById(R.id.tv_find_paypwd);

    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

}
