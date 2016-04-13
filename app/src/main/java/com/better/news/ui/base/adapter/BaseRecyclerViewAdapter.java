package com.better.news.ui.base.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.better.news.db.Cache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Better on 2016/3/15.
 * thk http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0804/3259.html
 */
public abstract class BaseRecyclerViewAdapter<E, T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    protected List<E> mList = new ArrayList<>();
    protected Activity mContent;

    protected Cache<E> mCache;
    protected boolean isFromCollect;

    public BaseRecyclerViewAdapter(Activity context) {
        this.mContent = context;
    }

    public BaseRecyclerViewAdapter(Activity context, Cache<E> cache) {
        this(context, cache, false);
    }

    public BaseRecyclerViewAdapter(Activity context, Cache<E> cache, Boolean isFromeCollect) {
        this.mContent = context;
        this.mCache = cache;
        this.isFromCollect = isFromeCollect;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 下拉数据，最新数据，会重置列表
     *
     * @param list
     */
    public void addDownData(List<E> list) {
        if (null == list || list.isEmpty()) return;
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
//        Utils.toastShort(mContent,"加载了数据"+list.size());
    }

    /**
     * 上拉数据，加载更多
     *
     * @param list
     */
    public void addPullData(List<E> list) {
        if (null == list || list.isEmpty()) return;
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    protected E getItem(int position) {
        return mList.get(position);
    }
}
