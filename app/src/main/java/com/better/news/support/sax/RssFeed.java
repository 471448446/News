package com.better.news.support.sax;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Better on 2016/3/9.
 */
public class RssFeed {
    private String title;
    private String author;
    private String link;
    private String description;

    private ArrayList<RssItem> items;

    public RssFeed() {
        items = new ArrayList<>();
    }

    public void addRSSItem(RssItem item) {
        items.add(item);
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<RssItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<RssItem> items) {
        this.items = items;
    }
}
