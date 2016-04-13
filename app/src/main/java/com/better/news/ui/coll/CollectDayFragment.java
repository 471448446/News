package com.better.news.ui.coll;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.better.news.db.Cache;
import com.better.news.db.coll.DayCollectCache;
import com.better.news.ui.base.SimpleNoLoadMoreRefreshFragment;
import com.better.news.ui.base.SimpleRefreshFragment;
import com.better.news.ui.base.adapter.BaseRecyclerViewAdapter;
import com.better.news.ui.days.DaysAdapter;

import better.lib.recyclerview.RequestType;

/**
 * Created by Better on 2016/4/9.
 */
public class CollectDayFragment extends SimpleNoLoadMoreRefreshFragment {
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
        return new DaysAdapter(getActivity());
    }

    @Override
    protected Cache getCache() {
        return new DayCollectCache(mHandler);
    }

    @Override
    protected void asyncListInfo(RequestType requestType) {
        mCache.loadFromNet(requestType);
    }

}
