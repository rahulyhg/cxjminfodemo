package com.example.cxjmcj.server.dto;

public class FamilyDTO {
	private String lsh;  //流水号
	private String aab999;  //家庭编号    
	private String aab400;  //户主姓名    
	private String aac058;  //户主证件类型
	private String aae135;  //户主证件号码
	private String aab401;  //户籍编号    
	private String bab041;  //参保人数    
	private String aae005;  //联系电话    
	private String aae006;  //住址        
	private String aab050;  //登记日期    
	
	public String getLsh() {
		return lsh;
	}
	public void setLsh(String lsh) {
		this.lsh = lsh;
	}
	public String getAab999() {
		return aab999;
	}
	public void setAab999(String aab999) {
		this.aab999 = aab999;
	}
	public String getAab400() {
		return aab400;
	}
	public void setAab400(String aab400) {
		this.aab400 = aab400;
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
	public String getAab401() {
		return aab401;
	}
	public void setAab401(String aab401) {
		this.aab401 = aab401;
	}
	public String getBab041() {
		return bab041;
	}
	public void setBab041(String bab041) {
		this.bab041 = bab041;
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
	public String getAab050() {
		return aab050;
	}
	public void setAab050(String aab050) {
		this.aab050 = aab050;
	}
	@Override
	public String toString() {
		return "FamilyDTO [lsh=" + lsh + ", aab999=" + aab999 + ", aab400=" + aab400 + ", aac058=" + aac058
				+ ", aae135=" + aae135 + ", aab401=" + aab401 + ", bab041=" + bab041 + ", aae005=" + aae005
				+ ", aae006=" + aae006 + ", aab050=" + aab050 + "]";
	}	
}
