package better.news.ui.science;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import better.news.data.sicence.ScienceOutBean;
import better.news.db.Cache;
import better.news.db.cache.ScienceCache;
import better.news.http.api.ScienceApi;
import better.news.support.C;
import better.news.ui.base.SimpleRefreshFragment;
import better.news.ui.base.adapter.BaseRecyclerViewAdapter;

import java.util.List;

import better.lib.http.RequestType;

/**
 * Created by Better on 2016/3/20.
 */
public class ScienceFragment extends SimpleRefreshFragment{
    public static Fragment newInstance(int pos){
        ScienceFragment fr=new ScienceFragment();
        Bundle bundle=new Bundle();
        bundle.putInt(C.EXTRA_POSITION, pos);
        fr.setArguments(bundle);
        return  fr;
    }
    private String url,category;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Req_Code != requestCode || null == data) return;
        String title=data.getStringExtra(C.EXTRA_KEY);
        boolean collect=resultCode==C.science_collect_add?true:false;
        List<ScienceOutBean.ScienceBean> list=adapter.getList();
        for (ScienceOutBean.ScienceBean item:list){
            if (title.equals(item.getTitle())){
                item.setIs_collected(collect==true?1:0);
            }
        }
        adapter.reSetList(list);
    }

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
        return new ScienceCache(mHandler,category,url);
    }

    @Override
    protected void asyncListInfo(final RequestType requestType) {
        mCache.load(requestType);
//        switch (requestType){
//            case DATA_REQUEST_UP_REFRESH:
//                postRequestError(requestType,null,getString(R.string.str_loading_footer_all));
//                break;
//            default:
//                HttpUtil.getRequest(url, new StringCallBack() {
//                    @Override
//                    public void onResponse(String response) {
//                       ScienceOutBean bean= JsonUtils.fromJson(response,ScienceOutBean.class);
//                        if (null!=bean){
//                            postRequestSuccess(requestType,bean.getResult(),"");
//                        }
//                    }
//
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        postRequestError(requestType,null,e.getMessage());
//                    }
//                },null);
//                break;
//        }
    }
    @Override
    protected void getArgs() {
        super.getArgs();
        url= ScienceApi.science_channel_url+ ScienceApi.channel_tag[getArguments().getInt(C.EXTRA_POSITION)];
        category=ScienceApi.channel_tag[getArguments().getInt(C.EXTRA_POSITION)];
    }
}
