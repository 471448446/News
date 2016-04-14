package better.news.ui.coll;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;

import better.news.R;
import better.news.ui.base.SimpleTabSildeFragment;
import better.news.ui.base.adapter.TabPagerAdapter;

/**
 * Created by Better on 2016/4/9.
 */
public class ColletTabFragment extends SimpleTabSildeFragment {
    PagerAdapter pagerAdapter;
    @Override
    protected PagerAdapter getPagerAdapter() {
        String[] title={getString(R.string.str_News),getString(R.string.str_days),getString(R.string.str_science),getString(R.string.str_reading)};
        return new TabPagerAdapter(getChildFragmentManager(),title) {
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return new CollectNewsFragment();
                    case 1:
                        return new CollectDayFragment();
                    case 2:
                        return new CollectScienceFragment();
                    default:
                        return new CollectReadFragment();
                }
            }
        };
    }
}
