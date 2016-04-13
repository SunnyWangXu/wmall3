package com.wjhgw.business.bean;

/**
 *用户发送的数据
 */
public class Gift_list_data {
    public String cab_gift_id;
    public String gift_note;
    public String limit_type;   //1多人,2单人
    public String add_time;
    public String receive_info;
    public String gift_state;   //0无效，1进行中，2用户已全部领取，3发起人已取消，4已过去
}
