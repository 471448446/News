package com.better.news.ui.read;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.better.news.R;
import com.better.news.data.read.ReadBean;
import com.better.news.db.cache.ReadingCache;
import com.better.news.db.table.ReadingTable;
import com.better.news.http.api.ReadApi;
import com.better.news.support.C;
import com.better.news.support.util.ImageUtil;
import com.better.news.ui.base.BaseDetailActivity;
import com.better.news.ui.base.adapter.TabPagerAdapter;

import butterknife.Bind;

public class BookDetailActivity extends BaseDetailActivity {
    private ReadBean.BooksBean mBean;
    private TabPagerAdapter mAdapter;
    private View mContainer;

    @Bind(R.id.read_detail_toolbar) Toolbar toolbar;
    @Bind(R.id.read_detail_img_book_img) ImageView img;
    @Bind(R.id.simpleTabSlide_tabLayout) TabLayout tabLayout;
    @Bind(R.id.simpleTabSlide_Pager) ViewPager viewPager;

    private ReadingCache mReadingCache;

    public static void start(Activity activity, ReadBean.BooksBean bean) {
        Intent intent = new Intent(activity, BookDetailActivity.class);
        intent.putExtra(C.EXTRA_KEY, bean);
        activity.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book_detail);
        mContainer = findViewById(R.id.book_detail_continer);
    }

    @Override
    protected void initData() {
        super.initData();
//        initTabSlide(R.id.simpleTabSlide_tabLayout, R.id.simpleTabSlide_Pager);
        setBackToolBar(toolbar).setTitle(mBean.getTitle());
        toolbar.setOnMenuItemClickListener(menuItemClickListener);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        if (null != mBean && !TextUtils.isEmpty(mBean.getEbook_url())) {
            getMenuInflater().inflate(R.menu.read_detail_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_share, menu);
        }
        updateCollectionMenu(menu.findItem(R.id.menu_collect));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected boolean childrenInflateMenu() {
        return true;
    }

    private Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_read_ebook:
//                    DealActivity.startDealActivity(BookDetailActivity.this, ReadApi.getReadEBookUrl(mBean.getEbook_url()));
                    ReadBookActivity.start(BookDetailActivity.this, ReadApi.getReadEBookUrl(mBean.getEbook_url()));
                    break;
//                case R.id.menu_collect:
//                    if (isCollected) {
//                        removeFromCollection();
//                        isCollected = false;
//                        updateCollectionMenu(item);
//                        Snackbar.make(mContainer, R.string.notify_remove_from_collection, Snackbar.LENGTH_SHORT).show();
//                    } else {
//                        addToCollection();
//                        isCollected = true;
//                        updateCollectionMenu(item);
//                        Snackbar.make(mContainer, R.string.notify_add_to_collection, Snackbar.LENGTH_SHORT).show();
//                    }
//                    break;
            }
            return true;
        }
    };

    @Override
    protected void getArgs() {
        super.getArgs();
        mBean = (ReadBean.BooksBean) getIntent().getSerializableExtra(C.EXTRA_KEY);
        isCollected = mBean.getIs_collected() == 1 ? true : false;
    }

//    protected boolean isCollected;

//    protected void updateCollectionMenu(MenuItem item) {
//        if (isCollected) {
//            item.setIcon(R.drawable.ic_star_collect_24dp);
//        } else {
//            item.setIcon(R.drawable.ic_star_normal_24dp);
//        }
//    }

    protected void addToCollection() {
        mReadingCache.addToCollection(mBean);
        mReadingCache.execSQL(ReadingTable.updateCollectionFlag(mBean.getTitle(), 1));
        Snackbar.make(mContainer, R.string.notify_remove_from_collection, Snackbar.LENGTH_SHORT).show();
    }

    protected void removeFromCollection() {
        mReadingCache.execSQL(ReadingTable.updateCollectionFlag(mBean.getTitle(), 0));
        mReadingCache.execSQL(ReadingTable.deleteCollectionFlag(mBean.getTitle()));
        Snackbar.make(mContainer, R.string.notify_add_to_collection, Snackbar.LENGTH_SHORT).show();
    }
}
