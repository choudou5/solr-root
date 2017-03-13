package com.choudoufu.solr.schema.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 字段类型
 * @author xuhaowen
 * @date 2017年3月13日
 */
public class FieldType implements Serializable{

	private static final long serialVersionUID = 7651390137342789295L;

	private String name;
	
	private String tokenizerClass;
	
	/** 索引 */
	private Analyzer index;
	/** 查询 */
	private Analyzer query;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTokenizerClass() {
		return tokenizerClass;
	}

	public void setTokenizerClass(String tokenizerClass) {
		this.tokenizerClass = tokenizerClass;
	}

	public Analyzer getIndex() {
		return index;
	}

	public void setIndex(Analyzer index) {
		this.index = index;
	}

	public Analyzer getQuery() {
		return query;
	}

	public void setQuery(Analyzer query) {
		this.query = query;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
