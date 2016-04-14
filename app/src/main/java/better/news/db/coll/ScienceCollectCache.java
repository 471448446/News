package better.news.db.coll;

import android.database.Cursor;
import android.os.Handler;

import better.news.MainApp;
import better.news.R;
import better.news.data.sicence.ScienceOutBean;
import better.news.db.Cache;
import better.news.db.table.ScienceTable;
import better.news.support.C;
import better.news.support.util.Utils;

import better.lib.recyclerview.RequestType;

/**
 * Created by Better on 2016/4/9.
 */
public class ScienceCollectCache extends Cache<ScienceOutBean.ScienceBean> {
    public ScienceCollectCache(Handler handler) {
        super(handler);
    }

    @Override
    public void loadFromCache() {
        mList.clear();
        Cursor cursor = query(ScienceTable.SELECT_ALL_FROM_COLLECTION);
        while (cursor.moveToNext()) {
            ScienceOutBean.ScienceBean articleBean = new ScienceOutBean.ScienceBean();
            articleBean.setTitle(cursor.getString(ScienceTable.ID_TITLE));
//            articleBean.setInfo(cursor.getString(ScienceTable.ID_INFO));
            articleBean.setSmall_image(cursor.getString(ScienceTable.ID_IMAGE));
            articleBean.setUrl(cursor.getString(ScienceTable.ID_URL));
            articleBean.setReplies_count(cursor.getShort(ScienceTable.ID_COMMENT_COUNT));
            articleBean.setSummary(cursor.getString(ScienceTable.ID_DESCRIPTION));
            articleBean.setIs_collected(1);
            mList.add(articleBean);
        }
        cursor.close();
//        sendMessage(RequestType.DATA_REQUEST_INIT, C.LOAD_FORM_CACHE);
        if (mList.isEmpty()) {

            mLoadFailNetException = new Exception(MainApp.getInstance().getString(R.string.str_no_colllect));
            sendMessage(RequestType.DATA_REQUEST_INIT, C.LOAD_FROM_NET_FAIL);
        } else {
            Utils.v("BRecyclerView","----------  list");
            sendMessage(RequestType.DATA_REQUEST_INIT, C.LOAD_FROM_NET_SUCCESS);
        }
    }

    @Override
    public void loadFromNet(RequestType type) {
        loadFromCache();
    }

    @Override
    public void putData() {

    }

    @Override
    public void putData(ScienceOutBean.ScienceBean bean) {

    }
}
