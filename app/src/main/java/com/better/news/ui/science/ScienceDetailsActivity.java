package com.better.news.ui.science;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.better.news.data.sicence.ScienceDetailsBean;
import com.better.news.http.HttpUtil;
import com.better.news.http.callback.StringCallBack;
import com.better.news.support.C;
import com.better.news.support.util.JsonUtils;
import com.better.news.ui.base.DetailImageActivity;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

public class ScienceDetailsActivity extends DetailImageActivity {

    private String loadUrl;
    /**
     *
     * @param activity
     * @param url  json数据网址
     */
    public static void start(Activity activity, String url) {
        Intent intent = new Intent(activity, ScienceDetailsActivity.class);
        intent.putExtra(C.EXTRA_URL, url);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asyncDetail();
    }

    @Override
    protected void asyncDetail() {
        HttpUtil.getRequest(loadUrl, new StringCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    ScienceDetailsBean bean = JsonUtils.fromJson(object.getString("result"), ScienceDetailsBean.class);
                    if (null != bean) {
                        Glide.with(ScienceDetailsActivity.this).load(bean.getSmall_image()).into(image);
                        webView.loadDataWithBaseURL("file:///android_asset/", "<link rel=\"stylesheet\" type=\"text/css\" href=\"dailycss.css\" />" + bean.getContent(), "text/html", "utf-8", null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Exception e) {

            }
        }, waitPolicy);
    }

    @Override
    protected void addToCollection() {

    }

    @Override
    protected void removeFromCollection() {

    }


    @Override
    protected void getArgs() {
        super.getArgs();
        loadUrl = getIntent().getStringExtra(C.EXTRA_URL);
    }
}
