package better.news.http;

import android.os.Handler;
import android.os.Looper;

import better.news.http.callback.CallBack;
import better.news.support.util.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import better.lib.waitpolicy.WaitPolicy;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.FormBody;


/**
 * Created by Better on 2016/3/13.
 */
public class CustomOkHttpClient {
    private static final String TAG="CustomOkHttpClient";
    private static CustomOkHttpClient instance;
    private okhttp3.OkHttpClient mDefaultClient;
    private Handler mDeliveryHandler;

//    private final int MSG_RESPONSE_FAILED = 100;
//    private final int MSG_RESPONSE_SUCCESS = MSG_RESPONSE_FAILED + 1;

    private CustomOkHttpClient() {
        okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder();
        builder.connectTimeout(20, TimeUnit.SECONDS);
        mDefaultClient = builder.build();
        mDefaultClient = new okhttp3.OkHttpClient();

        mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    public static CustomOkHttpClient getInstance() {
        if (null == instance) {
            synchronized (CustomOkHttpClient.class) {
                if (null == instance) {
                    instance = new CustomOkHttpClient();
                }
            }
        }
        return instance;
    }
    public void getRequest(String url, final CallBack callback,final WaitPolicy waitPolicy) {
        final Request request = new Request.Builder().url(url).build();
        sendRequest(request,callback,waitPolicy);
    }
    public void getRequest(String url,HashMap<String,Object> params, final CallBack callback,final WaitPolicy waitPolicy) {
        final Request request = new Request.Builder().url(appendGetParms(url,params)).build();
        sendRequest(request, callback, waitPolicy);
    }

    public void postRequest(String url,HashMap<String,String> params, final CallBack callback, WaitPolicy waitPolicy) {
        sendRequest(buildPostRequest(url, params),callback,waitPolicy);
    }
    private Request buildPostRequest(String url, HashMap<String,String> params) {
//        FormEncodingBuilder builder = new FormEncodingBuilder();
        FormBody.Builder builder=new FormBody.Builder();
        for (String key:params.keySet()){
            builder.add(key,params.get(key));
        }
        return new Request.Builder().url(url).post(builder.build()).build();
    }
    /**
     * 异步 请求
     *
     * @param request    添加context为 tag
     * @param callBack
     * @param waitPolicy
     */
    public void sendRequest(final Request request, final CallBack callBack, final WaitPolicy waitPolicy) {
        Utils.v(TAG,"执行请求 "+request.url());
        mDefaultClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendRequestFail(call, e, callBack, waitPolicy);
            }
            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    sendRequestSuccess(callBack, response, waitPolicy);
                } else {
                    sendRequestFail(call, null, callBack, waitPolicy);
                }
            }
        });
    }

    private void sendRequestSuccess(final CallBack callBack, final Response response, final WaitPolicy waitPolicy) {
        if (null == callBack) return;
        try {
            final Object str=callBack.parseNetworkResponse(response);
            mDeliveryHandler.post(new Runnable() {
                @Override
                public void run() {
                    Utils.v(TAG,"sendRequestSuccess（） str=="+str.toString());
                    callBack.onResponse(str);
                    if (null != waitPolicy) waitPolicy.disappear();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendRequestFail(final Call call, final IOException e, final CallBack callBack, final WaitPolicy waitPolicy) {
        if (null == callBack) return;
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onError(call, e);
                if (null != waitPolicy) waitPolicy.displayRetry(""+e.getMessage());
            }
        });
    }
    public static String appendGetParms(String url,HashMap<String,Object> parms){
        StringBuffer buffer=new StringBuffer(url);
        buffer.append("?");
        for (String key:parms.keySet()) {
            buffer.append(key).append("=").append(parms.get(key)).append("&");
        }
        buffer.delete(buffer.length() - 1, buffer.length() );
        Utils.v("Better", buffer.toString());
        return buffer.toString().trim();
    }

}
