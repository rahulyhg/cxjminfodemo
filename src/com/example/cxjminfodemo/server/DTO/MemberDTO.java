package com.example.cxjminfodemo.server.DTO;

public class MemberDTO {

	String lsh;// 流水号
	String AAC999;// 个人编号
	String AAC003;// 姓名
	String AAC058;// 证件类型
	String AAE135;// 公民身份号码
	String AAC005;// 民族
	String AAC004;// 性别
	String AAC006;// 出生日期
	String BAC067;// 参保人员类别
	String AAC030;// 登记日期
	String AAC069;// 与户主关系
	String AAE005;// 联系电话
	String AAE006;// 住址
	String AAC009;// 户口性质
	String HZSFZ;// 户主身份号码
	String JFBZ;// 缴费标志
	String XGBZ;// 修改标志

	public String getLsh() {
		return lsh;
	}

	public void setLsh(String lsh) {
		this.lsh = lsh;
	}

	public String getAAC999() {
		return AAC999;
	}

	public void setAAC999(String aAC999) {
		AAC999 = aAC999;
	}

	public String getAAC003() {
		return AAC003;
	}

	public void setAAC003(String aAC003) {
		AAC003 = aAC003;
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

	public String getAAC005() {
		return AAC005;
	}

	public void setAAC005(String aAC005) {
		AAC005 = aAC005;
	}

	public String getAAC004() {
		return AAC004;
	}

	public void setAAC004(String aAC004) {
		AAC004 = aAC004;
	}

	public String getAAC006() {
		return AAC006;
	}

	public void setAAC006(String aAC006) {
		AAC006 = aAC006;
	}

	public String getBAC067() {
		return BAC067;
	}

	public void setBAC067(String bAC067) {
		BAC067 = bAC067;
	}

	public String getAAC030() {
		return AAC030;
	}

	public void setAAC030(String aAC030) {
		AAC030 = aAC030;
	}

	public String getAAC069() {
		return AAC069;
	}

	public void setAAC069(String aAC069) {
		AAC069 = aAC069;
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

	public String getAAC009() {
		return AAC009;
	}

	public void setAAC009(String aAC009) {
		AAC009 = aAC009;
	}

	public String getHZSFZ() {
		return HZSFZ;
	}

	public void setHZSFZ(String hZSFZ) {
		HZSFZ = hZSFZ;
	}

	public String getJFBZ() {
		return JFBZ;
	}

	public void setJFBZ(String jFBZ) {
		JFBZ = jFBZ;
	}

	public String getXGBZ() {
		return XGBZ;
	}

	public void setXGBZ(String xGBZ) {
		XGBZ = xGBZ;
	}

	@Override
	public String toString() {
		return "MemberDTO [lsh=" + lsh + ", AAC999=" + AAC999 + ", AAC003=" + AAC003 + ", AAC058=" + AAC058
				+ ", AAE135=" + AAE135 + ", AAC005=" + AAC005 + ", AAC004=" + AAC004 + ", AAC006=" + AAC006
				+ ", BAC067=" + BAC067 + ", AAC030=" + AAC030 + ", AAC069=" + AAC069 + ", AAE005=" + AAE005
				+ ", AAE006=" + AAE006 + ", AAC009=" + AAC009 + ", HZSFZ=" + HZSFZ + ", JFBZ=" + JFBZ + ", XGBZ=" + XGBZ
				+ "]";
	}
}
