package com.choudoufu.solrj.model;

import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

import com.choudoufu.client.model.SolrBaseModel;

public class SolrTestModel extends SolrBaseModel {

	private static final long serialVersionUID = 4998589955082717576L;
	
	@Field
	private String title;
	@Field
	private String[] tag;
	@Field
	private Integer number;
	@Field
	private String type;
	@Field
	private String status;
	@Field
	private Date createTime;
	@Field
	private Date updateTime;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String[] getTag() {
		return tag;
	}
	public void setTag(String[] tag) {
		this.tag = tag;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
