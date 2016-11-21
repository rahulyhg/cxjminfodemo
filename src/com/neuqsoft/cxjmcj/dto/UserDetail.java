package com.neuqsoft.cxjmcj.dto;

public class UserDetail {
	public String id;
	public String taskid;
	public String cjarea;
	public String account;
	public String city;
	public String validcfcburl;
	public String sfcl;
	public String taskdesc;
	public String taskstatus;
	public String downloadflag;
	public String uploadflag;
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getCjarea() {
		return cjarea;
	}
	public void setCjarea(String cjarea) {
		this.cjarea = cjarea;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getValidcfcburl() {
		return validcfcburl;
	}
	public void setValidcfcburl(String validcfcburl) {
		this.validcfcburl = validcfcburl;
	}
	public String getSfcl() {
		return sfcl;
	}
	public void setSfcl(String sfcl) {
		this.sfcl = sfcl;
	}
	public String getTaskdesc() {
		return taskdesc;
	}
	public void setTaskdesc(String taskdesc) {
		this.taskdesc = taskdesc;
	}
	public String getTaskstatus() {
		return taskstatus;
	}
	public void setTaskstatus(String taskstatus) {
		this.taskstatus = taskstatus;
	}
	public String getDownloadflag() {
		return downloadflag;
	}
	public void setDownloadflag(String downloadflag) {
		this.downloadflag = downloadflag;
	}
	public String getUploadflag() {
		return uploadflag;
	}
	public void setUploadflag(String uploadflag) {
		this.uploadflag = uploadflag;
	}
	@Override
	public String toString() {
		return "UserDetail [taskid=" + taskid + ", cjarea=" + cjarea + ", account=" + account + ", city=" + city
				+ ", validcfcburl=" + validcfcburl + ", sfcl=" + sfcl + ", taskdesc=" + taskdesc + ", taskstatus="
				+ taskstatus + ", downloadflag=" + downloadflag + ", uploadflag=" + uploadflag + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	

}
