package com.wjhgw.business.bean;

import java.util.ArrayList;

/**
 * 售前退款申请数据
 */
public class add_refund_data_data {
    public String order_sn;
    public String order_amount;
    public String order_type;
    public ArrayList<OrderList_goods_list_data> goods_list;

}
