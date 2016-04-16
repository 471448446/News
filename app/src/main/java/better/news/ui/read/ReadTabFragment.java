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

////    @Bind(R.id.book_search)
////    FloatingActionButton btn;
//
    TabPagerAdapter adapter;

//    @Override
//    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_book,null);
//    }
//
//    @Override
//    protected void initWhenNullRootView() {
//        super.initWhenNullRootView();
//        initTabSilde(R.id.book_tabLayout, R.id.book_Pager);
//    }
//    @OnClick({R.id.book_search})
//    public void OnClick(View v){
//         new SearchDialog().showDialog(getChildFragmentManager());
//    }
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
