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
import com.wjhgw.business.bean.CartList_goods_list_data;
import com.wjhgw.ui.activity.PrductDetailActivity;
import com.wjhgw.ui.dialog.GoodsArrDialog;
import com.wjhgw.ui.dialog.ShoppingDialog;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 购物车
 */
public class ShoppingCartAdapter extends BaseAdapter {
    Context c;
    public ArrayList<CartList_goods_list_data> List;
    private LayoutInflater mInflater;
    public String[] goods_id = null;
    //public ArrayList<String> goods_id1 = null;
    private ImageView iv_select;
    private ImageView iv_select1;
    public int num = 0;
    public Address_del_Request Request;
    public TextView tv_total;
    public TextView tv_total_num;
    //合计金额
    public double total = 0;
    //已勾选商品个数
    public int total_num = 0;
    //对话框编辑的数量
    public int et_num = 0;
    private ShoppingDialog shoppingDialog;
    private GoodsArrDialog goodsArrDialog;
    private String key;

    public ShoppingCartAdapter(Context c, ArrayList<CartList_goods_list_data> dataList,
                               ImageView iv_select, ImageView iv_select1, TextView tv_total, TextView tv_total_num, Address_del_Request Request) {
        mInflater = LayoutInflater.from(c);
        this.c = c;
        this.List = dataList;
        goods_id = new String[List.size()];
        this.iv_select = iv_select;
        this.iv_select1 = iv_select1;
        this.tv_total = tv_total;
        this.tv_total_num = tv_total_num;
        this.Request = Request;
        for (int i = 0; i < List.size(); i++) {
            goods_id[i] = List.get(i).cart_id;
            total += Double.parseDouble(List.get(i).goods_num) *
                    Double.parseDouble(List.get(i).goods_price);
            total_num += Double.parseDouble(List.get(i).goods_num);
        }
        BigDecimal f = new BigDecimal(total);
        total = f.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        tv_total.setText("¥ " + total);
        tv_total_num.setText("(" + total_num + ")");
        num = goods_id.length;
        key = c.getSharedPreferences("key", c.MODE_APPEND).getString("key", "0");
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

        cellView = mInflater.inflate(R.layout.shopping_item, null);
        TextView tv_goods_name = (TextView) cellView.findViewById(R.id.tv_goods_name);
        TextView tv_goods_price = (TextView) cellView.findViewById(R.id.tv_goods_price);
        final TextView tv_num = (TextView) cellView.findViewById(R.id.tv_num);
        ImageView iv_goods_image_url = (ImageView) cellView.findViewById(R.id.iv_goods_image_url);
        final ImageView iv_reduction = (ImageView) cellView.findViewById(R.id.iv_reduction);
        final ImageView iv_add = (ImageView) cellView.findViewById(R.id.iv_add);
        final ImageView Choice = (ImageView) cellView.findViewById(R.id.iv_choice1);
        final LinearLayout ll_default = (LinearLayout) cellView.findViewById(R.id.ll_default);

        tv_goods_name.setText(List.get(position).goods_name);
        tv_goods_price.setText("¥ " + List.get(position).goods_price);
        tv_num.setText(List.get(position).goods_num);
        APP.getApp().getImageLoader().displayImage(List.get(position).goods_image_url, iv_goods_image_url,APP.getApp().getImageOptions());

        if (List.size() != 0 && !goods_id[position].equals("0")) {
            Choice.setImageResource(R.mipmap.ic_order_select);
        } else if (goods_id[position].equals("0")) {
            Choice.setImageResource(R.mipmap.ic_order_blank);
        }

        if (List.get(position).goods_num.equals("1")) {
            iv_reduction.setImageResource(R.mipmap.ic_disable_reduction);
        } else if (List.get(position).goods_num.equals("99")) {
            iv_add.setImageResource(R.mipmap.ic_add_disable);
        }
        cellView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, PrductDetailActivity.class);
                intent.putExtra("goods_id", List.get(position).goods_id);
                intent.putExtra("Shopping_Cart", "1");
                c.startActivity(intent);
            }
        });

        cellView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                goodsArrDialog = new GoodsArrDialog(c, "收藏", "删除");
                goodsArrDialog.show();
                goodsArrDialog.btnCollect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Request.favorites_add(List.get(position).goods_id,key);
                        goodsArrDialog.dismiss();
                    }
                });
                goodsArrDialog.btnGoodsarrAddshopcar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Request.cart_del(List.get(position).cart_id, key);
                        goodsArrDialog.dismiss();
                    }
                });
                return true;
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
                et_num = Integer.parseInt(List.get(position).goods_num);
                shoppingDialog.et_num.setText(et_num + "");
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
                        if (et_num <= 99) {
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
                        if (num < 99) {
                            Request.cart_edit_quantity(List.get(position).cart_id, et_num + "", key,"3" ,
                                    !goods_id[position].equals("0"),Double.parseDouble(List.get(position).goods_price),
                                    Integer.parseInt(List.get(position).goods_num),position);
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
                            } else if (et_num == 99) {
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
                int num = Integer.parseInt(List.get(position).goods_num);
                if (num < 99) {
                    BigDecimal f = new BigDecimal(total + Double.parseDouble(List.get(position).goods_price));
                    Request.cart_edit_quantity(List.get(position).cart_id, ++num + "", key,"1" ,
                            !goods_id[position].equals("0"),f.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(),
                            total_num+1,position);
                }
            }
        });

        /**
         * 单个商品减1
         */
        iv_reduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(List.get(position).goods_num);
                if (num > 1) {
                    BigDecimal f = new BigDecimal(total - Double.parseDouble(List.get(position).goods_price));
                    Request.cart_edit_quantity(List.get(position).cart_id, --num + "", key,"0" ,
                            !goods_id[position].equals("0"),f.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(),
                            total_num-1,position);

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
                    goods_id[position] = List.get(position).cart_id;

                    num++;
                    if (num == List.size()) {
                        iv_select.setImageResource(R.mipmap.ic_order_select);
                        iv_select1.setImageResource(R.mipmap.ic_order_select);
                    }
                    BigDecimal f = new BigDecimal(total + Integer.parseInt(List.get(position).goods_num) * Double.parseDouble(List.get(position).goods_price));
                    total = f.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                    total_num += Double.parseDouble(List.get(position).goods_num);
                    tv_total.setText("¥ " + total);
                    tv_total_num.setText("(" + total_num + ")");
                } else {
                    Choice.setImageResource(R.mipmap.ic_order_blank);
                    goods_id[position] = "0";
                    num--;
                    if (num < goods_id.length) {
                        iv_select.setImageResource(R.mipmap.ic_order_blank);
                        iv_select1.setImageResource(R.mipmap.ic_order_blank);
                    }
                    BigDecimal f = new BigDecimal(total - Integer.parseInt(List.get(position).goods_num) * Double.parseDouble(List.get(position).goods_price));
                    total = f.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    total_num -= Double.parseDouble(List.get(position).goods_num);
                    tv_total.setText("¥ " + total);
                    tv_total_num.setText("(" + total_num + ")");
                }
            }
        });

        return cellView;
    }

}
