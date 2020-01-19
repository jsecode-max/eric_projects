package com.example.helloworld;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context mContext;
    public static final String CREATE_TABLE = "create table Book(" +
            "id integer primary key autoincrement, " +
            "author text, " +   //文本    blob；二进制类型
            "price real, " +    //浮点数
            "pages integer," +  //整数
            "name text)";
    public static final String CREATE_CATEGORY = "create table Category(" +
            "id integer primary key autoincrement, " +
            "category_name text, " +
            "category_code integer)";


    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        //在某个Activity中初始化MyDatabaseHelper的时候，记录context
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //建表逻辑
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_CATEGORY);
        Toast.makeText(mContext, "Create Database succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Book");
        db.execSQL("drop table if exists Category");
        onCreate(db);
    }
}
