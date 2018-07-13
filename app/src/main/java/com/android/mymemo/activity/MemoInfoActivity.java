package com.android.mymemo.activity;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mymemo.R;
import com.android.mymemo.dialog.TimePickerDialog;
import com.android.mymemo.entity.Memo;
import com.android.mymemo.volley.VolleyCallback;
import com.android.mymemo.volley.VolleyRequest;
import com.android.volley.VolleyError;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

import ren.qinc.edit.PerformEdit;

public class MemoInfoActivity extends AppCompatActivity {
    private  PerformEdit mPerformEdit;
    private EditText et_title;
    private TextView tv_wc;
    private EditText et_content;
    private final String DEFAULT_TIPS = "在此输入内容...";
    private String function;
    private TimePickerDialog mTimePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_info);

        getSupportActionBar().setTitle("信息");


        et_title = findViewById(R.id.info_title);
        tv_wc = findViewById(R.id.info_word_count);
        et_content = findViewById(R.id.info_content);
        Intent intent = getIntent();
        function = intent.getStringExtra("function");
        if (function.equals("create")) {
            mPerformEdit = new PerformEdit(et_content) {
                @Override
                protected void onTextChanged(Editable s) {
                    //文本发生改变,可以是用户输入或者是EditText.setText触发.(setDefaultText的时候不会回调)
                    super.onTextChanged(s);
                    tv_wc.setText(et_content.getText().toString().length()+"");
                }
            };
            tv_wc.setText(DEFAULT_TIPS.length()+"");
            mPerformEdit.setDefaultText(DEFAULT_TIPS);
        } else if (function.equals("update")){
            //TODO
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.action_undo:
                mPerformEdit.undo();
                break;
            case R.id.action_redo:
                mPerformEdit.redo();
                break;
            case R.id.action_submit:
                String title = et_title.getText().toString();
                String content = et_content.getText().toString();
                String accid = "ssy";//TODO


                if (function.equals("create")){
                    Memo memo = new Memo(accid,title,content);
                    if (checkTitle() && checkContent()) {
                        VolleyRequest vr = new VolleyRequest();
                        vr.addMemo(memo, new VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                boolean b = Boolean.parseBoolean(result);
                                if (b) {
                                    Toast.makeText(MemoInfoActivity.this, "云端创建成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MemoInfoActivity.this, MemoActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MemoInfoActivity.this, "云端创建失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(VolleyError volleyError) {
                                Toast.makeText(MemoInfoActivity.this, "创建失败，网络错误", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else if (function.equals("update")){
                    //TODO
                    if (checkTitle() && checkContent()) {

                    }
                }
            case R.id.menu_info_not:
                mTimePickerDialog = new TimePickerDialog(this);
                mTimePickerDialog.showDateAndTimePickerDialog(new TimePickerDialog.TimePickerDialogInterface() {
                    @Override
                    public void positiveListener() {

                        int hour = mTimePickerDialog.getHour();
                        int minute = mTimePickerDialog.getMinute();
                        int year = mTimePickerDialog.getYear();
                        int month = mTimePickerDialog.getMonth();
                        int day = mTimePickerDialog.getDay();
                        Calendar c = Calendar.getInstance();
                        c.set(year,month-1,day,hour,minute,0);
                        Date date = c.getTime();


                    }

                    @Override
                    public void negativeListener() {

                    }
                });
                break;
            case R.id.menu_info_del:
                break;

        }

        return true;
    }

    private boolean checkTitle(){
        String title = et_title.getText().toString();
        if (!TextUtils.isEmpty(title)){
            if (title.length() <= 20){
                return true;
            }else{
                Toast.makeText(MemoInfoActivity.this,"标题长度不能大于20",Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(MemoInfoActivity.this,"标题不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean checkContent(){
        String content = et_content.getText().toString();

        if (content.length() <= 1000){
            return true;
        }else{
            Toast.makeText(MemoInfoActivity.this,"正文长度不能大于1000",Toast.LENGTH_SHORT).show();
            return false;
        }

    }


}
