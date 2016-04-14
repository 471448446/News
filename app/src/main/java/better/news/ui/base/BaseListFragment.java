package better.news.ui.base;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import better.lib.recyclerview.BRecyclerOnScrollListener;
import better.lib.recyclerview.BRecyclerView;
import better.lib.recyclerview.HeaderViewProxyRecyclerAdapter;
import better.lib.recyclerview.RequestType;
import better.lib.waitpolicy.emptyproxy.EmptyViewProxy;
import better.lib.waitpolicy.emptyproxy.FooterEmptyView;
import better.news.R;
import better.news.db.Cache;
import better.news.support.C;
import better.news.support.util.Utils;
import better.news.ui.base.adapter.BaseRecyclerViewAdapter;

/**
 * Created by Better on 2016/3/15.
 */
public abstract class BaseListFragment<E> extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
//    public static final int LOAD_FORM_CACHE = 10;
//    public static final int LOAD_FROM_NET_SUCCESS = LOAD_FORM_CACHE + 1;
//    public static final int LOAD_FROM_NET_FAIL = LOAD_FROM_NET_SUCCESS + 1;

    protected SwipeRefreshLayout mRefreshLayout;
    protected BRecyclerView mRecyclerView;
    protected HeaderViewProxyRecyclerAdapter mHeadAdapter;
    protected BaseRecyclerViewAdapter adapter;

    protected abstract Object getLastStartId();//id&String

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract BaseRecyclerViewAdapter getAdapter();

    //    /**
//     * footer重试
//     */
//    protected abstract void onFooterRetryClick();
    protected Cache<E> mCache;

    protected abstract Cache<E> getCache();

    /**
     * 获取列表信息
     *
     * @param requestType
     */
    protected abstract void asyncListInfo(RequestType requestType);

    protected void initRefresh(int refreshLayoutId, int recyclerId) {
        mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(refreshLayoutId);
        mRecyclerView = (BRecyclerView) rootView.findViewById(recyclerId);
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        adapter = getAdapter();

        mHeadAdapter = new HeaderViewProxyRecyclerAdapter(adapter);
        mHeadAdapter.setIsShowFooterView(mRecyclerView.isNeedFooter());
        mHeadAdapter.setIsShowHeaderView(mRecyclerView.isNeedHeader());
        if (mRecyclerView.isNeedFooter()) {
            createLoadingMoreLay();
            mRecyclerView.addOnScrollListener(new BRecyclerOnScrollListener() {
                @Override
                public void onBottom() {
                    asyncListInfo(RequestType.DATA_REQUEST_UP_REFRESH);
                }
            });
        }

        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setAdapter(mHeadAdapter);

        mRefreshLayout.setOnRefreshListener(this);

        if (mRecyclerView.isNeedEmptyView()) {
            mRecyclerView.getEmptyViewProxy().setOnRetryClickListener(new EmptyViewProxy.onLrRetryClickListener() {
                @Override
                public void onRetryClick() {
//                onLoadingRetry();
                    asyncListInfo(RequestType.DATA_REQUEST_INIT);
                }
            });
            mRecyclerView.getEmptyViewProxy().displayLoading();
            Utils.v("BRecyclerView","Fragment 显示Empty");
        }
        asyncListInfo(RequestType.DATA_REQUEST_INIT);
    }

