package better.news.ui.coll;

import android.support.v7.widget.RecyclerView;

import better.news.db.Cache;
import better.news.db.coll.DayCollectCache;
import better.news.ui.base.SimpleNoLoadMoreRefreshFragment;
import better.news.ui.base.adapter.BaseRecyclerViewAdapter;
import better.news.ui.days.DaysAdapter;

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
        return new DaysAdapter(getActivity(),this);
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
