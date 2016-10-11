/**
 *@filename PersonalDTO.java
 *@Email tengzhenjiu@qq.com
 *
 */
package com.example.cxjminfodemo.dto;

import android.widget.EditText;

/**
 * @Title PersonalDTO
 * @author tengzj
 * @data 2016年8月24日 上午10:00:19
 */
public class Personal {
	private Integer id;
	
	private String edit_cbrxm;
	private String edit_gmcfzh;
	private String edit_mz;
	private String edit_xb;
	private String edit_csrq;
	private String edit_cbrq;
	private String edit_cbrylb;
	private String edit_jf;
	private String edit_yhzgx;
	private String edit_xxjzdz;
	private String edit_hkxz;
	private String HZSFZ;
	private String isEdit;//0未修改 1修改了
	private String isUpload;//0w未上传 1已上传

	public Personal() {
		// 默认未交费
		this.edit_jf = "0";
		this.isEdit=  "0";
		this.isUpload=  "0";
	}

	/**
	 * @return the edit_cbrxm
	 */
	public String getEdit_cbrxm() {
		return edit_cbrxm;
	}

	/**
	 * @param edit_cbrxm
	 *            the edit_cbrxm to set
	 */
	public void setEdit_cbrxm(String edit_cbrxm) {
		this.edit_cbrxm = edit_cbrxm;
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
	 * @return the edit_mz
	 */
	public String getEdit_mz() {
		return edit_mz;
	}

	/**
	 * @param edit_mz
	 *            the edit_mz to set
	 */
	public void setEdit_mz(String edit_mz) {
		this.edit_mz = edit_mz;
	}

	/**
	 * @return the edit_xb
	 */
	public String getEdit_xb() {
		return edit_xb;
	}

	/**
	 * @param edit_xb
	 *            the edit_xb to set
	 */
	public void setEdit_xb(String edit_xb) {
		this.edit_xb = edit_xb;
	}

	/**
	 * @return the edit_csrq
	 */
	public String getEdit_csrq() {
		return edit_csrq;
	}

	/**
	 * @param edit_csrq
	 *            the edit_csrq to set
	 */
	public void setEdit_csrq(String edit_csrq) {
		this.edit_csrq = edit_csrq;
	}

	/**
	 * @return the edit_cbrq
	 */
	public String getEdit_cbrq() {
		return edit_cbrq;
	}

	/**
	 * @param edit_cbrq
	 *            the edit_cbrq to set
	 */
	public void setEdit_cbrq(String edit_cbrq) {
		this.edit_cbrq = edit_cbrq;
	}

	/**
	 * @return the edit_cbrylb
	 */
	public String getEdit_cbrylb() {
		return edit_cbrylb;
	}

	/**
	 * @param edit_cbrylb
	 *            the edit_cbrylb to set
	 */
	public void setEdit_cbrylb(String edit_cbrylb) {
		this.edit_cbrylb = edit_cbrylb;
	}

	@Override
	public String toString() {
		return "Personal [id=" + id + ", edit_cbrxm=" + edit_cbrxm + ", edit_gmcfzh=" + edit_gmcfzh + ", edit_mz="
				+ edit_mz + ", edit_xb=" + edit_xb + ", edit_csrq=" + edit_csrq + ", edit_cbrq=" + edit_cbrq
				+ ", edit_cbrylb=" + edit_cbrylb + ", edit_jf=" + edit_jf + ", edit_yhzgx=" + edit_yhzgx
				+ ", edit_xxjzdz=" + edit_xxjzdz + ", edit_hkxz=" + edit_hkxz + ", HZSFZ=" + HZSFZ + ", isEdit="
				+ isEdit + ", isUpload=" + isUpload + "]";
	}

	/**
	 * @return the edit_jf
	 */
	public String getEdit_jf() {
		return edit_jf;
	}

	/**
	 * @param edit_jf
	 *            the edit_jf to set
	 */
	public void setEdit_jf(String edit_jf) {
		this.edit_jf = edit_jf;
	}

	public String getEdit_yhzgx() {
		return edit_yhzgx;
	}

	public void setEdit_yhzgx(String edit_yhzgx) {
		this.edit_yhzgx = edit_yhzgx;
	}

	public String getEdit_xxjzdz() {
		return edit_xxjzdz;
	}

	public void setEdit_xxjzdz(String edit_xxjzdz) {
		this.edit_xxjzdz = edit_xxjzdz;
	}

	public String getEdit_hkxz() {
		return edit_hkxz;
	}

	public void setEdit_hkxz(String edit_hkxz) {
		this.edit_hkxz = edit_hkxz;
	}

	public String getHZSFZ() {
		return HZSFZ;
	}

	public void setHZSFZ(String hZSFZ) {
		HZSFZ = hZSFZ;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
