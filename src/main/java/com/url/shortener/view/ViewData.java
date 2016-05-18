package com.url.shortener.view;

import java.util.HashMap;
import java.util.Map;

public class ViewData {

	private String resource;
	private Map<String, Object> attributes = new HashMap<String, Object>();
	
	public String getResource() {
		return resource;
	}
	
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	public void addAttribute(String key, Object value) {
		this.attributes.put(key, value);
	}
	
}
