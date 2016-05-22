package better.news.db.coll;

import android.database.Cursor;
import android.os.Handler;

import java.util.ArrayList;

import better.lib.recyclerview.RequestType;
import better.news.MainApp;
import better.news.R;
import better.news.data.days.DaysBean;
import better.news.db.Cache;
import better.news.db.table.DayTable;
import better.news.support.C;

/**
 * Created by Better on 2016/4/9.
 */
public class DayCollectCache extends Cache<DaysBean.StoriesBean> {

    public DayCollectCache(Handler handler) {
        super(handler);
    }

    @Override
    public void loadFromCache() {
        mList.clear();
        Cursor cursor = query(DayTable.SELECT_ALL_FROM_COLLECTION);
        while (cursor.moveToNext()){
            DaysBean.StoriesBean storyBean = new DaysBean.StoriesBean();
            storyBean.setTitle(cursor.getString(DayTable.ID_TITLE));
            storyBean.setId(cursor.getInt(DayTable.ID_ID));
            ArrayList<String> list=new ArrayList<>();
            list.add(cursor.getString(DayTable.ID_IMAGE));
            storyBean.setImages(list);
            storyBean.setBody(cursor.getString(DayTable.ID_BODY));
            storyBean.setLargerImg(cursor.getString(DayTable.ID_LARGEPIC));
            storyBean.setIsColleted(1);
            mList.add(storyBean);
        }
        cursor.close();
        if (mList.isEmpty()) {
            mLoadFailNetException = new Exception(MainApp.getInstance().getString(R.string.str_no_colllect));
            sendMessage(RequestType.DATA_REQUEST_INIT, C.LOAD_FROM_NET_FAIL);
        } else {
            sendMessage(RequestType.DATA_REQUEST_INIT, C.LOAD_FROM_NET_SUCCESS);
        }
    }

    @Override
    public void loadFromNet(RequestType type) {

    }

    @Override
    public void load(RequestType type) {
//        if (RequestType.DATA_REQUEST_DOWN_REFRESH==type){
//            mLoadFailNetException=new Exception(MainApp.getInstance().getString(R.string.str_no_colllect));
//            sendMessage(type, C.LOAD_FROM_NET_FAIL);
//        }else{
//            loadFromCache();
//        }
        loadFromCache();
    }

    @Override
    public void putData() {

    }

    @Override
    public void putData(DaysBean.StoriesBean bean) {

    }
}
