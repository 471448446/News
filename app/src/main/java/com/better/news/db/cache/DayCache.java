package com.better.news.db.cache;

import android.database.Cursor;
import android.os.Handler;

import com.better.news.data.days.DaysBean;
import com.better.news.db.Cache;
import com.better.news.db.table.DayTable;
import com.better.news.http.HttpUtil;
import com.better.news.http.api.DaysApi;
import com.better.news.http.callback.StringCallBack;
import com.better.news.support.C;
import com.better.news.support.util.DateUtil;
import com.better.news.support.util.JsonUtils;
import com.better.news.support.util.Utils;

import java.util.ArrayList;

import better.lib.recyclerview.RequestType;
import okhttp3.Call;

/**
 * Created by Better on 2016/4/5.
 */
public class DayCache extends Cache<DaysBean.StoriesBean> {

    private DaysBean bean;

    public DayCache(Handler handler) {
        super(handler);
    }

    @Override
    public void loadFromCache() {
        Utils.v("Better","loadFromCache()");
        mList.clear();
        String sql = "select * from "+DayTable.NAME+" order by "+DayTable.ID+" desc";

        Cursor cursor = query(sql);
        while (cursor.moveToNext()){
            DaysBean.StoriesBean storyBean = new DaysBean.StoriesBean();
            storyBean.setTitle(cursor.getString(DayTable.ID_TITLE));
            storyBean.setId(cursor.getInt(DayTable.ID_ID));
            ArrayList<String> list=new ArrayList<>();
            list.add(cursor.getString(DayTable.ID_IMAGE));
            storyBean.setImages(list);
            storyBean.setBody(cursor.getString(DayTable.ID_BODY));
            storyBean.setLargerImg(cursor.getString(DayTable.ID_LARGEPIC));
            storyBean.setIsColleted(cursor.getInt(DayTable.ID_IS_COLLECTED));
            mList.add(storyBean);
        }
        cursor.close();

        sendMessage(RequestType.DATA_REQUEST_INIT, C.LOAD_FORM_CACHE);
    }

    @Override
    public void loadFromNet(final RequestType requestType) {
        switch (requestType) {
            case DATA_REQUEST_INIT:
                loadFromCache();
                break;
            default:
                String url;
                switch (requestType) {
                    case DATA_REQUEST_UP_REFRESH:
                        url = DaysApi.getBeforeUrl(getLastStartId());
                        break;
                    default:
                        url = DaysApi.latest;
                        break;
                }
                HttpUtil.getRequest(url, new StringCallBack() {
                    @Override
                    public void onError(Call call, Exception e) {
                        mLoadFailNetException = e;
                        sendMessage(requestType, C.LOAD_FROM_NET_FAIL);
                    }

                    @Override
                    public void onResponse(String response) {
                        ArrayList<String> collectionTitles = new ArrayList<String>();
                        if (null!=mList){
                            for(int i = 0 ; i<mList.size() ; i++ ){
                                if(mList.get(i).isColleted()){
                                    collectionTitles.add(mList.get(i).getTitle());
                                }
                            }
                        }
                        mList.clear();
                        bean = JsonUtils.fromJson(response, DaysBean.class);
                        if (null != bean) {
                            mList = bean.getStories();
                            for(String title:collectionTitles){
                                for(int i=0 ; i<mList.size() ; i++){
                                    if(title.equals(mList.get(i).getTitle())){
                                        mList.get(i).setIsColleted(1);
                                    }
                                }
                            }
                            sendMessage(requestType, C.LOAD_FROM_NET_SUCCESS);
                        } else {
                            sendMessage(requestType, C.LOAD_FROM_NET_FAIL);
                        }
                    }
                }, null);
                break;
        }
    }

    private String getLastStartId() {
        if (null != bean) return DateUtil.getDateBeforDay(bean.getDate());
        else return DateUtil.getDateBeforDay(DateUtil.getTodayStr());
    }

    @Override
    public void putData() {
        Utils.v("Better","putData()");
        db.execSQL(mHelper.DELETE_TABLE_DATA + DayTable.NAME);
        for(int i=0;i<mList.size();i++){
            DaysBean.StoriesBean storyBean = mList.get(i);
            values.put(DayTable.TITLE,storyBean.getTitle());
            values.put(DayTable.ID, storyBean.getId());
            values.put(DayTable.IMAGE,storyBean.getImages().get(0));
            values.put(DayTable.BODY,storyBean.getBody());
            values.put(DayTable.LARGEPIC,storyBean.getLargerImg());
            values.put(DayTable.IS_COLLECTED,storyBean.isColleted());
            db.insert(DayTable.NAME, null, values);
        }
        db.execSQL(DayTable.SQL_INIT_COLLECTION_FLAG);
    }

    @Override
    public void putData(DaysBean.StoriesBean bean) {
        values.put(DayTable.TITLE, bean.getTitle());
        values.put(DayTable.ID, bean.getId());
        values.put(DayTable.IMAGE, bean.getImages().get(0));
        values.put(DayTable.BODY, bean.getBody() == null ? "" : bean.getBody());
        values.put(DayTable.LARGEPIC, bean.getLargerImg());
        db.insert(DayTable.COLLECTION_NAME, null, values);
    }
}
