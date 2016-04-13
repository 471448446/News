package com.better.news.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;

import com.better.news.R;
import com.better.news.support.Setting;
import com.better.news.support.util.Utils;

import butterknife.ButterKnife;

/**
 * Created by Better on 2016/3/14.
 */
public class BaseActivity extends AppCompatActivity {
    //    private int mLang = -1;
    protected Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNightMode(savedInstanceState);
        mContext = this;
    }

    /**
     * Language
     */
    protected void changeLanguage() {
        int mLang = Utils.getCurrentLanguage(this);
        Utils.v("Better", "得到语言==" + mLang);
        if (mLang > -1) {
            log("换语言");
            Utils.changeLanguage(this, mLang);
        }
    }

    protected void changeNightMode() {
        switch (AppCompatDelegate.getDefaultNightMode()) {
            case AppCompatDelegate.MODE_NIGHT_YES:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
        Setting.setInt(Setting.KEY_NIGHT_MODE, AppCompatDelegate.getDefaultNightMode(), this);
        recreate();
    }

    protected void initNightMode(Bundle saveBindIstance) {
        if (null == saveBindIstance) {
            switch (Setting.getInt(Setting.KEY_NIGHT_MODE, this)) {
                case AppCompatDelegate.MODE_NIGHT_YES:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                default:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
            }
            recreate();
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        changeLanguage();
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        getArgs();
        initData();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
        initData();
    }

    protected void initData() {
    }

    protected Toolbar setBackToolBar(Toolbar toolBar) {
        setSupportActionBar(toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolBar.setNavigationIcon(R.drawable.icon_navgation_back_white);
        getSupportActionBar().setTitle("");
        return toolBar;
    }

    /**
     * 退出之前，如果需要额外处理，调用此方法
     *
     * @return {@link #onKeyDown(int, KeyEvent) onKeyDown}后续执行
     * </br><b>true</b>：	直接返回，停留在当前页面；
     * </br><b>false</b>：	继续执行父类操作
     * </br>{@link #onBackButtonClick(View) onBackButtonClick}后续执行
     * </br><b>true</b>：	直接返回，停留在当前页面；
     * </br><b>false</b>：	继续执行退出后续操作。
     * @see #onKeyDown(int, KeyEvent)
     * @see #onBackButtonClick(View)
     */
    protected boolean preBackExitPage() {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (preBackExitPage()) {
                return true;
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 标题返回，点击事件
     * xml文件onClick配置
     *
     * @param view
     */
    public void onBackButtonClick(View view) {
        if (preBackExitPage()) {
            return;
        }
        finish();
    }

    protected void forward(Class name) {
        startActivity(new Intent(this, name));
    }

    /**
     * 参数传递
     */
    protected void getArgs() {
    }

    ;

    protected void log(String msg) {
        Utils.v(this.getClass().getSimpleName(), msg);
    }

    protected void toast(String msg) {
        Utils.toastShort(this, msg);
    }
}
