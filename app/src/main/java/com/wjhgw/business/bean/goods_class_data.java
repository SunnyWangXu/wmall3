package com.wjhgw.business.bean;

public class goods_class_data {
    public String gc_id;
    public String gc_name;
    public String image;
    public String image1;

    public String getGc_id() {
        return gc_id;
    }

    public void setGc_id(String gc_id) {
        this.gc_id = gc_id;
    }

    public String getGc_name() {
        return gc_name;
    }

    public void setGc_name(String gc_name) {
        this.gc_name = gc_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }
    /*
    public void  fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return ;
        }
        this.gc_id = jsonObject.optString("gc_id");
        this.gc_name = jsonObject.optString("gc_name");
        this.image = jsonObject.optString("image");
        this.image1 = jsonObject.optString("im");
    }*/
}
