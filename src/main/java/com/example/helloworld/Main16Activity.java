package com.example.helloworld;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import util.HttpUtil;

public class Main16Activity extends AppCompatActivity {
    private WebView webView;
    private TextView responseText;
    public static final int UPDATE_MESSAGE = 1;
    private Handler handler = new Handler() {
        //重写handleMessage
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_MESSAGE:
                    responseText.setText("message from handler");
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main16);

        Button send_message_by_handler = (Button) findViewById(R.id.send_message);
        send_message_by_handler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = UPDATE_MESSAGE;
                        handler.sendMessage(message);   //发送消息出去，交给主线程的handleMessage处理
                    }
                }).start();
            }
        });

        Button goto_internet = (Button) findViewById(R.id.internet_go);
        webView = (WebView) findViewById(R.id.web_view);
        goto_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.getSettings().setJavaScriptEnabled(true);   //支持JavaScript
                webView.setWebViewClient(new WebViewClient());  //本地跳转
                webView.loadUrl("https://www.baidu.com");
            }
        });

        responseText = (TextView) findViewById(R.id.response_text);
        Button send_request = (Button) findViewById(R.id.send_request);
        send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestWithHttpURLConnection();
            }
        });

        Button send_request_by_OkHttp = (Button) findViewById(R.id.send_request_okhttp);
        send_request_by_OkHttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestWithOkHttp();
            }
        });

        Button send_request_by_util = (Button) findViewById(R.id.send_request_util);
        send_request_by_util.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.sendOkHttpRequest("https://www.baidu.com", new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        e.getMessage();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
//                        showResponse(response.toString());  //直返回状态信息
                        showResponse(response.body().string()); //返回内容
                    }
                });
            }
        });
    }

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                //POST方式唯一的区别：RequestBody
//                RequestBody requestBody = new FormBody.Builder()
//                        .add("username", "admin")
//                        .add("password", "123456")
//                        .build();
                Request request = new Request.Builder()
                        .url("https://www.baidu.com")
//                        .post(requestBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    showResponse(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendRequestWithHttpURLConnection() {
        //开线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://www.baidu.com");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();

//                    connection.setRequestMethod("POST");
//                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
//                    out.writeBytes("username=admin&password=123456");

                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    showResponse(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void showResponse(final String response) {
        //子线程中无法更新UI，Android提供了runOnThread调用主线程更新UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText("");
                responseText.setText(response);
            }
        });
    }
}
