package com.example.cxjminfodemo.server.DTO;

import java.util.List;

public class FamilyMemberDTO {
	String xzqh; // ��������
	String sfcl;// �շѲ���
	List<FamilyDTO> listFamily; // ��ͥ�б�
	List<MemberDTO> listMember;// ��ͥ��Ա�б�
	ResponseDTO res;

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

	public List<FamilyDTO> getListFamily() {
		return listFamily;
	}

	public void setListFamily(List<FamilyDTO> listFamily) {
		this.listFamily = listFamily;
	}

	public List<MemberDTO> getListMember() {
		return listMember;
	}

	public void setListMember(List<MemberDTO> listMember) {
		this.listMember = listMember;
	}

	@Override
	public String toString() {
		return "FamilyMemberDTO [xzqh=" + xzqh + ", sfcl=" + sfcl + ", listFamily=" + listFamily + ", listMember="
				+ listMember + "]";
	}

	public ResponseDTO getRes() {
		return res;
	}

	public void setRes(ResponseDTO res) {
		this.res = res;
	}

}
