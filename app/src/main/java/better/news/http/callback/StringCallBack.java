package better.news.http.callback;


import okhttp3.Response;


/**
 * Created by Better on 2016/3/14.
 * thk zhy
 */
public abstract class StringCallBack extends CallBack<String> {

    @Override
    public String parseNetworkResponse(Response response) throws Exception {
//        Utils.v("Better","StringCallBack=="+response.body().string());
        return response.body().string();
    }
}
