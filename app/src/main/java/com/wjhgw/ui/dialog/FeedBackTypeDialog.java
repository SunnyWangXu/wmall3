package com.wjhgw.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wjhgw.R;

/**
 * 意见反馈类型的弹出框
 */
public class FeedBackTypeDialog {

    private Dialog mDialog;
    public TextView tv_text1;
    public TextView tv_text2;
    public TextView tv_text3;
    public TextView tv_text4;
    public TextView tv_text5;
    public String MarkType = "1";

    public FeedBackTypeDialog(Context context, final TextView tvFeedBackType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.feedback_type_dialog, null);
        mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);

        tv_text1 = (TextView) view.findViewById(R.id.tv_text1);
        tv_text2 = (TextView) view.findViewById(R.id.tv_text2);
        tv_text3 = (TextView) view.findViewById(R.id.tv_text3);
        tv_text4 = (TextView) view.findViewById(R.id.tv_text4);
        tv_text5 = (TextView) view.findViewById(R.id.tv_text5);

        tv_text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                tvFeedBackType.setText("功能意见");
                MarkType = "1";
            }
        });

        tv_text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                tvFeedBackType.setText("界面意见");
                MarkType = "2";

            }
        });

        tv_text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                tvFeedBackType.setText("操作意见");
                MarkType = "3";
            }
        });

        tv_text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                tvFeedBackType.setText("流量问题");
                MarkType = "4";
            }
        });

        tv_text5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                tvFeedBackType.setText("其他");
                MarkType = "0";
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
