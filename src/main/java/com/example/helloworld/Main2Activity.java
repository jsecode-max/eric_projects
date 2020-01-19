package com.example.helloworld;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Main2Activity extends BaseActivity {

    private static final String TAG = "Main2Activity";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String returnedData = data.getStringExtra("data_return");
                    Log.d(TAG, "onActivityResult: " + returnedData);
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //隐藏系统自带的标题栏
        //如果没有隐藏，将同时显示系统和自定义的标题栏（工具栏） ToolBar
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null){
//            actionBar.hide();
//        }

        Button button1 = (Button) findViewById(R.id.button_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Main2Activity.this, "You clicked button 1",
                        Toast.LENGTH_SHORT).show();
                String data = "Hello Main3Activity";
//                Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
                Intent intent = new Intent("com.example.helloworld.ACTION_START");
                intent.addCategory("com.example.activitytest.MY_CATEGORY");
                intent.putExtra("logo", data);
                startActivity(intent);
            }
        });

        final Button startAtn4Res = (Button) findViewById(R.id.btn_forResult);
        startAtn4Res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Main4Activity.class);
                startActivityForResult(intent, 1);
            }
        });

        //转到自定义FruitAdapter适配器的ListView
        Button btn_2 = (Button) findViewById(R.id.btn_2);
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Main2Activity.this, "btn_2 is clicked.",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Main2Activity.this, Main5Activity.class);
                startActivity(intent);
            }
        });

        Button btn_back = (Button) findViewById(R.id.title_back);
        Button btn_edit = (Button) findViewById(R.id.title_edit);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Main2Activity.this, "You clicked the Back Button, so.....",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Main2Activity.this, "You clicked the Edit Button.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button btn_recycle_view = (Button) findViewById(R.id.btn_3);
        btn_recycle_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Main6Activity.class);
                startActivity(intent);
            }
        });

        Button btn_talk = (Button) findViewById(R.id.btn_4);
        btn_talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Main7Activity.class);
                startActivity(intent);
            }
        });

        Button btn_fragment = (Button) findViewById(R.id.btn_5);
        btn_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Main8Activity.class);
                startActivity(intent);
            }
        });

        Button btn_fragment_news = (Button) findViewById(R.id.btn_6);
        btn_fragment_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Main9Activity.class);
                startActivity(intent);
//                NewsContentActivity.actionStart(Main2Activity.this,
//                        "news from A2", "this is the content of news.");
            }
        });

        Button btn_broadcast_receiver = (Button) findViewById(R.id.btn_7);
        btn_broadcast_receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Main10Activity.class);
                startActivity(intent);
            }
        });

        Button btn_data_persistence = (Button) findViewById(R.id.btn_8);
        btn_data_persistence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Main11Activity.class);
                startActivity(intent);
            }
        });

        Button btn_runtime_permission = (Button) findViewById(R.id.btn_9);
        btn_runtime_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Main12Activity.class);
                startActivity(intent);
            }
        });

        Button btn_content_provider = (Button) findViewById(R.id.btn_10);
        btn_content_provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Main13Activity.class);
                startActivity(intent);
            }
        });

        Button btn_notification = (Button) findViewById(R.id.btn_11);
        btn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Main14Activity.class);
                startActivity(intent);
            }
        });

        Button btn_take_photo = (Button) findViewById(R.id.btn_12);
        btn_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Main15Activity.class);
                startActivity(intent);
            }
        });

        Button btn_network = (Button) findViewById(R.id.btn_13);
        btn_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Main16Activity.class);
                startActivity(intent);
            }
        });

        Button btn_service = (Button) findViewById(R.id.btn_14);
        btn_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Main17Activity.class);
                startActivity(intent);
            }
        });

        Button btn_alarm = (Button) findViewById(R.id.btn_15);
        btn_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, LongRunningService.class);
                startService(intent);
            }
        });

        Button btn_material_design = (Button) findViewById(R.id.btn_16);
        btn_material_design.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Main18Activity.class);
                startActivity(intent);
            }
        });
    }

    ///P39 to be continuing...
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                Toast.makeText(Main2Activity.this, "You clicked ADD",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.remove_item:
                Toast.makeText(Main2Activity.this, "You clicked REMOVE",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.finish_item:
                finish();
                break;
            default:
        }
        return true;
    }

}
