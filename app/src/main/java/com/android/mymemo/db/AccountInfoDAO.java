package com.android.mymemo.db;


import com.android.mymemo.entity.AccountInfo;

public interface AccountInfoDAO {

    public void insertAccountInfo(AccountInfo accountInfo);

    public void updateAccountInfo(AccountInfo accountInfo);

    public void deleteAccountInfo();

    public boolean isExisted();

    public AccountInfo getAccountInfo();



}
