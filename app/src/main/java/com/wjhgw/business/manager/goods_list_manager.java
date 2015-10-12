package com.wjhgw.business.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wjhgw.business.bean.goods_list_data;
import com.wjhgw.business.sqlite.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class goods_list_manager
{
    private DatabaseHelper helper;
    private SQLiteDatabase db;
    //public JSONObject status = null;
    public ArrayList<goods_list_data> data = new ArrayList<>();

    public goods_list_manager(Context context)
    {
        helper = new DatabaseHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add persons
     */
    public void add(String response)
    {
        try {
            //status =  new JSONObject(new JSONObject(response).getString("status"));
            JSONArray subItemArray = new JSONObject(response).getJSONArray("datas");
            for (int i =0; i < subItemArray.length(); i++){
                JSONObject subItemObject = subItemArray.getJSONObject(i);
                goods_list_data subItem = new goods_list_data();
                subItem.fromJson(subItemObject);
                data.add(subItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (data.size()>0){
            deleteOldPerson();
            // 采用事务处理，确保数据完整性
            db.beginTransaction(); // 开始事务
            try
            {
                for (goods_list_data person : data)
                {
                    db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME
                                    + " VALUES(null, ?, ?, ?)",
                            new Object[] { person.goods_name, person.age, person.goods_id });
                    // 带两个参数的execSQL()方法，采用占位符参数？，把参数值放在后面，顺序对应
                    // 一个参数的execSQL()方法中，用户输入特殊字符时需要转义
                    // 使用占位符有效区分了这种情况
                }
                db.setTransactionSuccessful(); // 设置事务成功完成
            }
            finally
            {
                db.endTransaction(); // 结束事务
            }
        }
    }

    /**
     * update person's age
     */
    public void updateAge(goods_list_data person)
    {
        ContentValues cv = new ContentValues();
        cv.put("age", person.age);
        db.update(DatabaseHelper.TABLE_NAME, cv, "name = ?",
                new String[]{person.goods_name});
    }

    /**
     * delete old person
     */
    public void deleteOldPerson()
    {
//        db.delete(DatabaseHelper.TABLE_NAME, "age > ?", new String[]{String.valueOf(person.age)});
        try {
            //db.execSQL("drop table " + DatabaseHelper.TABLE_NAME);

            db.delete(DatabaseHelper.TABLE_NAME, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * query all persons, return list
     */
    public ArrayList<goods_list_data> query()
    {
        ArrayList<goods_list_data> persons = new ArrayList<>();
        Cursor c = queryTheCursor();
        while (c.moveToNext())
        {
            goods_list_data person = new goods_list_data();
            person.goods_name = c.getString(c.getColumnIndex("goods_name"));
            person.goods_id = c.getString(c.getColumnIndex("goods_id"));
            persons.add(person);
        }
        c.close();
        return persons;
    }

    /**
     * query all persons, return cursor
     */
    public Cursor queryTheCursor()
    {
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME,
                null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB()
    {
        // 释放数据库资源
        db.close();

    }


}