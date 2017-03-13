package com.choudoufu.solr.schema.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;

import com.choudoufu.solr.util.JsonUtil;

/**
 * solr-字段
 * @author xuhaowen
 * @date 2017年3月4日
 */
public class SolrField implements Serializable{

	private static final long serialVersionUID = -218902660217120481L;
	
	/** 主键 */
	@Field
	private String id;
	
	/** 应用名  */
	@Field
	private String schemaName;
	
	/** 字段名*/
	@Field
	private String name;
	
	/** 类型*/
	private FieldType type;
	
	@Field
	private String typeJson;
	
	/** 标签*/
	@Field
	private String label;
	
	/** 是否被索引*/
	@Field
	private Boolean indexed = false;
	
	/** 是否被存储*/
	@Field
	private Boolean stored = false;
	
	/** 是否必填*/
	@Field
	private Boolean required = false;
	
	/** 是否有多个值*/
	@Field
	private Boolean multiValued = false;
	
	/** 是否使用gzip压缩（只有TextField和StrField可以压缩）*/
	@Field
	private Boolean compressed = false;
	
	/** 是否列表展示*/
	@Field
	private Boolean isListShow = false;
	
	/** 是否列表搜索*/
	@Field
	private Boolean isListSearch = false;
	
	/** 排序号*/
	@Field
	private Integer sortNo;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FieldType getType() {
		return type;
	}

	public void setType(FieldType type) {
		this.type = type;
	}
	
	public String getTypeJson() {
		return typeJson;
	}

	public void setTypeJson(String typeJson) {
		if(this.type != null){
			this.typeJson = JsonUtil.toString(type);
		}else{
			this.typeJson = typeJson;
		}
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