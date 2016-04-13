package com.better.news.ui.days;

import android.support.v7.widget.RecyclerView;

import com.better.news.data.days.DaysBean;
import com.better.news.db.Cache;
import com.better.news.db.cache.DayCache;
import com.better.news.support.util.DateUtil;
import com.better.news.support.util.Utils;
import com.better.news.ui.base.SimpleRefreshFragment;
import com.better.news.ui.base.adapter.BaseRecyclerViewAdapter;

import better.lib.recyclerview.RequestType;

/**
 * Created by Better on 2016/3/18.
 */
public class DaysFragment extends SimpleRefreshFragment {
    private DaysBean bean;

    @Override
    protected Object getLastStartId() {

        if (null != bean) return DateUtil.getDateBeforDay(bean.getDate());
        else return "";
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
        return new DayCache(mHandler);
    }

    @Override
    protected void asyncListInfo(final RequestType requestType) {
        mCache.loadFromNet(requestType);
//        String url;
//        switch (requestType){
//            case DATA_REQUEST_UP_REFRESH:
//                url=DaysApi.getBeforeUrl((String)getLastStartId());
//                break;
//            default:
//                url= DaysApi.latest;
//                break;
//        }
//        HttpUtil.getRequest(url, new StringCallBack() {
//            @Override
//            public void onError(Call call, Exception e) {
//                if(null==e){
//                    postRequestError(requestType,null,"已全部加载");
//                }else{
//                    postRequestError(requestType,null,e.getMessage());
//                }
//            }
//
//            @Override
//            public void onResponse(String response) {
//                bean= JsonUtils.fromJson(response,DaysBean.class);
//                log("获取到ist="+response);
//                if(null!=bean){
//                    postRequestSuccess(requestType,bean.getStories(),"");
//                }
//            }
//        },null);
    }
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        Utils.v("CustomOkHttpClient", "onViewCreated()");
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Utils.v("CustomOkHttpClient", "onResume()" + String.valueOf(null==bean));
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        Utils.v("CustomOkHttpClient", "onDestroy()" );
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Utils.v("CustomOkHttpClient", "onDestroy()" );
//    }
}
