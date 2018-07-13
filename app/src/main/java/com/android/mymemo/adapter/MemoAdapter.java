package com.android.mymemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.mymemo.R;
import com.android.mymemo.entity.Memo;

import java.util.List;

public class MemoAdapter extends ArrayAdapter {
    private final int resourceId;

    public MemoAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Memo memo = (Memo) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView tv_title = view.findViewById(R.id.memo_title);
        TextView tv_content = view.findViewById(R.id.memo_content);
        TextView tv_date = view.findViewById(R.id.memo_date);
        tv_title.setText(memo.getTitle());
        int clength = memo.getContent().length();
        int length = clength > 10 ? 10 : clength;
        tv_content.setText(memo.getContent().substring(0,clength)+"...");
        tv_date.setText(memo.getLastModifyDate().toLocaleString());
        return view;
    }
}
