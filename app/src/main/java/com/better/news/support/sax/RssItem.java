package com.better.news.support.sax;

import com.better.news.support.C;
import com.better.news.support.util.Utils;

import java.io.Serializable;

public class RssItem implements Serializable {
	private String title;
	private String author;
	private String link;
	private String description;
	private String data;

	private int is_collected = 0;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setTitleFormat(String title) {
		this.title = formatClearHtmlLabel(title);
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
	public void setDescriptionFormat(String description) {
		this.description = formatClearHtmlLabel(description);
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	public void setDataFormat(String data) {
		this.data = formatTime(data);
	}
	private String formatTime(String pubTime){
		String date = Utils.RegexFind("-.{4} ", pubTime)+"年"+
				formatMonth(Utils.RegexFind("-.{3}-", pubTime))+"月"+
				Utils.RegexFind(",.{1,2}-", pubTime)+"日"+
				Utils.RegexFind(" .{2}:", pubTime)+"点"+
				Utils.RegexFind(":.{2}:", pubTime)+"分"+
				Utils.RegexFind(":.{2} ", pubTime)+"秒";
		return date;
	}
	private String formatClearHtmlLabel(String string){
		return  this.description = Utils.RegexReplace("<[^>\n]*>",string,"");
	}

	private int formatMonth(String month){
		for(int i = 1 ; i < C.MONTH.length;i++)
			if(month.equalsIgnoreCase(C.MONTH[i]))
				return i;
		return -1;
	}


	public int getIs_collected() {
		return is_collected;
	}

	public void setIs_collected(int is_collected) {
		this.is_collected = is_collected;
	}
	@Override
	public String toString() {
		return "RssItem{" +
				"title='" + title + '\'' +
				", author='" + author + '\'' +
				", link='" + link + '\'' +
				", description='" + description + '\'' +
				", data='" + data + '\'' +
				'}';
	}
}
