package com.example.helloworld;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
    private static final String TAG = "******";
    private DownloadBinder mBinder = new DownloadBinder();

    public MyService() {
    }

    class DownloadBinder extends Binder {
        public void startDownload() {
            Log.d(TAG, "startDownload: ");
        }

        public int getProcess() {
            Log.d(TAG, "getProcess: ");
            return 0;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // 服务绑定，Binder需要单独定义，并在这里返回
        // Binder继承了IBinder接口
        return mBinder;
    }

    @Override
    public void onCreate() {
        //service创建的时候，执行一次
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        Toast.makeText(this, "service onCreate", Toast.LENGTH_SHORT).show();

        //前台服务
        Intent intent = new Intent(this, Main17Activity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        String id = "service_001";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(id, "service_", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, id)
                    .setContentTitle("This is contect title")
                    .setContentText("This is content title")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.fruit_pic)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.big_pic))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();
            startForeground(1, notification);
        } else {
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("This is contect title")
                    .setContentText("This is content title")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.fruit_pic)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.big_pic))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();
            startForeground(1, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //service每次启动的时候，都会执行
        Log.d(TAG, "onStartCommand: ");
        Toast.makeText(this, "service onStartCommand", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        //在service中的任何地方执行stopSelf()均可以停止service
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        Toast.makeText(this, "service onDestroy", Toast.LENGTH_SHORT).show();
    }
}
