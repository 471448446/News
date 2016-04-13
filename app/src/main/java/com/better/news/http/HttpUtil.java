package com.better.news.http;

import android.content.Context;
import android.net.ConnectivityManager;

import com.better.news.MainApp;
import com.better.news.http.callback.CallBack;

import java.util.HashMap;

import better.lib.waitpolicy.WaitPolicy;
import okhttp3.FormBody;
import okhttp3.Request;

/**
 * Created by Better on 2016/3/16.
 */
public class HttpUtil {
    public static void getRequest(String url, final CallBack callback,final WaitPolicy waitPolicy) {
//        final Request request = new Request.Builder().url(url).build();
        CustomOkHttpClient.getInstance().getRequest(url,callback,waitPolicy);
    }
    public static void getRequest(String url,HashMap<String,Object> parms, final CallBack callback,final WaitPolicy waitPolicy) {
//        final Request request = new Request.Builder().url(url).build();
        CustomOkHttpClient.getInstance().getRequest(url,parms,callback,waitPolicy);
    }

    public static void postRequest(String url,HashMap<String,String> params, final CallBack callback, WaitPolicy waitPolicy) {
        CustomOkHttpClient.getInstance().postRequest(url, params, callback, waitPolicy);
    }

    /**
     * 异步 请求
     *
     * @param request    添加context为 tag
     * @param callBack
     * @param waitPolicy
     */
    public static void sendRequest(final Request request, final CallBack callBack, final WaitPolicy waitPolicy) {
        CustomOkHttpClient.getInstance().sendRequest(request, callBack, waitPolicy);
    }
    public static boolean isWifiNet(){
        ConnectivityManager cm = (ConnectivityManager) MainApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
            return (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI);
        }
        return  false;
    }
}
