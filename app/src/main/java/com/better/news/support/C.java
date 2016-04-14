package com.better.news.support;

/**
 * Created by Better on 2016/3/17.
 */
public interface C {
    String MONTH[] =
            {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
//    String MONTH_NUM [] =
//            {"01","02","03","04","05","06","07","08","09","10","11","12",};

    String EXTRA_URL = "url";
    String EXTRA_Id = "id";
    String EXTRA_KEY = "key";
    String EXTRA_POSITION = "position";
    String EXTRA_BEAN = "bean";
    String EXTRA_CATEGORY = "extra_category";
    String EXTRA_MODE = "mode";
    int LOAD_FORM_CACHE = 10;
    int LOAD_FROM_NET_SUCCESS = LOAD_FORM_CACHE + 1;
    int LOAD_FROM_NET_FAIL = LOAD_FROM_NET_SUCCESS + 1;
    //
    int COLLECT_YES=1;//收藏
    int COLLEXT_NO =COLLECT_YES+1;//取消收藏
    //
    int MODE_DEFAUTT=0;
    int MODE_NEWS=MODE_DEFAUTT+1;
    int MODE_DAY=MODE_NEWS+1;
    int MODE_SCIENCE=MODE_DAY+1;
    int MODE_READING=MODE_SCIENCE+1;
    //
    int news_collect_add=10;
    int news_collect_cancle=news_collect_add+1;
    int science_collect_add=news_collect_cancle+1;
    int science_collect_cancle=science_collect_add+1;
    int day_collect_add=science_collect_cancle+1;
    int day_collect_cancle=day_collect_add+1;
    int read_collect_add=day_collect_cancle+1;
    int read_collect_cancle=read_collect_add+1;
}
