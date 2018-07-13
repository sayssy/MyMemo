package com.android.mymemo.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Memo {
    /**
     * state:
     * 0- not available
     * 1- available
     */
    private String id;
    private String accID;
    private String title;
    private String content;
    private Date createDate;
    private Date lastModifyDate;
    private Date notificationDate;
    private int state;
    public transient boolean visible = true;
    public Memo() {
    }

    public Memo(String id, String accID, String title, String content, Date createDate, Date lastModifyDate, Date notificationDate, int state) {
        this.id = id;
        this.accID = accID;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.lastModifyDate = lastModifyDate;
        this.notificationDate = notificationDate;
        this.state = state;
    }

    //create
    public Memo(String accID, String title, String content) {
        //generate id
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = Calendar.getInstance().getTime();
        sb.append(sdf.format(date));
        sb.append(String.format("%06d", this.hashCode() % 1000000));

        this.id = sb.toString();
        this.accID = accID;
        this.title = title;
        this.content = content;
        this.createDate = date;
        this.lastModifyDate = date;
    }

    //update
    public Memo(String id, String title, String content, Date lastModifyDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.lastModifyDate = lastModifyDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccID() {
        return accID;
    }

    public void setAccID(String accID) {
        this.accID = accID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
