package com.android.mymemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.mymemo.R;
import com.android.mymemo.adapter.MemoAdapter;
import com.android.mymemo.db.AccountDAOImpl;
import com.android.mymemo.db.MemoDAOImpl;
import com.android.mymemo.entity.Memo;
import com.android.mymemo.volley.VolleyCallback;
import com.android.mymemo.volley.VolleyRequest;
import com.android.volley.VolleyError;

import java.util.ArrayList;

public class CloudBinActivity extends AppCompatActivity {
    private ArrayList<Memo> memos = new ArrayList<Memo>();
    private MemoAdapter memoAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        getSupportActionBar().setTitle("云回收站");




        //备忘录列表
        memoAdapter = new MemoAdapter(
                CloudBinActivity.this, R.layout.memo_layout, memos);
        listView = findViewById(R.id.list_view);
        listView.setAdapter(memoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Memo memo = (Memo) listView.getItemAtPosition(position);
                String memo_id = memo.getId();
                Intent intent = new Intent(CloudBinActivity.this,MemoInfoActivity.class);
                intent.putExtra("function","cloud_bin_show");
                intent.putExtra("memo_obj",memo);
                startActivity(intent);
            }
        });
        VolleyRequest vr = new VolleyRequest();
        AccountDAOImpl adi = new AccountDAOImpl(this);
        vr.getAllDiscardedMemos(adi.getAccountInfo().getAccount().getId(), new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                memos.clear();
                ArrayList<Memo> cloud_bin_memos = VolleyRequest.parseJsonArray(result);
                memos.addAll(cloud_bin_memos);
                memoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError volleyError) {
                Toast.makeText(CloudBinActivity.this,"云回收站拉取失败",Toast.LENGTH_SHORT).show();
            }
        });


    }
}
