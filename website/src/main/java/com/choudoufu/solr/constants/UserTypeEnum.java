package com.choudoufu.solr.constants;

public enum UserTypeEnum {

	ADMIN(1, "管理员"),
	NORMAL(2, "普通用户"),
	TEMP(3, "临时用户");
	
	private int value;
	private String explain;
	
	UserTypeEnum(int value, String explain){
		this.value = value;
		this.explain = explain;
	}

	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}
}
