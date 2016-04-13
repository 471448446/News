package com.better.news.support.util;

import android.content.Context;
import android.content.res.Configuration;

import com.better.news.MainApp;
import com.better.news.support.C;
import com.better.news.support.Setting;
import com.better.news.support.sax.RssFeed;
import com.better.news.support.sax.RssHandlerPlus;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import better.lib.utils.BaseUtils;

/**
 * Created by Better on 2016/3/14.
 */
public class Utils extends BaseUtils {



    /**
     * 返回Rss
     * @param response
     * @return
     */
    public static RssFeed getFeed(String response){
        try {
            InputStream is=new ByteArrayInputStream(response.getBytes(Charset.forName("UTF-8")));
            RssHandlerPlus handler=new RssHandlerPlus();
            SAXParserFactory factory=SAXParserFactory.newInstance();
            SAXParser parser=factory.newSAXParser();
            XMLReader reader=parser.getXMLReader();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(is));
            return handler.getRssFeed();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String RegexFind(String regex,String string,int start,int end){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        String res = string;
        while (matcher.find()){
            res = matcher.group();
        }
        return res.substring(start, res.length() - end);
    }
    public static String RegexReplace(String regex,String string,String replace){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.replaceAll(replace);
    }
    public static String RegexFind(String regex,String string){
        return RegexFind(regex, string, 1, 1);
    }

    public static InputStream readFileFromRaw(int fileID){
        return MainApp.getInstance().getResources()
                .openRawResource(fileID);
    }

    public static Document getDocmentByIS(InputStream is){
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document doc =null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            doc = builder.parse(is);
            is.close();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc ;
    }

    /**
     * 获取随机数
     * @param size
     * @param max
     * @return
     */
    public static int[] getRandomDifferent(int size,int max){
        int[] diff=new int[size];
        int i;
        for (int j=0;j<size;j++){
            do{
                i=getRandom(0,max);
            }while (isEqual(diff,i,j));
        }
        return diff;
    }

    private static boolean isEqual(int[] diff,  int random,int currentP) {
        boolean is=false;
        for (int item:diff){
            is=item==random;
            if(is)break;
        }
        if(!is) diff[currentP]=random;
        return is;
    }

    /**
     * 获取随机数
     * @param s
     * @param e
     * @return
     */
    public static int getRandom(int s,int e){
        Random random=new Random();
        return random.nextInt(e)%(e-s+1)+s;
    }
    // Must be called before setContentView()
    public static void changeLanguage(Context context, int lang) {
        Configuration conf = context.getResources().getConfiguration();
        switch (lang) {
            case 0:
                conf.locale=Locale.SIMPLIFIED_CHINESE;
                break;
            default://1
                conf.locale=Locale.US;
                break;
        }
        context.getApplicationContext().getResources().updateConfiguration(conf, context.getResources().getDisplayMetrics());
    }
    public static int getCurrentLanguage(Context context) {
        int lang = Setting.getInt(Setting.KEY_LANGUAGE,context);
        if (lang == -1) {
            String language = Locale.getDefault().getLanguage();
            String country = Locale.getDefault().getCountry();
            if (language.equalsIgnoreCase("zh")) {
                if (country.equalsIgnoreCase("CN")) {
                    lang = 0;
                } else {//繁体
                    lang = 2;
                }
            } else {
                lang = 1;//英文
            }
        }
        Utils.v("MainActivity","getCurrentLanguage（）="+lang);
        return lang;
    }
}
