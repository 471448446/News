package com.better.news.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.better.news.R;
import com.better.news.support.Setting;
import com.better.news.support.util.ExitAppHelper;
import com.better.news.ui.base.BaseActivity;
import com.better.news.ui.coll.ColletTabFragment;
import com.better.news.ui.days.DaysFragment;
import com.better.news.ui.news.NewsTabFragment;
import com.better.news.ui.read.ReadTabFragment;
import com.better.news.ui.science.ScienceTabFragment;
import com.better.news.ui.setting.SettingActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.toolBar)
    Toolbar toolbar;
    @Bind(R.id.drawer)
    DrawerLayout drawerLayout;
    @Bind(R.id.drawer_days)
    TextView tvDays;
    @Bind(R.id.drawer_science)
    TextView tvScience;
    @Bind(R.id.drawer_news)
    TextView tvNews;
    @Bind(R.id.drawer_read)
    TextView tvRead;
    @Bind(R.id.drawer_collect)
    TextView tvCollect;
    SparseArray<TextView> views=new SparseArray<>();

    private ExitAppHelper appExitHelper;
    private int CONTAINER = R.id.content;
    private boolean isChangeNight;
//    private int mLang = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initNightMode(savedInstanceState);
        initToolBar();
        replaceFragment(R.id.drawer_news);
        drawerLayout.closeDrawers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Setting.needRecreate) {
            Setting.needRecreate = false;
            this.recreate();
        }
    }

    @Override
    protected void initData() {
        super.initData();
        appExitHelper = new ExitAppHelper(this, drawerLayout);
        views.put(R.id.drawer_days, tvDays);
        views.put(R.id.drawer_news, tvNews);
        views.put(R.id.drawer_read, tvRead);
        views.put(R.id.drawer_science, tvScience);
        views.put(R.id.drawer_collect, tvCollect);
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        final ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_string, R.string.close_string);
        actionBarDrawerToggle.syncState();
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);//是应许的，源码是添加到一个List中去的
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                actionBarDrawerToggle.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                actionBarDrawerToggle.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                actionBarDrawerToggle.onDrawerClosed(drawerView);
                if (isChangeNight) {
                    isChangeNight = false;
                    changeNightMode();
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                actionBarDrawerToggle.onDrawerStateChanged(newState);
            }
        });
    }


    @Override
    public void onBackButtonClick(View view) {
        super.onBackButtonClick(view);
    }

    @OnClick({R.id.drawer_setting, R.id.drawer_days, R.id.drawer_news, R.id.drawer_science, R.id.drawer_read,
            R.id.drawer_night, R.id.drawer_exit, R.id.drawer_collect})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawer_setting:
                forward(SettingActivity.class);
                break;
            case R.id.drawer_night:
                drawerLayout.closeDrawers();
                isChangeNight = true;
//                changeNightMode();
                break;
            case R.id.drawer_exit:
                drawerLayout.closeDrawers();
                appExitHelper.exitDreictly();
                break;
            default:
                replaceFragment(v.getId());
                break;
        }
    }
    private void replaceFragment(int clickId) {
        toolbar.setTitle(getTitle(clickId));

        String tag = String.valueOf(clickId);
        // 执行替换
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        if (null == getSupportFragmentManager().findFragmentByTag(tag)) {
            trans.replace(CONTAINER, getFragment(clickId), tag);
            // 不存在时，添加到stack，避免切换时，先前的被清除{fm.getFragments()}
            // {存在时，不添加，避免BackStackEntry不断累加}
            trans.addToBackStack(tag);
        } else {
            trans.replace(CONTAINER, getSupportFragmentManager().findFragmentByTag(tag), tag);
        }
        trans.commitAllowingStateLoss();
        clickItem(clickId);
    }

    private void clickItem(int v) {
        tvDays.setSelected(false);
        tvScience.setSelected(false);
        tvNews.setSelected(false);
        tvRead.setSelected(false);
        tvCollect.setSelected(false);
        views.get(v).setSelected(true);
        drawerLayout.closeDrawers();
    }
    private Fragment getFragment(int clickId) {
        switch (clickId) {
            case R.id.drawer_news:
                return new NewsTabFragment();
            case R.id.drawer_days:
                return new DaysFragment();
            case R.id.drawer_science:
                return new ScienceTabFragment();
            case R.id.drawer_read:
                return new ReadTabFragment();
            case R.id.drawer_collect:
                return new ColletTabFragment();
            default:
                return null;
        }
    }

    private String getTitle(int id) {
        switch (id) {
            case R.id.drawer_news:
                return getString(R.string.str_News);
            case R.id.drawer_days:
                return getString(R.string.str_days);
            case R.id.drawer_science:
                return getString(R.string.str_science);
            case R.id.drawer_read:
                return getString(R.string.str_reading);
            case R.id.drawer_collect:
                return getString(R.string.str_collect);
            default:
                return null;

        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        drawerLayout.closeDrawers();
        if (Setting.isExitConfirm && appExitHelper.onClickBack(keyCode, event)) return true;
        return super.onKeyDown(keyCode, event);
    }
}
