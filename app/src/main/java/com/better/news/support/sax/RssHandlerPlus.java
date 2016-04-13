package com.better.news.support.sax;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 解析时间，isParseRssItem=true时才开始设置数据
 */
public class RssHandlerPlus extends DefaultHandler {
    private RssItem mRssItem;
    private RssFeed mRssFeed;
    private StringBuffer item = new StringBuffer();
    private boolean isParseRssItem;
    //////////Rss 字段
    public static final String ITEM = "item";
    public static final String TITLE = "title";
    public static final String LINK = "link";
    public static final String DESCRIPTION = "description";
    public static final String AUTHOR = "author";

    /**
     * @throws SAXException
     */
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        mRssFeed = new RssFeed();
        log("startDocument()");
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        log("endDocument()");
    }

    /**
     * @param uri @param localName @param qName @param attributes @throws SAXException
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes); /*		log("localName=" + localName + "qName=" + qName);*/
        if (localName.equalsIgnoreCase(ITEM)) {
            mRssItem = new RssItem();
            isParseRssItem = true;
        } else if (localName.equalsIgnoreCase(DESCRIPTION) && isParseRssItem) {
            mRssItem.setDataFormat(item.toString());
            item=new StringBuffer();//用完就删除
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        super.endElement(uri, localName, qName);
        if (!isParseRssItem) return;
        //解析各个Item
        if (localName.equalsIgnoreCase(ITEM)) {
            mRssFeed.addRSSItem(mRssItem);
            mRssItem = null;
        } else if (localName.equalsIgnoreCase(TITLE)) {
            mRssItem.setTitleFormat(item.toString());
        } else if (localName.equalsIgnoreCase(AUTHOR)) {
            mRssItem.setAuthor(item.toString());
        } else if (localName.equalsIgnoreCase(LINK)) {
            mRssItem.setLink(item.toString());
        } else if (localName.equalsIgnoreCase(DESCRIPTION)) {
            mRssItem.setDescriptionFormat(item.toString());
        }
        item = new StringBuffer();
    }

    /**
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        if (isParseRssItem) item.append(ch, start, length);
    }

    public RssFeed getRssFeed() {
        return mRssFeed;
    }

    private void log(String msg) {
        Log.v("RSS", msg);
    }
}
