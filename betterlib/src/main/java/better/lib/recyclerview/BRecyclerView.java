package better.lib.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import better.lib.waitpolicy.emptyproxy.DefaultEmptyView;
import better.lib.R;
import better.lib.utils.BaseUtils;

/**
 * Created by Better on 2016/3/11.
 * 添加EmptyView,BRecyclerView的刷新控件外层必须的是LinearLayout，否则刷新无效。
 * loadingMore 采用RecyclerView的多布局来实现。
 * xml中设置需要footer
 * thk
 * Recycler添加emptyView http://blog.csdn.net/sbsujjbcy/article/details/46574421
 * http://stackoverflow.com/questions/27414173/equivalent-of-listview-setemptyview-in-recyclerview
 * 刷新RecyclerView，下拉刷新用SwipeRefreshLayout 所以默认支持EmptyView，支持footerView
 * 当一个屏幕占不满时如何 隐藏footerView？？
 */
public class BRecyclerView extends RecyclerView {
    private DefaultEmptyView emptyViewProxy;
    private int mDefaultItemCount=1;//默认RecyclerViewAdapter count 展示emptyView，默认展示FooterView，所以当adapter，count等于1表述没有数据，展示emptyview
    private boolean hasPreparedEmpty;//确保emptyView只添加一次
    private boolean isNeedEmptyView =true;//默认是有的
    private boolean isNeedFooter=true,isNeedHeader;

    public BRecyclerView(Context context) {
        super(context);
        prepareDefaultEmpty(context);
    }

    public BRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initValues(context, attrs);
        prepareDefaultEmpty(context);
    }

//    public BRecyclerView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        prepareDefaultEmpty(context);
//    }

    private void initValues(Context context, AttributeSet attrs) {
        if(null!=attrs){
            TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.BRecyclerView);
            final int N=typedArray.getIndexCount();
            for (int i = 0; i < N; i++) {
                initCustomAttr(typedArray.getIndex(i), typedArray);
            }
            typedArray.recycle();
        }
    }
    private void initCustomAttr(int attr, TypedArray typedArray){
        if(attr==R.styleable.BRecyclerView_isNeedFooter){
            isNeedFooter= typedArray.getBoolean(attr, isNeedFooter);
            if (!isNeedFooter) mDefaultItemCount--;//默认已经为其+1了，当不支持footerView时-1
//            if (isNeedFooter) mDefaultItemCount++;
//            log("设置 footer"+String.valueOf(isShowFooter)+mDefaultItemCount);
        }else if (attr==R.styleable.BRecyclerView_isNeedHeader){
            isNeedHeader= typedArray.getBoolean(attr,isNeedHeader);
            if (isNeedHeader) mDefaultItemCount++;
//            log("设置 header"+String.valueOf(isShowHeader)+mDefaultItemCount);
        }else if(attr==R.styleable.BRecyclerView_isNeedEmptyView){
            isNeedEmptyView =typedArray.getBoolean(attr, isNeedEmptyView);
        }
    }
    /**
     * 准备 emptyView的 parent
     *
     * @param context
     */
    private void prepareDefaultEmpty(Context context) {

        if (isNeedEmptyView) emptyViewProxy = new DefaultEmptyView(context);
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!hasPreparedEmpty) {
                    hasPreparedEmpty = true;
                    try {
                        if (isNeedEmptyView){
                            LinearLayout layout = (LinearLayout) BRecyclerView.this.getParent().getParent();
                            layout.addView(emptyViewProxy.getProxyView(), 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                            log(String.valueOf(null == layout) + "," + String.valueOf(null == BRecyclerView.this.getParent()));
                            adapterItemChanged();
                        }
                    } catch (Exception e) {
                        Log.w("BRecyclerView", "parent  不是LinearLayout" + e.getMessage());
                    }
                }
            }
        });
    }

    public DefaultEmptyView getEmptyViewProxy() {
        return emptyViewProxy;
    }

    public boolean isNeedFooter() {
        return isNeedFooter;
    }

    public boolean isNeedHeader() {
        return isNeedHeader;
    }

    public boolean isNeedEmptyView() {
        return isNeedEmptyView;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        log("isNeedEmptyView=========="+String.valueOf(isNeedEmptyView));
        if (isNeedEmptyView){
            if (null != adapter) {
                adapter.registerAdapterDataObserver(emptyObserver);
            }
            emptyObserver.onChanged();//不一定有用，没有完成布局 获取不到parent
        }
    }

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            adapterItemChanged();
        }
    };

    private void adapterItemChanged() {
        Adapter<?> adapter = getAdapter();
        if (null != adapter && null != emptyViewProxy&& hasPreparedEmpty) {
            log("adapter count="+adapter.getItemCount());
            if (mDefaultItemCount == adapter.getItemCount()) {    log("显示emotyView");
                BaseUtils.setGone(BRecyclerView.this);
                BaseUtils.setVisible(emptyViewProxy.getProxyView());
            } else {
                BaseUtils.setGone(emptyViewProxy.getProxyView());
                BaseUtils.setVisible(BRecyclerView.this);
            }
        }
    }

    private void log(String msg) {
        Log.v("BRecyclerView", msg);
    }
}
