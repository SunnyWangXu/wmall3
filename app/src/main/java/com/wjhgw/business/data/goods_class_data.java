package com.wjhgw.business.data;

import org.json.JSONException;
import org.json.JSONObject;

public class goods_class_data {
    public String gc_id;
    public String gc_name;
    public String image;

    public void  fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return ;
        }
        this.gc_id = jsonObject.optString("gc_id");
        this.gc_name = jsonObject.optString("gc_name");
        this.image = jsonObject.optString("image");
    }
}
