package com.example.cxjminfodemo.server.dto;

import java.util.List;


public class FamilyMemberDTO {
	private String xzqh;    //���������������Դ���Ϊ��λ
	private String sfcl;    //�շѲ���
	private String czr;     //����Ա���ϴ���ʹ��
	
	private List<FamilyDTO> jt;  //��ͥ��Ϣ
	private List<MemberDTO> ry;  //��ͥ��Ա��Ϣ
	
	public String getXzqh() {
		return xzqh;
	}
	public void setXzqh(String xzqh) {
		this.xzqh = xzqh;
	}
	public String getSfcl() {
		return sfcl;
	}
	public void setSfcl(String sfcl) {
		this.sfcl = sfcl;
	}
	public String getCzr() {
		return czr;
	}
	public void setCzr(String czr) {
		this.czr = czr;
	}
	public List<FamilyDTO> getJt() {
		return jt;
	}
	public void setJt(List<FamilyDTO> jt) {
		this.jt = jt;
	}
	public List<MemberDTO> getRy() {
		return ry;
	}
	public void setRy(List<MemberDTO> ry) {
		this.ry = ry;
	}
	@Override
	public String toString() {
		return "FamilyMemberDTO [xzqh=" + xzqh + ", sfcl=" + sfcl + ", czr=" + czr + ", jt=" + jt + ", ry=" + ry + "]";
	}

}
