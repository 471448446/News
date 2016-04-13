package com.better.news.ui.base;


import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by Better on 2016/3/15.
 * 上面一个tab，下面是可以滑动的。
 */
public abstract class BaseTabSlideFragment extends BaseFragment {
    protected TabLayout tabLayout;
    protected ViewPager viewPager;

    protected void initTabSilde(int iabId, int pagerId) {
        if (null == rootView) return;
        tabLayout = (TabLayout) rootView.findViewById(iabId);
        viewPager = (ViewPager) rootView.findViewById(pagerId);
        viewPager.setAdapter(getPagerAdapter());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);//不设置gravity没有效果
        reSetTabLayoutMode(tabLayout);
    }

    /**
     * 重写 设置TabLayout的模式
     * @param tabLayout
     */
    protected void reSetTabLayoutMode(TabLayout tabLayout) {
    }

    protected abstract PagerAdapter getPagerAdapter();
}
