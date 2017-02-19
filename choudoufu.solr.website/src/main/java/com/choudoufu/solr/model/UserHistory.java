package com.choudoufu.solr.model;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.solr.client.solrj.beans.Field;

import com.choudoufu.client.model.SolrBaseModel;

public class UserHistory extends SolrBaseModel{

	private static final long serialVersionUID = -218902660217120481L;

	/** 登录名 */
	@Field
	private String loginName;
	
	/** 动作 */
	@Field
	private String action;
	
	/** ip */
	@Field
	private String ip;
	
	/** 创建时间 */
	@Field
	private Date createTime;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
