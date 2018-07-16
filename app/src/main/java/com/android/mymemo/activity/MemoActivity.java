package com.android.mymemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.mymemo.R;
import com.android.mymemo.adapter.MemoAdapter;
import com.android.mymemo.db.AccountDAOImpl;
import com.android.mymemo.db.MemoDAOImpl;
import com.android.mymemo.entity.AccountInfo;
import com.android.mymemo.entity.Memo;
import com.android.mymemo.utility.DataUpdate;
import com.android.mymemo.volley.VolleyCallback;
import com.android.mymemo.volley.VolleyRequest;
import com.android.mymemo.volley.VolleyUtil;
import com.android.volley.VolleyError;

import java.util.ArrayList;

public class MemoActivity extends AppCompatActivity {
    private ArrayList<Memo> memos = new ArrayList<Memo>();
    private MemoAdapter memoAdapter;
    private ListView listView;
    private static DataUpdate dataUpdate = new DataUpdate(false);

    public static void updateData(){
        dataUpdate.setDataUpdated(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        init_config(getApplication());

        getSupportActionBar().setTitle("云备忘录");
        //刷新
        FloatingActionButton fab_refresh = (FloatingActionButton) findViewById(R.id.fab_refresh);
        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MemoDAOImpl mdi = new MemoDAOImpl(MemoActivity.this);
                if (mdi.getAllMemos(null,true).isEmpty()){
                    //如果本地数据库为空，就从云端拉取数据
                    syncMemoFromCloud();
                }else{
                    //否则从云端同步到本地
                    syncMemoFromLocal();
                }

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
                Intent intent = new Intent(MemoActivity.this,MemoInfoActivity.class);
                intent.putExtra("function","update");
                intent.putExtra("memo_id",memo_id);
                startActivity(intent);
            }
        });

        //检查是否有登陆
        AccountDAOImpl adi = new AccountDAOImpl(this);
        if (!adi.isExisted()) {
            Intent intent = new Intent(MemoActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }



    }

    private void show(){
        AccountDAOImpl adi = new AccountDAOImpl(this);
        if (adi.isExisted()){
            MemoDAOImpl mdi = new MemoDAOImpl(this);
            memos.clear();
            ArrayList<Memo> memoList = mdi.getAllMemos(adi.getAccountInfo().getArrangement(),false);
            memos.addAll(memoList);
            memoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {

        if (dataUpdate.isDataUpdated()) {
            AccountDAOImpl adi = new AccountDAOImpl(this);
            MemoDAOImpl mdi = new MemoDAOImpl(this);
            if (adi.isExisted()) {
                if (adi.getAccountInfo().getAutoSync() == 1) {
                    if (mdi.getAllMemos(null, true).isEmpty()) {
                        syncMemoFromCloud();
                    } else {
                        syncMemoFromLocal();
                    }
                }
                show();
            }
        }

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.memo_menu,menu);

        AccountDAOImpl adi = new AccountDAOImpl(this);
        if (adi.isExisted()) {
            AccountInfo ai = adi.getAccountInfo();
            String sort_way = ai.getArrangement();
            int autosync = ai.getAutoSync();

            if (sort_way.equals("M")) {
                menu.findItem(R.id.memo_menu_sortByM).setChecked(true);
            } else if (sort_way.equals("C")) {
                menu.findItem(R.id.memo_menu_sortByC).setChecked(true);
            }
            if (autosync == 1) {
                menu.findItem(R.id.memo_menu_autosync).setChecked(true);
            } else {
                menu.findItem(R.id.memo_menu_autosync).setChecked(false);
            }
        }
        return true; // true：允许创建的菜单显示出来，false：创建的菜单将无法显示。
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MemoDAOImpl mdi = new MemoDAOImpl(this);
        AccountDAOImpl adi = new AccountDAOImpl(this);
        AccountInfo ai = adi.getAccountInfo();
        switch (item.getItemId()){
            case R.id.memo_menu_sortByM:
                ai.setArrangement("M");
                adi.updateAccountInfo(ai);
                show();
                item.setChecked(true);
                break;
            case R.id.memo_menu_sortByC:
                ai.setArrangement("C");
                adi.updateAccountInfo(ai);
                show();
                item.setChecked(true);
                break;
            case R.id.memo_menu_autosync:
                if (ai.getAutoSync() == 1){
                    ai.setAutoSync(0);
                    adi.updateAccountInfo(ai);
                    item.setChecked(false);
                }else{
                    ai.setAutoSync(1);
                    adi.updateAccountInfo(ai);
                    item.setChecked(true);
                }
                break;
            case R.id.memo_menu_bin:
                Intent intent_cb = new Intent(MemoActivity.this,CloudBinActivity.class);
                startActivity(intent_cb);
                break;
            case R.id.memo_menu_logout:
                adi.deleteAccountInfo();
                mdi.deleteAllMemos();
                Intent intent = new Intent(MemoActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }
    private void syncMemoFromLocal(){
        MemoDAOImpl mdi = new MemoDAOImpl(MemoActivity.this);
        ArrayList<Memo> memoList = mdi.getAllMemos(null,true);

        VolleyRequest vr = new VolleyRequest();

        vr.synchronizeMemos(memoList, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d("====","result="+result);
                Boolean b = Boolean.parseBoolean(result);
                if (b){
                    Toast.makeText(MemoActivity.this,"同步成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MemoActivity.this,"同步失败",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(VolleyError volleyError) {
                Toast.makeText(MemoActivity.this,"同步至服务器失败，网络错误",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void syncMemoFromCloud(){

        VolleyRequest vr = new VolleyRequest();
        AccountDAOImpl adi = new AccountDAOImpl(this);
        String accid = adi.getAccountInfo().getAccount().getId();
        vr.getAllMyMemos(accid, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                ArrayList<Memo> cloud_memos = VolleyRequest.parseJsonArray(result);
                MemoDAOImpl mdi = new MemoDAOImpl(MemoActivity.this);
                if (cloud_memos != null && !cloud_memos.isEmpty()){
                    for (Memo memo : cloud_memos){
                        mdi.insertMemo(memo);
                    }
                }
                Toast.makeText(MemoActivity.this,"从服务器拉取数据成功",Toast.LENGTH_SHORT).show();
                show();

            }

            @Override
            public void onError(VolleyError volleyError) {
                Toast.makeText(MemoActivity.this,"从服务器同步失败，网络错误",Toast.LENGTH_SHORT).show();
            }
        });






    }
    public void init_config(Context context) {
        VolleyUtil.getInstance().init(context);
    }

}
