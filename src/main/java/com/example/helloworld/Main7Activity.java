package com.example.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class Main7Activity extends AppCompatActivity {

    private List<Msg> msgList = new ArrayList<>();
    private EditText inpuText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);

        //初始化数据
        initMsgs();

        //一些控件的初始化
        send = (Button) findViewById(R.id.send);
        inpuText = (EditText) findViewById(R.id.input_text);
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);

        msgRecyclerView.scrollToPosition(msgList.size() - 1);   //初始化的对话，定位到最后一行

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inpuText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SEND);
                    msgList.add(msg);   //当输入消息时，添加到列表
                    adapter.notifyItemInserted(msgList.size() - 1); //列表有变动（insert）通知适配器
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);   //RecyclerView滚动展示到最后的一行上
                    inpuText.setText("");
                }
            }
        });
    }

    private void initMsgs() {
        Msg msg1 = new Msg("Hello guy.", Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2 = new Msg("Hello. Who is that?", Msg.TYPE_SEND);
        msgList.add(msg2);
        Msg msg3 = new Msg("This is Tom. Nice talking to you .", Msg.TYPE_RECEIVED);
        msgList.add(msg3);
    }
}
