package com.better.news.http.api;

/**
 * Created by Better on 2016/3/18.
 * 知乎日报 api
 */
public class DaysApi {
    public static final String latest="http://news-at.zhihu.com/api/4/stories/latest";
    public static String before="http://news-at.zhihu.com/api/4/stories/before/";/*添加日期*/
    public static String detail="http://news-at.zhihu.com/api/4/story/";
    public static String detailWebUrl="http://daily.zhihu.com/story/";
    public static String story="http://news-at.zhihu.com/api/4/story/";

    /**
     * 根据日期返回 文章列表
     * @param date
     * @return
     */
    public static String getBeforeUrl(String date){
        return before+date;
    }
    /**
     *根据文章id查看 网页详情
     * @param id
     * @return  网页链接
     */
    public static String getDeatilUrl(int id){
        return detailWebUrl+id;
    }
    public static String getStoryUrl(int id){
        return story+id;
    }
}
