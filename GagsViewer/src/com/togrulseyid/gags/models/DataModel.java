package com.togrulseyid.gags.models;

public class DataModel {
	private String href;
	private String src;
	private String alt;
	private String title;
	private String text;

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "DataModel [href=" + href + ", src=" + src + ", alt=" + alt
				+ ", title=" + title + ", text=" + text + "]";
	}

}
