package com.android.mymemo.volley;

import android.util.Log;

import com.android.mymemo.entity.Account;
import com.android.mymemo.entity.Memo;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VolleyRequest {

    //URL
    public static final String URL_BASE_ACCOUNT_SERVLET = "http://119.23.74.157:8080/AccountServlet";
    public static final String URL_BASE_MEMO_SERVLET = "http://119.23.74.157:8080/MemoServlet";
    //public static final String URL_BASE_ACCOUNT_SERVLET = "http://192.168.1.115:8080/AccountServlet";
    //public static final String URL_BASE_MEMO_SERVLET = "http://192.168.1.115:8080/MemoServlet";

    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    //params pairs
    private Map<String, String> map = new HashMap<String, String>();
    private StringBuilder url = null;
    //Callback
    private VolleyCallback callback;

    //Response
    private Response.Listener<String> response = new Response.Listener<String>() {
        @Override
        public void onResponse(String result) {
            Log.i("result", result);
            callback.onSuccess(result);
        }
    };
    //Error Listener
    private Response.ErrorListener error = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e("error", volleyError.toString());
            callback.onError(volleyError);
        }
    };

    /**
     * Login
     * @param id
     * @param password
     * @param callback
     */
    public void login(String id, String password, final VolleyCallback callback) {
        this.callback = callback;
        map.put("function", "login");
        map.put("id", id);
        map.put("password", password);
        VolleyUtil.getInstance().postString(URL_BASE_ACCOUNT_SERVLET, map, response, error);
    }

    /**
     * Register
     * @param account
     * @param callback
     */
    public void register(Account account, final VolleyCallback callback) {
        this.callback = callback;
        map.put("function", "register");
        map.put("obj", gson.toJson(account));
        VolleyUtil.getInstance().postString(URL_BASE_ACCOUNT_SERVLET, map, response, error);
    }

    /**
     * Set Name
     * @param id
     * @param name
     * @param callback
     */
    public void setName(String id, String name, final VolleyCallback callback) {
        this.callback = callback;
        map.put("function", "setName");
        map.put("id", id);
        map.put("name", name);
        VolleyUtil.getInstance().postString(URL_BASE_ACCOUNT_SERVLET, map, response, error);
    }

    /**
     * Set Password
     * @param id
     * @param password
     * @param callback
     */
    public void setPassword(String id, String password, final VolleyCallback callback) {
        this.callback = callback;
        map.put("function", "setPassword");
        map.put("id", id);
        map.put("password", password);
        VolleyUtil.getInstance().postString(URL_BASE_ACCOUNT_SERVLET, map, response, error);
    }

    /**
     * Add Memo
     * @param memo
     * @param callback
     */
    public void addMemo(Memo memo, final VolleyCallback callback) {
        this.callback = callback;
        map.put("function", "addMemo");
        map.put("obj", gson.toJson(memo));
        Log.d("obj",map.get("obj").toString());
        VolleyUtil.getInstance().postString(URL_BASE_MEMO_SERVLET, map, response, error);
        //VolleyUtil.getInstance().postString("http://192.168.1.115:8080/MemoServer/MemoServlet", map, response, error);
    }

    /**
     * Update Memo
     * @param memo
     * @param callback
     */
    public void updateMemo(Memo memo, final VolleyCallback callback) {
        this.callback = callback;
        map.put("function", "updateMemo");
        map.put("obj", gson.toJson(memo));
        VolleyUtil.getInstance().postString(URL_BASE_MEMO_SERVLET, map, response, error);
    }

    /**
     * Synchronize Memos
     * @param list
     * @param callback
     */
    public void synchronizeMemos(ArrayList<Memo> list, final VolleyCallback callback) {
        this.callback = callback;
        if (list == null) {
            list =  new ArrayList<Memo>();
        }
        map.put("function", "synchronizeMemos");
        map.put("list", gson.toJson(list));
        VolleyUtil.getInstance().postString(URL_BASE_MEMO_SERVLET, map, response, error);
    }

    /**
     * Delete Memo
     * @param id
     * @param callback
     */
    public void delMemo(String id, final VolleyCallback callback) {
        this.callback = callback;
        url = new StringBuilder(URL_BASE_MEMO_SERVLET);
        url.append("?function=delMemo&id=").append(id);
        VolleyUtil.getInstance().getString(url.toString(), response, error);
    }

    /**
     * Set Notification Date
     * @param id
     * @param date
     * @param callback
     */
    public void setNotificationDate(String id, Date date, final VolleyCallback callback) {
        this.callback = callback;
        map.put("function", "setNotificationDate");
        map.put("date", date.toString());
        VolleyUtil.getInstance().postString(URL_BASE_MEMO_SERVLET, map, response, error);
    }

    /**
     * Get All My Memos
     * @param accId
     * @param callback
     */
    public void getAllMyMemos(String accId, final VolleyCallback callback) {
        this.callback = callback;
        url = new StringBuilder(URL_BASE_MEMO_SERVLET);
        url.append("?function=getAllMyMemos&accId=").append(accId);
        VolleyUtil.getInstance().getString(url.toString(), response, error);
    }

    /**
     * Get All Discarded Memos
     * @param accId
     * @param callback
     */
    public void getAllDiscardedMemos(String accId, final VolleyCallback callback) {
        this.callback = callback;
        url = new StringBuilder(URL_BASE_MEMO_SERVLET);
        url.append("?function=getAllDiscardedMemos&accId=").append(accId);
        VolleyUtil.getInstance().getString(url.toString(), response, error);
    }
//

    /**
     * Parse Json Array
     * @param json
     * @return
     */
    public static ArrayList<Memo> parseJsonArray(String json) {
        Log.d("JSON",json);
        JsonParser parser = new JsonParser();
        if (json == null || json.length() < 1) {
            json = "[]";
        }
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();
        ArrayList<Memo> list = new ArrayList<Memo>();

        for (JsonElement memo : jsonArray) {
            Memo m = gson.fromJson(memo, Memo.class);
            list.add(m);
        }
        return list;
    }


}
