/**
 *@filename FamilyDTO.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.example.cxjminfodemo.dto;

/**
 * @Title FamilyDTO
 * @author tengzj
 * @data 2016年8月24日 上午9:59:35
 */
public class FamilyDTO {

	private String edit_gmcfzh;
	private String edit_jgszcwh;
	private String edit_hzxm;
	private String edit_hjbh;
	private String edit_lxdh;
	private String edit_dzyx;
	private String edit_yzbm;
	private String edit_cjqtbxrs;
	private String edit_hkxxdz;

	/**
	 * @param string
	 * @param string2
	 * @param string3
	 * @param string4
	 */
	public FamilyDTO(String edit_gmcfzh, String edit_jgszcwh, String edit_hzxm, String edit_hjbh, String edit_lxdh) {
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
	public FamilyDTO() {
		// TODO Auto-generated constructor stub
		this.edit_gmcfzh="";
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FamilyDTO [edit_gmcfzh=" + edit_gmcfzh + ", edit_jgszcwh=" + edit_jgszcwh + ", edit_hzxm=" + edit_hzxm
				+ ", edit_hjbh=" + edit_hjbh + ", edit_lxdh=" + edit_lxdh + ", edit_dzyx=" + edit_dzyx + ", edit_yzbm="
				+ edit_yzbm + ", edit_cjqtbxrs=" + edit_cjqtbxrs + ", edit_hkxxdz=" + edit_hkxxdz + "]";
	}

}
