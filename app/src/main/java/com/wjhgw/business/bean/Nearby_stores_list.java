package com.wjhgw.business.bean;

import java.util.List;

/**
 * 获取附近店铺列表
 */
public class Nearby_stores_list {
    public NetStatus status;
    public List<Nearby_stores_list_data> datas;
    public   ActSearch_pagination pagination;
}
