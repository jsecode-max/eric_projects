package com.example.helloworld;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Main10Activity extends BaseActivity {

    //动态广播
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;

    //本地广播（动态），本地广播无法通过静态注册方式来接收
    private LocalBroadcastReceiver localBroadcastReceiver;
    private LocalBroadcastManager localBroadcastManager;    //sendBroadcast，registerReceiver
    private IntentFilter intentFilter_local;

    //转入登录界面
    private Button loginPage;

    //强制下线
    private Button forceOffline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main10);

        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        networkChangeReceiver = new NetworkChangeReceiver();

        //动态注册广播接收器
        registerReceiver(networkChangeReceiver, intentFilter);

        Button btn_send_broadcast_cus = (Button) findViewById(R.id.btn_broadcast);
        btn_send_broadcast_cus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //定义intent中包含的action字段，与Manifest中注册的一致
                Intent intent = new Intent("com.example.helloworld.MyBroadcast");
                //发送自定义广播，通过intent传播，由此可以携带一些数据
//                sendBroadcast(intent);  //发送标准广播Normal broadcast
                sendOrderedBroadcast(intent, null); //发送有序广播Ordered broadcast
            }
        });

        //Local broadcast receiver
        Button btn_local_broadcast = (Button) findViewById(R.id.btn_local_broadcast);
        //获取LocalBroadcastManager实例
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        btn_local_broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.helloworld.LOCAL_BROADCAST");
                localBroadcastManager.sendBroadcast(intent);    //发送本地广播
            }
        });

        //准备注册广播，需要BroadcastReceiver和IntentFilter
        localBroadcastReceiver = new LocalBroadcastReceiver();
        intentFilter_local = new IntentFilter();
        intentFilter_local.addAction("com.example.helloworld.LOCAL_BROADCAST");
        localBroadcastManager.registerReceiver(localBroadcastReceiver, intentFilter_local);

        loginPage = (Button) findViewById(R.id.to_login);
        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main10Activity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //强制下线
        forceOffline = (Button) findViewById(R.id.force_offline);
        forceOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里的广播注册在BaseActivity中
                Intent intent = new Intent("com.example.helloworld.LoginActivity.FORCE_OFFLINE");
                sendBroadcast(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //动态注册的广播需要手动取消注册
        unregisterReceiver(networkChangeReceiver);
        //取消本地广播
        localBroadcastManager.unregisterReceiver(localBroadcastReceiver);
    }

    class NetworkChangeReceiver extends BroadcastReceiver {
        //不要在广播接收器中执行过多的逻辑操作，如果onReceive方法执行时间过长，程序会报错
        //广播接收器类，动态创建
        //也可以New-Other-Broadcast Receiver单独添加(需要在AndroidManifest中注册--AS自动完成)
        @Override
        public void onReceive(Context context, Intent intent) {

            //通过getSystemService获取系统服务类
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isAvailable()) {
                Toast.makeText(context, "network is available", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "network is unabailable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class LocalBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "handle local broadcast", Toast.LENGTH_SHORT).show();
        }
    }
}
