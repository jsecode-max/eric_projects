package com.example.helloworld;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class Main8Activity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);

        Button button = (Button) findViewById(R.id.button_4);
        button.setOnClickListener(this);
        replaceFragment(new RightFragment());   //直接把RightFragment实例传过来

        //通过getSupportFragmentManager().findFragmentById获取Fragment实例，继而与碎片通信
        RightFragment rightFragment = (RightFragment) getSupportFragmentManager().findFragmentById(R.id.right_layout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_4:
                replaceFragment(new AnotherRightFragment());
                break;
            default:
                break;
        }
    }

    private void replaceFragment(Fragment fragmant) {
        //通过FragmentManager获取事务，通过事务替换，并提交
        FragmentManager fragmentManager = getSupportFragmentManager();  // 获取FragmentManager实例
        FragmentTransaction transaction = fragmentManager.beginTransaction();   //开启FragmentTransaction事务
        transaction.replace(R.id.right_layout, fragmant);   //将Fragment添加到指定的布局中，这与LayoutInflator差不多，也与在layout中静态映射Activity与布局一个意思
        transaction.addToBackStack(null);   //接收一个String的名字，一般写null即可，此时碎片处于停止状态
        transaction.commit();   //提交事务
    }
}
