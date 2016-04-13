package com.better.news.ui.days;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.better.news.MainApp;
import com.better.news.R;
import com.better.news.data.days.DaysBean;
import com.better.news.data.days.StoryDetailBean;
import com.better.news.db.cache.DayCache;
import com.better.news.db.table.DayTable;
import com.better.news.http.HttpUtil;
import com.better.news.http.api.DaysApi;
import com.better.news.http.callback.StringCallBack;
import com.better.news.support.C;
import com.better.news.support.util.ImageUtil;
import com.better.news.support.util.JsonUtils;
import com.better.news.ui.base.DetailImageActivity;

import okhttp3.Call;

public class DaysDetailActivity extends DetailImageActivity {

//    @Bind(R.id.days_detail_web)
//    WebView webView;
//    @Bind(R.id.days_detail_img)
//    ImageView image;
    private int id;
    private DaysBean.StoriesBean mIntentStories;
    private StoryDetailBean bean;
    private DayCache cache;

    public static void start(Activity activity, DaysBean.StoriesBean bean) {
        Intent intent = new Intent(activity, DaysDetailActivity.class);
        intent.putExtra(C.EXTRA_KEY, bean);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days_detail);
        if (null == mIntentStories || TextUtils.isEmpty(mIntentStories.getBody())) {
            asyncDetail();
        } else {
            waitPolicy.disappear();
            loadUIInfo(mIntentStories.getLargerImg(), mIntentStories.getBody());
        }
        cache = new DayCache(null);
    }


    @Override
    protected void asyncDetail() {
        waitPolicy.displayLoading();
        HttpUtil.getRequest(DaysApi.getStoryUrl(id), new StringCallBack() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                bean = JsonUtils.fromJson(response, StoryDetailBean.class);
                if (null != bean) {
                    loadUIInfo(bean.getImage(), bean.getBody());
                    DayCache.execSQL(DayTable.updateBodyContent(DayTable.NAME, bean.getTitle(), bean.getBody()));
                    DayCache.execSQL(DayTable.updateBodyContent(DayTable.COLLECTION_NAME, bean.getTitle(), bean.getBody()));
                    DayCache.execSQL(DayTable.updateLargePic(DayTable.NAME, bean.getTitle(), bean.getImage()));
                    DayCache.execSQL(DayTable.updateLargePic(DayTable.COLLECTION_NAME, bean.getTitle(), bean.getImage()));
                }
            }
        }, waitPolicy);
    }


    @Override
    protected void removeFromCollection() {
        DayCache.execSQL(DayTable.updateCollectionFlag(mIntentStories.getTitle(), 0));
        DayCache.execSQL(DayTable.deleteCollectionFlag(mIntentStories.getTitle()));
        setResult(C.COLLEXT_NO);
    }

    @Override
    protected void addToCollection() {
        DayCache.execSQL(DayTable.updateCollectionFlag(mIntentStories.getTitle(), 1));
        cache.addToCollection(mIntentStories);
        setResult(C.COLLECT_YES);
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        mIntentStories = (DaysBean.StoriesBean) getIntent().getSerializableExtra(C.EXTRA_KEY);
        id = mIntentStories.getId();
        isCollected = mIntentStories.isColleted();
    }

    protected void loadUIInfo(String imageUrl, String content) {
        ImageUtil.load(MainApp.getInstance(), imageUrl, image);//http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0823/3353.html
//        Glide.with(DaysDetailActivity.this).load(imageUrl).into(image);
        webView.loadDataWithBaseURL("file:///android_asset/", "<link rel=\"stylesheet\" type=\"text/css\" href=\"dailycss.css\" />" + content, "text/html", "utf-8", null);
    }
}
