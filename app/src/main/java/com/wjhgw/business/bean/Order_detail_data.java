package com.wjhgw.business.bean;

import java.util.ArrayList;

/**
 * 订单详情
 */
public class Order_detail_data {
    public String store_name;
    public String state_desc;
    public String order_sn;
    public String order_amount;
    public String payment_name;
    public String order_message;
    public String add_time;
    public String shipping_fee;
    public String payment_time;
    public String order_state;
    public String lock_state;
    public String order_type;
    public String order_id;
    public String pay_sn;
    public String rcb_amount;
    public String pd_amount;
    public ArrayList<OrderList_goods_list_data> extend_order_goods;
    public Extend_order_common extend_order_common;
    public boolean if_cancel;   //是否可用取消
    public boolean if_receive;  //是否可以收货
    public boolean if_lock;     //是否可以锁定（退款退货为锁定）
    public boolean if_deliver;  //是否可以查询物流
    public boolean if_remind;   //是否可以提醒卖家发货
    public boolean evaluation;  //是否可以评价
    public boolean delete;      //是否可以删除订单
    public boolean payment;     //是否可付款
    public boolean if_refund_cancel;     //是否退款


}
