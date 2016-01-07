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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.business.api.Address_del_Request;
import com.wjhgw.business.bean.CadList_data;
import com.wjhgw.ui.dialog.ShoppingDialog;

import java.util.ArrayList;

/**
 * 酒柜列表
 */
public class RecordAdapter extends BaseAdapter {
    Context c;
    public ArrayList<CadList_data> List;
    private LayoutInflater mInflater;
    public String[] goods_id = null;
    public int[] buy_number = null;
    private ImageView iv_select;
    public int num = 0;
    public Address_del_Request Request;
    public TextView tv_total_num;
    //已勾选商品个数
    public int total_num = 0;
    //对话框编辑的数量
    public int et_num = 0;
    private ShoppingDialog shoppingDialog;

    public RecordAdapter(Context c, ArrayList<CadList_data> dataList,
                         ImageView iv_select, TextView tv_total_num, Address_del_Request Request) {
        mInflater = LayoutInflater.from(c);
        this.c = c;
        this.List = dataList;
        goods_id = new String[List.size()];
        buy_number = new int[List.size()];
        this.iv_select = iv_select;
        this.tv_total_num = tv_total_num;
        this.Request = Request;
        for (int i = 0; i < List.size(); i++) {
            goods_id[i] = "0";
            buy_number[i] = 1;
        }
        tv_total_num.setText(total_num + "件");
    }

