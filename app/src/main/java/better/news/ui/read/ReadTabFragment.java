package better.news.ui.read;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;

import better.news.http.api.ReadApi;
import better.news.ui.base.SimpleTabSildeFragment;
import better.news.ui.base.adapter.TabPagerAdapter;

/**
 * Created by Better on 2016/3/23.
 */
public class ReadTabFragment extends SimpleTabSildeFragment {
    TabPagerAdapter adapter;
    @Override
    protected PagerAdapter getPagerAdapter() {
        adapter=new TabPagerAdapter(getChildFragmentManager(), ReadApi.Tag_Titles) {
            @Override
            public Fragment getItem(int position) {
                return ReadFragment.getInstance(ReadApi.getRandomTag(ReadApi.getApiTag(position)),ReadApi.Tag_Titles[position]);
            }
        };
        return adapter;
    }
}
