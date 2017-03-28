package com.choudoufu.solr.schema.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 分析器
 * @author xuhaowen
 * @date 2017年3月13日
 */
public class Analyzer implements Serializable{

	private static final long serialVersionUID = -6205603186486014523L;
	
	private Boolean useSmart;
	
	private String code;
	
	private String separator;
	
	private String sqlGroupSymbol;
	/** 过滤器 (多个逗号隔开) */
	private String filters;

	public Boolean getUseSmart() {
		return useSmart;
	}

	public void setUseSmart(Boolean useSmart) {
		this.useSmart = useSmart;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public String getSqlGroupSymbol() {
		return sqlGroupSymbol;
	}

	public void setSqlGroupSymbol(String sqlGroupSymbol) {
		this.sqlGroupSymbol = sqlGroupSymbol;
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
