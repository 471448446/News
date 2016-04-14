package better.news.db.coll;

import android.database.Cursor;
import android.os.Handler;

import better.news.MainApp;
import better.news.R;
import better.news.db.Cache;
import better.news.db.table.NewsTable;
import better.news.support.C;
import better.news.support.sax.RssItem;

import better.lib.recyclerview.RequestType;

/**
 * Created by Better on 2016/4/9.
 */
public class NewsCollectCach extends Cache<RssItem> {
    public NewsCollectCach(Handler handler) {
        super(handler);
    }

    @Override
    public void loadFromCache() {
        mList.clear();
        Cursor cursor = query(NewsTable.SELECT_ALL_FROM_COLLECTION);
        while (cursor.moveToNext()) {
            RssItem newsBean = new RssItem();
            newsBean.setTitle(cursor.getString(NewsTable.ID_TITLE));
            newsBean.setDescription(cursor.getString(NewsTable.ID_DESCRIPTION));
            newsBean.setLink(cursor.getString(NewsTable.ID_LINK));
            newsBean.setData(cursor.getString(NewsTable.ID_PUBTIME));
            newsBean.setIs_collected(1);
            mList.add(newsBean);
        }
        cursor.close();
//        sendMessage(RequestType.DATA_REQUEST_INIT, C.LOAD_FORM_CACHE);
        if (mList.isEmpty()) {
            mLoadFailNetException = new Exception(MainApp.getInstance().getString(R.string.str_no_colllect));
            sendMessage(RequestType.DATA_REQUEST_INIT, C.LOAD_FROM_NET_FAIL);
        } else {
            sendMessage(RequestType.DATA_REQUEST_INIT, C.LOAD_FROM_NET_SUCCESS);
        }
    }

    @Override
    public void loadFromNet(RequestType type) {
//        if (RequestType.DATA_REQUEST_DOWN_REFRESH==type){
//            mLoadFailNetException=new Exception(MainApp.getInstance().getString(R.string.str_no_colllect));
//            sendMessage(type,C.LOAD_FROM_NET_FAIL);
//        }else{
//            loadFromCache();
//        }
        loadFromCache();
    }

    @Override
    public void putData() {

    }

    @Override
    public void putData(RssItem bean) {

    }
}
