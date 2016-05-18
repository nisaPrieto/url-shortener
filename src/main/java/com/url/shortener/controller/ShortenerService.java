package com.url.shortener.controller;

import java.net.MalformedURLException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.url.shortener.controller.exception.NotAShortURLException;
import com.url.shortener.controller.exception.ShortURLException;
import com.url.shortener.controller.exception.URLNotFoundException;
import com.url.shortener.dao.URLDao;
import com.url.shortener.model.URL;

@Service
public class ShortenerService {
	
	private static int MAX_LENGHT = 10;
	private static int MIN_LENGHT = 4;

	private static String PREFIX = "http://cl.ip/";
	
	private static char characters[];
	private Random random = new Random();

	@Autowired
	URLDao urlDao;
	
	static {

		StringBuilder temp = new StringBuilder();
		for (char ch = '0'; ch <= '9'; ++ch)
			temp.append(ch);
		for (char ch = 'a'; ch <= 'z'; ++ch)
			temp.append(ch);
		for (char ch = 'A'; ch <= 'Z'; ++ch)
			temp.append(ch);
		characters = temp.toString().toCharArray();

	}

	@Autowired
	public void setUrlDao(URLDao urlDao) {
		this.urlDao = urlDao;
	}

	public String generateShortURL() {
		int lenght = random.nextInt(MAX_LENGHT - MIN_LENGHT) + MIN_LENGHT;
		String url = PREFIX + getPath(lenght);
		return url;
	}

	private String getPath(int n) {
		StringBuilder temp = new StringBuilder();
		while (n-- >= 0) {
			temp.append(characters[random.nextInt(characters.length)]);
		}
		return temp.toString();
	}

	public String getShortURL(String originalURL) throws MalformedURLException,
			ShortURLException {
		validateOriginalURL(originalURL);

		String shortURL = getShortURLFromBackend(originalURL);

		return shortURL;
	}

	private String getShortURLFromBackend(String originalURL) {
		String shortURL = "";
		URL url = urlDao.findByLongURL(originalURL);
		if (null != url) {
			shortURL = url.getShorturl();
		} else {
			shortURL = generateShortURL();
			urlDao.save(shortURL, originalURL);
		}

		return shortURL;
	}

	/**
	 * Validates: 
	 * - Is valid URL 
	 * - Is not a short URL. (to avoid shorting shortURLs)
	 * 
	 * @throws MalformedURLException
	 *             , ShortURLException
	 * */
	private boolean validateOriginalURL(String url)
			throws MalformedURLException, ShortURLException {
		validateURL(url);
		if (hasShortURLFormat(url)) {
			throw new ShortURLException();
		}
		return true;
	}

	private boolean hasShortURLFormat(String url) {
		return url.startsWith(PREFIX)
				&& url.length() <= PREFIX.length() + MAX_LENGHT;
	}

	public String getOriginalURL(String shortURL) throws MalformedURLException,
			NotAShortURLException, URLNotFoundException {
		validateShortURL(shortURL);
		return getOriginalURLFromBackend(shortURL);
	}

	/**
	 * Validates: 
	 * - Is valid URL 
	 * - Is not a short URL. (to avoid shorting shortURLs)
	 * 
	 * @throws MalformedURLException
	 *             , NotAShortURLException
	 * */
	private boolean validateShortURL(String url) throws MalformedURLException,
			NotAShortURLException {
		validateURL(url);
		if (!hasShortURLFormat(url)) {
			throw new NotAShortURLException();
		}
		return true;
	}

	private boolean validateURL(String url) throws MalformedURLException {
		new java.net.URL(url);
		return true;
	}

	private String getOriginalURLFromBackend(String shortURL)
			throws URLNotFoundException {
		URL url = urlDao.findByShortURL(shortURL);
		if (null == url) {
			throw new URLNotFoundException();
		}
		return url.getLongurl();
	}
}
