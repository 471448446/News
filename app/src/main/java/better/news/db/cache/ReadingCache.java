package better.news.db.cache;

import android.database.Cursor;
import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;

import better.lib.recyclerview.RequestType;
import better.news.data.read.ReadBean;
import better.news.db.Cache;
import better.news.db.table.ReadingTable;
import better.news.http.HttpUtil;
import better.news.http.api.ReadApi;
import better.news.http.callback.StringCallBack;
import better.news.support.C;
import better.news.support.util.JsonUtils;
import okhttp3.Call;

/**
 * Created by Better on 2016/4/7.
 */
public class ReadingCache extends Cache<ReadBean.BooksBean> {

    public ReadingCache(Handler handler, String category, String[] url) {
        super(handler, category, url);
    }

    @Override
    public void loadFromCache() {
        mList.clear();
        String sql = null;
        if(mCategory == null){
            sql = "select * from "+ReadingTable.NAME;
        }else {
            sql = "select * from "+ReadingTable.NAME +" where "+ReadingTable.CATEGORY+"=\'"+mCategory+"\'";
        }
        Cursor cursor = query(sql);
        while (cursor.moveToNext()){
            ReadBean.BooksBean bookBean = new ReadBean.BooksBean();
            bookBean.setTitle(cursor.getString(ReadingTable.ID_TITLE));
            bookBean.setImage(cursor.getString(ReadingTable.ID_IMAGE));
//            bookBean.setInfo(cursor.getString(ReadingTable.ID_INFO));
            bookBean.setAuthor_intro(cursor.getString(ReadingTable.ID_AUTHOR_INTRO));
            bookBean.setCatalog(cursor.getString(ReadingTable.ID_CATALOG));
            bookBean.setEbook_url(cursor.getString(ReadingTable.ID_EBOOK_URL));
            bookBean.setSummary(cursor.getString(ReadingTable.ID_SUMMARY));
            ArrayList<String> list=new ArrayList<>();
            list.add(cursor.getString(ReadingTable.ID_BOOK_AUTH));
            bookBean.setAuthor(list);
            bookBean.setPublisher(cursor.getString(ReadingTable.ID_BOOK_PUB));
            bookBean.setPages(cursor.getString(ReadingTable.ID_BOOK_PAGE));
            bookBean.setIs_collected(cursor.getInt(ReadingTable.ID_IS_COLLECTED));
            mList.add(bookBean);
        }
        cursor.close();
        sendMessage(RequestType.DATA_REQUEST_INIT,C.LOAD_FORM_CACHE);
    }

    @Override
    public void loadFromNet(final RequestType type) {
        if (RequestType.DATA_REQUEST_UP_REFRESH != type) {
            count = -1;
        }
        HashMap<String, Object> parms = new HashMap<>();

        parms.put("start", getLastStartId());
        parms.put("count", ReadApi.Offset);
        for (String tag : urls) {
            parms.put("tag", tag);
            HttpUtil.getRequest(ReadApi.searchUrl, parms, new StringCallBack() {
                @Override
                public void onResponse(String response) {
                    ArrayList<String> collectionTitles = new ArrayList<String>();
                    if (null!=mList){
                        for(int i = 0 ; i<mList.size() ; i++ ){
                            if(mList.get(i).getIs_collected() == 1){
                                collectionTitles.add(mList.get(i).getTitle());
                            }
                        }
                    }
                    ReadBean bean = JsonUtils.fromJson(response, ReadBean.class);
                    if (null != bean) {
                        mList.clear();
                        mList = bean.getBooks();
                        for(String title:collectionTitles){
                            for(int i=0 ; i<mList.size() ; i++){
                                if(title.equals(mList.get(i).getTitle())){
                                    mList.get(i).setIs_collected(1);
                                }
                            }
                        }
                        sendMessage(type, C.LOAD_FROM_NET_SUCCESS);
                    }
                }

                @Override
                public void onError(Call call, Exception e) {
                    mLoadFailNetException = e;
                    sendMessage(type, C.LOAD_FROM_NET_FAIL);
                }
            }, null);
        }
    }

    @Override
    public void load(final RequestType type) {
        if (null == urls || 0 == urls.length) return;
        if (RequestType.DATA_REQUEST_INIT==type){
            loadFromCache();
            return;
        }
       loadFromNet(type);
    }

    @Override
    public void putData() {
        db.execSQL(mHelper.DELETE_TABLE_DATA + ReadingTable.NAME + " where " + ReadingTable.CATEGORY + "=\'" + mCategory + "\'");
        for(int i=0;i<mList.size();i++){
            ReadBean.BooksBean bookBean = mList.get(i);
            values.put(ReadingTable.TITLE,bookBean.getTitle());
//            values.put(ReadingTable.INFO,bookBean.getInfo());
            values.put(ReadingTable.IMAGE, bookBean.getImage());
            values.put(ReadingTable.AUTHOR_INTRO,bookBean.getAuthor_intro() ==null ? "":bookBean.getAuthor_intro());
            values.put(ReadingTable.CATALOG,bookBean.getCatalog() == null? "":bookBean.getCatalog());
            values.put(ReadingTable.EBOOK_URL,bookBean.getEbook_url() == null?"":bookBean.getEbook_url());
            values.put(ReadingTable.CATEGORY,mCategory);
            values.put(ReadingTable.SUMMARY,bookBean.getSummary() == null?"":bookBean.getSummary());
            values.put(ReadingTable.BOOK_PAGE,bookBean.getPages());
            values.put(ReadingTable.BOOK_PUB,bookBean.getPublisher());
            values.put(ReadingTable.BOOK_AUTH,bookBean.getAuthor().get(0));
            values.put(ReadingTable.IS_COLLECTED,bookBean.getIs_collected());

            db.insert(ReadingTable.NAME,null,values);
        }
        db.execSQL(ReadingTable.SQL_INIT_COLLECTION_FLAG);
    }

    @Override
    public void putData(ReadBean.BooksBean bookBean) {
        values.put(ReadingTable.TITLE,bookBean.getTitle());
//        values.put(ReadingTable.INFO,bookBean.getInfo());
        values.put(ReadingTable.IMAGE, bookBean.getImage());
        values.put(ReadingTable.AUTHOR_INTRO,bookBean.getAuthor_intro() ==null ? "":bookBean.getAuthor_intro());
        values.put(ReadingTable.CATALOG,bookBean.getCatalog() == null? "":bookBean.getCatalog());
        values.put(ReadingTable.EBOOK_URL,bookBean.getEbook_url() == null?"":bookBean.getEbook_url());
        values.put(ReadingTable.BOOK_PAGE,bookBean.getPages());
        values.put(ReadingTable.BOOK_PUB,bookBean.getPublisher());
        values.put(ReadingTable.BOOK_AUTH,bookBean.getAuthor().get(0));
        values.put(ReadingTable.SUMMARY, bookBean.getSummary() == null ? "" : bookBean.getSummary());

        db.insert(ReadingTable.COLLECTION_NAME, null, values);
    }

    private int start = 0 - ReadApi.Offset;
    private int count = -1;//调用的次数

    protected Object getLastStartId() {
        count++;
        switch (count % ReadApi.PAGE_URL_COUNT) {
            case 0:
                return start + ReadApi.Offset;
            default:
                return start;
        }
    }
}
