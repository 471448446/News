package better.news.support.sax;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
@Deprecated //不好解析时间
public class RssHandler extends DefaultHandler {
    private RssItem mRssItem;
    private RssFeed mRssFeed;
    private String mCurrentTag;

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
        if (localName.equals("item")) mRssItem = new RssItem();
        this.mCurrentTag = localName;
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        super.endElement(uri, localName, qName);
        if (localName.equals("item")) {
            mRssFeed.addRSSItem(mRssItem);
            mRssItem = null;
        }
        mCurrentTag = null;
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
        log("characters()==");
        if (null != mCurrentTag) {
            String item = new String(ch, start, length);
            if (null == mRssItem) return;
            if (mCurrentTag.equals("title")) {
                mRssItem.setTitle(item);
            } else if (mCurrentTag.equals("author")) {
                mRssItem.setAuthor(item);
            } else if (mCurrentTag.equals("link")) {
                mRssItem.setLink(item);
            } else if (mCurrentTag.equals("description")) {
                mRssItem.setDescription(item);
            }
        }
    }

    public RssFeed getRssFeed() {
        return mRssFeed;
    }

    private void log(String msg) {
        Log.v("RSS", msg);
    }
}
