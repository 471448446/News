/*
 *  Copyright (C) 2015 MummyDing
 *
 *  This file is part of Leisure( <https://github.com/MummyDing/Leisure> )
 *
 *  Leisure is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *                             ｀
 *  Leisure is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Leisure.  If not, see <http://www.gnu.org/licenses/>.
 */

package better.news.http.api;

import better.news.R;
import better.news.support.util.Utils;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.InputStream;

public class NewsApi {
    private static String [] newsUrl = null;
    private static String [] newsTitle = null;
    private static Document document = null;

    public static String [] getNewsUrl(){
        // Check if it has already got the address before
        // if true ,return it directly. Otherwise try to sendRequest it from file
        if(newsUrl == null){
            if(document == null) {
                // convert file to InputStream
                InputStream is = Utils.readFileFromRaw(R.raw.news_api);
                // convert document format from InputStream format
                document = Utils.getDocmentByIS(is);
            }
            // Parsing required data from document.
            NodeList urlList = document.getElementsByTagName("url");
            newsUrl = new String[urlList.getLength()];
            for(int i = 0 ; i < urlList.getLength();i++){
                newsUrl[i] = urlList.item(i).getTextContent();
            }
        }
        return newsUrl;
    }

    public static String [] getNewsTitle(){
        if(newsTitle == null){
            if(document == null) {
                InputStream is = Utils.readFileFromRaw(R.raw.news_api);
                 document = Utils.getDocmentByIS(is);
            }
            NodeList titleList = document.getElementsByTagName("name");
            newsTitle = new String[titleList.getLength()];
            for(int i = 0 ; i < titleList.getLength();i++){
                newsTitle[i] = titleList.item(i).getTextContent();
            }
        }
        return newsTitle;
    }
}
