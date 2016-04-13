package com.better.news.ui.science;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.better.news.R;
import com.better.news.data.sicence.ScienceOutBean;
import com.better.news.db.Cache;
import com.better.news.db.cache.ScienceCache;
import com.better.news.http.HttpUtil;
import com.better.news.http.api.ScienceApi;
import com.better.news.http.callback.StringCallBack;
import com.better.news.support.C;
import com.better.news.support.util.JsonUtils;
import com.better.news.ui.base.SimpleRefreshFragment;
import com.better.news.ui.base.adapter.BaseRecyclerViewAdapter;

import better.lib.recyclerview.RequestType;
import okhttp3.Call;

/**
 * Created by Better on 2016/3/20.
 */
public class ScienceFragment extends SimpleRefreshFragment{
    public static Fragment newInstance(int pos){
        ScienceFragment fr=new ScienceFragment();
        Bundle bundle=new Bundle();
        bundle.putInt(C.EXTRA_POSITION, pos);
        fr.setArguments(bundle);
        return  fr;
    }
    private String url,category;

    @Override
    protected Object getLastStartId() {
        return null;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return getLinearLayoutManagerV();
    }

    @Override
    protected BaseRecyclerViewAdapter getAdapter() {
        return new ScienceAdapter(getActivity());
    }

    @Override
    protected Cache getCache() {
        return new ScienceCache(mHandler,category,url);
    }

    @Override
    protected void asyncListInfo(final RequestType requestType) {
        mCache.loadFromNet(requestType);
//        switch (requestType){
//            case DATA_REQUEST_UP_REFRESH:
//                postRequestError(requestType,null,getString(R.string.str_loading_footer_all));
//                break;
//            default:
//                HttpUtil.getRequest(url, new StringCallBack() {
//                    @Override
//                    public void onResponse(String response) {
//                       ScienceOutBean bean= JsonUtils.fromJson(response,ScienceOutBean.class);
//                        if (null!=bean){
//                            postRequestSuccess(requestType,bean.getResult(),"");
//                        }
//                    }
//
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        postRequestError(requestType,null,e.getMessage());
//                    }
//                },null);
//                break;
//        }
    }
    @Override
    protected void getArgs() {
        super.getArgs();
        url= ScienceApi.science_channel_url+ ScienceApi.channel_tag[getArguments().getInt(C.EXTRA_POSITION)];
        category=ScienceApi.channel_tag[getArguments().getInt(C.EXTRA_POSITION)];
    }
}
