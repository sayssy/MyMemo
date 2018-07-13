package com.android.mymemo.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.mymemo.R;
import com.android.mymemo.adapter.MemoAdapter;
import com.android.mymemo.entity.Memo;
import com.android.mymemo.volley.VolleyCallback;
import com.android.mymemo.volley.VolleyRequest;
import com.android.mymemo.volley.VolleyUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

public class MemoActivity extends AppCompatActivity {
    private ArrayList<Memo> memos = new ArrayList<Memo>();
    private MemoAdapter memoAdapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        getSupportActionBar().setTitle("云备忘录");
        //刷新
        FloatingActionButton fab_refresh = (FloatingActionButton) findViewById(R.id.fab_refresh);
        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MemoActivity.this,"刷新便签",Toast.LENGTH_SHORT).show();
            }
        });


        //添加便签按钮
        FloatingActionButton fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MemoActivity.this,MemoInfoActivity.class);
                intent.putExtra("function","create");
                startActivity(intent);
            }
        });

        //搜索
        EditText et_search = findViewById(R.id.memo_search_keyword);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (Memo memo : memos){
                    if (TextUtils.isEmpty(s) || memo.getTitle().contains(s) || memo.getContent().contains(s)){
                        memo.visible = true;
                    }else{
                        memo.visible = false;
                    }
                    memoAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //备忘录列表
        memoAdapter = new MemoAdapter(
                MemoActivity.this, R.layout.memo_layout, memos);
        listView = findViewById(R.id.list_view);
        listView.setAdapter(memoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Memo memo = (Memo) listView.getItemAtPosition(position);
                String memo_id = memo.getId();
                Toast.makeText(MemoActivity.this,memo_id,Toast.LENGTH_SHORT).show();
            }
        });
        //addMemo();
    }

    @Override
    protected void onResume() {
        syncMemoFromCloud();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.memo_menu,menu); //通过getMenuInflater()方法得到MenuInflater对象，再调用它的inflate()方法就可以给当前活动创建菜单了，第一个参数：用于指定我们通过哪一个资源文件来创建菜单；第二个参数：用于指定我们的菜单项将添加到哪一个Menu对象当中。
        return true; // true：允许创建的菜单显示出来，false：创建的菜单将无法显示。
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.memo_menu_sortByM:
                item.setChecked(true);
                break;
            case R.id.memo_menu_sortByC:
                item.setChecked(true);
                break;
            case R.id.memo_menu_autosync:
                item.setChecked(!item.isChecked());
                break;
            case R.id.memo_menu_bin:

                break;
            case R.id.memo_menu_logout:
                Toast.makeText(this, "你点击了 4！", Toast.LENGTH_SHORT).show();
                break;


            default:
                break;
        }

        return true;
    }



    private void syncMemoFromCloud(){

        VolleyRequest vr = new VolleyRequest();
        vr.getAllMyMemos("ssy", new VolleyCallback() { //TODO
            @Override
            public void onSuccess(String result) {
                memos.clear();
                memos.addAll(VolleyRequest.parseJsonArray(result));
            }

            @Override
            public void onError(VolleyError volleyError) {
                Toast.makeText(MemoActivity.this,"自动同步失败，网络错误",Toast.LENGTH_SHORT).show();
            }
        });






    }

}
