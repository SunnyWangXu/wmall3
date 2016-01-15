package com.wjhgw.business.bean;

/**
 * 确认下单返回的数据datas中data,发起微信支付需要的信息
 */
public class PayOrderData {
        public String type;
        public String pay_sn;
        public double total_fee;
        public String goods_name;
        public String goods_detail;

}
