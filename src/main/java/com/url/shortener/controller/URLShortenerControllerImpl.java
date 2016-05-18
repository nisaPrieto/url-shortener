package com.url.shortener.controller;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.url.shortener.controller.exception.NotAShortURLException;
import com.url.shortener.controller.exception.ShortURLException;
import com.url.shortener.controller.exception.URLNotFoundException;
import com.url.shortener.view.URLForm;
import com.url.shortener.view.ViewData;

@Controller
public class URLShortenerControllerImpl implements URLShortenerController {

	private static String INITIAL_VIEW = "urlform";
	private static String SUCCESSFUL_VIEW = "result";
	private static String ERROR_VIEW = "error";

	private static String ATTR_URLFORM = "urlform";
	private static String ATTR_ERRORMSG = "errormsg";
	private static String ATTR_SHORTURL = "shorturl";
	private static String ATTR_ORIURL = "oriurl";

	private static String ERROR_MSG_URL_MALFORMED = "That doens't looks like a real URL ;) please verify the URL provided. (Plz include the protocol as well)";
	private static String ERROR_MSG_IS_SHORT_URL = "That URL is short enough :) I can't make it any shorter ";
	private static String ERROR_MSG_URL_NOT_SHORT = "That doens't looks like one of our generated URL ;) please verify the URL provided.";
	private static String ERROR_MSG_URL_NOT_FOUND = "The Original URL can't be found :(";

	@Autowired
	ShortenerService shortener;
	
	public String shortenerForm(Model model) {
		ViewData viewData = getInitialView();
		model.addAllAttributes(viewData.getAttributes());
		return viewData.getResource();
	}

	public String shortenerSubmit(URLForm urlform, Model model) {
		ViewData viewData = getShortUrlResultView(urlform.getUrl().trim());
		model.addAllAttributes(viewData.getAttributes());
		return viewData.getResource();
	}

	public String revertSubmit(URLForm urlform, Model model) {
		ViewData viewData = getOriginalUrlResultView(urlform.getUrl().trim());
		model.addAllAttributes(viewData.getAttributes());
		return viewData.getResource();
	}

	public void setShortener(ShortenerService shortener) {
		this.shortener = shortener;
	}

	private ViewData getInitialView() {
		ViewData viewData = new ViewData();
		viewData.setResource(INITIAL_VIEW);
		viewData.addAttribute(ATTR_URLFORM, new URLForm());
		return viewData;
	}

	private ViewData getShortUrlResultView(String originalURL) {
		ViewData viewData = new ViewData();
		try {
			String shortURL = shortener.getShortURL(originalURL);
			viewData = getSuccessfullView(originalURL, shortURL);

		} catch (MalformedURLException e) {
			viewData = getErrorView(ERROR_MSG_URL_MALFORMED);
		} catch (ShortURLException e) {
			viewData = getErrorView(ERROR_MSG_IS_SHORT_URL);
		}
		return viewData;
	}

	private ViewData getOriginalUrlResultView(String shortURL) {
		ViewData viewData = new ViewData();
		try {
			String originalURL = shortener.getOriginalURL(shortURL);
			viewData = getSuccessfullView(originalURL, shortURL);

		} catch (MalformedURLException e) {
			viewData = getErrorView(ERROR_MSG_URL_MALFORMED);
		} catch (NotAShortURLException e) {
			viewData = getErrorView(ERROR_MSG_URL_NOT_SHORT);
		} catch (URLNotFoundException e) {
			viewData = getErrorView(ERROR_MSG_URL_NOT_FOUND);
		}
		return viewData;
	}

	private ViewData getSuccessfullView(String originalURL, String shortURL) {
		ViewData viewData = new ViewData();
		viewData.setResource(SUCCESSFUL_VIEW);
		viewData.addAttribute(ATTR_ORIURL, originalURL);
		viewData.addAttribute(ATTR_SHORTURL, shortURL);
		return viewData;
	}

	private ViewData getErrorView(String message) {
		ViewData viewData = new ViewData();
		viewData.setResource(ERROR_VIEW);
		viewData.addAttribute(ATTR_ERRORMSG, message);
		return viewData;
	}
}
