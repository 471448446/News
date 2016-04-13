package com.better.news.ui.base;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.better.news.R;

import better.lib.recyclerview.RequestType;

/**
 * Created by Better on 2016/3/15.
 */
public abstract class SimpleRefreshFragment extends BaseListFragment {
    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_simplerefresh, container, false);
    }

    @Override
    protected void initWhenNullRootView() {
        super.initWhenNullRootView();
        mCache=getCache();
        initRefresh(R.id.simpleRefresh_SwipeRefresh, R.id.simpleRefresh_recyclerView);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    protected LinearLayoutManager getLinearLayoutManagerV() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        return manager;
    }

//    @Override
//    protected boolean isNeedLoadMore() {
//        return true;
//    }
}
