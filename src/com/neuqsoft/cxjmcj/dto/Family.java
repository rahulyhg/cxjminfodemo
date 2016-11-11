/**
 *@filename FamilyDTO.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.neuqsoft.cxjmcj.dto;

import android.R.integer;

/**
 * @Title FamilyDTO
 * @author tengzj
 * @data 2016年8月24日 上午9:59:35
 */

/*
 * 家庭信息 序号 字段名称 描述 类型 长度 非空 备注 AAB999 家庭编号 Varchar2 16 为空代表新登记家庭 AAB400 户主姓名
 * Varchar2 50 √ AAC058 户主证件类型 Varchar2 3 √ 见代码表 AAE135 户主证件号码 Varchar2 20 √
 * AAB401 户籍编号 Varchar2 20 BAB041 参保人数 number 3 AAE005 联系电话 Varchar2 50 AAE006
 * 住址 Varchar2 100 AAB050 登记日期 Varchar2 10 √ 格式：yyyymmdd
 */

public class Family {
	public String id;
	public String edit_jtbh;
	public String edit_jhzzjlx;
	public String edit_gmcfzh;
	public String edit_jgszcwh;
	public String edit_hzxm;
	public String edit_hjbh;
	public String edit_lxdh;
	public String edit_dzyx;
	public String edit_yzbm;
	public String edit_cjqtbxrs;
	public String edit_hkxxdz;
	public String edit_djrq;
	public String xzqh;
	public String isEdit; // 0未修改 1修改了
	public String isUpload;// 0w未上传 1已上传

	/**
	 * @param string
	 * @param string2
	 * @param string3
	 * @param string4
	 *            /* 家庭信息 序号 字段名称 描述 类型 长度 非空 备注 AAB999 家庭编号 Varchar2 16
	 *            为空代表新登记家庭 getEdit_hzxm AAB400 户主姓名 Varchar2 50 √ AAC058 户主证件类型
	 *            Varchar2 3 √ 见代码表 getEdit_gmcfzh AAE135 户主证件号码 Varchar2 20 √
	 *            AAB401 户籍编号 Varchar2 20 getEdit_cjqtbxrs BAB041 参保人数 number 3
	 *            getEdit_lxdh AAE005 联系电话 Varchar2 50 getEdit_hkxxdz AAE006 住址
	 *            Varchar2 100 getEdit_djrq AAB050 登记日期 Varchar2 10 √
	 *            格式：yyyymmdd
	 */
	public Family(String edit_gmcfzh, String edit_jgszcwh, String edit_hzxm, String edit_hjbh, String edit_lxdh) {
		// TODO Auto-generated constructor stub
		this.edit_gmcfzh = edit_gmcfzh;
		this.edit_jgszcwh = edit_jgszcwh;
		this.edit_hzxm = edit_hzxm;
		this.edit_hjbh = edit_hjbh;
		this.edit_lxdh = edit_lxdh;
	}

	/**
	 * 
	 */
	public Family() {
		// TODO Auto-generated constructor stub
		this.edit_jhzzjlx = "";
		this.edit_gmcfzh = "";
		this.edit_jgszcwh = "";
		this.edit_hzxm = "";
		this.edit_hjbh = "";
		this.edit_lxdh = "";
		this.edit_dzyx = "";
		this.edit_yzbm = "";
		this.edit_cjqtbxrs = "0";
		this.edit_hkxxdz = "";
		this.edit_djrq = "";
		this.isEdit = "0";
		this.isUpload = "0";
	}

	/**
	 * @return the edit_gmcfzh
	 */
	public String getEdit_gmcfzh() {
		return edit_gmcfzh;
	}

	/**
	 * @param edit_gmcfzh
	 *            the edit_gmcfzh to set
	 */
	public void setEdit_gmcfzh(String edit_gmcfzh) {
		this.edit_gmcfzh = edit_gmcfzh;
	}

	/**
	 * @return the edit_jgszcwh
	 */
	public String getEdit_jgszcwh() {
		return edit_jgszcwh;
	}

	/**
	 * @param edit_jgszcwh
	 *            the edit_jgszcwh to set
	 */
	public void setEdit_jgszcwh(String edit_jgszcwh) {
		this.edit_jgszcwh = edit_jgszcwh;
	}

