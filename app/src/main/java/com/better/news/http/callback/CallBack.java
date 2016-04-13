package com.better.news.http.callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Better on 2016/3/14.
 */
public abstract class CallBack<T>{
    /**
     * Thread Pool Thread
     *
     * @param response
     */
    public abstract T parseNetworkResponse(Response response) throws Exception;

    public abstract void onResponse(T response);
    public abstract void onError(Call call, Exception e);
}
