package com.choudoufu.solr.model.plug;

import java.io.Serializable;

import org.apache.solr.client.solrj.beans.Field;

/**
 * 敏感词
 * @author xuhaowen
 * @date 2017年2月26日
 */
public class SensitiveWord implements Serializable{

	private static final long serialVersionUID = -218902660217120481L;

	/** 敏感词 */
	@Field
	private String word;
	
	public SensitiveWord(){}

	public SensitiveWord(String word){
		this.word = word;
	}
	
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	@Override
	public String toString() {
		return word;
	}
}
