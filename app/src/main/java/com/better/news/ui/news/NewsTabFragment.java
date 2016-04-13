package com.better.news.ui.news;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;

import com.better.news.http.api.NewsApi;
import com.better.news.ui.base.SimpleTabSildeFragment;
import com.better.news.ui.base.adapter.TabPagerAdapter;

/**
 * Created by Better on 2016/3/15.
 */
public class NewsTabFragment extends SimpleTabSildeFragment {
    private TabPagerAdapter tabPagerAdapter;
    private String [] name ;
    private String [] url ;
    @Override
    protected PagerAdapter getPagerAdapter() {
        name = NewsApi.getNewsTitle();
        url = NewsApi.getNewsUrl();
        tabPagerAdapter=new TabPagerAdapter(getChildFragmentManager(),name) {
            @Override
            public Fragment getItem(int position) {
                return NewsFragment.newInstace(url[position],name[position]);
            }
        };
        return tabPagerAdapter;
    }

    @Override
    protected void getArgs() {

    }
}
