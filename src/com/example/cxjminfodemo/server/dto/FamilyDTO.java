package com.example.cxjminfodemo.server.dto;

public class FamilyDTO {
	String lsh;// 流水号
	String AAB999;// 家庭编号
	String AAB400;// 户主姓名
	String AAC058;// 户主证件类型
	String AAE135;// 户主证件号码
	String AAB401;// 户籍编号
	String BAB041;// 参保人数
	String AAE005;// 联系电话
	String AAE006;// 住址
	String AAB050;// 登记日期

	public String getLsh() {
		return lsh;
	}

	public void setLsh(String lsh) {
		this.lsh = lsh;
	}

	public String getAAB999() {
		return AAB999;
	}

	public void setAAB999(String aAB999) {
		AAB999 = aAB999;
	}

	public String getAAB400() {
		return AAB400;
	}

	public void setAAB400(String aAB400) {
		AAB400 = aAB400;
	}

	public String getAAC058() {
		return AAC058;
	}

	public void setAAC058(String aAC058) {
		AAC058 = aAC058;
	}

	public String getAAE135() {
		return AAE135;
	}

	public void setAAE135(String aAE135) {
		AAE135 = aAE135;
	}

	public String getAAB401() {
		return AAB401;
	}

	public void setAAB401(String aAB401) {
		AAB401 = aAB401;
	}

	public String getBAB041() {
		return BAB041;
	}

	public void setBAB041(String bAB041) {
		BAB041 = bAB041;
	}

	public String getAAE005() {
		return AAE005;
	}

	public void setAAE005(String aAE005) {
		AAE005 = aAE005;
	}

	public String getAAE006() {
		return AAE006;
	}

	public void setAAE006(String aAE006) {
		AAE006 = aAE006;
	}

	public String getAAB050() {
		return AAB050;
	}

	public void setAAB050(String aAB050) {
		AAB050 = aAB050;
	}

	@Override
	public String toString() {
		return "FamilyDTO [lsh=" + lsh + ", AAB999=" + AAB999 + ", AAB400=" + AAB400 + ", AAC058=" + AAC058
				+ ", AAE135=" + AAE135 + ", AAB401=" + AAB401 + ", BAB041=" + BAB041 + ", AAE005=" + AAE005
				+ ", AAE006=" + AAE006 + ", AAB050=" + AAB050 + "]";
	}

}
