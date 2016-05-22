package better.news.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

import better.lib.recyclerview.RequestType;
import better.news.MainApp;
import better.news.R;

/**
 * Created by Better on 2016/4/5.
 */
public abstract class Cache<T> {

    protected DbOpenHelper mHelper;
    protected SQLiteDatabase db;
    protected ContentValues values;

    protected String mCategory;//区分 news等
    protected String url;
    protected String[] urls;

    protected Handler mHandle;
    public List<T> mList = new ArrayList<>();

    public Exception mLoadFailNetException =new Exception(MainApp.getInstance().getString(R.string.str_loading_footer_all));

    public Cache(Handler handler) {
        this.mHandle = handler;
        mHelper = DbOpenHelper.getInstance();
    }
    public Cache(Handler handler,String category) {
        this.mHandle = handler;
        this.mCategory=category;
        mHelper = DbOpenHelper.getInstance();
    }
    public Cache(Handler handler,String category,String url) {
        this.url=url;
        this.mHandle = handler;
        this.mCategory=category;
        mHelper = DbOpenHelper.getInstance();
    }
    public Cache(Handler handler,String category,String[] url) {
        this.urls=url;
        this.mHandle = handler;
        this.mCategory=category;
        mHelper = DbOpenHelper.getInstance();
    }
    public abstract void load(RequestType type);
    public abstract void loadFromCache();
    public abstract void loadFromNet(RequestType type);

    public abstract void putData();

    public abstract void putData(T bean);

    /**
     * 缓存列表
     */
    public synchronized void cache() {
        db = mHelper.getWritableDatabase();
        db.beginTransaction();
        values = new ContentValues();
        putData();
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 保存对象到收藏列表
     *
     * @param object
     */
    public  synchronized void addToCollection(T object) {
        db = mHelper.getWritableDatabase();
        db.beginTransaction();
        values = new ContentValues();
        putData(object);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static synchronized void execSQL(String sql) {
        DbOpenHelper.getInstance().getWritableDatabase().execSQL(sql);
    }

    protected Cursor query(String sql) {
        return mHelper.getReadableDatabase().rawQuery(sql, null);
    }

    /**
     * 发送对应的消息到 列表
     *
     * @param type 列表来源
     * @param what 请求类型
     */
    protected void sendMessage(RequestType type, int what) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = type;
        mHandle.sendMessage(msg);
    }
}
