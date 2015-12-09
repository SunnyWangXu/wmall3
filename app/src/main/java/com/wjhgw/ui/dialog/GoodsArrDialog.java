package com.wjhgw.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.wjhgw.R;

/**
 * 长按商品排序弹出的对话框
 */

public class GoodsArrDialog {


    private Button btnCollect;
    private Button btnGoodsarrAddshopcar;
    private Dialog mDialog;

    public GoodsArrDialog(Context context, String message1, String message2) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.goodsarr_dialog_layout, null);
        btnCollect = (Button) view.findViewById(R.id.btn_collect);
        btnGoodsarrAddshopcar = (Button) view.findViewById(R.id.btn_goodsarr_addshopcar);

        mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(view);

        btnCollect.setText(message1);
        btnGoodsarrAddshopcar.setText(message2);

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
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

}
