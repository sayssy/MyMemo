package com.android.mymemo.volley;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Volley Util
 */
public class VolleyUtil {
    private static RequestQueue requestQueue;

    private VolleyUtil() {

    }

    public static VolleyUtil getInstance() {
        return VolleyUtilHold.getVolleyUtilInstance();
    }

    public void init(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    private static final class VolleyUtilHold {
        public static VolleyUtil getVolleyUtilInstance() {
            return new VolleyUtil();
        }
    }

    /**
     *
     * @param url
     * @param success
     * @param error
     */
    public void getString(String url, Response.Listener<String> success, Response.ErrorListener error) {
        StringRequest stringRequest
                = new StringRequest(Request.Method.GET, url, success, error){
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String parsed;
                try {
                    //parsed = new String(response.data, "UTF-8");
                    parsed = new String(response.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    parsed = new String(response.data);
                }
                return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
            }

        };

        stringRequest.setTag("Request");
        requestQueue.add(stringRequest);
    }

    /**
     *
     * @param url
     * @param map
     * @param success
     * @param error
     */
    public void postString(String url, final Map<String, String> map, Response.Listener<String> success, Response.ErrorListener error) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url, success, error) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String parsed;
                try {
                    //parsed = new String(response.data, "UTF-8");
                    parsed = new String(response.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    parsed = new String(response.data);
                }
                return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        stringRequest.setTag("Request");
        requestQueue.add(stringRequest);
    }


    /**
     * 取消请求
     *
     * @param path 请求路径
     */
    public void removeRequest(String path) {
        requestQueue.cancelAll(path);
    }

}
