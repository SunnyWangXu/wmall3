package com.wjhgw.business.controller;


import android.content.Context;

import com.wjhgw.business.data.goods_list_data;
import com.wjhgw.business.manager.goods_list_manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class goods_list_controller {

    private Context mContext;
    private goods_list_manager dbManager;

    public goods_list_controller(Context context) {
        this.mContext = context;
        // 初始化DBManager
        dbManager = new goods_list_manager(context);
    }

    public void add(String response)
    {
        ArrayList<goods_list_data> persons = new ArrayList<>();
        try {
            JSONArray subItemArray = new JSONObject(response).getJSONArray("datas");
            for (int i =0; i < subItemArray.length(); i++){
                String goods_id = subItemArray.getJSONObject(i).getString("goods_id");
                String gppda_name = subItemArray.getJSONObject(i).getString("goods_name");
                String goods_price = subItemArray.getJSONObject(i).getString("goods_price");

                goods_list_data person1 = new goods_list_data(goods_id, i, gppda_name);
                persons.add(person1);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        dbManager.add(persons);
    }

    public void update()
    {
        // 把Jane的年龄改为30（注意更改的是数据库中的值，要查询才能刷新ListView中显示的结果）
        goods_list_data person = new goods_list_data();
        person.name = "华";
        person.age = 25;
        dbManager.updateAge(person);
    }

    public void delete()
    {
        // 删除所有三十岁以上的人（此操作在update之后进行，Jane会被删除（因为她的年龄被改为30））
        // 同样是查询才能查看更改结果
        goods_list_data person = new goods_list_data();
        person.age = 25;
        dbManager.deleteOldPerson(person);
    }

    public void query()
    {
        List<goods_list_data> persons = dbManager.query();
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (goods_list_data person : persons)
        {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", person.name);
            map.put("info", person.age + " years old, " + person.info);
            list.add(map);
        }
    }
}
