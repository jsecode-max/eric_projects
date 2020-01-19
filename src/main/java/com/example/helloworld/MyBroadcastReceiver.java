package com.example.helloworld;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast.makeText(context, "Send broadcast in private.", Toast.LENGTH_SHORT).show();
        //如果通过有序广播调用，且不想继续传播下去，那么就可以调用abordBroadcast截断广播
        abortBroadcast();
    }
}
