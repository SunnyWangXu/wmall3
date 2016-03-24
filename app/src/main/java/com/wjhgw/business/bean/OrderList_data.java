package com.wjhgw.business.bean;

import java.util.ArrayList;

/**
 * 订单列表一级数据
 */
public class OrderList_data {
    public String store_name;
    public String state_desc;
    public String order_sn;
    public String order_amount;
    public String payment_name;
    public String add_time;
    public String shipping_fee;
    public String order_state;
    public String order_id;
    public String lock_state;
    public String pay_sn;       //下单支付秘钥
    public String pay_amount;   //订单金额
    public String rcb_amount;   //使用的充值卡金额
    public String pd_amount;   //使用的余额金额
    public boolean if_cancel;   //是否可用取消
    public boolean if_receive;  //是否可以收货
    public boolean if_lock;     //是否可以锁定（退款退货为锁定）
    public boolean if_deliver;  //是否可以查询物流
    public boolean if_remind;   //是否可以提醒卖家发货
    public boolean evaluation;  //是否可以评价
    public boolean delete;      //是否可以删除订单
    public boolean payment;     //是否可付款
    public boolean if_refund_cancel;     //是否可申请售后
    public ArrayList<OrderList_goods_list_data> extend_order_goods;


}
