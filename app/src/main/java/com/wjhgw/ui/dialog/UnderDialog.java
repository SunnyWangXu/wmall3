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
 * 正在开发中
 */
public class UnderDialog {

    private Dialog mDialog;
    public TextView tv_goto_setpaypwd;
    public TextView message;

    public UnderDialog(Context context, String biaoti) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.under_development_dialog_layout, null);
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

        tv_goto_setpaypwd = (TextView) view.findViewById(R.id.tv_goto_setpaypwd);
        message = (TextView) view.findViewById(R.id.dialog_message);
        message.setText(biaoti);

    }


    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

}
