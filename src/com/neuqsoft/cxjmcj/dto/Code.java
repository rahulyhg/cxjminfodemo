package com.neuqsoft.cxjmcj.dto;

public class Code {
	public int _id;
	public String AAA100; // 代码类别
	public String AAA101; // 类别名称
	public String AAA103; // 中文含义
	public String AAA102; // 代码值

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getAAA100() {
		return AAA100;
	}

	public void setAAA100(String aAA100) {
		AAA100 = aAA100;
	}

	public String getAAA101() {
		return AAA101;
	}

	public void setAAA101(String aAA101) {
		AAA101 = aAA101;
	}

	public String getAAA103() {
		return AAA103;
	}

	public void setAAA103(String aAA103) {
		AAA103 = aAA103;
	}

	public String getAAA102() {
		return AAA102;
	}

	public void setAAA102(String aAA102) {
		AAA102 = aAA102;
	}

	@Override
	public String toString() {
		return "Code [_id=" + _id + ", AAA100=" + AAA100 + ", AAA101=" + AAA101 + ", AAA103=" + AAA103 + ", AAA102="
				+ AAA102 + "]";
	}

}
