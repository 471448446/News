package com.better.news.db.cache;

import android.database.Cursor;
import android.os.Handler;

import com.better.news.R;
import com.better.news.data.sicence.ScienceOutBean;
import com.better.news.db.Cache;
import com.better.news.db.table.ScienceTable;
import com.better.news.http.HttpUtil;
import com.better.news.http.callback.StringCallBack;
import com.better.news.support.C;
import com.better.news.support.util.JsonUtils;
import com.better.news.support.util.Utils;

import java.util.ArrayList;

import better.lib.recyclerview.RequestType;
import okhttp3.Call;

/**
 * Created by Better on 2016/4/7.
 */
public class ScienceCache extends Cache<ScienceOutBean.ScienceBean> {

    public ScienceCache(Handler handler, String category, String url) {
        super(handler, category, url);
    }

    @Override
    public void loadFromCache() {
        mList.clear();
        String sql = null;
        if(mCategory == null){
            sql = "select * from "+ ScienceTable.NAME;
        }else {
            sql = "select * from "+ScienceTable.NAME +" where "+ScienceTable.CATEGORY+"=\'"+mCategory+"\'";
        }
        Cursor cursor = query(sql);
        while (cursor.moveToNext()){
            ScienceOutBean.ScienceBean articleBean = new ScienceOutBean.ScienceBean();
            articleBean.setTitle(cursor.getString(ScienceTable.ID_TITLE));
            articleBean.setSummary(cursor.getString(ScienceTable.ID_DESCRIPTION));
//            if(articleBean.getImage_info() == null){
//                Utils.DLog(" " + articleBean.getImage_info());
//            }else {
//                articleBean.getImage_info().setUrl(cursor.getString(ScienceTable.ID_IMAGE));
//            }
            articleBean.setSmall_image(cursor.getString(ScienceTable.ID_IMAGE));
            articleBean.setReplies_count(cursor.getInt(ScienceTable.ID_COMMENT_COUNT));
//            articleBean.setInfo(cursor.getString(ScienceTable.ID_INFO));
            articleBean.setIs_collected(cursor.getInt(ScienceTable.ID_IS_COLLECTED));
            articleBean.setUrl(cursor.getString(ScienceTable.ID_URL));
            mList.add(articleBean);
        }
        cursor.close();
        sendMessage(RequestType.DATA_REQUEST_INIT,C.LOAD_FORM_CACHE);
    }

    @Override
    public void loadFromNet(final RequestType requestType) {
        switch (requestType){
            case DATA_REQUEST_UP_REFRESH:
                sendMessage(RequestType.DATA_REQUEST_UP_REFRESH, C.LOAD_FROM_NET_FAIL);
                break;
            case DATA_REQUEST_INIT:
                loadFromCache();
                break;
            default:
                HttpUtil.getRequest(url, new StringCallBack() {
                    @Override
                    public void onResponse(String response) {
                       ArrayList<String> collectionTitles = new ArrayList<String>();
                        if (null!=mList){
                            for(int i = 0 ; i<mList.size() ; i++ ){
                                if(mList.get(i).getIs_collected()==1){
                                    collectionTitles.add(mList.get(i).getTitle());
                                }
                            }
                        }
                        ScienceOutBean bean = JsonUtils.fromJson(response, ScienceOutBean.class);
                        if (null != bean) {
                            mList.clear();
                            mList=bean.getResult();
                            for(String title:collectionTitles){
                                for(int i=0 ; i<mList.size() ; i++){
                                    if(title.equals(mList.get(i).getTitle())){
                                        mList.get(i).setIs_collected(1);
                                    }
                                }
                            }
                           sendMessage(requestType,C.LOAD_FROM_NET_SUCCESS);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        sendMessage(requestType, C.LOAD_FROM_NET_FAIL);
                    }
                }, null);
                break;
        }
    }

    @Override
    public void putData() {
        db.execSQL(mHelper.DELETE_TABLE_DATA +ScienceTable.NAME+" where "+ScienceTable.CATEGORY+"=\'"+mCategory+"\'");
        // db.execSQL(table.CREATE_TABLE);
        for(int i=0;i<mList.size();i++){
            ScienceOutBean.ScienceBean articleBean = mList.get(i); ;
            values.put(ScienceTable.TITLE,articleBean.getTitle());
            values.put(ScienceTable.DESCRIPTION,articleBean.getSummary());
            values.put(ScienceTable.IMAGE,articleBean.getSmall_image());
            values.put(ScienceTable.COMMENT_COUNT,articleBean.getReplies_count());
//            values.put(ScienceTable.INFO,articleBean.getInfo());
            values.put(ScienceTable.URL,articleBean.getUrl());
            values.put(ScienceTable.CATEGORY,mCategory);
            values.put(ScienceTable.IS_COLLECTED,articleBean.getIs_collected());
            db.insert(ScienceTable.NAME,null,values);
        }
        db.execSQL(ScienceTable.SQL_INIT_COLLECTION_FLAG);
    }

    @Override
    public void putData(ScienceOutBean.ScienceBean articleBean) {
        values.put(ScienceTable.TITLE,articleBean.getTitle());
        values.put(ScienceTable.DESCRIPTION,articleBean.getSummary());
        values.put(ScienceTable.IMAGE,articleBean.getSmall_image());
        values.put(ScienceTable.COMMENT_COUNT,articleBean.getReplies_count());
        values.put(ScienceTable.URL,articleBean.getUrl());
//        values.put(ScienceTable.INFO,articleBean.getInfo());
        db.insert(ScienceTable.COLLECTION_NAME, null, values);
    }
}
