package com.choudoufu.solr.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;

/**
 * 系统-表字段
 * @author xuhaowen
 * @date 2017年3月4日
 */
public class SysTableField implements Serializable{

	private static final long serialVersionUID = -218902660217120481L;
	
	/** 主键 */
	@Field
	public String id;
	
	/** 表 */
	@Field
	public String table;
	
	/** 字段名*/
	@Field
	public String name;
	
	/** 类型*/
	@Field
	public String type;
	
	/** 标签*/
	@Field
	public String label;
	
	/** 是否被索引*/
	@Field
	public Boolean indexed;
	
	/** 是否被存储*/
	@Field
	public Boolean stored;
	
	/** 是否必填*/
	@Field
	public Boolean required;
	
	/** 是否有多个值*/
	@Field
	public Boolean multiValued;
	
	/** 是否使用gzip压缩（只有TextField和StrField可以压缩）*/
	@Field
	public Boolean compressed = false;
	
	/** 是否列表展示*/
	@Field
	public Boolean isListShow;
	
	/** 是否列表搜索*/
	@Field
	public Boolean isListSearch;
	
	/** 排序号*/
	@Field
	public Integer sortNo;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Boolean getIndexed() {
		return indexed;
	}

	public void setIndexed(Boolean indexed) {
		this.indexed = indexed;
	}

	public Boolean getStored() {
		return stored;
	}

	public void setStored(Boolean stored) {
		this.stored = stored;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Boolean getMultiValued() {
		return multiValued;
	}

	public void setMultiValued(Boolean multiValued) {
		this.multiValued = multiValued;
	}

	public Boolean getCompressed() {
		return compressed;
	}

	public void setCompressed(Boolean compressed) {
		this.compressed = compressed;
	}

	public Boolean getIsListShow() {
		return isListShow;
	}

	public void setIsListShow(Boolean isListShow) {
		this.isListShow = isListShow;
	}

	public Boolean getIsListSearch() {
		return isListSearch;
	}

	public void setIsListSearch(Boolean isListSearch) {
		this.isListSearch = isListSearch;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}