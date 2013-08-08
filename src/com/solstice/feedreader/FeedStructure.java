package com.solstice.feedreader;

/**
 * To save each entry in the feed
 * 
 * @author sampathpasupunuri
 */
public class FeedStructure {

	private String title;
	private String pubDate;
	private String author;
	private String content;
	private String imageLink;

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	// @param title set title
	public void setTitle(String title) {
		this.title = title;
	}

	// @return title
	public String getTitle() {
		return title;
	}

	// truncate title
	public String toString() {
		if (title.length() > 10) {
			return title.substring(0, 10) + "...";
		}
		return title;
	}

	// @param pubDate set pubDate
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	// @return pubDate
	public String getPubDate() {
		return pubDate;
	}

	// @param creator set creator
	public void setCreator(String creator) {
		this.author = creator;
	}

	// @return creator
	public String getCreator() {
		return author;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}