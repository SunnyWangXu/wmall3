package com.wjhgw.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wjhgw.R;

/**
 * 设置支付密码对话框
 */
public class EntryPaypwdDialog {

    public EditText etEntryPaypwd;
    private Dialog mDialog;
    public TextView tvEntryCancel;
    public TextView tvEntryPaypwd;

    public EntryPaypwdDialog(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.entry_paypwd_dialog_layout, null);

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

        etEntryPaypwd = (EditText) view.findViewById(R.id.et_entry_paspwd);
        tvEntryCancel = (TextView) view.findViewById(R.id.tv_entry_cancel);
        tvEntryPaypwd = (TextView) view.findViewById(R.id.tv_entry_paypwd);

        etEntryPaypwd.setFocusable(true);
        etEntryPaypwd.requestFocus();

    }


    public void show() {

        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

}
