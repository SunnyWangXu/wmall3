package com.wjhgw.business.bean;

import java.util.ArrayList;

/**
 * 限时抢购LimitDatas
 */
public class LimitDatas {

    public String xianshi_id;
    public String xianshi_name;
    public String xianshi_title;
    public String xianshi_explain;
    public String mobile_thumb;
    public String quota_id;
    public String start_time;
    public String end_time;
    public String store_id;
    public String store_name;
    public String lower_limit;
    public String state;
    public Long count_down_time;
    public ArrayList<LimitGoodsInfo> goods_info;


}
