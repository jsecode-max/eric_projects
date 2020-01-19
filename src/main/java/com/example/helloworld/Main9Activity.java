package com.example.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class Main9Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //当跳转到这里展示activity_main9 layout的时候，系统系统判断屏幕大小，决定调用哪一个activity_main9
        //如果是layout/activity_main9，那么（单页）仅展示NewsTitleFragment
        //如果是sw600dp/activity_main9，那么（双页）将展示NewsTitleFragment和NewsContentFragment
        setContentView(R.layout.activity_main9);

    }

}
