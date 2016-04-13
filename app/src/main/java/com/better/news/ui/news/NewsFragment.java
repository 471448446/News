package com.better.news.ui.news;

import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.better.news.R;
import com.better.news.db.Cache;
import com.better.news.db.cache.NewsCache;
import com.better.news.http.HttpUtil;
import com.better.news.http.callback.StringCallBack;
import com.better.news.support.C;
import com.better.news.support.sax.RssFeed;
import com.better.news.ui.base.adapter.BaseRecyclerViewAdapter;
import com.better.news.ui.base.SimpleRefreshFragment;
import com.better.news.support.util.Utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import better.lib.recyclerview.RequestType;
import okhttp3.Call;

/**
 * Created by Better on 2016/3/15.
 */
public class NewsFragment extends SimpleRefreshFragment {
    private static final String TAG_URL="tag_url";
    private String url;
    private String category;
    public static Fragment newInstace(String url,String category){
        NewsFragment fragment=new NewsFragment();
        Bundle bundle=new Bundle();
        bundle.putString(TAG_URL,url);
        bundle.putString(C.EXTRA_CATEGORY,category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void getArgs() {
        url=getArguments().getString(TAG_URL);
        category= getArguments().getString(C.EXTRA_CATEGORY);
    }

    @Override
    protected Object getLastStartId() {
        return null;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return linearLayoutManager;
    }

    @Override
    protected BaseRecyclerViewAdapter getAdapter() {
        return new NewsAdapter(getActivity(),mCache);
    }

    @Override
    protected Cache getCache() {
        return new NewsCache(mHandler,category,url);
    }

    @Override
    protected void asyncListInfo(final RequestType dataRequestInit) {
        mCache.loadFromNet(dataRequestInit);
//        switch (dataRequestInit){
//            case DATA_REQUEST_INIT:
//            case DATA_REQUEST_DOWN_REFRESH:
//                HttpUtil.getRequest(url, new StringCallBack() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        postRequestError(dataRequestInit,null,e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//                        InputStream is=new ByteArrayInputStream(response.getBytes());
//                        RssFeed rssFeed= Utils.getFeed(response);
//                        if(null!=rssFeed){
//                            postRequestSuccess(dataRequestInit,rssFeed.getItems(),"");
//                        }
//                    }
//                },null);
//                break;
//            default:
//                postRequestSuccess(dataRequestInit, new ArrayList<Object>(), getString(R.string.str_loading_footer_all));
//                break;
//        }
    }
}
