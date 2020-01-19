package com.example.helloworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    private ForceOffineReceiver forceOffineReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, getClass().getSimpleName());
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //活动到栈顶注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.helloworld.LoginActivity.FORCE_OFFLINE");
        forceOffineReceiver = new ForceOffineReceiver();
        registerReceiver(forceOffineReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //活动离开栈顶就注销广播
        unregisterReceiver(forceOffineReceiver);
    }

    class ForceOffineReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Warning!");
            builder.setMessage("You are forced to be offline. Please try to login again!");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
                    //弹出对话框之前销毁所有活动
                    ActivityCollector.finishAll();
                    //由于这个广播是被其他活动继承的，所以这里的intent和启动活动都是采用context配合调用
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }
            });

            builder.show();
        }

    }
}
