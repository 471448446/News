package com.better.news.ui.science;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;

import com.better.news.http.api.ScienceApi;
import com.better.news.ui.base.SimpleTabSildeFragment;
import com.better.news.ui.base.adapter.TabPagerAdapter;

/**
 * Created by Better on 2016/3/20.
 */
public class ScienceTabFragment extends SimpleTabSildeFragment {
    private TabPagerAdapter tabPagerAdapter;
    @Override
    protected PagerAdapter getPagerAdapter() {
        tabPagerAdapter=new TabPagerAdapter(getChildFragmentManager(), ScienceApi.channel_title) {
            @Override
            public Fragment getItem(int position) {
                return ScienceFragment.newInstance(position);
            }
        };
        return tabPagerAdapter ;
    }
}
