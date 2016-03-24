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
 *申请售后专用
 */
public class Customer_serviceDialog {

    private Dialog mDialog;
    public TextView tv_text1;
    public TextView tv_text2;
    public TextView tv_text3;
    public TextView tv_text4;
    public TextView tv_text5;



    public Customer_serviceDialog(Context context, final TextView tv_d4_text3) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.customer_service_dialog, null);
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
        tv_text5 = (TextView) view.findViewById(R.id.tv_text5);

        tv_text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                tv_d4_text3.setText("不能按时发货");
            }
        });
        tv_text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                tv_d4_text3.setText("认为是假货");
            }
        });
        tv_text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                tv_d4_text3.setText("保质期不符");
            }
        });
        tv_text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                tv_d4_text3.setText("商品破损、有污渍");
            }
        });
        tv_text5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                tv_d4_text3.setText("效果不好不喜欢");
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
