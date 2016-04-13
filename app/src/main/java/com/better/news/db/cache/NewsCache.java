package com.better.news.db.cache;

import android.database.Cursor;
import android.os.Handler;

import com.better.news.db.Cache;
import com.better.news.db.table.NewsTable;
import com.better.news.http.HttpUtil;
import com.better.news.http.callback.StringCallBack;
import com.better.news.support.C;
import com.better.news.support.sax.RssFeed;
import com.better.news.support.sax.RssItem;
import com.better.news.support.util.Utils;

import java.util.ArrayList;

import better.lib.recyclerview.RequestType;
import okhttp3.Call;

/**
 * Created by Better on 2016/4/7.
 */
public class NewsCache extends Cache<RssItem> {

    public NewsCache(Handler handler, String category, String url) {
        super(handler, category, url);
    }

    @Override
    public void loadFromCache() {
        mList.clear();
        String sql = null;
        if (mCategory == null) {
            sql = "select * from " + NewsTable.NAME;
        } else {
            sql = "select * from " + NewsTable.NAME + " where " + NewsTable.CATEGORY + "=\'" + mCategory + "\'";
        }
        Cursor cursor = query(sql);
        while (cursor.moveToNext()) {
            RssItem newsBean = new RssItem();
            newsBean.setTitle(cursor.getString(NewsTable.ID_TITLE));
            newsBean.setDescription(cursor.getString(NewsTable.ID_DESCRIPTION));
            newsBean.setData(cursor.getString(NewsTable.ID_PUBTIME));
            newsBean.setLink(cursor.getString(NewsTable.ID_LINK));
            newsBean.setIs_collected(cursor.getInt(NewsTable.ID_IS_COLLECTED));
            mList.add(newsBean);
        }
        cursor.close();
        sendMessage(RequestType.DATA_REQUEST_INIT, C.LOAD_FORM_CACHE);
    }

    @Override
    public void loadFromNet(final RequestType dataRequestInit) {
        switch (dataRequestInit) {
            case DATA_REQUEST_INIT:
                loadFromCache();
                break;
            case DATA_REQUEST_DOWN_REFRESH:
                HttpUtil.getRequest(url, new StringCallBack() {
                    @Override
                    public void onError(Call call, Exception e) {
                        mLoadFailNetException = e;
                        sendMessage(dataRequestInit, C.LOAD_FROM_NET_FAIL);
                    }

                    @Override
                    public void onResponse(String response) {
                        RssFeed rssFeed = Utils.getFeed(response);
                        if (null != rssFeed) {
                            ArrayList<String> collectionTitles = new ArrayList<String>();
                            for(int i = 0 ; i<mList.size() ; i++ ){
                                if(mList.get(i).getIs_collected() == 1){
                                    collectionTitles.add(mList.get(i).getTitle());
                                    Utils.v("Better"," 保存的Title="+mList.get(i).getTitle());
                                }
                            }
                            mList.clear();
                            mList.addAll(rssFeed.getItems());
                            for(String title:collectionTitles){
                                for(int i=0 ; i<mList.size() ; i++){
                                    if(title.equals(mList.get(i).getTitle())){
                                        mList.get(i).setIs_collected(1);
                                    }
                                }
                            }

//                            mList.clear();
//                            mList = rssFeed.getItems();
                            sendMessage(dataRequestInit, C.LOAD_FROM_NET_SUCCESS);
                        } else {
                            sendMessage(dataRequestInit, C.LOAD_FROM_NET_FAIL);
                        }
                    }
                }, null);
                break;
            default:
                sendMessage(dataRequestInit, C.LOAD_FROM_NET_FAIL);
                break;
        }
    }

    @Override
    public void putData() {
        db.execSQL(mHelper.DELETE_TABLE_DATA + NewsTable.NAME + " where " + NewsTable.CATEGORY + "=\'" + mCategory + "\'");
        for (int i = 0; i < mList.size(); i++) {
            RssItem newsBean = mList.get(i);
            values.put(NewsTable.TITLE, newsBean.getTitle());
            values.put(NewsTable.DESCRIPTION, newsBean.getDescription());
            values.put(NewsTable.PUBTIME, newsBean.getData());
            values.put(NewsTable.IS_COLLECTED, newsBean.getIs_collected());
            values.put(NewsTable.LINK, newsBean.getLink());
            values.put(NewsTable.CATEGORY, mCategory);
            db.insert(NewsTable.NAME, null, values);
        }
    }

    @Override
    public void putData(RssItem newsBean) {
        values.put(NewsTable.TITLE, newsBean.getTitle());
        values.put(NewsTable.DESCRIPTION, newsBean.getDescription());
        values.put(NewsTable.PUBTIME, newsBean.getData());
        values.put(NewsTable.LINK, newsBean.getLink());
        db.insert(NewsTable.COLLECTION_NAME, null, values);
        Utils.v("Better", "保存新闻" + newsBean.getTitle());
    }
}
