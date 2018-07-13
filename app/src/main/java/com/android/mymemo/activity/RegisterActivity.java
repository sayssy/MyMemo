package com.android.mymemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.BoringLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.mymemo.R;
import com.android.mymemo.entity.Account;
import com.android.mymemo.volley.VolleyCallback;
import com.android.mymemo.volley.VolleyRequest;
import com.android.mymemo.volley.VolleyUtil;
import com.android.volley.VolleyError;

public class RegisterActivity extends AppCompatActivity {
    private String accid;
    private String accname;
    private String accpw;
    private String accpwc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button register_btn = findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkRegister()){
                    Account account = new Account(accid,accname,accpw);
                    VolleyRequest vr = new VolleyRequest();
                    vr.register(account, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            boolean b = Boolean.parseBoolean(result);
                            if (b){
                                Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(RegisterActivity.this,"注册失败，服务器错误",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(VolleyError volleyError) {
                            Toast.makeText(RegisterActivity.this,"注册失败，网络错误",Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });
    }

    private boolean checkRegister(){
        EditText et_id = findViewById(R.id.register_id);
        EditText et_nm = findViewById(R.id.register_name);
        EditText et_pw = findViewById(R.id.register_pw);
        EditText et_pwc = findViewById(R.id.register_pwcheck);
        accid = et_id.getText().toString().trim();
        accname = et_nm.getText().toString().trim();
        accpw = et_pw.getText().toString().trim();
        accpwc = et_pwc.getText().toString().trim();

        if (TextUtils.isEmpty(accid) || TextUtils.isEmpty(accpw) || TextUtils.isEmpty(accname) || TextUtils.isEmpty(accpwc)){
            Toast.makeText(RegisterActivity.this,"账号、姓名和密码不能为空",Toast.LENGTH_SHORT).show();
        }else{
            if (accid.matches("^[A-Za-z0-9]{3,20}$") && accpw.matches("^[A-Za-z0-9]{6,20}$") && accname.matches("^.{1,20}$")){
                if (accpw.equals(accpwc)){
                    return true;
                }else{
                    Toast.makeText(RegisterActivity.this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(RegisterActivity.this,"账号、姓名或密码格式错误",Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }


}
