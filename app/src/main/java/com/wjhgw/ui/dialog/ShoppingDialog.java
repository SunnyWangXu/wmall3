package com.wjhgw.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjhgw.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 购物车改变商品数量对话框
 */
public class ShoppingDialog extends Dialog {

    private Dialog mDialog;
    public TextView positive;
    public TextView negative;
    public ImageView iv_reduction;
    public ImageView iv_add;
    public EditText et_num;
    public Context context;

    public ShoppingDialog(final Context context) {
        super(context);
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.shopping_dialog_layout, null);

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
        iv_reduction = (ImageView) view.findViewById(R.id.iv_reduction);
        iv_add = (ImageView) view.findViewById(R.id.iv_add);
        et_num = (EditText) view.findViewById(R.id.et_num);
        positive = (TextView) view.findViewById(R.id.tv_determine);
        negative = (TextView) view.findViewById(R.id.tv_no);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 200);
    }

    public void show() {
        mDialog.show();
        et_num.requestFocus();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

}
