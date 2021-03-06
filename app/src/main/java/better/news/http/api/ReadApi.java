package better.news.http.api;

import better.news.support.util.Utils;

/**
 * Created by Better on 2016/3/23.
 */
public class ReadApi {
    public static final int Offset=10;//每页条数
    public static final int PAGE_URL_COUNT=3;
    public static final String searchUrl="http://api.douban.com/v2/book/search";
    public static String readEBook="http://read.douban.com/reader/ebook";
    public static String Tag_Titles []={"综合","文学","程序员","流行","文化","生活","金融"};
    public static String HomeTag[] ={"科普", "互联网", "科学", "科技","科普","用户体验", "通信", "交互", "旅行","王小波",  "生活", "励志", "成长",  "悬疑", "武侠", "韩寒", "奇幻", "青春文学"};
    public static String LiterTag []={"小说","中国文学", "村上春树", "王小波", "余华", "鲁迅", "米兰·昆德拉", "外国文学", "经典", "童话", "儿童文学", "名著", "外国名著", "杜拉斯", "文学", "散文", "诗歌", "张爱玲", "钱钟书", "诗词", "港台", "随笔", "日本文学", "杂文", "古典文学", "当代文学", "茨威格","米兰·昆德拉","杜拉斯","港台"};
    public static String CoderTag[] ={"编程",  "交互", "设计", "算法", "web", "UE",  "互联网", "用户体验", "通信", "交互", "UCD"};
    public static String PopularTag[] ={"漫画","绘本", "推理", "青春", "言情","科幻", "东野圭吾", "悬疑", "武侠", "韩寒", "奇幻", "日本漫画", "耽美", "亦舒", " 三毛", "安妮宝贝", "网络小说", "郭敬明", "推理小说", "穿越", "金庸", "轻小说", "几米", "阿加莎·克里斯蒂","张小娴", "幾米", "魔幻", "青春文学", "J.K.罗琳", "科幻小说", "高木直子", "古龙", "沧月", "蔡康永", "落落", "张悦然"};
    public static String CultureTag[] ={"历史","心理学","哲学","传记","文化","社会学","艺术","设计","政治","社会","建筑","宗教","电影","数学","政治学","回忆录","思想","国学","中国历史","音乐","人文","戏剧","人物传记","绘画","艺术史","佛教","军事","西方哲学","近代史","二战","自由主义","考古","美术"};
    public static String LifeTag[]={"爱情","旅行", "生活", "励志", "成长", "心理", "摄影", "女性", "职场", "美食", "教育", "游记", "灵修", "情感", "健康", "手工", "养生", "两性", "人际关系", "家居", "自助游"};
    public static String FinancialTag[] ={"经济学", "管理", "经济", "金融", "商业", "投资", "营销", "创业", "理财", "广告", "股票", "企业史", "策划"};
    public static String bookTab_Titles[] ={"内容简介","目录","作者简介"};


    /**
     * 获取图书类容
     * @param pos
     * @return
     */
    public static String[] getApiTag(int pos){
        switch (pos){
            case 0:
                return HomeTag;
            case 1:
                return LiterTag;
            case 2:
                return CoderTag;
            case 3:
                return PopularTag;
            case 4:
                return CultureTag;
            case 5:
                return LifeTag;
            case 6:
                return FinancialTag;
        }
        return null;
    }
    public static String[] getRandomTag(String[] tags){
        String[] res=new String[PAGE_URL_COUNT];
        int[] differentP=Utils.getRandomDifferent(PAGE_URL_COUNT,tags.length);
        for(int i=0;i<PAGE_URL_COUNT;i++){
            res[i]=tags[differentP[i]];
        }
        return res;
    }
    public static String getReadEBookUrl(String eBookUrl){
        return readEBook+"/"+Utils.RegexFind("/[0-9]+/",eBookUrl)+"/";
    }

}