//    protected abstract boolean isNeedLoadMore();

    private void createLoadingMoreLay() {
        FooterEmptyView footerEmptyView = new FooterEmptyView(getActivity());
        footerEmptyView.setOnRetryClickListener(new EmptyViewProxy.onLrRetryClickListener() {
            @Override
            public void onRetryClick() {
                asyncListInfo(RequestType.DATA_REQUEST_UP_REFRESH);
            }
        });
        mHeadAdapter.addFooterViewProxy(footerEmptyView);
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        asyncListInfo(RequestType.DATA_REQUEST_DOWN_REFRESH);
    }

    public void postRequestSuccess(RequestType requestType, List<E> list, String requestMeg) {
        mRefreshLayout.setRefreshing(false);
        boolean isEmpty = list.isEmpty() || null == list;
        BaseRecyclerViewAdapter baseAdapter = (BaseRecyclerViewAdapter) mHeadAdapter.getWrappedAdapter();
        switch (requestType) {
            case DATA_REQUEST_INIT:
                if (isEmpty && mRecyclerView.isNeedEmptyView())
                    mRecyclerView.getEmptyViewProxy().displayRetry(requestMeg);
                if (null != baseAdapter) baseAdapter.addDownData(list);
                break;
            case DATA_REQUEST_DOWN_REFRESH:
                if (mRecyclerView.isNeedEmptyView())
                    Utils.setGone(mRecyclerView.getEmptyViewProxy().getProxyView());
//                if (isEmpty) Utils.toastShort(getActivity(), R.string.str_loading_header_all);
                if (isEmpty) {
                    if (baseAdapter.getItemCount() > 0)
                        Utils.toastShort(getActivity(), R.string.str_loading_header_all);
                    else if (null != mHeadAdapter.getFooterViewProxy())
                        mHeadAdapter.getFooterViewProxy().displayMessage(requestMeg);
                }
                if (null != baseAdapter) baseAdapter.addDownData(list);
                break;
            case DATA_REQUEST_UP_REFRESH:
                if (mRecyclerView.isNeedEmptyView())
                    Utils.setGone(mRecyclerView.getEmptyViewProxy().getProxyView());
                if (isEmpty)
                    if (null != mHeadAdapter.getFooterViewProxy())
                        mHeadAdapter.getFooterViewProxy().displayMessage(getString(R.string.str_loading_footer_all));
                if (null != baseAdapter) baseAdapter.addPullData(list);
                break;
        }
    }

    public void postRequestError(RequestType requestType, ArrayList<E> list, String requestMeg) {
        mRefreshLayout.setRefreshing(false);
        switch (requestType) {
            case DATA_REQUEST_INIT:
                if (mRecyclerView.isNeedEmptyView())
                    mRecyclerView.getEmptyViewProxy().displayRetry(requestMeg);
                break;
            case DATA_REQUEST_DOWN_REFRESH:
                Utils.toastShort(getActivity(), requestMeg);
                break;
            case DATA_REQUEST_UP_REFRESH:
                if (null != mHeadAdapter.getFooterViewProxy())
                    mHeadAdapter.getFooterViewProxy().displayRetry(requestMeg);
                break;
        }
    }

    protected Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            RequestType type = (RequestType) message.obj;
            if (null == type) type = RequestType.DATA_REQUEST_DOWN_REFRESH;
            switch (message.what) {
                case C.LOAD_FORM_CACHE:
                    if (null == mCache.mList || mCache.mList.isEmpty()) {
                        mCache.loadFromNet(RequestType.DATA_REQUEST_DOWN_REFRESH);
                    } else {//有数据
                        //这里只处理有数据的请求，没有数据的情况在loadFromCache()中处调用loadFromNet()
                        postRequestSuccess(type, mCache.mList, "");
                    }
                    break;
                case C.LOAD_FROM_NET_SUCCESS:
                    postRequestSuccess(type, mCache.mList, "");
                    if (RequestType.DATA_REQUEST_DOWN_REFRESH == type || RequestType.DATA_REQUEST_INIT == type) {
                        mCache.cache();
                    }
                    break;
                case C.LOAD_FROM_NET_FAIL:
                    String errorMsg;
                    if (null == mCache.mLoadFailNetException) {
                        errorMsg = getString(R.string.str_loading_footer_all);
                        Utils.v("BRecyclerView","--------------error  mLoadFailNetException="+String.valueOf(null== mCache.mLoadFailNetException));
                    } else {
                        errorMsg = mCache.mLoadFailNetException.getMessage();
                    }
                    postRequestError(type, null, errorMsg);
                    break;
            }
            return false;
        }
    });
}