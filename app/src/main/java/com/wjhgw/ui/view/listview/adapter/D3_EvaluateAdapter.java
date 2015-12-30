package com.wjhgw.ui.view.listview.adapter;

//
//                       __
//                      /\ \   _
//    ____    ____   ___\ \ \_/ \           _____    ___     ___
//   / _  \  / __ \ / __ \ \    <     __   /\__  \  / __ \  / __ \
//  /\ \_\ \/\  __//\  __/\ \ \\ \   /\_\  \/_/  / /\ \_\ \/\ \_\ \
//  \ \____ \ \____\ \____\\ \_\\_\  \/_/   /\____\\ \____/\ \____/
//   \/____\ \/____/\/____/ \/_//_/         \/____/ \/___/  \/___/
//     /\____/
//     \/___/
//
//  Powered by BeeFramework
//

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjhgw.APP;
import com.wjhgw.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 商品评价适配器
 */
public class D3_EvaluateAdapter extends BaseAdapter {
    Context c;
    public JSONArray List;
    private LayoutInflater mInflater;
    public String rating[];
    public String content[];

    public D3_EvaluateAdapter(Context c, JSONArray dataList) {
        mInflater = LayoutInflater.from(c);
        this.c = c;
        this.List = dataList;
        rating = new String[List.length()];
        content = new String[List.length()];
        for (int i = 0; i < List.length(); i++) {
            rating[i] = "5";
            content[i] = "";
        }
    }

    @Override
    public int getCount() {
        return List.length();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    public View getView(final int position, View cellView, ViewGroup parent) {
        cellView = mInflater.inflate(R.layout.d3_item, null);
        ImageView iv_goods_url = (ImageView) cellView.findViewById(R.id.iv_goods_url);
        final EditText et_name = (EditText) cellView.findViewById(R.id.et_name);
        final ImageView iv_rating1 = (ImageView) cellView.findViewById(R.id.iv_rating1);
        final ImageView iv_rating2 = (ImageView) cellView.findViewById(R.id.iv_rating2);
        final ImageView iv_rating3 = (ImageView) cellView.findViewById(R.id.iv_rating3);
        final ImageView iv_rating4 = (ImageView) cellView.findViewById(R.id.iv_rating4);
        final ImageView iv_rating5 = (ImageView) cellView.findViewById(R.id.iv_rating5);
        final TextView tv_text = (TextView) cellView.findViewById(R.id.tv_text);
        final TextView tv_num = (TextView) cellView.findViewById(R.id.tv_num);
        try {
            APP.getApp().getImageLoader().displayImage(((JSONObject) List.get(position)).getString("goods_image_url"), iv_goods_url);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 0) {
                    String a = et_name.getText().toString();
                    String c = a.substring(a.length() - 1, a.length());
                    String b = a.substring(0, a.length() - 1);
                    if (c.equals("|")) {
                        content[position] = b;
                        et_name.setText(b);
                        et_name.setSelection(b.length());
                    } else {
                        content[position] = a;
                    }
                    tv_num.setText(et_name.getText().length() + "/200");
                } else {
                    if (start == 0 && count > 0) {
                        tv_num.setText(et_name.getText().length() + "/200");
                    } else {
                        tv_num.setText("0/200");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        iv_rating1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_rating1.setImageResource(R.mipmap.ic_rating1);
                iv_rating2.setImageResource(R.mipmap.ic_rating2);
                iv_rating3.setImageResource(R.mipmap.ic_rating2);
                iv_rating4.setImageResource(R.mipmap.ic_rating2);
                iv_rating5.setImageResource(R.mipmap.ic_rating2);
                tv_text.setText("很不满意");
                rating[position] = "1";
            }
        });
        iv_rating2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_rating1.setImageResource(R.mipmap.ic_rating1);
                iv_rating2.setImageResource(R.mipmap.ic_rating1);
                iv_rating3.setImageResource(R.mipmap.ic_rating2);
                iv_rating4.setImageResource(R.mipmap.ic_rating2);
                iv_rating5.setImageResource(R.mipmap.ic_rating2);
                tv_text.setText("不满意");
                rating[position] = "2";
            }
        });
        iv_rating3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_rating1.setImageResource(R.mipmap.ic_rating1);
                iv_rating2.setImageResource(R.mipmap.ic_rating1);
                iv_rating3.setImageResource(R.mipmap.ic_rating1);
                iv_rating4.setImageResource(R.mipmap.ic_rating2);
                iv_rating5.setImageResource(R.mipmap.ic_rating2);
                tv_text.setText("一般");
                rating[position] = "3";
            }
        });
        iv_rating4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_rating1.setImageResource(R.mipmap.ic_rating1);
                iv_rating2.setImageResource(R.mipmap.ic_rating1);
                iv_rating3.setImageResource(R.mipmap.ic_rating1);
                iv_rating4.setImageResource(R.mipmap.ic_rating1);
                iv_rating5.setImageResource(R.mipmap.ic_rating2);
                tv_text.setText("满意");
                rating[position] = "4";
            }
        });
        iv_rating5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_rating1.setImageResource(R.mipmap.ic_rating1);
                iv_rating2.setImageResource(R.mipmap.ic_rating1);
                iv_rating3.setImageResource(R.mipmap.ic_rating1);
                iv_rating4.setImageResource(R.mipmap.ic_rating1);
                iv_rating5.setImageResource(R.mipmap.ic_rating1);
                tv_text.setText("很满意");
                rating[position] = "5";
            }
        });
        return cellView;
    }
}
