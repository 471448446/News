package com.better.news.ui.read;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.better.news.data.read.ReadBean;
import com.better.news.db.Cache;
import com.better.news.db.cache.ReadingCache;
import com.better.news.http.HttpUtil;
import com.better.news.http.api.ReadApi;
import com.better.news.http.callback.StringCallBack;
import com.better.news.support.C;
import com.better.news.support.util.JsonUtils;
import com.better.news.support.util.Utils;
import com.better.news.ui.base.SimpleRefreshFragment;
import com.better.news.ui.base.adapter.BaseRecyclerViewAdapter;

import java.util.HashMap;

import better.lib.recyclerview.RequestType;
import okhttp3.Call;

/**
 * Created by Better on 2016/3/23.
 */
public class ReadFragment extends SimpleRefreshFragment {
    public static Fragment getInstance(String[] urls,String categroy) {
        ReadFragment fr = new ReadFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray(C.EXTRA_URL, urls);
        bundle.putString(C.EXTRA_CATEGORY,categroy);
        fr.setArguments(bundle);
        return fr;
    }

    private String[] mUrls;
    private String categroy;
    private ReadBean mBean;

    private int start = 0 - ReadApi.Offset;
    private int count = -1;//调用的次数


    @Override
    protected Object getLastStartId() {
        count++;
        switch (count % ReadApi.PAGE_URL_COUNT) {
            case 0:
                return start + ReadApi.Offset;
            default:
                return start;
        }
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return getLinearLayoutManagerV();
    }

    @Override
    protected BaseRecyclerViewAdapter getAdapter() {
        return new ReadAdapter(getActivity(),mCache);
    }

    @Override
    protected Cache getCache() {
        return new ReadingCache(mHandler,categroy,mUrls);
    }

    @Override
    protected void asyncListInfo(final RequestType requestType) {
        mCache.loadFromNet(requestType);
//        if (null == mUrls || 0 == mUrls.length) return;
//        if (RequestType.DATA_REQUEST_UP_REFRESH != requestType) {
//            count = -1;
//        }
//        HashMap<String, Object> parms = new HashMap<>();
//
//        parms.put("start", getLastStartId());
//        parms.put("count", ReadApi.Offset);
//        for (String tag : mUrls) {
//            parms.put("tag",tag);
//            HttpUtil.getRequest(ReadApi.searchUrl, parms, new StringCallBack() {
//                @Override
//                public void onResponse(String response) {
//                    ReadBean bean= JsonUtils.fromJson(response,ReadBean.class);
//                    mBean=bean;
//                    if(null!=bean){
//                        postRequestSuccess(requestType,bean.getBooks(),"");
//                    }
//                }
//
//                @Override
//                public void onError(Call call, Exception e) {
//                    postRequestError(requestType,null,e.getMessage());
//                }
//            }, null);
//        }
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        mUrls = getArguments().getStringArray(C.EXTRA_URL);
        categroy=getArguments().getString(C.EXTRA_CATEGORY);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utils.v("CustomOkHttpClient", "onViewCreated()"+mUrls);
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.v("CustomOkHttpClient", "onResume()" + String.valueOf(null==mBean));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Utils.v("CustomOkHttpClient", "onDestroy()" + mUrls);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.v("CustomOkHttpClient", "onDestroy()" + mUrls);
    }
}
