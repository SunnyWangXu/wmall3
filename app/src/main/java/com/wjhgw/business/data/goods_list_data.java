package com.wjhgw.business.data;

public class goods_list_data {
    public int _id;
    public String name;
    public int age;
    public String info;

    public goods_list_data() {
    }

    public goods_list_data(String name, int age, String info) {
        this.name = name;
        this.age = age;
        this.info = info;
    }
}
