package better.news.ui.read;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import better.news.R;
import better.news.http.api.ReadApi;
import better.news.ui.base.BaseTabSlideFragment;
import better.news.ui.base.adapter.TabPagerAdapter;
import better.news.ui.widget.dialog.SearchDialog;
import butterknife.OnClick;

/**
 * Created by Better on 2016/3/23.
 */
public class ReadTabFragment extends BaseTabSlideFragment {

//    @Bind(R.id.book_search)
//    FloatingActionButton btn;

    TabPagerAdapter adapter;

    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book,null);
    }

    @Override
    protected void initWhenNullRootView() {
        super.initWhenNullRootView();
        initTabSilde(R.id.book_tabLayout, R.id.book_Pager);
    }
    @OnClick({R.id.book_search})
    public void OnClick(View v){
         new SearchDialog().showDialog(getChildFragmentManager());
    }
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
