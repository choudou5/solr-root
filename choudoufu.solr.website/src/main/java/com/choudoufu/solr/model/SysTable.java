package com.choudoufu.solr.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;

public class SysTable implements Serializable{

	private static final long serialVersionUID = -218902660217120481L;

	public SysTable(){}
	
	public SysTable(String table){
		this.id = table;
	}
	
	/** id: 主键（表名） */
	@Field
	public String id;
	
	/** 增长ID */
	@Field
	private long growthId = 0;
	
	/** 更新时间 */
	@Field
	private Date updateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getGrowthId() {
		return growthId;
	}

	public void setGrowthId(long growthId) {
		this.growthId = growthId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
