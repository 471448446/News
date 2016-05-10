package better.news.ui.read;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import better.news.R;
import better.news.data.read.ReadBean;
import better.news.db.cache.ReadingCache;
import better.news.db.table.ReadingTable;
import better.news.http.api.ReadApi;
import better.news.support.C;
import better.news.support.util.ImageUtil;
import better.news.ui.base.BaseDetailActivity;
import better.news.ui.base.SimpleRefreshFragment;
import better.news.ui.base.adapter.TabPagerAdapter;
import butterknife.Bind;

public class BookDetailActivity extends BaseDetailActivity {
    private ReadBean.BooksBean mBean;
    private TabPagerAdapter mAdapter;

    @Bind(R.id.read_detail_toolbar)
    Toolbar toolbar;
    @Bind(R.id.read_detail_img_book_img)
    ImageView img;
    @Bind(R.id.simpleTabSlide_tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.simpleTabSlide_Pager)
    ViewPager viewPager;

    private ReadingCache mReadingCache;

    public static void start(Activity activity, ReadBean.BooksBean bean) {
        Intent intent = new Intent(activity, BookDetailActivity.class);
        intent.putExtra(C.EXTRA_KEY, bean);
        activity.startActivity(intent);
    }

    public static void start(Fragment activity, ReadBean.BooksBean bean) {
        Intent intent = new Intent(activity.getActivity(), BookDetailActivity.class);
        intent.putExtra(C.EXTRA_KEY, bean);
        activity.startActivityForResult(intent, SimpleRefreshFragment.Req_Code);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book_detail);

    }

    @Override
    protected void initData() {
        super.initData();
//        initTabSlide(R.id.simpleTabSlide_tabLayout, R.id.simpleTabSlide_Pager);
        mContainer = findViewById(R.id.book_detail_continer);
        setBackToolBar(toolbar).setTitle(mBean.getTitle());
//        toolbar.setOnMenuItemClickListener(menuItemClickListener);
        viewPager.setAdapter(getPagerAdapter());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);//不设置gravity没有效果

        String url;
        if (null != mBean.getImages() && null != mBean.getImages().getLarge()) {
            url = mBean.getImages().getLarge();
        } else {
            url = mBean.getImage();
        }
        mReadingCache = new ReadingCache(null, null, null);
//        Glide.with(this).load(url).into(img);
        ImageUtil.load(this, url, img);

    }

    protected PagerAdapter getPagerAdapter() {
        mAdapter = new TabPagerAdapter(getSupportFragmentManager(), ReadApi.bookTab_Titles) {
            @Override
            public Fragment getItem(int position) {
                if (0 == position) return BookDetailFragment.getInstance(mBean.getSummary());
                else if (1 == position)
                    return BookDetailFragment.getInstance(mBean.getCatalog());
                else return BookDetailFragment.getInstance(mBean.getAuthor_intro());
            }
        };
        return mAdapter;
    }

    @Override
    protected boolean childrenInflateMenu(Menu menu) {
        if (null != mBean && !TextUtils.isEmpty(mBean.getEbook_url())) {
            getMenuInflater().inflate(R.menu.read_detail_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_share, menu);
        }
        return true;
    }

    @Override
    protected String getShareTitle() {
        return mBean.getTitle();
    }

    @Override
    public void onMenuSelected(MenuItem item) {
        super.onMenuSelected(item);
        if (R.id.action_read_ebook == item.getItemId()) {
            ReadBookActivity.start(BookDetailActivity.this, ReadApi.getReadEBookUrl(mBean.getEbook_url()));
        }
    }
    //    private Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
//        @Override
//        public boolean onMenuItemClick(MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.action_read_ebook:
////                    DealActivity.startDealActivity(BookDetailActivity.this, ReadApi.getReadEBookUrl(mBean.getEbook_url()));
//                    ReadBookActivity.start(BookDetailActivity.this, ReadApi.getReadEBookUrl(mBean.getEbook_url()));
//                    break;
////                case R.id.menu_collect:
////                    if (isCollected) {
////                        removeFromCollection();
////                        isCollected = false;
////                        updateCollectionMenu(item);
////                        Snackbar.make(mContiner, R.string.notify_remove_from_collection, Snackbar.LENGTH_SHORT).show();
////                    } else {
////                        addToCollection();
////                        isCollected = true;
////                        updateCollectionMenu(item);
////                        Snackbar.make(mContiner, R.string.notify_add_to_collection, Snackbar.LENGTH_SHORT).show();
////                    }
////                    break;
//            }
//            return true;
//        }
//    };

    @Override
    protected void getArgs() {
        super.getArgs();
        mBean = (ReadBean.BooksBean) getIntent().getSerializableExtra(C.EXTRA_KEY);
        isCollected = mBean.getIs_collected() == 1 ? true : false;
    }

    protected void addToCollection() {
        mReadingCache.addToCollection(mBean);
        mReadingCache.execSQL(ReadingTable.updateCollectionFlag(mBean.getTitle(), 1));
        Intent intent = new Intent();
        intent.putExtra(C.EXTRA_KEY, mBean.getTitle());
        setResult(C.read_collect_add, intent);
    }

    protected void removeFromCollection() {
        mReadingCache.execSQL(ReadingTable.updateCollectionFlag(mBean.getTitle(), 0));
        mReadingCache.execSQL(ReadingTable.deleteCollectionFlag(mBean.getTitle()));
        Intent intent = new Intent();
        intent.putExtra(C.EXTRA_KEY, mBean.getTitle());
        setResult(C.read_collect_cancle, intent);
    }
}
