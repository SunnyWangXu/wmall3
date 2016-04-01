package com.wjhgw.business.bean;

import java.util.ArrayList;

/**
 * 选中订单商品详情
 */
public class Store_cart_list {
    public ArrayList<Order_goods_list> goods_list;
    public String store_goods_total;
    public int freight;
    public String freight_message;
    public String store_name;
    public String store_id;
    public CancelCalcSidList cancel_calc_sid_list;
}
