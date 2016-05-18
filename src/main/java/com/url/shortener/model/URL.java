package com.url.shortener.model;

import java.io.Serializable;

public class URL implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String shorturl;
	private String longurl;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getShorturl() {
		return shorturl;
	}

	public void setShorturl(String shorturl) {
		this.shorturl = shorturl;
	}

	public String getLongurl() {
		return longurl;
	}

	public void setLongurl(String longurl) {
		this.longurl = longurl;
	}

	public URL(int id, String shorturl, String longurl) {
		this.id = id;
		this.shorturl = shorturl;
		this.longurl = longurl;
	}

	public URL() {
	}
}
