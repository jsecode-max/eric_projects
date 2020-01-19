package com.example.helloworld;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Main3Activity extends BaseActivity {

    private static final String TAG = "Main3Activity";
    private String[] data = {"Apple", "Banana", "Orange", "Watermelon", "Pear", "Grape",
            "Pineapple", "Strawberry","Cherry", "Mango", "Apple", "Banana", "Orange",
            "Watermelon", "Pear", "Grape", "Pineapple", "Strawberry","Cherry"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        //创建适配器，适配器用于存放要展示的数据
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                Main3Activity.this, android.R.layout.simple_expandable_list_item_1, data);
        ListView listView = (ListView) findViewById(R.id.list_view_1);
        listView.setAdapter(adapter);   //将适配器传给listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Main3Activity.this, data[position], Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = getIntent();
        String data = intent.getStringExtra("logo");
        Log.d(TAG, "onCreate: " + data);

        Button button2 = (Button) findViewById(R.id.button_2);
        Button dail = (Button) findViewById(R.id.DAIl);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.baidu.com"));
                startActivity(intent);
            }
        });
        dail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:10086"));
                startActivity(intent);
            }
        });
    }

    public static void actionStart(Context context, String data1, String data2){
        //Main3Activity的启动活动，其他活动要启动的话，只要调用该actionStart方法
        //调用(从Main2Activity中调)：Main3Activity.actionStart(Main2Activity.this, "data1", "data2")
        Intent intent = new Intent(context, Main3Activity.class);
        intent.putExtra("param1", data1);
        intent.putExtra("param2", data2);
        context.startActivity(intent);

    }


}
