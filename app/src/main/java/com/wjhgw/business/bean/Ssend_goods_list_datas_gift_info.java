package com.wjhgw.business.bean;

/**
 *发出礼包领取情况
 */
public class Ssend_goods_list_datas_gift_info {
    public String gift_note;    //礼包留意
    public String limit_type;   //1为多人礼包,0单人
    public String add_time;     //礼包创建时间
    public String gift_state;   //0无效1进行中2用户已全部领取3取消4过去
    public String member_nickname;  //用户名呢
    public String member_avatar;    //头像
    public String receive_info;     //领取情况
    public String gift_ico;         //礼包图片
    public String gift_link;        //礼包分享链接

}
