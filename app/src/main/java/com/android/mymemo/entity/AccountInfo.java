package com.android.mymemo.entity;

public class AccountInfo {
    private Account account;
    private String arrangement;
    private int autoSync;

    public AccountInfo() {
    }

    public AccountInfo(Account account, String arrangement, int autoSync) {
        this.account = account;
        this.arrangement = arrangement;
        this.autoSync = autoSync;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getArrangement() {
        return arrangement;
    }

    public void setArrangement(String arrangement) {
        this.arrangement = arrangement;
    }

    public int getAutoSync() {
        return autoSync;
    }

    public void setAutoSync(int autoSync) {
        this.autoSync = autoSync;
    }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "id=" + account.getId() +
                ", name=" + account.getName() +
                ", password" + account.getPassword() +
                ", arrangement='" + arrangement + '\'' +
                ", autoSync=" + autoSync +
                '}';
    }
}
