package com.wjhgw.business.analytical;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * 部分的Json解析
 */
public class ClassAnalytical {

    //public ArrayList<Object> data = new ArrayList<>();

    public <T> ArrayList<T> fromJsonArray(String response, Class<T> clazz) throws JSONException {
        JSONArray Array = new JSONObject(response).optJSONArray("datas");
        ArrayList<T> data = new ArrayList<>();
        try {
            for (int i = 0; i < Array.length(); i++) {
                JSONObject subItemObject = Array.getJSONObject(i);
                T obj = null;
                obj = clazz.newInstance();
                Method[] props = clazz.getDeclaredMethods();
                for (Method method : props) {
                    String methodName = method.getName();
                    if (methodName.startsWith("set")) {
                        String propName = methodName.substring(3).toLowerCase();
                        Object oooo = subItemObject.opt(propName);
                        if (oooo != null) {
                            if (oooo instanceof JSONArray) {
                                //this.fromJson(JSONObject);
                            } else if (oooo instanceof JSONObject) {

                            }
                            method.invoke(obj, oooo);
                        }
                    }
                }
                data.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public <T> T fromJsonObject(String response, Class<T> clazz) throws JSONException {
        try {
            JSONObject data = new JSONObject(response).optJSONObject("datas");
            T obj = null;
            obj = clazz.newInstance();
            Method[] props = clazz.getDeclaredMethods();
            for (Method method : props) {
                String methodName = method.getName();
                if (methodName.startsWith("set")) {
                    String propName = methodName.substring(3).toLowerCase();
                    Object oooo = data.opt(propName);
                    if (oooo != null) {
                        if (oooo instanceof JSONArray) {
                            //this.fromJson(JSONObject);
                        } else if (oooo instanceof JSONObject) {

                        }
                        method.invoke(obj, oooo);
                    }
                }
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*public <T> ArrayList<T> fromJson(String response, Class<T> clazz) throws JSONException {
        JSONArray subItemArray = new JSONObject(response).optJSONArray("datas");
        if (subItemArray == null) {
            //this.fromJson(new JSONObject(response).optJSONObject("datas"), clazz);
        } else {
            if (this.fromJson(subItemArray, clazz) == null) {
                return null;
            } else {
                return this.fromJson(subItemArray, clazz);
            }
        }
        if (null == response) {
            return null;
        }
        return null;
    }*/
}