    @Override
    public int getCount() {
        return List.size();
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

        cellView = mInflater.inflate(R.layout.cabinet_item, null);
        TextView tv_goods_name = (TextView) cellView.findViewById(R.id.tv_goods_name);
        final TextView tv_num = (TextView) cellView.findViewById(R.id.tv_num);
        final TextView tv_buy_number = (TextView) cellView.findViewById(R.id.tv_buy_number);
        ImageView iv_goods_image_url = (ImageView) cellView.findViewById(R.id.iv_goods_image_url);
        final ImageView iv_reduction = (ImageView) cellView.findViewById(R.id.iv_reduction);
        final ImageView iv_add = (ImageView) cellView.findViewById(R.id.iv_add);
        final ImageView Choice = (ImageView) cellView.findViewById(R.id.iv_choice1);
        final LinearLayout ll_default = (LinearLayout) cellView.findViewById(R.id.ll_default);

        tv_goods_name.setText(List.get(position).goods_name);
        tv_buy_number.setText("剩余"+List.get(position).buy_number);
        APP.getApp().getImageLoader().displayImage(List.get(position).goods_image, iv_goods_image_url, APP.getApp().getImageOptions());

        if (List.size() != 0 && !goods_id[position].equals("0")) {
            Choice.setImageResource(R.mipmap.ic_order_select);
        } else if (goods_id[position].equals("0")) {
            Choice.setImageResource(R.mipmap.ic_order_blank);
        }

        tv_num.setText("" + buy_number[position]);
        if(1 == buy_number[position]){
            iv_reduction.setImageResource(R.mipmap.ic_disable_reduction);
        }else if (buy_number[position] == Integer.parseInt(List.get(position).buy_number)){
            iv_add.setImageResource(R.mipmap.ic_add_disable);
        }


        /**
         * 编辑商品数量
         */
        tv_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingDialog = new ShoppingDialog(c);
                shoppingDialog.show();
                et_num = Integer.parseInt(List.get(position).buy_number);
                shoppingDialog.et_num.setText("1");
                shoppingDialog.et_num.selectAll();
                shoppingDialog.iv_reduction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (et_num > 1) {
                            shoppingDialog.et_num.setText(--et_num + "");
                            shoppingDialog.et_num.setSelection(("" + et_num).length());
                            if (et_num == 1) {
                                shoppingDialog.iv_reduction.setImageResource(R.mipmap.ic_disable_reduction);
                            }
                        }

                    }
                });
                shoppingDialog.iv_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (et_num < Integer.parseInt(List.get(position).buy_number)) {
                            shoppingDialog.et_num.setText(++et_num + "");
                            shoppingDialog.et_num.setSelection(("" + et_num).length());
                            if (et_num == 1) {
                                shoppingDialog.iv_add.setImageResource(R.mipmap.ic_add_disable);
                            }
                        }
                    }
                });
                shoppingDialog.positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        et_num = Integer.parseInt(shoppingDialog.et_num.getText().toString());
                        if (et_num <= Integer.parseInt(List.get(position).buy_number)) {
                                buy_number[position] = et_num;
                            if (!goods_id[position].equals("0")) {
                                tv_total_num.setText(++total_num + "件");
                            }
                        }
                        shoppingDialog.dismiss();
                    }
                });
                shoppingDialog.negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shoppingDialog.dismiss();
                    }
                });
                shoppingDialog.et_num.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (start > 0) {
                            et_num = Integer.parseInt(shoppingDialog.et_num.getText().toString());
                            if (et_num == 1) {
                                shoppingDialog.iv_reduction.setImageResource(R.mipmap.ic_disable_reduction);
                            } else if (et_num == Integer.parseInt(List.get(position).buy_number)) {
                                shoppingDialog.iv_add.setImageResource(R.mipmap.ic_add_disable);
                            } else {
                                shoppingDialog.iv_add.setImageResource(R.mipmap.ic_add);
                                shoppingDialog.iv_reduction.setImageResource(R.mipmap.ic_reduction);
                            }
                        } else {
                            if (start == 0 && count > 0) {
                                et_num = Integer.parseInt(shoppingDialog.et_num.getText().toString());
                                if (et_num == 0) {
                                    et_num = 1;
                                    shoppingDialog.et_num.setText(et_num + "");
                                    shoppingDialog.et_num.setSelection(("" + et_num).length());
                                }
                                if (et_num == 1) {
                                    shoppingDialog.iv_reduction.setImageResource(R.mipmap.ic_disable_reduction);
                                } else {
                                    shoppingDialog.iv_add.setImageResource(R.mipmap.ic_add);
                                    shoppingDialog.iv_reduction.setImageResource(R.mipmap.ic_reduction);
                                }
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
            }
        });
        /**
         * 单个商品加1
         */
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(List.get(position).buy_number);
                if (num > buy_number[position]) {
                        tv_total_num.setText(++total_num + "件");
                        tv_num.setText("" + ++buy_number[position]);
                    if(num == buy_number[position]){
                        iv_add.setImageResource(R.mipmap.ic_add_disable);
                    }else {
                        iv_reduction.setImageResource(R.mipmap.ic_reduction);
                    }
                }
            }
        });

        /**
         * 单个商品减1
         */
        iv_reduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int num = Integer.parseInt(List.get(position).buy_number);
                if (buy_number[position] > 1) {
                        tv_total_num.setText(--total_num + "件");
                        tv_num.setText("" + --buy_number[position]);
                    if(1 == buy_number[position]){
                        iv_reduction.setImageResource(R.mipmap.ic_disable_reduction);
                    }else {
                        iv_add.setImageResource(R.mipmap.ic_add);
                    }
                }
            }
        });

        /**
         * 勾选处理
         */
        ll_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (goods_id[position].equals("0")) {
                    Choice.setImageResource(R.mipmap.ic_order_select);
                    goods_id[position] = List.get(position).goods_id;

                    num++;
                    if (num == List.size()) {
                        iv_select.setImageResource(R.mipmap.ic_order_select);
                    }
                    total_num += buy_number[position];
                    tv_total_num.setText(total_num + "件");
                } else {
                    Choice.setImageResource(R.mipmap.ic_order_blank);
                    goods_id[position] = "0";
                    num--;
                    if (num < goods_id.length) {
                        iv_select.setImageResource(R.mipmap.ic_order_blank);
                    }
                    total_num -= buy_number[position];
                    tv_total_num.setText(total_num + "件");
                }
            }
        });

        return cellView;
    }

}
