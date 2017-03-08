package com.choudoufu.solr.schema.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;
import org.hibernate.validator.constraints.Length;

public class SysTable implements Serializable{

	private static final long serialVersionUID = -218902660217120481L;

	public SysTable(){}
	
	public SysTable(String table){
		this.id = table;
	}
	
	/** id: 主键（表名） */
	@NotNull(message="表名不能为空")
	@Length(min=4, max=30, message="表名长度必须介于 4 和 30 之间")
	@Pattern(regexp="^\\w{4,30}$", message="表名由 字母、下划线、或数字组")
	@Field
	private String id;
	
	/** 标题*/
	@NotNull(message="标题不能为空")
	@Field
	private String title;
	
	/** 描述*/
	@NotNull(message="描述不能为空")
	@Field
	private String explain;
	
	/** 图标*/
	@Field
	private String icon;
	
	/** 字段*/
	@NotNull(message="字段不能为空")
	private SysTableField[] fields;
	
	/** 创建人*/
	@Field
	private String createBy;
	
	/** 创建时间 */
	@Field
	private Date createTime;
	
	/** 更新人*/
	@Field
	private String updateBy;
	
	/** 更新时间 */
	@Field
	private Date updateTime;

	/** 增长ID */
	@Field
	private long growthId = 0;
	
	/** 主键序号*/
	@Field
	public Integer primaryNo = 0;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public SysTableField[] getFields() {
		return fields;
	}

	public void setFields(SysTableField[] fields) {
		this.fields = fields;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public long getGrowthId() {
		return growthId;
	}

	public void setGrowthId(long growthId) {
		this.growthId = growthId;
	}
	
	public Integer getPrimaryNo() {
		return primaryNo;
	}

	public void setPrimaryNo(Integer primaryNo) {
		this.primaryNo = primaryNo;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
