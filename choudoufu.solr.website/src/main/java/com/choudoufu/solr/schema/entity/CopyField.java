package com.choudoufu.solr.schema.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 复制 字段
 * @author xuhaowen
 * @date 2017年3月13日
 */
public class CopyField implements Serializable{
	
	private static final long serialVersionUID = -9970113546469040L;
	
	/** 源字段 */
	public String source;
	/** 目标字段 */
	public String dest;
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}