package com.better.news;

import android.app.Application;

import com.better.news.support.Setting;
import com.better.news.support.util.Utils;

/**
 * Created by Better on 2016/3/13.
 */
public class MainApp extends Application {
    private static MainApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        Utils.LOG_LEVEL=Utils.VERBOSE;
        new Setting(Setting.NAME);
    }
    public static Application getInstance(){
        return instance;
    }
}
