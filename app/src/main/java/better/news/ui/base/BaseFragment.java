package better.news.ui.base;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import better.news.support.util.Utils;

import butterknife.ButterKnife;

/**
 * Created by Better on 2016/3/14.
 */
public abstract class BaseFragment extends Fragment {
    protected View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getArgs();
        if (null == rootView) {
            rootView = initRootView(inflater, container, savedInstanceState);
            ButterKnife.bind(this, rootView);
            initWhenNullRootView();
        }
        return rootView;
    }

    protected void initWhenNullRootView() {
    }

    protected void log(String msg) {
        Utils.v(this.getClass().getSimpleName(), msg);
    }

    /**
     * 参数传递
     */
    protected void getArgs() {
    }

    ;

    protected abstract View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
}
