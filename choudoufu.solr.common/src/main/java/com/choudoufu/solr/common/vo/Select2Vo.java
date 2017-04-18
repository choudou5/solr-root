package com.choudoufu.solr.common.vo;

import java.io.Serializable;

public class Select2Vo implements Serializable{

	private static final long serialVersionUID = -2353262746605076979L;

	private String code;
	
	private String name;
	
	public Select2Vo(){}
	
	public Select2Vo(String code, String name){
		this.code = code;
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
