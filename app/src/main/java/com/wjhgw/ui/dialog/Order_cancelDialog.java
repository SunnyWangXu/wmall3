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
 *取消订单专用
 */
public class Order_cancelDialog {

    private Dialog mDialog;
    public TextView tv_text1;
    public TextView tv_text2;
    public TextView tv_text3;
    public TextView tv_text4;


    public Order_cancelDialog(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_cancel_dialog, null);
        mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });

        tv_text1 = (TextView) view.findViewById(R.id.tv_text1);
        tv_text2 = (TextView) view.findViewById(R.id.tv_text2);
        tv_text3 = (TextView) view.findViewById(R.id.tv_text3);
        tv_text4 = (TextView) view.findViewById(R.id.tv_text4);

    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

}
