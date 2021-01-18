package com.youyu.conference.result;

public class QiniuTokenVM {
	private String key;
	private String uptoken;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public QiniuTokenVM(String key, String uptoken) {
		super();
		this.key = key;
		this.uptoken = uptoken;
	}
	public QiniuTokenVM() {
		super();
	}
	public String getUptoken() {
		return uptoken;
	}
	public void setUptoken(String uptoken) {
		this.uptoken = uptoken;
	}

	
}
