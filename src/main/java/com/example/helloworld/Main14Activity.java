package com.example.helloworld;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class Main14Activity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main14);

        Button sendNotice = (Button) findViewById(R.id.show_notification);
        sendNotice.setOnClickListener(this);

        //点击通知后，通知消失的第二种方法；
        //第一种方法是在Notification中 .setAutoCancel(true)
        //设置自动消除哪一条通知，这里的参数1对应了：notificationManager.notify(1, notification);
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        manager.cancel(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_notification:
                //给PendingIntent准备
                Intent intent = new Intent(this, NotificationActivity.class);
                PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

                if (Build.VERSION.SDK_INT >= 26) {
                    // 对于sdk版本大于等于26的情况，需要采用通知通道NotificationChannel
                    // 否则手机提示：Fail to post notification on channel "null"
                    String id = "channel_1";    //channel的id
                    String description = "this is channel 1"; //channel的描述信息
                    int importance = NotificationManager.IMPORTANCE_LOW;    //channel的重要性
                    
                    NotificationChannel channel = new NotificationChannel(id, "NameOfChannel1", importance);   //生成channel
                    //为channel添加属性
                    channel.enableVibration(true);  //震动，无效meta20 pro
                    channel.enableLights(true);     //提示灯，无效meta20 pro

                    //通知管理器
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.createNotificationChannel(channel);

                    //构建通知对象，并绑定了通知通道的id
                    Notification notification = new NotificationCompat.Builder(this, id)
                            .setContentTitle("Title")   //标题
                            .setContentText("Text from Notification")   //类似通知内容摘要，继续下拉通知，会展示setStyle中的富文本内容
                            .setStyle(new NotificationCompat.BigTextStyle().bigText("你以德抱怨，那何以报德？别人以德来待你的时候，" +
                                    "你才需要以德来回报别人。可是现在别人打了你，你就应该'以直报怨。”\n" +
                                    "所以其实，孔子是反对我们常说的“以德报怨"))    //下拉后展示的通知内容
                            .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(
                                    BitmapFactory.decodeResource(getResources(), R.drawable.big_pic)
                            ))  //两个setStyle，后面覆盖前面
                            .setContentIntent(pi)   //设置点击通知后的延迟意图
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.drawable.fruit_pic)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.fruit_pic))
                            .setAutoCancel(true)    //设置自动清除这一条通知
//                            .setSound(Uri.fromFile(new File("...")))
//                            .setVibrate(new long[]{0, 1000, 1000, 1000})    //振动需要权限申请
                            .setLights(Color.YELLOW, 1000, 1000) //设置闪灯，似乎这个颜色在meta20 pro上都是绿色
                            .setDefaults(NotificationCompat.DEFAULT_ALL)    //根据手机当前环境设置通知默认的铃声和振动
                            .setPriority(NotificationCompat.PRIORITY_MAX)   //一共五个等级
                            .build();
                    //出发通知
                    notificationManager.notify(1, notification);
                } else {
                    // 对于sdk版本小于26的，则不需要通知通道的支持
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Notification notification = new NotificationCompat.Builder(this)
                            .setContentTitle("Title")
                            .setContentText("Text from Notification")
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.drawable.fruit_pic)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.fruit_pic))
                            .build();
                    notificationManager.notify(1, notification);
                }
                break;
            default:
                break;
        }
    }
}
