package com.better.news.ui.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.better.news.R;
import com.better.news.data.sicence.ScienceOutBean;
import com.better.news.db.cache.NewsCache;
import com.better.news.db.cache.ScienceCache;
import com.better.news.db.table.NewsTable;
import com.better.news.db.table.ScienceTable;
import com.better.news.support.C;
import com.better.news.support.sax.RssItem;
import com.better.news.ui.base.BaseDetailActivity;

import butterknife.Bind;

/**
 * 加载一个网页展示信息
 *
 * @author Better
 */
public class DealActivity extends BaseDetailActivity {
    public static final String WEBVIEW_TITLE_NAME = "title_name";
    public static final String WEBVIEW_URL = "url";

    public static void startDealActivity(Activity activity, String url) {
        startDealActivity(activity, "", url);
    }
    public static void startDealActivity(Activity activity, String titleName, String url) {
        Intent intent = new Intent(activity, DealActivity.class);
        intent.putExtra(WEBVIEW_TITLE_NAME, titleName);
        intent.putExtra(WEBVIEW_URL, url);
        activity.startActivity(intent);
    }
    public static void startFromNews(Activity activity,RssItem item){
        Intent intent = new Intent(activity, DealActivity.class);
        intent.putExtra(C.EXTRA_BEAN, item);
        intent.putExtra(C.EXTRA_MODE,C.MODE_NEWS);
        activity.startActivity(intent);
    }
    public static void startFromScience(Activity activity,ScienceOutBean.ScienceBean bean){
        Intent intent = new Intent(activity, DealActivity.class);
        intent.putExtra(C.EXTRA_BEAN, bean);
        intent.putExtra(C.EXTRA_MODE,C.MODE_SCIENCE);
        activity.startActivity(intent);
    }

    @Bind(R.id.toolBar) Toolbar toolbar;
    @Bind(R.id.deal_web_view) WebView webView;
    @Bind(R.id.web_progress_bar) ProgressBar mProgressBar;
    private String mUrl, mTitleName;
    private RssItem mRssItem;
    private ScienceOutBean.ScienceBean mScenceBean;
    private NewsCache mNewsCache;
    private ScienceCache mScienceCache;
    private int mMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);
        log("标题：" + mTitleName + "DealUrl=" + mUrl);
        mContainer=findViewById(R.id.deal_continer);
        webView.loadUrl(mUrl);
    }

    @Override
    protected void initData() {
        super.initData();
//        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//        toolbar.setNavigationIcon(R.drawable.icon_navgation_back_white);
//        getSupportActionBar().setTitle("");
        setBackToolBar(toolbar);
        if (!TextUtils.isEmpty(mTitleName)) {
            toolbar.setTitle(mTitleName);
        }else{
            toolbar.setTitle("");
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setSupportMultipleWindows(false);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                //				view.loadUrl("file:///android_asset/error.html");
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
//				tvCenter.setText(title);
                super.onReceivedTitle(view, title);
            }
        });
    }

        /**
     * 有些机子会崩溃
     */
    @Override
    protected void onDestroy() {
        if (null != webView && null != webView.getParent()) {
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.stopLoading();
            webView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        mTitleName = getIntent().getStringExtra(WEBVIEW_TITLE_NAME);
        mUrl = getIntent().getStringExtra(WEBVIEW_URL);

        mMode=getIntent().getIntExtra(C.EXTRA_MODE,C.MODE_DEFAUTT);
        if (C.MODE_NEWS==mMode){
            mRssItem= (RssItem) getIntent().getSerializableExtra(C.EXTRA_BEAN);
            if(null!=mRssItem){
                mUrl=mRssItem.getLink();
            }
            mNewsCache=new NewsCache(null,null,null);
        }else if (C.MODE_SCIENCE==mMode){
            mScenceBean = (ScienceOutBean.ScienceBean) getIntent().getSerializableExtra(C.EXTRA_BEAN);
            if (null!= mScenceBean){
                mUrl= mScenceBean.getUrl();
            }
            mScienceCache =new ScienceCache(null,null,null);
        }
    }

    @Override
    protected void addToCollection() {
        if (C.MODE_NEWS==mMode){
            mNewsCache.execSQL(NewsTable.updateCollectionFlag(mRssItem.getTitle(),1));
            mNewsCache.addToCollection(mRssItem);
        }else if (C.MODE_SCIENCE==mMode){
            mScienceCache.execSQL(ScienceTable.updateCollectionFlag(mScenceBean.getTitle(),1));
            mScienceCache.addToCollection(mScenceBean);
        }
    }

    @Override
    protected void removeFromCollection() {
        if (C.MODE_NEWS==mMode){
            mNewsCache.execSQL(NewsTable.updateCollectionFlag(mRssItem.getTitle(), 0));
            mNewsCache.execSQL(NewsTable.deleteCollectionFlag(mRssItem.getTitle()));
        }else if (C.MODE_SCIENCE==mMode){
            mScienceCache.execSQL(ScienceTable.updateCollectionFlag(mScenceBean.getTitle(), 0));
            mScienceCache.execSQL(ScienceTable.deleteCollectionFlag(mScenceBean.getTitle()));
        }
    }
}
