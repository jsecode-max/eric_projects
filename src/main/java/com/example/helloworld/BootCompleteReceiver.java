package com.example.helloworld;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {
    //静态注册在AndroidManifest中
    //不要在广播接收器中执行过多的逻辑操作，如果onReceive方法执行时间过长，程序会报错

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast.makeText(context, "Boot Complete", Toast.LENGTH_SHORT).show();
    }
}
