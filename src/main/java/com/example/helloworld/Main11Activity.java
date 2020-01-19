package com.example.helloworld;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.LitePalDB;
import org.litepal.crud.LitePalSupport;
import org.litepal.tablemanager.Connector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Main11Activity extends AppCompatActivity {

    private static final String TAG = "Main11Activity";

    private EditText edit;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main11);

        LitePal.initialize(this);

        edit = (EditText) findViewById(R.id.edit_file_persistence);
        //默认启动该活动的时候，加载文件
        String inputText = load();
        //TextUtils.isEmpty()一次进行两种空值判断（null或者空字符串），返回true或者false
        if (!TextUtils.isEmpty(inputText)) {
            edit.setText(inputText);
            edit.setSelection(inputText.length());  //设置鼠标落的位置
            Toast.makeText(this, "Restoring succeeded", Toast.LENGTH_SHORT).show();
        }

        //使用SharedPreference保存键值对
        Button saveDate = (Button) findViewById(R.id.save_data);
        final EditText et_name = (EditText) findViewById(R.id.name);
        final EditText et_age = (EditText) findViewById(R.id.age);
        final EditText et_marriage = (EditText) findViewById(R.id.married);
        saveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Context下获取Editor对象
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                //推数据仅editor
                editor.putString("name", et_name.getText().toString());
                editor.putInt("age", Integer.valueOf(et_age.getText().toString())); //String -> Integer
                editor.putBoolean("married", Boolean.valueOf(et_marriage.getText().toString()));    //String -> Boolean
                //editor提交
                editor.apply();
            }
        });
        //使用SharedPreference读取键值对
        Button restoreDate = (Button) findViewById(R.id.restore_date);
        restoreDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                String name = pref.getString("name", "gaojf");
                int age = pref.getInt("age", 0);
                boolean married = pref.getBoolean("married", false);
                StringBuilder builder = new StringBuilder();
                builder.append(name);
                builder.append("+");
                builder.append(age);
                builder.append("+");
                builder.append(married);
                builder.append("||||");
                edit.setText(builder.toString());
            }
        });

        //创建数据库SQLite
        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 5);
        Button createDatabase = (Button) findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //新建或打开现有数据库，首先检测有没有构造参数中提供的数据库名称；
                //然后回调onCreate函数，其中有建表逻辑，如果表已经存在，则不会重复建表
                dbHelper.getWritableDatabase();
            }
        });

        //insert数据到SQLite
        final Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", "The Da Vinci Code");
                values.put("author", "Dan Brown");
                values.put("pages", 566);
                values.put("price", 16.96);
                db.insert("Book", null, values);
                values.clear(); //清空重用
                values.put("name", "Python Code");
                values.put("author", "Eric Brown");
                values.put("pages", 5656);
                values.put("price", 146.936);
                db.insert("Book", null, values);
            }
        });
        //update数据
        Button updateData = (Button) findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price", 8888.88);
                db.update("Book", values, "name = ?", new String[]{"The Da Vinci Code"});
            }
        });
        //delete数据
        Button deleteData = (Button) findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Book", "name = ?", new String[]{"Python Code"});
            }
        });
        //query
        final TextView showData = (TextView) findViewById(R.id.from_query);
        Button queryData = (Button) findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("Book", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int page = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));

                        StringBuilder builder = new StringBuilder();
                        builder.append(name + " | ");
                        builder.append(author + " | ");
                        builder.append(page + " | ");
                        builder.append(price);
                        showData.setText(builder.toString());

                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        });

        //LitePal
        //Create database
        Button createDatabasePal = (Button) findViewById(R.id.create_database_pal);
        createDatabasePal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connector.getDatabase();
            }
        });

        //LitePal insert
        Button insertPal = (Button) findViewById(R.id.insert_pal);
        insertPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book();
                book.setAuthor("Dan Brown");
                book.setName("Python code");
                book.setPages(389);
                book.setPrice(54.76);
                book.setPress("Unknown");
                book.save();
            }
        });

        //LitePal update
        Button updatePal = (Button) findViewById(R.id.update_pal);
        updatePal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**更新方法一
                 Book book = new Book();
                 book.setAuthor("Dan Brown Eric");
                 book.setName("Python code bible");
                 book.setPages(222);
                 book.setPrice(35.79);
                 book.setPress("UnknownKK");
                 book.save();
                 book.setPrice(1000000);
                 book.save();**/
                //更新方法二
                Book book = new Book();
                book.setPrice(88888.88);
