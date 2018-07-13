package com.android.mymemo.activity;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.provider.AlarmClock;
import android.support.constraint.Group;
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
import com.android.mymemo.db.AccountDAOImpl;
import com.android.mymemo.db.MemoDAOImpl;
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
    private TextView tv_cd;
    private EditText et_content;
    private final String DEFAULT_TIPS = "在此输入内容...";
    private String function;
    private TimePickerDialog mTimePickerDialog;
    private Memo current_memo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_info);

        getSupportActionBar().setTitle("信息");


        et_title = findViewById(R.id.info_title);
        tv_wc = findViewById(R.id.info_word_count);
        tv_cd = findViewById(R.id.info_create_date);
        et_content = findViewById(R.id.info_content);
        Intent intent = getIntent();
        function = intent.getStringExtra("function");

        mPerformEdit = new PerformEdit(et_content) {
            @Override
            protected void onTextChanged(Editable s) {
                //文本发生改变,可以是用户输入或者是EditText.setText触发.(setDefaultText的时候不会回调)
                super.onTextChanged(s);
                tv_wc.setText(et_content.getText().toString().length()+"");
            }
        };
        if (function.equals("create")) {
            et_title.setText("标题");
            tv_wc.setText(DEFAULT_TIPS.length()+"");
            mPerformEdit.setDefaultText(DEFAULT_TIPS);
        } else if (function.equals("update")){
            String memo_id = intent.getStringExtra("memo_id");
            MemoDAOImpl mdi = new MemoDAOImpl(this);
            current_memo = mdi.getSingleMemo(memo_id);
            et_title.setText(current_memo.getTitle());
            tv_cd.setText(current_memo.getCreateDate().toLocaleString());
            mPerformEdit.setDefaultText(current_memo.getContent());
            tv_wc.setText(current_memo.getContent().length()+"");

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editor,menu);

        if (function.equals("create")){
            menu.findItem(R.id.menu_info_del).setVisible(false);
            menu.findItem(R.id.menu_info_not).setVisible(false);
            menu.findItem(R.id.menu_info_del).setEnabled(false);
            menu.findItem(R.id.menu_info_not).setEnabled(false);
        }else{
            //menu.findItem(R.id.menu_info_group).setVisible(true);
        }
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
                AccountDAOImpl adi = new AccountDAOImpl(this);
                String accid = adi.getAccountInfo().getAccount().getId();


                if (function.equals("create")){
                    Memo memo = new Memo(accid,title,content);
                    if (checkTitle() && checkContent()) {
                        MemoDAOImpl mdi = new MemoDAOImpl(this);
                        mdi.insertMemo(memo);
                    }else{
                        break;
                    }
                } else if (function.equals("update")){
                    Memo memo = new Memo(current_memo.getId(),title,content,Calendar.getInstance().getTime());
                    if (checkTitle() && checkContent()) {
                        MemoDAOImpl mdi = new MemoDAOImpl(this);
                        mdi.updateMemo(memo);
                    }else{
                        break;
                    }
                }
                Intent intent = new Intent(MemoInfoActivity.this,MemoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
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

                            //TODO



                    }

                    @Override
                    public void negativeListener() {

                    }
                });
                break;
            case R.id.menu_info_del:
                MemoDAOImpl mdi = new MemoDAOImpl(this);
                mdi.deleteMemo(current_memo.getId());
                Intent intent2 = new Intent(MemoInfoActivity.this,MemoActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
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

        if (content.length() <= 10000){
            return true;
        }else{
            Toast.makeText(MemoInfoActivity.this,"正文长度不能大于10000",Toast.LENGTH_SHORT).show();
            return false;
        }

    }


}
