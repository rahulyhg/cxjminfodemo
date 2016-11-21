package com.neuqsoft.cxjmcj.server.dto;

public class CodeDTO {
	public int _id;
	public String aaa100; // 代码类别
	public String aaa101; // 类别名称
	public String aaa103; // 中文含义
	public String aaa102; // 代码值

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getAAA100() {
		return aaa100;
	}

	public void setAAA100(String aAA100) {
		aaa100 = aAA100;
	}

	public String getAAA101() {
		return aaa101;
	}

	public void setAAA101(String aAA101) {
		aaa101 = aAA101;
	}

	public String getAAA103() {
		return aaa103;
	}

	public void setAAA103(String aAA103) {
		aaa103 = aAA103;
	}

	public String getAAA102() {
		return aaa102;
	}

	public void setAAA102(String aAA102) {
		aaa102 = aAA102;
	}

	@Override
	public String toString() {
		return "Code [_id=" + _id + ", AAA100=" + aaa100 + ", AAA101=" + aaa101 + ", AAA103=" + aaa103 + ", AAA102="
				+ aaa102 + "]";
	}

}
