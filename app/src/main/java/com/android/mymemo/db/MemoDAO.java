package com.android.mymemo.db;



import com.android.mymemo.entity.Memo;

import java.util.Date;
import java.util.List;

public interface MemoDAO {

    public void insertMemo(Memo memo);

    public void deleteMemo(String id);

    public void updateMemo(Memo memo);

    public Memo getSingleMemo(String id);

    public List<Memo> getAllMemos(String sort_way);

    public boolean isExists(String id);

    public void setNotificationDate(String id, Date date);
}
