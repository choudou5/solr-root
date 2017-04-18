package com.choudoufu.solr.schema.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;
import org.hibernate.validator.constraints.Length;

/**
 * 模型
 * @author xuhaowen
 * @date 2017年3月13日
 */
public class Schema implements Serializable{

	private static final long serialVersionUID = -218902660217120481L;

	public Schema(){}
	
	public Schema(String name){
		this.name = name;
	}
	
	/** id: 主键（表名） */
	@NotNull(message="应用名不能为空")
	@Length(min=4, max=30, message="应用名长度必须介于 4 和 30 之间")
	@Pattern(regexp="^\\w{4,30}$", message="应用名由 字母、下划线、或数字组")
	@Field
	private String name;
	
	/** 标题*/
	@NotNull(message="标题不能为空")
	@Field
	private String title;
	
	/** 描述*/
	@NotNull(message="描述不能为空")
	@Field
	private String explain;
	
	/** 字段*/
	@NotNull(message="字段不能为空")
	private SolrField[] fields;
	
	private CopyField[] copyFields;
	
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
	private transient long growthId = 0;
	
	/** 主键序号*/
	@Field
	private Integer primaryNo = 0;
	
	/** 是否为系统应用 */
	@Field
	private boolean isSys = false;
	
	/** 默认搜索表达式 */
	@Field
	private String defSearchExpr;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public SolrField[] getFields() {
		return fields;
	}

	public void setFields(SolrField[] fields) {
		this.fields = fields;
	}
	
	public CopyField[] getCopyFields() {
		return copyFields;
	}

	public void setCopyFields(CopyField[] copyFields) {
		this.copyFields = copyFields;
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

	public boolean getIsSys() {
		return isSys;
	}

	public void setIsSys(boolean isSys) {
		this.isSys = isSys;
	}
	
	public String getDefSearchExpr() {
		return defSearchExpr;
	}

	public void setDefSearchExpr(String defSearchExpr) {
		this.defSearchExpr = defSearchExpr;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
