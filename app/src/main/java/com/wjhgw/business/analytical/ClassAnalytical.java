package com.wjhgw.business.analytical;

import com.wjhgw.business.data.goods_class_data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 部分list的Json解析
 */
public class ClassAnalytical {

    public ArrayList<goods_class_data> data = new ArrayList<>();

    public void  fromJson(String response)  throws JSONException
    {
        if(null == response){
            return ;
        }
    try {
        //status =  new JSONObject(new JSONObject(response).getString("status"));
        JSONArray subItemArray = new JSONObject(response).getJSONArray("datas");
        for (int i =0; i < subItemArray.length(); i++){
            JSONObject subItemObject = subItemArray.getJSONObject(i);
            goods_class_data subItem = new goods_class_data();
            subItem.fromJson(subItemObject);
            data.add(subItem);
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }
    }
}
