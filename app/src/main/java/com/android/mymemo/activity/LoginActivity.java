package com.android.mymemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.mymemo.R;
import com.android.mymemo.entity.Account;
import com.android.mymemo.volley.VolleyCallback;
import com.android.mymemo.volley.VolleyRequest;
import com.android.mymemo.volley.VolleyUtil;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {
    private String accid;
    private String accpw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init_config(getApplication());
        getSupportActionBar().setTitle("登录");
        //登录功能
        Button login_btn = findViewById(R.id.login_loginbtn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLogin()){
                    VolleyRequest vr = new VolleyRequest();
                    vr.login(accid, accpw, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Account account = new Gson().fromJson(result,Account.class);
                            if (account != null){
                                Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this,MemoActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(VolleyError volleyError) {
                            Toast.makeText(LoginActivity.this,"注册失败，网络错误",Toast.LENGTH_SHORT).show();
                        }
                    });

                }



            }
        });

        //注册功能
        Button register_btn = findViewById(R.id.login_registerbtn);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkLogin(){
        EditText et_id = findViewById(R.id.login_id);
        EditText et_pw = findViewById(R.id.login_pw);
        accid = et_id.getText().toString().trim();
        accpw = et_pw.getText().toString().trim();

        if (TextUtils.isEmpty(accid) || TextUtils.isEmpty(accpw)){
            Toast.makeText(LoginActivity.this,"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
        }else{
            if (accid.matches("^[A-Za-z0-9]{3,20}$") && accpw.matches("^[A-Za-z0-9]{6,20}$")){
                return true;
            }else{
                Toast.makeText(LoginActivity.this,"用户名或密码格式错误",Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    public void init_config(Context context) {
        VolleyUtil.getInstance().init(context);
    }
}
