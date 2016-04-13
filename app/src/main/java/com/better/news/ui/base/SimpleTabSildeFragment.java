package com.better.news.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.better.news.R;

/**
 * Created by Better on 2016/3/15.
 * 提供一个简单的tabLayout+viewPager
 */
public abstract class SimpleTabSildeFragment extends BaseTabSlideFragment {
    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_simple_tab_sidle,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initTabSilde(R.id.simpleTabSlide_tabLayout,R.id.simpleTabSlide_Pager);
    }
}
