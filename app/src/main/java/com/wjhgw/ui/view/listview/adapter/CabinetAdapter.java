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
import android.content.Intent;
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
import com.wjhgw.ui.activity.PrductDetailActivity;
import com.wjhgw.ui.dialog.ShoppingDialog;

import java.util.ArrayList;

/**
 * 酒柜列表
 */
public class CabinetAdapter extends BaseAdapter {
    Context c;
    public ArrayList<CadList_data> List;
    private LayoutInflater mInflater;
    private ImageView iv_select;
    public int num = 0;
    public Address_del_Request Request;
    public TextView tv_total_num;
    //已勾选商品个数
    public int total_num = 0;
    //对话框编辑的数量
    public int et_num = 0;
    private ShoppingDialog shoppingDialog;

    public CabinetAdapter(Context c, ArrayList<CadList_data> dataList,
                          ImageView iv_select, TextView tv_total_num, Address_del_Request Request) {
        mInflater = LayoutInflater.from(c);
        this.c = c;
        this.List = dataList;
        this.iv_select = iv_select;
        this.tv_total_num = tv_total_num;
        this.Request = Request;

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
        TextView tv_goods_price = (TextView) cellView.findViewById(R.id.tv_goods_price);
        ImageView iv_goods_image_url = (ImageView) cellView.findViewById(R.id.iv_goods_image_url);
        final ImageView iv_reduction = (ImageView) cellView.findViewById(R.id.iv_reduction);
        final ImageView iv_add = (ImageView) cellView.findViewById(R.id.iv_add);
        final ImageView Choice = (ImageView) cellView.findViewById(R.id.iv_choice1);
        final LinearLayout ll_default = (LinearLayout) cellView.findViewById(R.id.ll_default);

        tv_goods_price.setText("¥ "+List.get(position).goods_price);
        tv_goods_name.setText(List.get(position).goods_name);
        tv_buy_number.setText("剩余" + List.get(position).total_num);
        APP.getApp().getImageLoader().displayImage(List.get(position).goods_image, iv_goods_image_url, APP.getApp().getImageOptions());

        if (List.size() != 0 && !List.get(position).selected.equals("0")) {
            Choice.setImageResource(R.mipmap.ic_order_select);
        } else if (List.get(position).selected.equals("0")) {
            Choice.setImageResource(R.mipmap.ic_order_blank);
        }

        if (num == List.size()) {
            iv_select.setImageResource(R.mipmap.ic_order_select);
        }else {
            iv_select.setImageResource(R.mipmap.ic_order_blank);
        }

        tv_num.setText("" + List.get(position).num);
        if (1 == List.get(position).num) {
            iv_reduction.setImageResource(R.mipmap.ic_disable_reduction);
        }
        if(List.get(position).num == List.get(position).total_num) {
            iv_add.setImageResource(R.mipmap.ic_add_disable);
        }

        cellView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, PrductDetailActivity.class);
                intent.putExtra("goods_id", List.get(position).goods_id);
                //intent.putExtra("Shopping_Cart", "1");
                c.startActivity(intent);
            }
        });

        /**
         * 编辑商品数量
         */
        tv_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingDialog = new ShoppingDialog(c);
                shoppingDialog.show();
                et_num = List.get(position).total_num;
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
                        if (et_num < List.get(position).total_num) {
                            shoppingDialog.et_num.setText(++et_num + "");
                            shoppingDialog.et_num.setSelection(("" + et_num).length());
                            if (et_num == List.get(position).total_num) {
                                shoppingDialog.iv_add.setImageResource(R.mipmap.ic_add_disable);
                            }
                        }
                    }
                });
                shoppingDialog.positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        et_num = Integer.parseInt(shoppingDialog.et_num.getText().toString());
                        if (et_num <= List.get(position).total_num) {
                            List.get(position).num = et_num;
                            if (!List.get(position).selected.equals("0")) {
                                total_num = total_num - Integer.parseInt(tv_num.getText().toString())
                                        + List.get(position).num;
                                tv_total_num.setText(total_num + "件");
                            }
                            tv_num.setText(""+List.get(position).num);

                            if (et_num == List.get(position).total_num) {
                                iv_add.setImageResource(R.mipmap.ic_add_disable);
                            } else {
                                iv_add.setImageResource(R.mipmap.ic_add);
                            }
                             if(et_num == 1){
                                iv_reduction.setImageResource(R.mipmap.ic_disable_reduction);
                            }else {
                                 iv_reduction.setImageResource(R.mipmap.ic_reduction);
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
                            } else if (et_num <= List.get(position).total_num) {
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
                int num = List.get(position).total_num;
                if (num > List.get(position).num) {
                    tv_num.setText("" + ++List.get(position).num);
                    if(!List.get(position).selected.equals("0")){
                        tv_total_num.setText(++total_num + "件");
                    }
                    if (num == List.get(position).num) {
                        iv_add.setImageResource(R.mipmap.ic_add_disable);
                    } else {
                        iv_add.setImageResource(R.mipmap.ic_add);
                    }
                    if(num == 1){
                        iv_reduction.setImageResource(R.mipmap.ic_disable_reduction);
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
                int num = List.get(position).total_num;
                if (List.get(position).num > 1) {
                    tv_num.setText("" + --List.get(position).num);
                    if(!List.get(position).selected.equals("0")){
                        tv_total_num.setText(--total_num + "件");
                    }
                    /*if (1 == List.get(position).num) {
                        iv_reduction.setImageResource(R.mipmap.ic_disable_reduction);
                    } else {
                        iv_add.setImageResource(R.mipmap.ic_add);
                    }*/

                    if (num == List.get(position).num) {
                        iv_add.setImageResource(R.mipmap.ic_add_disable);
                    } else {
                        iv_add.setImageResource(R.mipmap.ic_add);
                    }
                    if(List.get(position).num == 1){
                        iv_reduction.setImageResource(R.mipmap.ic_disable_reduction);
                    }else {
                        iv_reduction.setImageResource(R.mipmap.ic_reduction);
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

                if (List.get(position).selected.equals("0")) {
                    Choice.setImageResource(R.mipmap.ic_order_select);
                    List.get(position).selected = List.get(position).goods_id;

                    num++;
                    if (num == List.size()) {
                        iv_select.setImageResource(R.mipmap.ic_order_select);
                    }
                    total_num += List.get(position).num;
                    tv_total_num.setText(total_num + "件");
                } else {
                    Choice.setImageResource(R.mipmap.ic_order_blank);
                    List.get(position).selected = "0";
                    num--;
                    if (num < List.size()) {
                        iv_select.setImageResource(R.mipmap.ic_order_blank);
                    }

                    total_num -= List.get(position).num;
                    /*int total_num1 = total_num - List.get(position).num;
                    total_num = total_num1;*/
                    tv_total_num.setText(total_num + "件");
                }
            }
        });

        return cellView;
    }

}
