package com.wjhgw.business.bean;

import java.util.List;

/**
 * 退款退货详情datas
 */
public class RefundDetailDatas {
    public String refund_id;
    public String refund_sn;
    public String seller_message;
    public String store_name;
    public String refund_type;
    public String reason_info;
    public String refund_amount;
    public String goods_num;
    public String buyer_message;
    public String seller_state_desc;
    public String order_id;
    public String order_sn;
    public String express_name;
    public String ship_goods;
    public String invoice_no;
    public List<RefundDetailList> goods_list;
    public String[] pic_info;
}
