package com.example.helloworld;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Main5Activity extends AppCompatActivity {

    private List<Fruit> fruitList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //初始化水果数据
        initFruits();
        //创建适配器，将要展示的数据fruitList，展示到布局R.layout.fruit_item上，也就是列表的一行
        FruitAdapter adapter = new FruitAdapter(Main5Activity.this, R.layout.fruit_item, fruitList);
        ListView listView = (ListView) this.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        //ListView注册监听器，与普通按钮的点击注册函数不同
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fruit fruit = fruitList.get(position);  //通过position判断点击的是哪一个子项
                Toast.makeText(Main5Activity.this, fruit.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initFruits() {
        for (int i = 0; i < 10; i++) {
            Fruit apple = new Fruit("Apple", R.drawable.fruit_pic);
            fruitList.add(apple);
            Fruit orange = new Fruit("Orange", R.drawable.fruit_pic);
            fruitList.add(orange);
        }
    }

}

