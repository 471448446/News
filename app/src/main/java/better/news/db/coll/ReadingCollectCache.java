package better.news.db.coll;

import android.database.Cursor;
import android.os.Handler;

import java.util.ArrayList;

import better.lib.recyclerview.RequestType;
import better.news.MainApp;
import better.news.R;
import better.news.data.read.ReadBean;
import better.news.db.Cache;
import better.news.db.table.ReadingTable;
import better.news.support.C;

/**
 * Created by Better on 2016/4/9.
 */
public class ReadingCollectCache extends Cache<ReadBean.BooksBean> {
    public ReadingCollectCache(Handler handler) {
        super(handler);
    }

    @Override
    public void loadFromCache() {
        mList.clear();
        Cursor cursor = query(ReadingTable.SELECT_ALL_FROM_COLLECTION);
        while (cursor.moveToNext()) {
            ReadBean.BooksBean bookBean = new ReadBean.BooksBean();
            bookBean.setTitle(cursor.getString(ReadingTable.ID_TITLE));
            bookBean.setSummary(cursor.getString(ReadingTable.ID_SUMMARY));
            bookBean.setImage(cursor.getString(ReadingTable.ID_INFO));
            bookBean.setImage(cursor.getString(ReadingTable.ID_IMAGE));
//            bookBean.setInfo(cursor.getString(ReadingTable.ID_INFO));
            bookBean.setEbook_url(cursor.getString(ReadingTable.ID_EBOOK_URL));
            bookBean.setAuthor_intro(cursor.getString(ReadingTable.ID_AUTHOR_INTRO));
            bookBean.setPages(cursor.getString(ReadingTable.ID_BOOK_PAGE));
            bookBean.setPublisher(cursor.getString(ReadingTable.ID_BOOK_PUB));
            bookBean.setCatalog(cursor.getString(ReadingTable.ID_CATALOG));
            ArrayList<String> list = new ArrayList<>();
            list.add(cursor.getString(ReadingTable.ID_BOOK_AUTH));
            bookBean.setAuthor(list);
            bookBean.setIs_collected(1);
            mList.add(bookBean);
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

    }

    @Override
    public void load(RequestType type) {
//        if (RequestType.DATA_REQUEST_DOWN_REFRESH == type) {
//            mLoadFailNetException = new Exception(MainApp.getInstance().getString(R.string.str_no_colllect));
//            sendMessage(type, C.LOAD_FROM_NET_FAIL);
//        } else {
//            loadFromCache();
//        }
        loadFromCache();
    }

    @Override
    public void putData() {

    }

    @Override
    public void putData(ReadBean.BooksBean bean) {

    }
}
