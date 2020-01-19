package com.example.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main6Activity extends AppCompatActivity {

    private List<Fruit> fruitList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        initFruits();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.revycler_view);

        //LinerLayoutManager用于指定RecyclerView的布局方式
        //LinearLayoutManager, GridLayoutManager(网格), StaggeredGridLayoutManager(瀑布流)
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //配合布局文件采用不同的展示方式，默认为vertical
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        final RecyclerFruitAdapter adapter = new RecyclerFruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);
        //设置分隔线 0：纵向分割线； 1：横向分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        //设置增加或删除条目的动画(这里可以继续研究，自定义更多动画效果)
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(1000);
        defaultItemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(defaultItemAnimator);

        //添加item
        Button btn_add = (Button) findViewById(R.id.action_add);
        Button btn_remove = (Button) findViewById(R.id.action_remove);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addData(1);
            }
        });
        //移除item
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.removeData(1);
            }
        });
    }

    private void initFruits() {
        // 水果 文字和图片
        for (int i = 0; i < 10; i++) {
            Fruit apple = new Fruit(getRandomLengthName("Apple"), R.drawable.fruit_pic);
            fruitList.add(apple);
            Fruit orange = new Fruit(getRandomLengthName("Orange"), R.drawable.fruit_pic);
            fruitList.add(orange);
        }
    }

    private String getRandomLengthName(String name) {
        Random random = new Random();
        int length = random.nextInt(20) + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(name);
        }
        return builder.toString();
    }
}
