package com.wjhgw.business.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class goods_list_data {
    public String goods_name;
    public int age = 1;
    public String goods_id;

    public void  fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return ;
        }
        this.goods_id = jsonObject.optString("goods_id");
        this.goods_name = jsonObject.optString("goods_name");
        return ;
    }
}
