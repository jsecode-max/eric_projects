package com.example.helloworld;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

    private SharedPreferences perf;
    private SharedPreferences.Editor editor;

    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private CheckBox rememberPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        perf = PreferenceManager.getDefaultSharedPreferences(this);

        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);

        boolean isRemember = perf.getBoolean("remember_password", false);
        if (isRemember) {
            //第一次登陆的时候，就开始判断，肯定没有值在SharedPreference中，上面的默认是否记住：false
            String account = perf.getString("account", "");
            String password = perf.getString("password", "");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountEdit.getText().toString().equals("a") &&
                        passwordEdit.getText().toString().equals("1")) {
                    editor = perf.edit();
                    if (rememberPass.isChecked()) {
                        editor.putBoolean("remember_password", true);
                        editor.putString("account", accountEdit.getText().toString());
                        editor.putString("password", passwordEdit.getText().toString());
                    } else {
                        //清空SharedPreference，不管是否有内容
                        //如果将editor.putBoolean("remember_password", false);
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, Main10Activity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "account or password is invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
