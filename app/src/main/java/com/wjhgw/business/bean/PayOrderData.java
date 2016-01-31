package com.wjhgw.business.bean;

import java.util.List;

/**
 * 确认下单返回的数据datas中data,发起微信和支付宝支付需要的信息
 */
public class PayOrderData {
        public String type;
        public String pay_sn;
        public String total_fee;
        public String goods_name;
        public String goods_detail;
        public List<Confirm_order_list> order_list;


}
