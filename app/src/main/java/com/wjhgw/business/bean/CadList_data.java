package com.wjhgw.business.bean;

/**
 * 酒柜商品列表数据
 */
public class CadList_data {
    //public String buy_number;   //自己存酒柜的数量
    public String receive_number;   //接收数量，不能赠送
    public String buy_number;   //自己卖的，可自提和赠送
    public int total_num;   //总数量
    public String update_time;   //更新时间
    public String goods_name;
    public String goods_image;
    public String goods_id;
    public String goods_price;
    public int num = 1; //编辑的商品数量
    public String selected = "0"; //勾选
    public boolean receive = false; //用于判断是否可以是否可赠送

}
