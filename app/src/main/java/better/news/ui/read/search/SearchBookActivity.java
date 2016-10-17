package better.news.ui.read.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.HashMap;

import better.lib.http.RequestType;
import better.news.R;
import better.news.data.read.ReadBean;
import better.news.db.Cache;
import better.news.http.HttpUtil;
import better.news.http.api.ReadApi;
import better.news.http.callback.StringCallBack;
import better.news.support.C;
import better.news.support.util.JsonUtils;
import better.news.support.util.Utils;
import better.news.ui.base.BaseListActivity;
import better.news.ui.base.adapter.BaseRecyclerViewAdapter;
import better.news.ui.read.ReadAdapter;
import butterknife.Bind;
import okhttp3.Call;

public class SearchBookActivity extends BaseListActivity<ReadBean.BooksBean> {
    @Bind(R.id.toolBar)
    Toolbar toolbar;

    private final int pageCount=30;
    public static void start(Activity activity, String bookName) {
        Intent intent = new Intent(activity, SearchBookActivity.class);
        intent.putExtra(C.EXTRA_KEY, bookName);
        activity.startActivity(intent);
    }
    private String mBookName;

    @Override
    protected void getArgs() {
        super.getArgs();
        mBookName = getIntent().getStringExtra(C.EXTRA_KEY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
    }

    @Override
    protected void initData() {
        super.initData();
        setBackToolBar(toolbar).setTitle(R.string.str_search_result);
        initRefresh(R.id.book_search_SwipeRefresh, R.id.book_search_recyclerView);
    }
    private int start = 0 - ReadApi.Offset;
    private int count = -1;//调用的次数
    @Override
    protected Object getLastStartId() {
        count++;
        switch (count % pageCount) {
            case 0:
                return start + ReadApi.Offset;
            default:
                return start;
        }
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return getLinearLayoutManagerV();
    }

    @Override
    protected BaseRecyclerViewAdapter getAdapter() {
        return new ReadAdapter(mContext);
    }

    @Override
    protected Cache<ReadBean.BooksBean> getCache() {
        return null;
    }

    @Override
    protected void asyncListInfo(final RequestType requestType) {
        HashMap<String, Object> parms = new HashMap<>();
        parms.put("count", pageCount);
        parms.put("start", getLastStartId());
        parms.put("tag", mBookName);
        HttpUtil.getRequest(ReadApi.searchUrl, parms, new StringCallBack() {
            @Override
            public void onResponse(String response) {
                ReadBean bean = JsonUtils.fromJson(response, ReadBean.class);
                if (null==bean) return;
                postRequestSuccess(requestType,bean.getBooks(),mContext.getString(R.string.books_no));
//                log(requestType+","+String.valueOf(null==bean)+"length=="+bean.getBooks().size()+","+String.valueOf(mRecyclerView.getEmptyViewProxy().getProxyView().getVisibility()== View.VISIBLE));
            }
            @Override
            public void onError(Call call, Exception e) {
                toast(Utils.getExceptionMsg(e,mContext.getString(R.string.books_no)));
                postRequestError(requestType,null, Utils.getExceptionMsg(e,mContext.getString(R.string.books_no)));
            }
        }, null);
    }
}