	/**
	 * @return the edit_hzxm
	 */
	public String getEdit_hzxm() {
		return edit_hzxm;
	}

	/**
	 * @param edit_hzxm
	 *            the edit_hzxm to set
	 */
	public void setEdit_hzxm(String edit_hzxm) {
		this.edit_hzxm = edit_hzxm;
	}

	/**
	 * @return the edit_hjbh
	 */
	public String getEdit_hjbh() {
		return edit_hjbh;
	}

	/**
	 * @param edit_hjbh
	 *            the edit_hjbh to set
	 */
	public void setEdit_hjbh(String edit_hjbh) {
		this.edit_hjbh = edit_hjbh;
	}

	/**
	 * @return the edit_lxdh
	 */
	public String getEdit_lxdh() {
		return edit_lxdh;
	}

	/**
	 * @param edit_lxdh
	 *            the edit_lxdh to set
	 */
	public void setEdit_lxdh(String edit_lxdh) {
		this.edit_lxdh = edit_lxdh;
	}

	/**
	 * @return the edit_dzyx
	 */
	public String getEdit_dzyx() {
		return edit_dzyx;
	}

	/**
	 * @param edit_dzyx
	 *            the edit_dzyx to set
	 */
	public void setEdit_dzyx(String edit_dzyx) {
		this.edit_dzyx = edit_dzyx;
	}

	/**
	 * @return the edit_yzbm
	 */
	public String getEdit_yzbm() {
		return edit_yzbm;
	}

	/**
	 * @param edit_yzbm
	 *            the edit_yzbm to set
	 */
	public void setEdit_yzbm(String edit_yzbm) {
		this.edit_yzbm = edit_yzbm;
	}

	/**
	 * @return the edit_cjqtbxrs
	 */
	public String getEdit_cjqtbxrs() {
		return edit_cjqtbxrs;
	}

	/**
	 * @param edit_cjqtbxrs
	 *            the edit_cjqtbxrs to set
	 */
	public void setEdit_cjqtbxrs(String edit_cjqtbxrs) {
		this.edit_cjqtbxrs = edit_cjqtbxrs;
	}

	/**
	 * @return the edit_hkxxdz
	 */
	public String getEdit_hkxxdz() {
		return edit_hkxxdz;
	}

	/**
	 * @param edit_hkxxdz
	 *            the edit_hkxxdz to set
	 */
	public void setEdit_hkxxdz(String edit_hkxxdz) {
		this.edit_hkxxdz = edit_hkxxdz;
	}

	@Override
	public String toString() {
		return "Family [id=" + id + ", edit_jtbh=" + edit_jtbh + ", edit_jhzzjlx=" + edit_jhzzjlx + ", edit_gmcfzh="
				+ edit_gmcfzh + ", edit_jgszcwh=" + edit_jgszcwh + ", edit_hzxm=" + edit_hzxm + ", edit_hjbh="
				+ edit_hjbh + ", edit_lxdh=" + edit_lxdh + ", edit_dzyx=" + edit_dzyx + ", edit_yzbm=" + edit_yzbm
				+ ", edit_cjqtbxrs=" + edit_cjqtbxrs + ", edit_hkxxdz=" + edit_hkxxdz + ", edit_djrq=" + edit_djrq
				+ ", isEdit=" + isEdit + ", isUpload=" + isUpload + "]";
	}

	public String getEdit_djrq() {
		return edit_djrq;
	}

	public void setEdit_djrq(String edit_djrq) {
		this.edit_djrq = edit_djrq;
	}

	public String getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}

	public String getIsUpload() {
		return isUpload;
	}

	public void setIsUpload(String isUpload) {
		this.isUpload = isUpload;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEdit_jhzzjlx() {
		return edit_jhzzjlx;
	}

	public void setEdit_jhzzjlx(String edit_jhzzjlx) {
		this.edit_jhzzjlx = edit_jhzzjlx;
	}

	public String getEdit_jtbh() {
		return edit_jtbh;
	}

	public void setEdit_jtbh(String edit_jtbh) {
		this.edit_jtbh = edit_jtbh;
	}

}