//                book.setPress("China");
                book.setToDefault("press");
                book.updateAll("name = ? and author = ?", "Python code bible",
                        "Dan Brown Eric");
            }
        });

        //LitePal delete
        Button deletePal = (Button) findViewById(R.id.delete_pal);
        deletePal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book();
                LitePal.deleteAll(Book.class, "id = ?", "19");
                book.delete();
            }
        });

        //LitePal query(find)
        Button queryPal = (Button) findViewById(R.id.query_pal);
        queryPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回的是实体类型的list
                List<Book> list = LitePal.findAll(Book.class);
                for (Book book : list) {
                    Log.d(TAG, "book name is : " + book.getName());
                    Log.d(TAG, "book author is : " + book.getAuthor());
                    Log.d(TAG, "book pages is : " + book.getPages());
                    Log.d(TAG, "book price is : " + book.getPrice());
                    Log.d(TAG, "book press is : " + book.getPress());
                }
                Book firstBook = LitePal.findFirst(Book.class);
                Book lastBook = LitePal.findLast(Book.class);
                Log.d(TAG, "book name is : " + firstBook.getName());
                Log.d(TAG, "book author is : " + firstBook.getAuthor());
                Log.d(TAG, "book pages is : " + firstBook.getPages());
                Log.d(TAG, "book price is : " + firstBook.getPrice());
                Log.d(TAG, "book press is : " + firstBook.getPress());

                //带条件的检索
                List<Book> books = LitePal
                        .select("name", "author")
                        .where("pages > ?", "300")
                        .order("price desc")
                        .limit(3)
                        .offset(1)  //查询前3条，偏移1，那就是第2，3，4条
                        .find(Book.class);
                for (Book book : books) {
                    Log.d(TAG, "book name is : " + book.getName());
                    Log.d(TAG, "book author is : " + book.getAuthor());
                    Log.d(TAG, "book pages is : " + book.getPages());
                    Log.d(TAG, "book price is : " + book.getPrice());
                    Log.d(TAG, "book press is : " + book.getPress());
                }
                //原生SQL检索
                Cursor bookList = LitePal.findBySQL("select * from Book where pages > ? and price < ?",
                        "200", "60");
                if (bookList.moveToFirst()) {
                    do {
                        String name = bookList.getString(bookList.getColumnIndex("name"));
                        String author = bookList.getString(bookList.getColumnIndex("author"));
                        int page = bookList.getInt(bookList.getColumnIndex("pages"));
                        double price = bookList.getDouble(bookList.getColumnIndex("price"));
                        String press = bookList.getString(bookList.getColumnIndex("press"));
                        StringBuilder builder = new StringBuilder();
                        builder.append(name + " | " + author + " | " + page + " | " + price + " | " + press);
                        Log.d(TAG, "PAL BY SQL(cursor): " + builder.toString());
                    } while (bookList.moveToNext());
                }
                bookList.close();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String inputText = edit.getText().toString();
        save(inputText);
    }

    public void save(String inputText) {
        //写文件，到本地文件，路径默认/data/data/PROJECT/
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            //这里采用了文件流的方式
            // openFileOutput -> FileOutputStream -> OutputStreamWriter -> BufferedWriter
            out = openFileOutput("data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String load() {
        //读本地文件，从默认路径
        // openFileInput -> FileIntputStream -> InputStreamReader -> BufferedReader
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput("data");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

}
