package better.news.ui.coll;

import android.support.v7.widget.RecyclerView;

import better.news.db.Cache;
import better.news.db.coll.ScienceCollectCache;
import better.news.ui.base.SimpleNoLoadMoreRefreshFragment;
import better.news.ui.base.adapter.BaseRecyclerViewAdapter;
import better.news.ui.science.ScienceAdapter;

import better.lib.http.RequestType;

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
        return new ScienceAdapter(getActivity(),this);
    }

    @Override
    protected Cache getCache() {
        return new ScienceCollectCache(mHandler);
    }

    @Override
    protected void asyncListInfo(RequestType requestType) {
        mCache.load(requestType);
    }
}
