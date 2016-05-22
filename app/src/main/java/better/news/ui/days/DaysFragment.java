package better.news.ui.days;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import better.lib.recyclerview.RequestType;
import better.news.data.days.DaysBean;
import better.news.db.Cache;
import better.news.db.cache.DayCache;
import better.news.support.C;
import better.news.support.util.DateUtil;
import better.news.ui.base.SimpleRefreshFragment;
import better.news.ui.base.adapter.BaseRecyclerViewAdapter;

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Req_Code != requestCode || null == data) return;
        String title=data.getStringExtra(C.EXTRA_KEY);
        boolean collect=resultCode==C.day_collect_add?true:false;
        List<DaysBean.StoriesBean> list=adapter.getList();
        for (DaysBean.StoriesBean item:list){
            if (title.equals(item.getTitle())){
                item.setIsColleted(collect==true?1:0);
            }
        }
        adapter.reSetList(list);
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
        return new DayCache(mHandler);
    }

    @Override
    protected void asyncListInfo(final RequestType requestType) {
        mCache.load(requestType);
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
