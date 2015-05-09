package com.togrulseyid.gags.models;

public class CoreModel {

	private Integer coreId;
	private Integer messageId;
	private String appVersion;
	private boolean ads = true;
	private boolean ads2 = true;
	private String sysLang;

	public CoreModel() {
		super();
	}

	public CoreModel(Integer coreId) {
		this.coreId = coreId;
	}

	public Integer getCoreId() {
		return coreId;
	}

	public void setCoreId(Integer coreId) {
		this.coreId = coreId;
	}

	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public boolean isAds() {
		return ads;
	}

	public void setAds(boolean ads) {
		this.ads = ads;
	}

	public boolean isAds2() {
		return ads2;
	}

	public void setAds2(boolean ads2) {
		this.ads2 = ads2;
	}

	public String getSysLang() {
		return sysLang;
	}

	public void setSysLang(String sysLang) {
		this.sysLang = sysLang;
	}

	@Override
	public String toString() {
		return "CoreModel [coreId=" + coreId + ", messageId=" + messageId
				+ ", appVersion=" + appVersion + ", ads=" + ads + ", ads2="
				+ ads2 + ", sysLang=" + sysLang + "]";
	}

}