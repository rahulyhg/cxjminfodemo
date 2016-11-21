package com.neuqsoft.cxjmcj.dto;

public class CJUrl {

	public String url; // 服务器地址

	public String name; // 服务器名称

	public String cjarea; // 行政区划

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCjarea() {
		return cjarea;
	}

	public void setCjarea(String cjarea) {
		this.cjarea = cjarea;
	}

	@Override
	public String toString() {
		return "Url [url=" + url + ", name=" + name + ", cjarea=" + cjarea + "]";
	}
}
