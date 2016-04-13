package com.better.news.ui.coll;

import android.support.v7.widget.RecyclerView;

import com.better.news.db.Cache;
import com.better.news.db.coll.ScienceCollectCache;
import com.better.news.ui.base.SimpleNoLoadMoreRefreshFragment;
import com.better.news.ui.base.SimpleRefreshFragment;
import com.better.news.ui.base.adapter.BaseRecyclerViewAdapter;
import com.better.news.ui.science.ScienceAdapter;

import better.lib.recyclerview.RequestType;

/**
 * Created by Better on 2016/4/10.
 */
public class CollectScienceFragment extends SimpleNoLoadMoreRefreshFragment {
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
        return new ScienceCollectCache(mHandler);
    }

    @Override
    protected void asyncListInfo(RequestType requestType) {
        mCache.loadFromNet(requestType);
    }
}
