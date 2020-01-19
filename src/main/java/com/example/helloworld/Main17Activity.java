package com.example.helloworld;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Main17Activity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Main17Activity";

    //服务绑定
    private MyService.DownloadBinder downloadBinder;
    private DownloadService.DownloadBinder downloadBinderNew;
    //活动与服务绑定和解绑时调用
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //参数service接收service的onBind返回的IBinder对象
            //Binder继承了IBinder接口
            downloadBinder = (MyService.DownloadBinder) service;
            //执行service中定义的执行方法
            downloadBinder.startDownload();
            downloadBinder.getProcess();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private ServiceConnection connectionX = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinderNew = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main17);

        Button startService = (Button) findViewById(R.id.start_service);
        Button stopService = (Button) findViewById(R.id.stop_service);
        Button bindService = (Button) findViewById(R.id.bind_service);
        Button unbindService = (Button) findViewById(R.id.unbind_service);
        Button startIntentSerivce = (Button) findViewById(R.id.start_intent_service);
        Button startDownload = (Button) findViewById(R.id.start_download);
        Button pauseDownload = (Button) findViewById(R.id.pause_download);
        Button cancelDownload = (Button) findViewById(R.id.cancel_download);
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        bindService.setOnClickListener(this);
        unbindService.setOnClickListener(this);
        startIntentSerivce.setOnClickListener(this);
        startDownload.setOnClickListener(this);
        pauseDownload.setOnClickListener(this);
        cancelDownload.setOnClickListener(this);

        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);   //保持一直在后台
        bindService(intent, connectionX, BIND_AUTO_CREATE); //保持活动与服务的通信

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_service:
                Intent startIntent = new Intent(this, MyService.class);
                startService(startIntent);
                break;
            case R.id.stop_service:
                Intent stopService = new Intent(this, MyService.class);
                stopService(stopService);
                break;
            case R.id.bind_service:
                Intent bindIntent = new Intent(this, MyService.class);
                //BIND_AUTO_CREATE：在活动和服务进行绑定后自动创建服务，Service的onCreate执行，但是onStartCommand不会执行
                //bindService获得服务的持久连接，通过服务的onBind返回 Ibinder对象的实例
                bindService(bindIntent, connection, BIND_AUTO_CREATE);
                break;
            case R.id.unbind_service:
                unbindService(connection);
                break;
            case R.id.start_intent_service:
                Log.d(TAG, "====: " + Thread.currentThread().getId());
                Intent intentService = new Intent(this, MyIntentService.class);
                startService(intentService);
                break;
            case R.id.start_download:
                String url = "http://www.baidu.com";
                downloadBinderNew.startDownload(url);
                break;
            case R.id.pause_download:
                downloadBinderNew.pauseDownload();
                break;
            case R.id.cancel_download:
                downloadBinderNew.cancelDownload();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "拒绝权限无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connectionX);
    }
}
