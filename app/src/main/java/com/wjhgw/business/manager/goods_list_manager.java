package com.wjhgw.business.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wjhgw.business.data.goods_list_data;
import com.wjhgw.business.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class goods_list_manager
{
    private DatabaseHelper helper;
    private SQLiteDatabase db;

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
    public void add(List<goods_list_data> persons)
    {
        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务
        try
        {
            for (goods_list_data person : persons)
            {
                db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME
                        + " VALUES(null, ?, ?, ?)", new Object[] { person.name,
                        person.age, person.info });
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

    /**
     * update person's age
     */
    public void updateAge(goods_list_data person)
    {
        ContentValues cv = new ContentValues();
        cv.put("age", person.age);
        db.update(DatabaseHelper.TABLE_NAME, cv, "name = ?",
                new String[]{person.name});
    }

    /**
     * delete old person
     */
    public void deleteOldPerson(goods_list_data person)
    {
        db.delete(DatabaseHelper.TABLE_NAME, "age < ?",
                new String[]{String.valueOf(person.age)});
    }

    /**
     * query all persons, return list
     */
    public List<goods_list_data> query()
    {
        ArrayList<goods_list_data> persons = new ArrayList<goods_list_data>();
        Cursor c = queryTheCursor();
        while (c.moveToNext())
        {
            goods_list_data person = new goods_list_data();
            //person._id = c.getInt(c.getColumnIndex("_id"));
            person.name = c.getString(c.getColumnIndex("name"));
            person.age = c.getInt(c.getColumnIndex("age"));
            person.info = c.getString(c.getColumnIndex("info"));
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