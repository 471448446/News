package better.news.ui.base.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import better.news.db.Cache;
/**
 * Des  RecyclerView Adapter 基类 子类需要重写 onBindViewHolder onCreateViewHolder<br>
 * thk http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0804/3259.html
 * 动画 http://www.tqcto.com/article/mobile/138414.html
 * Created by Better on 2016/4/15 10:26.
 */
public abstract class BaseRecyclerViewAdapter<E, T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    protected List<E> mList = new ArrayList<>();
    protected Activity mContext;
    protected Fragment mFragment;

    protected Cache<E> mCache;
    protected boolean isFromCollect;

    protected boolean isScrollToTop;//Item滑动动画
    /**
     * 一般list
     * @param context
     */
    public BaseRecyclerViewAdapter(Activity context) {
        this.mContext = context;
    }

    /**
     * 一般list 需要用Fragment的
     * @param context
     * @param fragment
     */
    public BaseRecyclerViewAdapter(Activity context,Fragment fragment) {
        this.mContext = context;
        this.mFragment=fragment;
    }

    /**
     * 需要做缓存的list
     * @param context
     * @param cache
     */
    public BaseRecyclerViewAdapter(Activity context, Cache<E> cache) {
        this(context, cache, false);
    }

    /**
     * 需要做缓存的list  不用到fragment
     * @param context
     * @param cache
     * @param isFromeCollect
     */
    public BaseRecyclerViewAdapter(Activity context, Cache<E> cache, Boolean isFromeCollect) {
        this(context,null,cache,isFromeCollect);
    }

    /**
     * 需要做缓存的list切用到fragment
     * @param context
     * @param fragment
     * @param cache
     * @param isFromCollect
     */
    public BaseRecyclerViewAdapter(Activity context,Fragment fragment, Cache<E> cache, Boolean isFromCollect) {
        this.mContext = context;
        this.mCache = cache;
        this.isFromCollect = isFromCollect;
        this.mFragment=fragment;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onViewDetachedFromWindow(T holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public void setIsScrollToTop(boolean isScrollToTop) {
        this.isScrollToTop = isScrollToTop;
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
//        Utils.toastShort(mContext,"加载了数据"+list.size());
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
    public void reSetList(List<E> list){
        this.mList=list;
        notifyDataSetChanged();
    }

    public List<E> getList(){
        return this.mList;
    }
    protected E getItem(int position) {
        return mList.get(position);
    }
}
