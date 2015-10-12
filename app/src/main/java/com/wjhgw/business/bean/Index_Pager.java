package com.wjhgw.business.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/10/12 0012.
 */
public class Index_Pager {
    private  NetStatus status;
    private  List<Index_Pager_data> datas;

    public NetStatus getStatus() {
        return status;
    }

    public void setStatus(NetStatus status) {
        this.status = status;
    }

    public List<Index_Pager_data> getDatas() {
        return datas;
    }

    public void setDatas(List<Index_Pager_data> datas) {
        this.datas = datas;
    }
}
