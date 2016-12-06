package com.megvii.idcardproject.dto;

public class Xzqh {

	public String country; // 鏉�

	public String town; // 涔￠�?
	public String county; // 鍖哄�?
	public String city; // 甯�
	public String province; // 鐪�
	public String name; // 涔℃潙鍚嶇�?
	public String sfcl; // �?惰垂绛栫�?
	public String cjzt = "0"; // 閲囬泦鐘舵��?

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSfcl() {
		return sfcl;
	}

	public void setSfcl(String sfcl) {
		this.sfcl = sfcl;
	}

	public String getCjzt() {
		return cjzt;
	}

	public void setCjzt(String cjzt) {
		this.cjzt = cjzt;
	}

	@Override
	public String toString() {
		return "Xzqh [country=" + country + ", town=" + town + ", county=" + county + ", city=" + city + ", province="
				+ province + ", name=" + name + ", sfcl=" + sfcl + ", cjzt=" + cjzt + "]";
	}
}
