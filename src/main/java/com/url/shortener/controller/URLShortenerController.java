package com.url.shortener.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.url.shortener.view.URLForm;

public interface URLShortenerController {

	@RequestMapping(value = "/urlshortener", method = RequestMethod.GET)
	public String shortenerForm(Model model);

	@RequestMapping(value = "/urlshortener", method = RequestMethod.POST)
	public String shortenerSubmit(@ModelAttribute URLForm urlform, Model model);
	
	@RequestMapping(value = "/revert", method = RequestMethod.POST)
	public String revertSubmit(@ModelAttribute URLForm urlform, Model model);
}
