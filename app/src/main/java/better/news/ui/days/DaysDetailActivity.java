package better.news.ui.days;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import better.news.MainApp;
import better.news.data.days.DaysBean;
import better.news.data.days.StoryDetailBean;
import better.news.db.cache.DayCache;
import better.news.db.table.DayTable;
import better.news.http.HttpUtil;
import better.news.http.api.DaysApi;
import better.news.http.callback.StringCallBack;
import better.news.support.C;
import better.news.support.util.ImageUtil;
import better.news.support.util.JsonUtils;
import better.news.ui.base.DetailImageActivity;
import better.news.ui.base.SimpleRefreshFragment;
import okhttp3.Call;

public class DaysDetailActivity extends DetailImageActivity {

    private int id;
    private DaysBean.StoriesBean mIntentStories;
    private StoryDetailBean bean;
    private DayCache cache;

    public static void start(Fragment activity, DaysBean.StoriesBean bean) {
        Intent intent = new Intent(activity.getActivity(), DaysDetailActivity.class);
        intent.putExtra(C.EXTRA_KEY, bean);
        activity.startActivityForResult(intent, SimpleRefreshFragment.Req_Code);
    }

    @Override
    protected void initData() {
        super.initData();
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
    protected void addToCollection() {
        DayCache.execSQL(DayTable.updateCollectionFlag(mIntentStories.getTitle(), 1));
        cache.addToCollection(mIntentStories);
        Intent intent = new Intent();
        intent.putExtra(C.EXTRA_KEY, mIntentStories.getTitle());
        setResult(C.day_collect_add, intent);
    }
    @Override
       protected void removeFromCollection() {
        DayCache.execSQL(DayTable.updateCollectionFlag(mIntentStories.getTitle(), 0));
        DayCache.execSQL(DayTable.deleteCollectionFlag(mIntentStories.getTitle()));
        Intent intent = new Intent();
        intent.putExtra(C.EXTRA_KEY, mIntentStories.getTitle());
        setResult(C.day_collect_cancle, intent);
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
