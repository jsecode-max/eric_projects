package com.example.helloworld;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


public class Main13Activity extends AppCompatActivity {

    private static final String TAG = "=============";

    private ArrayAdapter<String> adapter;
    private List<String> contactsList = new ArrayList<>();

    private String newId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main13);
        Button get_contact = (Button) findViewById(R.id.get_content_from_contact);
        ListView listView = (ListView) findViewById(R.id.contacts_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, contactsList);
        listView.setAdapter(adapter);

        //获取电话簿
        get_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(Main13Activity.this,
                        Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Main13Activity.this,
                            new String[]{Manifest.permission.READ_CONTACTS}, 1);
                } else {
                    readContacts();
                }
            }
        });

        //通过自定义的ContentProvider完成CRUD
        Button add_data_by_provider = (Button) findViewById(R.id.get_content_from_customized_provider_add_data);
        Button query_data_by_provider = (Button) findViewById(R.id.get_content_from_customized_provider_query_data);
        Button update_data_by_provider = (Button) findViewById(R.id.get_content_from_customized_provider_update_data);
        Button delete_data_by_provider = (Button) findViewById(R.id.get_content_from_customized_provider_delete_data);
        final TextView show_query_data_by_provider = (TextView) findViewById(R.id.tv_by_content_provider);

        add_data_by_provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://com.example.helloworld.provider/book");
                ContentValues values = new ContentValues();
                values.put("name", "aaaaaaa");
                values.put("author", "bbbbbbb");
                values.put("pages", 1040);
                values.put("price", 22.222);
                //初始化ContentProvider，调用DatabaseProvider中重写的insert方法
                //ContentResolver通过解析uri，获取到callingPkg（auth），和path
                //  IContentProvider provider = acquireProvider(uri); 从AndroidManifest中关联具体的provider
                Uri newUri = getContentResolver().insert(uri, values);
                newId = newUri.getPathSegments().get(1);
            }
        });
        query_data_by_provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://com.example.helloworld.provider/book");
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
//                        Log.d(TAG, "The book for : " + name + "|" + author + "|" + pages + "|" + price + "|" + price);
                        show_query_data_by_provider.setText(name + " | " + author + " | " + pages + " | " + price + " | " + price);
                        show_query_data_by_provider.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());  //通过view获取Context
                                builder.setTitle("Warning!");
                                builder.setMessage("do not touch me!!!");
                                builder.setCancelable(true);
                                builder.setIcon(R.drawable.fruit_pic);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(Main13Activity.this, "do not touch me!", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(Main13Activity.this, "you clicked the button NO!", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                                builder.show();
                            }
                        });
                    }
                    cursor.close();
                }
            }
        });
        update_data_by_provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://com.example.helloworld.provider/book/" + newId);
                ContentValues values = new ContentValues();
                values.put("name", "eric's book");
                values.put("price", 8888);
                getContentResolver().update(uri, values, null, null);
            }
        });
        delete_data_by_provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://com.example.helloworld.provider/book/" + newId);
                getContentResolver().delete(uri, null, null);
            }
        });
    }

    private void readContacts() {
        Cursor cursor = null;
        try {
            //查询电话簿
            //这里的query会调用
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    //获取联系人姓名
                    String displayName = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    //获取联系人号码
                    String number = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactsList.add(displayName + "\n" + number);
                }
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
