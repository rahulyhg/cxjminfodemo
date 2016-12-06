package com.megvii.idcardproject.server.dto;

public class MemberDTO {
	private String lsh; // 流水号
	private String aac999; // 个人编号
	private String aac003; // 姓名
	private String aac058; // 证件类型
	private String aae135; // 公民身份
	private String aac005; // 民族
	private String aac004; // 性别
	private String aac006; // 出生日期
	private String bac067; // 参保人员
	private String aac030; // 登记日期
	private String aac069; // 与户主关
	private String aae005; // 联系电话
	private String aae006; // 住址
	private String aac009; // 户口性质
	private String hzsfz; // 户主身份证号
	private String xgbz; // 修改标志
	private String jfbz; // 缴费标志

	public String getLsh() {
		return lsh;
	}

	public void setLsh(String lsh) {
		this.lsh = lsh;
	}

	public String getAac999() {
		return aac999;
	}

	public void setAac999(String aac999) {
		this.aac999 = aac999;
	}

	public String getAac003() {
		return aac003;
	}

	public void setAac003(String aac003) {
		this.aac003 = aac003;
	}

	public String getAac058() {
		return aac058;
	}

	public void setAac058(String aac058) {
		this.aac058 = aac058;
	}

	public String getAae135() {
		return aae135;
	}

	public void setAae135(String aae135) {
		this.aae135 = aae135;
	}

	public String getAac005() {
		return aac005;
	}

	public void setAac005(String aac005) {
		this.aac005 = aac005;
	}

	public String getAac004() {
		return aac004;
	}

	public void setAac004(String aac004) {
		this.aac004 = aac004;
	}

	public String getAac006() {
		return aac006;
	}

	public void setAac006(String aac006) {
		this.aac006 = aac006;
	}

	public String getBac067() {
		return bac067;
	}

	public void setBac067(String bac067) {
		this.bac067 = bac067;
	}

	public String getAac030() {
		return aac030;
	}

	public void setAac030(String aac030) {
		this.aac030 = aac030;
	}

	public String getAac069() {
		return aac069;
	}

	public void setAac069(String aac069) {
		this.aac069 = aac069;
	}

	public String getAae005() {
		return aae005;
	}

	public void setAae005(String aae005) {
		this.aae005 = aae005;
	}

	public String getAae006() {
		return aae006;
	}

	public void setAae006(String aae006) {
		this.aae006 = aae006;
	}

	public String getAac009() {
		return aac009;
	}

	public void setAac009(String aac009) {
		this.aac009 = aac009;
	}

	public String getHzsfz() {
		return hzsfz;
	}

	public void setHzsfz(String hzsfz) {
		this.hzsfz = hzsfz;
	}

	public String getJfbz() {
		return jfbz;
	}

	public void setJfbz(String jfbz) {
		this.jfbz = jfbz;
	}

	public String getXgbz() {
		return xgbz;
	}

	public void setXgbz(String xgbz) {
		this.xgbz = xgbz;
	}

	@Override
	public String toString() {
		return "MemberDTO [lsh=" + lsh + ", aac999=" + aac999 + ", aac003=" + aac003 + ", aac058=" + aac058
				+ ", aae135=" + aae135 + ", aac005=" + aac005 + ", aac004=" + aac004 + ", aac006=" + aac006
				+ ", bac067=" + bac067 + ", aac030=" + aac030 + ", aac069=" + aac069 + ", aae005=" + aae005
				+ ", aae006=" + aae006 + ", aac009=" + aac009 + ", hzsfz=" + hzsfz + ", jfbz=" + jfbz + ", xgbz=" + xgbz
				+ "]";
	}
}
