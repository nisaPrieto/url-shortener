package com.url.shortener.dao;

import com.url.shortener.model.URL;

public interface URLDao {
	URL findByShortURL(String shortURL);

	URL findByLongURL(String longURL);
	
	void save(String shortURL, String longURL);

}
