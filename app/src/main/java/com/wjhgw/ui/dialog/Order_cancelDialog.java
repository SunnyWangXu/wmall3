package com.wjhgw.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjhgw.R;

/**
 *
 */
public class Order_cancelDialog {

    private Dialog mDialog;
    public TextView tv_cancel;
    public TextView tv_determine;
    public EditText et_content;
    public ImageView iv_button1;
    public ImageView iv_button2;
    public ImageView iv_button3;
    public ImageView iv_button4;


    public Order_cancelDialog(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_cancel_dialog, null);
        mDialog = new Dialog(context, R.style.shareDialog_style);
        mDialog.setContentView(view);
        mDialog.getWindow().setGravity(Gravity.BOTTOM);
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

        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_determine = (TextView) view.findViewById(R.id.tv_determine);
        et_content = (EditText) view.findViewById(R.id.et_content);
        iv_button1 = (ImageView) view.findViewById(R.id.iv_button1);
        iv_button2 = (ImageView) view.findViewById(R.id.iv_button2);
        iv_button3 = (ImageView) view.findViewById(R.id.iv_button3);
        iv_button4 = (ImageView) view.findViewById(R.id.iv_button4);

    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

}
